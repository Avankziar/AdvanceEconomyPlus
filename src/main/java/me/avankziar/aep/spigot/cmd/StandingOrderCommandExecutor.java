package main.java.me.avankziar.aep.spigot.cmd;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.KeyHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class StandingOrderCommandExecutor implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private static CommandConstructor cc;
	
	public StandingOrderCommandExecutor(AdvancedEconomyPlus plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		StandingOrderCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if (!(sender instanceof Player)) 
		{
			AdvancedEconomyPlus.log.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
			return false;
		}
		Player player = (Player) sender;
		if(cc == null)
		{
			return false;
		}
		if (args.length >= 1) 
		{
			if(MatchApi.isInteger(args[0]))
			{
				if(!player.hasPermission(cc.getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
					return false;
				}
				if(args.length == 1)
				{
					baseCommands(player, Integer.parseInt(args[0]), null);
					return true;
				} else if(args.length == 2)
				{
					baseCommands(player, Integer.parseInt(args[0]), args[1]);
					return true;
				}
			}
		} else if(args.length == 0)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			baseCommands(player, 0, null);
			return true;
		}
		int length = args.length-1;
		ArrayList<ArgumentConstructor> aclist = cc.subcommands;
		for(int i = 0; i <= length; i++)
		{
			for(ArgumentConstructor ac : aclist)
			{
				if(args[i].equalsIgnoreCase(ac.getName()))
				{
					if(length >= ac.minArgsConstructor && length <= ac.maxArgsConstructor)
					{
						if(player.hasPermission(ac.getPermission()))
						{
							ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
							if(am != null)
							{
								try
								{
									am.run(sender, args);
								} catch (IOException e)
								{
									e.printStackTrace();
								}
							} else
							{
								AdvancedEconomyPlus.log.info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName()));
								player.spigot().sendMessage(ChatApi.tctl(
										"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName())));
								return false;
							}
							return false;
						} else
						{
							///Du hast dafür keine Rechte!
							player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
							return false;
						}
					}/* else if(length > ac.maxArgsConstructor) 
					{
						///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
						player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, AdvancedEconomy.infoCommand));
						return false;
					}*/ else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
		player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("InputIsWrong"),
				ClickEvent.Action.RUN_COMMAND, AdvancedEconomyPlus.infoCommand));
		return false;
	}
	
	public void baseCommands(final Player player, int page, String otherplayer)
	{
		if(!AEPSettings.settings.isStandingOrder())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoStandingOrder")));
			return;
		}
		String playeruuid = player.getUniqueId().toString();
		if(otherplayer != null)
		{
			if(!otherplayer.equals(playeruuid))
			{
				if(!player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_LIST))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				AEPUser user = AEPUserHandler.getEcoPlayer(otherplayer);
				if(user != null)
				{
					playeruuid = user.getUUID();
				}
			}
		}
		int start = page*25;
		int end = 24;
		boolean desc = true;
		ArrayList<StandingOrder> list = ConvertHandler.convertListV(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.STANDINGORDER, "`id`", desc, start, end,
						"`from_player` = ? OR `to_player` = ?", playeruuid, playeruuid));
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.STANDINGORDER,
				"`from_player` = ? OR `to_player` = ?", playeruuid, playeruuid);
		if(page == 0 && list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.NoStandingOrders")));
			return;
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		ArrayList<BaseComponent> bc = new ArrayList<>();
		for(StandingOrder so : list)
		{
			bc.add(ChatApi.tctl("&3"+so.getId()+"&f:&6"+so.getName()+"&f:"));
			bc.add(ChatApi.apiChat("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, AEPSettings.settings.getCommands(KeyHandler.SO_INFO)+" "+so.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("GeneralHover")));
			bc.add(ChatApi.apiChat("&c✖", 
					ClickEvent.Action.SUGGEST_COMMAND, AEPSettings.settings.getCommands(KeyHandler.SO_DELETE)+" "+so.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("GeneralHover")));
			bc.add(ChatApi.tctl(" &1| "));
		}
		TextComponent tx = ChatApi.tc("");
		tx.setExtra(bc);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.List.Headline")
				.replace("%player%", player.getName())));
		player.spigot().sendMessage(tx);
		String cmdstring = plugin.getYamlHandler().getCom().getString(cc.getPath()+".CommandString");
		LogHandler.pastNextPage(plugin, player, "CmdMoney.", playeruuid, page, lastpage, cmdstring);
		return;
	}
}