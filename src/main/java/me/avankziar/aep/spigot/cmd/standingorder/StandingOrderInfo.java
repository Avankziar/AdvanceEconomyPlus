package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.StandingOrder;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

public class StandingOrderInfo extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public StandingOrderInfo(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
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
						plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoPendingOrder")));
				return;
			}
			so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
		} else
		{
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.OrderDontExist")));
				return;
			}
			StandingOrder soid = (StandingOrder) plugin.getMysqlHandler().getData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
			if(!soid.getOwner().toString().equals(player.getUniqueId().toString())
					&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_STANDINGORDER)))
			{
				player.sendMessage(ChatApi.tl(
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
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.Headline")
				.replace("%id%", String.valueOf(so.getID()))
				.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_CANCEL).trim().replace(" ", "+"))
				.replace("%cmdII%", CommandSuggest.get(null, CommandExecuteType.STORDER_DELETE).replace(" ", "+")+(add != null ? add : ""))
				));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.Name")
				.replace("%name%", so.getName())));
		String owner = Utility.convertUUIDToName(so.getOwner().toString(), EconomyEntity.EconomyType.PLAYER);
		if(owner == null)
		{
			owner = "N.A.";
		}
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.Owner")
				.replace("%owner%", owner)));
		Account from = plugin.getIFHApi().getAccount(so.getAccountFrom());
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.From")
				.replace("%accountname%", from.getAccountName())
				.replace("%ownername%", from.getOwner().getName())
				.replace("%id%", String.valueOf(from.getID()))));
		Account to = plugin.getIFHApi().getAccount(so.getAccountFrom());
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.To")
				.replace("%accountname%", to.getAccountName())
				.replace("%ownername%", to.getOwner().getName())
				.replace("%id%", String.valueOf(to.getID()))));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.Amount")
				.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_AMOUNT).trim().replace(" ", "+")
						+so.getAmount()+"+"+(add != null ? add : ""))
				.replace("%format%", plugin.getIFHApi().format(so.getAmount(), from.getCurrency()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.AmountPaidSoFar")
				.replace("%format%", plugin.getIFHApi().format(so.getAmountPaidSoFar(), from.getCurrency()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.AmountPaidToTax")
				.replace("%format%", plugin.getIFHApi().format(so.getAmountPaidToTax(), from.getCurrency()))));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.RepeatingTime")
				.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_REPEATINGTIME).trim().replace(" ", "+")
						+TimeHandler.getRepeatingTime(so.getRepeatingTime())+"+"+(add != null ? add : ""))
				.replace("%repeatingtime%", TimeHandler.getRepeatingTime(so.getRepeatingTime()))));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.StartTime")
				.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_STARTTIME).trim().replace(" ", "+"))
				.replace("%starttime%", TimeHandler.getTime(so.getStartTime()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.LastTime")
				.replace("%lasttime%", TimeHandler.getTime(so.getLastTime()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.isCancelled")
				.replace("%cancelled%", String.valueOf(so.isCancelled()))));
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Info.isPaused")
				.replace("%paused%", String.valueOf(so.isPaused()))));
		return;
	}
}
