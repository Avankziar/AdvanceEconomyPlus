package me.avankziar.aep.bungee.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import me.avankziar.aep.bungee.AEP;
import me.avankziar.aep.general.database.MysqlBaseHandler;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.database.QueryType;

public class MysqlHandler extends MysqlBaseHandler
{
	//FirstKey == ServerName, SecondKey = inserts etc.
	public static LinkedHashMap<String, LinkedHashMap<QueryType, Integer>> serverPerformance = new LinkedHashMap<>();
	
	public MysqlHandler(AEP plugin)
	{
		super(plugin.getLogger(), plugin.getMysqlSetup());
	}
	
	public double getSum(MysqlType type, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		try (Connection conn = AEP.getPlugin().getMysqlSetup().getConnection();)
		{
			String sql = " SELECT sum("+whereColumn+") FROM `"+type.getValue()
					+"` WHERE 1";
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
}