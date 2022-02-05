package main.java.me.avankziar.aep.spigot.object;

import java.util.UUID;

import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;

public class ActionLogger
{
	private int id;
	private long unixTime;
	private int fromAccountID;
	private int toAccountID;
	private int taxAccountID;
	private OrdererType orderType;
	private UUID ordererUUID; //Only UUID from a player or the Plugin
	private String ordererPlugin;
	private double amountToWithdraw;
	private double amountToDeposit;
	private double amountToTax;
	private String category;
	private String comment;
	
	public ActionLogger(int id, long unixTime,
			int fromAccountID, int toAccountID, int taxAccountID, OrdererType orderType, UUID ordererUUID, String ordererPlugin,
			double amountToWithdraw, double amountToDeposit, double amountToTax, String category, String comment)
	{
		setId(id);
		setUnixTime(unixTime);
		setFromAccountID(fromAccountID);
		setToAccountID(toAccountID);
		setTaxAccountID(taxAccountID);
		setOrderType(orderType);
		setOrdererUUID(ordererUUID);
		setOrdererPlugin(ordererPlugin);
		setAmountToWithdraw(amountToWithdraw);
		setAmountToDeposit(amountToDeposit);
		setAmountToTax(amountToTax);
		setCategory(category);
		setComment(comment);
	}

	public long getUnixTime()
	{
		return unixTime;
	}

	public void setUnixTime(long unixTime)
	{
		this.unixTime = unixTime;
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

	/**
	 * @return the taxAccountID
	 */
	public int getTaxAccountID()
	{
		return taxAccountID;
	}

	/**
	 * @param taxAccountID the taxAccountID to set
	 */
	public void setTaxAccountID(int taxAccountID)
	{
		this.taxAccountID = taxAccountID;
	}

	public OrdererType getOrderType()
	{
		return orderType;
	}

	public void setOrderType(OrdererType orderType)
	{
		this.orderType = orderType;
	}

	public UUID getOrdererUUID()
	{
		return ordererUUID;
	}

	public void setOrdererUUID(UUID ordererUUID)
	{
		this.ordererUUID = ordererUUID;
	}

	public String getOrdererPlugin()
	{
		return ordererPlugin;
	}

	public void setOrdererPlugin(String ordererPlugin)
	{
		this.ordererPlugin = ordererPlugin;
	}

	/**
	 * @return the amountToWithdraw
	 */
	public double getAmountToWithdraw()
	{
		return amountToWithdraw;
	}

	/**
	 * @param amountToWithdraw the amountToWithdraw to set
	 */
	public void setAmountToWithdraw(double amountToWithdraw)
	{
		this.amountToWithdraw = amountToWithdraw;
	}

	/**
	 * @return the amountToDeposit
	 */
	public double getAmountToDeposit()
	{
		return amountToDeposit;
	}

	/**
	 * @param amountToDeposit the amountToDeposit to set
	 */
	public void setAmountToDeposit(double amountToDeposit)
	{
		this.amountToDeposit = amountToDeposit;
	}

	/**
	 * @return the amountToTax
	 */
	public double getAmountToTax()
	{
		return amountToTax;
	}

	/**
	 * @param amountToTax the amountToTax to set
	 */
	public void setAmountToTax(double amountToTax)
	{
		this.amountToTax = amountToTax;
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
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
