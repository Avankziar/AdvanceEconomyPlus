package main.java.me.avankziar.aep.spigot.hook;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import me.arcaniax.hdb.api.PlayerClickHeadEvent;

public class HeadDatabaseHook implements Listener
{
	private AdvancedEconomyPlus plugin;
	
	public HeadDatabaseHook(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onHeadPurchase(PlayerClickHeadEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		String playeruuid = event.getPlayer().getUniqueId().toString();
		String playername = event.getPlayer().getName();
		double amount = event.getPrice();
		ItemStack is = event.getHead();
		String itemname = is.getType().toString();
		if(is.hasItemMeta())
		{
			if(is.getItemMeta().hasDisplayName())
			{
				itemname = is.getItemMeta().getDisplayName();
			}
		}
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(),
				playeruuid, plugin.getYamlHandler().getL().getString("HeadDatabase.UUID"),
				playername, plugin.getYamlHandler().getL().getString("HeadDatabase.Name"),
				plugin.getYamlHandler().getL().getString("HeadDatabase.Orderer"),
				amount, 
				ActionLoggerEvent.Type.TAKEN,
				plugin.getYamlHandler().getL().getString("HeadDatabase.Comment")
				.replace("%head%", itemname)));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
				LocalDate.now(), playeruuid, amount, AEPUserHandler.getEcoPlayer(playeruuid).getBalance()));
	}
}
