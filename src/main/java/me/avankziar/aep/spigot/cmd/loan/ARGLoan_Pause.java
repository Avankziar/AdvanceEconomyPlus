package main.java.me.avankziar.aep.spigot.cmd.loan;

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
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;

public class ARGLoan_Pause extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGLoan_Pause(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoLoan")));
			return;
		}
		String ids = args[1];
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(!dr.getLoanOwner().equals(player.getUniqueId().toString())
				&& !player.hasPermission(Utility.PERM_BYPASS_LOAN_PAUSE))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.NotLoanOwner")));
			return;
		}
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanAlreadyForgiven")));
			return;
		}
		if(dr.isFinished())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanAlreadyPaidOff")));
			return;
		}
		if(dr.isPaused())
		{
			dr.setPaused(false);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
			String msg = ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Pause.Unpaused")
					.replace("%name%", dr.getName())
					.replace("%player%", player.getName()));
			player.sendMessage(msg);
			OLD_AEPUser fromplayer = _AEPUserHandler_OLD.getEcoPlayer(dr.getFrom());
			boolean bungee = AEPSettings.settings.isBungee();
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
			String msg = ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Pause.Paused")
					.replace("%name%", dr.getName())
					.replace("%player%", player.getName()));
			player.sendMessage(msg);
			OLD_AEPUser fromplayer = _AEPUserHandler_OLD.getEcoPlayer(dr.getFrom());
			boolean bungee = AEPSettings.settings.isBungee();
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