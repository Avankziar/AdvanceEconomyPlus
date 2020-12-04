package main.java.me.avankziar.aep.spigot.cmd.money.action;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler.Methode;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;

public class ARGMoneyActionLog extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyActionLog(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		int page = 0;
		String playername = null;
		if(args.length >= 2)
		{
			String pagenumber = args[1];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length >= 3)
		{
			if(args[2].equals(playername))
			{
				playername = args[2];
			} else
			{
				if(!player.hasPermission(Utility.PERM_CMD_MONEY_LOG_OTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				playername = args[2];
			}
		}
		LoggerSettingsHandler fsth = new LoggerSettingsHandler(plugin);
		LoggerSettings fst = LoggerSettingsHandler.getLoggerSettings().get(player.getUniqueId());
		if(fst == null)
		{
			fst = new LoggerSettings(player.getUniqueId(), null, page);
			fst.setAction(true);
		}
		if(playername != null)
		{
			AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
			if(eco != null)
			{
				fst.getActionFilter().setFrom(eco.getUUID());
				fst.getActionFilter().setTo(eco.getUUID());
			}
		}
		if(LoggerSettingsHandler.getLoggerSettings().containsKey(player.getUniqueId()))
		{
			LoggerSettingsHandler.getLoggerSettings().replace(player.getUniqueId(), fst);
		} else
		{
			LoggerSettingsHandler.getLoggerSettings().put(player.getUniqueId(), fst);
		}
		fsth.forwardingToOutput(player, fst, true, Methode.LOG, page);
		return;
	}
}