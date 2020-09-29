package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class ARGMoneyLoan_Pause extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Pause(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(!dr.getLoanOwner().equals(player.getUniqueId().toString())
				&& !player.hasPermission(Utility.PERM_BYPASS_LOAN_PAUSE))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.NotLoanOwner")));
			return;
		}
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanAlreadyForgiven")));
			return;
		}
		if(dr.isFinished())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanAlreadyPaidOff")));
			return;
		}
		if(dr.isPaused())
		{
			dr.setPaused(false);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
			String msg = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Pause.Unpaused")
					.replace("%name%", dr.getName())
					.replace("%player%", player.getName()));
			player.sendMessage(msg);
			EcoPlayer fromplayer = EcoPlayerHandler.getEcoPlayer(dr.getFrom());
			boolean bungee = EconomySettings.settings.isBungee();
			if(fromplayer.isMoneyPlayerFlow())
			{
				if(bungee)
				{
					BungeeBridge.sendBungeeMessage(player, fromplayer.getUUID(), msg, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())).sendMessage(msg);
					}
				}
			}
		} else
		{
			dr.setPaused(true);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
			String msg = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Pause.Paused")
					.replace("%name%", dr.getName())
					.replace("%player%", player.getName()));
			player.sendMessage(msg);
			EcoPlayer fromplayer = EcoPlayerHandler.getEcoPlayer(dr.getFrom());
			boolean bungee = EconomySettings.settings.isBungee();
			if(fromplayer.isMoneyPlayerFlow())
			{
				if(bungee)
				{
					BungeeBridge.sendBungeeMessage(player, fromplayer.getUUID(), msg, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())).sendMessage(msg);
					}
				}
			}
		}
		return;
	}
}