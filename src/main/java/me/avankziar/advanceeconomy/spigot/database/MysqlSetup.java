package main.java.me.avankziar.advanceeconomy.spigot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;

public class MysqlSetup 
{
	private AdvanceEconomy plugin;
	private Connection conn = null;
	
	public MysqlSetup(AdvanceEconomy plugin) 
	{
		this.plugin = plugin;
		loadMysqlSetup();
	}
	
	
	public boolean loadMysqlSetup()
	{
		if(!connectToDatabase())
		{
			return false;
		}
		if(!setupDatabaseI())
		{
			return false;
		}
		//Disabled until bank accounts are fully implemented
		/*if(!setupDatabaseII())
		{
			return false;
		}*/
		if(!setupDatabaseIII())
		{
			return false;
		}
		if(!setupDatabaseIV())
		{
			return false;
		}
		return true;
	}
	
	public boolean connectToDatabase() 
	{
		AdvanceEconomy.log.info("Connecting to the database...");
		try 
		{
       	 	//Load Drivers
            Class.forName("com.mysql.jdbc.Driver");
            Properties properties = new Properties();
            properties.setProperty("user", plugin.getYamlHandler().get().getString("Mysql.User"));
            properties.setProperty("password", plugin.getYamlHandler().get().getString("Mysql.Password"));
            properties.setProperty("autoReconnect", 
            		plugin.getYamlHandler().get().getBoolean("Mysql.AutoReconnect", true) + "");
            properties.setProperty("verifyServerCertificate", 
            		plugin.getYamlHandler().get().getBoolean("Mysql.VerifyServerCertificate", false) + "");
            properties.setProperty("useSSL", 
            		plugin.getYamlHandler().get().getBoolean("Mysql.SSLEnabled", false) + "");
            properties.setProperty("requireSSL", 
            		plugin.getYamlHandler().get().getBoolean("Mysql.SSLEnabled", false) + "");
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + plugin.getYamlHandler().get().getString("Mysql.Host") 
            		+ ":" + plugin.getYamlHandler().get().getInt("Mysql.Port", 3306) + "/" 
            		+ plugin.getYamlHandler().get().getString("Mysql.DatabaseName"), properties);
           
          } catch (ClassNotFoundException e) 
		{
        	  AdvanceEconomy.log.severe("Could not locate drivers for mysql! Error: " + e.getMessage());
            return false;
          } catch (SQLException e) 
		{
        	  AdvanceEconomy.log.severe("Could not connect to mysql database! Error: " + e.getMessage());
            return false;
          }
		AdvanceEconomy.log.info("Database connection successful!");
		return true;
	}
	
	public boolean setupDatabaseI() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameI
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		        		+ " player_uuid char(36) NOT NULL UNIQUE,"
		        		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
		        		+ " balance double DEFAULT '0.00',"
		        		+ " bankaccountlist mediumtext,"
		        		+ " moneyplayerflow boolean DEFAULT '1',"
		        		+ " moneybankflow boolean DEFAULT '1',"
		        		+ " generalmessage boolean DEFAULT '1',"
		        		+ " pendinginvite text DEFAULT NULL,"
		        		+ " frozen boolean DEFAULT '1');";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        AdvanceEconomy.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public boolean setupDatabaseII() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameII
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		        		+ " bank_name text,"
		        		+ " accountnumber text,"
		        		+ " balance double,"
		        		+ " owner_uuid char(36) NOT NULL,"
		        		+ " vice_uuid mediumtext,"
		        		+ " member_uuid mediumtext);";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        AdvanceEconomy.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public boolean setupDatabaseIII() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameIII
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		        		+ " datetime text,"
		        		+ " from_uuidornumber text,"
		        		+ " from_name text,"
		        		+ " to_uuidornumber text,"
		        		+ " to_name text,"
		        		+ " orderer_uuid text,"
		        		+ " amount double,"
		        		+ " eco_type text,"
		        		+ " comment mediumtext);";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        AdvanceEconomy.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public boolean setupDatabaseIV() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameIV
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		        		+ " dates text,"
		        		+ " trend_type text,"
		        		+ " uuidornumber text,"
		        		+ " relative_amount_change double,"
		        		+ " firstvalue double,"
		        		+ " lastvalue double);";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        AdvanceEconomy.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public Connection getConnection() 
	{
		checkConnection();
		return conn;
	}
	
	public void checkConnection() 
	{
		try {
			if (conn == null) 
			{
				AdvanceEconomy.log.warning("Connection failed. Reconnecting...");
				reConnect();
			}
			if (!conn.isValid(3)) 
			{
				AdvanceEconomy.log.warning("Connection is idle or terminated. Reconnecting...");
				reConnect();
			}
			if (conn.isClosed() == true) 
			{
				AdvanceEconomy.log.warning("Connection is closed. Reconnecting...");
				reConnect();
			}
		} catch (Exception e) 
		{
			AdvanceEconomy.log.severe("Could not reconnect to Database! Error: " + e.getMessage());
		}
	}
	
	public boolean reConnect() 
	{
		try 
		{            
            long start = 0;
			long end = 0;
			
		    start = System.currentTimeMillis();
		    AdvanceEconomy.log.info("Attempting to establish a connection to the MySQL server!");
            Class.forName("com.mysql.jdbc.Driver");
            Properties properties = new Properties();
            properties.setProperty("user", plugin.getYamlHandler().get().getString("Mysql.User"));
            properties.setProperty("password", plugin.getYamlHandler().get().getString("Mysql.Password"));
            properties.setProperty("autoReconnect", 
            		plugin.getYamlHandler().get().getBoolean("Mysql.AutoReconnect", true) + "");
            properties.setProperty("verifyServerCertificate", 
            		plugin.getYamlHandler().get().getBoolean("Mysql.VerifyServerCertificate", false) + "");
            properties.setProperty("useSSL", 
            		plugin.getYamlHandler().get().getBoolean("Mysql.SSLEnabled", false) + "");
            properties.setProperty("requireSSL", 
            		plugin.getYamlHandler().get().getBoolean("Mysql.SSLEnabled", false) + "");
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + plugin.getYamlHandler().get().getString("Mysql.Host") 
            		+ ":" + plugin.getYamlHandler().get().getInt("Mysql.Port", 3306) + "/" 
            		+ plugin.getYamlHandler().get().getString("Mysql.DatabaseName"), properties);
		    end = System.currentTimeMillis();
		    AdvanceEconomy.log.info("Connection to MySQL server established!");
		    AdvanceEconomy.log.info("Connection took " + ((end - start)) + "ms!");
            return true;
		} catch (Exception e) 
		{
			AdvanceEconomy.log.severe("Error re-connecting to the database! Error: " + e.getMessage());
			return false;
		}
	}
	
	public void closeConnection() 
	{
		try
		{
			AdvanceEconomy.log.info("Closing database connection...");
			conn.close();
			conn = null;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}
