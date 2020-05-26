package main.java.me.avankziar.advanceeconomy.spigot.object;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TrendLogger
{
	public enum Type
	{
		STABIL, UP, DOWN;
	}
	
	private LocalDate date;
	private Type type;
	private String UUIDOrNumber;
	private double relativeAmountChange;
	private double firstValue;
	private double lastValue;
	
	public TrendLogger(LocalDate date, Type type, String UUIDOrNumber, double relativeAmountChange,
			double firstValue, double lastValue)
	{
		setDate(date);
		setType(type);
		setUUIDOrNumber(UUIDOrNumber);
		setRelativeAmountChange(relativeAmountChange);
		setFirstValue(firstValue);
		setLastValue(lastValue);
	}
	
	public static LocalDate deserialised(String date)
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
	
	public static ArrayList<TrendLogger> convertList(ArrayList<?> list)
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

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public String getUUIDOrNumber()
	{
		return UUIDOrNumber;
	}

	public void setUUIDOrNumber(String uUIDOrNumber)
	{
		UUIDOrNumber = uUIDOrNumber;
	}

	public double getRelativeAmountChange()
	{
		return relativeAmountChange;
	}

	public void setRelativeAmountChange(double relativeAmountChange)
	{
		this.relativeAmountChange = relativeAmountChange;
	}

	public double getFirstValue()
	{
		return firstValue;
	}

	public void setFirstValue(double firstValue)
	{
		this.firstValue = firstValue;
	}

	public double getLastValue()
	{
		return lastValue;
	}

	public void setLastValue(double lastValue)
	{
		this.lastValue = lastValue;
	}

}
