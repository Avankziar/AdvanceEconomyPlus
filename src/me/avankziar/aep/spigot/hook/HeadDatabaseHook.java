package me.avankziar.aep.spigot.hook;

import java.time.LocalDate;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.avankziar.aep.general.objects.ActionLogger;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.api.LoggerApi;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.arcaniax.hdb.api.PlayerClickHeadEvent;

public class HeadDatabaseHook implements Listener
{
	private AEP plugin;
	
	public HeadDatabaseHook(AEP plugin)
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
		UUID cuuid = event.getPlayer().getUniqueId();
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
		String category = plugin.getYamlHandler().getLang().getString("HeadDatabase.Category");
		String comment = plugin.getYamlHandler().getLang().getString("HeadDatabase.Comment")
				.replace("%head%", itemname);
		Account from = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
		Account to = plugin.getIFHApi().getDefaultAccount(cuuid, AccountCategory.VOID);
		if(to == null && plugin.getIFHApi().getDefaultServer() != null)
		{
			to = plugin.getIFHApi().getDefaultAccount(plugin.getIFHApi().getDefaultServer().getUUID(), AccountCategory.VOID);
		}
		if(from == null && to == null)
		{
			return;
		} else if(from != null && to != null)
		{
			LoggerApi.addActionLogger(new ActionLogger(
					0,
					System.currentTimeMillis(),
					from.getID(), to.getID(), 0, 
					OrdererType.PLAYER, cuuid, null, amount, amount, 0.0, category, comment));
			LoggerApi.addTrendLogger(LocalDate.now(), from.getID(), -amount, from.getBalance());
			LoggerApi.addTrendLogger(LocalDate.now(), to.getID(), amount, to.getBalance());
		} else if(from != null && to == null)
		{
			LoggerApi.addActionLogger(new ActionLogger(
					0,
					System.currentTimeMillis(),
					from.getID(), 0, 0,
					OrdererType.PLAYER, cuuid, null, amount, amount, 0.0, category, comment));
			LoggerApi.addTrendLogger(LocalDate.now(), from.getID(), -amount, from.getBalance());
		}
	}
}
