package main.java.me.avankziar.aep.spigot.api;

import java.util.UUID;

public class MatchApi
{
	//https://regex101.com/ für regular expression zu bauen
	/*public static boolean isBankAccountNumber(String numberstring)
	{
		if(numberstring == null)
		{
			return false;
		}
		switch(AEPSettings.settings.getNumberType())
		{
		case TWO_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{2}$")) {return true;}
			else {return false;}
		case TWO_DIGITS_TIMES_TWO:
			if(numberstring.matches("^[0-9]{2}-[0-9]{2}$")) {return true;}
			else {return false;}
		case TWO_DIGITS_TIMES_THREE:
			if(numberstring.matches("^[0-9]{2}-[0-9]{2}-[0-9]{2}$")) {return true;}
			else {return false;}
		case TWO_DIGITS_TIMES_FOUR:
			if(numberstring.matches("^[0-9]{2}-[0-9]{2}-[0-9]{2}-[0-9]{2}$")) {return true;}
			else {return false;}
		case TWO_DIGITS_TIMES_FIVE:
			if(numberstring.matches("^[0-9]{2}-[0-9]{2}-[0-9]{2}-[0-9]{2}-[0-9]{2}$")) {return true;}
			else {return false;}
		case THREE_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{3}$")) {return true;}
			else {return false;}
		case THREE_DIGITS_TIMES_TWO:
			if(numberstring.matches("^[0-9]{3}-[0-9]{3}$")) {return true;}
			else {return false;}
		case THREE_DIGITS_TIMES_THREE:
			if(numberstring.matches("^[0-9]{3}-[0-9]{3}-[0-9]{3}$")) {return true;}
			else {return false;}
		case THREE_DIGITS_TIMES_FOUR:
			if(numberstring.matches("^[0-9]{3}-[0-9]{3}-[0-9]{3}-[0-9]{3}$")) {return true;}
			else {return false;}
		case FOUR_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{4}$")) {return true;}
			else {return false;}
		case FOUR_DIGITS_TIMES_TWO:
			if(numberstring.matches("^[0-9]{4}-[0-9]{4}$")) {return true;}
			else {return false;}
		case FOUR_DIGITS_TIMES_THREE:
			if(numberstring.matches("^[0-9]{4}-[0-9]{4}-[0-9]{4}$")) {return true;}
			else {return false;}
		case FIVE_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{5}$")) {return true;}
			else {return false;}
		case FIVE_DIGITS_TIMES_TWO:
			if(numberstring.matches("^[0-9]{5}-[0-9]{5}$")) {return true;}
			else {return false;}
		case SIX_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{6}$")) {return true;}
			else {return false;}
		case SIX_DIGITS_TIMES_TWO:
			if(numberstring.matches("^[0-9]{6}-[0-9]{6}$")) {return true;}
			else {return false;}
		case SEVEN_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{7}$")) {return true;}
			else {return false;}
		case SEVEN_DIGITS_TIMES_TWO:
			if(numberstring.matches("^[0-9]{7}-[0-9]{7}$")) {return true;}
			else {return false;}
		case EIGHT_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{8}$")) {return true;}
			else {return false;}
		case NINE_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{9}$")) {return true;}
			else {return false;}
		case TEN_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{10}$")) {return true;}
			else {return false;}
		case ELEVEN_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{11}$")) {return true;}
			else {return false;}
		case TWELVE_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{12}$")) {return true;}
			else {return false;}
		case THRITEEN_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{13}$")) {return true;}
			else {return false;}
		case FOURTEEN_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{14}$")) {return true;}
			else {return false;}
		case FIFTEEN_DIGITS_TIMES_ONE:
			if(numberstring.matches("^[0-9]{15}$")) {return true;}
			else {return false;}
		}
		return false;
	}*/
	
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
}
