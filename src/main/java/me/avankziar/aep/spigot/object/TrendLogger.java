package main.java.me.avankziar.aep.spigot.object;

import java.time.LocalDate;

import main.java.me.avankziar.aep.spigot.handler.TimeHandler;

public class TrendLogger
{
	public enum Type
	{
		STABIL, UP, DOWN;
	}
	
	private long unixTime;
	private Type type;
	private int accountID;
	private double relativeAmountChange;
	private double firstValue;
	private double lastValue;
	
	public TrendLogger(long unixTime, Type type, int accountID, double relativeAmountChange,
			double firstValue, double lastValue)
	{
		setUnixTime(unixTime);
		setType(type);
		setAccountID(accountID);
		setRelativeAmountChange(relativeAmountChange);
		setFirstValue(firstValue);
		setLastValue(lastValue);
	}
	
	public TrendLogger(LocalDate date, Type type, int accountID, double relativeAmountChange,
			double firstValue, double lastValue)
	{
		setUnixTime(TimeHandler.getTime(date));
		setType(type);
		setAccountID(accountID);
		setRelativeAmountChange(relativeAmountChange);
		setFirstValue(firstValue);
		setLastValue(lastValue);
	}

	/**
	 * @return the unixTime
	 */
	public long getUnixTime()
	{
		return unixTime;
	}

	/**
	 * @param unixTime the unixTime to set
	 */
	public void setUnixTime(long unixTime)
	{
		this.unixTime = unixTime;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	/**
	 * @return the accountID
	 */
	public int getAccountID()
	{
		return accountID;
	}

	/**
	 * @param accountID the accountID to set
	 */
	public void setAccountID(int accountID)
	{
		this.accountID = accountID;
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
