package me.avankziar.aep.spigot.listenerhandler;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import me.avankziar.aep.general.objects.LoggerSettings;
import me.avankziar.aep.general.objects.LoggerSettings.InventoryHandlerType;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.handler.LoggerSettingsHandler;

public class LoggerSettingsListenerHandler implements Listener
{
	private AEP plugin;
	
	public LoggerSettingsListenerHandler(AEP plugin)
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
					|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ACCOUNT_ID
					|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ORDERER
					|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_CATEGORY)
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
				|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ACCOUNT_ID
				|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ORDERER
				|| fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_CATEGORY)
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
