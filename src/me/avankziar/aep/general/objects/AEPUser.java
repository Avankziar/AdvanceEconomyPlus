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

public class AEPUser implements MysqlHandable
{
	private UUID uuid;
	private String name;
	private boolean walletMoneyFlowNotification;
	private boolean bankMoneyFlowNotification;
	private long lastTimeLogin;
	
	public AEPUser() {}
	
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
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `player_name`, `wallet_moneyflow_notification`, `bank_moneyflow_notification`,"
					+ " `unixtime`) " 
					+ "VALUES(?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID().toString());
	        ps.setString(2, getName());
	        ps.setBoolean(3, isWalletMoneyFlowNotification());
	        ps.setBoolean(4, isBankMoneyFlowNotification());
	        ps.setLong(5, getLastTimeLogin());
	        
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
					+ "` SET `player_uuid` = ?, `player_name` = ?, `wallet_moneyflow_notification` = ?, `bank_moneyflow_notification` = ?,"
					+ " `unixtime` = ?"
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID().toString());
	        ps.setString(2, getName());
	        ps.setBoolean(3, isWalletMoneyFlowNotification());
	        ps.setBoolean(4, isBankMoneyFlowNotification());
	        ps.setLong(5, getLastTimeLogin());
	        
	        int i = 6;
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
				al.add(new AEPUser(
	        			UUID.fromString(rs.getString("player_uuid")),
	        			rs.getString("player_name"),
	        			rs.getBoolean("wallet_moneyflow_notification"), 
	        			rs.getBoolean("bank_moneyflow_notification"),
	        			rs.getLong("unixtime")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<AEPUser> convert(ArrayList<Object> arrayList)
	{
		ArrayList<AEPUser> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof AEPUser)
			{
				l.add((AEPUser) o);
			}
		}
		return l;
	}
}