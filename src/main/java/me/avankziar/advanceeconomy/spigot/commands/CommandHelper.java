package main.java.me.avankziar.advanceeconomy.spigot.commands;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandHelper
{
	private AdvanceEconomy plugin;
	private String econ = "CmdEco.";
	private String money = "CmdMoney.";
	//private String bank = "CmdBank.";

	public CommandHelper(AdvanceEconomy plugin)
	{
		this.plugin = plugin;
	}
	
	private void sendInfo(Player player, CommandModule module, String path)
	{
		player.spigot().sendMessage(ChatApi.apiChat(
				plugin.getYamlHandler().getL().getString(econ+"Info."+path+module.argument),
				ClickEvent.Action.SUGGEST_COMMAND, module.commandSuggest,
				HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
	}

	public void eco(Player player, int page)
	{
		int count = 0;
		int start = page*10;
		int end = page*10+6;
		int last = 0;
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(econ+"Info.Headline")));
		if(count >= start && count <= end)
		{
			player.spigot().sendMessage(ChatApi.apiChat(
					plugin.getYamlHandler().getL().getString(econ+"Info.econ.econ"),
					ClickEvent.Action.SUGGEST_COMMAND, "/econ",
					HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
		}
		count++;
		last++;
		for(String argument : AdvanceEconomy.ecoarguments.keySet())
		{
			if(argument.equals(AdvanceEconomy.ecoarguments.get(argument).argument))
			{
				if(count >= start && count <= end)
				{
					if(player.hasPermission(AdvanceEconomy.ecoarguments.get(argument).permission))
					{
						sendInfo(player, AdvanceEconomy.ecoarguments.get(argument), "econ.");
					}
				}
				count++;
				last++;
			}
		}
		if(count >= start && count <= end)
		{
			if(player.hasPermission(StringValues.PERM_CMD_MONEY))
			{
				player.spigot().sendMessage(ChatApi.apiChat(
						plugin.getYamlHandler().getL().getString(econ+"Info.money.money"),
						ClickEvent.Action.SUGGEST_COMMAND, "/money",
						HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
			}
		}
		count++;
		last++;
		for(String argument : AdvanceEconomy.moneyarguments.keySet())
		{
			if(argument.equals(AdvanceEconomy.moneyarguments.get(argument).argument))
			{
				if(count >= start && count <= end)
				{
					if(player.hasPermission(AdvanceEconomy.moneyarguments.get(argument).permission))
					{
						sendInfo(player, AdvanceEconomy.moneyarguments.get(argument), "money.");
					}
				}
				count++;
				last++;
			}
		}
		if(count >= start && count <= end)
		{
			if(player.hasPermission(StringValues.PERM_CMD_BANK))
			{
				player.spigot().sendMessage(ChatApi.apiChat(
						plugin.getYamlHandler().getL().getString(econ+"Info.bank.bank"),
						ClickEvent.Action.SUGGEST_COMMAND, "/bank",
						HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
			}
		}
		count++;
		last++;
		for(String argument : AdvanceEconomy.bankarguments.keySet())
		{
			if(argument.equals(AdvanceEconomy.bankarguments.get(argument).argument))
			{
				if(count >= start && count <= end)
				{
					if(player.hasPermission(AdvanceEconomy.bankarguments.get(argument).permission))
					{
						sendInfo(player, AdvanceEconomy.bankarguments.get(argument), "bank.");
					}
				}
				count++;
				last++;
			}
			
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		pastNext(player, page, lastpage, econ);
	}
	
	public void money(Player player)
	{
		if(!player.hasPermission(StringValues.PERM_CMD_MONEY))
		{
			player.sendMessage(plugin.getYamlHandler().getL().getString("NoPermission"));
			return;
		}
		EcoPlayer eco = EcoPlayer.getEcoPlayer(player);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(money+"PlayerBalance")
				.replace("%time%", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
				.replace("%balance%", AdvanceEconomy.getVaultApi().format(eco.getBalance()))
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())));
		return;
	}
	
	public void bank(Player player)
	{
		
	}
	
	private void pastNext(Player player, int page, boolean lastpage, String path)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		TextComponent MSG = ChatApi.tctl("");
		List<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+"Info.Past"));
			String cmd = "/econ "+String.valueOf(j);
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+"Info.Next"));
			String cmd = "/econ "+String.valueOf(i);
			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
		return;
	}
}
