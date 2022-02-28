package main.java.me.avankziar.aep.spigot.cmd.cet.loggersettings;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;

public class LoggerSettingsText extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public LoggerSettingsText(ArgumentConstructor ac)
	{
		super(ac);
		this.plugin = BaseConstructor.getPlugin();
		this.ac = ac;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Cmd only for Players!");
			return;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(ac.getPermission()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		middlePart(player, ac.getCommandString(), args);
	}
	
	/*
	 * 
	 */
	private void middlePart(Player player, String cmdString, String[] args)
	{
		LoggerSettings ls = LoggerSettingsHandler.getLoggerSettings().get(player.getUniqueId());
		if(ls == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getConfig().getString("Log.NoLoggerSettingsFound")));
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