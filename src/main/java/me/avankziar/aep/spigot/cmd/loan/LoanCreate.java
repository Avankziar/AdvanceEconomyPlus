package main.java.me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.general.objects.LoanRepayment;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

public class LoanCreate extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public LoanCreate(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
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
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[2])));
			return;
		}
		fromid = Integer.parseInt(args[2]);
		if(!MatchApi.isInteger(args[3]))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[3])));
			return;
		}
		toid = Integer.parseInt(args[3]);
		AEPUser debtor = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", dt);
		if(debtor == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		Account fac = plugin.getIFHApi().getAccount(fromid);
		Account tac = plugin.getIFHApi().getAccount(toid);
		if(fac == null || tac == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.AccountDontExist")
					.replace("%account%", fac == null ? args[2] : args[3])));
			return;
		}
		if(!ConfigHandler.isLoanEnabled(fac.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.CurrencyDontAllowLoan")
					.replace("%currency%", fac.getCurrency().getUniqueName())));
			return;
		}
		if(!ConfigHandler.isLoanEnabled(tac.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.CurrencyDontAllowLoan")
					.replace("%currency%", tac.getCurrency().getUniqueName())));
			return;
		}
		if(!fac.getCurrency().getUniqueName().equals(tac.getCurrency().getUniqueName()))
		{
			if(!fac.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", fac.getCurrency().getUniqueName())));
				return;
			}
			if(!tac.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", tac.getCurrency().getUniqueName())));
				return;
			}
		}
		if(!plugin.getIFHApi().canManageAccount(tac, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.YouCantBeCreateALoanIfYouHaveNoWithdrawRight")));
			return;
		}
		boolean mustbeowner = plugin.getYamlHandler().getConfig().getBoolean("Loan.ToLendingALoanPlayerMustBeTheOwnerOfTheAccount", true);
		if(mustbeowner)
		{
			if(tac.getOwner().getUUID().toString().equals(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Loan.YouCantBeCreateALoanIfYouHaveNoWithdrawRight")));
				return;
			}
		}
		if(fac.getOwner().getUUID().toString().equals(player.getUniqueId().toString()))
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
		}
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
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Create.isCreate"));
		}
		return;
	}
}
