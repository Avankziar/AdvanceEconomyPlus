package main.java.me.avankziar.aep.spigot.cmd;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.general.objects.StandingOrder;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class StandingOrderCommandExecutor implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private static CommandConstructor cc;
	
	public StandingOrderCommandExecutor(CommandConstructor cc)
	{
		this.plugin = BaseConstructor.getPlugin();
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
		if(!ConfigHandler.isStandingOrderEnabled())
		{
			player.sendMessage(ChatApi.tl(
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
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
							player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
		player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
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
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(
						MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", otherplayer);
				if(user != null)
				{
					playeruuid = user.getUUID().toString();
				}
			}
		}
		int start = page*25;
		int end = 24;
		ArrayList<StandingOrder> list = ConvertHandler.convertListV(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.STANDINGORDER, "`id` DESC", start, end,
						"`owner_uuid` = ?", playeruuid));
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.STANDINGORDER,
				"`owner_uuid` = ?", playeruuid);
		if(page == 0 && list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoStandingOrders")));
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
			bc.add(ChatApi.tctl("&3"+so.getID()+"&f:&6"+so.getName()+"&f:"));
			bc.add(ChatApi.apiChat("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, CommandSuggest.get(null, CommandExecuteType.STORDER_INFO).trim()+" "+so.getID(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.apiChat("&c✖", 
					ClickEvent.Action.SUGGEST_COMMAND, CommandSuggest.get(null, CommandExecuteType.STORDER_DELETE).trim()+" "+so.getID(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.tctl(" &1| "));
		}
		
		ArrayList<BaseComponent> bcII = new ArrayList<>();
		bcII.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.List.Headline")
				.replace("%player%", player.getName())));
		msg.add(bcII);
		msg.add(bc);
		LogHandler.pastNextPage(plugin, player, msg, page, lastpage, cc.getCommandString(), otherplayer, null);
		return;
	}
}