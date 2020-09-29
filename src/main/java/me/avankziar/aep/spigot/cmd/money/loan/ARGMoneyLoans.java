package main.java.me.avankziar.aep.spigot.cmd.money.loan;

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
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGMoneyLoans extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public ARGMoneyLoans(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
		this.ac = argumentConstructor;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoLoan")));
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
		if(args.length == 3)
		{
			if(args[2].equals(playername))
			{
				playername = args[2];
			} else
			{
				if(!player.hasPermission(Utility.PERM_BYPASS_LOAN_LIST))
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
		ArrayList<LoanRepayment> list = ConvertHandler.convertListVI(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.LOAN, "`id`", desc, start, end,
						"`from_player` = ? OR `to_player` = ? OR `Loanowner` = ?",
						player.getUniqueId().toString(), player.getUniqueId().toString(), player.getUniqueId().toString()));
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.LOAN,
				"`from_player` = ? OR `to_player` = ? OR `Loanowner` = ?",
				player.getUniqueId().toString(), player.getUniqueId().toString(), player.getUniqueId().toString());
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
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
			bc.add(ChatApi.hoverEvent(color+dr.getId()+"&f:"+color+dr.getName()+"&f:",
					HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("CmdMoney.Loan.HoverInfo")
					.replace("%id%", String.valueOf(dr.getId()))
					.replace("%name%", dr.getName())
					.replace("%from%", dr.getFrom())
					.replace("%to%", dr.getTo())
					.replace("%owner%", dr.getLoanOwner())
					.replace("%st%", TimeHandler.getTime(dr.getStartTime()))
					.replace("%et%", TimeHandler.getTime(dr.getEndTime()))
					.replace("%rt%", TimeHandler.getRepeatingTime(dr.getRepeatingTime()))
					.replace("%apsf%", color+String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getAmountPaidSoFar())))
					.replace("%ta%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getTotalAmount())))
					.replace("%ar%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getAmountRatio())))
					.replace("%in%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getInterest())))
					.replace("%pa%", String.valueOf(dr.isPaused()))
					.replace("%fo%", String.valueOf(dr.isForgiven()))
					.replace("%fi%", String.valueOf(dr.isFinished()))
					));
			bc.add(ChatApi.apiChat("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, plugin.getYamlHandler().getL().getString("CmdMoney.Loan.InfoCmd")+" "+dr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("GeneralHover")));
			bc.add(ChatApi.apiChat("&a✔", 
					ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getL().getString("CmdMoney.Loan.ForgiveCmd")+" "+dr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("GeneralHover")));
			bc.add(ChatApi.tctl(" &1| "));
		}
		TextComponent tx = ChatApi.tc("");
		tx.setExtra(bc);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.List.Headline")
				.replace("%name%", player.getName())));
		player.spigot().sendMessage(tx);
		String cmdstring = plugin.getYamlHandler().getCom().getString(ac.getPath()+".CommandString");
		LogHandler.pastNextPage(plugin, player, "CmdMoney.", playername, page, lastpage, cmdstring);
		return;
	}
}
