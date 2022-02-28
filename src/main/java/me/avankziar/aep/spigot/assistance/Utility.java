package main.java.me.avankziar.aep.spigot.assistance;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.ne_w.AEPUser;
import main.java.me.avankziar.aep.spigot.object.ne_w.EntityData;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;

public class Utility
{
	private static AdvancedEconomyPlus plugin;
	
	public Utility(AdvancedEconomyPlus plugin)
	{
		Utility.plugin = plugin;
		setPermissions();
	}
	
	public static String
	PERM_CMD_MONEY_LOGGERSETTINGSLOGOTHER = "",
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
		PERM_CMD_MONEY_LOGGERSETTINGSLOGOTHER = plugin.getYamlHandler().getCom().getString("Bypass.LoggerSettingsLogOther", "eco.cmd.money.filterlogother");
		PERM_BYPASS_RECOMMENT = plugin.getYamlHandler().getCom().getString("Bypass.Recomment", "eco.cmd.bypass.recomment");
		PERM_CMD_ECO_DELETELOG = plugin.getYamlHandler().getCom().getString("eco_deletelog.Permission", "eco.cmd.eco.deletelog");
		PERM_BYPASS_LOGOTHER = plugin.getYamlHandler().getCom().getString("Bypass.LogOther", "eco.bypass.logother");
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
	
	public static String convertUUIDToName(String uuid, EconomyEntity.EconomyType type)
	{
		switch(type)
		{
		case ENTITY:
		case SERVER:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid, type.toString()))
			{
				return ((EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
						"`entity_uuid` = ? AND `entity_type` = ?", uuid, type.toString())).getName();
			}
			break;
		case PLAYER:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", uuid))
			{
				return ((AEPUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", uuid)).getName();
			}
			break;
		}
		return null;
	}
	
	public static UUID convertNameToUUID(String name, EconomyEntity.EconomyType type)
	{
		switch(type)
		{
		case ENTITY:
		case SERVER:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, type.toString()))
			{
				return ((EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
						"`entity_name` = ? AND `entity_type` = ?", name, type.toString())).getUUID();
			}
			break;
		case PLAYER:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", name))
			{
				return ((AEPUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.OLDPLAYER, "`player_name` = ?", name)).getUUID();
			}
			break;
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
