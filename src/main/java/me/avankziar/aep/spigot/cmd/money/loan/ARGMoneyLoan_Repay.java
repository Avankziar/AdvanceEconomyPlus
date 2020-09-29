package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class ARGMoneyLoan_Repay extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Repay(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		String ids = args[2];
		String amounts = args[3];
		int id = 0;
		double amount = 0;
		String confirm = "";
		if(args.length >= 5)
		{
			confirm = args[4];
		}
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!MatchApi.isDouble(amounts))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", amounts)));
			return;
		}
		amount = Double.parseDouble(amounts);
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(dr.isFinished())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanAlreadyPaidOff")));
			return;
		}
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanAlreadyForgiven")));
			return;
		}
		if(!dr.getFrom().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Repay.IsNotYourLoan")));
		}
		if(!confirm.equalsIgnoreCase(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.ConfirmTerm")))
		{
			
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Accept.PleaseConfirm")
					.replace("%cmd%", plugin.getYamlHandler().getL().getString("CmdMoney.Loan.RepayCmd")
							.replace("%id%", ids)
							.replace("%amount%", amounts))));
			return;
		}
		double dif = dr.getTotalAmount()-dr.getAmountPaidSoFar();
		double recieved = 0;
		if(dif <= amount)
		{
			dr.addPayment(dif);
			dr.setFinished(true);
			recieved = dif;
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Repay.RepayMoreThanNeeded")
					.replace("%name%", dr.getName())
					.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
					.replace("%amount%", amounts)
					.replace("%dif%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dif)))));
		} else
		{
			dr.addPayment(amount);
			recieved = amount;
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Repay.RepayedAmount")
					.replace("%name%", dr.getName())
					.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
					.replace("%amount%", amounts)));
		}
		String message = plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Repay.RepayRecieved")
				.replace("%name%", dr.getName())
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%amount%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(recieved)));
		if(EconomySettings.settings.isBungee())
		{
			BungeeBridge.sendBungeeMessage(player, dr.getTo(), message, false, "");
		} else
		{
			if(Bukkit.getPlayer(UUID.fromString(dr.getTo())) != null)
			{
				Bukkit.getPlayer(UUID.fromString(dr.getTo())).sendMessage(message);
			}
		}
		return;
	}
}