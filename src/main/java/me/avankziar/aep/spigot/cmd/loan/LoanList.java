package main.java.me.avankziar.aep.spigot.cmd.loan;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;
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
		for(LoanRepayment lr : list)
		{
			String color = "";
			double percent = (lr.getAmountPaidSoFar()/lr.getTotalAmount())*100;
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
			Account from = plugin.getIFHApi().getAccount(lr.getAccountFromID());
			Account to = plugin.getIFHApi().getAccount(lr.getAccountToID());
			String low = Utility.convertUUIDToName(lr.getOwner().toString(), EconomyType.PLAYER);
			String ldb = Utility.convertUUIDToName(lr.getDebtor().toString(), EconomyType.PLAYER);
			if(low == null)
			{
				low = "/";
			}
			if(ldb == null)
			{
				ldb = "/";
			}
			bc.add(ChatApi.hoverEvent("&3"+lr.getId()+"&f:&6"+lr.getName()+"&f:",
					HoverEvent.Action.SHOW_TEXT,
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.HoverInfo")
					.replace("%id%", String.valueOf(lr.getId()))
					.replace("%name%", lr.getName())
					.replace("%fromaccount%", from != null ? from.getOwner().getName() : "N.A.")
					.replace("%fromowner%", from != null ? from.getAccountName() : "N.A.")
					.replace("%toaccount%", to != null ? to.getAccountName() : "N.A.")
					.replace("%toowner%", to != null ? to.getOwner().getName() : "N.A.")
					.replace("%owner%", low)
					.replace("%owner%", ldb)
					.replace("%st%", TimeHandler.getTime(lr.getStartTime()))
					.replace("%et%", TimeHandler.getTime(lr.getEndTime()))
					.replace("%rt%", TimeHandler.getRepeatingTime(lr.getRepeatingTime()))
					.replace("%apsf%", color+plugin.getIFHApi().format(lr.getAmountPaidSoFar(), from.getCurrency()))
					.replace("%ta%", plugin.getIFHApi().format(lr.getTotalAmount(), from.getCurrency()))
					.replace("%ar%", plugin.getIFHApi().format(lr.getAmountRatio(), from.getCurrency()))
					.replace("%in%", String.valueOf(lr.getInterest()))
					.replace("%pa%", String.valueOf(lr.isPaused()))
					.replace("%fo%", String.valueOf(lr.isForgiven()))
					.replace("%fi%", String.valueOf(lr.isFinished()))
					));
			bc.add(ChatApi.apiChat("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, plugin.getYamlHandler().getLang().getString("Cmd.Loan.InfoCmd")+" "+lr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.apiChat("&a✔", 
					ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getLang().getString("Cmd.Loan.ForgiveCmd")+" "+lr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.tctl(" &1| "));
		}
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.List.Headline")
				.replace("%name%", player.getName())));
		msg.add(m1);
		msg.add(bc);
		LogHandler.pastNextPage(plugin, player, msg, page, lastpage, ac.getCommandString(), null, null);
		return;
	}
}
