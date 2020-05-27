package main.java.me.avankziar.advanceeconomy.spigot.listener;

import java.time.LocalDate;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.handler.ConvertHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;

public class PlayerListener implements Listener
{
	private AdvanceEconomy plugin;
	
	public PlayerListener(AdvanceEconomy plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(event.getPlayer().getUniqueId().toString());
		if(eco == null)
		{
			AdvanceEconomy.getVaultApi().createPlayerAccount(event.getPlayer());
		} else
		{
			String newname = event.getPlayer().getName();
			if(!newname.equals(eco.getName()))
			{
				eco.setName(newname);
				plugin.getMysqlHandler().updateData(Type.PLAYER, eco, "`id` = ?", eco.getId());
			}
		}
		eco = EcoPlayerHandler.getEcoPlayer(event.getPlayer().getUniqueId().toString());
		if(!plugin.getMysqlHandler().exist(Type.TREND,
				"`dates` = ? AND `uuidornumber` = ?", ConvertHandler.serialised(LocalDate.now()), event.getPlayer().getUniqueId().toString()))
		{
			Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
					LocalDate.now(), eco.getUUID(), 0, eco.getBalance()));
		}
	}
}
