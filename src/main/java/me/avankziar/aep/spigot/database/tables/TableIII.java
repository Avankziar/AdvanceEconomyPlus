package main.java.me.avankziar.aep.spigot.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.aep.spigot.object.ActionLogger.Type;

public interface TableIII
{
	
	default boolean existIII(AdvancedEconomyPlus plugin, String whereColumn, Object... object) 
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameIII 
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
	
	default boolean createIII(AdvancedEconomyPlus plugin, Object object) 
	{
		if(!(object instanceof ActionLogger))
		{
			return false;
		}
		ActionLogger el = (ActionLogger) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + plugin.getMysqlHandler().tableNameIII 
						+ "`(`datetime`,"
						+ " `from_uuidornumber`, `from_name`, `to_uuidornumber`, `to_name`,"
						+ " `orderer_uuid`, `amount`, `eco_type`, `comment`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, ConvertHandler.serialised(el.getDateTime()));
		        preparedStatement.setString(2, el.getFromUUIDOrNumber());
		        preparedStatement.setString(3, el.getFromName());
		        preparedStatement.setString(4, el.getToUUIDOrNumber());
		        preparedStatement.setString(5, el.getToName());
		        preparedStatement.setString(6, el.getOrdereruuid());
		        preparedStatement.setDouble(7, el.getAmount());
		        preparedStatement.setString(8, el.getType().toString());
		        preparedStatement.setString(9, el.getComment());
		        
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
	
	default boolean updateDataIII(AdvancedEconomyPlus plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof ActionLogger))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		ActionLogger el = (ActionLogger) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + plugin.getMysqlHandler().tableNameIII
						+ "` SET `datetime` = ?,"
						+ " `from_uuidornumber` = ?, `from_name` = ?, `to_uuidornumber` = ?, `to_name` = ?," 
						+ " `orderer_uuid` = ?, `amount` = ?, `eco_type` = ? , `comment` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, ConvertHandler.serialised(el.getDateTime()));
		        preparedStatement.setString(2, el.getFromUUIDOrNumber());
		        preparedStatement.setString(3, el.getFromName());
		        preparedStatement.setString(4, el.getToUUIDOrNumber());
		        preparedStatement.setString(5, el.getToName());
		        preparedStatement.setString(6, el.getOrdereruuid());
		        preparedStatement.setDouble(7, el.getAmount());
		        preparedStatement.setString(8, el.getType().toString());
		        preparedStatement.setString(9, el.getComment());
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
	
