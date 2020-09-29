package main.java.me.avankziar.aep.spigot.object;

public class LoanRepayment
{
	//Schulden Tilgung
	private int id;
	private String name;
	private String from; //UUID or Bank
	private String to; //UUID or Bank
	private String loanOwner; //Kann nur ein Spieler sein. Und nur dieser kann die Schulden als "getilgt" setzen.
	private double totalAmount; //Gesamtbetrag der zu zahlen ist.
	private double amountRatio; //Schulden Rate, wieviel pro Zahlung gezahlt werden soll.
	private double amountPaidSoFar; //Bisher gezahlter Betrag
	private double interest; //Zinsen, nur auf komplett summe gerechnet. Prozent angabe
	private long startTime; //Beginn der Zahlung
	private long repeatingTime; //Millisekunden, wann wieder gezahlt werden muss.
	private long lastTime; //Millisekunden, wann das letzte mal gezahlt wird.
	private long endTime; //Millisekunden, wann die restliche Betrag fällig ist.
	private boolean forgiven; //Sind die Schulden vom Eigentümer vergeben worden.
	private boolean paused; //Sind die Schulden vom Eigentümer pausiert worden.
	private boolean finished; //Ist alles bezahlt worden.
	
	public LoanRepayment(int id, String name, String from, String to, String loanOwner,
			double totalAmount, double amountRatio, double amountPaidSoFar, double interest,
			long startTime, long repeatingTime, long lastTime, long endTime,
			boolean forgiven, boolean paused, boolean finished)
	{
		setId(id);
		setName(name);
		setFrom(from);
		setTo(to);
		setLoanOwner(loanOwner);
		setTotalAmount(totalAmount);
		setAmountRatio(amountRatio);
		setAmountPaidSoFar(amountPaidSoFar);
		setInterest(interest);
		setStartTime(startTime);
		setRepeatingTime(repeatingTime);
		setLastTime(lastTime);
		setEndTime(endTime);
		setForgiven(forgiven);
		setPaused(paused);
		setFinished(finished);
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

	public double getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	public double getAmountRatio()
	{
		return amountRatio;
	}

	public void setAmountRatio(double amountRatio)
	{
		this.amountRatio = amountRatio;
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

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public boolean isForgiven()
	{
		return forgiven;
	}

	public void setForgiven(boolean forgiven)
	{
		this.forgiven = forgiven;
	}

	public boolean isPaused()
	{
		return paused;
	}

	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}

	public boolean isFinished()
	{
		return finished;
	}

	public void setFinished(boolean finished)
	{
		this.finished = finished;
	}

	public double getInterest()
	{
		return interest;
	}

	public void setInterest(double interest)
	{
		this.interest = interest;
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
	
	public void addPayment(double amount)
	{
		amountPaidSoFar = amountPaidSoFar + amount;
	}

	public String getLoanOwner()
	{
		return loanOwner;
	}

	public void setLoanOwner(String loanOwner)
	{
		this.loanOwner = loanOwner;
	}
}
