package main.java.me.avankziar.aep.spigot.cmd.money;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class _ß_ARGMoneyGetTotal extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public _ß_ARGMoneyGetTotal(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String searchword = null;
		String playername = player.getName();
		if(args.length >= 2)
		{
			searchword = args[1];
		}
		if(args.length == 3)
		{
			if(args[2].equals(playername))
			{
				playername = args[2];
			} else
			{
				/*if(!player.hasPermission(StringValues.PERM_CMD_MONEY_GETTOTALOTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}*/
				playername = args[2];
			}
		}
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		AEPUser eco = AEPUserHandler.getEcoPlayer(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		int start = 0;
		int end = Integer.MAX_VALUE;
		boolean desc = true;
		ArrayList<ActionLogger> list = new ArrayList<>();
		int last = 0;
		if(searchword == null)
		{
			list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
							"`to_uuidornumber` = ? OR `orderer_uuid` = ? OR `from_uuidornumber` = ?",
							eco.getUUID(), eco.getUUID(), eco.getUUID()));
			last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
					"`to_uuidornumber` = ? OR `orderer_uuid` = ? OR `from_uuidornumber` = ?",
					eco.getUUID(), eco.getUUID(), eco.getUUID());
		} else
		{
			AEPUser ep = AEPUserHandler.getEcoPlayer(searchword);
			if(ep != null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdMoney.GetTotal.SearchWordIsPlayer")));
				return;
			}
			list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getList(Type.ACTION, "`id`", desc, start, end,
					"(`to_uuidornumber` = ? OR `from_uuidornumber` = ? OR `orderer_uuid` = ?) AND (`orderer_uuid` = ? OR `to_name` = ?  OR `from_name` = ?)",
					eco.getUUID(), eco.getUUID(), eco.getUUID(), searchword, searchword, searchword));
			last = plugin.getMysqlHandler().countWhereID(Type.ACTION,
					"(`to_uuidornumber` = ? OR `from_uuidornumber` = ? OR `orderer_uuid` = ?) AND (`orderer_uuid` = ? OR `to_name` = ?  OR `from_name` = ?)",
					eco.getUUID(), eco.getUUID(), eco.getUUID(), searchword, searchword, searchword);
		}
		LogHandler.sendGetTotal(plugin, player, eco, "CmdMoney.", list, playername, last, searchword);
		return;
	}
}