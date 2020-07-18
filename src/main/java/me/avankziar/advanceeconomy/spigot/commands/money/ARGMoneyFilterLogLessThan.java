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

public class ARGMoneyFilterLogLessThan extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyFilterLogLessThan(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_FILTERLOGLESSTHAN, StringValues.PERM_CMD_MONEY_FILTERLOGLESSTHAN,
				AdvanceEconomy.moneyarguments,2,4,StringValues.ARG_MONEY_FILTERLOGLESSTHAN_ALIAS,
				StringValues.MONEY_SUGGEST_FILTERLOGLESSTHAN);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
		double value = 0.0;
		int page = 0;
		String playername = player.getName();
		if(!MatchApi.isDouble(args[1]))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", args[1])));
			return;
		}
		value = Double.parseDouble(args[1]);
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
						"`amount` < ? AND (`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?)",
						value, eco.getUUID(), eco.getUUID(), eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.LOGGER,
				"`amount` < ? AND (`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?)",
				value, eco.getUUID(), eco.getUUID(), eco.getUUID());
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		String cmdstring = "/money filterloglessthan "+value;
		LogHandler.sendLogs(plugin, player, eco, path, list, page, lastpage, playername, last, cmdstring);
		return;
	}
}