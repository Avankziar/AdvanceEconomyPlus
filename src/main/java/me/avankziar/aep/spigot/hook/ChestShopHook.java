package main.java.me.avankziar.aep.spigot.hook;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.ChestShop.Events.TransactionEvent;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;

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
        OLD_AEPUser owner = _AEPUserHandler_OLD.getEcoPlayer(UUID.fromString(owneruuid));
        OLD_AEPUser client = _AEPUserHandler_OLD.getEcoPlayer(UUID.fromString(clientuuid));
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
      //FIXME
      		/*
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
					plugin.getYamlHandler().getLang().getString("ChestShopHook.Buy")
					.replace("%amount%", String.valueOf(itemQuantities))
					.replace("%item%", itemId)
					.replace("%player%", clientname)));
			Bukkit.getPluginManager().callEvent(
					new TrendLoggerEvent(
					LocalDate.now(), 
					owneruuid, 
					amount, 
					_AEPUserHandler_OLD.getEcoPlayer(
							UUID.fromString(owneruuid))
					.getBalance()));
			Bukkit.getPluginManager().callEvent(
					new TrendLoggerEvent(
					LocalDate.now(), 
					clientuuid,
					-amount,
					_AEPUserHandler_OLD.getEcoPlayer(
							UUID.fromString(clientuuid))
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
					plugin.getYamlHandler().getLang().getString("ChestShopHook.Sell")
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
		*/
	}

}
