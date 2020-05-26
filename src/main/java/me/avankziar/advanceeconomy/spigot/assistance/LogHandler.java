package main.java.me.avankziar.advanceeconomy.spigot.assistance;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomyLogger;
import main.java.me.avankziar.advanceeconomy.spigot.object.TrendLogger;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LogHandler
{
	public static void pastNextPage(AdvanceEconomy plugin, Player player, String path, String playername,
			int page, boolean lastpage, String cmdstring)
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
					plugin.getYamlHandler().getL().getString(path+"Log.Past"));
			String cmd = cmdstring+" "+String.valueOf(j)+" "+playername;
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+"Log.Next"));
			String cmd = cmdstring+" "+String.valueOf(i)+" "+playername;
			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}
	
	public static void sendLogs(AdvanceEconomy plugin, Player player, EcoPlayer eco, String path, ArrayList<EconomyLogger> list,
			int page, boolean lastpage, String playername, int last, String cmdstring)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Log.Headline")
				.replace("%name%", eco.getName())
				.replace("%amount%", String.valueOf(last))));
		for(EconomyLogger el : list)
		{
			String orderer = "";
			EcoPlayer ord = EcoPlayer.getEcoPlayer(el.getOrdereruuid());
			if(ord == null)
			{
				orderer = el.getOrdereruuid();
			} else
			{
				orderer = EcoPlayer.getEcoPlayer(el.getOrdereruuid()).getName();
			}
			String comment = "";
			if(el.getComment() != null)
			{
				comment = el.getComment();
			}
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("%orderer%", orderer);
			map.put("%comment%", comment);
			if(MatchApi.isBankAccountNumber(el.getFromUUIDOrNumber()))
			{
				if(player.hasPermission(StringValues.PERM_CMD_ECO_DELETELOG) && 
						(el.getOrdereruuid().equals(player.getUniqueId().toString()) 
								|| player.hasPermission(StringValues.PERM_BYPASS_RECOMMENT)))
				{
					player.spigot().sendMessage(ChatApi.generateTextComponent(
							plugin.getYamlHandler().getL().getString(path+"Log.LoggerBToPPositivAll")
							.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
							.replace("%fromnumber%", el.getFromUUIDOrNumber()).replace("%fromname%", el.getFromName())
							.replace("%toname%", el.getToName())
							.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
							.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
							.replace("%id%", String.valueOf(el.getId())),
							map));
				} else if(player.hasPermission(StringValues.PERM_CMD_ECO_DELETELOG))
				{
					player.spigot().sendMessage(ChatApi.generateTextComponent(
							plugin.getYamlHandler().getL().getString(path+"Log.LoggerBToPPositivDelete")
							.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
							.replace("%fromnumber%", el.getFromUUIDOrNumber()).replace("%fromname%", el.getFromName())
							.replace("%toname%", el.getToName())
							.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
							.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
							.replace("%id%", String.valueOf(el.getId())),
							map));
				} else if(el.getOrdereruuid().equals(player.getUniqueId().toString()) 
						|| player.hasPermission(StringValues.PERM_BYPASS_RECOMMENT))
				{
					player.spigot().sendMessage(ChatApi.generateTextComponent(
							plugin.getYamlHandler().getL().getString(path+"Log.LoggerBToPPositivRe")
							.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
							.replace("%fromnumber%", el.getFromUUIDOrNumber()).replace("%fromname%", el.getFromName())
							.replace("%toname%", el.getToName())
							.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
							.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
							.replace("%id%", String.valueOf(el.getId())),
							map));
				} else
				{
					player.spigot().sendMessage(ChatApi.generateTextComponent(
							plugin.getYamlHandler().getL().getString(path+"Log.LoggerBToPPositiv")
							.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
							.replace("%fromnumber%", el.getFromUUIDOrNumber()).replace("%fromname%", el.getFromName())
							.replace("%toname%", el.getToName())
							.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
							.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
							.replace("%id%", String.valueOf(el.getId())),
							map));
				}
			} else //isUUID
			{
				if(MatchApi.isBankAccountNumber(el.getToUUIDOrNumber()))
				{
					if(player.hasPermission(StringValues.PERM_CMD_ECO_DELETELOG) && 
							(el.getOrdereruuid().equals(player.getUniqueId().toString()) 
									|| player.hasPermission(StringValues.PERM_BYPASS_RECOMMENT)))
					{
						player.spigot().sendMessage(ChatApi.generateTextComponent(
								plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToBNegativAll")
								.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
								.replace("%fromname%", el.getFromName())
								.replace("%tonumber%", el.getToName()).replace("%toname%", el.getToName())
								.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
								.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
								.replace("%id%", String.valueOf(el.getId())),
								map));
					} else if(player.hasPermission(StringValues.PERM_CMD_ECO_DELETELOG))
					{
						player.spigot().sendMessage(ChatApi.generateTextComponent(
								plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToBNegativDelete")
								.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
								.replace("%fromname%", el.getFromName())
								.replace("%tonumber%", el.getToName()).replace("%toname%", el.getToName())
								.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
								.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
								.replace("%id%", String.valueOf(el.getId())),
								map));
					} else if(el.getOrdereruuid().equals(player.getUniqueId().toString()) 
							|| player.hasPermission(StringValues.PERM_BYPASS_RECOMMENT))
					{
						player.spigot().sendMessage(ChatApi.generateTextComponent(
								plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToBNegativRe")
								.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
								.replace("%fromname%", el.getFromName())
								.replace("%tonumber%", el.getToName()).replace("%toname%", el.getToName())
								.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
								.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
								.replace("%id%", String.valueOf(el.getId())),
								map));
					} else
					{
						player.spigot().sendMessage(ChatApi.generateTextComponent(
								plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToBNegativ")
								.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
								.replace("%fromname%", el.getFromName())
								.replace("%tonumber%", el.getToName()).replace("%toname%", el.getToName())
								.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
								.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
								.replace("%id%", String.valueOf(el.getId())),
								map));
					}
				} else //isUUID
				{
					if(el.getFromUUIDOrNumber().equals(eco.getUUID()))
					{
						if(player.hasPermission(StringValues.PERM_CMD_ECO_DELETELOG) && 
								(el.getOrdereruuid().equals(player.getUniqueId().toString()) 
										|| player.hasPermission(StringValues.PERM_BYPASS_RECOMMENT)))
						{
							player.spigot().sendMessage(ChatApi.generateTextComponent(
									plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToPNegativAll")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
									.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else if(player.hasPermission(StringValues.PERM_CMD_ECO_DELETELOG))
						{
							player.spigot().sendMessage(ChatApi.generateTextComponent(
									plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToPNegativDelete")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
									.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else if(el.getOrdereruuid().equals(player.getUniqueId().toString()) 
								|| player.hasPermission(StringValues.PERM_BYPASS_RECOMMENT))
						{
							player.spigot().sendMessage(ChatApi.generateTextComponent(
									plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToPNegativRe")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
									.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else
						{
							player.spigot().sendMessage(ChatApi.generateTextComponent(
									plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToPNegativ")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
									.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						}
					} else
					{
						if(player.hasPermission(StringValues.PERM_CMD_ECO_DELETELOG) && 
								(el.getOrdereruuid().equals(player.getUniqueId().toString()) 
										|| player.hasPermission(StringValues.PERM_BYPASS_RECOMMENT)))
						{
							player.spigot().sendMessage(ChatApi.generateTextComponent(
									plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToPPositivAll")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
									.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else if(player.hasPermission(StringValues.PERM_CMD_ECO_DELETELOG))
						{
							player.spigot().sendMessage(ChatApi.generateTextComponent(
									plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToPPositivDelete")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
									.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else if(el.getOrdereruuid().equals(player.getUniqueId().toString()) 
								|| player.hasPermission(StringValues.PERM_BYPASS_RECOMMENT))
						{
							player.spigot().sendMessage(ChatApi.generateTextComponent(
									plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToPPositivRe")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
									.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else
						{
							player.spigot().sendMessage(ChatApi.generateTextComponent(
									plugin.getYamlHandler().getL().getString(path+"Log.LoggerPToPPositiv")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvanceEconomy.getVaultApi().format(el.getAmount()))
									.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						}
					}
				}
			}
		}
		pastNextPage(plugin, player, path, playername, page, lastpage, cmdstring);
	}
	
	public static void sendGetTotal(AdvanceEconomy plugin, Player player, EcoPlayer eco, String path, ArrayList<EconomyLogger> list,
			String playername, int last, String searchword)
	{
		double positiv = 0.0;
		double negativ = 0.0;
		double total = 0.0;
		for(EconomyLogger el : list)
		{
			if(el.getFromUUIDOrNumber().equals(eco.getUUID()))
			{
				negativ -= el.getAmount();
			} else if(el.getToUUIDOrNumber().equals(eco.getUUID()))
			{
				positiv += el.getAmount();
			}
			total += el.getAmount();
		}
		if(searchword == null)
		{
			searchword = "/";
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"GetTotal.Headline")
				.replace("%name%", eco.getName())
				.replace("%amount%", String.valueOf(last))
				.replace("%searchword%", searchword)));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"GetTotal.PositivValues")
				.replace("%value%", AdvanceEconomy.getVaultApi().format(positiv))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"GetTotal.NegativValues")
				.replace("%value%", AdvanceEconomy.getVaultApi().format(negativ))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"GetTotal.TotalValues")
				.replace("%value%", AdvanceEconomy.getVaultApi().format(total))));
	}
	
	public static void sendTrendLogs(AdvanceEconomy plugin, Player player, EcoPlayer eco, String path, ArrayList<TrendLogger> list,
			int page, boolean lastpage, String playername, int last, String cmdstring)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"TrendLog.Headline")
				.replace("%name%", eco.getName())
				.replace("%amount%", String.valueOf(last))));
		for(TrendLogger tl : list)
		{
			if(MatchApi.isPositivNumber(tl.getRelativeAmountChange()))
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(plugin.getYamlHandler().getL().getString(path+"TrendLog.Change")
						.replace("%date%", tl.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
						.replace("%first%", AdvanceEconomy.getVaultApi().format(tl.getFirstValue()))
						.replace("%last%", AdvanceEconomy.getVaultApi().format(tl.getLastValue())),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString(path+"TrendLog.Positiv")
						.replace("%relativ%", AdvanceEconomy.getVaultApi().format(tl.getRelativeAmountChange()))));
			} else
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(plugin.getYamlHandler().getL().getString(path+"TrendLog.Change")
						.replace("%date%", tl.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
						.replace("%first%", AdvanceEconomy.getVaultApi().format(tl.getFirstValue()))
						.replace("%last%", AdvanceEconomy.getVaultApi().format(tl.getLastValue())),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString(path+"TrendLog.Negativ")
						.replace("%relativ%", AdvanceEconomy.getVaultApi().format(tl.getRelativeAmountChange()))));
			}
		}
		pastNextPage(plugin, player, path, playername, page, lastpage, cmdstring);
	}
}
