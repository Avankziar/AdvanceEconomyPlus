package main.java.me.avankziar.aep.spigot.cmd.money.loggersettings;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;

public class ARGMoneyLoggerSettings_Other extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoggerSettings_Other(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		int page = 0;
		String otherplayername = args[2];
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(otherplayername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		if(!LoggerSettingsHandler.getLoggerSettings().containsKey(UUID.fromString(eco.getUUID())))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoOtherLoggerSettingsFound")));
			return;
		}
		new LoggerSettingsHandler(plugin).generateGUI(player, UUID.fromString(eco.getUUID()), UUID.fromString(eco.getUUID()), null, null, page);
		return;
	}
}