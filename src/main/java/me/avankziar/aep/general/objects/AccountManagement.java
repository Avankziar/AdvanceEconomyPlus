package main.java.me.avankziar.aep.general.objects;

import java.util.UUID;

import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;

public class AccountManagement
{
	private int accountID;
	private UUID uuid;
	private AccountManagementType maganagementType;
	
	public AccountManagement(int accountID, UUID uuid, AccountManagementType maganagementType)
	{
		setAccountID(accountID);
		setUUID(uuid);
		setMaganagementType(maganagementType);
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
	 * @return the playerUUID
	 */
	public UUID getUUID()
	{
		return uuid;
	}

	/**
	 * @param playerUUID the playerUUID to set
	 */
	public void setUUID(UUID playerUUID)
	{
		this.uuid = playerUUID;
	}

	/**
	 * @return the maganagementType
	 */
	public AccountManagementType getMaganagementType()
	{
		return maganagementType;
	}

	/**
	 * @param maganagementType the maganagementType to set
	 */
	public void setMaganagementType(AccountManagementType maganagementType)
	{
		this.maganagementType = maganagementType;
	}
}
