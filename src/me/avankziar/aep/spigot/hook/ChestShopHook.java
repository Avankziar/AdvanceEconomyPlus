package me.avankziar.aep.spigot.hook;

import java.time.LocalDate;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType;

import me.avankziar.aep.general.objects.ActionLogger;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.api.LoggerApi;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class ChestShopHook implements Listener
{
	private AEP plugin;
	
	public ChestShopHook(AEP plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTransaction(TransactionEvent event)
	{
		//Hier mal serververkaufsschilder testen
		if(event.isCancelled())
		{
			return;
		}
		UUID ouuid = event.getOwnerAccount().getUuid();
		String oname = event.getOwnerAccount().getName();
		UUID cuuid = event.getClient().getUniqueId();
		String cname = event.getClient().getName();
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
        String category = plugin.getYamlHandler().getLang().getString("ChestShopHook.Category");
		
		if(event.getTransactionType() == TransactionType.SELL)
		{
			String comment = plugin.getYamlHandler().getLang().getString("ChestShopHook.Sell")
					.replace("%amount%", String.valueOf(itemQuantities))
					.replace("%item%", itemId)
					.replace("%player%", cname);
			Account from = plugin.getIFHApi().getDefaultAccount(ouuid, AccountCategory.SHOP, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			if(from == null)
			{
				from = plugin.getIFHApi().getDefaultAccount(ouuid, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			}
			Account to = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.SHOP, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			if(to == null)
			{
				to = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			}
			if(from == null || to == null)
			{
				return;
			}
			LoggerApi.addActionLogger(new ActionLogger(
					0,
					System.currentTimeMillis(),
					from.getID(), to.getID(),
					-1, OrdererType.PLAYER, cuuid, null, amount, amount, 0.0, category, comment));
			LoggerApi.addTrendLogger(LocalDate.now(), from.getID(), -amount, from.getBalance());
			LoggerApi.addTrendLogger(LocalDate.now(), to.getID(), amount, to.getBalance());
		} else
		{
			String comment = plugin.getYamlHandler().getLang().getString("ChestShopHook.Buy")
					.replace("%amount%", String.valueOf(itemQuantities))
					.replace("%item%", itemId)
					.replace("%player%", oname);
			Account from = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.SHOP, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			if(from == null)
			{
				from = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			}
			Account to = plugin.getIFHApi().getDefaultAccount(ouuid, AccountCategory.SHOP, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			if(to == null)
			{
				to = plugin.getIFHApi().getDefaultAccount(ouuid, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			}
			if(from == null || to == null)
			{
				return;
			}
			LoggerApi.addActionLogger(new ActionLogger(
					0,
					System.currentTimeMillis(),
					from.getID(), to.getID(),
					-1, OrdererType.PLAYER, cuuid, null, amount, amount, 0.0, category, comment));
			LoggerApi.addTrendLogger(LocalDate.now(), from.getID(), -amount, from.getBalance());
			LoggerApi.addTrendLogger(LocalDate.now(), to.getID(), amount, to.getBalance());
		}
	}

}
