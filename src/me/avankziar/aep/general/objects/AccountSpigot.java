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
import me.avankziar.aep.spigot.AEP;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class AccountSpigot extends me.avankziar.ifh.spigot.economy.account.Account implements MysqlHandable
{
	public AccountSpigot()
	{
		super("", AccountType.WALLET, AccountCategory.MAIN, null, null, 0, false);
	}
	
	public AccountSpigot(String accountName, AccountType type, AccountCategory category, EconomyCurrency currency,
			EconomyEntity owner, double balance, boolean predefinedAccount)
	{
		super(accountName, type, category, currency, owner, balance, predefinedAccount);
	}
	
	public AccountSpigot(int id, String accountName, AccountType type, AccountCategory category, EconomyCurrency currency,
			EconomyEntity owner, double balance, boolean predefinedAccount)
	{
		super(id, accountName, type, category, currency, owner, balance, predefinedAccount);
	}
	
	public AccountSpigot(me.avankziar.ifh.spigot.economy.account.Account acc)
	{
		super(acc.getID(), acc.getAccountName(), acc.getType(), acc.getCategory(),
				acc.getCurrency(), acc.getOwner(), acc.getBalance(), acc.isPredefinedAccount());
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`account_name`, `account_type`, `account_category`, `account_currency`, `account_predefined`,"
					+ " `owner_uuid`, `owner_type`, `owner_name`, `balance`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getAccountName());
	        ps.setString(2, getType().toString());
	        ps.setString(3, getCategory().toString());
	        ps.setString(4, getCurrency().getUniqueName());
	        ps.setBoolean(5, isPredefinedAccount());
	        ps.setString(6, getOwner().getUUID().toString());
	        ps.setString(7, getOwner().getType().toString());
	        ps.setString(8, getOwner().getName());
	        ps.setDouble(9, getBalance());
	        
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
					+ "` SET `account_name` = ?,"
					+ " `account_type` = ?, `account_category` = ?, `account_currency` = ?,"
					+ " `account_predefined` = ?, `owner_uuid` = ?, `owner_type` = ?,"
					+ " `owner_name` = ?, `balance` = ?"
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getAccountName());
	        ps.setString(2, getType().toString());
	        ps.setString(3, getCategory().toString());
	        ps.setString(4, getCurrency().getUniqueName());
	        ps.setBoolean(5, isPredefinedAccount());
	        ps.setString(6, getOwner().getUUID().toString());
	        ps.setString(7, getOwner().getType().toString());
	        ps.setString(8, getOwner().getName());
	        ps.setDouble(9, getBalance());
	        
	        int i = 10;
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
				al.add(new AccountSpigot(
	        			rs.getInt("id"),
	        			rs.getString("account_name"),
	        			AccountType.valueOf(rs.getString("account_type")), 
	        			AccountCategory.valueOf(rs.getString("account_category")),
	        			AEP.getPlugin().getIFHApi().getCurrency(rs.getString("account_currency")),
	        			new EconomyEntity(EconomyEntity.EconomyType.valueOf(rs.getString("owner_type")),
	        					UUID.fromString(rs.getString("owner_uuid")),
	        					rs.getString("owner_name")),
	        			rs.getDouble("balance"), 
	        			rs.getBoolean("account_predefined")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<AccountSpigot> convert(ArrayList<Object> arrayList)
	{
		ArrayList<AccountSpigot> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof AccountSpigot)
			{
				l.add((AccountSpigot) o);
			}
		}
		return l;
	}
}