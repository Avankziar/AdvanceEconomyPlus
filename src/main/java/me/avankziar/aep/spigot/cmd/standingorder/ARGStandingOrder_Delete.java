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

public class ARGStandingOrder_Delete extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGStandingOrder_Delete(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
					plugin.getYamlHandler().getLang().getString("NoStandingOrder")));
			return;
		}
		String ids = args[1];
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdStandingOrder.OrderDontExist")));
			return;
		}
		StandingOrder so = (StandingOrder) plugin.getMysqlHandler().getData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
		if(!so.getFrom().equals(player.getUniqueId().toString()) && !player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_DELETE))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdStandingOrder.NotOrderer")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Delete.IsDeleted")));
		return;
	}
}