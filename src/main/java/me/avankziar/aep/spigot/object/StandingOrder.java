package main.java.me.avankziar.aep.spigot.object;

public class StandingOrder
{
	//Dauerauftrag
	private int id;
	private String name;
	private String from; //UUID or Banknumber
	private String to; //UUID or Banknumber
	private double amount; //Betrag
	private double amountPaidSoFar; //Bisher gezahlter Betrag
	private long startTime; //Anfangsdatum
	private long repeatingTime; //Millisekunden, wann wieder gezahlt wird.
	private long lastTime; //Millisekunden, wann das letzte man gezahl wurde.
	private boolean cancelled; //Wurde der Dauerauftrag abgebrochen? Aka konnte der Spieler einmal nicht zahlen können.
	private boolean paused; //Hat der Spieler den Auftrag pausiert.
	
	public StandingOrder(int id, String name, String from, String to, double amount, double amountPaidSoFar,
			long startTime, long repeatingTime, long lastTime, boolean cancelled, boolean paused)
	{
		setId(id);
		setName(name);
		setFrom(from);
		setTo(to);
		setAmount(amount);
		setAmountPaidSoFar(amountPaidSoFar);
		setStartTime(startTime);
		setRepeatingTime(repeatingTime);
		setLastTime(lastTime);
		setCancelled(cancelled);
		setPaused(paused);
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
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

	public int getId()
	{
		return id;
	}

	public void setId(int id)
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

}
