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
		Player player = (Player) sender;
		String fromplayername = args[1];
		String amountstring = args[2];
		double amount = 0.0;
		String orderer = player.getUniqueId().toString();
		String comment = "";
		if(!AEPSettings.settings.isPlayerAccount())
		{
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoPlayerAccount")));
			return;
		}
		if(MatchApi.isDouble(amountstring))
		{
			amount = Double.parseDouble(amountstring);
		} else
		{
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", amountstring)));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", amountstring)));
			return;
		}
		if(args.length >= 4)
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
		OLD_AEPUser fromplayer = _AEPUserHandler_OLD.getEcoPlayer(fromplayername);
		if(fromplayer == null)
		{
			//Der Spieler existiert nicht!
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		boolean has = AdvancedEconomyPlus.getVault().has(Bukkit.getOfflinePlayer(UUID.fromString(fromplayer.getUUID())), amount);
		if(has == false)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMoney.Take.NoFullAmount")
					.replace("%amount%", AdvancedEconomyPlus.getVault().format(amount))
					.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())));
			return;
		}
		EconomyResponse withdraw = AdvancedEconomyPlus.getVault().withdrawPlayer(fromplayer.getName(), amount);
		if(!withdraw.transactionSuccess())
		{
			sender.sendMessage(ChatApi.tl(withdraw.errorMessage));
			return;
		}
		fromplayer = _AEPUserHandler_OLD.getEcoPlayer(fromplayername);
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(), fromplayer.getUUID(), player.getUniqueId().toString(), fromplayer.getName(), player.getName(), orderer,
				amount, ActionLoggerEvent.Type.TAKEN, comment));		
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), fromplayer.getUUID(),
				-amount, fromplayer.getBalance()));
		List<BaseComponent> list = new ArrayList<>();
		TextComponent message = ChatApi.apiChat(
				plugin.getYamlHandler().getLang().getString("CmdMoney.Take.DepositWithDraw")
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%amount%", AdvancedEconomyPlus.getVault().format(amount))
				.replace("%name1%", fromplayer.getName())
				.replace("%name2%", player.getName()), null, "",
				HoverEvent.Action.SHOW_TEXT,
				plugin.getYamlHandler().getLang().getString("CmdMoney.Log.LoggerOrdererNote")
				.replace("%orderer%", orderer)
				.replace("%comment%", comment));
		list.add(message);
		String messageII = ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMoney.Take.DepositWithDrawBalance")
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%name%", fromplayer.getName())
				.replace("%balance%", AdvancedEconomyPlus.getVault().format(fromplayer.getBalance())));
		boolean bungee = AEPSettings.settings.isBungee();
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