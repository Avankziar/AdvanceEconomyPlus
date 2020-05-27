package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.handler.ConvertHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.LogHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;
import main.java.me.avankziar.advanceeconomy.spigot.object.TrendLogger;

public class ARGMoneyTrendLog extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyTrendLog(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_TRENDLOG, StringValues.PERM_CMD_MONEY_TRENDLOG,
				AdvanceEconomy.moneyarguments,1,3,StringValues.ARG_MONEY_TRENDLOG_ALIAS,
				StringValues.MONEY_SUGGEST_TRENDLOG);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
		int page = 0;
		String playername = player.getName();
		if(args.length >= 2)
		{
			String pagenumber = args[1];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length == 3)
		{
			if(args[2].equals(playername))
			{
				playername = args[2];
			} else
			{
				if(!player.hasPermission(StringValues.PERM_CMD_MONEY_LOG_OTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				playername = args[2];
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
		ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getList(Type.TREND, "`id`", desc, start, end,
						"(`uuidornumber` = ?)", eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"(`uuidornumber` = ?)", eco.getUUID());
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		String cmdstring = "/money trendlog";
		LogHandler.sendTrendLogs(plugin, player, eco, path, list, page, lastpage, playername, last, cmdstring);
		return;
	}
}