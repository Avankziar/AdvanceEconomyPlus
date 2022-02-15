package main.java.me.avankziar.aep.spigot.cmd.loan;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;

public class LoanRepay extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public LoanRepay(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
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
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!MatchApi.isDouble(amounts))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", amounts)));
			return;
		}
		amount = Double.parseDouble(amounts);
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(dr.isFinished())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanAlreadyPaidOff")));
			return;
		}
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanAlreadyForgiven")));
			return;
		}
		if(!dr.getFrom().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Repay.IsNotYourLoan")));
		}
		if(!confirm.equalsIgnoreCase(plugin.getYamlHandler().getLang().getString("CmdLoan.ConfirmTerm")))
		{
			
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdLoan.Accept.PleaseConfirm")
					.replace("%repaycmd%", CommandSuggest.get(null, CommandExecuteType.LOAN_REPAY).trim()+" %id% %amount% "
							.replace("%id%", ids)
							.replace("%amount%", amounts)
							+" "+plugin.getYamlHandler().getLang().getString("CmdLoan.ConfirmTerm"))));
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Repay.RepayMoreThanNeeded")
					.replace("%name%", dr.getName())
					.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
					.replace("%amount%", amounts)
					.replace("%dif%", String.valueOf(AdvancedEconomyPlus.getVault().format(dif)))));
		} else
		{
			dr.addPayment(amount);
			recieved = amount;
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Repay.RepayedAmount")
					.replace("%name%", dr.getName())
					.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
					.replace("%amount%", amounts)));
		}
		String message = plugin.getYamlHandler().getLang().getString("CmdLoan.Repay.RepayRecieved")
				.replace("%name%", dr.getName())
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%amount%", String.valueOf(AdvancedEconomyPlus.getVault().format(recieved)));
		if(Bukkit.getPlayer(UUID.fromString(dr.getTo())) == null)
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