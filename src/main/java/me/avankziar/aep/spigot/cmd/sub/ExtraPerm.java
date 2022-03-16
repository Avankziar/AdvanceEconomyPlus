package main.java.me.avankziar.aep.spigot.cmd.sub;

import java.util.LinkedHashMap;

public class ExtraPerm
{
	public enum Type
	{
		COUNT_ACCOUNT,
		BYPASS_JOINLISTENER,
		BYPASS_ACCOUNTOPEN,
		BYPASS_ACCOUNTMANAGEMENT,
		BYPASS_ACTIONLOG_OTHER,
		BYPASS_TRENDLOG_OTHER,
		BYPASS_LOGGERSETTINGS_OTHER,
		BYPASS_RECOMMENT,
		BYPASS_DELETELOG,
		BYPASS_STANDINGORDER,
		BYPASS_LOAN,
		CAN_SETDEFAULTACCOUNT,
		CAN_SETACCOUNTNAME
	}
	
	private static LinkedHashMap<Type, String> map = new LinkedHashMap<>();
	
	public static void set(Type t, String s)
	{
		map.put(t, s);
	}
	
	public static String get(Type t)
	{
		return map.get(t);
	}
}
