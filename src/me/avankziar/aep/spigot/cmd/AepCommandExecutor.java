package me.avankziar.aep.spigot.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.cmd.tree.BaseConstructor;
import me.avankziar.aep.general.cmd.tree.CommandConstructor;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class AepCommandExecutor implements CommandExecutor
{
	private AEP plugin;
	private static CommandConstructor cc;
	
	public AepCommandExecutor(CommandConstructor cc)
	{
		this.plugin = AEP.getPlugin();
		AepCommandExecutor.cc = cc;
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
		if(cc == null)
		{
			return false;
		}
		if (args.length == 1) 
		{
			if(MatchApi.isInteger(args[0]))
			{
				if(!player.hasPermission(cc.getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
				baseCommands(player, Integer.parseInt(args[0])); //Base and Info Command
				return true;
			}
		} else if(args.length == 0)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			baseCommands(player, 0); //Base and Info Command
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
	
	public void baseCommands(final Player player, int page)
	{
		int count = 0;
		int start = page*10;
		int end = page*10+10;
		int last = 0;
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString(
				"Log.Headline")));
		for(BaseConstructor bc : plugin.getCommandHelpList())
		{
			if(count >= start && count <= end)
			{
				if(player.hasPermission(bc.getPermission()))
				{
					sendInfo(player, bc);
				}
			}
			count++;
			last++;
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		pastNextPage(player, "Log.", page, lastpage, CommandSuggest.get(null, CommandExecuteType.AEP));
	}
	
	private void sendInfo(Player player, BaseConstructor bc)
	{
		player.spigot().sendMessage(ChatApiOld.clickHover(
				bc.getHelpInfo(),
				ClickEvent.Action.SUGGEST_COMMAND, bc.getSuggestion(),
				HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getLang().getString("GeneralHover")));
	}
	
	public void pastNextPage(Player player, String path,
			int page, boolean lastpage, String cmdstring, String...objects)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		TextComponent MSG = ChatApiOld.tctl("");
		List<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString(path+"Past"));
			String cmd = cmdstring+" "+String.valueOf(j);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString(path+"Next"));
			String cmd = cmdstring+" "+String.valueOf(i);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApiOld.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}

}