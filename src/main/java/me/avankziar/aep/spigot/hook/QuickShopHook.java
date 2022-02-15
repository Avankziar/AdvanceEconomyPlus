package main.java.me.avankziar.aep.spigot.hook;

import java.io.IOException;

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
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;

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
		String ownername = Utility.convertUUIDToName(owneruuid, EconomyType.PLAYER);
		if(owneruuid == null)
		{
			ownername = "none";
		}
		OLD_AEPUser owner = _AEPUserHandler_OLD.getEcoPlayer(owneruuid);
        OLD_AEPUser client = _AEPUserHandler_OLD.getEcoPlayer(clientuuid);
        
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

		//FIXME 
		/*
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
					plugin.getYamlHandler().getLang().getString("QuickShopHook.Buy")
					.replace("%amount%", String.valueOf(quantity))
					.replace("%item%", isname)
					.replace("%player%", clientname)));
			Bukkit.getPluginManager().callEvent(
					new TrendLoggerEvent(
					LocalDate.now(), 
					owneruuid, 
					amount, 
					_AEPUserHandler_OLD.getEcoPlayer(
							owneruuid)
					.getBalance()));
			Bukkit.getPluginManager().callEvent(
					new TrendLoggerEvent(
					LocalDate.now(), 
					clientuuid,
					-amount,
					_AEPUserHandler_OLD.getEcoPlayer(
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
					plugin.getYamlHandler().getLang().getString("QuickShopHook.Sell")
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
		*/
	}

}
