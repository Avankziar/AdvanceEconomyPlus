package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.handler.ConvertHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.LogHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomyLogger;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;

public class ARGMoneyGetTotal extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyGetTotal(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_GETTOTAL, StringValues.PERM_CMD_MONEY_GETTOTAL,
				AdvanceEconomy.moneyarguments,1,3,StringValues.ARG_MONEY_GETTOTAL_ALIAS,
				StringValues.MONEY_SUGGEST_GETTOTAL);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
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
				if(!player.hasPermission(StringValues.PERM_CMD_MONEY_GETTOTALOTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				playername = args[2];
			}
		}
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayerFromName(playername);
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
		ArrayList<EconomyLogger> list = new ArrayList<>();
		int last = 0;
		if(searchword == null)
		{
			list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getList(Type.LOGGER, "`id`", desc, start, end,
							"`to_uuidornumber` = ? OR `orderer_uuid` = ? OR `from_uuidornumber` = ?",
							eco.getUUID(), eco.getUUID(), eco.getUUID()));
			last = plugin.getMysqlHandler().countWhereID(Type.LOGGER,
					"`to_uuidornumber` = ? OR `orderer_uuid` = ? OR `from_uuidornumber` = ?",
					eco.getUUID(), eco.getUUID(), eco.getUUID());
		} else
		{
			EcoPlayer ep = EcoPlayerHandler.getEcoPlayerFromName(searchword);
			if(ep != null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString(path+"GetTotal.SearchWordIsPlayer")));
				return;
			}
			list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getList(Type.LOGGER, "`id`", desc, start, end,
					"(`to_uuidornumber` = ? OR `from_uuidornumber` = ? OR `orderer_uuid` = ?) AND (`orderer_uuid` = ? OR `to_name` = ?  OR `from_name` = ?)",
					eco.getUUID(), eco.getUUID(), eco.getUUID(), searchword, searchword, searchword));
			last = plugin.getMysqlHandler().countWhereID(Type.LOGGER,
					"(`to_uuidornumber` = ? OR `from_uuidornumber` = ? OR `orderer_uuid` = ?) AND (`orderer_uuid` = ? OR `to_name` = ?  OR `from_name` = ?)",
					eco.getUUID(), eco.getUUID(), eco.getUUID(), searchword, searchword, searchword);
		}
		LogHandler.sendGetTotal(plugin, player, eco, path, list, playername, last, searchword);
		return;
	}
}