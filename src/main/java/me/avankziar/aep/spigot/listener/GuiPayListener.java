package main.java.me.avankziar.aep.spigot.listener;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.PayThroughGui;
import main.java.me.avankziar.aep.spigot.object.subs.GuiPay;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

public class GuiPayListener implements Listener
{
	private AdvancedEconomyPlus plugin;
	public static LinkedHashMap<UUID, GuiPay> guiPayMap = new LinkedHashMap<>();
	
	public GuiPayListener(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event)
	{ 
		if(!(event.getWhoClicked() instanceof Player))
		{
			return;
		}
		if(event.getClickedInventory() == null)
		{
			return;
		}
		if(event.getClickedInventory().getType() != InventoryType.CHEST)
		{
			return;
		}
		if(event.getCurrentItem() == null)
		{
			return;
		}
		Player player = (Player) event.getWhoClicked();
		if(!guiPayMap.containsKey(player.getUniqueId()))
		{
			return;
		}
		event.setCancelled(true);
		event.setResult(Result.DENY);
		if(!event.getCurrentItem().hasItemMeta() || event.getCurrentItem().getItemMeta().getDisplayName() == null)
		{
			return;
		}
		String id = event.getCurrentItem().getItemMeta().getDisplayName();
		GuiPay gp = GuiPayListener.guiPayMap.get(player.getUniqueId());
		if(gp.getStep() == 1)
		{
			Account ac = plugin.getIFHApi().getAccount(Integer.parseInt(id));
			if(ac == null)
			{
				player.closeInventory();
				return;
			}
			gp.setFromAccountID(ac.getID());
			Account tax = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.TAX, ac.getCurrency());
			if(tax != null)
			{
				gp.setTaxAccountID(tax.getID());
			}
			gp.setStep(2);
			guiPayMap.put(player.getUniqueId(), gp);
			try
			{
				PayThroughGui.openPayThroughGui(player);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else
		{
			gp.setToAccountID(MatchApi.isInteger(id) ? Integer.parseInt(id) : 0);
			PayThroughGui.endPart(player, gp);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event)
	{
		if(!guiPayMap.containsKey(event.getPlayer().getUniqueId()))
		{
			return;
		}
		guiPayMap.remove(event.getPlayer().getUniqueId());
	}
}
