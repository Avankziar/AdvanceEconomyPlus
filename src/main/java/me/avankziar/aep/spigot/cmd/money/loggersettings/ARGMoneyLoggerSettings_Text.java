package main.java.me.avankziar.aep.spigot.cmd.money.loggersettings;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;

public class ARGMoneyLoggerSettings_Text extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoggerSettings_Text(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		LoggerSettings ls = LoggerSettingsHandler.getLoggerSettings().get(player.getUniqueId());
		if(ls == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getConfig().getString("CmdMoney.Log.NoLoggerSettingsFound")));
			return;
		}
		String searchtext = "";
		for(int i = 2; i < args.length; i++)
		{
			searchtext += args[i]+" ";
		}
		searchtext = searchtext.substring(0, searchtext.length()-1);
		new LoggerSettingsHandler(plugin).anvilStringEditorOutput(player, searchtext);
		return;
	}
}