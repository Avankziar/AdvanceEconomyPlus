package main.java.me.avankziar.aep.spigot.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;

public interface TableV
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
						+ "`(`standing_order_name`, `from_player`, `to_player`, `amount`, `amountpaidsofar`,"
						+ " `starttime`, `repeatingtime`, `lasttime`, `cancelled`, `paused`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, ep.getName());
		        preparedStatement.setString(2, ep.getFrom());
		        preparedStatement.setString(3, ep.getTo());
		        preparedStatement.setDouble(4, ep.getAmount());
		        preparedStatement.setDouble(5, ep.getAmountPaidSoFar());
		        preparedStatement.setLong(6, ep.getStartTime());
		        preparedStatement.setLong(7, ep.getRepeatingTime());
		        preparedStatement.setLong(8, ep.getLastTime());
		        preparedStatement.setBoolean(9, ep.isCancelled());
		        preparedStatement.setBoolean(10, ep.isPaused());
		        
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
						+ "` SET `standing_order_name` = ?, `from_player` = ?, `to_player` = ?, `amount` = ?, `amountpaidsofar` = ?," 
						+ " `starttime` = ?, `repeatingtime` = ?, `lasttime` = ?, `cancelled` = ?, `paused` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, ep.getName());
				preparedStatement.setString(2, ep.getFrom());
		        preparedStatement.setString(3, ep.getTo());
		        preparedStatement.setDouble(4, ep.getAmount());
		        preparedStatement.setDouble(5, ep.getAmountPaidSoFar());
		        preparedStatement.setLong(6, ep.getStartTime());
		        preparedStatement.setLong(7, ep.getRepeatingTime());
		        preparedStatement.setLong(8, ep.getLastTime());
		        preparedStatement.setBoolean(9, ep.isCancelled());
		        preparedStatement.setBoolean(10, ep.isPaused());
		        int i = 11;
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
		        			result.getString("from_player"), 
		        			result.getString("to_player"),
		        			result.getDouble("amount"), 
		        			result.getDouble("amountpaidsofar"), 
		        			result.getLong("starttime"), 
		        			result.getLong("repeatingtime"), 
		        			result.getLong("lasttime"), 
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
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
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
		        			result.getString("from_player"), 
		        			result.getString("to_player"),
		        			result.getDouble("amount"), 
		        			result.getDouble("amountpaidsofar"), 
		        			result.getLong("starttime"), 
		        			result.getLong("repeatingtime"), 
		        			result.getLong("lasttime"), 
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
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<StandingOrder> list = new ArrayList<StandingOrder>();
		        while (result.next()) 
		        {
		        	StandingOrder ep = new StandingOrder(
		        			result.getInt("id"),
		        			result.getString("standing_order_name"),
		        			result.getString("from_player"), 
		        			result.getString("to_player"),
		        			result.getDouble("amount"), 
		        			result.getDouble("amountpaidsofar"), 
		        			result.getLong("starttime"), 
		        			result.getLong("repeatingtime"), 
		        			result.getLong("lasttime"), 
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
		        			result.getString("from_player"), 
		        			result.getString("to_player"),
		        			result.getDouble("amount"), 
		        			result.getDouble("amountpaidsofar"), 
		        			result.getLong("starttime"), 
		        			result.getLong("repeatingtime"), 
		        			result.getLong("lasttime"), 
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
