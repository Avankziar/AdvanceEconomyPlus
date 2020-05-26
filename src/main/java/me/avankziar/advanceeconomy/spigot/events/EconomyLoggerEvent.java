package main.java.me.avankziar.advanceeconomy.spigot.events;

import java.time.LocalDateTime;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EconomyLoggerEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	public enum Type
	{
		DEPOSIT_WITHDRAW, TAKEN, GIVEN
	}
	
	private boolean isCancelled;
	private LocalDateTime dateTime;
	private String fromUUIDOrNumber;
	private String toUUIDOrNumber;
	private String fromName;
	private String toName;
	private String ordererUUID;
	private double amount;
	private EconomyLoggerEvent.Type type;
	private String comment;
	
	public EconomyLoggerEvent(LocalDateTime dateTime, String fromUUIDOrNumber, String toUUIDOrNumber,
			String fromName, String toName, String ordererUUID, double amount, EconomyLoggerEvent.Type type, String comment)
	{
		setDateTime(dateTime);
		setFromUUIDOrNumber(fromUUIDOrNumber);
		setToUUIDOrNumber(toUUIDOrNumber);
		setFromName(fromName);
		setToName(toName);
		setOrdererUUID(ordererUUID);
		setAmount(amount);
		setType(type);
		setComment(comment);
		setCancelled(false);
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

	public String getFromUUIDOrNumber()
	{
		return fromUUIDOrNumber;
	}

	public void setFromUUIDOrNumber(String fromUUIDOrNumber)
	{
		this.fromUUIDOrNumber = fromUUIDOrNumber;
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

	public String getOrdererUUID()
	{
		return ordererUUID;
	}

	public void setOrdererUUID(String ordererUUID)
	{
		this.ordererUUID = ordererUUID;
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

	public LocalDateTime getDateTime()
	{
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime)
	{
		this.dateTime = dateTime;
	}
}