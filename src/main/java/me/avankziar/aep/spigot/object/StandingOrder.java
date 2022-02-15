package main.java.me.avankziar.aep.spigot.object;

import java.util.UUID;

public class StandingOrder
{
	//Dauerauftrag
	private int id;
	private String name;
	private UUID owner;
	private int accountFrom;
	private int accountTo;
	private double amount; //Betrag
	private double amountPaidSoFar; //Bisher gezahlter Betrag
	private double amountPaidToTax; //Gezahlter betrag an Steuern.
	private long startTime; //Anfangsdatum
	private long repeatingTime; //Millisekunden, wann wieder gezahlt wird.
	private long lastTime; //Millisekunden, wann das letzte man gezahl wurde.
	private boolean cancelled; //Wurde der Dauerauftrag abgebrochen? Aka konnte der Spieler einmal nicht zahlen können.
	private boolean paused; //Hat der Spieler den Auftrag pausiert.
	
	public StandingOrder(int id, String name, UUID owner, int accountFrom, int accountTo, double amount, double amountPaidSoFar, double amountPaidToTax,
			long startTime, long repeatingTime, long lastTime, boolean cancelled, boolean paused)
	{
		setID(id);
		setName(name);
		setOwner(owner);
		setAccountFrom(accountFrom);
		setAccountTo(accountTo);
		setAmount(amount);
		setAmountPaidSoFar(amountPaidSoFar);
		setAmountPaidToTax(amountPaidToTax);
		setStartTime(startTime);
		setRepeatingTime(repeatingTime);
		setLastTime(lastTime);
		setCancelled(cancelled);
		setPaused(paused);
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public long getRepeatingTime()
	{
		return repeatingTime;
	}

	public void setRepeatingTime(long repeatingTime)
	{
		this.repeatingTime = repeatingTime;
	}

	public long getLastTime()
	{
		return lastTime;
	}

	public void setLastTime(long lastTime)
	{
		this.lastTime = lastTime;
	}

	public boolean isCancelled()
	{
		return cancelled;
	}

	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}

	public boolean isPaused()
	{
		return paused;
	}

	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}

	public double getAmountPaidSoFar()
	{
		return amountPaidSoFar;
	}

	public void setAmountPaidSoFar(double amountPaidSoFar)
	{
		this.amountPaidSoFar = amountPaidSoFar;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	public int getID()
	{
		return id;
	}

	public void setID(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the accountFrom
	 */
	public int getAccountFrom()
	{
		return accountFrom;
	}

	/**
	 * @param accountFrom the accountFrom to set
	 */
	public void setAccountFrom(int accountFrom)
	{
		this.accountFrom = accountFrom;
	}

	/**
	 * @return the accountTo
	 */
	public int getAccountTo()
	{
		return accountTo;
	}

	/**
	 * @param accountTo the accountTo to set
	 */
	public void setAccountTo(int accountTo)
	{
		this.accountTo = accountTo;
	}

	/**
	 * @return the amountPaidToTax
	 */
	public double getAmountPaidToTax()
	{
		return amountPaidToTax;
	}

	/**
	 * @param amountPaidToTax the amountPaidToTax to set
	 */
	public void setAmountPaidToTax(double amountPaidToTax)
	{
		this.amountPaidToTax = amountPaidToTax;
	}

	/**
	 * @return the owner
	 */
	public UUID getOwner()
	{
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

}
