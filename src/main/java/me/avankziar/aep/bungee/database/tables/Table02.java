package main.java.me.avankziar.aep.bungee.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.aep.bungee.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.bungee.database.MysqlHandler;
import main.java.me.avankziar.ifh.bungee.economy.account.Account;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.account.AccountType;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;

public interface Table02
{	
	default boolean createII(AdvancedEconomyPlus plugin, Object object) 
	{
		if(!(object instanceof Account))
		{
			return false;
		}
		Account el = (Account) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.ACCOUNT.getValue()
						+ "`(`account_name`, `account_type`, `account_category`, `account_currency`, `account_predefined`,"
						+ " `owner_uuid`, `owner_type`, `owner_name`, `balance`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, el.getAccountName());
		        preparedStatement.setString(2, el.getType().toString());
		        preparedStatement.setString(3, el.getCategory().toString());
		        preparedStatement.setString(4, el.getCurrency().getUniqueName());
		        preparedStatement.setBoolean(5, el.isPredefinedAccount());
		        preparedStatement.setString(6, el.getOwner().getUUID().toString());
		        preparedStatement.setString(7, el.getOwner().getType().toString());
		        preparedStatement.setString(8, el.getOwner().getName());
		        preparedStatement.setDouble(9, el.getBalance());
		        
		        preparedStatement.executeUpdate();
		        return true;
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return false;
	}
	
	default boolean updateDataII(AdvancedEconomyPlus plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof Account))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		Account el = (Account) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.ACCOUNT.getValue()
						+ "` SET `account_name` = ?,"
						+ " `account_type` = ?, `account_category` = ?, `account_currency`, = ?"
						+ " `account_predefined` = ?, `owner_uuid` = ?, `owner_type` = ?,"
						+ " `owner_name` = ?, `balance` = ?"
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, el.getAccountName());
		        preparedStatement.setString(2, el.getType().toString());
		        preparedStatement.setString(3, el.getCategory().toString());
		        preparedStatement.setString(4, el.getCurrency().getUniqueName());
		        preparedStatement.setBoolean(5, el.isPredefinedAccount());
		        preparedStatement.setString(6, el.getOwner().getUUID().toString());
		        preparedStatement.setString(7, el.getOwner().getType().toString());
		        preparedStatement.setString(8, el.getOwner().getName());
		        preparedStatement.setDouble(9, el.getBalance());
		        int i = 10;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
				
				preparedStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					if (preparedStatement != null) 
					{
						preparedStatement.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        return false;
	}
	
	default Object getDataII(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ACCOUNT.getValue() 
						+ "` WHERE "+whereColumn+" LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return new Account(
		        			result.getInt("id"),
		        			result.getString("account_name"),
		        			AccountType.valueOf(result.getString("account_type")), 
		        			AccountCategory.valueOf(result.getString("account_category")),
		        			plugin.getIFHApi().getCurrency(result.getString("account_currency")),
		        			new EconomyEntity(EconomyEntity.EconomyType.valueOf(result.getString("owner_type")),
		        					UUID.fromString(result.getString("owner_uuid")),
		        					result.getString("owner_name")),
		        			result.getDouble("balance"), 
		        			result.getBoolean("account_predefined"));
		        }
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	default ArrayList<Account> getListII(AdvancedEconomyPlus plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ACCOUNT.getValue() 
					+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<Account> list = new ArrayList<>();
		        while (result.next()) 
		        {
		        	Account el = new Account(
		        			result.getInt("id"),
		        			result.getString("account_name"),
		        			AccountType.valueOf(result.getString("account_type")), 
		        			AccountCategory.valueOf(result.getString("account_category")),
		        			plugin.getIFHApi().getCurrency(result.getString("account_currency")),
		        			new EconomyEntity(EconomyEntity.EconomyType.valueOf(result.getString("owner_type")),
		        					UUID.fromString(result.getString("owner_uuid")),
		        					result.getString("owner_name")),
		        			result.getDouble("balance"), 
		        			result.getBoolean("account_predefined"));
		        	list.add(el);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	default ArrayList<Account> getTopII(AdvancedEconomyPlus plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ACCOUNT.getValue() 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<Account> list = new ArrayList<Account>();
		        while (result.next()) 
		        {
		        	Account el = new Account(
		        			result.getInt("id"),
		        			result.getString("account_name"),
		        			AccountType.valueOf(result.getString("account_type")), 
		        			AccountCategory.valueOf(result.getString("account_category")),
		        			plugin.getIFHApi().getCurrency(result.getString("account_currency")),
		        			new EconomyEntity(EconomyEntity.EconomyType.valueOf(result.getString("owner_type")),
		        					UUID.fromString(result.getString("owner_uuid")),
		        					result.getString("owner_name")),
		        			result.getDouble("balance"), 
		        			result.getBoolean("account_predefined"));
		        	list.add(el);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	default ArrayList<Account> getAllListAtII(AdvancedEconomyPlus plugin, String orderByColumn,
			String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ACCOUNT.getValue()
				+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
				
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<Account> list = new ArrayList<Account>();
		        while (result.next()) 
		        {
		        	Account el = new Account(
		        			result.getInt("id"),
		        			result.getString("account_name"),
		        			AccountType.valueOf(result.getString("account_type")), 
		        			AccountCategory.valueOf(result.getString("account_category")),
		        			plugin.getIFHApi().getCurrency(result.getString("account_currency")),
		        			new EconomyEntity(EconomyEntity.EconomyType.valueOf(result.getString("owner_type")),
		        					UUID.fromString(result.getString("owner_uuid")),
		        					result.getString("owner_name")),
		        			result.getDouble("balance"), 
		        			result.getBoolean("account_predefined"));
		        	list.add(el);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
}
