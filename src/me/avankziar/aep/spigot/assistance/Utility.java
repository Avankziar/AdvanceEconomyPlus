package me.avankziar.aep.spigot.assistance;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.EntityData;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.ifh.general.economy.account.EconomyEntity;

public class Utility
{
	private static AEP plugin;
	
	public Utility(AEP plugin)
	{
		Utility.plugin = plugin;
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
			if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid, type.toString()))
			{
				return ((EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
						"`entity_uuid` = ? AND `entity_type` = ?", uuid, type.toString())).getName();
			}
			break;
		case PLAYER:
			if(plugin.getMysqlHandler().exist(MysqlType.PLAYERDATA, "`player_uuid` = ?", uuid))
			{
				return ((AEPUser) plugin.getMysqlHandler().getData(MysqlType.PLAYERDATA, "`player_uuid` = ?", uuid)).getName();
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
			if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, type.toString()))
			{
				return ((EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
						"`entity_name` = ? AND `entity_type` = ?", name, type.toString())).getUUID();
			}
			break;
		case PLAYER:
			if(plugin.getMysqlHandler().exist(MysqlType.PLAYERDATA, "`player_name` = ?", name))
			{
				return ((AEPUser) plugin.getMysqlHandler().getData(MysqlType.PLAYERDATA, "`player_name` = ?", name)).getUUID();
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
