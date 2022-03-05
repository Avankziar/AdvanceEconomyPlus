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
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.InventoryHandlerType;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.OrderType;


public interface Table07
{
	default boolean createVII(AdvancedEconomyPlus plugin, Object object) 
	{
		if(!(object instanceof LoggerSettings))
		{
			return false;
		}
		LoggerSettings ep = (LoggerSettings) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.LOGGERSETTINGSPRESET.getValue() 
						+ "`(`slotid`, `player_uuid`, `account_id`,"
						+ " `isaction`, `inventoryhandlertype`, `isdescending`,"
						+ " `ordertype`, `minimum`, `maximum`," 
						+ " `category`, `orderer`, `comment`, `firststand`, `laststand`)"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setInt(1, ep.getSlotid());
		        preparedStatement.setString(2, ep.getOwner().toString());
		        preparedStatement.setInt(3, ep.getAccountID());
		        preparedStatement.setBoolean(4, ep.isAction());
		        preparedStatement.setString(5, ep.getInventoryHandlerType().toString());
		        preparedStatement.setBoolean(6, ep.isDescending());
		        preparedStatement.setString(7, ep.getOrderType().toString());
		        Double min = ep.getMin();
		        if(min == null) {min = 0.0;}
		        Double max = ep.getMax();
		        if(max == null) {max = 0.0;}
		        preparedStatement.setDouble(8, min);
		        preparedStatement.setDouble(9, max);
		        preparedStatement.setString(10, ep.getActionFilter().getCategory());
		        preparedStatement.setString(11, ep.getActionFilter().getOrderer());
		        preparedStatement.setString(12, ep.getActionFilter().getComment());
		        Double firststand = ep.getTrendfFilter().getFirstStand();
		        if(firststand == null) {firststand = 0.0;}
		        Double laststand = ep.getTrendfFilter().getLastStand();
		        if(laststand == null) {laststand = 0.0;}
		        preparedStatement.setDouble(13, firststand);
		        preparedStatement.setDouble(14, laststand);
		        
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
	
	default boolean updateDataVII(AdvancedEconomyPlus plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof LoggerSettings))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		LoggerSettings ep = (LoggerSettings) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.LOGGERSETTINGSPRESET.getValue()
						+ "` SET `slotid` = ?, `player_uuid` = ?, `account_id` = ?,"
						+ " `isaction` = ?, `inventoryhandlertype` = ?, `isdescending` = ?,"
						+ " `ordertype` = ?, `minimum` = ?, `maximum` = ?," 
						+ " `category` = ?, `orderer` = ?,"
						+ " `comment` = ?, `firststand` = ?, `laststand` = ? "
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setInt(1, ep.getSlotid());
		        preparedStatement.setString(2, ep.getOwner().toString());
		        preparedStatement.setInt(3, ep.getAccountID());
		        preparedStatement.setBoolean(4, ep.isAction());
		        preparedStatement.setString(5, ep.getInventoryHandlerType().toString());
		        preparedStatement.setBoolean(6, ep.isDescending());
		        preparedStatement.setString(7, ep.getOrderType().toString());
		        Double min = ep.getMin();
		        if(min == null) {min = 0.0;}
		        Double max = ep.getMax();
		        if(max == null) {max = 0.0;}
		        preparedStatement.setDouble(8, min);
		        preparedStatement.setDouble(9, max);
		        preparedStatement.setString(10, ep.getActionFilter().getCategory());
		        preparedStatement.setString(11, ep.getActionFilter().getOrderer());
		        preparedStatement.setString(12, ep.getActionFilter().getComment());
		        Double firststand = ep.getTrendfFilter().getFirstStand();
		        if(firststand == null) {firststand = 0.0;}
		        Double laststand = ep.getTrendfFilter().getLastStand();
		        if(laststand == null) {laststand = 0.0;}
		        preparedStatement.setDouble(13, firststand);
		        preparedStatement.setDouble(14, laststand);
		        int i = 15;
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
	
	default Object getDataVII(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.LOGGERSETTINGSPRESET.getValue()
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
		        	LoggerSettings ep = new LoggerSettings(
		        			result.getInt("slotid"),
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getInt("account_id"),
		        			result.getBoolean("isaction"),
		        			InventoryHandlerType.valueOf(result.getString("inventoryhandlertype")),
		        			result.getBoolean("isdescending"),
		        			OrderType.valueOf(result.getString("ordertype")),
		        			Double.valueOf(result.getDouble("minimum")),
		        			Double.valueOf(result.getDouble("maximum")),
		        			result.getString("category"),
		        			result.getString("orderer"),
		        			result.getString("comment"),
		        			result.getDouble("firststand"),
		        			result.getDouble("laststand"));
		        	return ep;
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
	
	default ArrayList<LoggerSettings> getListVII(AdvancedEconomyPlus plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.LOGGERSETTINGSPRESET.getValue()
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<LoggerSettings> list = new ArrayList<LoggerSettings>();
		        while (result.next()) 
		        {
		        	LoggerSettings ep = new LoggerSettings(
		        			result.getInt("slotid"),
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getInt("account_id"),
		        			result.getBoolean("isaction"),
		        			InventoryHandlerType.valueOf(result.getString("inventoryhandlertype")),
		        			result.getBoolean("isdescending"),
		        			OrderType.valueOf(result.getString("ordertype")),
		        			Double.valueOf(result.getDouble("minimum")),
		        			Double.valueOf(result.getDouble("maximum")),
		        			result.getString("category"),
		        			result.getString("orderer"),
		        			result.getString("comment"),
		        			result.getDouble("firststand"),
		        			result.getDouble("laststand"));
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
	
	default ArrayList<LoggerSettings> getTopVII(AdvancedEconomyPlus plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.LOGGERSETTINGSPRESET.getValue()
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<LoggerSettings> list = new ArrayList<LoggerSettings>();
		        while (result.next()) 
		        {
		        	LoggerSettings ep = new LoggerSettings(
		        			result.getInt("slotid"),
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getInt("account_id"),
		        			result.getBoolean("isaction"),
		        			InventoryHandlerType.valueOf(result.getString("inventoryhandlertype")),
		        			result.getBoolean("isdescending"),
		        			OrderType.valueOf(result.getString("ordertype")),
		        			Double.valueOf(result.getDouble("minimum")),
		        			Double.valueOf(result.getDouble("maximum")),
		        			result.getString("category"),
		        			result.getString("orderer"),
		        			result.getString("comment"),
		        			result.getDouble("firststand"),
		        			result.getDouble("laststand"));
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
	
	default ArrayList<LoggerSettings> getAllListAtVII(AdvancedEconomyPlus plugin, String orderByColumn,
			String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.LOGGERSETTINGSPRESET.getValue()
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<LoggerSettings> list = new ArrayList<LoggerSettings>();
		        while (result.next()) 
		        {
		        	LoggerSettings ep = new LoggerSettings(
		        			result.getInt("slotid"),
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getInt("account_id"),
		        			result.getBoolean("isaction"),
		        			InventoryHandlerType.valueOf(result.getString("inventoryhandlertype")),
		        			result.getBoolean("isdescending"),
		        			OrderType.valueOf(result.getString("ordertype")),
		        			Double.valueOf(result.getDouble("minimum")),
		        			Double.valueOf(result.getDouble("maximum")),
		        			result.getString("category"),
		        			result.getString("orderer"),
		        			result.getString("comment"),
		        			result.getDouble("firststand"),
		        			result.getDouble("laststand"));
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
