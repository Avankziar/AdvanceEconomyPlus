package main.java.me.avankziar.aep.spigot.cmd.cst.sub;

import main.java.me.avankziar.aep.spigot.object.CommandExecuteType;

public class CommandSuggest
{
	public static String EDITLOG = "";
	public static String DELETELOG = "";
	public static String BALANCE = "";
	public static String PAY = "";
	public static String GIVE = "";
	public static String GIVE_CONSOLE = "";
	public static String SET = "";
	public static String SET_CONSOLE = "";
	public static String TAKE = "";
	public static String TAKE_CONSOLE = "";
	public static String WALLETNOTIFICATION = "";
	public static String BANKNOTIFICATION = "";
	public static String ACTIONLOG = "";
	public static String TRENDLOG = "";
	public static String LOGGERSETTINGS = "";
	public static String LOGGERSETTINGS_GUI = "";
	public static String LOGGERSETTINGS_OTHER = "";
	public static String LOGGERSETTINGS_TEXT = "";
	public static String ACCOUNT_OPEN = "";
	public static String ACCOUNT_CLOSE = "";
	public static String ACCOUNT_INFO = "";
	public static String ACCOUNT_SETNAME = "";
	public static String ACCOUNT_SETOWNER = "";
	public static String ACCOUNT_SETCATEGORY = "";
	public static String ACCOUNT_MANAGE = "";
	public static String ACCOUNT_SETSHORTPAY = "";
	public static String ACCOUNT_SETDEFAULT = "";
	public static String CURRENCY_INFO = "";
	public static String CURRENCY_EXCHANGE = "";
	public static String TOPRANKING = "";
	public static String TOPLIST = "";
	
	public static void set(CommandExecuteType ces, String s)
	{
		switch(ces)
		{
		case EDITLOG:
			EDITLOG = s;
			break;
		case DELETELOG:
			DELETELOG = s;
			break;
		case BALANCE:
			BALANCE = s;
			break;
		case ACTIONLOG:
			ACTIONLOG = s;
			break;
		case TRENDLOG:
			TRENDLOG = s;
			break;
		case PAY:
			PAY = s;
			break;
		case GIVE:
			GIVE = s;
			break;
		case GIVE_CONSOLE:
			GIVE_CONSOLE = s;
			break;
		case TAKE:
			TAKE = s;
			break;
		case TAKE_CONSOLE:
			TAKE_CONSOLE = s;
			break;
		case SET:
			SET = s;
			break;
		case SET_CONSOLE:
			SET_CONSOLE = s;
			break;
		case WALLETNOTIFICATION:
			WALLETNOTIFICATION = s;
			break;
		case BANKNOTIFICATION:
			BANKNOTIFICATION = s;
			break;
		case ACCOUNT_CLOSE:
			ACCOUNT_CLOSE = s;
			break;
		case ACCOUNT_INFO:
			ACCOUNT_INFO = s;
			break;
		case ACCOUNT_MANAGE:
			ACCOUNT_MANAGE = s;
			break;
		case ACCOUNT_OPEN:
			ACCOUNT_OPEN = s;
			break;
		case ACCOUNT_SETCATEGORY:
			ACCOUNT_SETCATEGORY = s;
			break;
		case ACCOUNT_SETDEFAULT:
			ACCOUNT_SETDEFAULT = s;
			break;
		case ACCOUNT_SETNAME:
			ACCOUNT_SETNAME = s;
			break;
		case ACCOUNT_SETOWNER:
			ACCOUNT_SETOWNER = s;
			break;
		case ACCOUNT_SETSHORTPAY:
			ACCOUNT_SETSHORTPAY = s;
			break;
		case CURRENCY_INFO:
			CURRENCY_INFO = s;
			break;
		case CURRENCY_EXCHANGE:
			CURRENCY_EXCHANGE = s;
			break;
		case LOGGERSETTINGS:
			LOGGERSETTINGS = s;
			break;
		case LOGGERSETTINGS_GUI:
			LOGGERSETTINGS_GUI = s;
			break;
		case LOGGERSETTINGS_OTHER:
			LOGGERSETTINGS_OTHER = s;
			break;
		case LOGGERSETTINGS_TEXT:
			LOGGERSETTINGS_TEXT = s;
			break;
		case TOPRANKING:
			TOPRANKING = s;
			break;
		case TOPLIST:
			TOPLIST = s;
			break;
		}
	}
}
