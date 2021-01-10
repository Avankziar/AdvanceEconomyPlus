package main.java.me.avankziar.aep.spigot.cmd;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import net.md_5.bungee.api.chat.ClickEvent;

public class MoneyCommandExecutor implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private static CommandConstructor cc;
	
	public MoneyCommandExecutor(AdvancedEconomyPlus plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		MoneyCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		Player player = null;
		if (sender instanceof Player) 
		{
			player = (Player) sender;
		}
		if(cc == null)
		{
			return false;
		}
		if(args.length == 0)
		{
			if(cc.canConsoleAccess() && player == null)
			{
				AdvancedEconomyPlus.log.warning("Console cannot access the /money command!");
			} else
			{
				if(!player.hasPermission(cc.getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
					return false;
				}
				money(player);
				return true;
			}
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
						ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
						if(ac.canConsoleAccess() && player == null)
						{
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
								return false;
							}
							return false;
						} else if(player != null)
						{
							if(player.hasPermission(ac.getPermission()))
							{
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
									player.sendMessage(ChatApi.tl(
											"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
											.replace("%ac%", ac.getName())));
									return false;
								}
								return false;
							} else
							{
								///Du hast dafür keine Rechte!
								player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPermission")));
								return false;
							}
						} else
						{
							AdvancedEconomyPlus.log.info("Cannot access ArgumentModule! Command is not for ConsoleAccess and Executer is Console "
									+ "or Executor is Player and a other Error set place!"
									.replace("%ac%", ac.getName()));
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
		/*String arg = "";
		for(String s : args)
		{
			arg += s;
		}*/
		if(player != null)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, AdvancedEconomyPlus.infoCommand));
			//AdvancedEconomyPlus.log.info(sender.getName() + " send command: " + cmd.getName() + arg);
		} else
		{
			sender.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, AdvancedEconomyPlus.infoCommand).toLegacyText());
			//AdvancedEconomyPlus.log.info(sender.getName() + " send command: " + cmd.getName() + arg);
		}
		
		return false;
	}
	
	public void money(Player player)
	{
		AEPUser eco = AEPUserHandler.getEcoPlayer(player);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.PlayerBalance")
				.replace("%time%", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
				.replace("%balance%", AdvancedEconomyPlus.getVault().format(eco.getBalance()))
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())));
		return;
	}
}