package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;

public class ARGStandingOrder_Pause extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGStandingOrder_Pause(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isStandingOrder())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoStandingOrder")));
			return;
		}
		String ids = args[1];
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.OrderDontExist")));
			return;
		}
		StandingOrder so = (StandingOrder) plugin.getMysqlHandler().getData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
		if(!so.getFrom().equals(player.getUniqueId().toString()) && !player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_PAUSE))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.NotOrderer")));
			return;
		}
		if(so.isCancelled())
		{
			so.setCancelled(false);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.Pause.WasCancelled")));
			return;
		} else
		{
			if(so.isPaused())
			{
				so.setPaused(false);
				plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getId());
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.Pause.IsUnpaused")));
				return;
			} else
			{
				so.setPaused(true);
				plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getId());
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.Pause.IsPaused")));
				return;
			}
		}
	}
}