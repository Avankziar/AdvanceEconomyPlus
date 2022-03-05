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
import java.util.UUID;

import org.json.simple.JSONValue;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;

public interface Table03
{	
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
				String sql = "INSERT INTO `" + MysqlHandler.Type.ACTION.getValue()
						+ "`(`unixtime`,"
						+ " `from_account_id`, `to_account_id`, `tax_account_id`,"
						+ " `orderer_type`, `orderer_uuid`, `orderer_plugin`,"
						+ " `amount_to_withdraw`, `amount_to_deposit`, `amount_to_tax`,"
						+ " `category`, `comment`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setLong(1, el.getUnixTime());
		        preparedStatement.setInt(2, el.getFromAccountID());
		        preparedStatement.setInt(3, el.getToAccountID());
		        preparedStatement.setInt(4, el.getTaxAccountID());
		        preparedStatement.setString(5, el.getOrderType().toString());
		        preparedStatement.setString(6, el.getOrdererUUID() != null ? el.getOrdererUUID().toString() : null);
		        preparedStatement.setString(7, el.getOrdererPlugin() != null ? el.getOrdererPlugin() : null);
		        preparedStatement.setDouble(8, el.getAmountToWithdraw());
		        preparedStatement.setDouble(9, el.getAmountToDeposit());
		        preparedStatement.setDouble(10, el.getAmountToTax());
		        preparedStatement.setString(11, el.getCategory());
		        preparedStatement.setString(12, el.getComment());
		        
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
				String data = "UPDATE `" + MysqlHandler.Type.ACTION.getValue()
						+ "` SET `unixtime` = ?,"
						+ " `from_account_id` = ?, `to_account_id` = ? = ?, `tax_account_id`, = ?"
						+ " `orderer_type` = ?, `orderer_uuid` = ?, `orderer_plugin` = ?,"
						+ " `amount_to_withdraw` = ?, `amount_to_deposit` = ?, `amount_to_tax`, = ?"
						+ " `category` = ?, `comment` = ?"
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setLong(1, el.getUnixTime());
		        preparedStatement.setInt(2, el.getFromAccountID());
		        preparedStatement.setInt(3, el.getToAccountID());
		        preparedStatement.setInt(4, el.getTaxAccountID());
		        preparedStatement.setString(5, el.getOrderType().toString());
		        preparedStatement.setString(6, el.getOrdererUUID() != null ? el.getOrdererUUID().toString() : null);
		        preparedStatement.setString(7, el.getOrdererPlugin() != null ? el.getOrdererPlugin() : null);
		        preparedStatement.setDouble(8, el.getAmountToWithdraw());
		        preparedStatement.setDouble(9, el.getAmountToDeposit());
		        preparedStatement.setDouble(10, el.getAmountToTax());
		        preparedStatement.setString(11, el.getCategory());
		        preparedStatement.setString(12, el.getComment());
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
	
	default Object getDataIII(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ACTION.getValue() 
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
		        			result.getLong("unixtime"),
		        			result.getInt("from_account_id"), 
		        			result.getInt("to_account_id"),
		        			result.getInt("tax_account_id"),
		        			OrdererType.valueOf(result.getString("orderer_type")),
		        			UUID.fromString(result.getString("orderer_uuid")),
		        			result.getString("orderer_plugin"),
		        			result.getDouble("amount_to_withdraw"),
		        			result.getDouble("amount_to_deposit"),
		        			result.getDouble("amount_to_tax"),
		        			result.getString("category"),
		        			result.getString("comment"));
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
	
	default ArrayList<ActionLogger> getListIII(AdvancedEconomyPlus plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ACTION.getValue() 
					+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
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
		        			result.getLong("unixtime"),
		        			result.getInt("from_account_id"), 
		        			result.getInt("to_account_id"),
		        			result.getInt("tax_account_id"),
		        			OrdererType.valueOf(result.getString("orderer_type")),
		        			UUID.fromString(result.getString("orderer_uuid")),
		        			result.getString("orderer_plugin"),
		        			result.getDouble("amount_to_withdraw"),
		        			result.getDouble("amount_to_deposit"),
		        			result.getDouble("amount_to_tax"),
		        			result.getString("category"),
		        			result.getString("comment"));
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
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ACTION.getValue() 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<ActionLogger> list = new ArrayList<ActionLogger>();
		        while (result.next()) 
		        {
		        	ActionLogger el = new ActionLogger(
		        			result.getInt("id"),
		        			result.getLong("unixtime"),
		        			result.getInt("from_account_id"), 
		        			result.getInt("to_account_id"),
		        			result.getInt("tax_account_id"),
		        			OrdererType.valueOf(result.getString("orderer_type")),
		        			UUID.fromString(result.getString("orderer_uuid")),
		        			result.getString("orderer_plugin"),
		        			result.getDouble("amount_to_withdraw"),
		        			result.getDouble("amount_to_deposit"),
		        			result.getDouble("amount_to_tax"),
		        			result.getString("category"),
		        			result.getString("comment"));
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
			String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ACTION.getValue()
				+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
				
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
		        			result.getLong("unixtime"),
		        			result.getInt("from_account_id"), 
		        			result.getInt("to_account_id"),
		        			result.getInt("tax_account_id"),
		        			OrdererType.valueOf(result.getString("orderer_type")),
		        			UUID.fromString(result.getString("orderer_uuid")),
		        			result.getString("orderer_plugin"),
		        			result.getDouble("amount_to_withdraw"),
		        			result.getDouble("amount_to_deposit"),
		        			result.getDouble("amount_to_tax"),
		        			result.getString("category"),
		        			result.getString("comment"));
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
	
	default ArrayList<ActionLogger> getAllListAtIIIUnixtimeModified(AdvancedEconomyPlus plugin, String orderByColumn,
			LocalDateTime start, LocalDateTime end, String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ACTION.getValue()
				+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
				
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
		        	LocalDateTime dt = TimeHandler.getLocalDateTime(result.getLong("unixtime"));
		        	if(dt.isAfter(start) && dt.isBefore(end))
		        	{
		        		ActionLogger el = new ActionLogger(
			        			result.getInt("id"),
			        			result.getLong("unixtime"),
			        			result.getInt("from_account_id"), 
			        			result.getInt("to_account_id"),
			        			result.getInt("tax_account_id"),
			        			OrdererType.valueOf(result.getString("orderer_type")),
			        			UUID.fromString(result.getString("orderer_uuid")),
			        			result.getString("orderer_plugin"),
			        			result.getDouble("amount_to_withdraw"),
			        			result.getDouble("amount_to_deposit"),
			        			result.getDouble("amount_to_tax"),
			        			result.getString("category"),
			        			result.getString("comment"));
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.ACTION.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.ACTION.getValue()
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
