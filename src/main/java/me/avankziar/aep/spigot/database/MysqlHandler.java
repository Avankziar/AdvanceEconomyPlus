package main.java.me.avankziar.aep.spigot.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.tables.OLDTableI;
import main.java.me.avankziar.aep.spigot.database.tables.Table00;
import main.java.me.avankziar.aep.spigot.database.tables.Table01;
import main.java.me.avankziar.aep.spigot.database.tables.Table02;
import main.java.me.avankziar.aep.spigot.database.tables.Table03;
import main.java.me.avankziar.aep.spigot.database.tables.Table04;
import main.java.me.avankziar.aep.spigot.database.tables.Table05;
import main.java.me.avankziar.aep.spigot.database.tables.Table06;
import main.java.me.avankziar.aep.spigot.database.tables.Table07;
import main.java.me.avankziar.aep.spigot.database.tables.Table08;
import main.java.me.avankziar.aep.spigot.database.tables.Table09;
import main.java.me.avankziar.aep.spigot.database.tables.Table10;

public class MysqlHandler implements OLDTableI, Table00, Table01, Table02, Table03, Table04, Table05, Table06, Table07, Table08, Table09, Table10
{
	public enum Type
	{
		OLDPLAYER("economyPlayerData"),
		ACTION("aepActionLogger"),
		TREND("aepTrendLogger"),
		STANDINGORDER("aepStandingOrder"),
		LOAN("aepLoan"),
		LOGGERSETTINGSPRESET("aepLoggerSettingsPreset"),
		//NEw stuff
		ENTITYDATA("aepEntityData"),
		PLAYERDATA("aepPlayerData"),
		ACCOUNT("aepAccount"),
		DEFAULTACCOUNT("aepDefaultAccount"),
		ACCOUNTMANAGEMENT("aepAccountManagement"),
		QUICKPAYACCOUNT("aepQuickPayAccount");
		
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
		case OLDPLAYER:
			return OLDTableI.super.createIOLD(plugin, object);
		case ENTITYDATA:
			return Table00.super.create0(plugin, object);
		case PLAYERDATA:
			return Table01.super.createI(plugin, object);
		case ACCOUNT:
			return Table02.super.createII(plugin, object);
		case ACTION:
			return Table03.super.createIII(plugin, object);
		case TREND:
			return Table04.super.createIV(plugin, object);
		case STANDINGORDER:
			return Table05.super.createV(plugin, object);
		case LOAN:
			return Table06.super.createVI(plugin, object);
		case LOGGERSETTINGSPRESET:
			return Table07.super.createVII(plugin, object);
		case DEFAULTACCOUNT:
			return Table08.super.createVIII(plugin, object);
		case ACCOUNTMANAGEMENT:
			return Table09.super.createIX(plugin, object);
		case QUICKPAYACCOUNT:
			return Table10.super.createX(plugin, object);
		}
		return false;
	}
	
	public boolean updateData(Type type, Object object, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case OLDPLAYER:
			return OLDTableI.super.updateDataIOLD(plugin, object, whereColumn, whereObject);
		case ENTITYDATA:
			return Table00.super.updateData0(plugin, object, whereColumn, whereObject);
		case PLAYERDATA:
			return Table01.super.updateDataI(plugin, object, whereColumn, whereObject);
		case ACCOUNT:
			return Table02.super.updateDataII(plugin, object, whereColumn, whereObject);
		case ACTION:
			return Table03.super.updateDataIII(plugin, object, whereColumn, whereObject);
		case TREND:
			return Table04.super.updateDataIV(plugin, object, whereColumn, whereObject);
		case STANDINGORDER:
			return Table05.super.updateDataV(plugin, object, whereColumn, whereObject);
		case LOAN:
			return Table06.super.updateDataVI(plugin, object, whereColumn, whereObject);
		case LOGGERSETTINGSPRESET:
			return Table07.super.updateDataVII(plugin, object, whereColumn, whereObject);
		case DEFAULTACCOUNT:
			return Table08.super.updateDataVIII(plugin, object, whereColumn, whereObject);
		case ACCOUNTMANAGEMENT:
			return Table09.super.updateDataIX(plugin, object, whereColumn, whereObject);
		case QUICKPAYACCOUNT:
			return Table10.super.updateDataX(plugin, object, whereColumn, whereObject);
		}
		return false;
	}
	
	public Object getData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case OLDPLAYER:
			return OLDTableI.super.getDataIOLD(plugin, whereColumn, whereObject);
		case ENTITYDATA:
			return Table00.super.getData0(plugin, whereColumn, whereObject);
		case PLAYERDATA:
			return Table01.super.getDataI(plugin, whereColumn, whereObject);
		case ACCOUNT:
			return Table02.super.getDataII(plugin, whereColumn, whereObject);
		case ACTION:
			return Table03.super.getDataIII(plugin, whereColumn, whereObject);
		case TREND:
			return Table04.super.getDataIV(plugin, whereColumn, whereObject);
		case STANDINGORDER:
			return Table05.super.getDataV(plugin, whereColumn, whereObject);
		case LOAN:
			return Table06.super.getDataVI(plugin, whereColumn, whereObject);
		case LOGGERSETTINGSPRESET:
			return Table07.super.getDataVII(plugin, whereColumn, whereObject);
		case DEFAULTACCOUNT:
			return Table08.super.getDataVIII(plugin, whereColumn, whereObject);
		case ACCOUNTMANAGEMENT:
			return Table09.super.getDataIX(plugin, whereColumn, whereObject);
		case QUICKPAYACCOUNT:
			return Table10.super.getDataX(plugin, whereColumn, whereObject);
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
	
	public int getCount(Type type, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = " SELECT count(*) FROM `"+type.getValue()
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
	
	public double getSum(Type type, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
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
		}
		return 0;
	}
	
	public double getSumII(Type type, String whichColumn, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
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
		}
		return 0;
	}
	
	public ArrayList<?> getList(Type type, String orderByColumn, int start, int quantity, String whereColumn, Object...whereObject)
	{
		switch(type)
		{
		case OLDPLAYER:
			return OLDTableI.super.getListIOLD(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case ENTITYDATA:
			return Table00.super.getList0(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case PLAYERDATA:
			return Table01.super.getListI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case ACCOUNT:
			return Table02.super.getListII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case ACTION:
			return Table03.super.getListIII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case TREND:
			return Table04.super.getListIV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case STANDINGORDER:
			return Table05.super.getListV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case LOAN:
			return Table06.super.getListVI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case LOGGERSETTINGSPRESET:
			return Table07.super.getListVII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case DEFAULTACCOUNT:
			return Table08.super.getListVIII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case ACCOUNTMANAGEMENT:
			return Table09.super.getListIX(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case QUICKPAYACCOUNT:
			return Table10.super.getListX(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		}
		return null;
	}
	
	public ArrayList<?> getTop(Type type, String orderByColumn, int start, int end)
	{
		switch(type)
		{
		case OLDPLAYER:
			return OLDTableI.super.getTopIOLD(plugin, orderByColumn, start, end);
		case ENTITYDATA:
			return Table00.super.getTop0(plugin, orderByColumn, start, end);
		case PLAYERDATA:
			return Table01.super.getTopI(plugin, orderByColumn, start, end);
		case ACCOUNT:
			return Table02.super.getTopII(plugin, orderByColumn, start, end);
		case ACTION:
			return Table03.super.getTopIII(plugin, orderByColumn, start, end);
		case TREND:
			return Table04.super.getTopIV(plugin, orderByColumn, start, end);
		case STANDINGORDER:
			return Table05.super.getTopV(plugin, orderByColumn, start, end);
		case LOAN:
			return Table06.super.getTopVI(plugin, orderByColumn, start, end);
		case LOGGERSETTINGSPRESET:
			return Table07.super.getTopVII(plugin, orderByColumn, start, end);
		case DEFAULTACCOUNT:
			return Table08.super.getTopVIII(plugin, orderByColumn, start, end);
		case ACCOUNTMANAGEMENT:
			return Table09.super.getTopIX(plugin, orderByColumn, start, end);
		case QUICKPAYACCOUNT:
			return Table10.super.getTopX(plugin, orderByColumn, start, end);
		}
		return null;
	}
	
	public ArrayList<?> getAllListAt(Type type, String orderByColumn,
			String whereColumn, Object...whereObject) throws IOException
	{
		switch(type)
		{
		case OLDPLAYER:
			return OLDTableI.super.getAllListAtIOLD(plugin, orderByColumn, whereColumn, whereObject);
		case ENTITYDATA:
			return Table00.super.getAllListAt0(plugin, orderByColumn, whereColumn, whereObject);
		case PLAYERDATA:
			return Table01.super.getAllListAtI(plugin, orderByColumn, whereColumn, whereObject);
		case ACCOUNT:
			return Table02.super.getAllListAtII(plugin, orderByColumn, whereColumn, whereObject);
		case ACTION:
			return Table03.super.getAllListAtIII(plugin, orderByColumn, whereColumn, whereObject);
		case TREND:
			return Table04.super.getAllListAtIV(plugin, orderByColumn, whereColumn, whereObject);
		case STANDINGORDER:
			return Table05.super.getAllListAtV(plugin, orderByColumn, whereColumn, whereObject);
		case LOAN:
			return Table06.super.getAllListAtVI(plugin, orderByColumn, whereColumn, whereObject);
		case LOGGERSETTINGSPRESET:
			return Table07.super.getAllListAtVII(plugin, orderByColumn, whereColumn, whereObject);
		case DEFAULTACCOUNT:
			return Table08.super.getAllListAtVIII(plugin, orderByColumn, whereColumn, whereObject);
		case ACCOUNTMANAGEMENT:
			return Table09.super.getAllListAtIX(plugin, orderByColumn, whereColumn, whereObject);
		case QUICKPAYACCOUNT:
			return Table10.super.getAllListAtX(plugin, orderByColumn, whereColumn, whereObject);
		}
		return null;
	}
}
