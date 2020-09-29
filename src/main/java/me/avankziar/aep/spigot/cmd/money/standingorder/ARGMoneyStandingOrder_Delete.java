package main.java.me.avankziar.aep.spigot.cmd.money.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;

public class ARGMoneyStandingOrder_Delete extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyStandingOrder_Delete(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isStandingOrder())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoStandingOrder")));
			return;
		}
		String ids = args[2];
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.OrderDontExist")));
			return;
		}
		StandingOrder so = (StandingOrder) plugin.getMysqlHandler().getData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
		if(!so.getFrom().equals(player.getUniqueId().toString()) && !player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_DELETE))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.NotOrderer")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Delete.IsDeleted")));
		return;
	}
}