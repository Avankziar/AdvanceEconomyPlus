package me.avankziar.aep.general.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import me.avankziar.aep.general.database.MysqlBaseHandler;
import me.avankziar.aep.general.database.MysqlHandable;
import me.avankziar.aep.general.database.QueryType;
import me.avankziar.aep.spigot.object.subs.ActionFilterSettings;
import me.avankziar.aep.spigot.object.subs.TrendFilterSettings;

public class LoggerSettings implements MysqlHandable
{
	public enum OrderType
	{
		ID, AMOUNT;
	}
	
	public enum InventoryHandlerType
	{
		NONE, NORMAL, ANVILEDITOR_ACCOUNT_ID, ANVILEDITOR_ORDERER, ANVILEDITOR_CATEGORY, ANVILEDITOR_COMMENT;
	}
	
	private UUID owner;
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
	
	public LoggerSettings() {}
	
	public LoggerSettings(int accountID, UUID owner, int page)
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
	
	public LoggerSettings(int slotid, UUID owner, int accountID, boolean isAction, InventoryHandlerType inventoryHandlerType,
			boolean descending, OrderType orderType, Double min, Double max, String category, String orderer, String comment,
			double firststand, double laststand)
	{
		setSlotid(slotid);
		setOwner(owner);
		setAccountID(accountID);
		setAction(isAction);
		setInventoryHandlerType(inventoryHandlerType);
		setDescending(descending);
		setOrderType(orderType);
		setMin(min);
		setMax(max);
		ActionFilterSettings afs = new ActionFilterSettings();
		afs.setCategory(category);
		afs.setOrderer(orderer);
		afs.setComment(comment);
		setActionFilter(afs);
		TrendFilterSettings tfs = new TrendFilterSettings();
		tfs.setFirstStand(firststand);
		tfs.setLastStand(laststand);
		setTrendfFilter(tfs);
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

	/**
	 * @return the owner
	 */
	public UUID getOwner()
	{
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`slotid`, `player_uuid`, `account_id`,"
					+ " `isaction`, `inventoryhandlertype`, `isdescending`,"
					+ " `ordertype`, `minimum`, `maximum`," 
					+ " `category`, `orderer`, `comment`, `firststand`, `laststand`)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getSlotid());
	        ps.setString(2, getOwner().toString());
	        ps.setInt(3, getAccountID());
	        ps.setBoolean(4, isAction());
	        ps.setString(5, getInventoryHandlerType().toString());
	        ps.setBoolean(6, isDescending());
	        ps.setString(7, getOrderType().toString());
	        Double min = getMin();
	        if(min == null) {min = 0.0;}
	        Double max = getMax();
	        if(max == null) {max = 0.0;}
	        ps.setDouble(8, min);
	        ps.setDouble(9, max);
	        ps.setString(10, getActionFilter().getCategory());
	        ps.setString(11, getActionFilter().getOrderer());
	        ps.setString(12, getActionFilter().getComment());
	        Double firststand = getTrendfFilter().getFirstStand();
	        if(firststand == null) {firststand = 0.0;}
	        Double laststand = getTrendfFilter().getLastStand();
	        if(laststand == null) {laststand = 0.0;}
	        ps.setDouble(13, firststand);
	        ps.setDouble(14, laststand);
	        
	        int i = ps.executeUpdate();
	        MysqlBaseHandler.addRows(QueryType.INSERT, i);
	        return true;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not create a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "UPDATE `" + tablename
					+ "` SET `slotid` = ?, `player_uuid` = ?, `account_id` = ?,"
					+ " `isaction` = ?, `inventoryhandlertype` = ?, `isdescending` = ?,"
					+ " `ordertype` = ?, `minimum` = ?, `maximum` = ?," 
					+ " `category` = ?, `orderer` = ?,"
					+ " `comment` = ?, `firststand` = ?, `laststand` = ? "
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getSlotid());
	        ps.setString(2, getOwner().toString());
	        ps.setInt(3, getAccountID());
	        ps.setBoolean(4, isAction());
	        ps.setString(5, getInventoryHandlerType().toString());
	        ps.setBoolean(6, isDescending());
	        ps.setString(7, getOrderType().toString());
	        Double min = getMin();
	        if(min == null) {min = 0.0;}
	        Double max = getMax();
	        if(max == null) {max = 0.0;}
	        ps.setDouble(8, min);
	        ps.setDouble(9, max);
	        ps.setString(10, getActionFilter().getCategory());
	        ps.setString(11, getActionFilter().getOrderer());
	        ps.setString(12, getActionFilter().getComment());
	        Double firststand = getTrendfFilter().getFirstStand();
	        if(firststand == null) {firststand = 0.0;}
	        Double laststand = getTrendfFilter().getLastStand();
	        if(laststand == null) {laststand = 0.0;}
	        ps.setDouble(13, firststand);
	        ps.setDouble(14, laststand);
	        
	        int i = 15;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}			
			int u = ps.executeUpdate();
			MysqlBaseHandler.addRows(QueryType.UPDATE, u);
			return true;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not update a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "SELECT * FROM `" + tablename
				+ "` WHERE "+whereColumn+" ORDER BY "+orderby+limit;
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 1;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}
			
			ResultSet rs = ps.executeQuery();
			MysqlBaseHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new LoggerSettings(
	        			rs.getInt("slotid"),
	        			UUID.fromString(rs.getString("player_uuid")),
	        			rs.getInt("account_id"),
	        			rs.getBoolean("isaction"),
	        			InventoryHandlerType.valueOf(rs.getString("inventoryhandlertype")),
	        			rs.getBoolean("isdescending"),
	        			OrderType.valueOf(rs.getString("ordertype")),
	        			Double.valueOf(rs.getDouble("minimum")),
	        			Double.valueOf(rs.getDouble("maximum")),
	        			rs.getString("category"),
	        			rs.getString("orderer"),
	        			rs.getString("comment"),
	        			rs.getDouble("firststand"),
	        			rs.getDouble("laststand")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<LoggerSettings> convert(ArrayList<Object> arrayList)
	{
		ArrayList<LoggerSettings> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof LoggerSettings)
			{
				l.add((LoggerSettings) o);
			}
		}
		return l;
	}
}