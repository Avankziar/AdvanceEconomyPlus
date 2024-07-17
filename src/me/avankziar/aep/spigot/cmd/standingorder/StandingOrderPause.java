package me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.StandingOrder;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;

public class StandingOrderPause extends ArgumentModule
{
	private AEP plugin;
	
	public StandingOrderPause(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String ids = args[1];
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!plugin.getMysqlHandler().exist(MysqlType.STANDINGORDER, "`id` = ?", id))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.OrderDontExist")));
			return;
		}
		StandingOrder so = (StandingOrder) plugin.getMysqlHandler().getData(MysqlType.STANDINGORDER, "`id` = ?", id);
		if(!so.getOwner().toString().equals(player.getUniqueId().toString()) && !player.hasPermission(ExtraPerm.get(Type.BYPASS_STANDINGORDER)))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NotOrderer")));
			return;
		}
		if(so.isCancelled())
		{
			so.setCancelled(false);
			so.setPaused(false);
			plugin.getMysqlHandler().updateData(MysqlType.STANDINGORDER, so, "`id` = ?", so.getID());
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Pause.WasCancelled")));
			return;
		} else
		{
			if(so.isPaused())
			{
				so.setPaused(false);
				plugin.getMysqlHandler().updateData(MysqlType.STANDINGORDER, so, "`id` = ?", so.getID());
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Pause.IsUnpaused")));
				return;
			} else
			{
				so.setPaused(true);
				plugin.getMysqlHandler().updateData(MysqlType.STANDINGORDER, so, "`id` = ?", so.getID());
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Pause.IsPaused")));
				return;
			}
		}
	}
}