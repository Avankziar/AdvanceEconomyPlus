package main.java.me.avankziar.aep.spigot.object;

import java.util.List;

public class EcoPlayer
{
	private int id;
	private String uuid;
	private String name;
	private double balance;
	private boolean moneyPlayerFlow;
	private boolean moneyBankFlow;
	private boolean generalMessage;
	private String pendingInvite;
	private boolean frozen; //To Freeze a Playeraccount
	private List<String> bankAccountNumber;
	
	public EcoPlayer(int id, String uuid, String name,
			double balance, List<String> bankAccountNumber,
			boolean moneyPlayerFlow, boolean moneyBankFlow, boolean generalMessage,
			String pendingInvite, boolean frozen)
	{
		setId(id);
		setUUID(uuid);
		setName(name);
		setBalance(balance);
		setBankAccountNumber(bankAccountNumber);
		setMoneyPlayerFlow(moneyPlayerFlow);
		setMoneyBankFlow(moneyBankFlow);
		setGeneralMessage(generalMessage);
		setPendingInvite(pendingInvite);
		setFrozen(frozen);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getUUID()
	{
		return uuid;
	}

	public void setUUID(String uuid)
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public double getBalance()
	{
		return balance;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

	public List<String> getBankAccountNumber()
	{
		return bankAccountNumber;
	}

	public void setBankAccountNumber(List<String> bankAccountNumber)
	{
		this.bankAccountNumber = bankAccountNumber;
	}
	
	public boolean isMoneyPlayerFlow()
	{
		return moneyPlayerFlow;
	}

	public void setMoneyPlayerFlow(boolean moneyPlayerFlow)
	{
		this.moneyPlayerFlow = moneyPlayerFlow;
	}

	public boolean isMoneyBankFlow()
	{
		return moneyBankFlow;
	}

	public void setMoneyBankFlow(boolean moneyBankFlow)
	{
		this.moneyBankFlow = moneyBankFlow;
	}

	public boolean isGeneralMessage()
	{
		return generalMessage;
	}

	public void setGeneralMessage(boolean generalMessage)
	{
		this.generalMessage = generalMessage;
	}

	public String getPendingInvite()
	{
		return pendingInvite;
	}

	public void setPendingInvite(String pendingInvite)
	{
		this.pendingInvite = pendingInvite;
	}

	public boolean isFrozen()
	{
		return frozen;
	}

	public void setFrozen(boolean frozen)
	{
		this.frozen = frozen;
	}
}
