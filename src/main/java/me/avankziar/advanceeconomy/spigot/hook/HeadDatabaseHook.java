package main.java.me.avankziar.advanceeconomy.spigot.hook;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import me.arcaniax.hdb.api.PlayerClickHeadEvent;

public class HeadDatabaseHook implements Listener
{
	private AdvanceEconomy plugin;
	
	public HeadDatabaseHook(AdvanceEconomy plugin)
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
		Bukkit.getPluginManager().callEvent(new EconomyLoggerEvent(
				LocalDateTime.now(),
				playeruuid, plugin.getYamlHandler().getL().getString("HeadDatabase.UUID"),
				playername, plugin.getYamlHandler().getL().getString("HeadDatabase.Name"),
				plugin.getYamlHandler().getL().getString("HeadDatabase.Orderer"),
				amount, 
				EconomyLoggerEvent.Type.TAKEN,
				plugin.getYamlHandler().getL().getString("HeadDatabase.Comment")
				.replace("%head%", itemname)));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
				LocalDate.now(), playeruuid, amount, EcoPlayer.getEcoPlayer(playeruuid).getBalance()));
	}
}
