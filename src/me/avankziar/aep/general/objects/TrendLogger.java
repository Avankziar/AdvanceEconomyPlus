package me.avankziar.aep.general.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;

import me.avankziar.aep.general.assistance.TimeHandler;
import me.avankziar.aep.general.database.MysqlBaseHandler;
import me.avankziar.aep.general.database.MysqlHandable;
import me.avankziar.aep.general.database.QueryType;

public class TrendLogger implements MysqlHandable
{
	public enum Type
	{
		STABIL, UP, DOWN;
	}
	
	private long unixTime;
	private Type type;
	private int accountID;
	private double relativeAmountChange;
	private double firstValue;
	private double lastValue;
	
	public TrendLogger() {}
	
	public TrendLogger(long unixTime, Type type, int accountID, double relativeAmountChange,
			double firstValue, double lastValue)
	{
		LocalDateTime ldt = TimeHandler.getLocalDateTime(unixTime);
		LocalDate ld = LocalDate.of(ldt.getYear(), ldt.getMonth(), ldt.getDayOfMonth());
		setUnixTime(TimeHandler.getTime(ld));
		setType(type);
		setAccountID(accountID);
		setRelativeAmountChange(relativeAmountChange);
		setFirstValue(firstValue);
		setLastValue(lastValue);
	}
	
	public TrendLogger(LocalDate date, Type type, int accountID, double relativeAmountChange,
			double firstValue, double lastValue)
	{
		setUnixTime(TimeHandler.getTime(date));
		setType(type);
		setAccountID(accountID);
		setRelativeAmountChange(relativeAmountChange);
		setFirstValue(firstValue);
		setLastValue(lastValue);
	}

	/**
	 * @return the unixTime
	 */
	public long getUnixTime()
	{
		return unixTime;
	}

	/**
	 * @param unixTime the unixTime to set
	 */
	public void setUnixTime(long unixTime)
	{
		this.unixTime = unixTime;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
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

	public double getRelativeAmountChange()
	{
		return relativeAmountChange;
	}

	public void setRelativeAmountChange(double relativeAmountChange)
	{
		this.relativeAmountChange = relativeAmountChange;
	}

	public double getFirstValue()
	{
		return firstValue;
	}

	public void setFirstValue(double firstValue)
	{
		this.firstValue = firstValue;
	}

	public double getLastValue()
	{
		return lastValue;
	}

	public void setLastValue(double lastValue)
	{
		this.lastValue = lastValue;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`dates`, `trend_type`, `account_id`, `relative_amount_change`,"
					+ " `firstvalue`, `lastvalue`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getUnixTime());
	        ps.setString(2, getType().toString());
	        ps.setInt(3, getAccountID());
	        ps.setDouble(4, getRelativeAmountChange());
	        ps.setDouble(5, getFirstValue());
	        ps.setDouble(6, getLastValue());
	        
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
					+ "` SET `dates` = ?, `trend_type` = ?, `account_id` = ?, `relative_amount_change` = ?,"
					+ " `firstvalue` = ?, `lastvalue` = ?" 
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getUnixTime());
	        ps.setString(2, getType().toString());
	        ps.setInt(3, getAccountID());
	        ps.setDouble(4, getRelativeAmountChange());
	        ps.setDouble(5, getFirstValue());
	        ps.setDouble(6, getLastValue());
	        
	        int i = 7;
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
				al.add(new TrendLogger(rs.getLong("dates"),
	        			Type.valueOf(rs.getString("trend_type")),
	        			rs.getInt("account_id"),
	        			rs.getDouble("relative_amount_change"),
	        			rs.getDouble("firstvalue"),
	        			rs.getDouble("lastvalue")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<TrendLogger> convert(ArrayList<Object> arrayList)
	{
		ArrayList<TrendLogger> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof TrendLogger)
			{
				l.add((TrendLogger) o);
			}
		}
		return l;
	}
}
