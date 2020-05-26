package main.java.me.avankziar.advanceeconomy.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import main.java.me.avankziar.advanceeconomy.spigot.api.LoggerApi;
import main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomyLogger;

public class LoggerListener implements Listener
{
	/*
	 * Bukkit.getPluginManager().callEvent(new EconomyLoggerEvent());
	 */
	
	@EventHandler
	public void onEconomyLogger(EconomyLoggerEvent event)
	{
		LoggerApi.addEconomyLogger(event.getDateTime(), event.getFromUUIDOrNumber(), event.getToUUIDOrNumber(),
				event.getFromName(), event.getToName(), event.getOrdererUUID(),
				event.getAmount(), EconomyLogger.Type.valueOf(event.getType().toString()), event.getComment());
	}
	
	@EventHandler
	public void onTrendLogger(TrendLoggerEvent event)
	{
		LoggerApi.addTrendLogger(event.getDate(), event.getUUIDOrNumber(), event.getRelativeAmountChange(), event.getBalance());
	}

}
