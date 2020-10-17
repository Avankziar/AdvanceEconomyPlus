package main.java.me.avankziar.aep.spigot.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.InventoryHandlerType;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.OrderType;
import main.java.me.avankziar.aep.spigot.object.subs.ActionFilterSettings;
import main.java.me.avankziar.aep.spigot.object.subs.TrendFilterSettings;

public interface TableVII
{
	default boolean existVII(AdvancedEconomyPlus plugin, String whereColumn, Object... object) 
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameVII
						+ "` WHERE "+whereColumn+" LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : object)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return true;
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
		return false;
	}
	
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
				String sql = "INSERT INTO `" + plugin.getMysqlHandler().tableNameVII
						+ "`(`slotid`, `player_uuid`, `banknumber`,"
						+ " `isaction`, `inventoryhandlertype`, `isdescending`,"
						+ " `ordertype`, `minimum`, `maximum`," 
						+ " `from_value`, `to_value`, `orderer`,"
						+ " `comment`, `firststand`, `laststand`)"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setInt(1, ep.getSlotid());
		        preparedStatement.setString(2, ep.getUuid().toString());
		        preparedStatement.setString(3, ep.getBankNumber());
		        preparedStatement.setBoolean(4, ep.isAction());
		        preparedStatement.setString(5, ep.getInventoryHandlerType().toString());
		        preparedStatement.setBoolean(6, ep.isDescending());
		        preparedStatement.setString(7, ep.getOrderType().toString());
		        preparedStatement.setDouble(8, ep.getMin());
		        preparedStatement.setDouble(9, ep.getMax());
		        preparedStatement.setString(10, ep.getActionFilter().getFrom());
		        preparedStatement.setString(11, ep.getActionFilter().getTo());
		        preparedStatement.setString(12, ep.getActionFilter().getOrderer());
		        preparedStatement.setString(13, ep.getActionFilter().getComment());
		        preparedStatement.setDouble(14, ep.getTrendfFilter().getFirstStand());
		        preparedStatement.setDouble(15, ep.getTrendfFilter().getLastStand());
		        
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
				String data = "UPDATE `" + plugin.getMysqlHandler().tableNameI
						+ "` SET `slotid` = ?, `player_uuid` = ?, `banknumber` = ?,"
						+ " `isaction` = ?, `inventoryhandlertype` = ?, `isdescending` = ?,"
						+ " `ordertype` = ?, `minimum` = ?, `maximum` = ?," 
						+ " `from_value` = ?, `to_value` = ?, `orderer` = ?,"
						+ " `comment` = ?, `firststand` = ?, `laststand` = ? "
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setInt(1, ep.getSlotid());
		        preparedStatement.setString(2, ep.getUuid().toString());
		        preparedStatement.setString(3, ep.getBankNumber());
		        preparedStatement.setBoolean(4, ep.isAction());
		        preparedStatement.setString(5, ep.getInventoryHandlerType().toString());
		        preparedStatement.setBoolean(6, ep.isDescending());
		        preparedStatement.setString(7, ep.getOrderType().toString());
		        preparedStatement.setDouble(8, ep.getMin());
		        preparedStatement.setDouble(9, ep.getMax());
		        preparedStatement.setString(10, ep.getActionFilter().getFrom());
		        preparedStatement.setString(11, ep.getActionFilter().getTo());
		        preparedStatement.setString(12, ep.getActionFilter().getOrderer());
		        preparedStatement.setString(13, ep.getActionFilter().getComment());
		        preparedStatement.setDouble(14, ep.getTrendfFilter().getFirstStand());
		        preparedStatement.setDouble(15, ep.getTrendfFilter().getLastStand());
		        int i = 16;
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
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVII
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
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("banknumber"),
		        			0);
		        	ep.setAction(result.getBoolean("isaction"));
		        	ActionFilterSettings afs = new ActionFilterSettings();
		        	afs.setComment(result.getString("comment"));
		        	afs.setFrom(result.getString("from_value"));
		        	afs.setOrderer(result.getString("orderer"));
		        	afs.setTo(result.getString("to_value"));
		        	ep.setActionFilter(afs);
		        	ep.setDescending(result.getBoolean("isdescending"));
		        	ep.setInventoryHandlerType(InventoryHandlerType.valueOf(result.getString("inventoryhandlertype")));
		        	Double min = result.getDouble("minimum");
		        	if(min != 0.0)
		        	{
		        		ep.setMin(min);
		        	}
		        	Double max = result.getDouble("maximum");
		        	if(max != 0.0)
		        	{
		        		ep.setMax(max);
		        	}
		        	ep.setOrderType(OrderType.valueOf(result.getString("ordertype")));
		        	ep.setSlotid(result.getInt("slotid"));
		        	TrendFilterSettings tfs = new TrendFilterSettings();
		        	Double firstStand = result.getDouble("firststand");
		        	if(firstStand != 0.0)
		        	{
		        		tfs.setFirstStand(firstStand);
		        	}
		        	Double lastStand = result.getDouble("laststand");
		        	if(lastStand != 0.0)
		        	{
		        		tfs.setLastStand(lastStand);
		        	}
		        	ep.setTrendfFilter(tfs);
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
	
	default boolean deleteDataVII(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + plugin.getMysqlHandler().tableNameVII+ "` WHERE "+whereColumn;
			preparedStatement = conn.prepareStatement(sql);
			int i = 1;
	        for(Object o : whereObject)
	        {
	        	preparedStatement.setObject(i, o);
	        	i++;
	        }
			preparedStatement.execute();
			return true;
		} catch (Exception e) 
		{
			e.printStackTrace();
		} finally 
		{
			try {
				if (preparedStatement != null) 
				{
					preparedStatement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	default int lastIDVII(AdvancedEconomyPlus plugin)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameVII+ "` ORDER BY `id` DESC LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        while(result.next())
		        {
		        	return result.getInt("id");
		        }
		    } catch (SQLException e) 
			{
		    	e.printStackTrace();
		    	return 0;
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
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return 0;
	}
	
	default int countWhereIDVII(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameI
						+ "` WHERE "+whereColumn
						+ " ORDER BY `id` DESC";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        int count = 0;
		        while(result.next())
		        {
		        	count++;
		        }
		        return count;
		    } catch (SQLException e) 
			{
		    	e.printStackTrace();
		    	return 0;
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
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return 0;
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
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameI
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
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("banknumber"),
		        			0);
		        	ep.setAction(result.getBoolean("isaction"));
		        	ActionFilterSettings afs = new ActionFilterSettings();
		        	afs.setComment(result.getString("comment"));
		        	afs.setFrom(result.getString("from_value"));
		        	afs.setOrderer(result.getString("orderer"));
		        	afs.setTo(result.getString("to_value"));
		        	ep.setActionFilter(afs);
		        	ep.setDescending(result.getBoolean("isdescending"));
		        	ep.setInventoryHandlerType(InventoryHandlerType.valueOf(result.getString("inventoryhandlertype")));
		        	Double min = result.getDouble("minimum");
		        	if(min != 0.0)
		        	{
		        		ep.setMin(min);
		        	}
		        	Double max = result.getDouble("maximum");
		        	if(max != 0.0)
		        	{
		        		ep.setMax(max);
		        	}
		        	ep.setOrderType(OrderType.valueOf(result.getString("ordertype")));
		        	ep.setSlotid(result.getInt("slotid"));
		        	TrendFilterSettings tfs = new TrendFilterSettings();
		        	Double firstStand = result.getDouble("firststand");
		        	if(firstStand != 0.0)
		        	{
		        		tfs.setFirstStand(firstStand);
		        	}
		        	Double lastStand = result.getDouble("laststand");
		        	if(lastStand != 0.0)
		        	{
		        		tfs.setLastStand(lastStand);
		        	}
		        	ep.setTrendfFilter(tfs);
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
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVII
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<LoggerSettings> list = new ArrayList<LoggerSettings>();
		        while (result.next()) 
		        {
		        	LoggerSettings ep = new LoggerSettings(
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("banknumber"),
		        			0);
		        	ep.setAction(result.getBoolean("isaction"));
		        	ActionFilterSettings afs = new ActionFilterSettings();
		        	afs.setComment(result.getString("comment"));
		        	afs.setFrom(result.getString("from_value"));
		        	afs.setOrderer(result.getString("orderer"));
		        	afs.setTo(result.getString("to_value"));
		        	ep.setActionFilter(afs);
		        	ep.setDescending(result.getBoolean("isdescending"));
		        	ep.setInventoryHandlerType(InventoryHandlerType.valueOf(result.getString("inventoryhandlertype")));
		        	Double min = result.getDouble("minimum");
		        	if(min != 0.0)
		        	{
		        		ep.setMin(min);
		        	}
		        	Double max = result.getDouble("maximum");
		        	if(max != 0.0)
		        	{
		        		ep.setMax(max);
		        	}
		        	ep.setOrderType(OrderType.valueOf(result.getString("ordertype")));
		        	TrendFilterSettings tfs = new TrendFilterSettings();
		        	ep.setSlotid(result.getInt("slotid"));
		        	Double firstStand = result.getDouble("firststand");
		        	if(firstStand != 0.0)
		        	{
		        		tfs.setFirstStand(firstStand);
		        	}
		        	Double lastStand = result.getDouble("laststand");
		        	if(lastStand != 0.0)
		        	{
		        		tfs.setLastStand(lastStand);
		        	}
		        	ep.setTrendfFilter(tfs);
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
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameI
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
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
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("banknumber"),
		        			0);
		        	ep.setAction(result.getBoolean("isaction"));
		        	ActionFilterSettings afs = new ActionFilterSettings();
		        	afs.setComment(result.getString("comment"));
		        	afs.setFrom(result.getString("from_value"));
		        	afs.setOrderer(result.getString("orderer"));
		        	afs.setTo(result.getString("to_value"));
		        	ep.setActionFilter(afs);
		        	ep.setDescending(result.getBoolean("isdescending"));
		        	ep.setInventoryHandlerType(InventoryHandlerType.valueOf(result.getString("inventoryhandlertype")));
		        	Double min = result.getDouble("minimum");
		        	if(min != 0.0)
		        	{
		        		ep.setMin(min);
		        	}
		        	Double max = result.getDouble("maximum");
		        	if(max != 0.0)
		        	{
		        		ep.setMax(max);
		        	}
		        	ep.setOrderType(OrderType.valueOf(result.getString("ordertype")));
		        	ep.setSlotid(result.getInt("slotid"));
		        	TrendFilterSettings tfs = new TrendFilterSettings();
		        	Double firstStand = result.getDouble("firststand");
		        	if(firstStand != 0.0)
		        	{
		        		tfs.setFirstStand(firstStand);
		        	}
		        	Double lastStand = result.getDouble("laststand");
		        	if(lastStand != 0.0)
		        	{
		        		tfs.setLastStand(lastStand);
		        	}
		        	ep.setTrendfFilter(tfs);
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
