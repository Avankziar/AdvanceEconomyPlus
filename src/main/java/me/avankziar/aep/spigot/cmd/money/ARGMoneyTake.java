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
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class ARGMoneyTake extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyTake(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		String fromplayername = args[1];
		String amountstring = args[2];
		double amount = 0.0;
		String customTo = args[3];
		String customOrderer = args[4];
		String comment = "";
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			customOrderer = player.getUniqueId().toString();
		}
		if(!EconomySettings.settings.isPlayerAccount())
		{
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		if(MatchApi.isDouble(amountstring))
		{
			amount = Double.parseDouble(amountstring);
		} else
		{
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", amountstring)));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", amountstring)));
			return;
		}
		if(args.length >= 6)
		{
			for(int i = 5; i < args.length; i++)
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
		AEPUser fromplayer = AEPUserHandler.getEcoPlayer(fromplayername);
		if(fromplayer == null)
		{
			//Der Spieler existiert nicht!
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		boolean has = AdvancedEconomyPlus.getVaultApi().has(Bukkit.getOfflinePlayer(UUID.fromString(fromplayer.getUUID())), amount);
		if(has == false)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Take.NoFullAmount")
					.replace("%amount%", AdvancedEconomyPlus.getVaultApi().format(amount))
					.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())));
			return;
		}
		EconomyResponse withdraw = AdvancedEconomyPlus.getVaultApi().withdrawPlayer(fromplayer.getName(), amount);
		if(!withdraw.transactionSuccess())
		{
			sender.sendMessage(ChatApi.tl(withdraw.errorMessage));
			return;
		}
		fromplayer = AEPUserHandler.getEcoPlayer(fromplayername);
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(), fromplayer.getUUID(), customTo, fromplayer.getName(), customTo, customOrderer,
				amount, ActionLoggerEvent.Type.TAKEN, comment));		
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), fromplayer.getUUID(),
				-amount, fromplayer.getBalance()));
		List<BaseComponent> list = new ArrayList<>();
		TextComponent message = ChatApi.apiChat(
				plugin.getYamlHandler().getL().getString("CmdMoney.Take.DepositWithDraw")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%amount%", AdvancedEconomyPlus.getVaultApi().format(amount))
				.replace("%name1%", fromplayer.getName())
				.replace("%name2%", customTo), null, "",
				HoverEvent.Action.SHOW_TEXT,
				plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerOrdererNote")
				.replace("%orderer%", customOrderer)
				.replace("%comment%", comment));
		list.add(message);
		String messageII = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Take.DepositWithDrawBalance")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%name%", fromplayer.getName())
				.replace("%balance%", AdvancedEconomyPlus.getVaultApi().format(fromplayer.getBalance())));
		boolean bungee = EconomySettings.settings.isBungee();
		sender.spigot().sendMessage(message);
		sender.sendMessage(messageII);
		if(fromplayer != null)
		{
			if(fromplayer.isMoneyPlayerFlow())
			{
				if(sender instanceof Player)
				{
					if(bungee)
					{
						Player player = (Player) sender;
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
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())).spigot().sendMessage(message);
						Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())).sendMessage(messageII);
					} //Without a player, the perform the command, a way around must be find to send to bungee.
				}
			}
		}
		return;
	}
}