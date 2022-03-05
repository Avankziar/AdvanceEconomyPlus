package main.java.me.avankziar.aep.spigot.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;

public interface Table01
{	
	default boolean createI(AdvancedEconomyPlus plugin, Object object) 
	{
		if(!(object instanceof AEPUser))
		{
			return false;
		}
		AEPUser el = (AEPUser) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.PLAYERDATA.getValue()
						+ "`(`player_uuid`, `player_name`, `wallet_moneyflow_notification`, `bank_moneyflow_notification`,"
						+ " `unixtime`) " 
						+ "VALUES(?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, el.getUUID().toString());
		        preparedStatement.setString(2, el.getName());
		        preparedStatement.setBoolean(3, el.isWalletMoneyFlowNotification());
		        preparedStatement.setBoolean(4, el.isBankMoneyFlowNotification());
		        preparedStatement.setLong(5, el.getLastTimeLogin());
		        
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
	
	default boolean updateDataI(AdvancedEconomyPlus plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof AEPUser))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		AEPUser el = (AEPUser) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
						+ "` SET `player_uuid` = ?, `player_name` = ?, `wallet_moneyflow_notification` = ?, `bank_moneyflow_notification`, = ?"
						+ " `unixtime` = ?"
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, el.getUUID().toString());
		        preparedStatement.setString(2, el.getName());
		        preparedStatement.setBoolean(3, el.isWalletMoneyFlowNotification());
		        preparedStatement.setBoolean(4, el.isBankMoneyFlowNotification());
		        preparedStatement.setLong(5, el.getLastTimeLogin());
		        int i = 6;
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
	
	default Object getDataI(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PLAYERDATA.getValue() 
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
		        	return new AEPUser(
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("player_name"),
		        			result.getBoolean("wallet_moneyflow_notification"), 
		        			result.getBoolean("bank_moneyflow_notification"),
		        			result.getLong("unixtime"));
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
	
	default ArrayList<AEPUser> getListI(AdvancedEconomyPlus plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PLAYERDATA.getValue() 
					+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<AEPUser> list = new ArrayList<>();
		        while (result.next()) 
		        {
		        	AEPUser el = new AEPUser(
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("player_name"),
		        			result.getBoolean("wallet_moneyflow_notification"), 
		        			result.getBoolean("bank_moneyflow_notification"),
		        			result.getLong("unixtime"));
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
	
	default ArrayList<AEPUser> getTopI(AdvancedEconomyPlus plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PLAYERDATA.getValue() 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<AEPUser> list = new ArrayList<AEPUser>();
		        while (result.next()) 
		        {
		        	AEPUser el = new AEPUser(
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("player_name"),
		        			result.getBoolean("wallet_moneyflow_notification"), 
		        			result.getBoolean("bank_moneyflow_notification"),
		        			result.getLong("unixtime"));
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
	
	default ArrayList<AEPUser> getAllListAtI(AdvancedEconomyPlus plugin, String orderByColumn,
			String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PLAYERDATA.getValue()
				+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
				
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<AEPUser> list = new ArrayList<AEPUser>();
		        while (result.next()) 
		        {
		        	AEPUser el = new AEPUser(
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("player_name"),
		        			result.getBoolean("wallet_moneyflow_notification"), 
		        			result.getBoolean("bank_moneyflow_notification"),
		        			result.getLong("unixtime"));
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
