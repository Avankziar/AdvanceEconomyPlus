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
import me.avankziar.ifh.general.economy.account.AccountManagementType;

public class AccountManagement implements MysqlHandable
{
	private int accountID;
	private UUID uuid;
	private AccountManagementType maganagementType;
	
	public AccountManagement() {}
	
	public AccountManagement(int accountID, UUID uuid, AccountManagementType maganagementType)
	{
		setAccountID(accountID);
		setUUID(uuid);
		setMaganagementType(maganagementType);
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
	 * @return the playerUUID
	 */
	public UUID getUUID()
	{
		return uuid;
	}

	/**
	 * @param playerUUID the playerUUID to set
	 */
	public void setUUID(UUID playerUUID)
	{
		this.uuid = playerUUID;
	}

	/**
	 * @return the maganagementType
	 */
	public AccountManagementType getMaganagementType()
	{
		return maganagementType;
	}

	/**
	 * @param maganagementType the maganagementType to set
	 */
	public void setMaganagementType(AccountManagementType maganagementType)
	{
		this.maganagementType = maganagementType;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `account_id`,"
					+ " `account_management_type`)"
					+ "VALUES(?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID().toString());
	        ps.setInt(2, getAccountID());
	        ps.setString(3, getMaganagementType().toString());
	        
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
					+ " `account_management_type` = ? "
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID().toString());
	        ps.setInt(2, getAccountID());
	        ps.setString(3, getMaganagementType().toString());
	        
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
				al.add(new AccountManagement(
	        			rs.getInt("account_id"),
	        			UUID.fromString(rs.getString("player_uuid")),
	        			AccountManagementType.valueOf(rs.getString("account_management_type"))));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<AccountManagement> convert(ArrayList<Object> arrayList)
	{
		ArrayList<AccountManagement> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof AccountManagement)
			{
				l.add((AccountManagement) o);
			}
		}
		return l;
	}
}