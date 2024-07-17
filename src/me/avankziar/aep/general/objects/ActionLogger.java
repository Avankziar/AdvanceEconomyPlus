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
import me.avankziar.ifh.general.economy.action.OrdererType;

public class ActionLogger implements MysqlHandable
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
	
	public ActionLogger() {}
	
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
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`unixtime`,"
					+ " `from_account_id`, `to_account_id`, `tax_account_id`,"
					+ " `orderer_type`, `orderer_uuid`, `orderer_plugin`,"
					+ " `amount_to_withdraw`, `amount_to_deposit`, `amount_to_tax`,"
					+ " `category`, `comment`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getUnixTime());
	        ps.setInt(2, getFromAccountID());
	        ps.setInt(3, getToAccountID());
	        ps.setInt(4, getTaxAccountID());
	        ps.setString(5, getOrderType().toString());
	        ps.setString(6, getOrdererUUID() != null ? getOrdererUUID().toString() : null);
	        ps.setString(7, getOrdererPlugin() != null ? getOrdererPlugin() : null);
	        ps.setDouble(8, getAmountToWithdraw());
	        ps.setDouble(9, getAmountToDeposit());
	        ps.setDouble(10, getAmountToTax());
	        ps.setString(11, getCategory());
	        ps.setString(12, getComment());
	        
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
					+ "` SET `unixtime` = ?,"
					+ " `from_account_id` = ?, `to_account_id` = ?, `tax_account_id` = ?,"
					+ " `orderer_type` = ?, `orderer_uuid` = ?, `orderer_plugin` = ?,"
					+ " `amount_to_withdraw` = ?, `amount_to_deposit` = ?, `amount_to_tax` = ?,"
					+ " `category` = ?, `comment` = ?"
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getUnixTime());
	        ps.setInt(2, getFromAccountID());
	        ps.setInt(3, getToAccountID());
	        ps.setInt(4, getTaxAccountID());
	        ps.setString(5, getOrderType().toString());
	        ps.setString(6, getOrdererUUID() != null ? getOrdererUUID().toString() : null);
	        ps.setString(7, getOrdererPlugin() != null ? getOrdererPlugin() : null);
	        ps.setDouble(8, getAmountToWithdraw());
	        ps.setDouble(9, getAmountToDeposit());
	        ps.setDouble(10, getAmountToTax());
	        ps.setString(11, getCategory());
	        ps.setString(12, getComment());
	        
	        int i = 13;
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
				al.add(new ActionLogger(
	        			rs.getInt("id"),
	        			rs.getLong("unixtime"),
	        			rs.getInt("from_account_id"), 
	        			rs.getInt("to_account_id"),
	        			rs.getInt("tax_account_id"),
	        			OrdererType.valueOf(rs.getString("orderer_type")),
	        			rs.getString("orderer_uuid") != null ? UUID.fromString(rs.getString("orderer_uuid")) : null,
	        			rs.getString("orderer_plugin"),
	        			rs.getDouble("amount_to_withdraw"),
	        			rs.getDouble("amount_to_deposit"),
	        			rs.getDouble("amount_to_tax"),
	        			rs.getString("category"),
	        			rs.getString("comment")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<ActionLogger> convert(ArrayList<Object> arrayList)
	{
		ArrayList<ActionLogger> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof ActionLogger)
			{
				l.add((ActionLogger) o);
			}
		}
		return l;
	}
}