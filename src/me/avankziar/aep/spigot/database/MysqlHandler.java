package me.avankziar.aep.spigot.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import me.avankziar.aep.general.assistance.TimeHandler;
import me.avankziar.aep.general.database.MysqlBaseHandler;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.database.QueryType;
import me.avankziar.aep.general.objects.ActionLogger;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.ifh.general.economy.action.OrdererType;

public class MysqlHandler extends MysqlBaseHandler
{
	//FirstKey == ServerName, SecondKey = inserts etc.
	public static LinkedHashMap<String, LinkedHashMap<QueryType, Integer>> serverPerformance = new LinkedHashMap<>();
	
	public MysqlHandler(AEP plugin)
	{
		super(plugin.getLogger(), plugin.getMysqlSetup());
	}
	
	public double getSumII(MysqlType type, String whichColumn, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		try (Connection conn = AEP.getPlugin().getMysqlSetup().getConnection();)
		{
			String sql = " SELECT sum("+whichColumn+") FROM `"+type.getValue()
					+"` WHERE "+whereColumn;
	        preparedStatement = conn.prepareStatement(sql);
	        int i = 1;
	        for(Object o : whereObject)
	        {
	        	preparedStatement.setObject(i, o);
	        	i++;
	        }
	        
	        result = preparedStatement.executeQuery();
	        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
	        while (result.next()) 
	        {
	        	return result.getDouble(1);
	        }
	    } catch (SQLException e) 
		{
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
		return 0;
	}
	
	public ArrayList<ActionLogger> getAllListAtIIIUnixtimeModified(AEP plugin, String orderByColumn,
			LocalDateTime start, LocalDateTime end, String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		try (Connection conn = plugin.getMysqlSetup().getConnection();)
		{
			String sql = "SELECT * FROM `" + MysqlType.ACTION.getValue()
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
		        			result.getString("orderer_uuid") != null ? UUID.fromString(result.getString("orderer_uuid")) : null,
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
			  plugin.getLogger().warning("Error: " + e.getMessage());
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
		return null;
	}
	
	public ArrayList<Object[]> getTop10Balance(String currency)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		try (Connection conn = AEP.getPlugin().getMysqlSetup().getConnection();)
		{
			String sql = "SELECT `owner_uuid`, SUM(`balance`) AS total_balance"
					+ "FROM `aepaccount`"
					+ "WHERE `account_currency` = ? AND `owner_type` = ? AND (`account_category` != ? OR `account_category` != ?)"
					+ "GROUP BY `owner_uuid`"
					+ "ORDER BY total_balance DESC"
					+ "LIMIT 10;";
	        preparedStatement = conn.prepareStatement(sql);
	        preparedStatement.setObject(1, currency);
	        preparedStatement.setObject(2, "PLAYER");
	        preparedStatement.setObject(3, "TAX");
	        preparedStatement.setObject(4, "VOID");
	        
	        result = preparedStatement.executeQuery();
	        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
	        ArrayList<Object[]> l = new ArrayList<>();
	        while (result.next()) 
	        {
	        	String uuid = result.getString("owner_uuid");
	        	double tb = result.getDouble("total_balance");
	        	Object[] o = new Object[] {uuid, tb};
	        	l.add(o);
	        }
	        return l;
	    } catch (SQLException e) 
		{
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
		return new ArrayList<>();
	}
}