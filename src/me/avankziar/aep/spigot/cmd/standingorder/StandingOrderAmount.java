package me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.StandingOrder;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.cst.transaction.Transfer;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.PendingHandler;

public class StandingOrderAmount extends ArgumentModule
{
	private AEP plugin;
	
	public StandingOrderAmount(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String amounts = Transfer.convertDecimalSeperator(args[1]);
		int id = -1;
		double amount = 0;
		if(!MatchApi.isDouble(amounts))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", amounts)));
			return;
		}
		amount = Double.parseDouble(amounts);
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", amounts)));
			return;
		}
		if(args.length >= 3)
		{
			if(MatchApi.isInteger(args[2]))
			{
				id = Integer.parseInt(args[2]);
			}
		}
		if(id < 0)
		{
			if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
			{
					player.sendMessage(ChatApiOld.tl(
							plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoPendingOrder")));
					return;
			}
			StandingOrder so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
			so.setAmount(amount);
			PendingHandler.standingOrder.replace(player.getUniqueId().toString(), so);
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Amount.SetAmount")
					.replace("%name%", so.getName())
					.replace("%format%", plugin.getIFHApi().format(amount, plugin.getIFHApi().getAccount(so.getAccountFrom()).getCurrency()))));
		} else
		{
			StandingOrder so = (StandingOrder) plugin.getMysqlHandler().getData(MysqlType.STANDINGORDER, "`id` = ?", id);
			if(!so.getOwner().toString().equals(player.getUniqueId().toString()) 
					&& !player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_STANDINGORDER)))
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NotOrderer")));
				return;
			}
			so.setAmount(amount);
			plugin.getMysqlHandler().updateData(MysqlType.STANDINGORDER, so, "`id` = ?", so.getID());
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Amount.ChangeAmount")
					.replace("%name%", so.getName())
					.replace("%format%", plugin.getIFHApi().format(amount, plugin.getIFHApi().getAccount(so.getAccountFrom()).getCurrency()))));
		}		
		return;
	}
}