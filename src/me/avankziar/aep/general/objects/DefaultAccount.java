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
import me.avankziar.ifh.general.economy.account.AccountCategory;

public class DefaultAccount implements MysqlHandable
{
	private UUID playerUUID;
	private int accountID;
	private String currencyUniqueName;
	private AccountCategory category;
	
	public DefaultAccount() {}
	
	public DefaultAccount(UUID playerUUID, int accountID, String currencyUniqueName, AccountCategory category)
	{
		setPlayerUUID(playerUUID);
		setAccountID(accountID);
		setCurrencyUniqueName(currencyUniqueName);
		setCategory(category);
	}

	/**
	 * @return the playerUUID
	 */
	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	/**
	 * @param playerUUID the playerUUID to set
	 */
	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
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

	/**
	 * @return the currencyUniqueName
	 */
	public String getCurrencyUniqueName()
	{
		return currencyUniqueName;
	}

	/**
	 * @param currencyUniqueName the currencyUniqueName to set
	 */
	public void setCurrencyUniqueName(String currencyUniqueName)
	{
		this.currencyUniqueName = currencyUniqueName;
	}

	/**
	 * @return the category
	 */
	public AccountCategory getCategory()
	{
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(AccountCategory category)
	{
		this.category = category;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `account_id`,"
					+ " `account_currency`, `account_category`)"
					+ "VALUES(?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getPlayerUUID().toString());
	        ps.setInt(2, getAccountID());
	        ps.setString(3, getCurrencyUniqueName());
	        ps.setString(4, getCategory().toString());
	        
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
					+ " `account_currency` = ?, `account_category` = ? "
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getPlayerUUID().toString());
	        ps.setInt(2, getAccountID());
	        ps.setString(3, getCurrencyUniqueName());
	        ps.setString(4, getCategory().toString());
	        
	        int i = 5;
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
				al.add(new DefaultAccount(
	        			UUID.fromString(rs.getString("player_uuid")),
	        			rs.getInt("account_id"),
	        			rs.getString("account_currency"),
	        			AccountCategory.valueOf(rs.getString("account_category"))));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<DefaultAccount> convert(ArrayList<Object> arrayList)
	{
		ArrayList<DefaultAccount> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof DefaultAccount)
			{
				l.add((DefaultAccount) o);
			}
		}
		return l;
	}
}