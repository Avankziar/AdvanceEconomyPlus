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
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGMoneySet extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneySet(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		String comment = "";
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			customOrderer = player.getUniqueId().toString();
		}
		if(!AEPSettings.settings.isPlayerAccount())
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
		AEPUser toplayer = AEPUserHandler.getEcoPlayer(toplayername);
		if(toplayer == null)
		{
			//Der Spieler existiert nicht!
			sender.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		toplayer = AEPUserHandler.getEcoPlayer(toplayername);
		toplayer.setBalance(amount);
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(), "System", toplayer.getUUID(), customTo, toplayer.getName(),
				customOrderer,
				amount, ActionLoggerEvent.Type.GIVEN, comment));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), toplayer.getUUID(), amount, toplayer.getBalance()));
		List<BaseComponent> list = new ArrayList<>();
		TextComponent message = ChatApi.apiChat(plugin.getYamlHandler().getL().getString("CmdMoney.Set.BalanceIsSet")
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%amount%", AdvancedEconomyPlus.getVault().format(amount))
				.replace("%name%", toplayer.getName()), null, "", 
				HoverEvent.Action.SHOW_TEXT, 
				plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerOrdererNote")
				.replace("%orderer%", customOrderer)
				.replace("%comment%", comment));
		list.add(message);
		boolean bungee = AEPSettings.settings.isBungee();
		sender.spigot().sendMessage(message);
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
					} else
					{
						if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) != null)
						{
							Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).spigot().sendMessage(message);
						}
					}
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).spigot().sendMessage(message);
					} //Without a player, the perform the command, a way around must be find to send to bungee.
				}
			}
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYER, toplayer, "`player_uuid` = ?", toplayer.getUUID());
		return;
	}
}