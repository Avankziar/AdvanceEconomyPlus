package me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.assistance.TimeHandler;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.StandingOrder;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.ChatApiSmall;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.PendingHandler;

public class StandingOrderRepeatingtime extends ArgumentModule
{
	private AEP plugin;
	
	public StandingOrderRepeatingtime(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String rt = args[1];
		int id = -1;
		long repeatingtime = TimeHandler.getRepeatingTime(rt);
		if(args.length >= 3)
		{
			if(!MatchApi.isInteger(args[2]))
			{
				player.sendMessage(ChatApiOld.tl(
						plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%args%", args[2])));
				return;
			}
			id = Integer.parseInt(args[2]);
		}
		if(repeatingtime == 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.RepeatingTime.WrongSyntax")));
			return;
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
			so.setRepeatingTime(repeatingtime);
			PendingHandler.standingOrder.replace(player.getUniqueId().toString(), so);
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.RepeatingTime.SetRepeatingTime")
					.replace("%rt%", rt)));
		} else
		{
			StandingOrder so = (StandingOrder) plugin.getMysqlHandler().getData(MysqlType.STANDINGORDER, "`id` = ?", id);
			if(!so.getOwner().toString().equals(player.getUniqueId().toString()) 
					&& !player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_STANDINGORDER)))
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NotOrderer")));
				return;
			}
			so.setRepeatingTime(repeatingtime);
			plugin.getMysqlHandler().updateData(MysqlType.STANDINGORDER, so, "`id` = ?", so.getID());
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Amount.ChangeAmount")
					.replace("%rt%", rt)));
		}
		return;
	}
}