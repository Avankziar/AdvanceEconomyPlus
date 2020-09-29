package main.java.me.avankziar.aep.spigot.handler;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import main.java.me.avankziar.aep.spigot.object.TrendLogger;

public class LogMethodeHandler
{
	public enum Methode
	{
		DIAGRAM, GRAFIC, LOG;
	}
	
	public static void actionLog(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
						"`from_uuidornumber` = ? OR `to_uuidornumber` = ?", eco.getUUID(), eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"`from_uuidornumber` = ? OR `to_uuidornumber` = ?",
				eco.getUUID(), eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void actionLogBetween(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			double min, double max, int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
						"`amount` > ? AND `amount` < ? AND (`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?)",
						min, max, eco.getUUID(), eco.getUUID(), eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"`amount` > ? AND `amount` < ? AND (`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?)",
				min, max, eco.getUUID(), eco.getUUID(), eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void actionLogComment(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			String searchword, int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
						"(`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?) AND (`comment` LIKE ?)",
						eco.getUUID(), eco.getUUID(), eco.getUUID(),"%"+searchword+"%"));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"(`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?) AND (`comment` LIKE ?)",
				eco.getUUID(), eco.getUUID(), eco.getUUID(),"%"+searchword+"%");
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void actionLogCommentOther(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			String searchword, boolean desc, int page,
			String cmdstring)
	{
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
		int end = 9;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`amount`", desc, start, end,
						"(`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?) AND (`comment` LIKE ?)",
						eco.getUUID(), eco.getUUID(), eco.getUUID(),"%"+searchword+"%"));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"(`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?) AND (`comment` LIKE ?)",
				eco.getUUID(), eco.getUUID(), eco.getUUID(),"%"+searchword+"%");
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void actionLogFrom(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			String searchword, int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
						"`from_name` = ?  AND (`orderer_uuid` = ? OR `to_uuidornumber` = ?)",
						searchword, eco.getUUID(), eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"`from_name` = ?  AND (`orderer_uuid` = ? OR `to_uuidornumber` = ?)",
				searchword, eco.getUUID(), eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void actionLogGreaterThan(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			double value, int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
						"`amount` > ? AND (`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?)",
						value, eco.getUUID(), eco.getUUID(), eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"`amount` > ? AND (`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?)",
				value, eco.getUUID(), eco.getUUID(), eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void actionLogLessThan(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			double value, int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
						"`amount` < ? AND (`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?)",
						value, eco.getUUID(), eco.getUUID(), eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"`amount` < ? AND (`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?)",
				value, eco.getUUID(), eco.getUUID(), eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void actionLogOrderer(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			String searchword, int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
						"`orderer_uuid` = ?  AND (`to_uuidornumber` = ? OR `from_uuidornumber` = ?)",
						searchword, eco.getUUID(), eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"`orderer_uuid` = ?  AND (`to_uuidornumber` = ? OR `from_uuidornumber` = ?)",
				searchword, eco.getUUID(), eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void actionLogSort(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			boolean desc, int page,
			String cmdstring)
	{
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
		int end = 9;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`amount`", desc, start, end,
						"`from_uuidornumber` = ? OR `orderer_uuid` = ? OR `to_uuidornumber` = ?",
						eco.getUUID(), eco.getUUID(), eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"`from_uuidornumber` = ?  OR `orderer_uuid` = ? OR `to_uuidornumber` = ?",
				eco.getUUID(), eco.getUUID(), eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void actionLogTo(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			String searchword, int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
						"`to_name` = ?  AND (`orderer_uuid` = ? OR `from_uuidornumber` = ?)",
						searchword, eco.getUUID(), eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
				"`to_name` = ?  AND (`orderer_uuid` = ? OR `from_uuidornumber` = ?)",
				searchword, eco.getUUID(), eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendActionLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void trendLog(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getList(Type.TREND, "`id`", desc, start, end,
						"(`uuidornumber` = ?)", eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"(`uuidornumber` = ?)", eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendTrendDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendTrendGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendTrendLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void trendLogBetween(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			double min, double max, int page,
			String cmdstring)
	{
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
		int end = 9;
		boolean desc = true;
		ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getList(Type.TREND, "`id`", desc, start, end,
						"`relative_amount_change` > ? AND `relative_amount_change` < ? AND `uuidornumber` = ?",
						min, max, eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"`relative_amount_change` > ? AND `relative_amount_change` < ? AND `uuidornumber` = ?",
				min, max, eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendTrendDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendTrendGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendTrendLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
	}
	
	public static void trendLogGreaterThan(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			double value, int page,
			String cmdstring)
	{
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
						"`relative_amount_change` > ? AND `uuidornumber` = ?",
						value, eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"`relative_amount_change` > ? AND `uuidornumber` = ?",
				value, eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendTrendDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendTrendGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendTrendLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
		return;
	}
	
	public static void trendLogLessThan(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			double value, int page,
			String cmdstring)
	{
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
						"`relative_amount_change` < ? AND `uuidornumber` = ?",
						value, eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"`relative_amount_change` < ? AND `uuidornumber` = ?",
				value, eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendTrendDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendTrendGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendTrendLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
		return;
	}
	
	public static void trendLogSort(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			boolean desc, int page,
			String cmdstring)
	{
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
		ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getList(Type.TREND, "`relative_amount_change`", desc, start, end,
						"`uuidornumber` = ?",
						eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"`uuidornumber` = ?",
				eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendTrendDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendTrendGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendTrendLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
		return;
	}
	
	public static void trendLogStand(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			boolean desc, int page,
			String cmdstring)
	{
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
		ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getList(Type.TREND, "`firstvalue`, `lastvalue`", desc, start, end,
						"`uuidornumber` = ?",
						eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"`uuidornumber` = ?",
				eco.getUUID());
		switch(methode)
		{
		case DIAGRAM:
			LogHandler.sendTrendDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			LogHandler.sendTrendGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			LogHandler.sendTrendLogs(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		}
		return;
	}
}
