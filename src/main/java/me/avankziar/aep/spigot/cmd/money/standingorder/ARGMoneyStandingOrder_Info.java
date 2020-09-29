package main.java.me.avankziar.aep.spigot.cmd.money.standingorder;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;

public class ARGMoneyStandingOrder_Info extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyStandingOrder_Info(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isStandingOrder())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoStandingOrder")));
			return;
		}
		String ids = null;
		int id = 0;
		StandingOrder so = null;
		if(args.length >= 3)
		{
			ids = args[2];
			if(!MatchApi.isInteger(ids))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("NoNumber")
						.replace("%args%", ids)));
				return;
			}
			id = Integer.parseInt(ids);			
		}
		if(ids == null)
		{
			if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.NoPendingOrder")));
				return;
			}
			so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
		} else
		{
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.OrderDontExist")));
				return;
			}
			StandingOrder soid = (StandingOrder) plugin.getMysqlHandler().getData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
			if(!soid.getFrom().equals(player.getUniqueId().toString()) && !soid.getTo().equals(player.getUniqueId().toString())
					&& !player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_INFO))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.NoInfo")));
				return;
			}
			so = soid;
		}
		
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.Headline")
				.replace("%id%", String.valueOf(so.getId()))));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.Name")
				.replace("%name%", so.getName())));
		String from = Utility.convertUUIDToName(so.getFrom());
		if(from == null)
		{
			from = "/";
		}
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.From")
				.replace("%from%", from)));
		String to = Utility.convertUUIDToName(so.getTo());
		if(to == null)
		{
			to = "/";
		}
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.To")
				.replace("%to%", to)));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.Amount")
				.replace("%amount%", String.valueOf(so.getAmount()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.AmountPaidSoFar")
				.replace("%amountpaidsofar%", String.valueOf(so.getAmountPaidSoFar()))));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.StartTime")
				.replace("%starttime%", TimeHandler.getTime(so.getStartTime()))));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.RepeatingTime")
				.replace("%repeatingtime%", TimeHandler.getRepeatingTime(so.getRepeatingTime()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.LastTime")
				.replace("%lasttime%", TimeHandler.getTime(so.getLastTime()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.isCancelled")
				.replace("%cancelled%", String.valueOf(so.isCancelled()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Info.isPaused")
				.replace("%paused%", String.valueOf(so.isPaused()))));
		return;
	}
}
