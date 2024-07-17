package me.avankziar.aep.spigot.cmd.standingorder;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.StandingOrder;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import me.avankziar.aep.spigot.handler.ConvertHandler;
import me.avankziar.aep.spigot.handler.LogHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class StandingOrderList extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public StandingOrderList(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
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
				plugin.getMysqlHandler().getList(MysqlType.STANDINGORDER, "`id` ASC", start, end, "?", 1));
		int last = plugin.getMysqlHandler().lastID(MysqlType.STANDINGORDER);
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> bc = new ArrayList<>();
		for(StandingOrder so : list)
		{
			bc.add(ChatApiOld.tctl("&3"+so.getID()+"&f:&6"+so.getName()+"&f:"));
			bc.add(ChatApiOld.clickHover("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, CommandSuggest.get(null, CommandExecuteType.STORDER_INFO).trim()+" "+so.getID(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApiOld.clickHover("&4✖", 
					ClickEvent.Action.SUGGEST_COMMAND, CommandSuggest.get(null, CommandExecuteType.STORDER_DELETE).trim()+" "+so.getID(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApiOld.tctl(" &1| "));
		}
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdStandingOrder.List.Headline")
				.replace("%player%", player.getName())));
		msg.add(m1);
		msg.add(bc);
		LogHandler.pastNextPage(plugin, player, msg, page, lastpage, ac.getCommandString(), null, null);
		return;
	}
}