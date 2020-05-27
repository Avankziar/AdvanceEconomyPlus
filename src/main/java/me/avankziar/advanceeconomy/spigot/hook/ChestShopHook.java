package main.java.me.avankziar.advanceeconomy.spigot.hook;

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

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;

public class ChestShopHook implements Listener
{
	private AdvanceEconomy plugin;
	
	public ChestShopHook(AdvanceEconomy plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTransaktion(TransactionEvent event)
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
			Bukkit.getPluginManager().callEvent(new EconomyLoggerEvent(
					LocalDateTime.now(),
					clientuuid, owneruuid,
					clientname, ownername,
					clientuuid, amount, EconomyLoggerEvent.Type.DEPOSIT_WITHDRAW,
					plugin.getYamlHandler().getL().getString("ChestShopHook.Buy")
					.replace("%amount%", String.valueOf(itemQuantities))
					.replace("%item%", itemId)
					.replace("%player%", clientname)));
			Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
					LocalDate.now(), owneruuid, amount, EcoPlayerHandler.getEcoPlayer(owneruuid).getBalance()));
			Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
					LocalDate.now(), clientuuid, -amount,EcoPlayerHandler.getEcoPlayer(clientuuid).getBalance()));
		} else 
		{
			Bukkit.getPluginManager().callEvent(new EconomyLoggerEvent(
					LocalDateTime.now(),
					owneruuid, clientuuid,
					ownername, clientname,
					clientuuid, amount, EconomyLoggerEvent.Type.DEPOSIT_WITHDRAW,
					plugin.getYamlHandler().getL().getString("ChestShopHook.Sell")
					.replace("%amount%", String.valueOf(itemQuantities))
					.replace("%item%", itemId)
					.replace("%player%", ownername)));
			Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
					LocalDate.now(), owneruuid, -amount, EcoPlayerHandler.getEcoPlayer(owneruuid).getBalance()));
			Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
					LocalDate.now(), clientuuid, amount,EcoPlayerHandler.getEcoPlayer(clientuuid).getBalance()));
		}
	}

}
