package main.java.me.avankziar.aep.spigot.object.ne_w;

import java.util.UUID;

public class AEPUser
{
	private UUID uuid;
	private String name;
	private int shortPayAccountID;
	private boolean walletMoneyFlowNotification;
	private boolean bankMoneyFlowNotification;
	
	public AEPUser(UUID uuid, String name, int shortPayAccountID, boolean walletMoneyFlowNotification, boolean bankMoneyFlowNotification)
	{
		setUUID(uuid);
		setName(name);
		setShortPayAccountID(shortPayAccountID);
		setWalletMoneyFlowNotification(walletMoneyFlowNotification);
		setBankMoneyFlowNotification(bankMoneyFlowNotification);
	}

	/**
	 * @return the uuid
	 */
	public UUID getUUID()
	{
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the shortPayAccount
	 */
	public int getShortPayAccountID()
	{
		return shortPayAccountID;
	}

	/**
	 * @param shortPayAccount the shortPayAccount to set
	 */
	public void setShortPayAccountID(int shortPayAccountID)
	{
		this.shortPayAccountID = shortPayAccountID;
	}

	/**
	 * @return the walletMoneyFlowNotification
	 */
	public boolean isWalletMoneyFlowNotification()
	{
		return walletMoneyFlowNotification;
	}

	/**
	 * @param walletMoneyFlowNotification the walletMoneyFlowNotification to set
	 */
	public void setWalletMoneyFlowNotification(boolean walletMoneyFlowNotification)
	{
		this.walletMoneyFlowNotification = walletMoneyFlowNotification;
	}

	/**
	 * @return the bankMoneyFlowNotification
	 */
	public boolean isBankMoneyFlowNotification()
	{
		return bankMoneyFlowNotification;
	}

	/**
	 * @param bankMoneyFlowNotification the bankMoneyFlowNotification to set
	 */
	public void setBankMoneyFlowNotification(boolean bankMoneyFlowNotification)
	{
		this.bankMoneyFlowNotification = bankMoneyFlowNotification;
	}

}
