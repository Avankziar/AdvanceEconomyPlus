package main.java.me.avankziar.advanceeconomy.spigot.commands.eco;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomyLogger;

public class ARGEcoDeleteLog extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGEcoDeleteLog(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_ECO_DELETELOG,StringValues.PERM_CMD_ECO_DELETELOG,
				AdvanceEconomy.ecoarguments,2,2,StringValues.ARG_ECO_DELETELOG_ALIAS,
				StringValues.ECO_SUGGEST_DELETELOG);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_ECO;
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
		EconomyLogger el = (EconomyLogger) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOGGER, "`id` = ?",id);
		if(el == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"DeleteLog.LogNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.LOGGER, "`id` = ?", id);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"DeleteLog.LogWasDeleted")
				.replace("%id%", idstring)));
		return;
	}
}
