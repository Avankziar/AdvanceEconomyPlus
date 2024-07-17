package me.avankziar.aep.spigot.listener;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.cst.transaction.PayThroughGui;
import me.avankziar.aep.spigot.object.subs.GuiPay;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.spigot.economy.account.Account;

public class GuiPayListener implements Listener
{
	private AEP plugin;
	public static LinkedHashMap<String, GuiPay> guiPayMap = new LinkedHashMap<>();
	
	public GuiPayListener(AEP plugin)
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
		final Player player = (Player) event.getWhoClicked();
		if(!GuiPayListener.guiPayMap.containsKey(player.getUniqueId().toString()))
		{
			return;
		}
		if(!event.getCurrentItem().hasItemMeta())
		{
			GuiPayListener.guiPayMap.remove(player.getUniqueId().toString());
			return;
		}
		if(!event.getCurrentItem().getItemMeta().hasDisplayName())
		{
			GuiPayListener.guiPayMap.remove(player.getUniqueId().toString());
			return;
		}
		if(!MatchApi.isInteger(event.getCurrentItem().getItemMeta().getDisplayName()))
		{
			GuiPayListener.guiPayMap.remove(player.getUniqueId().toString());
			return;
		}
		event.setCancelled(true);
		event.setResult(Result.DENY);
		String id = event.getCurrentItem().getItemMeta().getDisplayName();
		GuiPay gp = GuiPayListener.guiPayMap.get(player.getUniqueId().toString());
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
			GuiPayListener.guiPayMap.put(player.getUniqueId().toString(), gp);
			player.closeInventory();
			try
			{
				PayThroughGui.openPayThroughGui(player, false);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			return;
		} else
		{
			gp.setToAccountID(MatchApi.isInteger(id) ? Integer.parseInt(id) : 0);
			player.closeInventory();
			PayThroughGui.endPart(player, gp);
		}
	}
}
