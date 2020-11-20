package main.java.me.avankziar.aep.spigot.listenerhandler;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.InventoryHandlerType;

public class LoggerSettingsListenerHandler implements Listener
{
	private AdvancedEconomyPlus plugin;
	
	public LoggerSettingsListenerHandler(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		LoggerSettings fst = LoggerSettingsHandler.getLoggerSettings().get(event.getPlayer().getUniqueId());
		if(fst == null)
		{
			Player player = (Player) event.getPlayer();
			player.updateInventory();
			return;
		} else
		{
			if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_COMMENT
					|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_FROM
					|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ORDERER
					|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_TO)
			{
				Player player = (Player) event.getPlayer();
				player.updateInventory();
				return;
			}
			if(fst.getInventoryHandlerType() != InventoryHandlerType.NONE)
			{
				fst.setInventoryHandlerType(InventoryHandlerType.NONE);
				LoggerSettingsHandler.getLoggerSettings().replace(event.getPlayer().getUniqueId(), fst);
			}
			Player player = (Player) event.getPlayer();
			player.updateInventory();
			return;
		}
	}
	
	@EventHandler
	public void onNormalChestKlick(InventoryClickEvent event) throws IOException
	{
		if(event.getClickedInventory() == null)
		{
			return;
		}
		LoggerSettings fst = LoggerSettingsHandler.getLoggerSettings().get(event.getWhoClicked().getUniqueId());
		if(fst == null)
		{
			return;
		}
		if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_COMMENT
				|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_FROM
				|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ORDERER
				|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_TO)
		{
			return;
		}
		if(event.getInventory().getType() == InventoryType.CHEST 
				&& fst.getInventoryHandlerType() == InventoryHandlerType.NORMAL)
		{
			LoggerSettingsHandler fsth = new LoggerSettingsHandler(plugin);
			fsth.generateActionFromClick(event);
		} else
		{
			fst.setInventoryHandlerType(InventoryHandlerType.NONE);
			LoggerSettingsHandler.getLoggerSettings().replace(event.getWhoClicked().getUniqueId(), fst);
		}
		return;
	}
}
