package main.java.me.avankziar.aep.spigot.cmd.standingorder;

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
import main.java.me.avankziar.aep.spigot.handler.KeyHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;

public class ARGStandingOrder_Info extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGStandingOrder_Info(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isStandingOrder())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoStandingOrder")));
			return;
		}
		String ids = null;
		int id = 0;
		StandingOrder so = null;
		if(args.length >= 2)
		{
			ids = args[1];
			if(!MatchApi.isInteger(ids))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("NoNumber")
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
						plugin.getYamlHandler().getLang().getString("CmdStandingOrder.NoPendingOrder")));
				return;
			}
			so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
		} else
		{
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("CmdStandingOrder.OrderDontExist")));
				return;
			}
			StandingOrder soid = (StandingOrder) plugin.getMysqlHandler().getData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
			if(!soid.getFrom().equals(player.getUniqueId().toString()) && !soid.getTo().equals(player.getUniqueId().toString())
					&& !player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_INFO))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.NoInfo")));
				return;
			}
			so = soid;
		}
		
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.Headline")
				.replace("%id%", String.valueOf(so.getId()))
				.replace("%cancelcmd%", AEPSettings.settings.getCommands(KeyHandler.SO_CANCEL).replace(" ", "+"))
				.replace("%deletecmd%", AEPSettings.settings.getCommands(KeyHandler.SO_DELETE).replace(" ", "+"))));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.Name")
				.replace("%name%", so.getName())));
		String from = Utility.convertUUIDToName(so.getFrom(), EconomyType.PLAYER);
		if(from == null)
		{
			from = "/";
		}
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.From")
				.replace("%from%", from)));
		String to = Utility.convertUUIDToName(so.getTo(), EconomyType.PLAYER);
		if(to == null)
		{
			to = "/";
		}
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.To")
				.replace("%to%", to)));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.Amount")
				.replace("%amountcmd%", AEPSettings.settings.getCommands(KeyHandler.SO_AMOUNT).replace(" ", "+"))
				.replace("%amount%", String.valueOf(so.getAmount()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.AmountPaidSoFar")
				.replace("%amountpaidsofar%", String.valueOf(so.getAmountPaidSoFar()))));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.RepeatingTime")
				.replace("%repeatingtimecmd%", AEPSettings.settings.getCommands(KeyHandler.SO_REPEATINGTIME).replace(" ", "+"))
				.replace("%repeatingtime%", TimeHandler.getRepeatingTime(so.getRepeatingTime()))));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.StartTime")
				.replace("%starttimecmd%", AEPSettings.settings.getCommands(KeyHandler.SO_STARTTIME).replace(" ", "+"))
				.replace("%starttime%", TimeHandler.getTime(so.getStartTime()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.LastTime")
				.replace("%lasttime%", TimeHandler.getTime(so.getLastTime()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.isCancelled")
				.replace("%cancelled%", String.valueOf(so.isCancelled()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Info.isPaused")
				.replace("%paused%", String.valueOf(so.isPaused()))));
		return;
	}
}
