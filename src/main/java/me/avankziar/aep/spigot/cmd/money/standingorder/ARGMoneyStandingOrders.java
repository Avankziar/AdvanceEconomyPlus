package main.java.me.avankziar.aep.spigot.cmd.money.standingorder;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGMoneyStandingOrders extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public ARGMoneyStandingOrders(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
		this.ac = argumentConstructor;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isStandingOrder())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoStandingOrder")));
			return;
		}
		int page = 0;
		String playername = player.getName();
		if(args.length >= 2)
		{
			String pagenumber = args[1];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length >= 3)
		{
			if(args[2].equals(playername))
			{
				playername = args[2];
			} else
			{
				if(!player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_LIST))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				playername = args[2];
			}
		}
		int start = page*25;
		int end = 24;
		boolean desc = true;
		ArrayList<StandingOrder> list = ConvertHandler.convertListV(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.STANDINGORDER, "`id`", desc, start, end,
						"`from_player` = ? OR `to_player` = ?", player.getUniqueId().toString(), player.getUniqueId().toString()));
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.STANDINGORDER,
				"`from_player` = ? OR `to_player` = ?", player.getUniqueId().toString(), player.getUniqueId().toString());
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		ArrayList<BaseComponent> bc = new ArrayList<>();
		for(StandingOrder so : list)
		{
			bc.add(ChatApi.tctl("&3"+so.getId()+"&f:&6"+so.getName()+"&f:"));
			bc.add(ChatApi.apiChat("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.InfoCmd")+" "+so.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("GeneralHover")));
			bc.add(ChatApi.apiChat("&c✖", 
					ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.DeleteCmd")+" "+so.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("GeneralHover")));
			bc.add(ChatApi.tctl(" &1| "));
		}
		TextComponent tx = ChatApi.tc("");
		tx.setExtra(bc);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.List.Headline")
				.replace("%name%", player.getName())));
		player.spigot().sendMessage(tx);
		String cmdstring = plugin.getYamlHandler().getCom().getString(ac.getPath()+".CommandString");
		LogHandler.pastNextPage(plugin, player, "CmdMoney.", playername, page, lastpage, cmdstring);
		return;
	}
}