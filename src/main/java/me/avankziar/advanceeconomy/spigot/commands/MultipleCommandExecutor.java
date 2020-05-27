package main.java.me.avankziar.advanceeconomy.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import net.md_5.bungee.api.chat.ClickEvent;

public class MultipleCommandExecutor implements CommandExecutor 
{
	private AdvanceEconomy plugin;
	
	public MultipleCommandExecutor(AdvanceEconomy plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		// Checks if the label is one of yours.
		if (cmd.getName().equalsIgnoreCase("econ")) 
		{		
			if (!(sender instanceof Player)) 
			{
				AdvanceEconomy.log.info("/econ is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if (args.length == 0) 
			{
				plugin.getCommandHelper().eco(player, 0); //Info Command
				return false;
			}
			if(args.length == 1 && MatchApi.isInteger(args[0]))
			{
				plugin.getCommandHelper().eco(player, Integer.parseInt(args[0])); //Info Command
				return false;
			}
			if (AdvanceEconomy.ecoarguments.containsKey(args[0])) 
			{
				CommandModule mod = AdvanceEconomy.ecoarguments.get(args[0]);
				//Abfrage ob der Spieler die Permission hat
				if (player.hasPermission(mod.permission)) 
				{
					//Abfrage, ob der Spieler in den min und max Argumenten Bereich ist.
					if(args.length >= mod.minArgs && args.length <= mod.maxArgs)
					{
						mod.run(sender, args);
					} else
					{
						///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
						player.spigot().sendMessage(ChatApi.clickEvent(
								plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, "/econ"));
						return false;
					}
				} else 
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return false;
				}
			} else 
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/econ"));
				return false;
			}
		} else if(cmd.getName().equalsIgnoreCase("money"))
		{
			if (!(sender instanceof Player)) 
			{
				AdvanceEconomy.log.info("/money is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if (args.length == 0) 
			{
				plugin.getCommandHelper().money(player); //Info Command
				return false;
			}
			if (AdvanceEconomy.moneyarguments.containsKey(args[0])) 
			{
				CommandModule mod = AdvanceEconomy.moneyarguments.get(args[0]);
				//Abfrage ob der Spieler die Permission hat
				if (player.hasPermission(mod.permission)) 
				{
					//Abfrage, ob der Spieler in den min und max Argumenten Bereich ist.
					if(args.length >= mod.minArgs && args.length <= mod.maxArgs)
					{
						mod.run(sender, args);
					} else
					{
						///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
						player.spigot().sendMessage(ChatApi.clickEvent(
								plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, "/econ"));
						return false;
					}
				} else 
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return false;
				}
			} else 
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/econ"));
				return false;
			}
		} else if(cmd.getName().equalsIgnoreCase("bank"))
		{
			if (!(sender instanceof Player)) 
			{
				AdvanceEconomy.log.info("/bank is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if (args.length == 0) 
			{
				plugin.getCommandHelper().bank(player);
				return false;
			}
			if (AdvanceEconomy.bankarguments.containsKey(args[0])) 
			{
				CommandModule mod = AdvanceEconomy.bankarguments.get(args[0]);
				//Abfrage ob der Spieler die Permission hat
				if (player.hasPermission(mod.permission)) 
				{
					//Abfrage, ob der Spieler in den min und max Argumenten Bereich ist.
					if(args.length >= mod.minArgs && args.length <= mod.maxArgs)
					{
						mod.run(sender, args);
					} else
					{
						///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
						player.spigot().sendMessage(ChatApi.clickEvent(
								plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, "/econ"));
						return false;
					}
				} else 
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return false;
				}
			} else 
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/econ"));
				return false;
			}
		}
		return false;
	}
}
