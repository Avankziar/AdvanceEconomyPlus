package main.java.me.avankziar.aep.spigot.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.tables.OLDTableI;
import main.java.me.avankziar.aep.spigot.database.tables.TableIII;
import main.java.me.avankziar.aep.spigot.database.tables.TableIV;
import main.java.me.avankziar.aep.spigot.database.tables.TableV;
import main.java.me.avankziar.aep.spigot.database.tables.TableVI;
import main.java.me.avankziar.aep.spigot.database.tables.TableVII;

public class MysqlHandler implements OLDTableI, TableIII, TableIV, TableV, TableVI, TableVII
{
	public enum Type
	{
		PLAYER("economyPlayerData"),
		ACTION("aepActionLogger"),
		TREND("aepTrendLogger"),
		STANDINGORDER("economyStandingOrder"),
		LOAN("economyLoan"),
		LOGGERSETTINGSPRESET("economyLoggerSettingsPreset"),
		//NEw stuff
		ENTITYDATA("aepEntityData"),
		PLAYERDATA("aepPlayerData"),
		ACCOUNT("aepAccount"),
		DEFAULTACCOUNT("aepDefaultAccount"),
		ACCOUNTMANAGEMENT("aepAccountManagement");
		
		private Type(String value)
		{
			this.value = value;
		}
		
		private final String value;

		public String getValue()
		{
			return value;
		}
	}
	
	public enum QueryType
	{
		INSERT, UPDATE, DELETE, READ;
	}
	
	/*
	 * Alle Mysql Reihen, welche durch den Betrieb aufkommen.
	 */
	public static long startRecordTime = System.currentTimeMillis();
	public static int inserts = 0;
	public static int updates = 0;
	public static int deletes = 0;
	public static int reads = 0;
	
	public static void addRows(QueryType type, int amount)
	{
		switch(type)
		{
		case DELETE:
			deletes += amount;
			break;
		case INSERT:
			inserts += amount;
		case READ:
			reads += amount;
			break;
		case UPDATE:
			updates += amount;
			break;
		}
	}
	
	public static void resetsRows()
	{
		inserts = 0;
		updates = 0;
		reads = 0;
		deletes = 0;
	}
	
	private AdvancedEconomyPlus plugin;
	
	public MysqlHandler(AdvancedEconomyPlus plugin) 
	{
		this.plugin = plugin;
	}
	
	public boolean exist(Type type, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + type.getValue()
						+ "` WHERE "+whereColumn+" LIMIT 1";
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
	
	public boolean create(Type type, Object object)
	{
		switch(type)
		{
		case PLAYER:
			return OLDTableI.super.createI(plugin, object);
		case ACTION:
			return TableIII.super.createIII(plugin, object);
		case TREND:
			return TableIV.super.createIV(plugin, object);
		case STANDINGORDER:
			return TableV.super.createV(plugin, object);
		case LOAN:
			return TableVI.super.createVI(plugin, object);
		case LOGGERSETTINGSPRESET:
			return TableVII.super.createVII(plugin, object);
		}
		return false;
	}
	
	public boolean updateData(Type type, Object object, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case PLAYER:
			return OLDTableI.super.updateDataI(plugin, object, whereColumn, whereObject);
		case ACTION:
			return TableIII.super.updateDataIII(plugin, object, whereColumn, whereObject);
		case TREND:
			return TableIV.super.updateDataIV(plugin, object, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.updateDataV(plugin, object, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.updateDataVI(plugin, object, whereColumn, whereObject);
		case LOGGERSETTINGSPRESET:
			return TableVII.super.updateDataVII(plugin, object, whereColumn, whereObject);
		}
		return false;
	}
	
	public Object getData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case PLAYER:
			return OLDTableI.super.getDataI(plugin, whereColumn, whereObject);
		case ACTION:
			return TableIII.super.getDataIII(plugin, whereColumn, whereObject);
		case TREND:
			return TableIV.super.getDataIV(plugin, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.getDataV(plugin, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.getDataVI(plugin, whereColumn, whereObject);
		case LOGGERSETTINGSPRESET:
			return TableVII.super.getDataVII(plugin, whereColumn, whereObject);
		}
		return null;
	}
	
	public int deleteData(Type type, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + type.getValue() + "` WHERE "+whereColumn;
			preparedStatement = conn.prepareStatement(sql);
			int i = 1;
	        for(Object o : whereObject)
	        {
	        	preparedStatement.setObject(i, o);
	        	i++;
	        }
	        int d = preparedStatement.executeUpdate();
			MysqlHandler.addRows(QueryType.DELETE, d);
			return d;
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
		return 0;
	}
	
	public int lastID(Type type)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + type.getValue() + "` ORDER BY `id` DESC LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
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
	
	public int countWhereID(Type type, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + type.getValue()
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
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
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
	
	public int getCount(Type type, String orderByColumn, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = " SELECT count(*) FROM `"+type.getValue()
						+"` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
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
		        	return result.getInt(1);
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
		}
		return 0;
	}
	
	public ArrayList<?> getList(Type type, String orderByColumn, int start, int quantity, String whereColumn, Object...whereObject)
	{
		switch(type)
		{
		case PLAYER:
			return OLDTableI.super.getListI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case ACTION:
			return TableIII.super.getListIII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case TREND:
			return TableIV.super.getListIV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.getListV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.getListVI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case LOGGERSETTINGSPRESET:
			return TableVII.super.getListVII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		}
		return null;
	}
	
	public ArrayList<?> getTop(Type type, String orderByColumn, int start, int end)
	{
		switch(type)
		{
		case PLAYER:
			return OLDTableI.super.getTopI(plugin, orderByColumn, start, end);
		case ACTION:
			return TableIII.super.getTopIII(plugin, orderByColumn, start, end);
		case TREND:
			return TableIV.super.getTopIV(plugin, orderByColumn, start, end);
		case STANDINGORDER:
			return TableV.super.getTopV(plugin, orderByColumn, start, end);
		case LOAN:
			return TableVI.super.getTopVI(plugin, orderByColumn, start, end);
		case LOGGERSETTINGSPRESET:
			return TableVII.super.getTopVII(plugin, orderByColumn, start, end);
		}
		return null;
	}
	
	public ArrayList<?> getAllListAt(Type type, String orderByColumn,
			String whereColumn, Object...whereObject) throws IOException
	{
		switch(type)
		{
		case PLAYER:
			return OLDTableI.super.getAllListAtI(plugin, orderByColumn, whereColumn, whereObject);
		case ACTION:
			return TableIII.super.getAllListAtIII(plugin, orderByColumn, whereColumn, whereObject);
		case TREND:
			return TableIV.super.getAllListAtIV(plugin, orderByColumn, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.getAllListAtV(plugin, orderByColumn, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.getAllListAtVI(plugin, orderByColumn, whereColumn, whereObject);
		case LOGGERSETTINGSPRESET:
			return TableVII.super.getAllListAtVII(plugin, orderByColumn, whereColumn, whereObject);
		}
		return null;
	}
}
