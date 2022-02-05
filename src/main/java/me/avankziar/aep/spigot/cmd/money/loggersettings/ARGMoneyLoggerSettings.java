package main.java.me.avankziar.aep.spigot.cmd.money.loggersettings;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;

public class ARGMoneyLoggerSettings extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoggerSettings(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String otherplayername = player.getName();
		OLD_AEPUser eco = _AEPUserHandler_OLD.getEcoPlayer(otherplayername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(args.length <= 2)
		{
			new LoggerSettingsHandler(plugin).generateGUI(player, player.getUniqueId(), UUID.fromString(eco.getUUID()), null, null, 0);
		} else
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("OtherCmd")));
		}
		return;
	}
}