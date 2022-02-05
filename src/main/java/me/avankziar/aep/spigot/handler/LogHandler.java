package main.java.me.avankziar.aep.spigot.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.ChatApiSmall;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.cst.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.cst.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler.Methode;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.aep.spigot.object.TrendLogger;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class LogHandler
{
	public static void pastNextPage(AdvancedEconomyPlus plugin, Player player, ArrayList<ArrayList<BaseComponent>> msg,
			int page, boolean lastpage, String cmdstring, String playervalue, String accountName)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		ArrayList<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Log.Past"));
			String cmd = cmdstring.trim();
			if(playervalue != null)
			{
				cmd += " "+playervalue;
			}
			if(accountName != null)
			{
				cmd += " "+accountName;
			}
			cmd += " "+String.valueOf(j);
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Log.Next"));
			String cmd = cmdstring.trim();
			if(playervalue != null)
			{
				cmd += " "+playervalue;
			}
			if(accountName != null)
			{
				cmd += " "+accountName;
			}
			cmd += " "+String.valueOf(i);
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		msg.add(pages);
		for(List<BaseComponent> m : msg)
		{
			TextComponent n = ChatApi.tctl("");
			n.setExtra(m);
			player.spigot().sendMessage(n);
		}
	}
	
	public static void pastNextPageLoggerSettings(AdvancedEconomyPlus plugin, Player player, ArrayList<ArrayList<BaseComponent>> msg,
			int page, boolean lastpage, String cmdstring, String methode)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		ArrayList<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Log.Past"));
			String cmd = cmdstring+String.valueOf(j)+" "+methode;
			msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Log.Next"));
			String cmd = cmdstring+String.valueOf(i)+" "+methode;
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		msg.add(pages);
		for(List<BaseComponent> m : msg)
		{
			TextComponent n = ChatApi.tctl("");
			n.setExtra(m);
			player.spigot().sendMessage(n);
		}
	}
	
	public static void sendActionLogs(AdvancedEconomyPlus plugin, Player player,
			LoggerSettings fst, ArrayList<ActionLogger> list,
			int page, int end, int last,
			LoggerSettingsHandler.Access access, String cmdstring)
	{
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		Account ac = plugin.getIFHApi().getAccount(fst.getAccountID());
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Log.ActionLog.Headline")
				.replace("%accountName%", ac.getAccountName())
				.replace("%accountID%", String.valueOf(ac.getID()))
				.replace("%accountOwner%", ac.getOwner().getName())
				.replace("%amount%", String.valueOf(last))));
		for(ActionLogger al : list)
		{
			String orderer = "";
			if(al.getOrderType() == OrdererType.PLAYER)
			{
				if(al.getOrdererUUID() != null)
				{
					String other = Utility.convertUUIDToName(al.getOrdererUUID().toString(), EconomyEntity.EconomyType.PLAYER);
					orderer = other != null ? other : "N.A.";
				} else
				{
					orderer = "N.A.";
				}
			} else
			{
				orderer = al.getOrdererPlugin();
			}
			
			String category = al.getCategory() != null ? al.getCategory() : "N.A.";
			String comment = al.getComment() != null ? al.getComment() : "N.A.";
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("%orderer%", orderer);
			map.put("%category%", category);
			map.put("%comment%", comment);
			
			Account send = null;
			Account rec = null;
			if(al.getFromAccountID() == ac.getID())
			{
				send = ac;
				rec = plugin.getIFHApi().getAccount(al.getToAccountID());
			} else if(al.getToAccountID() == ac.getID())
			{
				send = plugin.getIFHApi().getAccount(al.getFromAccountID());
				rec = ac;
			}
			String withdraw = plugin.getIFHApi().format(al.getAmountToWithdraw(), send.getCurrency());
			String deposit = plugin.getIFHApi().format(al.getAmountToDeposit(), rec.getCurrency());
			map.put("%fromaccountid%", String.valueOf(send.getID()));
			map.put("%fromaccountname%", send.getAccountName());
			map.put("%fromaccountowner%", send.getOwner().getName());
			map.put("%toaccountid%", String.valueOf(rec.getID()));
			map.put("%toaccountname%", rec.getAccountName());
			map.put("%toaccountowner%", rec.getOwner().getName());
			map.put("%withdraw%", withdraw);
			map.put("%tax%", plugin.getIFHApi().format(al.getAmountToTax(), send.getCurrency()));
			map.put("%deposit%", deposit);
			
			ArrayList<BaseComponent> m2 = new ArrayList<>();
			if(al.getFromAccountID() == ac.getID())
			{
				m2.addAll(ChatApiSmall.generateTextComponentII(
						plugin.getYamlHandler().getLang().getString("Log.ActionLog.MainMessage")
						.replace("%date%", TimeHandler.getTimeSlim(al.getUnixTime()))
						.replace("%fromcolor%", plugin.getYamlHandler().getLang().getString("Log.ActionLog.Negative"))
						.replace("%fromaccountid%", String.valueOf(send.getID()))
						.replace("%fromaccountname%", send.getAccountName())
						.replace("%fromaccountowner%", send.getOwner().getName())
						.replace("%tocolor%", plugin.getYamlHandler().getLang().getString("Log.ActionLog.Neutral"))
						.replace("%toaccountid%", String.valueOf(rec.getID()))
						.replace("%toaccountid%", String.valueOf(rec.getID()))
						.replace("%toaccountname%", rec.getAccountName())
						.replace("%toaccountowner%", rec.getOwner().getName())
						.replace("%format%", withdraw),
						map));
			} else
			{
				m2.addAll(ChatApiSmall.generateTextComponentII(
						plugin.getYamlHandler().getLang().getString("Log.ActionLog.MainMessage")
						.replace("%date%", TimeHandler.getTimeSlim(al.getUnixTime()))
						.replace("%fromcolor%", plugin.getYamlHandler().getLang().getString("Log.ActionLog.Neutral"))
						.replace("%fromaccountid%", String.valueOf(send.getID()))
						.replace("%fromaccountname%", send.getAccountName())
						.replace("%fromaccountowner%", send.getOwner().getName())
						.replace("%tocolor%", plugin.getYamlHandler().getLang().getString("Log.ActionLog.Positive"))
						.replace("%toaccountid%", String.valueOf(rec.getID()))
						.replace("%toaccountid%", String.valueOf(rec.getID()))
						.replace("%toaccountname%", rec.getAccountName())
						.replace("%toaccountowner%", rec.getOwner().getName())
						.replace("%format%", deposit),
						map));
			}
			if(
					(
					al.getOrderType() == OrdererType.PLAYER 
					&& al.getOrdererUUID() != null 
					&& player.getUniqueId().toString().equals(al.getOrdererUUID().toString())
					) 
					|| player.hasPermission(ExtraPerm.map.get(ExtraPerm.Type.BYPASS_EDITLOG))
					|| player.hasPermission(ExtraPerm.map.get(ExtraPerm.Type.BYPASS_DELETELOG)))
			{
				m2.add(ChatApi.tctl(" | "));
				if(
						(
						al.getOrderType() == OrdererType.PLAYER 
						&& al.getOrdererUUID() != null 
						&& player.getUniqueId().toString().equals(al.getOrdererUUID().toString())
						)
						||player.hasPermission(ExtraPerm.map.get(ExtraPerm.Type.BYPASS_EDITLOG)))
				{
					m2.addAll(ChatApiSmall.generateTextComponentII(
							plugin.getYamlHandler().getLang().getString("Log.ActionLog.Edit")
							.replace("%cmd%", CommandSuggest.EDITLOG)
							.replace("%id%", String.valueOf(al.getId())),
							map));
				}
				if(player.hasPermission(ExtraPerm.map.get(ExtraPerm.Type.BYPASS_DELETELOG)))
				{
					m2.addAll(ChatApiSmall.generateTextComponentII(
							plugin.getYamlHandler().getLang().getString("Log.ActionLog.Delete")
							.replace("%cmd%", CommandSuggest.DELETELOG)
							.replace("%id%", String.valueOf(al.getId())),
							map));
				}
			}
			msg.add(m2);
		}
		if(access == LoggerSettingsHandler.Access.COMMAND)
		{
			pastNextPage(plugin, player, msg, page, lastpage, cmdstring, ac.getOwner().getName(), ac.getAccountName());
		} else
		{
			pastNextPageLoggerSettings(plugin, player, msg, page, lastpage, cmdstring, LoggerSettingsHandler.Methode.LOG.toString());
		}
	}
	
	public static void sendActionDiagram(AdvancedEconomyPlus plugin, Player player,
			LoggerSettings fst, ArrayList<ActionLogger> list,
			int page, int end, int last, String cmdstring)
	{
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Log.Diagram.NotEnoughValues")));
			return;
		}
		double maxamount = 0.0;
		double minamount = 0.0;
		int i = 0;
		Account ac = plugin.getIFHApi().getAccount(fst.getAccountID());
		while(i<list.size())
		{
			ActionLogger el  = list.get(i);
			int check = ac.getID() == el.getFromAccountID() ? 1 : (ac.getID() == el.getToAccountID() ? 2 : 3) ;
			if(i == 0)
			{
				minamount = check == 1 ? el.getAmountToDeposit() : (check == 2 ? el.getAmountToWithdraw() : el.getAmountToTax());
			}
			if(minamount > (check == 1 ? el.getAmountToDeposit() : (check == 2 ? el.getAmountToWithdraw() : el.getAmountToTax())))
			{
				minamount = check == 1 ? el.getAmountToDeposit() : (check == 2 ? el.getAmountToWithdraw() : el.getAmountToTax());
			}
			if(maxamount < (check == 1 ? el.getAmountToDeposit() : (check == 2 ? el.getAmountToWithdraw() : el.getAmountToTax())))
			{
				maxamount = check == 1 ? el.getAmountToDeposit() : (check == 2 ? el.getAmountToWithdraw() : el.getAmountToTax());
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
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Log.Diagram.HeadlineII")
				.replace("%accountname%", ac.getAccountName())
				.replace("%amount%", String.valueOf(last))));
		msg.add(m1);
		ArrayList<BaseComponent> m2 = new ArrayList<>();
		m2.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Log.Diagram.Infoline")
				.replace("%max%", plugin.getIFHApi().format(maxamount, ac.getCurrency()))
				.replace("%min%", plugin.getIFHApi().format(minamount, ac.getCurrency()))
				.replace("%median%", plugin.getIFHApi().format(median, ac.getCurrency()))));
		msg.add(m2);
		while(i<=list.size()-1)
		{
			ActionLogger al = list.get(i);
			String message = "&e"+TimeHandler.getTime(al.getUnixTime())+": ";
			final int firstlength = message.length();
			double amount = 0;
			if(ac.getID() == al.getFromAccountID())
			{
				//Negativ
				amount = -al.getAmountToWithdraw();
			} else if(ac.getID() == al.getTaxAccountID())
			{
				//positive
				amount = al.getAmountToTax();
			} else 
			{
				//positive
				amount = al.getAmountToDeposit();
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
			ArrayList<BaseComponent> m3 = new ArrayList<>();
			if(MatchApi.isPositivNumber(amount))
			{
				m3.add(ChatApi.hoverEvent(message,HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMoney.Diagram.Positiv")
						.replace("%relativ%", plugin.getIFHApi().format(amount, ac.getCurrency()))
						.replace("%percent%", plugin.getIFHApi().format(percent, ac.getCurrency()))));
			} else
			{
				m3.add(ChatApi.hoverEvent(message,HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMoney.Diagram.Negativ")
						.replace("%relativ%", plugin.getIFHApi().format(amount, ac.getCurrency()))
						.replace("%percent%", plugin.getIFHApi().format(percent, ac.getCurrency()))));
			}
			msg.add(m3);
			i++;
		}
		pastNextPageLoggerSettings(plugin, player, msg, page, lastpage, cmdstring, Methode.DIAGRAM.toString());
	}
	
	public static void sendActionGrafic(AdvancedEconomyPlus plugin, Player player,
			LoggerSettings fst, ArrayList<ActionLogger> list,
			int page, int end, int last, String cmdstring)
	{
		double maxamount = 0.0;
		double minamount = 0.0;
		int safeline = -1;
		int i = 0;
		double amount = 0;
		Account ac = plugin.getIFHApi().getAccount(fst.getAccountID());
		while(i<list.size())
		{
			ActionLogger al  = list.get(i);
			if(ac.getID() == al.getFromAccountID())
			{
				//Negativ
				amount = -al.getAmountToWithdraw();
			} else if(ac.getID() == al.getTaxAccountID())
			{
				//positive
				amount = al.getAmountToTax();
			} else 
			{
				//positive
				amount = al.getAmountToDeposit();
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
		String headmessage = "&a▼"+plugin.getIFHApi().format(maxamount, ac.getCurrency());
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
		String bottommessage = "&c▲"+plugin.getIFHApi().format(minamount, ac.getCurrency());
		String lastline = "&6▲"+TimeHandler.getTime(list.get(list.size()-1).getUnixTime());
		i = list.size()-1;
		while(i>=0)
		{
			ActionLogger al = list.get(i);
			double median = 0;
			if(ac.getID() == al.getFromAccountID())
			{
				//Negativ
				amount = -al.getAmountToWithdraw();
			} else if(ac.getID() == al.getTaxAccountID())
			{
				//positive
				amount = al.getAmountToTax();
			} else 
			{
				//positive
				amount = al.getAmountToDeposit();
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
		lastline += "&6"+TimeHandler.getTime(list.get(0).getUnixTime())+"▲";
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMoney.Grafic.HeadlineII")
				.replace("%accountname%", ac.getAccountName())
				.replace("%amount%", String.valueOf(last))));
		msg.add(m1);
		ArrayList<BaseComponent> m2 = new ArrayList<>();
		m2.add(ChatApi.tctl(headmessage));
		msg.add(m2);
		ArrayList<BaseComponent> m3 = new ArrayList<>();
		m3.add(ChatApi.tctl(messageI));
		msg.add(m3);
		ArrayList<BaseComponent> m4 = new ArrayList<>();
		m4.add(ChatApi.tctl(messageII));
		msg.add(m4);
		ArrayList<BaseComponent> m5 = new ArrayList<>();
		m5.add(ChatApi.tctl(messageIII));
		msg.add(m5);
		ArrayList<BaseComponent> m6 = new ArrayList<>();
		m6.add(ChatApi.tctl(messageIV));
		msg.add(m6);
		ArrayList<BaseComponent> m7 = new ArrayList<>();
		m7.add(ChatApi.tctl(messageV));
		msg.add(m7);
		ArrayList<BaseComponent> m8 = new ArrayList<>();
		m8.add(ChatApi.tctl(middlemessage));
		msg.add(m8);
		ArrayList<BaseComponent> m9 = new ArrayList<>();
		m9.add(ChatApi.tctl(messageVI));
		msg.add(m9);
		ArrayList<BaseComponent> m10 = new ArrayList<>();
		m10.add(ChatApi.tctl(messageVII));
		msg.add(m10);
		ArrayList<BaseComponent> m11 = new ArrayList<>();
		m11.add(ChatApi.tctl(messageVIII));
		msg.add(m11);
		ArrayList<BaseComponent> m12 = new ArrayList<>();
		m12.add(ChatApi.tctl(messageIX));
		msg.add(m12);
		ArrayList<BaseComponent> m13 = new ArrayList<>();
		m13.add(ChatApi.tctl(messageX));
		msg.add(m13);
		ArrayList<BaseComponent> m14 = new ArrayList<>();
		m14.add(ChatApi.tctl(messageXI));
		msg.add(m14);
		ArrayList<BaseComponent> m15 = new ArrayList<>();
		m15.add(ChatApi.tctl(bottommessage));
		msg.add(m15);
		ArrayList<BaseComponent> m16 = new ArrayList<>();
		m16.add(ChatApi.tctl(lastline));
		msg.add(m16);
		pastNextPageLoggerSettings(plugin, player, msg, page, lastpage, cmdstring, Methode.GRAFIC.toString());
		return;
	}
	
	public static void sendActionBarChart(AdvancedEconomyPlus plugin, Player player, LoggerSettings fst, ArrayList<ActionLogger> list,
			int page, int end, int last, String cmdstring)
	{
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Log.Diagram.NotEnoughValues")));
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
		
		Account ac = plugin.getIFHApi().getAccount(fst.getAccountID());
		
		int i = 0;
		LocalDateTime startDate = TimeHandler.getLocalDateTime(list.get(list.size()-1).getUnixTime());
		while(i < list.size())
		{
			ActionLogger al  = list.get(i);
			if(ac.getID() == al.getFromAccountID())
			{
				//negativ
				maxNegativ -= al.getAmountToWithdraw();
			} else if(ac.getID() == al.getTaxAccountID())
			{
				//positiv
				maxPositiv += al.getAmountToTax();
			} else if(ac.getID() == al.getToAccountID())
			{
				//positiv
				maxPositiv += al.getAmountToDeposit();
			}
			
			LocalDateTime tldt = TimeHandler.getLocalDateTime(al.getUnixTime());
			LocalDateTime sdClone = startDate;
			int j = 1;
			sdClone = sdClone.minusMonths(1);
			while(j <= 13)
			{
				sdClone = sdClone.plusMonths(1);
				if(tldt.getMonthValue() == sdClone.getMonthValue() && tldt.getYear() == sdClone.getYear())
				{
					switch(j)
					{
					case 13:
						firstMonth.add(al);
						break;  //Break unterbricht switch case
					case 12:
						secondMonth.add(al);
						break;
					case 11:
						thirdMonth.add(al);
						break;
					case 10:
						fourthMonth.add(al);
						break;
					case 9:
						fifthMonth.add(al);
						break;
					case 8:
						sixthMonth.add(al);
						break;
					case 7:
						seventhMonth.add(al);
						break;
					case 6:
						eighthMonth.add(al);
						break;
					case 5:
						ninthMonth.add(al);
						break;
					case 4:
						tenthMonth.add(al);
						break;
					case 3:
						eleventhMonth.add(al);
						break;
					case 2:
						twelfthMonth.add(al);
						break;
					case 1:
						thirteenthMonth.add(al);
						break;
					}
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
				month = TimeHandler.getLocalDateTime(lists.get(0).getUnixTime());
			} else
			{
				k++;
				continue;
			}
			for(ActionLogger al : lists)
			{
				if(ac.getID() == al.getFromAccountID())
				{
					//negativ
					smaxNegativ -= al.getAmountToWithdraw();
				} else if(ac.getID() == al.getTaxAccountID())
				{
					//positiv
					smaxPositiv += al.getAmountToTax();
				} else if(ac.getID() == al.getToAccountID())
				{
					//positiv
					smaxPositiv += al.getAmountToDeposit();
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
						plugin.getYamlHandler().getLang().getString("CmdMoney.BarChart.HoverMessageII")
						.replace("%totalvalue%", color+AdvancedEconomyPlus.getVault().format(smaxTotal))
						.replace("%positivvalue%", AdvancedEconomyPlus.getVault().format(smaxPositiv))
						.replace("%negativvalue%", AdvancedEconomyPlus.getVault().format(smaxNegativ))
						.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
						));
				totalbc.get(k).add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMoney.BarChart.LastYear")));
				totalbc.get(k).add(ChatApi.hoverEvent(barsII, Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMoney.BarChart.HoverMessageII")
						.replace("%totalvalue%", color+AdvancedEconomyPlus.getVault().format(smaxTotal))
						.replace("%positivvalue%", AdvancedEconomyPlus.getVault().format(smaxPositiv))
						.replace("%negativvalue%", AdvancedEconomyPlus.getVault().format(smaxNegativ))
						.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
						));
			} else
			{
				totalbc.get(k).add(ChatApi.hoverEvent(bars, Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMoney.BarChart.HoverMessage")
						.replace("%percentP%", AdvancedEconomyPlus.getVault().format(hpercentP))
						.replace("%percentN%", AdvancedEconomyPlus.getVault().format(hpercentN))
						.replace("%totalvalue%", color+AdvancedEconomyPlus.getVault().format(smaxTotal))
						.replace("%positivvalue%", AdvancedEconomyPlus.getVault().format(smaxPositiv))
						.replace("%negativvalue%", AdvancedEconomyPlus.getVault().format(smaxNegativ))
						.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
						));
				totalbc.get(k).add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMoney.BarChart.Month")
						.replace("%month%", month.format(DateTimeFormatter.ofPattern("MM.yyyy")))));
				totalbc.get(k).add(ChatApi.hoverEvent(barsII, Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMoney.BarChart.HoverMessage")
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
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMoney.BarChart.Headline")
				.replace("%player%", ac.getOwner().getName())
				.replace("%amount%", String.valueOf(list.size()))));
		msg.add(m1);
		ArrayList<BaseComponent> m2 = new ArrayList<>();
		m2.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMoney.BarChart.Infoline")));
		msg.add(m2);
		for(ArrayList<BaseComponent> bc : totalbc)
		{
			if(!bc.isEmpty())
			{
				ArrayList<BaseComponent> m3 = new ArrayList<>();
				TextComponent tc = ChatApi.tc("");
				tc.setExtra(bc);
				m3.add(tc);
				msg.add(m3);
			}
		}
		
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		pastNextPageLoggerSettings(plugin, player, msg, page, lastpage, cmdstring, Methode.BARCHART.toString());
		return;
	}
	
	public static void sendTrendLogs(AdvancedEconomyPlus plugin, Player player,
			LoggerSettings fst, ArrayList<TrendLogger> list,
			int page, int end, int last,
			LoggerSettingsHandler.Access access, String cmdstring)
	{
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		Account ac = plugin.getIFHApi().getAccount(fst.getAccountID());
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Log.TrendLog.Headline")
				.replace("%accountName%", ac.getAccountName())
				.replace("%accountID%", String.valueOf(ac.getID()))
				.replace("%accountOwner%", ac.getOwner().getName())
				.replace("%amount%", String.valueOf(last))));
		msg.add(m1);
		for(TrendLogger tl : list)
		{
			ArrayList<BaseComponent> m2 = new ArrayList<>();
			if(MatchApi.isPositivNumber(tl.getRelativeAmountChange()))
			{
				m2.add(ChatApi.hoverEvent(
						plugin.getYamlHandler().getLang().getString("Log.TrendLog.ChangePositiv")
						.replace("%date%", TimeHandler.getLocalDateTime(tl.getUnixTime()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
						.replace("%first%", AdvancedEconomyPlus.getVault().format(tl.getFirstValue()))
						.replace("%last%", AdvancedEconomyPlus.getVault().format(tl.getLastValue())),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("Log.TrendLog.Positiv")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))));
			} else if(tl.getRelativeAmountChange() == 0) 
			{
				m2.add(ChatApi.hoverEvent(
						plugin.getYamlHandler().getLang().getString("Log.TrendLog.ChangeNeutral")
						.replace("%date%", TimeHandler.getLocalDateTime(tl.getUnixTime()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
						.replace("%first%", AdvancedEconomyPlus.getVault().format(tl.getFirstValue()))
						.replace("%last%", AdvancedEconomyPlus.getVault().format(tl.getLastValue())),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("Log.TrendLog.Positiv")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))));
			} else
			{
				m2.add(ChatApi.hoverEvent(
						plugin.getYamlHandler().getLang().getString("Log.TrendLog.ChangeNegativ")
						.replace("%date%", TimeHandler.getLocalDateTime(tl.getUnixTime()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
						.replace("%first%", AdvancedEconomyPlus.getVault().format(tl.getFirstValue()))
						.replace("%last%", AdvancedEconomyPlus.getVault().format(tl.getLastValue())),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("Log.TrendLog.Negativ")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))));
			}
			msg.add(m2);
		}
		if(access == LoggerSettingsHandler.Access.COMMAND)
		{
			pastNextPage(plugin, player, msg, page, lastpage, cmdstring, ac.getOwner().getName(), ac.getAccountName());
		} else
		{
			pastNextPageLoggerSettings(plugin, player, msg, page, lastpage, cmdstring, LoggerSettingsHandler.Methode.LOG.toString());
		}
	}
	
	public static void sendTrendDiagram(AdvancedEconomyPlus plugin, Player player, 
			LoggerSettings fst, ArrayList<TrendLogger> list,
			int page, int end, int last, String cmdstring)
	{
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Log.Diagram.NotEnoughValues")));
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
		Account ac = plugin.getIFHApi().getAccount(fst.getAccountID());
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Log.Diagram.Headline")
				.replace("%accountName%", ac.getAccountName())
				.replace("%accountID%", String.valueOf(ac.getID()))
				.replace("%accountOwner%", ac.getOwner().getName())
				.replace("%amount%", String.valueOf(last))));
		msg.add(m1);
		ArrayList<BaseComponent> m2 = new ArrayList<>();
		m2.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Log.Diagram.Infoline")
				.replace("%max%", AdvancedEconomyPlus.getVault().format(maxamount))
				.replace("%min%", AdvancedEconomyPlus.getVault().format(minamount))
				.replace("%median%", AdvancedEconomyPlus.getVault().format(median))));
		msg.add(m2);
		while(i < list.size())
		{
			TrendLogger tl = list.get(i);
			if(tl == null)
			{
				continue;
			}
			String message = "&e"+TimeHandler.getLocalDateTime(tl.getUnixTime()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))+": ";
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
			ArrayList<BaseComponent> m3 = new ArrayList<>();
			if(MatchApi.isPositivNumber(tl.getRelativeAmountChange()))
			{
				m3.add(ChatApi.hoverEvent(message,HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("LLog.TrendDiagram.Positiv")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))
						.replace("%percent%", AdvancedEconomyPlus.getVault().format(percent))));
			} else
			{
				m3.add(ChatApi.hoverEvent(message,HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMoney.TrendDiagram.Negativ")
						.replace("%relativ%", AdvancedEconomyPlus.getVault().format(tl.getRelativeAmountChange()))
						.replace("%percent%", AdvancedEconomyPlus.getVault().format(percent))));
			}
			msg.add(m3);
			i++;
		}
		pastNextPageLoggerSettings(plugin, player, msg, page, lastpage, cmdstring, Methode.DIAGRAM.toString());
	}
	
	public static void sendTrendGrafic(AdvancedEconomyPlus plugin, Player player,
			LoggerSettings fst, ArrayList<TrendLogger> list,
			int page, int end, int last, String cmdstring)
	{
		//ˉ
		//Es passen max 45 `x` in die chatleiste, das bedeutet maximal 
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Log.Grafic.NotEnoughValues")));
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
		String lastline = "&6▲"+TimeHandler.getLocalDateTime(list.get(list.size()-1).getUnixTime()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
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
		lastline += "&6"+TimeHandler.getLocalDateTime(list.get(0).getUnixTime()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))+"▲";
		Account ac = plugin.getIFHApi().getAccount(fst.getAccountID());
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Log.Grafic.Headline")
				.replace("%accountName%", ac.getAccountName())
				.replace("%accountID%", String.valueOf(ac.getID()))
				.replace("%accountOwner%", ac.getOwner().getName())
				.replace("%amount%", String.valueOf(last))));
		msg.add(m1);
		ArrayList<BaseComponent> m2 = new ArrayList<>();
		m2.add(ChatApi.tctl(headmessage));
		ArrayList<BaseComponent> m3 = new ArrayList<>();
		m3.add(ChatApi.tctl(messageI));
		ArrayList<BaseComponent> m4 = new ArrayList<>();
		m4.add(ChatApi.tctl(messageII));
		ArrayList<BaseComponent> m5 = new ArrayList<>();
		m5.add(ChatApi.tctl(messageIII));
		ArrayList<BaseComponent> m6 = new ArrayList<>();
		m6.add(ChatApi.tctl(messageIV));
		ArrayList<BaseComponent> m7 = new ArrayList<>();
		m7.add(ChatApi.tctl(messageV));
		ArrayList<BaseComponent> m8 = new ArrayList<>();
		m8.add(ChatApi.tctl(middlemessage));
		ArrayList<BaseComponent> m9 = new ArrayList<>();
		m9.add(ChatApi.tctl(messageVI));
		ArrayList<BaseComponent> m10 = new ArrayList<>();
		m10.add(ChatApi.tctl(messageVII));
		ArrayList<BaseComponent> m11 = new ArrayList<>();
		m11.add(ChatApi.tctl(messageVIII));
		ArrayList<BaseComponent> m12 = new ArrayList<>();
		m12.add(ChatApi.tctl(messageIX));
		ArrayList<BaseComponent> m13 = new ArrayList<>();
		m13.add(ChatApi.tctl(messageX));
		ArrayList<BaseComponent> m14 = new ArrayList<>();
		m14.add(ChatApi.tctl(messageXI));
		ArrayList<BaseComponent> m15 = new ArrayList<>();
		m15.add(ChatApi.tctl(bottommessage));
		ArrayList<BaseComponent> m16 = new ArrayList<>();
		m16.add(ChatApi.tctl(lastline));
		pastNextPageLoggerSettings(plugin, player, msg, page, lastpage, cmdstring, Methode.GRAFIC.toString());
		return;
	}
}