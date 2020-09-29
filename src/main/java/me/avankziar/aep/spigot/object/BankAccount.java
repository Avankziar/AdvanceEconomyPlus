package main.java.me.avankziar.aep.spigot.object;

import java.util.ArrayList;

public class BankAccount
{
	private int id;
	private String name;
	private String accountNumber;
	private double balance;
	private String ownerUUID;
	private ArrayList<String> viceUUID;
	private ArrayList<String> memberUUID;

	public BankAccount(int id, String name, String accountNumber, double balance,
			String ownerUUID, ArrayList<String> viceUUID, ArrayList<String> memberUUID)
	{
		setId(id);
		setName(name);
		setaccountNumber(accountNumber);
		setBalance(balance);
		setOwnerUUID(ownerUUID);
		setViceUUID(viceUUID);
		setMemberUUID(memberUUID);
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

	public String getaccountNumber()
	{
		return accountNumber;
	}

	public void setaccountNumber(String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	public double getBalance()
	{
		return balance;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

	public String getOwnerUUID()
	{
		return ownerUUID;
	}

	public void setOwnerUUID(String ownerUUID)
	{
		this.ownerUUID = ownerUUID;
	}

	public ArrayList<String> getViceUUID()
	{
		return viceUUID;
	}

	public void setViceUUID(ArrayList<String> viceUUID)
	{
		this.viceUUID = viceUUID;
	}

	public ArrayList<String> getMemberUUID()
	{
		return memberUUID;
	}

	public void setMemberUUID(ArrayList<String> memberUUID)
	{
		this.memberUUID = memberUUID;
	}
}
