package main.java.me.avankziar.aep.spigot.handler;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;

public class AEPUserHandler
{
	public static AEPUser getEcoPlayer(UUID playeruuid)
	{
		return (AEPUser) AdvancedEconomyPlus.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", playeruuid.toString());
	}
	
	public static AEPUser getEcoPlayer(String playername)
	{
		return (AEPUser) AdvancedEconomyPlus.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_name` = ?", playername);
	}
	
	public static AEPUser getEcoPlayer(OfflinePlayer player)
	{
		return (AEPUser) AdvancedEconomyPlus.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", player.getUniqueId().toString());
	}
}
