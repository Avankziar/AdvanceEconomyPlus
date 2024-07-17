package me.avankziar.aep.spigot.cmd.sub;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import me.avankziar.aep.spigot.cmd.tree.CommandStructurType;

public class TabCompletion implements TabCompleter
{
	private AEP plugin;
	private CommandStructurType cst;
	private CommandExecuteType cet;
	
	public TabCompletion(AEP plugin, CommandExecuteType type, CommandStructurType cst)
	{
		this.plugin = plugin;
		this.cet = type;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			 String lable, String[] args)
	{
		if(!(sender instanceof Player))
		{
			return null;
		}
		List<String> list = new ArrayList<>();
		switch(cet)
		{
		case BALANCE:
			if(cst == CommandStructurType.SINGLE)
			{
				
			} else
			{
				
			}
		}
		return list;
	}
}