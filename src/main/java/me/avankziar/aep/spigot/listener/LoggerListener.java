package main.java.me.avankziar.aep.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import main.java.me.avankziar.aep.spigot.api.LoggerApi;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;

public class LoggerListener implements Listener
{
	/*
	 * Bukkit.getPluginManager().callEvent(new EconomyLoggerEvent());
	 */
	
	@EventHandler
	public void onEconomyLogger(ActionLoggerEvent event)
	{
		LoggerApi.addEconomyLogger(event.getDateTime(), event.getFromUUIDOrNumber(), event.getToUUIDOrNumber(),
				event.getFromName(), event.getToName(), event.getOrdererUUID(),
				event.getAmount(), ActionLogger.Type.valueOf(event.getType().toString()), event.getComment());
	}
	
	@EventHandler
	public void onTrendLogger(TrendLoggerEvent event)
	{
		LoggerApi.addTrendLogger(event.getDate(), event.getUUIDOrNumber(), event.getRelativeAmountChange(), event.getBalance());
	}

}
