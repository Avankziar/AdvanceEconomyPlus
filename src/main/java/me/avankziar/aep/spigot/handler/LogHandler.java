package main.java.me.avankziar.aep.spigot.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.ChatApiSmall;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler.Methode;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.aep.spigot.object.TrendLogger;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class LogHandler
{
	public static void pastNextPage(AdvancedEconomyPlus plugin, Player player, String path, String playername,
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
			String cmd = cmdstring+String.valueOf(j);
			if(playername != null)
			{
				cmd += " "+playername;
			}
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+"Log.Next"));
			String cmd = cmdstring+String.valueOf(i);
			if(playername != null)
			{
				cmd += " "+playername;
			}
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}
	
	public static void pastNextPageFirstNameThanNumber(AdvancedEconomyPlus plugin, Player player, String path, String playername,
			int page, boolean lastpage, String cmdstring, String methode)
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
			String cmd = cmdstring;
			if(playername != null)
			{
				cmd += playername;
			}
			cmd += " "+String.valueOf(j)+" "+methode;
			msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+"Log.Next"));
			String cmd = cmdstring;
			if(playername != null)
			{
				cmd += playername;
			}
			cmd += " "+String.valueOf(i)+" "+methode;
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}
	
	public static void sendActionLogs(AdvancedEconomyPlus plugin, Player player, AEPUser eco,
			LoggerSettings fst, ArrayList<ActionLogger> list,
			int page, int end, String playername, int last,  String cmdstring)
	{
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		String name = eco.getName();
		if(fst.getActionFilter().getFrom() != null && fst.getActionFilter().getTo() != null)
		{
			if(fst.getActionFilter().getFrom().equals(fst.getActionFilter().getTo())
					&& !MatchApi.isBankAccountNumber(fst.getActionFilter().getFrom()))
			{
				UUID uuid = UUID.fromString(fst.getActionFilter().getFrom());
				if(uuid != null)
				{
					AEPUser user = AEPUserHandler.getEcoPlayer(uuid);
					if(user != null)
					{
						name = user.getName();
					}
				} else
				{
					name = fst.getActionFilter().getFrom();
				}
			} /*else
			{
				//TODO BANK
			}*/
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Log.Headline")
				.replace("%name%", name)
				.replace("%amount%", String.valueOf(last))));
		for(ActionLogger el : list)
		{
			String orderer = "";
			UUID uuid = null;
			try
			{
				uuid = UUID.fromString(el.getOrdereruuid());
			} catch(IllegalArgumentException e) {}
			if(uuid != null)
			{
				AEPUser ord = AEPUserHandler.getEcoPlayer(uuid);
				if(ord == null)
				{
					orderer = el.getOrdereruuid();
				} else
				{
					orderer = ord.getName();
				}
			} else
			{
				orderer = el.getOrdereruuid();
			}
			String comment = "";
			if(el.getComment() != null)
			{
				comment = el.getComment();
			}
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("%orderer%", orderer);
			map.put("%comment%", comment);
			
			String sender = player.getUniqueId().toString();
			String reciver = player.getUniqueId().toString();
			if(fst.getActionFilter().getFrom() != null)
			{
				sender = fst.getActionFilter().getFrom();
			}
			if(fst.getActionFilter().getTo() != null)
			{
				reciver = fst.getActionFilter().getTo();
			}
			if(MatchApi.isBankAccountNumber(el.getFromUUIDOrNumber()))
			{
				if(player.hasPermission(Utility.PERM_CMD_ECO_DELETELOG) && 
						(el.getOrdereruuid().equals(reciver) 
								|| player.hasPermission(Utility.PERM_BYPASS_RECOMMENT)))
				{
					player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
							plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerBToPPositivAll")
							.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
							.replace("%fromnumber%", el.getFromUUIDOrNumber()).replace("%fromname%", el.getFromName())
							.replace("%toname%", el.getToName())
							.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
							.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
							.replace("%id%", String.valueOf(el.getId())),
							map));
				} else if(player.hasPermission(Utility.PERM_CMD_ECO_DELETELOG))
				{
					player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
							plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerBToPPositivDelete")
							.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
							.replace("%fromnumber%", el.getFromUUIDOrNumber()).replace("%fromname%", el.getFromName())
							.replace("%toname%", el.getToName())
							.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
							.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
							.replace("%id%", String.valueOf(el.getId())),
							map));
				} else if(el.getOrdereruuid().equals(reciver) 
						|| player.hasPermission(Utility.PERM_BYPASS_RECOMMENT))
				{
					player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
							plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerBToPPositivRe")
							.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
							.replace("%fromnumber%", el.getFromUUIDOrNumber()).replace("%fromname%", el.getFromName())
							.replace("%toname%", el.getToName())
							.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
							.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
							.replace("%id%", String.valueOf(el.getId())),
							map));
				} else
				{
					player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
							plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerBToPPositiv")
							.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
							.replace("%fromnumber%", el.getFromUUIDOrNumber()).replace("%fromname%", el.getFromName())
							.replace("%toname%", el.getToName())
							.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
							.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
							.replace("%id%", String.valueOf(el.getId())),
							map));
				}
			} else //isUUID
			{
				if(MatchApi.isBankAccountNumber(el.getToUUIDOrNumber()))
				{
					if(player.hasPermission(Utility.PERM_CMD_ECO_DELETELOG) && 
							(el.getOrdereruuid().equals(sender) 
									|| player.hasPermission(Utility.PERM_BYPASS_RECOMMENT)))
					{
						player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
								plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToBNegativAll")
								.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
								.replace("%fromname%", el.getFromName())
								.replace("%tonumber%", el.getToName()).replace("%toname%", el.getToName())
								.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
								.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
								.replace("%id%", String.valueOf(el.getId())),
								map));
					} else if(player.hasPermission(Utility.PERM_CMD_ECO_DELETELOG))
					{
						player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
								plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToBNegativDelete")
								.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
								.replace("%fromname%", el.getFromName())
								.replace("%tonumber%", el.getToName()).replace("%toname%", el.getToName())
								.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
								.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
								.replace("%id%", String.valueOf(el.getId())),
								map));
					} else if(el.getOrdereruuid().equals(sender) 
							|| player.hasPermission(Utility.PERM_BYPASS_RECOMMENT))
					{
						player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
								plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToBNegativRe")
								.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
								.replace("%fromname%", el.getFromName())
								.replace("%tonumber%", el.getToName()).replace("%toname%", el.getToName())
								.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
								.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
								.replace("%id%", String.valueOf(el.getId())),
								map));
					} else
					{
						player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
								plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToBNegativ")
								.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
								.replace("%fromname%", el.getFromName())
								.replace("%tonumber%", el.getToName()).replace("%toname%", el.getToName())
								.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
								.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
								.replace("%id%", String.valueOf(el.getId())),
								map));
					}
				} else //isUUID
				{
					if(el.getFromUUIDOrNumber().equals(sender))
					{
						if(player.hasPermission(Utility.PERM_CMD_ECO_DELETELOG) && 
								(el.getOrdereruuid().equals(reciver) 
										|| player.hasPermission(Utility.PERM_BYPASS_RECOMMENT)))
						{
							player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
									plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToPNegativAll")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
									.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else if(player.hasPermission(Utility.PERM_CMD_ECO_DELETELOG))
						{
							player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
									plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToPNegativDelete")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
									.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else if(el.getOrdereruuid().equals(reciver) 
								|| player.hasPermission(Utility.PERM_BYPASS_RECOMMENT))
						{
							player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
									plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToPNegativRe")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
									.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else
						{
							player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
									plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToPNegativ")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
									.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						}
					} else
					{
						if(player.hasPermission(Utility.PERM_CMD_ECO_DELETELOG) && 
								(el.getOrdereruuid().equals(sender) 
										|| player.hasPermission(Utility.PERM_BYPASS_RECOMMENT)))
						{
							player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
									plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToPPositivAll")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
									.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else if(player.hasPermission(Utility.PERM_CMD_ECO_DELETELOG))
						{
							player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
									plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToPPositivDelete")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
									.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else if(el.getOrdereruuid().equals(sender) 
								|| player.hasPermission(Utility.PERM_BYPASS_RECOMMENT))
						{
							player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
									plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToPPositivRe")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
									.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						} else
						{
							player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
									plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerPToPPositiv")
									.replace("%date%", el.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")))
									.replace("%fromname%", el.getFromName())
									.replace("%toname%", el.getToName())
									.replace("%amount%", AdvancedEconomyPlus.getVault().format(el.getAmount()))
									.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
									.replace("%id%", String.valueOf(el.getId())),
									map));
						}
					}
				}
			}
		}
		pastNextPageFirstNameThanNumber(plugin, player, "CmdMoney.", playername, page, lastpage, cmdstring, Methode.LOG.toString());
	}
	
	public static void sendActionDiagram(AdvancedEconomyPlus plugin, Player player, AEPUser eco, ArrayList<ActionLogger> list,
			int page, int end, String playername, int last, String cmdstring)
	{
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.NotEnoughValues")));
			return;
		}
		double maxamount = 0.0;
		double minamount = 0.0;
		int i = 0;
		while(i<list.size())
		{
			ActionLogger el  = list.get(i);
			if(i == 0)
			{
				minamount = el.getAmount();
			}
			if(minamount > el.getAmount())
			{
				minamount = el.getAmount();
			}
			if(maxamount < el.getAmount())
			{
				maxamount = el.getAmount();
			}
			i++;
		} 
		if(maxamount < minamount)
		{
			double safe = maxamount;
			double safeII = minamount;
			maxamount = safeII;
			minamount = safe;
		}
		double hpercent = (maxamount-minamount);
		double median = (hpercent/2)+minamount;
		i = 0;
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.HeadlineII")
				.replace("%player%", eco.getName())
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.Infoline")
				.replace("%max%", AdvancedEconomyPlus.getVault().format(maxamount))
				.replace("%min%", AdvancedEconomyPlus.getVault().format(minamount))
				.replace("%median%", AdvancedEconomyPlus.getVault().format(median))));
		while(i<=list.size()-1)
		{
			ActionLogger tl = list.get(i);
			String message = "&e"+tl.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))+": ";
			final int firstlength = message.length();
			double amount = 0;
			if(MatchApi.isBankAccountNumber(tl.getFromUUIDOrNumber()))
			{
				//positiv
				amount = tl.getAmount();
			} else
			{
				if(MatchApi.isBankAccountNumber(tl.getToUUIDOrNumber()))
				{
					//negativ
					amount = -tl.getAmount();
				} else
				{
					if(tl.getFromUUIDOrNumber().equals(eco.getUUID()))
					{
						//negativ
						amount = -tl.getAmount();
					} else
					{
						//positiv
						amount = tl.getAmount();
					}
				}
			}
			double percent = (amount/hpercent)*100;
			int greenBlocks = 10+(int) Utility.round((amount/hpercent)*10,0);
			int redBlocks = 20-greenBlocks;
			if(greenBlocks > 20)
			{
				greenBlocks = 20;
				redBlocks = 0;
			}
			if(greenBlocks < 0)
			{
				greenBlocks = 0;
				redBlocks = 20;
			}
			message = StringUtils.rightPad(message, message.length()+greenBlocks*3, "&a█");
			message = StringUtils.rightPad(message, message.length()+redBlocks*3, "&c█");
			final int secondlength = (message.length()-firstlength)/2;
			message = message.substring(0, firstlength+secondlength)+"&f|"+message.substring(firstlength+secondlength);
			if(MatchApi.isPositivNumber(amount))
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(message,HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.Positiv")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(amount))
						.replace("%percent%", AdvancedEconomyPlus.getVault().format(percent))));
			} else
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(message,HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.Negativ")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(amount))
						.replace("%percent%", AdvancedEconomyPlus.getVault().format(percent))));
			}
			i++;
		}
		pastNextPageFirstNameThanNumber(plugin, player, "CmdMoney.", playername, page, lastpage, cmdstring, Methode.DIAGRAM.toString());
	}
	
	public static void sendActionGrafic(AdvancedEconomyPlus plugin, Player player, AEPUser eco, ArrayList<ActionLogger> list,
			int page, int end, String playername, int last, String cmdstring)
	{
		double maxamount = 0.0;
		double minamount = 0.0;
		int safeline = -1;
		int i = 0;
		double amount = 0;
		while(i<list.size())
		{
			ActionLogger el  = list.get(i);
			if(MatchApi.isBankAccountNumber(el.getFromUUIDOrNumber()))
			{
				//positiv
				amount = el.getAmount();
			} else
			{
				if(MatchApi.isBankAccountNumber(el.getToUUIDOrNumber()))
				{
					//negativ
					amount = -el.getAmount();
				} else
				{
					if(el.getFromUUIDOrNumber().equals(eco.getUUID()))
					{
						//negativ
						amount = -el.getAmount();
					} else
					{
						//positiv
						amount = el.getAmount();
					}
				}
			}
			if(i == 0)
			{
				minamount = amount;
			}
			if(minamount > amount)
			{
				minamount = amount;
			}
			if(maxamount < amount)
			{
				maxamount = amount;
			}
			i++;
		} 
		if(maxamount < minamount)
		{
			double safe = maxamount;
			double safeII = minamount;
			maxamount = safeII;
			minamount = safe;
		}
		double hpercent = (maxamount-minamount);
		String wim = plugin.getYamlHandler().getConfig().getString("GraficSpaceSymbol", "ˉ");
		String x = plugin.getYamlHandler().getConfig().getString("GraficPointSymbol", "x");
		String headmessage = "&a▼"+AdvancedEconomyPlus.getVault().format(maxamount);
		String messageI = "|";
		String messageII = "|";
		String messageIII = "|";
		String messageIV = "|";
		String messageV = "|";
		String middlemessage = "|&e";
		String messageVI = "|";
		String messageVII = "|";
		String messageVIII = "|";
		String messageIX = "|";
		String messageX = "|";
		String messageXI = wim;
		String bottommessage = "&c▲"+AdvancedEconomyPlus.getVault().format(minamount);
		String lastline = "&6▲"+list.get(list.size()-1).getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
		i = list.size()-1;
		while(i>=0)
		{
			ActionLogger tl = list.get(i);
			double median = 0;
			if(MatchApi.isBankAccountNumber(tl.getFromUUIDOrNumber()))
			{
				//positiv
				median = tl.getAmount();
			} else
			{
				if(MatchApi.isBankAccountNumber(tl.getToUUIDOrNumber()))
				{
					//negativ
					median = -tl.getAmount();
				} else
				{
					if(tl.getFromUUIDOrNumber().equals(eco.getUUID()))
					{
						//negativ
						median = -tl.getAmount();
					} else
					{
						//positiv
						median = tl.getAmount();
					}
				}
			}
			safeline = (int) Utility.round(((maxamount-median)/hpercent)*10,0);
			if(safeline > 10)
			{
				safeline = 10;
			}
			if(safeline < 1)
			{
				safeline = 1;
			}
			middlemessage  += "--";
			lastline += " ";
			if(safeline == 1)
			{
				messageI 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 2)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 3)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 4)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 5)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 6)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 7)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 8)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+"&6"+x+"&0"+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 9)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 10)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageXI 	+= wim+wim+wim+wim;
			}
			i--;
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		lastline += "&6"+list.get(0).getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))+"▲";
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.TrendGrafic.HeadlineII")
				.replace("%player%", eco.getName())
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(headmessage));
		player.sendMessage(ChatApi.tl(messageI));
		player.sendMessage(ChatApi.tl(messageII));
		player.sendMessage(ChatApi.tl(messageIII));
		player.sendMessage(ChatApi.tl(messageIV));
		player.sendMessage(ChatApi.tl(messageV));
		player.sendMessage(ChatApi.tl(middlemessage));
		player.sendMessage(ChatApi.tl(messageVI));
		player.sendMessage(ChatApi.tl(messageVII));
		player.sendMessage(ChatApi.tl(messageVIII));
		player.sendMessage(ChatApi.tl(messageIX));
		player.sendMessage(ChatApi.tl(messageX));
		player.sendMessage(ChatApi.tl(messageXI));
		player.sendMessage(ChatApi.tl(bottommessage));
		player.sendMessage(ChatApi.tl(lastline));
		pastNextPageFirstNameThanNumber(plugin, player, "CmdMoney.", playername, page, lastpage, cmdstring, Methode.GRAFIC.toString());
		return;
	}
	
	//TODO:System.outs
	public static void sendActionBarChart(AdvancedEconomyPlus plugin, Player player, AEPUser eco, ArrayList<ActionLogger> list,
			int page, int end, String playername, int last, String cmdstring)
	{
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.NotEnoughValues")));
			return;
		}
		double maxNegativ = 0.0;
		double maxPositiv = 0.0;
		
		ArrayList<ActionLogger> firstMonth = new ArrayList<>();
		ArrayList<ActionLogger> secondMonth = new ArrayList<>();
		ArrayList<ActionLogger> thirdMonth = new ArrayList<>();
		ArrayList<ActionLogger> fourthMonth = new ArrayList<>();
		ArrayList<ActionLogger> fifthMonth = new ArrayList<>();
		ArrayList<ActionLogger> sixthMonth = new ArrayList<>();
		ArrayList<ActionLogger> seventhMonth = new ArrayList<>();
		ArrayList<ActionLogger> eighthMonth = new ArrayList<>();
		ArrayList<ActionLogger> ninthMonth = new ArrayList<>();
		ArrayList<ActionLogger> tenthMonth = new ArrayList<>();
		ArrayList<ActionLogger> eleventhMonth = new ArrayList<>();
		ArrayList<ActionLogger> twelfthMonth = new ArrayList<>();
		ArrayList<ActionLogger> thirteenthMonth = new ArrayList<>();
		
		int i = 0;
		LocalDateTime startDate = list.get(list.size()-1).getDateTime();
		while(i < list.size())
		{
			ActionLogger tl  = list.get(i);
			if(MatchApi.isBankAccountNumber(tl.getFromUUIDOrNumber()))
			{
				//positiv
				maxPositiv += tl.getAmount();
			} else
			{
				if(MatchApi.isBankAccountNumber(tl.getToUUIDOrNumber()))
				{
					//negativ
					maxNegativ -= tl.getAmount();
				} else
				{
					if(tl.getFromUUIDOrNumber().equals(eco.getUUID()))
					{
						//negativ
						maxNegativ -= tl.getAmount();
					} else
					{
						//positiv
						maxPositiv += tl.getAmount();
					}
				}
			}
			LocalDateTime tldt = tl.getDateTime();
			LocalDateTime sdClone = startDate;
			int j = 1;
			sdClone = sdClone.minusMonths(1);
			while(j <= 13)
			{
				sdClone = sdClone.plusMonths(1);
				AdvancedEconomyPlus.log.info(tldt.toString()+" | "+sdClone.toString());
				AdvancedEconomyPlus.log.info((tldt.getMonthValue()==sdClone.getMonthValue())
				+" | "+(tldt.getYear() == sdClone.getYear()));
				if(tldt.getMonthValue() == sdClone.getMonthValue() && tldt.getYear() == sdClone.getYear())
				{
					switch(j)
					{
					case 13:
						firstMonth.add(tl);
						break;  //Break unterbricht switch case
					case 12:
						secondMonth.add(tl);
						break;
					case 11:
						thirdMonth.add(tl);
						break;
					case 10:
						fourthMonth.add(tl);
						break;
					case 9:
						fifthMonth.add(tl);
						break;
					case 8:
						sixthMonth.add(tl);
						break;
					case 7:
						seventhMonth.add(tl);
						break;
					case 6:
						eighthMonth.add(tl);
						break;
					case 5:
						ninthMonth.add(tl);
						break;
					case 4:
						tenthMonth.add(tl);
						break;
					case 3:
						eleventhMonth.add(tl);
						break;
					case 2:
						twelfthMonth.add(tl);
						break;
					case 1:
						thirteenthMonth.add(tl);
						break;
					}
					AdvancedEconomyPlus.log.info("tldt break");
					break;
				}
				j++;
			}			
			i++;
		}
		//double maxTotal = maxPositiv + maxNegativ;
		//double median = 
		
		ArrayList<ArrayList<ActionLogger>> totallist = new ArrayList<>();
		totallist.add(list);
		totallist.add(firstMonth);
		totallist.add(secondMonth);
		totallist.add(thirdMonth);
		totallist.add(fourthMonth);
		totallist.add(fifthMonth);
		totallist.add(sixthMonth);
		totallist.add(seventhMonth);
		totallist.add(eighthMonth);
		totallist.add(ninthMonth);
		totallist.add(tenthMonth);
		totallist.add(eleventhMonth);
		totallist.add(twelfthMonth);
		totallist.add(thirteenthMonth);
		
		ArrayList<BaseComponent> firstbc = new ArrayList<>();
		ArrayList<BaseComponent> secondbc = new ArrayList<>();
		ArrayList<BaseComponent> thirdbc = new ArrayList<>();
		ArrayList<BaseComponent> fourthbc = new ArrayList<>();
		ArrayList<BaseComponent> fifthbc = new ArrayList<>();
		ArrayList<BaseComponent> sixthbc = new ArrayList<>();
		ArrayList<BaseComponent> seventhbc = new ArrayList<>();
		ArrayList<BaseComponent> eighthbc = new ArrayList<>();
		ArrayList<BaseComponent> ninthbc = new ArrayList<>();
		ArrayList<BaseComponent> tenthbc = new ArrayList<>();
		ArrayList<BaseComponent> eleventhbc = new ArrayList<>();
		ArrayList<BaseComponent> twelfthbc = new ArrayList<>();
		ArrayList<BaseComponent> thirteenthbc = new ArrayList<>();
		ArrayList<BaseComponent> yearbc = new ArrayList<>();
		ArrayList<ArrayList<BaseComponent>> totalbc = new ArrayList<>();
		totalbc.add(yearbc);
		totalbc.add(firstbc);
		totalbc.add(secondbc);
		totalbc.add(thirdbc);
		totalbc.add(fourthbc);
		totalbc.add(fifthbc);
		totalbc.add(sixthbc);
		totalbc.add(seventhbc);
		totalbc.add(eighthbc);
		totalbc.add(ninthbc);
		totalbc.add(tenthbc);
		totalbc.add(eleventhbc);
		totalbc.add(twelfthbc);
		totalbc.add(thirteenthbc);
		
		
		int k = 0;
		for(ArrayList<ActionLogger> lists : totallist)
		{
			//Hier Rechnung für die spezifischen maxTotal, maxPositiv, maxNegativ
			double smaxPositiv = 0.0;
			double smaxNegativ = 0.0;
			LocalDateTime month = null;
			if(lists.size() >= 1)
			{
				month = lists.get(0).getDateTime();
			} else
			{
				k++;
				continue;
			}
			for(ActionLogger tl : lists)
			{
				if(MatchApi.isBankAccountNumber(tl.getFromUUIDOrNumber()))
				{
					//positiv
					smaxPositiv += tl.getAmount();
				} else
				{
					if(MatchApi.isBankAccountNumber(tl.getToUUIDOrNumber()))
					{
						//negativ
						smaxNegativ -= tl.getAmount();
					} else
					{
						if(tl.getFromUUIDOrNumber().equals(eco.getUUID()))
						{
							//negativ
							smaxNegativ -= tl.getAmount();
						} else
						{
							//positiv
							smaxPositiv += tl.getAmount();
						}
					}
				}
			}
			double smaxTotal = smaxPositiv + smaxNegativ;		
			
			double hpercentP = (smaxPositiv/maxPositiv)*100;
			double hpercentN = (smaxNegativ/maxNegativ)*100;
			int amountBarsGreen = (int) Utility.round(hpercentP/2,5);
			if(amountBarsGreen < 0)
			{
				amountBarsGreen = 0;
			}
			int amountBarsGray = 50-amountBarsGreen;
			//Hier die Balken berechnen
			String bars = "";
			bars = StringUtils.rightPad(bars, bars.length()+amountBarsGray*3, "&7|");
			bars = StringUtils.rightPad(bars, bars.length()+amountBarsGreen*3, "&a|");
			
			int amountBarsRed = (int) Utility.round(hpercentN/2,5);
			if(amountBarsRed < 0)
			{
				amountBarsRed = 0;
			}
			int amountBarsGrayII = 50-amountBarsRed;
			//Hier die Balken berechnen
			String barsII = "";
			barsII = StringUtils.rightPad(barsII, barsII.length()+amountBarsRed*3, "&c|");
			barsII = StringUtils.rightPad(barsII, barsII.length()+amountBarsGrayII*3, "&7|");
			
			
			String color = "";
			if(smaxTotal > 0)
			{
				color = "&a";
			} else
			{
				color = "&c";
			}
			
			if(k == 0)
			{
				totalbc.get(k).add(ChatApi.hoverEvent(bars, Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.BarChart.HoverMessageII")
						.replace("%totalvalue%", color+AdvancedEconomyPlus.getVault().format(smaxTotal))
						.replace("%positivvalue%", AdvancedEconomyPlus.getVault().format(smaxPositiv))
						.replace("%negativvalue%", AdvancedEconomyPlus.getVault().format(smaxNegativ))
						.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
						));
				totalbc.get(k).add(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdMoney.BarChart.LastYear")));
				totalbc.get(k).add(ChatApi.hoverEvent(barsII, Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.BarChart.HoverMessageII")
						.replace("%totalvalue%", color+AdvancedEconomyPlus.getVault().format(smaxTotal))
						.replace("%positivvalue%", AdvancedEconomyPlus.getVault().format(smaxPositiv))
						.replace("%negativvalue%", AdvancedEconomyPlus.getVault().format(smaxNegativ))
						.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
						));
			} else
			{
				totalbc.get(k).add(ChatApi.hoverEvent(bars, Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.BarChart.HoverMessage")
						.replace("%percentP%", AdvancedEconomyPlus.getVault().format(hpercentP))
						.replace("%percentN%", AdvancedEconomyPlus.getVault().format(hpercentN))
						.replace("%totalvalue%", color+AdvancedEconomyPlus.getVault().format(smaxTotal))
						.replace("%positivvalue%", AdvancedEconomyPlus.getVault().format(smaxPositiv))
						.replace("%negativvalue%", AdvancedEconomyPlus.getVault().format(smaxNegativ))
						.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
						));
				totalbc.get(k).add(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdMoney.BarChart.Month")
						.replace("%month%", month.format(DateTimeFormatter.ofPattern("MM.yyyy")))));
				totalbc.get(k).add(ChatApi.hoverEvent(barsII, Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.BarChart.HoverMessage")
						.replace("%percentP%", AdvancedEconomyPlus.getVault().format(hpercentP))
						.replace("%percentN%", AdvancedEconomyPlus.getVault().format(hpercentN))
						.replace("%totalvalue%", color+AdvancedEconomyPlus.getVault().format(smaxTotal))
						.replace("%positivvalue%", AdvancedEconomyPlus.getVault().format(smaxPositiv))
						.replace("%negativvalue%", AdvancedEconomyPlus.getVault().format(smaxNegativ))
						.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
						));
			}
			k++;
		}
		
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.BarChart.Headline")
				.replace("%player%", eco.getName())
				.replace("%amount%", String.valueOf(list.size()))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.BarChart.Infoline")));
		for(ArrayList<BaseComponent> bc : totalbc)
		{
			if(!bc.isEmpty())
			{
				TextComponent tc = ChatApi.tc("");
				tc.setExtra(bc);
				player.spigot().sendMessage(tc);
			}
		}
		
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		pastNextPageFirstNameThanNumber(plugin, player, "CmdMoney.", playername, page, lastpage, cmdstring, Methode.BARCHART.toString());
		return;
	}
	
	//TODO nicht richtig
	public static void sendGetTotal(AdvancedEconomyPlus plugin, Player player, AEPUser eco, String path, ArrayList<ActionLogger> list,
			String playername, int last, String searchword)
	{
		double positiv = 0.0;
		double negativ = 0.0;
		double total = 0.0;
		for(ActionLogger el : list)
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
				.replace("%value%", AdvancedEconomyPlus.getVault().format(positiv))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"GetTotal.NegativValues")
				.replace("%value%", AdvancedEconomyPlus.getVault().format(negativ))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"GetTotal.TotalValues")
				.replace("%value%", AdvancedEconomyPlus.getVault().format(total))));
	}
	
	public static void sendTrendLogs(AdvancedEconomyPlus plugin, Player player, AEPUser eco,
			LoggerSettings fst, ArrayList<TrendLogger> list,
			int page, int end, String playername, int last, String cmdstring)
	{
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		String name = eco.getName();
		if(fst.getActionFilter().getFrom() != null && fst.getActionFilter().getTo() != null)
		{
			if(fst.getActionFilter().getFrom().equals(fst.getActionFilter().getTo())
					&& !MatchApi.isBankAccountNumber(fst.getActionFilter().getFrom()))
			{
				UUID uuid = UUID.fromString(fst.getActionFilter().getFrom());
				if(uuid != null)
				{
					AEPUser user = AEPUserHandler.getEcoPlayer(uuid);
					if(user != null)
					{
						name = user.getName();
					}
				} else
				{
					name = fst.getActionFilter().getFrom();
				}
			} /*else
			{
				//TODO BANK
			}*/
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.TrendLog.Headline")
				.replace("%player%", name)
				.replace("%amount%", String.valueOf(last))));
		for(TrendLogger tl : list)
		{
			if(MatchApi.isPositivNumber(tl.getRelativeAmountChange()))
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendLog.ChangePositiv")
						.replace("%date%", tl.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
						.replace("%first%", AdvancedEconomyPlus.getVault().format(tl.getFirstValue()))
						.replace("%last%", AdvancedEconomyPlus.getVault().format(tl.getLastValue())),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendLog.Positiv")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))));
			} else if(tl.getRelativeAmountChange() == 0) 
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendLog.ChangeNeutral")
						.replace("%date%", tl.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
						.replace("%first%", AdvancedEconomyPlus.getVault().format(tl.getFirstValue()))
						.replace("%last%", AdvancedEconomyPlus.getVault().format(tl.getLastValue())),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendLog.Positiv")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))));
			} else
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendLog.ChangeNegativ")
						.replace("%date%", tl.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
						.replace("%first%", AdvancedEconomyPlus.getVault().format(tl.getFirstValue()))
						.replace("%last%", AdvancedEconomyPlus.getVault().format(tl.getLastValue())),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendLog.Negativ")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))));
			}
		}
		pastNextPageFirstNameThanNumber(plugin, player, "CmdMoney.", playername, page, lastpage, cmdstring, Methode.LOG.toString());
	}
	
	public static void sendTrendDiagram(AdvancedEconomyPlus plugin, Player player, AEPUser eco, ArrayList<TrendLogger> list,
			int page, int end, String playername, int last, String cmdstring)
	{
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.NotEnoughValues")));
			return;
		}
		double maxamount = 0.0;
		double minamount = 0.0;
		int i = 0;
		while(i < list.size())
		{
			TrendLogger el  = list.get(i);
			if(el == null)
			{
				continue;
			}
			if(i == 0)
			{
				minamount = el.getFirstValue();
			}
			if(minamount > el.getFirstValue())
			{
				minamount = el.getFirstValue();
			}
			if(maxamount < el.getLastValue())
			{
				maxamount = el.getLastValue();
			}
			i++;
		} 
		if(maxamount < minamount)
		{
			double safe = maxamount;
			double safeII = minamount;
			maxamount = safeII;
			minamount = safe;
		}
		double hpercent = (maxamount-minamount);
		double median = (hpercent/2)+minamount;
		i = 0;
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.Headline")
				.replace("%player%", eco.getName())
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.Infoline")
				.replace("%max%", AdvancedEconomyPlus.getVault().format(maxamount))
				.replace("%min%", AdvancedEconomyPlus.getVault().format(minamount))
				.replace("%median%", AdvancedEconomyPlus.getVault().format(median))));
		while(i < list.size())
		{
			TrendLogger tl = list.get(i);
			if(tl == null)
			{
				continue;
			}
			String message = "&e"+tl.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))+": ";
			final int firstlength = message.length();
			double percent = (tl.getRelativeAmountChange()/hpercent)*100;
			int greenBlocks =10+ (int)percent/10;
			int redBlocks = 20-greenBlocks;
			if(greenBlocks > 20)
			{
				greenBlocks = 20;
				redBlocks = 0;
			}
			if(greenBlocks < 0)
			{
				greenBlocks = 0;
				redBlocks = 20;
			}
			message = StringUtils.rightPad(message, message.length()+greenBlocks*3, "&a█");
			message = StringUtils.rightPad(message, message.length()+redBlocks*3, "&c█");
			final int secondlength = (message.length()-firstlength)/2;
			message = message.substring(0, firstlength+secondlength)+"&f|"+message.substring(firstlength+secondlength);
			if(MatchApi.isPositivNumber(tl.getRelativeAmountChange()))
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(message,HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.Positiv")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))
						.replace("%percent%", AdvancedEconomyPlus.getVault().format(percent))));
			} else
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(message,HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdMoney.TrendDiagram.Negativ")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))
						.replace("%percent%", AdvancedEconomyPlus.getVault().format(percent))));
			}
			i++;
		}
		pastNextPageFirstNameThanNumber(plugin, player, "CmdMoney.", playername, page, lastpage, cmdstring, Methode.DIAGRAM.toString());
	}
	
	public static void sendTrendGrafic(AdvancedEconomyPlus plugin, Player player, AEPUser eco, ArrayList<TrendLogger> list,
			int page, int end, String playername, int last, String cmdstring)
	{
		//ˉ
		//Es passen max 45 `x` in die chatleiste, das bedeutet maximal 
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.TrendGrafic.NotEnoughValues")));
			return;
		}
		double maxamount = 0.0;
		double minamount = 0.0;
		int safeline = -1;
		int i = 0;
		while(i<list.size())
		{
			TrendLogger el  = list.get(i);
			if(el == null)
			{
				continue;
			}
			if(i == 0)
			{
				minamount = el.getFirstValue();
			}
			if(minamount > el.getFirstValue())
			{
				minamount = el.getFirstValue();
			}
			if(minamount > el.getLastValue())
			{
				minamount = el.getLastValue();
			}
			if(maxamount < el.getFirstValue())
			{
				maxamount = el.getFirstValue();
			}
			if(maxamount < el.getLastValue())
			{
				maxamount = el.getLastValue();
			}
			i++;
		} 
		if(maxamount < minamount)
		{
			double safe = maxamount;
			double safeII = minamount;
			maxamount = safeII;
			minamount = safe;
		}
		double hpercent = (maxamount-minamount);
		String wim = plugin.getYamlHandler().getConfig().getString("GraficSpaceSymbol", "ˉ");
		String x = plugin.getYamlHandler().getConfig().getString("GraficPointSymbol", "x");
		String headmessage = "&a▼"+AdvancedEconomyPlus.getVault().format(maxamount);
		String messageI = "|";
		String messageII = "|";
		String messageIII = "|";
		String messageIV = "|";
		String messageV = "|";
		String middlemessage = "|&e";
		String messageVI = "|";
		String messageVII = "|";
		String messageVIII = "|";
		String messageIX = "|";
		String messageX = "|";
		String messageXI = wim;
		String bottommessage = "&c▲"+AdvancedEconomyPlus.getVault().format(minamount);
		String lastline = "&6▲"+list.get(list.size()-1).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		i = list.size()-1;
		while(i>=0)
		{
			TrendLogger tl = list.get(i);
			if(tl == null)
			{
				continue;
			}
			double median = (Math.max(tl.getFirstValue(), tl.getLastValue())+Math.min(tl.getFirstValue(), tl.getLastValue()))/2;
			safeline = (int) Utility.round(((maxamount-median)/hpercent)*10,0);
			if(safeline > 10)
			{
				safeline = 10;
			}
			if(safeline < 1)
			{
				safeline = 1;
			}
			middlemessage  += "--";
			lastline += " ";
			if(safeline == 1)
			{
				messageI 	+= "&0"+wim+"&6"+x+"&0"+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 2)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+=  "&0"+wim+"&6"+x+"&0"+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 3)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+=  "&0"+wim+"&6"+x+"&0"+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 4)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+=  "&0"+wim+"&6"+x+"&0"+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 5)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+=  "&0"+wim+"&6"+x+"&0"+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 6)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+=  "&0"+wim+"&6"+x+"&0"+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 7)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+=  "&0"+wim+"&6"+x+"&0"+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 8)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII +=  "&0"+wim+"&6"+x+"&0"+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 9)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+=  "&0"+wim+"&6"+x+"&0"+wim;
				messageX 	+= "&0"+wim+wim+wim+wim;
				messageXI 	+= wim+wim+wim+wim;
			} else if(safeline == 10)
			{
				messageI 	+= "&0"+wim+wim+wim+wim;
				messageII 	+= "&0"+wim+wim+wim+wim;
				messageIII 	+= "&0"+wim+wim+wim+wim;
				messageIV 	+= "&0"+wim+wim+wim+wim;
				messageV 	+= "&0"+wim+wim+wim+wim;
				messageVI 	+= "&0"+wim+wim+wim+wim;
				messageVII 	+= "&0"+wim+wim+wim+wim;
				messageVIII += "&0"+wim+wim+wim+wim;
				messageIX 	+= "&0"+wim+wim+wim+wim;
				messageX 	+=  "&0"+wim+"&6"+x+"&0"+wim;
				messageXI 	+= wim+wim+wim+wim;
			}
			i--;
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		lastline += "&6"+list.get(0).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))+"▲";
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.TrendGrafic.Headline")
				.replace("%player%", eco.getName())
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(headmessage));
		player.sendMessage(ChatApi.tl(messageI));
		player.sendMessage(ChatApi.tl(messageII));
		player.sendMessage(ChatApi.tl(messageIII));
		player.sendMessage(ChatApi.tl(messageIV));
		player.sendMessage(ChatApi.tl(messageV));
		player.sendMessage(ChatApi.tl(middlemessage));
		player.sendMessage(ChatApi.tl(messageVI));
		player.sendMessage(ChatApi.tl(messageVII));
		player.sendMessage(ChatApi.tl(messageVIII));
		player.sendMessage(ChatApi.tl(messageIX));
		player.sendMessage(ChatApi.tl(messageX));
		player.sendMessage(ChatApi.tl(messageXI));
		player.sendMessage(ChatApi.tl(bottommessage));
		player.sendMessage(ChatApi.tl(lastline));
		pastNextPageFirstNameThanNumber(plugin, player, "CmdMoney.", playername, page, lastpage, cmdstring, Methode.GRAFIC.toString());
		return;
	}
}
