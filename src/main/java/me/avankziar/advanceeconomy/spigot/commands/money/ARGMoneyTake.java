package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class ARGMoneyTake extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyTake(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_TAKE,StringValues.PERM_CMD_MONEY_TAKE,
				AdvanceEconomy.moneyarguments,3,9999,StringValues.ARG_MONEY_TAKE_ALIAS,
				StringValues.MONEY_SUGGEST_TAKE);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
		String fromplayername = args[1];
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
		EcoPlayer fromplayer = EcoPlayer.getEcoPlayerFromName(fromplayername);
		if(fromplayer == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		boolean has = AdvanceEconomy.getVaultApi().has(Bukkit.getOfflinePlayer(UUID.fromString(fromplayer.getUUID())), amount);
		if(has == false)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Take.NoFullAmount")
					.replace("%amount%", AdvanceEconomy.getVaultApi().format(amount))
					.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())));
			return;
		}
		EconomyResponse withdraw = AdvanceEconomy.getVaultApi().withdrawPlayer(fromplayer.getName(), amount);
		if(!withdraw.transactionSuccess())
		{
			player.sendMessage(withdraw.errorMessage);
			return;
		}
		fromplayer = EcoPlayer.getEcoPlayerFromName(fromplayername);
		Bukkit.getPluginManager().callEvent(new EconomyLoggerEvent(
				LocalDateTime.now(), fromplayer.getUUID(), "System", fromplayer.getName(), "System", player.getUniqueId().toString(),
				amount, EconomyLoggerEvent.Type.TAKEN, comment));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), fromplayer.getUUID(),
				-amount, fromplayer.getBalance()));
		List<BaseComponent> list = new ArrayList<>();
		TextComponent message = ChatApi.apiChat(
				plugin.getYamlHandler().getL().getString(path+"Take.DepositWithDraw")
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
				.replace("%amount%", AdvanceEconomy.getVaultApi().format(amount))
				.replace("%name1%", fromplayer.getName())
				.replace("%name2%", "System"), null, "",
				HoverEvent.Action.SHOW_TEXT,
				plugin.getYamlHandler().getL().getString(path+"Log.LoggerOrdererNote")
				.replace("%orderer%", player.getName())
				.replace("%comment%", comment));
		list.add(message);
		String messageII = ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Take.DepositWithDrawBalance")
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
				.replace("%name%", fromplayer.getName())
				.replace("%balance%", AdvanceEconomy.getVaultApi().format(fromplayer.getBalance())));
		boolean bungee = EconomySettings.settings.isBungee();
		player.spigot().sendMessage(message);
		player.sendMessage(messageII);
		if(fromplayer != null)
		{
			if(fromplayer.isMoneyPlayerFlow())
			{
				if(bungee)
				{
					BungeeBridge.sendBungeeTextComponent(player, fromplayer.getUUID(), BungeeBridge.generateMessage(list), false, "");
					BungeeBridge.sendBungeeMessage(player, fromplayer.getUUID(), messageII, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())).spigot().sendMessage(message);
						Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())).sendMessage(messageII);
					}
				}
			}
		}
		return;
	}
}