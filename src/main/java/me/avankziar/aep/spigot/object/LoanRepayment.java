package main.java.me.avankziar.aep.spigot.object;

import java.util.UUID;

public class LoanRepayment
{
	//Schulden Tilgung
	private int id;
	private String name;
	private int fromID;
	private int toID;
	private UUID owner; //Kann nur ein Spieler sein. Und nur dieser kann die Schulden als "getilgt" setzen.
	private UUID debtor; //Der Schuldner
	private double loanAmount; //Betrag welcher geliehen wurde, ohne zinsen, steuern etc.
	private double totalAmount; //Gesamtbetrag der zu zahlen ist.
	private double amountRatio; //Schulden Rate, wieviel pro Zahlung gezahlt werden soll.
	private double taxInDecimal; //Steuern in decimalzahl: 1.0 == 100%
	private double amountPaidSoFar; //Bisher gezahlter Betrag
	private double amountPaidToTax; //Bisher gezahlter Betrag
	private double interest; //Zinsen, nur auf komplett summe gerechnet. Prozent angabe
	private long startTime; //Beginn der Zahlung
	private long repeatingTime; //Millisekunden, wann wieder gezahlt werden muss.
	private long lastTime; //Millisekunden, wann das letzte mal gezahlt wird.
	private long endTime; //Millisekunden, wann die restliche Betrag fällig ist.
	private boolean forgiven; //Sind die Schulden vom Eigentümer vergeben worden.
	private boolean paused; //Sind die Schulden vom Eigentümer pausiert worden.
	private boolean finished; //Ist alles bezahlt worden.
	
	public LoanRepayment(int id, String name, int fromID, int toID, UUID owner, UUID debtor,
			double totalAmount, double loanAmount, double amountRatio, double taxInDecimal,
			double amountPaidSoFar, double amountPaidToTax, double interest,
			long startTime, long repeatingTime, long lastTime, long endTime,
			boolean forgiven, boolean paused, boolean finished)
	{
		setId(id);
		setName(name);
		setAccountFromID(fromID);
		setAccountToID(toID);
		setOwner(owner);
		setDebtor(debtor);
		setTotalAmount(totalAmount);
		setLoanAmount(loanAmount);
		setAmountRatio(amountRatio);
		setTaxInDecimal(taxInDecimal);
		setAmountPaidSoFar(amountPaidSoFar);
		setAmountPaidToTax(amountPaidToTax);
		setInterest(interest);
		setStartTime(startTime);
		setRepeatingTime(repeatingTime);
		setLastTime(lastTime);
		setEndTime(endTime);
		setForgiven(forgiven);
		setPaused(paused);
		setFinished(finished);
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
	
	public void addTax(double amount)
	{
		amountPaidToTax = amountPaidToTax + amount;
	}

	public int getAccountFromID()
	{
		return fromID;
	}

	public void setAccountFromID(int fromID)
	{
		this.fromID = fromID;
	}

	public int getAccountToID()
	{
		return toID;
	}

	public void setAccountToID(int toID)
	{
		this.toID = toID;
	}

	public UUID getOwner()
	{
		return owner;
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	public double getAmountPaidToTax()
	{
		return amountPaidToTax;
	}

	public void setAmountPaidToTax(double amountPaidToTax)
	{
		this.amountPaidToTax = amountPaidToTax;
	}

	/**
	 * @return the debtor
	 */
	public UUID getDebtor()
	{
		return debtor;
	}

	/**
	 * @param debtor the debtor to set
	 */
	public void setDebtor(UUID debtor)
	{
		this.debtor = debtor;
	}

	/**
	 * @return the loanAmount
	 */
	public double getLoanAmount()
	{
		return loanAmount;
	}

	/**
	 * @param loanAmount the loanAmount to set
	 */
	public void setLoanAmount(double loanAmount)
	{
		this.loanAmount = loanAmount;
	}

	/**
	 * @return the taxInDecimal
	 */
	public double getTaxInDecimal()
	{
		return taxInDecimal;
	}

	/**
	 * @param taxInDecimal the taxInDecimal to set
	 */
	public void setTaxInDecimal(double taxInDecimal)
	{
		this.taxInDecimal = taxInDecimal;
	}
}
