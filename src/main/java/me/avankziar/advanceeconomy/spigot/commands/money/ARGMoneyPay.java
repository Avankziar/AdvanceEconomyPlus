package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class ARGMoneyPay extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyPay(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_PAY,StringValues.PERM_CMD_MONEY_PAY,
				AdvanceEconomy.moneyarguments,3,9999,StringValues.ARG_MONEY_PAY_ALIAS,
				StringValues.MONEY_SUGGEST_PAY);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
		String toplayername = args[1];
		String amountstring = args[2];
		double amount = 0.0;
		String comment = "";
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		if(MatchApi.isDouble(amountstring))
		{
			amount = Double.parseDouble(amountstring);
		} else
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", amountstring)));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", amountstring)));
			return;
		}
		if(args.length >= 3)
		{
			for(int i = 3; i < args.length; i++)
			{
				if(i == args.length-1)
				{
					comment += args[i];
				} else
				{
					comment += args[i]+" ";
				}
				
			}
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(player);
		if(eco.isFrozen())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Freeze.YourAccountIsFrozen")));
			return;
		}
		if(!AdvanceEconomy.getVaultApi().has(player, amount))
		{
			//&f%amount% &cübersteigt dein Guthaben!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Pay.NotEnoughBalance")
					.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
					.replace("%amount%", amountstring)));
			return;
		}
		if(player.getName().equals(toplayername))
		{
			//&7Du steckst das Geld von der einen Taschen in die anderen!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Pay.SelfPay")
					.replace("%amount%", amountstring)));
			return;
		}
		EcoPlayer toplayer = EcoPlayerHandler.getEcoPlayerFromName(toplayername);
		if(toplayer == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		if(toplayer.isFrozen())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Freeze.TheAccountIsFrozen")));
			return;
		}
		EconomyResponse withdraw = AdvanceEconomy.getVaultApi().withdrawPlayer(player, amount);
		if(!withdraw.transactionSuccess())
		{
			player.sendMessage(withdraw.errorMessage);
			return;
		}
		EconomyResponse deposit = AdvanceEconomy.getVaultApi().depositPlayer(toplayer.getName(), amount);
		if(!deposit.transactionSuccess())
		{
			AdvanceEconomy.getVaultApi().depositPlayer(player, amount);
			player.sendMessage(deposit.errorMessage);
			return;
		}
		eco = EcoPlayerHandler.getEcoPlayer(player);
		toplayer = EcoPlayerHandler.getEcoPlayerFromName(toplayername);
		Bukkit.getPluginManager().callEvent(new EconomyLoggerEvent(
				LocalDateTime.now(), player.getUniqueId().toString(), toplayer.getUUID(),
				player.getName(), toplayer.getName(), player.getUniqueId().toString(), amount, 
				EconomyLoggerEvent.Type.DEPOSIT_WITHDRAW, comment));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
				LocalDate.now(), eco.getUUID(), -amount, eco.getBalance()));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), toplayer.getUUID(), amount, toplayer.getBalance()));
		TextComponent frommessage = ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Pay.DepositWithDraw")
				.replace("%amount%", AdvanceEconomy.getVaultApi().format(amount))
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
				.replace("%name1%", player.getName())
				.replace("%name2%", toplayer.getName()));
		ChatApi.hoverEvent(frommessage, HoverEvent.Action.SHOW_TEXT, 
				plugin.getYamlHandler().getL().getString(path+"Log.LoggerOrdererNote")
				.replace("%orderer%", player.getName())
				.replace("%comment%", comment));
		String frommessageII = ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Pay.DepositWithDrawBalance")
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
				.replace("%name%", player.getName())
				.replace("%balance%", AdvanceEconomy.getVaultApi().format(eco.getBalance())));
		List<BaseComponent> list = new ArrayList<>();
		TextComponent toomessage = ChatApi.apiChat(plugin.getYamlHandler().getL().getString(path+"Pay.DepositWithDraw")
				.replace("%amount%", AdvanceEconomy.getVaultApi().format(amount))
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
				.replace("%name1%", player.getName())
				.replace("%name2%", toplayer.getName()), null, "",
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(path+"Log.LoggerOrdererNote")
				.replace("%orderer%", player.getName())
				.replace("%comment%", comment));
		list.add(toomessage);
		String toomessageII = ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Pay.DepositWithDrawBalance")
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
				.replace("%name%", toplayer.getName())
				.replace("%balance%", AdvanceEconomy.getVaultApi().format(toplayer.getBalance())));
		boolean bungee = EconomySettings.settings.isBungee();
		player.spigot().sendMessage(frommessage);
		player.sendMessage(frommessageII);
		if(toplayer.isMoneyPlayerFlow())
		{
			if(bungee)
			{
				BungeeBridge.sendBungeeTextComponent(player, toplayer.getUUID(),
						BungeeBridge.generateMessage(list), false, "");
				BungeeBridge.sendBungeeMessage(player, toplayer.getUUID(), toomessageII, false, "");
			} else
			{
				if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) != null)
				{
					Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).spigot().sendMessage(toomessage);
					Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).sendMessage(toomessageII);
				}
			}
		}
		toplayer = null;
		return;
	}
}