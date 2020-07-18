package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.general.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.handler.ConvertHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.LogHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomyLogger;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;

public class ARGMoneyFilterLogComment extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyFilterLogComment(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_FILTERLOGCOMMENT, StringValues.PERM_CMD_MONEY_FILTERLOGCOMMENT,
				AdvanceEconomy.moneyarguments,2,4,StringValues.ARG_MONEY_FILTERLOGCOMMENT_ALIAS,
				StringValues.MONEY_SUGGEST_FILTERLOGCOMMENT);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
		String searchword = args[1]; 
		int page = 0;
		String playername = player.getName();
		if(args.length >= 3)
		{
			String pagenumber = args[2];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length == 4)
		{
			if(args[3].equals(playername))
			{
				playername = args[3];
			} else
			{
				if(!player.hasPermission(StringValues.PERM_CMD_MONEY_FILTERLOGOTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				playername = args[3];
			}
		}
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayerFromName(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		int start = page*10;
		int end = page*10+9;
		boolean desc = true;
		ArrayList<EconomyLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.LOGGER, "`id`", desc, start, end,
						"(`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?) AND (`comment` LIKE ?)",
						eco.getUUID(), eco.getUUID(), eco.getUUID(),"%"+searchword+"%"));
		int last = plugin.getMysqlHandler().countWhereID(Type.LOGGER,
				"(`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?) AND (`comment` LIKE ?)",
				eco.getUUID(), eco.getUUID(), eco.getUUID(),"%"+searchword+"%");
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		String cmdstring = "/money filterlogcomment "+searchword;
		LogHandler.sendLogs(plugin, player, eco, path, list, page, lastpage, playername, last, cmdstring);
		return;
	}
}