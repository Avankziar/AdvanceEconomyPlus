package main.java.me.avankziar.aep.spigot.assistance;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;

public class Utility
{
	private static AdvancedEconomyPlus plugin;
	
	public Utility(AdvancedEconomyPlus plugin)
	{
		Utility.plugin = plugin;
		setPermissions();
	}
	
	public static String 
	PERM_CMD_MONEY_LOG_OTHER = "",
	PERM_CMD_MONEY_FILTERLOGOTHER = "",
	PERM_CMD_MONEY_TRENDDIAGRAM_OTHER = "",
	PERM_CMD_MONEY_TRENDGRAFIC_OTHER = "",
	PERM_CMD_MONEY_TRENDLOG_OTHER = "",
	PERM_BYPASS_RECOMMENT = "",
	PERM_CMD_ECO_DELETELOG = "",
	PERM_BYPASS_LOGOTHER = "",
	PERM_BYPASS_STANDINGORDER_CREATE = "",
	PERM_BYPASS_STANDINGORDER_INFO = "",
	PERM_BYPASS_STANDINGORDER_DELETE = "",
	PERM_BYPASS_STANDINGORDER_PAUSE = "",
	PERM_BYPASS_STANDINGORDER_LIST = "",
	PERM_BYPASS_LOAN_CREATE = "",
	PERM_BYPASS_LOAN_FORGIVE = "",
	PERM_BYPASS_LOAN_INFO = "",
	PERM_BYPASS_LOAN_LIST = "",
	PERM_BYPASS_LOAN_PAUSE = "",
	PERM_BYPASS_LOAN_TRANSFER = "";
	
	public void setPermissions()
	{
		PERM_CMD_MONEY_LOG_OTHER = plugin.getYamlHandler().getCom().getString("Bypass.LogOther", "eco.cmd.money.logother");
		PERM_CMD_MONEY_FILTERLOGOTHER = plugin.getYamlHandler().getCom().getString("Bypass.FilterLogOther", "eco.cmd.money.filterlogother");
		PERM_CMD_MONEY_TRENDDIAGRAM_OTHER = plugin.getYamlHandler().getCom().getString("Bypass.TrendDiagramOther", "eco.cmd.money.trenddiagramother");
		PERM_CMD_MONEY_TRENDGRAFIC_OTHER = plugin.getYamlHandler().getCom().getString("Bypass.TrendGraficOther", "eco.cmd.money.trendgraficother");
		PERM_CMD_MONEY_TRENDLOG_OTHER = plugin.getYamlHandler().getCom().getString("Bypass.TrendLogOther", "eco.cmd.money.trendlogother");
		PERM_BYPASS_RECOMMENT = plugin.getYamlHandler().getCom().getString("Bypass.Recomment", "eco.cmd.bypass.recomment");
		PERM_CMD_ECO_DELETELOG = plugin.getYamlHandler().getCom().getString("eco_deletelog.Permission", "eco.cmd.eco.deletelog");
		PERM_BYPASS_LOGOTHER = plugin.getYamlHandler().getCom().getString("Bypass.FilterSettingsLogOther", "eco.bypass.logother");
		PERM_BYPASS_STANDINGORDER_CREATE = plugin.getYamlHandler().getCom().getString("Bypass.StandingOrderCreate", "eco.cmd.bypass.standingorder.create");
		PERM_BYPASS_STANDINGORDER_INFO = plugin.getYamlHandler().getCom().getString("Bypass.StandingOrderInfo", "eco.cmd.bypass.standingorder.info");
		PERM_BYPASS_STANDINGORDER_DELETE = plugin.getYamlHandler().getCom().getString("Bypass.StandingOrderDelete", "eco.cmd.bypass.standingorder.delete");
		PERM_BYPASS_STANDINGORDER_PAUSE = plugin.getYamlHandler().getCom().getString("Bypass.StandingOrderPause", "eco.cmd.bypass.standingorder.pause");
		PERM_BYPASS_STANDINGORDER_LIST = plugin.getYamlHandler().getCom().getString("Bypass.StandingOrderList", "eco.cmd.bypass.standingorder.list");
		PERM_BYPASS_LOAN_CREATE = plugin.getYamlHandler().getCom().getString("Bypass.LoanCreate", "eco.cmd.bypass.loan.create");
		PERM_BYPASS_LOAN_FORGIVE = plugin.getYamlHandler().getCom().getString("Bypass.LoanForgive", "eco.cmd.bypass.loan.forgive");
		PERM_BYPASS_LOAN_INFO = plugin.getYamlHandler().getCom().getString("Bypass.LoanInfo", "eco.cmd.bypass.loan.info");
		PERM_BYPASS_LOAN_LIST = plugin.getYamlHandler().getCom().getString("Bypass.LoanList", "eco.cmd.bypass.loan.list");
		PERM_BYPASS_LOAN_PAUSE = plugin.getYamlHandler().getCom().getString("Bypass.LoanPause", "eco.cmd.bypass.loan.pause");
		PERM_BYPASS_LOAN_TRANSFER = plugin.getYamlHandler().getCom().getString("Bypass.LoanTransfer", "eco.cmd.bypass.loan.transfer");
	}
	
