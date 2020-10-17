package main.java.me.avankziar.aep.spigot.cmd.money.statistics;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.aep.spigot.handler.LogMethodeHandler.Methode;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;

public class ARGMoneyLoggerSettings extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoggerSettings(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		int page = 0;
		String otherplayername = player.getName();
		Methode methode = Methode.LOG;
		if(args.length >= 2)
		{
			if(args[1].equals(otherplayername))
			{
				otherplayername = args[1];
			} else
			{
				if(!player.hasPermission(Utility.PERM_BYPASS_LOGOTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				otherplayername = args[1];
			}
		}
		if(args.length >= 3)
		{
			String pagenumber = args[2];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length >= 4)
		{
			try
			{
				methode = Methode.valueOf(args[3]);
			} catch(EnumConstantNotPresentException e) {}
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(otherplayername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		if(args.length <= 2)
		{
			new LoggerSettingsHandler(plugin).generateGUI(player, player.getUniqueId(), UUID.fromString(eco.getUUID()), null, null, page);
		} else
		{
			if(!LoggerSettingsHandler.getLoggerSettings().containsKey(player.getUniqueId()))
			{
				player.sendMessage(ChatApi.tl("No loggersettings found"));
				return; //TODO
			}
			LoggerSettings fst = LoggerSettingsHandler.getLoggerSettings().get(player.getUniqueId());
			new LoggerSettingsHandler(plugin).forwardingToOutput(player, fst, fst.isAction(), methode, page);
		}
		return;
	}
}