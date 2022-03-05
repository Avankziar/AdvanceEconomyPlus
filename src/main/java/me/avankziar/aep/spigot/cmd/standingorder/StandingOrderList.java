package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.StandingOrder;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class StandingOrderList extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public StandingOrderList(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
		this.ac = argumentConstructor;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		int page = 0;
		if(args.length >= 2)
		{
			String pagenumber = args[1];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		int start = page*25;
		int end = 24;
		ArrayList<StandingOrder> list = ConvertHandler.convertListV(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.STANDINGORDER, "`id`", start, end));
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.STANDINGORDER);
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> bc = new ArrayList<>();
		for(StandingOrder so : list)
		{
			bc.add(ChatApi.tctl("&3"+so.getID()+"&f:&6"+so.getName()+"&f:"));
			bc.add(ChatApi.apiChat("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, CommandSuggest.get(null, CommandExecuteType.STORDER_INFO).trim()+" "+so.getID(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.apiChat("&4✖", 
					ClickEvent.Action.SUGGEST_COMMAND, CommandSuggest.get(null, CommandExecuteType.STORDER_DELETE).trim()+" "+so.getID(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.tctl(" &1| "));
		}
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdStandingOrder.List.Headline")
				.replace("%player%", player.getName())));
		msg.add(m1);
		msg.add(bc);
		LogHandler.pastNextPage(plugin, player, msg, page, lastpage, ac.getCommandString(), null, null);
		return;
	}
}