	public static double getNumberFormat(double d)//FIN
	{
		BigDecimal bd = new BigDecimal(d).setScale(1, RoundingMode.HALF_UP);
		double newd = bd.doubleValue();
		return newd;
	}
	
	public static double getNumberFormat(double d, int scale)//FIN
	{
		BigDecimal bd = new BigDecimal(d).setScale(scale, RoundingMode.HALF_UP);
		double newd = bd.doubleValue();
		return newd;
	}
	
	public static String convertUUIDToName(String uuid) throws IOException
	{
		String name = null;
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYER, "player_uuid = ?", uuid))
		{
			name = ((AEPUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYER,
					"player_uuid = ?", uuid)).getName();
			return name;
		}
		return null;
	}
	
	public static String convertNameToUUID(String playername)
	{
		String uuid = "";
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYER, "`player_name` = ?", playername))
		{
			uuid = ((AEPUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYER, "`player_name` = ?", playername)).getUUID();
			return uuid;
		}
		return null;
	}
	
	public boolean existMethod(Class<?> externclass, String method)
	{
	    try 
	    {
	    	Method[] mtds = externclass.getMethods();
	    	for(Method methods : mtds)
	    	{
	    		if(methods.getName().equalsIgnoreCase(method))
	    		{
	    	    	//SimpleChatChannels.log.info("Method "+method+" in Class "+externclass.getName()+" loaded!");
	    	    	return true;
	    		}
	    	}
	    	return false;
	    } catch (Exception e) 
	    {
	    	return false;
	    }
	}
	
	public static String serialised(LocalDateTime dt)
	{
		String MM = "";
		int month = 0;
		if(dt.getMonthValue()<10)
		{
			MM+=month;
		}
		MM += dt.getMonthValue();
		String dd = "";
		int day = 0;
		if(dt.getDayOfMonth()<10)
		{
			dd+=day;
		}
		dd +=dt.getDayOfMonth();
		String hh = "";
		int hour = 0;
		if(dt.getHour()<10)
		{
			hh+=hour;
		}
		hh += dt.getHour();
		String mm = "";
		int min = 0;
		if(dt.getMinute()<10)
		{
			mm+=min;
		}
		mm += dt.getMinute();
		return dd+"."+MM+"."+dt.getYear()+" "+hh+":"+mm;
	}
	
	public static double round(double value, int places) 
	{
	    if (places < 0) throw new IllegalArgumentException();
	    try
	    {
	    	BigDecimal bd = BigDecimal.valueOf(value);
		    bd = bd.setScale(places, RoundingMode.HALF_UP);
		    return bd.doubleValue();
	    } catch (NumberFormatException e)
	    {
	    	return 0;
	    }
	}
}
