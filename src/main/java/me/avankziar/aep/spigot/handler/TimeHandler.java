package main.java.me.avankziar.aep.spigot.handler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import main.java.me.avankziar.aep.spigot.api.MatchApi;

public class TimeHandler
{
	//private final static long ss = 1000;
	private static long mm = 1000*60;
	private static long HH = 1000*60*60;
	private static long dd = 1000*60*60*24;
	//private final static long yyyy = 1000*60*60*24*365;
	
	public static String getTime(long l)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss"));
	}
	
	public static String getTimeSlim(long l)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("dd.MM-HH:mm"));
	}
	
	public static long getTime(String l)
	{
		try
		{
			return LocalDateTime.parse(l, DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss"))
				.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		} catch(Exception e)
		{
			return 0;
		}
	}
	
	public static long getTime(LocalDateTime l)
	{
		return l.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();	
	}
	
	public static long getTime(LocalDate l)
	{
		return l.toEpochSecond(LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)*1000;
	}
	
	public static LocalDateTime getLocalDateTime(long l)
	{
		return LocalDateTime.parse(getTime(l), DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss"));
	}
	
	public static String getRepeatingTime(long l) // dd-HH:mm
	{
		long ll = l;
		String time = "";
		int d = 0;
		while(ll >= dd)
		{
			ll = ll - dd;
			d++;
		}
		time += String.valueOf(d)+"-";
		int H = 0;
		while(ll >= HH)
		{
			ll = ll - HH;
			H++;
		}
		if(H < 10)
		{
			time += String.valueOf(0);
		}
		time += String.valueOf(H)+":";
		int m = 0;
		while(ll >= mm)
		{
			ll = ll - mm;
			m++;
		}
		if(m < 10)
		{
			time += String.valueOf(0);
		}
		time += String.valueOf(m);
		return time;
	}
	
	public static long getRepeatingTime(String l) //dd-HH:mm
	{
		String[] a = l.split("-");
		if(a.length != 2)
		{
			return 0;
		}
		if(!MatchApi.isInteger(a[0]))
		{
			return 0;
		}
		int d = Integer.parseInt(a[0]);
		String[] b = a[1].split(":");
		if(b.length != 2)
		{
			return 0;
		}
		if(!MatchApi.isInteger(b[0]))
		{
			return 0;
		}
		if(!MatchApi.isInteger(b[1]))
		{
			return 0;
		}
		int H = Integer.parseInt(b[0]);
		int m = Integer.parseInt(b[1]);
		long time = d*dd + H*HH + m*mm;
		return time;
	}
}
