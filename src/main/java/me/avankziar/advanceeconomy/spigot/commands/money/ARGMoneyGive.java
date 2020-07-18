package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.general.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.BungeeBridge;
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

public class ARGMoneyGive extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyGive(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_GIVE,StringValues.PERM_CMD_MONEY_GIVE,
				AdvanceEconomy.moneyarguments,3,9999,StringValues.ARG_MONEY_GIVE_ALIAS,
				StringValues.MONEY_SUGGEST_GIVE);
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
		EcoPlayer toplayer = EcoPlayerHandler.getEcoPlayerFromName(toplayername);
		if(toplayer == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		EconomyResponse deposit = AdvanceEconomy.getVaultApi().depositPlayer(toplayer.getName(), amount);
		if(!deposit.transactionSuccess())
		{
			player.sendMessage(ChatApi.tl(deposit.errorMessage));
			return;
		}
		toplayer = EcoPlayerHandler.getEcoPlayerFromName(toplayername);
		Bukkit.getPluginManager().callEvent(new EconomyLoggerEvent(
				LocalDateTime.now(), "System", toplayer.getUUID(), "System", toplayer.getName(), player.getUniqueId().toString(),
				amount, EconomyLoggerEvent.Type.GIVEN, comment));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), toplayer.getUUID(), amount, toplayer.getBalance()));
		List<BaseComponent> list = new ArrayList<>();
		TextComponent message = ChatApi.apiChat(plugin.getYamlHandler().getL().getString(path+"Give.DepositWithDraw")
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
				.replace("%amount%", AdvanceEconomy.getVaultApi().format(amount))
				.replace("%name1%", "System")
				.replace("%name2%", toplayer.getName()), null, "", 
				HoverEvent.Action.SHOW_TEXT, 
				plugin.getYamlHandler().getL().getString(path+"Log.LoggerOrdererNote")
				.replace("%orderer%", player.getName())
				.replace("%comment%", comment));
		list.add(message);
		String messageII = ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Give.DepositWithDrawBalance")
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
				.replace("%name%", toplayer.getName())
				.replace("%balance%", AdvanceEconomy.getVaultApi().format(toplayer.getBalance())));
		boolean bungee = EconomySettings.settings.isBungee();
		player.spigot().sendMessage(message);
		player.sendMessage(messageII);
		if(toplayer != null)
		{
			if(toplayer.isMoneyPlayerFlow())
			{
				if(bungee)
				{
					BungeeBridge.sendBungeeTextComponent(player, toplayer.getUUID(), 
							BungeeBridge.generateMessage(list), false, "");
					BungeeBridge.sendBungeeMessage(player, toplayer.getUUID(), 
							messageII, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).spigot().sendMessage(message);
						Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).sendMessage(messageII);
					}
				}
			}
		}
		return;
	}
}