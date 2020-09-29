package main.java.me.avankziar.aep.spigot.handler;

import org.bukkit.OfflinePlayer;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;

public class EcoPlayerHandler
{
	public static EcoPlayer getEcoPlayer(String playeruuid)
	{
		return (EcoPlayer) AdvancedEconomyPlus.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", playeruuid);
	}
	
	public static EcoPlayer getEcoPlayerFromName(String playername)
	{
		return (EcoPlayer) AdvancedEconomyPlus.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_name` = ?", playername);
	}
	
	public static EcoPlayer getEcoPlayer(OfflinePlayer player)
	{
		return (EcoPlayer) AdvancedEconomyPlus.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", player.getUniqueId().toString());
	}
}
