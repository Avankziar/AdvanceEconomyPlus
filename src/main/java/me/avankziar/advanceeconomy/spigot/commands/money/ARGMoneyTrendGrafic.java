package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.LogHandler;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.Utility;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;
import main.java.me.avankziar.advanceeconomy.spigot.object.TrendLogger;

public class ARGMoneyTrendGrafic extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyTrendGrafic(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_TRENDGRAFIC, StringValues.PERM_CMD_MONEY_TRENDGRAFIC,
				AdvanceEconomy.moneyarguments,1,3,StringValues.ARG_MONEY_TRENDGRAFIC_ALIAS,
				StringValues.MONEY_SUGGEST_TRENDGRAFIC);
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
			if(!player.hasPermission(StringValues.PERM_CMD_MONEY_TRENDGRAFIC_OTHER))
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
		EcoPlayer eco = EcoPlayer.getEcoPlayerFromName(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		int start = page*10;
		int end = page*10+24;
		boolean desc = true;
		ArrayList<TrendLogger> list = TrendLogger.convertList(
				plugin.getMysqlHandler().getList(Type.TREND, "`id`", desc, start, end,
						"(`uuidornumber` = ?)", eco.getUUID()));
		int last = plugin.getMysqlHandler().countWhereID(Type.TREND,
				"(`uuidornumber` = ?)", eco.getUUID());
		//ˉ
		//Es passen max 45 `x` in die chatleiste, das bedeutet maximal 
		if(list.size()<2)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"TrendGrafic.NotEnoughValues")));
			return;
		}
		double maxamount = 0.0;
		double minamount = 0.0;
		int safeline = -1;
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
		String headmessage = "&a▼"+AdvanceEconomy.getVaultApi().format(maxamount);
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
		String messageXI = "ˉ";
		String bottommessage = "&c▲"+AdvanceEconomy.getVaultApi().format(minamount);
		String lastline = "&6▲"+list.get(list.size()-1).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		i = list.size()-1;
		while(i>=0)
		{
			TrendLogger tl = list.get(i);
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
				messageI 	+= "&0ˉ&6x&0ˉ";
				messageII 	+= "&0ˉˉˉˉ";
				messageIII 	+= "&0ˉˉˉˉ";
				messageIV 	+= "&0ˉˉˉˉ";
				messageV 	+= "&0ˉˉˉˉ";
				messageVI 	+= "&0ˉˉˉˉ";
				messageVII 	+= "&0ˉˉˉˉ";
				messageVIII += "&0ˉˉˉˉ";
				messageIX 	+= "&0ˉˉˉˉ";
				messageX 	+= "&0ˉˉˉˉ";
				messageXI 	+= "ˉˉˉˉ";
			} else if(safeline == 2)
			{
				messageI 	+= "&0ˉˉˉˉ";
				messageII 	+= "&0ˉ&6x&0ˉ";
				messageIII 	+= "&0ˉˉˉˉ";
				messageIV 	+= "&0ˉˉˉˉ";
				messageV 	+= "&0ˉˉˉˉ";
				messageVI 	+= "&0ˉˉˉˉ";
				messageVII 	+= "&0ˉˉˉˉ";
				messageVIII += "&0ˉˉˉˉ";
				messageIX 	+= "&0ˉˉˉˉ";
				messageX 	+= "&0ˉˉˉˉ";
				messageXI 	+= "ˉˉˉˉ";
			} else if(safeline == 3)
			{
				messageI 	+= "&0ˉˉˉˉ";
				messageII 	+= "&0ˉˉˉˉ";
				messageIII 	+= "&0ˉ&6x&0ˉ";
				messageIV 	+= "&0ˉˉˉˉ";
				messageV 	+= "&0ˉˉˉˉ";
				messageVI 	+= "&0ˉˉˉˉ";
				messageVII 	+= "&0ˉˉˉˉ";
				messageVIII += "&0ˉˉˉˉ";
				messageIX 	+= "&0ˉˉˉˉ";
				messageX 	+= "&0ˉˉˉˉ";
				messageXI 	+= "ˉˉˉˉ";
			} else if(safeline == 4)
			{
				messageI 	+= "&0ˉˉˉˉ";
				messageII 	+= "&0ˉˉˉˉ";
				messageIII 	+= "&0ˉˉˉˉ";
				messageIV 	+= "&0ˉ&6x&0ˉ";
				messageV 	+= "&0ˉˉˉˉ";
				messageVI 	+= "&0ˉˉˉˉ";
				messageVII 	+= "&0ˉˉˉˉ";
				messageVIII += "&0ˉˉˉˉ";
				messageIX 	+= "&0ˉˉˉˉ";
				messageX 	+= "&0ˉˉˉˉ";
				messageXI 	+= "ˉˉˉˉ";
			} else if(safeline == 5)
			{
				messageI 	+= "&0ˉˉˉˉ";
				messageII 	+= "&0ˉˉˉˉ";
				messageIII 	+= "&0ˉˉˉˉ";
				messageIV 	+= "&0ˉˉˉˉ";
				messageV 	+= "&0ˉ&6x&0ˉ";
				messageVI 	+= "&0ˉˉˉˉ";
				messageVII 	+= "&0ˉˉˉˉ";
				messageVIII += "&0ˉˉˉˉ";
				messageIX 	+= "&0ˉˉˉˉ";
				messageX 	+= "&0ˉˉˉˉ";
				messageXI 	+= "ˉˉˉˉ";
			} else if(safeline == 6)
			{
				messageI 	+= "&0ˉˉˉˉ";
				messageII 	+= "&0ˉˉˉˉ";
				messageIII 	+= "&0ˉˉˉˉ";
				messageIV 	+= "&0ˉˉˉˉ";
				messageV 	+= "&0ˉˉˉˉ";
				messageVI 	+= "&0ˉ&6x&0ˉ";
				messageVII 	+= "&0ˉˉˉˉ";
				messageVIII += "&0ˉˉˉˉ";
				messageIX 	+= "&0ˉˉˉˉ";
				messageX 	+= "&0ˉˉˉˉ";
				messageXI 	+= "ˉˉˉˉ";
			} else if(safeline == 7)
			{
				messageI 	+= "&0ˉˉˉˉ";
				messageII 	+= "&0ˉˉˉˉ";
				messageIII 	+= "&0ˉˉˉˉ";
				messageIV 	+= "&0ˉˉˉˉ";
				messageV 	+= "&0ˉˉˉˉ";
				messageVI 	+= "&0ˉˉˉˉ";
				messageVII 	+= "&0ˉ&6x&0ˉ";
				messageVIII += "&0ˉˉˉˉ";
				messageIX 	+= "&0ˉˉˉˉ";
				messageX 	+= "&0ˉˉˉˉ";
				messageXI 	+= "ˉˉˉˉ";
			} else if(safeline == 8)
			{
				messageI 	+= "&0ˉˉˉˉ";
				messageII 	+= "&0ˉˉˉˉ";
				messageIII 	+= "&0ˉˉˉˉ";
				messageIV 	+= "&0ˉˉˉˉ";
				messageV 	+= "&0ˉˉˉˉ";
				messageVI 	+= "&0ˉˉˉˉ";
				messageVII 	+= "&0ˉˉˉˉ";
				messageVIII += "&0ˉ&6x&0ˉ";
				messageIX 	+= "&0ˉˉˉˉ";
				messageX 	+= "&0ˉˉˉˉ";
				messageXI 	+= "ˉˉˉˉ";
			} else if(safeline == 9)
			{
				messageI 	+= "&0ˉˉˉˉ";
				messageII 	+= "&0ˉˉˉˉ";
				messageIII 	+= "&0ˉˉˉˉ";
				messageIV 	+= "&0ˉˉˉˉ";
				messageV 	+= "&0ˉˉˉˉ";
				messageVI 	+= "&0ˉˉˉˉ";
				messageVII 	+= "&0ˉˉˉˉ";
				messageVIII += "&0ˉˉˉˉ";
				messageIX 	+= "&0ˉ&6x&0ˉ";
				messageX 	+= "&0ˉˉˉˉ";
				messageXI 	+= "ˉˉˉˉ";
			} else if(safeline == 10)
			{
				messageI 	+= "&0ˉˉˉˉ";
				messageII 	+= "&0ˉˉˉˉ";
				messageIII 	+= "&0ˉˉˉˉ";
				messageIV 	+= "&0ˉˉˉˉ";
				messageV 	+= "&0ˉˉˉˉ";
				messageVI 	+= "&0ˉˉˉˉ";
				messageVII 	+= "&0ˉˉˉˉ";
				messageVIII += "&0ˉˉˉˉ";
				messageIX 	+= "&0ˉˉˉˉ";
				messageX 	+= "&0ˉ&6x&0ˉ";
				messageXI 	+= "ˉˉˉˉ";
			}
			i--;
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		String cmdstring = "/money trendgrafic";
		lastline += "&6"+list.get(0).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))+"▲";
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"TrendGrafic.Headline")
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
		LogHandler.pastNextPage(plugin, player, path, playername, page, lastpage, cmdstring);
		return;
	}
}