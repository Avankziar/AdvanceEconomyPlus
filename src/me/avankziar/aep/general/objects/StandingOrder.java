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

public class StandingOrder implements MysqlHandable
{
	//Dauerauftrag
	private int id;
	private String name;
	private UUID owner;
	private int accountFrom;
	private int accountTo;
	private double amount; //Betrag
	private double amountPaidSoFar; //Bisher gezahlter Betrag
	private double amountPaidToTax; //Gezahlter betrag an Steuern.
	private long startTime; //Anfangsdatum
	private long repeatingTime; //Millisekunden, wann wieder gezahlt wird.
	private long lastTime; //Millisekunden, wann das letzte man gezahl wurde.
	private long endtime; //Letzte Zahlungsanweisung
	private boolean cancelled; //Wurde der Dauerauftrag abgebrochen? Aka konnte der Spieler einmal nicht zahlen können.
	private boolean paused; //Hat der Spieler den Auftrag pausiert.
	
	public StandingOrder() {}
	
	public StandingOrder(int id, String name, UUID owner, int accountFrom, int accountTo,
			double amount, double amountPaidSoFar, double amountPaidToTax,
			long startTime, long repeatingTime, long lastTime, long endtime, boolean cancelled, boolean paused)
	{
		setID(id);
		setName(name);
		setOwner(owner);
		setAccountFrom(accountFrom);
		setAccountTo(accountTo);
		setAmount(amount);
		setAmountPaidSoFar(amountPaidSoFar);
		setAmountPaidToTax(amountPaidToTax);
		setStartTime(startTime);
		setRepeatingTime(repeatingTime);
		setLastTime(lastTime);
		setCancelled(cancelled);
		setPaused(paused);
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public long getRepeatingTime()
	{
		return repeatingTime;
	}

	public void setRepeatingTime(long repeatingTime)
	{
		this.repeatingTime = repeatingTime;
	}

	public long getLastTime()
	{
		return lastTime;
	}

	public void setLastTime(long lastTime)
	{
		this.lastTime = lastTime;
	}

	public boolean isCancelled()
	{
		return cancelled;
	}

	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}

	public boolean isPaused()
	{
		return paused;
	}

	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}

	public double getAmountPaidSoFar()
	{
		return amountPaidSoFar;
	}

	public void setAmountPaidSoFar(double amountPaidSoFar)
	{
		this.amountPaidSoFar = amountPaidSoFar;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	public int getID()
	{
		return id;
	}

	public void setID(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the accountFrom
	 */
	public int getAccountFrom()
	{
		return accountFrom;
	}

	/**
	 * @param accountFrom the accountFrom to set
	 */
	public void setAccountFrom(int accountFrom)
	{
		this.accountFrom = accountFrom;
	}

	/**
	 * @return the accountTo
	 */
	public int getAccountTo()
	{
		return accountTo;
	}

	/**
	 * @param accountTo the accountTo to set
	 */
	public void setAccountTo(int accountTo)
	{
		this.accountTo = accountTo;
	}

	/**
	 * @return the amountPaidToTax
	 */
	public double getAmountPaidToTax()
	{
		return amountPaidToTax;
	}

	/**
	 * @param amountPaidToTax the amountPaidToTax to set
	 */
	public void setAmountPaidToTax(double amountPaidToTax)
	{
		this.amountPaidToTax = amountPaidToTax;
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

	/**
	 * @return the endtime
	 */
	public long getEndtime()
	{
		return endtime;
	}

	/**
	 * @param endtime the endtime to set
	 */
	public void setEndtime(long endtime)
	{
		this.endtime = endtime;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`standing_order_name`, `owner_uuid`, `from_account`, `to_account`,"
					+ " `amount`, `amount_paid_so_far`, `amount_paid_to_tax`,"
					+ " `start_time`, `repeating_time`, `last_time`, `end_time`, `cancelled`, `paused`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getName());
	        ps.setString(2, getOwner().toString());
	        ps.setInt(3, getAccountFrom());
	        ps.setInt(4, getAccountTo());
	        ps.setDouble(5, getAmount());
	        ps.setDouble(6, getAmountPaidSoFar());
	        ps.setDouble(7, getAmountPaidToTax());
	        ps.setLong(8, getStartTime());
	        ps.setLong(9, getRepeatingTime());
	        ps.setLong(10, getLastTime());
	        ps.setLong(11, getEndtime());
	        ps.setBoolean(12, isCancelled());
	        ps.setBoolean(13, isPaused());
	        
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
					+ "` SET `standing_order_name` = ?, `owner_uuid` = ?, `from_account` = ?, `to_account` = ?,"
					+ " `amount` = ?, `amount_paid_so_far` = ?, `amount_paid_to_tax` = ?," 
					+ " `start_time` = ?, `repeating_time` = ?, `last_time` = ?, `end_time` = ?, `cancelled` = ?, `paused` = ?" 
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getName());
	        ps.setString(2, getOwner().toString());
	        ps.setInt(3, getAccountFrom());
	        ps.setInt(4, getAccountTo());
	        ps.setDouble(5, getAmount());
	        ps.setDouble(6, getAmountPaidSoFar());
	        ps.setDouble(7, getAmountPaidToTax());
	        ps.setLong(8, getStartTime());
	        ps.setLong(9, getRepeatingTime());
	        ps.setLong(10, getLastTime());
	        ps.setLong(11, getEndtime());
	        ps.setBoolean(12, isCancelled());
	        ps.setBoolean(13, isPaused());
	        
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
				al.add(new StandingOrder(
	        			rs.getInt("id"),
	        			rs.getString("standing_order_name"),
	        			UUID.fromString(rs.getString("owner_uuid")),
	        			rs.getInt("from_account"), 
	        			rs.getInt("to_account"),
	        			rs.getDouble("amount"), 
	        			rs.getDouble("amount_paid_so_far"),
	        			rs.getDouble("amount_paid_to_tax"),
	        			rs.getLong("start_time"), 
	        			rs.getLong("repeating_time"), 
	        			rs.getLong("last_time"), 
	        			rs.getLong("end_time"), 
	        			rs.getBoolean("cancelled"), 
	        			rs.getBoolean("paused")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<StandingOrder> convert(ArrayList<Object> arrayList)
	{
		ArrayList<StandingOrder> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof StandingOrder)
			{
				l.add((StandingOrder) o);
			}
		}
		return l;
	}
}