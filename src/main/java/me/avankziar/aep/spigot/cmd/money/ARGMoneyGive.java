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

public class ARGMoneyGive extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyGive(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		String toplayername = args[1];
		String amountstring = args[2];
		double amount = 0.0;
		String customTo = args[3];
		String customOrderer = args[4];
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			customOrderer = player.getUniqueId().toString();
		}
		String comment = "";
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
		EcoPlayer toplayer = EcoPlayerHandler.getEcoPlayerFromName(toplayername);
		if(toplayer == null)
		{
			//Der Spieler existiert nicht!
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		EconomyResponse deposit = AdvancedEconomyPlus.getVaultApi().depositPlayer(toplayer.getName(), amount);
		if(!deposit.transactionSuccess())
		{
			sender.sendMessage(ChatApi.tl(deposit.errorMessage));
			return;
		}
		toplayer = EcoPlayerHandler.getEcoPlayerFromName(toplayername);
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(), customTo, toplayer.getUUID(), customTo, toplayer.getName(), 
				customOrderer,
				amount, ActionLoggerEvent.Type.GIVEN, comment));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), toplayer.getUUID(), amount, toplayer.getBalance()));
		List<BaseComponent> list = new ArrayList<>();
		TextComponent message = ChatApi.apiChat(plugin.getYamlHandler().getL().getString("CmdMoney.Give.DepositWithDraw")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%amount%", AdvancedEconomyPlus.getVaultApi().format(amount))
				.replace("%name1%", customTo)
				.replace("%name2%", toplayer.getName()), null, "", 
				HoverEvent.Action.SHOW_TEXT, 
				plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerOrdererNote")
				.replace("%orderer%", customOrderer)
				.replace("%comment%", comment));
		list.add(message);
		String messageII = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Give.DepositWithDrawBalance")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%name%", toplayer.getName())
				.replace("%balance%", AdvancedEconomyPlus.getVaultApi().format(toplayer.getBalance())));
		boolean bungee = EconomySettings.settings.isBungee();
		sender.spigot().sendMessage(message);
		sender.sendMessage(messageII);
		if(toplayer != null)
		{
			if(toplayer.isMoneyPlayerFlow())
			{
				if(sender instanceof Player)
				{
					if(bungee)
					{
						Player player = (Player) sender;
						BungeeBridge.sendBungeeTextComponent(player, toplayer.getUUID(), BungeeBridge.generateMessage(list), false, "");
						BungeeBridge.sendBungeeMessage(player, toplayer.getUUID(), messageII, false, "");
					} else
					{
						if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) != null)
						{
							Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).spigot().sendMessage(message);
							Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).sendMessage(messageII);
						}
					}
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).spigot().sendMessage(message);
						Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).sendMessage(messageII);
					} //Without a player, the perform the command, a way around must be find to send to bungee.
				}
			}
		}
		return;
	}
}