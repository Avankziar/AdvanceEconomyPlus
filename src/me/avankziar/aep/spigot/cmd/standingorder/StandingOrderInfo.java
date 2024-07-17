package me.avankziar.aep.spigot.cmd.standingorder;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.assistance.TimeHandler;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.StandingOrder;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.Utility;
import me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import me.avankziar.aep.spigot.handler.PendingHandler;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.spigot.economy.account.Account;

public class StandingOrderInfo extends ArgumentModule
{
	private AEP plugin;
	
	public StandingOrderInfo(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String ids = null;
		int id = 0;
		StandingOrder so = null;
		if(args.length >= 2)
		{
			ids = args[1];
			if(!MatchApi.isInteger(ids))
			{
				player.sendMessage(ChatApiOld.tl(
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
				player.sendMessage(ChatApiOld.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoPendingOrder")));
				return;
			}
			so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
		} else
		{
			if(!plugin.getMysqlHandler().exist(MysqlType.STANDINGORDER, "`id` = ?", id))
			{
				player.sendMessage(ChatApiOld.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.OrderDontExist")));
				return;
			}
			StandingOrder soid = (StandingOrder) plugin.getMysqlHandler().getData(MysqlType.STANDINGORDER, "`id` = ?", id);
			if(!soid.getOwner().toString().equals(player.getUniqueId().toString())
					&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_STANDINGORDER)))
			{
				player.sendMessage(ChatApiOld.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.NoInfo")));
				return;
			}
			so = soid;
		}
		
		String add = null;
		if(so.getID() > 0)
		{
			add = "+"+String.valueOf(so.getID());
		}
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.Headline")
				.replace("%id%", String.valueOf(so.getID()))
				.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_CANCEL).trim().replace(" ", "+"))
				.replace("%cmdII%", CommandSuggest.get(null, CommandExecuteType.STORDER_DELETE).replace(" ", "+")+(add != null ? add : ""))
				));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.Name")
				.replace("%name%", so.getName())));
		String owner = Utility.convertUUIDToName(so.getOwner().toString(), EconomyEntity.EconomyType.PLAYER);
		if(owner == null)
		{
			owner = "N.A.";
		}
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.Owner")
				.replace("%owner%", owner)));
		Account from = plugin.getIFHApi().getAccount(so.getAccountFrom());
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.From")
				.replace("%accountname%", from.getAccountName())
				.replace("%ownername%", from.getOwner().getName())
				.replace("%id%", String.valueOf(from.getID()))));
		Account to = plugin.getIFHApi().getAccount(so.getAccountTo());
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.To")
				.replace("%accountname%", to.getAccountName())
				.replace("%ownername%", to.getOwner().getName())
				.replace("%id%", String.valueOf(to.getID()))));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.Amount")
				.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_AMOUNT).trim().replace(" ", "+")
						+"+"+so.getAmount()+"+"+(add != null ? add : ""))
				.replace("%format%", plugin.getIFHApi().format(so.getAmount(), from.getCurrency()))));
		player.spigot().sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.AmountPaidSoFar")
				.replace("%format%", plugin.getIFHApi().format(so.getAmountPaidSoFar(), from.getCurrency()))));
		player.spigot().sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.AmountPaidToTax")
				.replace("%format%", plugin.getIFHApi().format(so.getAmountPaidToTax(), from.getCurrency()))));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.RepeatingTime")
				.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_REPEATINGTIME).trim().replace(" ", "+")
						+"+"+TimeHandler.getRepeatingTime(so.getRepeatingTime())+"+"+(add != null ? add : ""))
				.replace("%repeatingtime%", TimeHandler.getRepeatingTime(so.getRepeatingTime()))));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.StartTime")
				.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_STARTTIME).trim().replace(" ", "+"))
				.replace("%starttime%", TimeHandler.getTime(so.getStartTime()))));
		player.spigot().sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.LastTime")
				.replace("%lasttime%", TimeHandler.getTime(so.getLastTime()))));
		if(so.getEndtime() > 0)
		{
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.EndTime")
					.replace("%endtime%", TimeHandler.getTime(so.getEndtime()))));
		}
		player.spigot().sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.isCancelled")
				.replace("%cancelled%", plugin.getIFHApi().getBoolean(so.isCancelled()))));
		player.spigot().sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.isPaused")
				.replace("%paused%", plugin.getIFHApi().getBoolean(so.isPaused()))));
		return;
	}
}
