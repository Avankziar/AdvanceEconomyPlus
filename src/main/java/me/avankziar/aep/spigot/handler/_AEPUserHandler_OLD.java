package main.java.me.avankziar.aep.spigot.handler;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;

public class _AEPUserHandler_OLD
{
	public static OLD_AEPUser getEcoPlayer(UUID playeruuid)
	{
		return (OLD_AEPUser) AdvancedEconomyPlus.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", playeruuid.toString());
	}
	
	public static OLD_AEPUser getEcoPlayer(String playername)
	{
		return (OLD_AEPUser) AdvancedEconomyPlus.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_name` = ?", playername);
	}
	
	public static OLD_AEPUser getEcoPlayer(OfflinePlayer player)
	{
		return (OLD_AEPUser) AdvancedEconomyPlus.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", player.getUniqueId().toString());
	}
}
