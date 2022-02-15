package main.java.me.avankziar.aep.spigot.cmd.sub;

import java.util.LinkedHashMap;

import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class CommandSuggest
{
	public static LinkedHashMap<String, LinkedHashMap<CommandExecuteType, String>> map = new LinkedHashMap<>();
	public static LinkedHashMap<CommandExecuteType, String> othermap = new LinkedHashMap<>();
	
	public static void set(EconomyCurrency ecc, CommandExecuteType ces, String s)
	{
		if(s == null)
		{
			return;
		}
		if(ecc == null)
		{
			othermap.put(ces, s);
		} else
		{
			if(map.containsKey(ecc.getUniqueName()))
			{
				LinkedHashMap<CommandExecuteType, String> sub = map.get(ecc.getUniqueName());
				sub.put(ces, s);
				map.put(ecc.getUniqueName(), sub);
			} else
			{
				LinkedHashMap<CommandExecuteType, String> sub = new LinkedHashMap<>();
				sub.put(ces, s);
				map.put(ecc.getUniqueName(), sub);
			}
		}
	}
	
	public static String get(EconomyCurrency ecc, CommandExecuteType ces)
	{
		if(ecc == null)
		{
			return othermap.get(ces);
		} else
		{
			if(map.containsKey(ecc.getUniqueName()))
			{
				return map.get(ecc.getUniqueName()).get(ces);
			}
		}
		return null;
	}
}
