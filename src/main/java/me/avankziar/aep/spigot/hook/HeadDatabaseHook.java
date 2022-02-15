package main.java.me.avankziar.aep.spigot.hook;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
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
		//FIXME 
		/*
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(),
				playeruuid, plugin.getYamlHandler().getLang().getString("HeadDatabase.UUID"),
				playername, plugin.getYamlHandler().getLang().getString("HeadDatabase.Name"),
				plugin.getYamlHandler().getLang().getString("HeadDatabase.Orderer"),
				amount, 
				ActionLoggerEvent.Type.TAKEN,
				plugin.getYamlHandler().getLang().getString("HeadDatabase.Comment")
				.replace("%head%", itemname)));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
				LocalDate.now(), playeruuid, amount, _AEPUserHandler_OLD.getEcoPlayer(UUID.fromString(playeruuid)).getBalance()));
		*/
	}
}
