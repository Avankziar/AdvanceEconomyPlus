package main.java.me.avankziar.aep.spigot.api;

import java.util.UUID;

import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.account.AccountType;

public class MatchApi
{
	//https://regex101.com/ für regular expression zu bauen
	
	public static boolean isNumber(String numberstring)
	{
		if(numberstring == null)
		{
			return false;
		}
		if(numberstring.matches("(([0-9]+[.]?[0-9]*)|([0-9]*[.]?[0-9]+))"))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isBoolean(String number)
	{
		if(number == null)
		{
			return false;
		}
		try
		{
			Boolean.parseBoolean(number);
			return true;
		} catch (Exception e) 
		{
			return false;
		}
	}
	
	public static boolean isInteger(String number)
	{
		if(number == null)
		{
			return false;
		}
		try
		{
			Integer.parseInt(number);
			return true;
		} catch (Exception e) 
		{
			return false;
		}
	}
	
	public static boolean isLong(String number)
	{
		if(number == null)
		{
			return false;
		}
		try
		{
			Long.parseLong(number);
			return true;
		} catch (Exception e) 
		{
			return false;
		}
	}
	
	public static boolean isDouble(String number)
	{
		if(number == null)
		{
			return false;
		}
		try
		{
			Double.parseDouble(number);
			return true;
		} catch (Exception e) 
		{
			return false;
		}
	}
	
	public static boolean isUUID(String string)
	{
		try 
		{
			UUID uuid = UUID.fromString(string);
			if(uuid != null)
			{
				return true;
			}
		} catch (IllegalArgumentException e)
		{
			return false;
		}
		return false;
	}
	
	public static boolean isPositivNumber(int number)
	{
		if(number >= 0)
		{
			return true;
		}
		return false;
	}
	
	public static boolean isPositivNumber(double number)
	{
		if(number >= 0.0)
		{
			return true;
		}
		return false;
	}
	
	public static boolean isAccountType(String s)
	{
		try 
		{
			AccountType.valueOf(s);
		} catch (IllegalArgumentException e)
		{
			return false;
		}
		return true;
	}
	public static boolean isAccountCategory(String s)
	{
		try 
		{
			AccountCategory.valueOf(s);
		} catch (IllegalArgumentException e)
		{
			return false;
		}
		return true;
	}
}
