package main.java.me.avankziar.advanceeconomy.spigot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;

public class TABCompletion implements TabCompleter
{	
	public TABCompletion()
	{
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		Player player = (Player)sender;
		List<String> list = new ArrayList<String>();
		if (cmd.getName().equalsIgnoreCase("econ") && args.length == 1) 
		{
			if (!args[0].equals("")) 
			{
				for (String commandString : AdvanceEconomy.ecoarguments.keySet()) 
				{
					CommandModule mod = AdvanceEconomy.ecoarguments.get(commandString);
					if (player.hasPermission(mod.permission))
					{
						if (commandString.startsWith(args[0])) 
						{
							list.add(commandString);
						}
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				for (String commandString : AdvanceEconomy.ecoarguments.keySet()) 
				{
					CommandModule mod = AdvanceEconomy.ecoarguments.get(commandString);
					if (player.hasPermission(mod.permission)) 
					{
						list.add(commandString);
					}
				}
				Collections.sort(list);
				return list;
			}
		} else if (cmd.getName().equalsIgnoreCase("money") && args.length == 1) 
		{
			if (!args[0].equals("")) 
			{
				for (String commandString : AdvanceEconomy.moneyarguments.keySet()) 
				{
					CommandModule mod = AdvanceEconomy.moneyarguments.get(commandString);
					if (player.hasPermission(mod.permission))
					{
						if (commandString.startsWith(args[0])) 
						{
							list.add(commandString);
						}
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				for (String commandString : AdvanceEconomy.moneyarguments.keySet()) 
				{
					CommandModule mod = AdvanceEconomy.moneyarguments.get(commandString);
					if (player.hasPermission(mod.permission)) 
					{
						list.add(commandString);
					}
				}
				Collections.sort(list);
				return list;
			}
		} else if (cmd.getName().equalsIgnoreCase("bank") && args.length == 1) 
		{
			if (!args[0].equals("")) 
			{
				for (String commandString : AdvanceEconomy.bankarguments.keySet()) 
				{
					CommandModule mod = AdvanceEconomy.bankarguments.get(commandString);
					if (player.hasPermission(mod.permission))
					{
						if (commandString.startsWith(args[0])) 
						{
							list.add(commandString);
						}
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				for (String commandString : AdvanceEconomy.bankarguments.keySet()) 
				{
					CommandModule mod = AdvanceEconomy.bankarguments.get(commandString);
					if (player.hasPermission(mod.permission)) 
					{
						list.add(commandString);
					}
				}
				Collections.sort(list);
				return list;
			}
		}
		return null;
	}
}
