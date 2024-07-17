package me.avankziar.aep.spigot.object.subs;

import org.bukkit.OfflinePlayer;

public class GuiPay
{
	private int step = 1;
	private OfflinePlayer player;
	private String toPlayer;
	private int fromAccountID = 0;
	private int toAccountID = 0;
	private int taxAccountID = 0;
	private double amount;
	private String category;
	private String comment;
	
	public GuiPay(OfflinePlayer player, String toPlayer, double amount, String category, String comment)
	{
		setPlayer(player);
		setToPlayer(toPlayer);
		setAmount(amount);
		setCategory(category);
		setComment(comment);
	}

	public OfflinePlayer getPlayer()
	{
		return player;
	}

	public void setPlayer(OfflinePlayer player)
	{
		this.player = player;
	}

	public int getFromAccountID()
	{
		return fromAccountID;
	}

	public void setFromAccountID(int fromAccountID)
	{
		this.fromAccountID = fromAccountID;
	}

	public int getToAccountID()
	{
		return toAccountID;
	}

	public void setToAccountID(int toAccountID)
	{
		this.toAccountID = toAccountID;
	}

	public int getTaxAccountID()
	{
		return taxAccountID;
	}

	public void setTaxAccountID(int taxAccountID)
	{
		this.taxAccountID = taxAccountID;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return the step
	 */
	public int getStep()
	{
		return step;
	}

	/**
	 * @param step the step to set
	 */
	public void setStep(int step)
	{
		this.step = step;
	}

	/**
	 * @return the toPlayer
	 */
	public String getToPlayer()
	{
		return toPlayer;
	}

	/**
	 * @param toPlayer the toPlayer to set
	 */
	public void setToPlayer(String toPlayer)
	{
		this.toPlayer = toPlayer;
	}

}
