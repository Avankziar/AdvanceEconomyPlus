package main.java.me.avankziar.aep.spigot.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.TrendLogger;
import main.java.me.avankziar.aep.spigot.object.TrendLogger.Type;

public interface Table04
{
	default boolean createIV(AdvancedEconomyPlus plugin, Object object) 
	{
		if(!(object instanceof TrendLogger))
		{
			return false;
		}
		TrendLogger tl = (TrendLogger) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.TREND.getValue()
						+ "`(`dates`, `trend_type`, `account_id`, `relative_amount_change`,"
						+ " `firstvalue`, `lastvalue`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setLong(1, tl.getUnixTime());
		        preparedStatement.setString(2, tl.getType().toString());
		        preparedStatement.setInt(3, tl.getAccountID());
		        preparedStatement.setDouble(4, tl.getRelativeAmountChange());
		        preparedStatement.setDouble(5, tl.getFirstValue());
		        preparedStatement.setDouble(6, tl.getLastValue());
		        
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
	
	default boolean updateDataIV(AdvancedEconomyPlus plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof TrendLogger))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		TrendLogger tl = (TrendLogger) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.TREND.getValue()
						+ "` SET `dates` = ?, `trend_type` = ?, `account_id` = ?, `relative_amount_change` = ?,"
						+ " `firstvalue` = ?, `lastvalue` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setLong(1, tl.getUnixTime());
		        preparedStatement.setString(2, tl.getType().toString());
		        preparedStatement.setInt(3, tl.getAccountID());
		        preparedStatement.setDouble(4, tl.getRelativeAmountChange());
		        preparedStatement.setDouble(5, tl.getFirstValue());
		        preparedStatement.setDouble(6, tl.getLastValue());
		        
		        int i = 7;
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
	
	default Object getDataIV(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.TREND.getValue() 
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
		        	return new TrendLogger(result.getLong("dates"),
		        			Type.valueOf(result.getString("trend_type")),
		        			result.getInt("account_id"),
		        			result.getDouble("relative_amount_change"),
		        			result.getDouble("firstvalue"),
		        			result.getDouble("lastvalue"));
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
	
	default ArrayList<TrendLogger> getListIV(AdvancedEconomyPlus plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.TREND.getValue() 
				+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<TrendLogger> list = new ArrayList<TrendLogger>();
		        while (result.next()) 
		        {
		        	TrendLogger tl = new TrendLogger(result.getLong("dates"),
		        			Type.valueOf(result.getString("trend_type")),
		        			result.getInt("account_id"),
		        			result.getDouble("relative_amount_change"),
		        			result.getDouble("firstvalue"),
		        			result.getDouble("lastvalue"));
		        	list.add(tl);
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
	
	default ArrayList<Object> getTopIV(AdvancedEconomyPlus plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.TREND.getValue() 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<Object> list = new ArrayList<Object>();
		        while (result.next()) 
		        {
		        	TrendLogger tl = new TrendLogger(result.getLong("dates"),
		        			Type.valueOf(result.getString("trend_type")),
		        			result.getInt("account_id"),
		        			result.getDouble("relative_amount_change"),
		        			result.getDouble("firstvalue"),
		        			result.getDouble("lastvalue"));
		        	list.add(tl);
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
	
	default ArrayList<TrendLogger> getAllListAtIV(AdvancedEconomyPlus plugin, String orderByColumn,
			String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.TREND.getValue()
				+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<TrendLogger> list = new ArrayList<TrendLogger>();
		        while (result.next()) 
		        {
		        	TrendLogger tl = new TrendLogger(result.getLong("dates"),
		        			Type.valueOf(result.getString("trend_type")),
		        			result.getInt("account_id"),
		        			result.getDouble("relative_amount_change"),
		        			result.getDouble("firstvalue"),
		        			result.getDouble("lastvalue"));
		        	list.add(tl);
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
	default String getJSONOutputIV(AdvancedEconomyPlus plugin, String playerName, String orderByColumn,
			String whereColumn, Object...whereObject) 
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.TREND.getValue()
				+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
				
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
			                       columnMap.put(key, "");
			                   else if (val.chars().allMatch(Character::isDigit))
			                       columnMap.put(key,  Long.parseLong(val));
			                   else
			                       columnMap.put(key,  val);
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