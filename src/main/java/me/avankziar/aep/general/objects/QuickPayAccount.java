package main.java.me.avankziar.aep.general.objects;

import java.util.UUID;

public class QuickPayAccount
{
	private UUID playerUUID;
	private int accountID;
	private String currencyUniqueName;
	
	public QuickPayAccount(UUID playerUUID, int accountID, String currencyUniqueName)
	{
		setPlayerUUID(playerUUID);
		setAccountID(accountID);
		setCurrencyUniqueName(currencyUniqueName);
	}

	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public int getAccountID()
	{
		return accountID;
	}

	public void setAccountID(int accountID)
	{
		this.accountID = accountID;
	}

	public String getCurrencyUniqueName()
	{
		return currencyUniqueName;
	}

	public void setCurrencyUniqueName(String currencyUniqueName)
	{
		this.currencyUniqueName = currencyUniqueName;
	}

}
