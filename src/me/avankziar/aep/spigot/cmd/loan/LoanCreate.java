package me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.ConfigHandler;
import me.avankziar.aep.spigot.handler.PendingHandler;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class LoanCreate extends ArgumentModule
{
	private AEP plugin;
	
	public LoanCreate(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String name = args[1];
		int fromid = -1;
		int toid = -1;
		String dt = args[4];
		if(!MatchApi.isInteger(args[2]))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[2])));
			return;
		}
		fromid = Integer.parseInt(args[2]);
		if(!MatchApi.isInteger(args[3]))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[3])));
			return;
		}
		toid = Integer.parseInt(args[3]);
		AEPUser debtor = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlType.PLAYERDATA, "`player_name` = ?", dt);
		if(debtor == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		Account fac = plugin.getIFHApi().getAccount(fromid);
		Account tac = plugin.getIFHApi().getAccount(toid);
		if(fac == null || tac == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.AccountDontExist")
					.replace("%account%", fac == null ? args[2] : args[3])));
			return;
		}
		if(fac.getCurrency() == null)
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", fac.getAccountName()));
			return;
		}
		if(tac.getCurrency() == null)
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", tac.getAccountName()));
			return;
		}
		if(!ConfigHandler.isLoanEnabled(fac.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.CurrencyDontAllowLoan")
					.replace("%currency%", fac.getCurrency().getUniqueName())));
			return;
		}
		if(!ConfigHandler.isLoanEnabled(tac.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.CurrencyDontAllowLoan")
					.replace("%currency%", tac.getCurrency().getUniqueName())));
			return;
		}
		if(!fac.getCurrency().getUniqueName().equals(tac.getCurrency().getUniqueName()))
		{
			if(!fac.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", fac.getCurrency().getUniqueName())));
				return;
			}
			if(!tac.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", tac.getCurrency().getUniqueName())));
				return;
			}
		}
		if(!plugin.getIFHApi().canManageAccount(tac, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.YouCantBeCreateALoanIfYouHaveNoWithdrawRight")));
			return;
		}
		boolean mustbeowner = plugin.getYamlHandler().getConfig().getBoolean("Loan.ToLendingALoanPlayerMustBeTheOwnerOfTheAccount", true);
		if(mustbeowner)
		{
			if(!tac.getOwner().getUUID().toString().equals(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApiOld.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Loan.YouCantBeCreateALoanIfYouHaveNoWithdrawRight")));
				return;
			}
		}
		/*if(fac.getOwner().getUUID().toString().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.YouCantBeTheOwnerOfYourOwnLoan")));
			return;
		}
		if(!debtor.getUUID().toString().equals(player.getUniqueId().toString()) && !player.hasPermission(ExtraPerm.get(Type.BYPASS_LOAN)))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.YouCantBeTheOwnerOfYourOwnLoan")));
			return;
		} */ // FIXME Wieder einbauen. nur für test ausgebaut
		if(PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
		{
			LoanRepayment dr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
			dr.setName(name);
			dr.setAccountFromID(fromid);
			dr.setAccountToID(toid);
		} else
		{
			LoanRepayment dr = new LoanRepayment(0, name, fromid, toid, player.getUniqueId(), debtor.getUUID(),
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false);
			PendingHandler.loanRepayment.put(player.getUniqueId().toString(), dr);
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Create.isCreate")));
		}
		return;
	}
}
