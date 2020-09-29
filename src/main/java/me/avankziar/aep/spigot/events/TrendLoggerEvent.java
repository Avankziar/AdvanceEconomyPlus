package main.java.me.avankziar.aep.spigot.events;

import java.time.LocalDate;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.me.avankziar.aep.spigot.object.TrendLogger;

public class TrendLoggerEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean isCancelled;	
	private LocalDate date;
	private String UUIDOrNumber;
	private double relativeAmountChange;
	private double balance;
	
	/**
	 * Call this Event to update the daily- or "trend"Log. Please keep in mind, that when you have a transaction
	 * from a player to another, than call this event <b>TWICE</b>! For each player one.
	 * <p><b>Do not call this event for the System or void or whatsoever you call it!
	 * @param  date | The exact local date.
	 * @param  UUIDOrNumber | The playeruuid or bankaccountnumber wich is involved.
	 * @param  relativeAmountChange | The relative money change.
	 * @author Christoph Steins/Avankziar
	 * @see TrendLogger
	 */
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
