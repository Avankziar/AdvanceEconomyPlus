package main.java.me.avankziar.aep.spigot.hook;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.maxgamer.quickshop.event.ShopSuccessPurchaseEvent;
import org.maxgamer.quickshop.shop.Shop;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;

public class QuickShopHook implements Listener
{
	private AdvancedEconomyPlus plugin;
	
	public QuickShopHook(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTransaction(ShopSuccessPurchaseEvent event) throws IOException
	{
		Shop shop = event.getShop();
		Player player = event.getPlayer();
		String clientuuid = player.getUniqueId().toString();
		String clientname = player.getName();
		String owneruuid = shop.getOwner().toString();
		String ownername = Utility.convertUUIDToName(owneruuid);
		if(owneruuid == null)
		{
			ownername = "none";
		}
		EcoPlayer owner = EcoPlayerHandler.getEcoPlayer(owneruuid);
        EcoPlayer client = EcoPlayerHandler.getEcoPlayer(clientuuid);
        
        Double balanceowner = 0.0; //TODO bank
        Double balanceclient = 0.0;
        
        if(owner != null) 
        {
        	balanceowner = owner.getBalance();
        }
        if(client != null)
        {
        	balanceclient = client.getBalance();
        }
        
		int quantity = event.getAmount();
		double amount = event.getBalance();
		ItemStack is = shop.getItem();
		String isname = null;
		if(is.hasItemMeta())
		{
			ItemMeta im = is.getItemMeta();
			if(im.hasDisplayName())
			{
				isname = im.getDisplayName();
			}
		}
		if(isname == null)
		{
			isname = is.getType().toString();
		}
		
		if(shop.isBuying())
		{
			Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
					LocalDateTime.now(),
					clientuuid, 
					owneruuid,
					clientname, 
					ownername,
					clientuuid, 
					amount, 
					ActionLoggerEvent.Type.DEPOSIT_WITHDRAW,
					plugin.getYamlHandler().getL().getString("QuickShopHook.Buy")
					.replace("%amount%", String.valueOf(quantity))
					.replace("%item%", isname)
					.replace("%player%", clientname)));
			Bukkit.getPluginManager().callEvent(
					new TrendLoggerEvent(
					LocalDate.now(), 
					owneruuid, 
					amount, 
					EcoPlayerHandler.getEcoPlayer(
							owneruuid)
					.getBalance()));
			Bukkit.getPluginManager().callEvent(
					new TrendLoggerEvent(
					LocalDate.now(), 
					clientuuid,
					-amount,
					EcoPlayerHandler.getEcoPlayer(
							clientuuid)
					.getBalance()));
		} else 
		{
			Bukkit.getPluginManager().callEvent(
					new ActionLoggerEvent(
					LocalDateTime.now(),
					owneruuid, 
					clientuuid,
					ownername, 
					clientname,
					clientuuid, 
					amount, 
					ActionLoggerEvent.Type.DEPOSIT_WITHDRAW,
					plugin.getYamlHandler().getL().getString("QuickShopHook.Sell")
					.replace("%amount%", String.valueOf(quantity))
					.replace("%item%", isname)
					.replace("%player%", ownername)));
			Bukkit.getPluginManager().callEvent(
					new TrendLoggerEvent(
					LocalDate.now(), 
					owneruuid, 
					-amount, 
					balanceowner));
			Bukkit.getPluginManager().callEvent(
					new TrendLoggerEvent(
					LocalDate.now(), 
					clientuuid, 
					amount,
					balanceclient));
		}
	}

}
