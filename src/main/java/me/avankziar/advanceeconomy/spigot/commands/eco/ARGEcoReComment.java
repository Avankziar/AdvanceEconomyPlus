package main.java.me.avankziar.advanceeconomy.spigot.commands.eco;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler;
import main.java.me.avankziar.advanceeconomy.general.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomyLogger;

public class ARGEcoReComment extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGEcoReComment(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_ECO_RECOMMENT,StringValues.PERM_CMD_ECO_RECOMMENT,
				AdvanceEconomy.ecoarguments,3,9999,StringValues.ARG_ECO_RECOMMENT_ALIAS,
				StringValues.ECO_SUGGEST_RECOMMENT);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_ECO;
		int id = 0;
		String idstring = args[1];
		String recomment = "";
		if(!MatchApi.isInteger(idstring))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", idstring)));
			return;
		}
		id = Integer.parseInt(idstring);
		if(args.length >= 3)
		{
			for(int i = 2; i < args.length; i++)
			{
				if(i == args.length-1)
				{
					recomment += args[i];
				} else
				{
					recomment += args[i]+" ";
				}
				
			}
		}
		EconomyLogger el = (EconomyLogger) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOGGER, "`id` = ?",id);
		if(el == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"ReComment.LogNotExist")));
			return;
		}
		if(!el.getOrdereruuid().equals(player.getUniqueId().toString()) && !player.hasPermission(StringValues.PERM_BYPASS_RECOMMENT))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"ReComment.NoOrderer")));
			return;
		}
		el.setComment(recomment);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOGGER, el, "`id` = ?",id);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"ReComment.CommentWasChange")
				.replace("%id%", idstring)
				.replace("%comment%", recomment)));
		return;
	}
}