	default Object getDataIII(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII 
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
		        	return new ActionLogger(
		        			result.getInt("id"),
		        			ConvertHandler.deserialised(result.getString("datetime")),
		        			result.getString("from_uuidornumber"), result.getString("to_uuidornumber"),
		        			result.getString("from_name"), result.getString("to_name"),
		        			result.getString("orderer_uuid"), result.getDouble("amount"),
		        			Type.valueOf(result.getString("eco_type")), result.getString("comment"));
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
	
	default boolean deleteDataIII(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + plugin.getMysqlHandler().tableNameIII + "` WHERE "+whereColumn;
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
	
	default int lastIDIII(AdvancedEconomyPlus plugin)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameIII + "` ORDER BY `id` DESC LIMIT 1";
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
	
	default int countWhereIDIII(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameIII 
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
	
	default ArrayList<ActionLogger> getListIII(AdvancedEconomyPlus plugin, String orderByColumn, boolean desc,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "";
				if(desc)
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII 
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
				} else
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII 
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" ASC LIMIT "+start+", "+end;
				}
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<ActionLogger> list = new ArrayList<>();
		        while (result.next()) 
		        {
		        	ActionLogger el = new ActionLogger(
		        			result.getInt("id"),
		        			ConvertHandler.deserialised(result.getString("datetime")),
		        			result.getString("from_uuidornumber"), result.getString("to_uuidornumber"),
		        			result.getString("from_name"), result.getString("to_name"),
		        			result.getString("orderer_uuid"), result.getDouble("amount"),
		        			Type.valueOf(result.getString("eco_type")), result.getString("comment"));
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
	
	default ArrayList<ActionLogger> getTopIII(AdvancedEconomyPlus plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<ActionLogger> list = new ArrayList<ActionLogger>();
		        while (result.next()) 
		        {
		        	ActionLogger el = new ActionLogger(
		        			result.getInt("id"),
		        			ConvertHandler.deserialised(result.getString("datetime")),
		        			result.getString("from_uuidornumber"), result.getString("to_uuidornumber"),
		        			result.getString("from_name"), result.getString("to_name"),
		        			result.getString("orderer_uuid"), result.getDouble("amount"),
		        			Type.valueOf(result.getString("eco_type")), result.getString("comment"));
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
	
	default ArrayList<ActionLogger> getAllListAtIII(AdvancedEconomyPlus plugin, String orderByColumn,
			boolean desc, String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "";
				if(desc)
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" ASC";
				}
				
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<ActionLogger> list = new ArrayList<ActionLogger>();
		        while (result.next()) 
		        {
		        	ActionLogger el = new ActionLogger(
		        			result.getInt("id"),
		        			ConvertHandler.deserialised(result.getString("datetime")),
		        			result.getString("from_uuidornumber"), result.getString("to_uuidornumber"),
		        			result.getString("from_name"), result.getString("to_name"),
		        			result.getString("orderer_uuid"), result.getDouble("amount"),
		        			Type.valueOf(result.getString("eco_type")), result.getString("comment"));
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
	
	default ArrayList<ActionLogger> getAllListAtIIIDateTimeModified(AdvancedEconomyPlus plugin, String orderByColumn,
			boolean desc, LocalDateTime start, LocalDateTime end, String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "";
				if(desc)
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
					/*
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" ASC";*/
				}
				
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<ActionLogger> list = new ArrayList<ActionLogger>();
		        while (result.next()) 
		        {
		        	LocalDateTime dt = ConvertHandler.deserialised(result.getString("datetime"));
		        	if(dt.isAfter(start) && dt.isBefore(end))
		        	{
		        		ActionLogger el = new ActionLogger(
			        			result.getInt("id"),
			        			dt,
			        			result.getString("from_uuidornumber"), result.getString("to_uuidornumber"),
			        			result.getString("from_name"), result.getString("to_name"),
			        			result.getString("orderer_uuid"), result.getDouble("amount"),
			        			Type.valueOf(result.getString("eco_type")), result.getString("comment"));
			        	list.add(el);
		        	}
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default String getJSONOutputIII(AdvancedEconomyPlus plugin, String playerName, String orderByColumn,
			boolean desc, String whereColumn, Object...whereObject) 
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "";
				if(desc)
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" ASC";
				}
				
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
			    List list = new ArrayList();
			    if(result!=null)
			    {
			        try {
			            ResultSetMetaData metaData = result.getMetaData();
			            while(result.next())
			            {
			                Map<String,Object> columnMap = new HashMap<String, Object>();
			                for(int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++)
			                {
			                    String val = result.getString(metaData.getColumnName(columnIndex));
			                    String key = metaData.getColumnLabel(columnIndex);
			                    if(val == null)
			                    {
			                    	columnMap.put(key, "");
			                    }
			                    if(val.isEmpty())
			                    {
			                    	columnMap.put(key, "");
			                    }
			                    else if (MatchApi.isLong(val))
			                    {
			                    	columnMap.put(key, Long.parseLong(val));
			                    }
			                    else if (MatchApi.isDouble(val))
			                    {
			                    	columnMap.put(key, Double.parseDouble(val));
			                    }
			                    else
			                    {
			                    	columnMap.put(key,  val);
			                    }
			                }
			                list.add(columnMap);
			            }
			        } catch (SQLException e) {
			            e.printStackTrace();
			        }
			     }
			     return JSONValue.toJSONString(list);
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
