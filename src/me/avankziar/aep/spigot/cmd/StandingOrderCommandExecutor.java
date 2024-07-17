package me.avankziar.aep.spigot.cmd;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.cmd.tree.CommandConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.StandingOrder;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import me.avankziar.aep.spigot.handler.ConfigHandler;
import me.avankziar.aep.spigot.handler.ConvertHandler;
import me.avankziar.aep.spigot.handler.LogHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class StandingOrderCommandExecutor implements CommandExecutor
{
	private AEP plugin;
	private static CommandConstructor cc;
	
	public StandingOrderCommandExecutor(CommandConstructor cc)
	{
		this.plugin = AEP.getPlugin();
		StandingOrderCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if (!(sender instanceof Player)) 
		{
			AEP.logger.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
			return false;
		}
		Player player = (Player) sender;
		if(!ConfigHandler.isStandingOrderEnabled())
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoStandingOrder")));
			return false;
		}
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
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
				if(args.length == 1)
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							baseCommands(player, Integer.parseInt(args[0]), null);
						}
					}.runTaskAsynchronously(plugin);
					return true;
				} else if(args.length == 2)
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							baseCommands(player, Integer.parseInt(args[0]), args[1]);
						}
					}.runTaskAsynchronously(plugin);
					return true;
				}
			}
		} else if(args.length == 0)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					baseCommands(player, 0, null);
				}
			}.runTaskAsynchronously(plugin);
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
								new BukkitRunnable()
								{
									@Override
									public void run()
									{
										try
										{
											am.run(sender, args);
										} catch (IOException e)
										{
											e.printStackTrace();
										}
									}
								}.runTaskAsynchronously(plugin);
							} else
							{
								AEP.logger.info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName()));
								player.spigot().sendMessage(ChatApiOld.tctl(
										"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName())));
								return false;
							}
							return false;
						} else
						{
							///Du hast dafür keine Rechte!
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
		player.spigot().sendMessage(ChatApiOld.click(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
				ClickEvent.Action.RUN_COMMAND, CommandSuggest.get(null, CommandExecuteType.AEP)));
		return false;
	}
	
	public void baseCommands(final Player player, int page, String otherplayer)
	{
		String playeruuid = player.getUniqueId().toString();
		if(otherplayer != null)
		{
			if(!otherplayer.equals(player.getName()))
			{
				if(!player.hasPermission(ExtraPerm.get(Type.BYPASS_STANDINGORDER)))
				{
					player.sendMessage(ChatApiOld.tl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(
						MysqlType.PLAYERDATA, "`player_name` = ?", otherplayer);
				if(user != null)
				{
					playeruuid = user.getUUID().toString();
				}
			}
		}
		int start = page*25;
		int end = 24;
		ArrayList<StandingOrder> list = ConvertHandler.convertListV(
				plugin.getMysqlHandler().getList(MysqlType.STANDINGORDER, "`id` DESC", start, end,
						"`owner_uuid` = ?", playeruuid));
		int last = plugin.getMysqlHandler().getCount(MysqlType.STANDINGORDER,
				"`owner_uuid` = ?", playeruuid);
		if(page == 0 && list.isEmpty())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoStandingOrders")));
			return;
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> bc = new ArrayList<>();
		for(StandingOrder so : list)
		{
			bc.add(ChatApiOld.tctl("&3"+so.getID()+"&f:&6"+so.getName()+"&f:"));
			bc.add(ChatApiOld.clickHover("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, CommandSuggest.get(null, CommandExecuteType.STORDER_INFO).trim()+" "+so.getID(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApiOld.clickHover("&c✖", 
					ClickEvent.Action.SUGGEST_COMMAND, CommandSuggest.get(null, CommandExecuteType.STORDER_DELETE).trim()+" "+so.getID(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApiOld.tctl(" &1| "));
		}
		
		ArrayList<BaseComponent> bcII = new ArrayList<>();
		bcII.add(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.List.Headline")
				.replace("%player%", player.getName())));
		msg.add(bcII);
		msg.add(bc);
		LogHandler.pastNextPage(plugin, player, msg, page, lastpage, cc.getCommandString(), otherplayer, null);
		return;
	}
}