package main.java.me.avankziar.aep.spigot.cmd.loan;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class LoanList extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public LoanList(ArgumentConstructor argumentConstructor)
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
		ArrayList<LoanRepayment> list = ConvertHandler.convertListVI(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.LOAN, "`id`", start, end));
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.LOAN);
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> bc = new ArrayList<>();
		for(LoanRepayment dr : list)
		{
			String color = "";
			double percent = (dr.getAmountPaidSoFar()/dr.getTotalAmount())*100;
			if(percent < 16.66666666)
			{
				color = "&4";
			} else if(percent >= 16.6666666667 && percent < 33.3333333334)
			{
				color = "&c";
			} else if(percent >= 33.3333333334 && percent < 50.0)
			{
				color = "&6";
			} else if(percent >= 50.0 && percent < 66.6666666667)
			{
				color = "&e";
			} else if(percent >= 66.6666666667 && percent < 87.5)
			{
				color = "&a";
			} else
			{
				color = "&2";
			}
			bc.add(ChatApi.hoverEvent("&3"+dr.getId()+"&f:&6"+dr.getName()+"&f:",
					HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getLang().getString("CmdLoan.HoverInfo")
					.replace("%id%", String.valueOf(dr.getId()))
					.replace("%name%", dr.getName())
					.replace("%from%", dr.getFrom())
					.replace("%to%", dr.getTo())
					.replace("%owner%", dr.getLoanOwner())
					.replace("%st%", TimeHandler.getTime(dr.getStartTime()))
					.replace("%et%", TimeHandler.getTime(dr.getEndTime()))
					.replace("%rt%", TimeHandler.getRepeatingTime(dr.getRepeatingTime()))
					.replace("%apsf%", color+String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getAmountPaidSoFar())))
					.replace("%ta%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getTotalAmount())))
					.replace("%ar%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getAmountRatio())))
					.replace("%in%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getInterest())))
					.replace("%pa%", String.valueOf(dr.isPaused()))
					.replace("%fo%", String.valueOf(dr.isForgiven()))
					.replace("%fi%", String.valueOf(dr.isFinished()))
					));
			bc.add(ChatApi.apiChat("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, plugin.getYamlHandler().getLang().getString("CmdLoan.InfoCmd")+" "+dr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.apiChat("&a✔", 
					ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getLang().getString("CmdLoan.ForgiveCmd")+" "+dr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.tctl(" &1| "));
		}
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdLoan.List.Headline")
				.replace("%name%", player.getName())));
		msg.add(m1);
		msg.add(bc);
		LogHandler.pastNextPage(plugin, player, msg, page, lastpage, ac.getCommandString(), null, null);
		return;
	}
}
