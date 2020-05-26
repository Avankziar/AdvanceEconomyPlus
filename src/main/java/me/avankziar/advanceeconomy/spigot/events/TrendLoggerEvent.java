package main.java.me.avankziar.advanceeconomy.spigot.events;

import java.time.LocalDate;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TrendLoggerEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean isCancelled;	
	private LocalDate date;
	private String UUIDOrNumber;
	private double relativeAmountChange;
	private double balance;
	
	public TrendLoggerEvent(LocalDate date, String UUIDOrNumber, double relativeAmountChange,
			double balance)
	{
		setCancelled(false);
		setDate(date);
		setUUIDOrNumber(UUIDOrNumber);
		setRelativeAmountChange(relativeAmountChange);
		setBalance(balance);
	}
	
	public HandlerList getHandlers() 
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() 
    {
        return HANDLERS;
    }

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
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

	public double getBalance()
	{
		return balance;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

}
