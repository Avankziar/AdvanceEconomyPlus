package main.java.me.avankziar.aep.spigot.listenerhandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.InventoryHandlerType;

public class FilterSettingsListenerHandler implements Listener
{
	private AdvancedEconomyPlus plugin;
	
	public FilterSettingsListenerHandler(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		LoggerSettings fst = LoggerSettingsHandler.getLoggerSettings().get(event.getPlayer().getUniqueId());
		if(fst == null)
		{
			return;
		} else
		{
			if(fst.getInventoryHandlerType() != InventoryHandlerType.NONE)
			{
				fst.setInventoryHandlerType(InventoryHandlerType.NONE);
				LoggerSettingsHandler.getLoggerSettings().replace(event.getPlayer().getUniqueId(), fst);
			}
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
		if(event.getInventory().getType() == InventoryType.CHEST 
				&& fst.getInventoryHandlerType() == InventoryHandlerType.NORMAL)
		{
			LoggerSettingsHandler fsth = new LoggerSettingsHandler(plugin);
			fsth.generateActionFromClick(event);
			return;
		}
		if(event.getInventory().getType() == InventoryType.ANVIL
				&& event.getSlotType() != SlotType.RESULT
				&& 
				(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_COMMENT
				|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_FROM
				|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ORDERER
				|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_TO))
		{
			event.setCancelled(true);
			event.setResult(Result.DENY);
			return;
		}
		if(event.getInventory().getType() == InventoryType.ANVIL
				&& event.getSlotType() == SlotType.RESULT
						&& 
						(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_COMMENT
						|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_FROM
						|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ORDERER
						|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_TO))
		{
			new LoggerSettingsHandler(plugin).anvilStringEditorOutput(event);
			return;
		}
	}
}
