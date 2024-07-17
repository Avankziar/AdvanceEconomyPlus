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

public class QuickPayAccount implements MysqlHandable
{
	private UUID playerUUID;
	private int accountID;
	private String currencyUniqueName;
	
	public QuickPayAccount() {}
	
	public QuickPayAccount(UUID playerUUID, int accountID, String currencyUniqueName)
	{
		setPlayerUUID(playerUUID);
		setAccountID(accountID);
		setCurrencyUniqueName(currencyUniqueName);
	}

	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public int getAccountID()
	{
		return accountID;
	}

	public void setAccountID(int accountID)
	{
		this.accountID = accountID;
	}

	public String getCurrencyUniqueName()
	{
		return currencyUniqueName;
	}

	public void setCurrencyUniqueName(String currencyUniqueName)
	{
		this.currencyUniqueName = currencyUniqueName;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `account_id`, `account_currency`) "
					+ "VALUES(?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getPlayerUUID().toString());
	        ps.setInt(2, getAccountID());
	        ps.setString(3, getCurrencyUniqueName());
	        
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
					+ "` SET `player_uuid` = ?, `account_id` = ?,"
					+ " `account_currency` = ? "
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getPlayerUUID().toString());
	        ps.setInt(2, getAccountID());
	        ps.setString(3, getCurrencyUniqueName());
	        
	        int i = 4;
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
				al.add(new QuickPayAccount(
	        			UUID.fromString(rs.getString("player_uuid")),
	        			rs.getInt("account_id"),
	        			rs.getString("account_currency")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<QuickPayAccount> convert(ArrayList<Object> arrayList)
	{
		ArrayList<QuickPayAccount> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof QuickPayAccount)
			{
				l.add((QuickPayAccount) o);
			}
		}
		return l;
	}
}