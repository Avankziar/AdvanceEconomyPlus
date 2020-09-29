package main.java.me.avankziar.aep.spigot.object;

import java.time.LocalDate;

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
