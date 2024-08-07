package me.avankziar.aep.spigot.cmd.loan;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.general.objects.TaxationCase;
import me.avankziar.aep.general.objects.TaxationSet;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.api.economy.CurrencyHandler;
import me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import me.avankziar.aep.spigot.handler.PendingHandler;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class LoanAccept extends ArgumentModule
{
	private AEP plugin;
	
	public LoanAccept(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!PendingHandler.loanToAccept.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.NoToAcceptLoan")));
			return;
		}
		String confirm = "";
		if(args.length >= 2)
		{
			confirm = args[1];
		}
		if(!confirm.equalsIgnoreCase(plugin.getYamlHandler().getLang().getString("Cmd.Loan.ConfirmTerm")))
		{
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.PleaseConfirm")
					.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.LOAN_ACCEPT).trim().replace(" ", "+")
							+"+"+plugin.getYamlHandler().getLang().getString("Cmd.Loan.ConfirmTerm"))));
			return;
		}
		LoanRepayment lr = PendingHandler.loanToAccept.get(player.getUniqueId().toString());
		AEPUser lender = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlType.PLAYERDATA, "`player_uuid` = ?", lr.getOwner().toString());
		if(lender == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		AEPUser debtor = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlType.PLAYERDATA, "`player_uuid` = ?", lr.getDebtor().toString());
		if(debtor == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		String category = plugin.getYamlHandler().getLang().getString("LoanRepayment.CategoryI", null);
		String comment = plugin.getYamlHandler().getLang().getString("LoanRepayment.CommentI", null);
		
		Account from = plugin.getIFHApi().getAccount(lr.getAccountFromID());
		Account to = plugin.getIFHApi().getAccount(lr.getAccountToID());
		Account tax = plugin.getIFHApi().getDefaultAccount(to.getOwner().getUUID(), AccountCategory.TAX, from.getCurrency());
		if(comment != null)
		{
			comment = comment.replace("%name%", lr.getName())
					.replace("%lender%", lender.getName())
					.replace("%debtor%", player.getName())
					.replace("%owner%", to.getOwner().getName())
					.replace("%accountname%", to.getAccountName())
					.replace("%id%", String.valueOf(to.getID()));
		}
		double amount = lr.getLoanAmount();
		LinkedHashMap<TaxationCase, TaxationSet> map = CurrencyHandler.taxationMap.get(from.getCurrency().getUniqueName());
		TaxationSet ts = map.containsKey(TaxationCase.LOANLENDING) ? map.get(TaxationCase.LOANLENDING) : null;
		double taxation = ts != null ? ts.getTaxInPercent() : 0.0;
		boolean taxAreExclusive = ts != null ? ts.isTaxAreExclusive() : true;
		EconomyAction ea = null;
		if(from.getCurrency().getUniqueName().equals(to.getCurrency().getUniqueName()))
		{
			if(tax == null && category == null)
			{
				ea = plugin.getIFHApi().transaction(from, to, amount);
			} else if(tax == null && category != null)
			{
				ea = plugin.getIFHApi().transaction(from, to, amount, OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
			} else if(tax != null && category == null)
			{
				ea = plugin.getIFHApi().transaction(from, to, amount, taxation, taxAreExclusive, tax);
			} else if(tax != null && category != null)
			{
				ea = plugin.getIFHApi().transaction(from, to, amount, taxation, taxAreExclusive, tax, 
						OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
			}
		} else
		{
			Account taxII = plugin.getIFHApi().getDefaultAccount(from.getOwner().getUUID(), AccountCategory.TAX, from.getCurrency());
			if(tax == null && taxII == null && category != null)
			{
				ea = plugin.getIFHApi().exchangeCurrencies(
						from, to, amount,
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			} else if(tax != null && taxII == null && category != null)
			{
				ea = plugin.getIFHApi().exchangeCurrencies(
						from, to, amount,
						taxation, taxAreExclusive, taxII, tax,
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			}
		}
		if(!ea.isSuccess())
		{
			player.sendMessage(ChatApiOld.tl(ea.getDefaultErrorMessage()));
			return;
		}
		
		plugin.getMysqlHandler().create(MysqlType.LOAN, lr);

		player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Accept.YouHaveAccepted")
				.replace("%drowner%", lender.getName())
				.replace("%name%", lr.getName())));
		
		String tomsg = ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Accept.PayerHasAccepted")
				.replace("%toplayer%", lender.getName())
				.replace("%name%", lr.getName())
				.replace("%player%", player.getName()));
		if((to.getType() == AccountType.WALLET && lender.isWalletMoneyFlowNotification()
				|| to.getType() == AccountType.BANK && lender.isBankMoneyFlowNotification()))
		{
			if(Bukkit.getPlayer(lender.getUUID()) == null)
			{
				//BungeeBridge.sendBungeeMessage(player, toplayer.getUUID(), tomsg, false, "");
			} else
			{
				Bukkit.getPlayer(lender.getUUID()).sendMessage(tomsg);
			}
		}
		AEPUser otherowner = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlType.PLAYERDATA, "`player_uuid` = ?", to.getOwner().getUUID().toString());
		if(otherowner != null)
		{
			if(!to.getOwner().getUUID().toString().equals(lr.getOwner().toString()))
			{
				if((to.getType() == AccountType.WALLET && lender.isWalletMoneyFlowNotification()
						|| to.getType() == AccountType.BANK && lender.isBankMoneyFlowNotification()))
				{
					if(Bukkit.getPlayer(to.getOwner().getUUID()) == null)
					{
						//BungeeBridge.sendBungeeMessage(player, Loanowner.getUUID(), tomsg, false, "");
					} else
					{
						Bukkit.getPlayer(to.getOwner().getUUID()).sendMessage(tomsg);
					}
				}
			}
		}
		PendingHandler.loanRepayment.remove(lr.getOwner().toString());
		PendingHandler.loanToAccept.remove(player.getUniqueId().toString());
		return;
	}
}