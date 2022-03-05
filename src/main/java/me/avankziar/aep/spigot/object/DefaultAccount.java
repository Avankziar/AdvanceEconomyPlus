package main.java.me.avankziar.aep.spigot.object;

import java.util.UUID;

import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;

public class DefaultAccount
{
	private UUID playerUUID;
	private int accountID;
	private String currencyUniqueName;
	private AccountCategory category;
	
	public DefaultAccount(UUID playerUUID, int accountID, String currencyUniqueName, AccountCategory category)
	{
		setPlayerUUID(playerUUID);
		setAccountID(accountID);
		setCurrencyUniqueName(currencyUniqueName);
		setCategory(category);
	}

	/**
	 * @return the playerUUID
	 */
	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	/**
	 * @param playerUUID the playerUUID to set
	 */
	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	/**
	 * @return the accountID
	 */
	public int getAccountID()
	{
		return accountID;
	}

	/**
	 * @param accountID the accountID to set
	 */
	public void setAccountID(int accountID)
	{
		this.accountID = accountID;
	}

	/**
	 * @return the currencyUniqueName
	 */
	public String getCurrencyUniqueName()
	{
		return currencyUniqueName;
	}

	/**
	 * @param currencyUniqueName the currencyUniqueName to set
	 */
	public void setCurrencyUniqueName(String currencyUniqueName)
	{
		this.currencyUniqueName = currencyUniqueName;
	}

	/**
	 * @return the category
	 */
	public AccountCategory getCategory()
	{
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(AccountCategory category)
	{
		this.category = category;
	}

}
