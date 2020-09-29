package main.java.me.avankziar.aep.spigot.cmd.money;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class ARGMoneyPay extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyPay(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
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
					plugin.getYamlHandler().getL().getString("CmdMoney.Freeze.YourAccountIsFrozen")));
			return;
		}
		if(!AdvancedEconomyPlus.getVaultApi().has(player, amount))
		{
			//&f%amount% &cübersteigt dein Guthaben!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Pay.NotEnoughBalance")
					.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
					.replace("%amount%", amountstring)));
			return;
		}
		if(player.getName().equals(toplayername))
		{
			//&7Du steckst das Geld von der einen Taschen in die anderen!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Pay.SelfPay")
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
					plugin.getYamlHandler().getL().getString("CmdMoney.Freeze.TheAccountIsFrozen")));
			return;
		}
		EconomyResponse withdraw = AdvancedEconomyPlus.getVaultApi().withdrawPlayer(player, amount);
		if(!withdraw.transactionSuccess())
		{
			player.sendMessage(withdraw.errorMessage);
			return;
		}
		EconomyResponse deposit = AdvancedEconomyPlus.getVaultApi().depositPlayer(toplayer.getName(), amount);
		if(!deposit.transactionSuccess())
		{
			AdvancedEconomyPlus.getVaultApi().depositPlayer(player, amount);
			player.sendMessage(deposit.errorMessage);
			return;
		}
		eco = EcoPlayerHandler.getEcoPlayer(player);
		toplayer = EcoPlayerHandler.getEcoPlayerFromName(toplayername);
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(), player.getUniqueId().toString(), toplayer.getUUID(),
				player.getName(), toplayer.getName(), player.getUniqueId().toString(), amount, 
				ActionLoggerEvent.Type.DEPOSIT_WITHDRAW, comment));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
				LocalDate.now(), eco.getUUID(), -amount, eco.getBalance()));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), toplayer.getUUID(), amount, toplayer.getBalance()));
		TextComponent frommessage = ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdMoney.Pay.DepositWithDraw")
				.replace("%amount%", AdvancedEconomyPlus.getVaultApi().format(amount))
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%name1%", player.getName())
				.replace("%name2%", toplayer.getName()));
		ChatApi.hoverEvent(frommessage, HoverEvent.Action.SHOW_TEXT, 
				plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerOrdererNote")
				.replace("%orderer%", player.getName())
				.replace("%comment%", comment));
		String frommessageII = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Pay.DepositWithDrawBalance")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%name%", player.getName())
				.replace("%balance%", AdvancedEconomyPlus.getVaultApi().format(eco.getBalance())));
		List<BaseComponent> list = new ArrayList<>();
		TextComponent toomessage = ChatApi.apiChat(plugin.getYamlHandler().getL().getString("CmdMoney.Pay.DepositWithDraw")
				.replace("%amount%", AdvancedEconomyPlus.getVaultApi().format(amount))
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%name1%", player.getName())
				.replace("%name2%", toplayer.getName()), null, "",
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerOrdererNote")
				.replace("%orderer%", player.getName())
				.replace("%comment%", comment));
		list.add(toomessage);
		String toomessageII = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Pay.DepositWithDrawBalance")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%name%", toplayer.getName())
				.replace("%balance%", AdvancedEconomyPlus.getVaultApi().format(toplayer.getBalance())));
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