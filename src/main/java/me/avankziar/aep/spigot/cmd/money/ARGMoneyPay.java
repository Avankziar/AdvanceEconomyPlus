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
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
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
		if(!AEPSettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoPlayerAccount")));
			return;
		}
		if(MatchApi.isDouble(amountstring))
		{
			amount = Double.parseDouble(amountstring);
		} else
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", amountstring)));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
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
		OLD_AEPUser eco = _AEPUserHandler_OLD.getEcoPlayer(player);
		if(eco.isFrozen())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdMoney.Freeze.YourAccountIsFrozen")));
			return;
		}
		if(!AdvancedEconomyPlus.getVault().has(player, amount))
		{
			//&f%amount% &cübersteigt dein Guthaben!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdMoney.Pay.NotEnoughBalance")
					.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
					.replace("%amount%", amountstring)));
			return;
		}
		if(player.getName().equals(toplayername))
		{
			//&7Du steckst das Geld von der einen Taschen in die anderen!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdMoney.Pay.SelfPay")
					.replace("%amount%", amountstring)));
			return;
		}
		OLD_AEPUser toplayer = _AEPUserHandler_OLD.getEcoPlayer(toplayername);
		if(toplayer == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(toplayer.isFrozen())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdMoney.Freeze.TheAccountIsFrozen")));
			return;
		}
		EconomyResponse withdraw = AdvancedEconomyPlus.getVault().withdrawPlayer(player, amount);
		if(!withdraw.transactionSuccess())
		{
			player.sendMessage(ChatApi.tl(withdraw.errorMessage));
			return;
		}
		EconomyResponse deposit = AdvancedEconomyPlus.getVault().depositPlayer(toplayer.getName(), amount);
		if(!deposit.transactionSuccess())
		{
			AdvancedEconomyPlus.getVault().depositPlayer(player, amount);
			player.sendMessage(ChatApi.tl(deposit.errorMessage));
			return;
		}
		eco = _AEPUserHandler_OLD.getEcoPlayer(player);
		toplayer = _AEPUserHandler_OLD.getEcoPlayer(toplayername);
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(), player.getUniqueId().toString(), toplayer.getUUID(),
				player.getName(), toplayer.getName(), player.getUniqueId().toString(), amount, 
				ActionLoggerEvent.Type.DEPOSIT_WITHDRAW, comment));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
				LocalDate.now(), eco.getUUID(), -amount, eco.getBalance()));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), toplayer.getUUID(), amount, toplayer.getBalance()));
		TextComponent frommessage = ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMoney.Pay.DepositWithDraw")
				.replace("%amount%", AdvancedEconomyPlus.getVault().format(amount))
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%name1%", player.getName())
				.replace("%name2%", toplayer.getName()));
		ChatApi.hoverEvent(frommessage, HoverEvent.Action.SHOW_TEXT, 
				plugin.getYamlHandler().getLang().getString("CmdMoney.Log.LoggerOrdererNote")
				.replace("%orderer%", player.getName())
				.replace("%comment%", comment));
		String frommessageII = ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMoney.Pay.DepositWithDrawBalance")
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%name%", player.getName())
				.replace("%balance%", AdvancedEconomyPlus.getVault().format(eco.getBalance())));
		List<BaseComponent> list = new ArrayList<>();
		TextComponent toomessage = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMoney.Pay.DepositWithDraw")
				.replace("%amount%", AdvancedEconomyPlus.getVault().format(amount))
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%name1%", player.getName())
				.replace("%name2%", toplayer.getName()), null, "",
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("CmdMoney.Log.LoggerOrdererNote")
				.replace("%orderer%", player.getName())
				.replace("%comment%", comment));
		list.add(toomessage);
		String toomessageII = ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMoney.Pay.DepositWithDrawBalance")
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%name%", toplayer.getName())
				.replace("%balance%", AdvancedEconomyPlus.getVault().format(toplayer.getBalance())));
		boolean bungee = AEPSettings.settings.isBungee();
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