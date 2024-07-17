package me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;

public class LoanRepay extends ArgumentModule
{
	private AEP plugin;
	
	public LoanRepay(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String ids = args[1];
		String amounts = args[2];
		int id = 0;
		double amount = 0;
		String confirm = "";
		if(args.length >= 4)
		{
			confirm = args[3];
		}
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!MatchApi.isDouble(amounts))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", amounts)));
			return;
		}
		amount = Double.parseDouble(amounts);
		if(!plugin.getMysqlHandler().exist(MysqlType.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlType.LOAN, "`id` = ?", id);
		if(dr.isFinished())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyPaidOff")));
			return;
		}
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyForgiven")));
			return;
		}
		if(!confirm.equalsIgnoreCase(plugin.getYamlHandler().getLang().getString("Cmd.Loan.ConfirmTerm")))
		{
			
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.Accept.PleaseConfirm")
					.replace("%repaycmd%", CommandSuggest.get(null, CommandExecuteType.LOAN_REPAY).trim()+" %id% %amount% "
							.replace("%id%", ids)
							.replace("%amount%", amounts)
							+" "+plugin.getYamlHandler().getLang().getString("Cmd.Loan.ConfirmTerm"))));
			return;
		}
		double dif = dr.getTotalAmount()-dr.getAmountPaidSoFar();
		double recieved = 0;
		if(dif <= amount)
		{
			dr.addPayment(dif);
			dr.setFinished(true);
			recieved = dif;
			plugin.getMysqlHandler().updateData(MysqlType.LOAN, dr, "`id` = ?", dr.getId());
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Repay.RepayMoreThanNeeded")
					.replace("%name%", dr.getName())
					.replace("%currency%", AEP.getVault().currencyNamePlural())
					.replace("%amount%", amounts)
					.replace("%dif%", String.valueOf(AEP.getVault().format(dif)))));
		} else
		{
			dr.addPayment(amount);
			recieved = amount;
			plugin.getMysqlHandler().updateData(MysqlType.LOAN, dr, "`id` = ?", dr.getId());
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Repay.RepayedAmount")
					.replace("%name%", dr.getName())
					.replace("%currency%", AEP.getVault().currencyNamePlural())
					.replace("%amount%", amounts)));
		}
		String message = plugin.getYamlHandler().getLang().getString("Cmd.Loan.Repay.RepayRecieved")
				.replace("%name%", dr.getName())
				.replace("%currency%", AEP.getVault().currencyNamePlural())
				.replace("%amount%", String.valueOf(AEP.getVault().format(recieved)));
		if(Bukkit.getPlayer(dr.getOwner()) == null)
		{
			//BungeeBridge.sendBungeeMessage(player, dr.getTo(), message, false, "");
		} else
		{
			Bukkit.getPlayer(dr.getOwner()).sendMessage(message);
		}
		return;
	}
}