package main.java.me.avankziar.aep.spigot.hook;

import java.time.LocalDate;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.aep.general.objects.ActionLogger;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.LoggerApi;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.action.OrdererType;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import net.brcdev.shopgui.event.ShopPostTransactionEvent;
import net.brcdev.shopgui.shop.ShopManager.ShopAction;
import net.brcdev.shopgui.shop.ShopTransactionResult.ShopTransactionResultType;

public class ShopGuiPlusHook implements Listener
{
	private AdvancedEconomyPlus plugin;
	
	public ShopGuiPlusHook(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTransaction(ShopPostTransactionEvent event)
	{
		if(event.getResult().getResult() != ShopTransactionResultType.SUCCESS)
		{
			return;
		}
		UUID cuuid = event.getResult().getPlayer().getUniqueId();
		double amount =  event.getResult().getPrice();
		ItemStack is = event.getResult().getShopItem().getItem();
		String itemId = "";
		if(is.hasItemMeta() && is.getItemMeta().hasDisplayName())
		{
			itemId = is.getItemMeta().getDisplayName();
		} else
		{
			itemId = event.getResult().getShopItem().getItem().getType().toString();
		}
        int itemQuantities = event.getResult().getAmount();
        String category = plugin.getYamlHandler().getLang().getString("ShopGuiPlusHook.Category");
		if(ShopAction.BUY == event.getResult().getShopAction())
		{
			String comment = plugin.getYamlHandler().getLang().getString("ShopGuiPlusHook.Buy")
					.replace("%amount%", String.valueOf(itemQuantities))
					.replace("%item%", itemId);
			Account from = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.SHOP, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			if(from == null)
			{
				from = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			}
			LoggerApi.addActionLogger(new ActionLogger(
					0,
					System.currentTimeMillis(),
					from.getID(), -1,
					-1, OrdererType.PLAYER, cuuid, null, amount, amount, 0.0, category, comment));
			LoggerApi.addTrendLogger(LocalDate.now(), from.getID(), -amount, from.getBalance());
		} else
		{
			String comment = plugin.getYamlHandler().getLang().getString("ShopGuiPlusHook.Sell")
					.replace("%amount%", String.valueOf(itemQuantities))
					.replace("%item%", itemId);
			
			Account to = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.SHOP, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			if(to == null)
			{
				to = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
			}
			LoggerApi.addActionLogger(new ActionLogger(
					0,
					System.currentTimeMillis(),
					-1, to.getID(),
					-1, OrdererType.PLAYER, cuuid, null, amount, amount, 0.0, category, comment));
			LoggerApi.addTrendLogger(LocalDate.now(), to.getID(), amount, to.getBalance());
		}
	}
}