package main.java.me.avankziar.aep.spigot.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import main.java.me.avankziar.aep.spigot.object.BankAccount;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import main.java.me.avankziar.aep.spigot.object.TrendLogger;

public class ConvertHandler
{
	public static ArrayList<AEPUser> convertListI(ArrayList<?> list)
	{
		ArrayList<AEPUser> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof AEPUser)
			{
				el.add((AEPUser) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<BankAccount> convertListII(ArrayList<?> list)
	{
		ArrayList<BankAccount> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof BankAccount)
			{
				el.add((BankAccount) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<ActionLogger> convertListIII(ArrayList<?> list)
	{
		ArrayList<ActionLogger> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof ActionLogger)
			{
				el.add((ActionLogger) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<TrendLogger> convertListIV(ArrayList<?> list)
	{
		ArrayList<TrendLogger> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof TrendLogger)
			{
				el.add((TrendLogger) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<StandingOrder> convertListV(ArrayList<?> list)
	{
		ArrayList<StandingOrder> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof StandingOrder)
			{
				el.add((StandingOrder) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<LoanRepayment> convertListVI(ArrayList<?> list)
	{
		ArrayList<LoanRepayment> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof LoanRepayment)
			{
				el.add((LoanRepayment) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static LocalDateTime deserialised(String datetime)
	{
		LocalDateTime dt = LocalDateTime.parse((CharSequence) datetime,
				DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		return dt;
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
		String ss = "";
		int sec = 0;
		if(dt.getSecond()<10)
		{
			ss+=sec;
		}
		ss += dt.getSecond();
		return dd+"."+MM+"."+dt.getYear()+" "+hh+":"+mm+":"+ss;
	}
	
	public static LocalDate deserialisedDate(String date)
	{
		LocalDate d = LocalDate.parse((CharSequence) date,
				DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		return d;
	}
	
	public static String serialised(LocalDate d)
	{
		String mm = "";
		int month = 0;
		if(d.getMonthValue()<10)
		{
			mm+=month;
		}
		mm += d.getMonthValue();
		String dd = "";
		int day = 0;
		if(d.getDayOfMonth()<10)
		{
			dd+=day;
		}
		dd +=d.getDayOfMonth();
		return dd+"."+mm+"."+d.getYear();
	}
}
