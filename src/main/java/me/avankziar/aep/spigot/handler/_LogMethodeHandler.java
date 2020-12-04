package main.java.me.avankziar.aep.spigot.handler;

public class _LogMethodeHandler
{
	
	
	/*public static void actionLog(AdvancedEconomyPlus plugin,
			Methode methode,
			Player player, String playername,
			int page,
			String cmdstring) throws IOException
	{
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		int start = 0;
		int end = 0;
		boolean desc = true;
		ArrayList<ActionLogger> list = new ArrayList<>();
		int last = 0;
		switch(methode)
		{
		case BARCHART:
			start = page*1;
			end = page*1+1;
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime ending = LocalDateTime.of(
					now.getYear()-(2*start),
					now.getMonth(),
					now.getDayOfMonth(),
					now.getHour(),
					now.getMinute(),
					now.getSecond());
			LocalDateTime starting = LocalDateTime.of(
					now.getYear()-(2*end),
					now.getMonth(),
					1, 0, 0, 1);
			desc = false;
			list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getAllListAtIIIDateTimeModified(plugin, "`id`", desc, starting, ending,
					"`from_uuidornumber` = ? OR `to_uuidornumber` = ?",
					eco.getUUID(), eco.getUUID()));
			last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
					"`from_uuidornumber` = ? OR `to_uuidornumber` = ?",
					eco.getUUID(), eco.getUUID());
			LogHandler.sendActionBarChart(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case DIAGRAM:
			start = page*10;
			end = 9;
			desc = true;
			list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
							"`from_uuidornumber` = ? OR `to_uuidornumber` = ?", eco.getUUID(), eco.getUUID()));
			last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
					"`from_uuidornumber` = ? OR `to_uuidornumber` = ?",
					eco.getUUID(), eco.getUUID());
			LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case GRAFIC:
			start = page*10;
			end = 9;
			desc = true;
			list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
							"`from_uuidornumber` = ? OR `to_uuidornumber` = ?", eco.getUUID(), eco.getUUID()));
			last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
					"`from_uuidornumber` = ? OR `to_uuidornumber` = ?",
					eco.getUUID(), eco.getUUID());
			LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, playername, last, cmdstring);
			return;
		case LOG:
			start = page*10;
			end = 9;
			desc = true;
			list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
							"`from_uuidornumber` = ? OR `to_uuidornumber` = ?", eco.getUUID(), eco.getUUID()));
			last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
					"`from_uuidornumber` = ? OR `to_uuidornumber` = ?",
					eco.getUUID(), eco.getUUID());
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
						"`relative_amount_change` > ? AND `uuidornumber` = ?",
						value, eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"`relative_amount_change` > ? AND `uuidornumber` = ?",
				value, eco.getUUID());
		switch(methode)
		{
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
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
						"`relative_amount_change` < ? AND `uuidornumber` = ?",
						value, eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"`relative_amount_change` < ? AND `uuidornumber` = ?",
				value, eco.getUUID());
		switch(methode)
		{
		case BARCHART:
			return;
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
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		int start = page*10;
		int end = 9;
		ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getList(Type.TREND, "`relative_amount_change`", desc, start, end,
						"`uuidornumber` = ?",
						eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"`uuidornumber` = ?",
				eco.getUUID());
		switch(methode)
		{
		case BARCHART:
			return;
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
			boolean desc, boolean isFirstStand, int page,
			String cmdstring)
	{
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		int start = page*10;
		int end = 9;
		ArrayList<TrendLogger> list = new ArrayList<>();
		if(isFirstStand)
		{
			list = ConvertHandler.convertListIV(
					plugin.getMysqlHandler().getList(Type.TREND, "`firstvalue`"
							, desc, start, end,
							"`uuidornumber` = ?", eco.getUUID()));
		} else
		{
			list = ConvertHandler.convertListIV(
					plugin.getMysqlHandler().getList(Type.TREND, "`lastvalue`"
							, desc, start, end,
							"`uuidornumber` = ?", eco.getUUID()));
		}
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"`uuidornumber` = ?", eco.getUUID());
		switch(methode)
		{
		case BARCHART:
			return;
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
	}*/
}
