package main.java.me.avankziar.aep.spigot.cmd.money.loggersettings;

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
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.handler.LogMethodeHandler.Methode;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;

public class ARGMoneyLoggerSettings_GUI extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoggerSettings_GUI(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		if(args.length >= 3)
		{
			if(args[2].equals(otherplayername))
			{
				otherplayername = args[2];
			} else
			{
				if(!player.hasPermission(Utility.PERM_BYPASS_LOGOTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				otherplayername = args[2];
			}
		}
		if(args.length >= 4)
		{
			String pagenumber = args[3];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length >= 5)
		{
			try
			{
				methode = Methode.valueOf(args[4]);
			} catch(EnumConstantNotPresentException e) {}
		}
		AEPUser eco = AEPUserHandler.getEcoPlayer(otherplayername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		if(args.length <= 3)
		{
			new LoggerSettingsHandler(plugin).generateGUI(player, player.getUniqueId(), UUID.fromString(eco.getUUID()), null, null, page);
		} else
		{
			if(!LoggerSettingsHandler.getLoggerSettings().containsKey(player.getUniqueId()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getConfig().getString("CmdMoney.Log.NoLoggerSettingsFound")));
				return;
			}
			LoggerSettings fst = LoggerSettingsHandler.getLoggerSettings().get(player.getUniqueId());
			new LoggerSettingsHandler(plugin).forwardingToOutput(player, fst, fst.isAction(), methode, page);
		}
		return;
	}
}