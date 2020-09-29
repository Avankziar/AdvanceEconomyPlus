package main.java.me.avankziar.aep.spigot.hook;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;

public class ChestShopHook implements Listener
{
	private AdvancedEconomyPlus plugin;
	
	public ChestShopHook(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTransaction(TransactionEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		String owneruuid = event.getOwnerAccount().getUuid().toString();
		String ownername = event.getOwnerAccount().getName();
		String clientuuid = event.getClient().getUniqueId().toString();
		String clientname = event.getClient().getName();
		double amount = event.getExactPrice().doubleValue();
		String itemId = "";
        int itemQuantities = 0;
        Material itemType = null;
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

        for (ItemStack item : event.getStock()) 
        {
            if (itemType == null) {
                itemType = item.getType();
                itemId = MaterialUtil.getSignName(item);
            }
            if (item.getType() == itemType) {
                itemQuantities += item.getAmount();
            }
        }
		if(event.getTransactionType() == TransactionType.BUY)
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
					plugin.getYamlHandler().getL().getString("ChestShopHook.Buy")
					.replace("%amount%", String.valueOf(itemQuantities))
					.replace("%item%", itemId)
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
					plugin.getYamlHandler().getL().getString("ChestShopHook.Sell")
					.replace("%amount%", String.valueOf(itemQuantities))
					.replace("%item%", itemId)
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
