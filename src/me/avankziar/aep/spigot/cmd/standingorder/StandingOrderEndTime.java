package me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.TimeHandler;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.StandingOrder;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.ChatApiSmall;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.PendingHandler;
import me.avankziar.ifh.spigot.economy.account.Account;

public class StandingOrderEndTime extends ArgumentModule
{
	private AEP plugin;
	
	public StandingOrderEndTime(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String et = args[1];
		String sos = null;
		if(args.length >= 3)
		{
			sos = args[2];
		}
		long endtime = -1;
		StandingOrder so = null;
		if(sos == null)
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
			so = (StandingOrder) plugin.getMysqlHandler().getData(MysqlType.STANDINGORDER,
					"`standing_order_name` = ? AND `owner_uuid` = ?", sos, player.getUniqueId().toString());
		}
		Account fac = plugin.getIFHApi().getAccount(so.getAccountFrom());
		Account tac = plugin.getIFHApi().getAccount(so.getAccountTo());
		if(fac == null || tac == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.AccountDontExist")
					.replace("%account%", fac != null ? String.valueOf(so.getAccountFrom()) : String.valueOf(so.getAccountTo()))));
			return;
		}
		endtime = TimeHandler.getTime(et);
		if(endtime == 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.StartTime.WrongSyntax")));
			return;
		}
		so.setEndtime(endtime);
		if(sos == null)
		{
			PendingHandler.standingOrder.replace(player.getUniqueId().toString(), so);
		} else
		{
			plugin.getMysqlHandler().updateData(MysqlType.STANDINGORDER, so, "`id` = ?", so.getID());
		}
		player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.EndTime.SetEndTime")
				.replace("%endtime%", et)));
		return;
		
	}
}