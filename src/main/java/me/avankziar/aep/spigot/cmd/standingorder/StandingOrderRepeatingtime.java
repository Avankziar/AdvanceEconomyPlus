package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.StandingOrder;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.ChatApiSmall;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;

public class StandingOrderRepeatingtime extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public StandingOrderRepeatingtime(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
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
				player.sendMessage(ChatApi.tl(
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
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoPendingOrder")));
				return;
			}
			StandingOrder so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
			so.setRepeatingTime(repeatingtime);
			PendingHandler.standingOrder.replace(player.getUniqueId().toString(), so);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.RepeatingTime.SetRepeatingTime")
					.replace("%rt%", rt)));
		} else
		{
			StandingOrder so = (StandingOrder) plugin.getMysqlHandler().getData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
			if(!so.getOwner().toString().equals(player.getUniqueId().toString()) 
					&& !player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_STANDINGORDER)))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NotOrderer")));
				return;
			}
			so.setRepeatingTime(repeatingtime);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getID());
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Amount.ChangeAmount")
					.replace("%rt%", rt)));
		}
		return;
	}
}