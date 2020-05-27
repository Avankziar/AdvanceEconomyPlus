package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.Utility;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.handler.ConvertHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.LogHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;
import main.java.me.avankziar.advanceeconomy.spigot.object.TrendLogger;
import net.md_5.bungee.api.chat.HoverEvent;

public class ARGMoneyTrendDiagram extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyTrendDiagram(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_TRENDDIAGRAM, StringValues.PERM_CMD_MONEY_TRENDDIAGRAM,
				AdvanceEconomy.moneyarguments,1,3,StringValues.ARG_MONEY_TRENDDIAGRAM_ALIAS,
				StringValues.MONEY_SUGGEST_TRENDDIAGRAM);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
		String playername = player.getName();
		int page = 0;
		if(args.length >= 2)
		{
			String pagenumber = args[1];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length == 3)
		{
			if(!player.hasPermission(StringValues.PERM_CMD_MONEY_TRENDDIAGRAM_OTHER))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return;
			}
			playername = args[2];
		}
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayerFromName(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		int start = page*10;
		int end = page*10+9;
		boolean desc = true;
		ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getList(Type.TREND, "`id`", desc, start, end,
						"`uuidornumber` = ?", eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"(`uuidornumber` = ?)", eco.getUUID());
		//█
		//Es passen max 20 sichtbare Zeilen in den Chat.
		//Daher wären 10 sehr gut sichtbar.
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"TrendDiagram.NotEnoughValues")));
			return;
		}
		double maxamount = 0.0;
		double minamount = 0.0;
		int i = 0;
		while(i<list.size())
		{
			TrendLogger el  = list.get(i);
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
		String cmdstring = "/money trenddiagram";
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"TrendDiagram.Headline")
				.replace("%player%", eco.getName())
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"TrendDiagram.Infoline")
				.replace("%max%", AdvanceEconomy.getVaultApi().format(maxamount))
				.replace("%min%", AdvanceEconomy.getVaultApi().format(minamount))
				.replace("%median%", AdvanceEconomy.getVaultApi().format(median))));
		while(i<=list.size()-1)
		{
			TrendLogger tl = list.get(i);
			String message = "&e"+tl.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))+": ";
			final int firstlength = message.length();
			double percent = (tl.getRelativeAmountChange()/hpercent)*100;
			int greenBlocks = 10+(int) Utility.round((tl.getRelativeAmountChange()/hpercent)*10,0);
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
						plugin.getYamlHandler().getL().getString(path+"TrendDiagram.Positiv")
						.replace("%relativ%", AdvanceEconomy.getVaultApi().format(tl.getRelativeAmountChange()))
						.replace("%percent%", AdvanceEconomy.getVaultApi().format(percent))));
			} else
			{
				player.spigot().sendMessage(ChatApi.hoverEvent(message,HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString(path+"TrendDiagram.Negativ")
						.replace("%relativ%", AdvanceEconomy.getVaultApi().format(tl.getRelativeAmountChange()))
						.replace("%percent%", AdvanceEconomy.getVaultApi().format(percent))));
			}
			i++;
		}
		LogHandler.pastNextPage(plugin, player, path, playername, page, lastpage, cmdstring);
		return;
	}
}