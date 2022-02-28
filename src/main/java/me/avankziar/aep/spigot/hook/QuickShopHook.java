package main.java.me.avankziar.aep.spigot.hook;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.maxgamer.quickshop.event.ShopSuccessPurchaseEvent;
import org.maxgamer.quickshop.shop.Shop;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.LoggerApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;
import main.java.me.avankziar.ifh.spigot.economy.currency.CurrencyType;

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
		UUID cuuid = player.getUniqueId();
		String cname = player.getName();
		UUID ouuid = shop.getOwner();
		String oname = Utility.convertUUIDToName(ouuid.toString(), EconomyType.PLAYER);
		if(oname == null)
		{
			ouuid = plugin.getIFHApi().getDefaultServer().getUUID();
			oname = plugin.getIFHApi().getDefaultServer().getName();
		}
        
		int itemQuantities = event.getAmount();
		double amount = event.getBalance();
		ItemStack is = shop.getItem();
		String itemId = null;
		if(is.hasItemMeta())
		{
			ItemMeta im = is.getItemMeta();
			if(im.hasDisplayName())
			{
				itemId = im.getDisplayName();
			}
		}
		if(itemId == null)
		{
			itemId = is.getType().toString();
		}
		String category = plugin.getYamlHandler().getLang().getString("QuickShopHook.Category");

		if(shop.isBuying())
		{
			String comment = plugin.getYamlHandler().getLang().getString("QuickShopHook.Buy")
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
		} else
		{
			String comment = plugin.getYamlHandler().getLang().getString("QuickShopHook.Sell")
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
		}
	}

}
