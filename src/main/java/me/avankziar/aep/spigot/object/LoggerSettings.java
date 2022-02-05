package main.java.me.avankziar.aep.spigot.object;

import main.java.me.avankziar.aep.spigot.object.subs.ActionFilterSettings;
import main.java.me.avankziar.aep.spigot.object.subs.TrendFilterSettings;

public class LoggerSettings
{
	public enum OrderType
	{
		ID, AMOUNT;
	}
	
	public enum InventoryHandlerType
	{
		NONE, NORMAL, ANVILEDITOR_ACCOUNT_ID, ANVILEDITOR_ORDERER, ANVILEDITOR_CATEGORY, ANVILEDITOR_COMMENT;
	}
	
	private int accountID;
	private int slotid;
	private int page;
	private boolean descending;
	private OrderType orderType;
	private boolean isAction;
	private InventoryHandlerType inventoryHandlerType;
	private Double min;
	private Double max;
	private ActionFilterSettings actionFilter;
	private TrendFilterSettings trendfFilter;
	
	public LoggerSettings(int accountID, int page)
	{
		setAccountID(accountID);
		setSlotid(1);
		setPage(page);
		setActionFilter(new ActionFilterSettings());
		setTrendfFilter(new TrendFilterSettings());
		setDescending(true);
		setOrderType(OrderType.ID);
		setMin(null);
		setMax(null);
		setInventoryHandlerType(InventoryHandlerType.NONE);
		setAction(true);
	}

	public ActionFilterSettings getActionFilter()
	{
		return actionFilter;
	}

	public void setActionFilter(ActionFilterSettings actionFilter)
	{
		this.actionFilter = actionFilter;
	}

	public TrendFilterSettings getTrendfFilter()
	{
		return trendfFilter;
	}

	public void setTrendfFilter(TrendFilterSettings trendfFilter)
	{
		this.trendfFilter = trendfFilter;
	}

	public boolean isDescending()
	{
		return descending;
	}

	public void setDescending(boolean descending)
	{
		this.descending = descending;
	}

	public OrderType getOrderType()
	{
		return orderType;
	}

	public void setOrderType(OrderType orderType)
	{
		this.orderType = orderType;
	}

	public Double getMin()
	{
		return min;
	}

	public void setMin(Double min)
	{
		this.min = min;
	}

	public Double getMax()
	{
		return max;
	}

	public void setMax(Double max)
	{
		this.max = max;
	}

	public InventoryHandlerType getInventoryHandlerType()
	{
		return inventoryHandlerType;
	}

	public void setInventoryHandlerType(InventoryHandlerType inventoryHandlerType)
	{
		this.inventoryHandlerType = inventoryHandlerType;
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

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public boolean isAction()
	{
		return isAction;
	}

	public void setAction(boolean isAction)
	{
		this.isAction = isAction;
	}

	public int getSlotid()
	{
		return slotid;
	}

	public void setSlotid(int slotid)
	{
		this.slotid = slotid;
	}

}
