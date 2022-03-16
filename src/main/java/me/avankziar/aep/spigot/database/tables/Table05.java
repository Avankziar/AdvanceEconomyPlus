package main.java.me.avankziar.aep.spigot.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.aep.general.objects.StandingOrder;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;

public interface Table05
{
	default boolean createV(AdvancedEconomyPlus plugin, Object object) 
	{
		if(!(object instanceof StandingOrder))
		{
			return false;
		}
		StandingOrder ep = (StandingOrder) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.STANDINGORDER.getValue() 
						+ "`(`standing_order_name`, `owner_uuid`, `from_account`, `to_account`,"
						+ " `amount`, `amount_paid_so_far`, `amount_paid_to_tax`,"
						+ " `start_time`, `repeating_time`, `last_time`, `cancelled`, `paused`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, ep.getName());
		        preparedStatement.setString(2, ep.getOwner().toString());
		        preparedStatement.setInt(3, ep.getAccountFrom());
		        preparedStatement.setInt(4, ep.getAccountTo());
		        preparedStatement.setDouble(5, ep.getAmount());
		        preparedStatement.setDouble(6, ep.getAmountPaidSoFar());
		        preparedStatement.setDouble(7, ep.getAmountPaidToTax());
		        preparedStatement.setLong(8, ep.getStartTime());
		        preparedStatement.setLong(9, ep.getRepeatingTime());
		        preparedStatement.setLong(10, ep.getLastTime());
		        preparedStatement.setBoolean(11, ep.isCancelled());
		        preparedStatement.setBoolean(12, ep.isPaused());
		        
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
	
	default boolean updateDataV(AdvancedEconomyPlus plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof StandingOrder))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		StandingOrder ep = (StandingOrder) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.STANDINGORDER.getValue() 
						+ "` SET `standing_order_name` = ?, `owner_uuid` = ?, `from_account` = ?, `to_account` = ?,"
						+ " `amount` = ?, `amount_paid_so_far` = ?, `amount_paid_to_tax` = ?," 
						+ " `start_time` = ?, `repeating_time` = ?, `last_time` = ?, `cancelled` = ?, `paused` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, ep.getName());
		        preparedStatement.setString(2, ep.getOwner().toString());
		        preparedStatement.setInt(3, ep.getAccountFrom());
		        preparedStatement.setInt(4, ep.getAccountTo());
		        preparedStatement.setDouble(5, ep.getAmount());
		        preparedStatement.setDouble(6, ep.getAmountPaidSoFar());
		        preparedStatement.setDouble(7, ep.getAmountPaidToTax());
		        preparedStatement.setLong(8, ep.getStartTime());
		        preparedStatement.setLong(9, ep.getRepeatingTime());
		        preparedStatement.setLong(10, ep.getLastTime());
		        preparedStatement.setBoolean(11, ep.isCancelled());
		        preparedStatement.setBoolean(12, ep.isPaused());
		        int i = 13;
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
	
	default Object getDataV(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.STANDINGORDER.getValue()  
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
		        	return new StandingOrder(
		        			result.getInt("id"),
		        			result.getString("standing_order_name"),
		        			UUID.fromString(result.getString("owner_uuid")),
		        			result.getInt("from_account"), 
		        			result.getInt("to_account"),
		        			result.getDouble("amount"), 
		        			result.getDouble("amount_paid_so_far"),
		        			result.getDouble("amount_paid_to_tax"),
		        			result.getLong("start_time"), 
		        			result.getLong("repeating_time"), 
		        			result.getLong("last_time"), 
		        			result.getBoolean("cancelled"), 
		        			result.getBoolean("paused"));
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
	
	default ArrayList<StandingOrder> getListV(AdvancedEconomyPlus plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.STANDINGORDER.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<StandingOrder> list = new ArrayList<StandingOrder>();
		        while (result.next()) 
		        {
		        	StandingOrder ep = new StandingOrder(
		        			result.getInt("id"),
		        			result.getString("standing_order_name"),
		        			UUID.fromString(result.getString("owner_uuid")),
		        			result.getInt("from_account"), 
		        			result.getInt("to_account"),
		        			result.getDouble("amount"), 
		        			result.getDouble("amount_paid_so_far"),
		        			result.getDouble("amount_paid_to_tax"),
		        			result.getLong("start_time"), 
		        			result.getLong("repeating_time"), 
		        			result.getLong("last_time"), 
		        			result.getBoolean("cancelled"), 
		        			result.getBoolean("paused"));
		        	list.add(ep);
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
	
	default ArrayList<StandingOrder> getTopV(AdvancedEconomyPlus plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.STANDINGORDER.getValue()  
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<StandingOrder> list = new ArrayList<StandingOrder>();
		        while (result.next()) 
		        {
		        	StandingOrder ep = new StandingOrder(
		        			result.getInt("id"),
		        			result.getString("standing_order_name"),
		        			UUID.fromString(result.getString("owner_uuid")),
		        			result.getInt("from_account"), 
		        			result.getInt("to_account"),
		        			result.getDouble("amount"), 
		        			result.getDouble("amount_paid_so_far"),
		        			result.getDouble("amount_paid_to_tax"),
		        			result.getLong("start_time"), 
		        			result.getLong("repeating_time"), 
		        			result.getLong("last_time"), 
		        			result.getBoolean("cancelled"), 
		        			result.getBoolean("paused"));
		        	list.add(ep);
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
	
	default ArrayList<StandingOrder> getAllListAtV(AdvancedEconomyPlus plugin, String orderByColumn,
			String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.STANDINGORDER.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<StandingOrder> list = new ArrayList<StandingOrder>();
		        while (result.next()) 
		        {
		        	StandingOrder ep = new StandingOrder(
		        			result.getInt("id"),
		        			result.getString("standing_order_name"),
		        			UUID.fromString(result.getString("owner_uuid")),
		        			result.getInt("from_account"), 
		        			result.getInt("to_account"),
		        			result.getDouble("amount"), 
		        			result.getDouble("amount_paid_so_far"),
		        			result.getDouble("amount_paid_to_tax"),
		        			result.getLong("start_time"), 
		        			result.getLong("repeating_time"), 
		        			result.getLong("last_time"), 
		        			result.getBoolean("cancelled"), 
		        			result.getBoolean("paused"));
		        	list.add(ep);
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
