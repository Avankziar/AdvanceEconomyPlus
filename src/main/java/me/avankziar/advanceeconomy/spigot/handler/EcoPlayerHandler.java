package main.java.me.avankziar.advanceeconomy.spigot.handler;

import org.bukkit.OfflinePlayer;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;

public class EcoPlayerHandler
{
	public static EcoPlayer getEcoPlayer(String playeruuid)
	{
		return (EcoPlayer) AdvanceEconomy.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", playeruuid);
	}
	
	public static EcoPlayer getEcoPlayerFromName(String playername)
	{
		return (EcoPlayer) AdvanceEconomy.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_name` = ?", playername);
	}
	
	public static EcoPlayer getEcoPlayer(OfflinePlayer player)
	{
		return (EcoPlayer) AdvanceEconomy.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", player.getUniqueId().toString());
	}
}
