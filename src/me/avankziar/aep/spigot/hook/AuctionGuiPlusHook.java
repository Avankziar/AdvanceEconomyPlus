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
import net.brcdev.auctiongui.auction.Auction;
import net.brcdev.auctiongui.auction.AuctionState;
import net.brcdev.auctiongui.event.AuctionEndEvent;

public class AuctionGuiPlusHook implements Listener
{
	private AEP plugin;
	
	public AuctionGuiPlusHook(AEP plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onAuctionEnd(AuctionEndEvent event)
	{
		Auction auction = event.getAuction();
		if(auction == null) return;
		if(auction.getState() != AuctionState.ENDED_SOLD) return;
		if(auction.getHighestBid() == null) return;
		if(auction.getItemStack() == null) return;
		UUID bidWinner = auction.getHighestBid().getBidderUuid();
		String bidWinnerName = auction.getHighestBidBidderName();
		double amount = auction.getHighestBid().getAmount();
		UUID auctioneer = auction.getOwnerUuid();
		ItemStack is = auction.getItemStack().clone();
		String itemId = "";
		if(is.hasItemMeta() && is.getItemMeta().hasDisplayName())
		{
			itemId = is.getItemMeta().getDisplayName();
		} else
		{
			itemId = is.getType().toString();
		}
		String category = plugin.getYamlHandler().getLang().getString("AuctionGuiPlusHook.Category");
		String comment = plugin.getYamlHandler().getLang().getString("AuctionGuiPlusHook.Buy")
				.replace("%item%", itemId)
				.replace("%player%", bidWinnerName);
		Account from = plugin.getIFHApi().getDefaultAccount(bidWinner, AccountCategory.SHOP, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
		if(from == null)
		{
			from = plugin.getIFHApi().getDefaultAccount(bidWinner, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
		}
		Account to = plugin.getIFHApi().getDefaultAccount(auctioneer, AccountCategory.SHOP, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
		if(to == null)
		{
			to = plugin.getIFHApi().getDefaultAccount(auctioneer, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
		}
		if(from == null || to == null)
		{
			return;
		}
		LoggerApi.addActionLogger(new ActionLogger(
				0,
				System.currentTimeMillis(),
				from.getID(), to.getID(),
				-1, OrdererType.PLAYER, bidWinner, null, amount, amount, 0.0, category, comment));
		LoggerApi.addTrendLogger(LocalDate.now(), from.getID(), -amount, from.getBalance());
		LoggerApi.addTrendLogger(LocalDate.now(), to.getID(), amount, to.getBalance());
	}
}