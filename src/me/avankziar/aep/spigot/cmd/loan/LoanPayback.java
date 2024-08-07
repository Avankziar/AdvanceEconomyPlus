package me.avankziar.aep.spigot.cmd.loan;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.Utility;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class LoanPayback extends ArgumentModule
{
	private AEP plugin;
	
	public LoanPayback(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String ids = args[1];
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!plugin.getMysqlHandler().exist(MysqlType.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment lr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlType.LOAN, "`id` = ?", id);
		if(lr.isForgiven())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyForgiven")));
			return;
		}
		if(lr.isFinished())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyPaidOff")));
			return;
		}
		double payback = lr.getTotalAmount() - lr.getAmountPaidSoFar();
		if(payback <= 0)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Payback.IsAlreadyPaidOff")));
			return;
		}
		Account from = plugin.getIFHApi().getAccount(lr.getAccountFromID());
		Account to = plugin.getIFHApi().getAccount(lr.getAccountToID());
		if(from == null || to == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.AccountDontExist")
					.replace("%account%", from == null ? String.valueOf(lr.getAccountFromID()) : String.valueOf(lr.getAccountToID()))));
			return;
		}
		String category = plugin.getYamlHandler().getLang().getString("LoanRepayment.CategoryII", null);
		String comment = plugin.getYamlHandler().getLang().getString("LoanRepayment.CommentIII", null);
		if(comment != null)
		{
			comment = comment
					.replace("%name%", lr.getName())
					.replace("%payback%", plugin.getIFHApi().format(payback, from.getCurrency()));
		}
		Account tax = plugin.getIFHApi().getDefaultAccount(to.getOwner().getUUID(), AccountCategory.TAX, to.getCurrency());
		double taxation = lr.getTaxInDecimal();
		boolean taxAreExclusive = (lr.getLoanAmount()+lr.getLoanAmount()*lr.getInterest()+lr.getLoanAmount()*taxation) > lr.getTotalAmount() ? true : false;
		
		EconomyAction ea = null;
		if(from.getCurrency().getUniqueName().equals(to.getCurrency().getUniqueName()))
		{
			if(tax == null && category != null)
			{
				ea = plugin.getIFHApi().transaction(
						from, to, payback,
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			} else if(tax != null && category != null)
			{
				ea = plugin.getIFHApi().transaction(
						from, to, payback,
						taxation, taxAreExclusive, tax, 
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			}
			if(!ea.isSuccess())
			{
				
				return;
			}
			plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", lr.getId());
		} else
		{
			if(!from.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", from.getCurrency().getUniqueName())));
				return;
			}
			if(!to.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", to.getCurrency().getUniqueName())));
				return;
			}
			Account taxII = plugin.getIFHApi().getDefaultAccount(to.getOwner().getUUID(), AccountCategory.TAX, to.getCurrency());
			if(tax == null && taxII == null && category != null)
			{
				ea = plugin.getIFHApi().exchangeCurrencies(
						from, to, payback,
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			} else if(tax != null && taxII == null && category != null)
			{
				ea = plugin.getIFHApi().exchangeCurrencies(
						from, to, payback,
						taxation, taxAreExclusive, tax, taxII,
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			}
			plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", lr.getId());
		}
		lr.setLastTime(System.currentTimeMillis());
		lr.setAmountPaidSoFar(lr.getTotalAmount());
		lr.setFinished(true);
		String othername = Utility.convertUUIDToName(lr.getOwner().toString(), EconomyType.PLAYER);
		if(othername == null)
		{
			othername = "/";
		}
		String message = ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Payback.IsPayedBack")
				.replace("%player%", player.getName())
				.replace("%to%", othername)
				.replace("%name%", lr.getName())
				.replace("%id%", String.valueOf(id)));
		player.sendMessage(message);
		if(Bukkit.getPlayer(lr.getOwner()) != null)
		{
			Bukkit.getPlayer(lr.getOwner()).sendMessage(message);
		} else
		{
			//BungeeBridge.sendBungeeMessage(player, lr.getTo(), message, false, "");
		}
		plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", id);
		return;
	}
}