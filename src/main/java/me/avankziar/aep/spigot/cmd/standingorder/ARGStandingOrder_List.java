package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.aep.spigot.handler.KeyHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGStandingOrder_List extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public ARGStandingOrder_List(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
		this.ac = argumentConstructor;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isStandingOrder())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoStandingOrder")));
			return;
		}
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
		ArrayList<BaseComponent> bc = new ArrayList<>();
		for(StandingOrder so : list)
		{
			bc.add(ChatApi.tctl("&3"+so.getId()+"&f:&6"+so.getName()+"&f:"));
			bc.add(ChatApi.apiChat("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, AEPSettings.settings.getCommands(KeyHandler.SO_INFO)+" "+so.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("GeneralHover")));
			bc.add(ChatApi.apiChat("&c✖", 
					ClickEvent.Action.SUGGEST_COMMAND, AEPSettings.settings.getCommands(KeyHandler.SO_DELETE)+" "+so.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("GeneralHover")));
			bc.add(ChatApi.tctl(" &1| "));
		}
		TextComponent tx = ChatApi.tc("");
		tx.setExtra(bc);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.List.Headline")
				.replace("%name%", player.getName())));
		player.spigot().sendMessage(tx);
		String cmdstring = plugin.getYamlHandler().getCom().getString(ac.getPath()+".CommandString");
		LogHandler.pastNextPage(plugin, player, "Cmd", "", page, lastpage, cmdstring);
		return;
	}
}