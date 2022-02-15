package main.java.me.avankziar.aep.spigot.object.ne_w;

import java.util.UUID;

public class AEPUser
{
	private UUID uuid;
	private String name;
	private boolean walletMoneyFlowNotification;
	private boolean bankMoneyFlowNotification;
	private long lastTimeLogin;
	
	public AEPUser(UUID uuid, String name, boolean walletMoneyFlowNotification, boolean bankMoneyFlowNotification, long lastTimeLogin)
	{
		setUUID(uuid);
		setName(name);
		setWalletMoneyFlowNotification(walletMoneyFlowNotification);
		setBankMoneyFlowNotification(bankMoneyFlowNotification);
		setLastTimeLogin(lastTimeLogin);
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

	/**
	 * @return the lastTimeLogin
	 */
	public long getLastTimeLogin()
	{
		return lastTimeLogin;
	}

	/**
	 * @param lastTimeLogin the lastTimeLogin to set
	 */
	public void setLastTimeLogin(long lastTimeLogin)
	{
		this.lastTimeLogin = lastTimeLogin;
	}

}
