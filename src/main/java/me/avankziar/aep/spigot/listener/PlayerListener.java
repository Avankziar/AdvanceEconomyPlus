package main.java.me.avankziar.aep.spigot.listener;

import java.time.LocalDate;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;

public class PlayerListener implements Listener
{
	private AdvancedEconomyPlus plugin;
	
	public PlayerListener(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		AEPUser eco = AEPUserHandler.getEcoPlayer(event.getPlayer().getUniqueId().toString());
		if(eco == null)
		{
			AdvancedEconomyPlus.getVault().createPlayerAccount(event.getPlayer());
		} else
		{
			String newname = event.getPlayer().getName();
			if(!newname.equals(eco.getName()))
			{
				eco.setName(newname);
				plugin.getMysqlHandler().updateData(Type.PLAYER, eco, "`id` = ?", eco.getId());
			}
		}
		eco = AEPUserHandler.getEcoPlayer(event.getPlayer().getUniqueId());
		if(!plugin.getMysqlHandler().exist(Type.TREND,
				"`dates` = ? AND `uuidornumber` = ?", ConvertHandler.serialised(LocalDate.now()), event.getPlayer().getUniqueId().toString()))
		{
			Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
					LocalDate.now(), eco.getUUID(), 0, eco.getBalance()));
		}
	}
}
