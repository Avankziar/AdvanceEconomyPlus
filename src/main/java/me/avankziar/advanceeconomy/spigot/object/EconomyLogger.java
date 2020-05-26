package main.java.me.avankziar.advanceeconomy.spigot.object;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EconomyLogger
{
	public enum Type
	{
		DEPOSIT_WITHDRAW, TAKEN, GIVEN
	}
	
	private int id;
	private LocalDateTime dateTime;
	private String fromUUIDOrNumber;
	private String toUUIDOrNumber;
	private String fromName; //Bankname oder Playername
	private String toName;
	private String ordereruuid;
	private double amount;
	private Type type;
	private String comment;
	
	public EconomyLogger(int id, LocalDateTime dateTime,
			String FromUUIDOrNumber, String toUUIDOrNumber, String fromName, String toName, String ordereruuid,
			double amount, Type type, String comment)
	{
		setId(id);
		setDateTime(dateTime);
		setFromUUIDOrNumber(FromUUIDOrNumber);
		setFromName(fromName);
		setToUUIDOrNumber(toUUIDOrNumber);
		setToName(toName);
		setOrdereruuid(ordereruuid);
		setAmount(amount);
		setType(type);
		setComment(comment);
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
	
	public static ArrayList<EconomyLogger> convertList(ArrayList<?> list)
	{
		ArrayList<EconomyLogger> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof EconomyLogger)
			{
				el.add((EconomyLogger) o);
			} else
			{
				return null;
			}
		}
		return el;
	}

	public LocalDateTime getDateTime()
	{
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime)
	{
		this.dateTime = dateTime;
	}

	public String getFromUUIDOrNumber()
	{
		return fromUUIDOrNumber;
	}

	public void setFromUUIDOrNumber(String FromUUIDOrNumber)
	{
		this.fromUUIDOrNumber = FromUUIDOrNumber;
	}

	public String getToUUIDOrNumber()
	{
		return toUUIDOrNumber;
	}

	public void setToUUIDOrNumber(String toUUIDOrNumber)
	{
		this.toUUIDOrNumber = toUUIDOrNumber;
	}

	public String getFromName()
	{
		return fromName;
	}

	public void setFromName(String fromName)
	{
		this.fromName = fromName;
	}

	public String getToName()
	{
		return toName;
	}

	public void setToName(String toName)
	{
		this.toName = toName;
	}

	public String getOrdereruuid()
	{
		return ordereruuid;
	}

	public void setOrdereruuid(String ordereruuid)
	{
		this.ordereruuid = ordereruuid;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
