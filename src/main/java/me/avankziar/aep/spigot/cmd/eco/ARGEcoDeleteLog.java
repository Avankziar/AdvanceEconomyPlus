package main.java.me.avankziar.aep.spigot.cmd.eco;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;

public class ARGEcoDeleteLog extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGEcoDeleteLog(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		int id = 0;
		String idstring = args[1];
		if(!MatchApi.isInteger(idstring))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", idstring)));
			return;
		}
		id = Integer.parseInt(idstring);
		ActionLogger el = (ActionLogger) plugin.getMysqlHandler().getData(MysqlHandler.Type.ACTION, "`id` = ?",id);
		if(el == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdEco.DeleteLog.LogNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACTION, "`id` = ?", id);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdEco.DeleteLog.LogWasDeleted")
				.replace("%id%", idstring)));
		return;
	}
}
