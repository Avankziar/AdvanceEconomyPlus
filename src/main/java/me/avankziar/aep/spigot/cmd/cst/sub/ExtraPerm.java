package main.java.me.avankziar.aep.spigot.cmd.cst.sub;

import java.util.LinkedHashMap;

public class ExtraPerm
{
	public enum Type
	{
		BYPASS_ACCOUNTMANAGEMENT,
		BYPASS_ACTIONLOG_OTHER,
		BYPASS_TRENDLOG_OTHER,
		BYPASS_EDITLOG,
		BYPASS_DELETELOG,
	}
	
	public static LinkedHashMap<Type, String> map = new LinkedHashMap<>();
	
	public static void set(Type t, String s)
	{
		map.put(t, s);
	}
}
