package main.java.me.avankziar.aep.spigot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;

public class MysqlSetup 
{
	private AdvancedEconomyPlus plugin;
	private Connection conn = null;
	
	public MysqlSetup(AdvancedEconomyPlus plugin) 
	{
		this.plugin = plugin;
		loadMysqlSetup();
	}
	
	/*
	 * Convert für EconomyPlus 
	 * INSERT INTO `economyPlayerData`(`player_uuid`, `player_name`, `balance`) SELECT `player`, 'default', `moneys` FROM `economy`;
	 */
	
	public boolean loadMysqlSetup()
	{
		if(!connectToDatabase())
		{
			return false;
		}
		if(!setupOLD_DatabaseI())
		{
			return false;
		}
		if(!setupDatabaseI())
		{
			return false;
		}
		if(!setupDatabaseIII())
		{
			return false;
		}
		if(!setupDatabaseIV())
		{
			return false;
		}
		if(!setupDatabaseV())
		{
			return false;
		}
		if(!setupDatabaseVI())
		{
			return false;
		}
		if(!setupDatabaseVII())
		{
			return false;
		}
		if(!setupDatabaseVIII())
		{
			return false;
		}
		if(!setupDatabaseIX())
		{
			return false;
		}
		if(!setupDatabaseX())
		{
			return false;
		}
		return true;
	}
	
	public boolean connectToDatabase() 
	{
		AdvancedEconomyPlus.log.info("Connecting to the database...");
		boolean bool = false;
	    try
	    {
	    	// Load new Drivers for papermc
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	    	bool = true;
	    } catch (Exception e)
	    {
	    	bool = false;
	    } 
	    try
	    {
	    	if (bool == false)
	    	{
	    		// Load old Drivers for spigot
	    		Class.forName("com.mysql.jdbc.Driver");
	    	}
	        Properties properties = new Properties();
            properties.setProperty("user", plugin.getYamlHandler().getConfig().getString("Mysql.User"));
            properties.setProperty("password", plugin.getYamlHandler().getConfig().getString("Mysql.Password"));
            properties.setProperty("autoReconnect", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.AutoReconnect", true) + "");
            properties.setProperty("verifyServerCertificate", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.VerifyServerCertificate", false) + "");
            properties.setProperty("useSSL", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false) + "");
            properties.setProperty("requireSSL", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false) + "");
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + plugin.getYamlHandler().getConfig().getString("Mysql.Host") 
            		+ ":" + plugin.getYamlHandler().getConfig().getInt("Mysql.Port", 3306) + "/" 
            		+ plugin.getYamlHandler().getConfig().getString("Mysql.DatabaseName"), properties);
           
          } catch (ClassNotFoundException e) 
		{
        	  AdvancedEconomyPlus.log.severe("Could not locate drivers for mysql! Error: " + e.getMessage());
            return false;
          } catch (SQLException e) 
		{
        	  AdvancedEconomyPlus.log.severe("Could not connect to mysql database! Error: " + e.getMessage());
            return false;
          }
		AdvancedEconomyPlus.log.info("Database connection successful!");
		return true;
	}
	
	@Deprecated
	public boolean setupOLD_DatabaseI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.OLDPLAYER.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " player_uuid char(36) NOT NULL UNIQUE,"
        		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
        		+ " balance double DEFAULT '0.00',"
        		+ " bankaccountlist mediumtext,"
        		+ " moneyplayerflow boolean DEFAULT '1',"
        		+ " moneybankflow boolean DEFAULT '1',"
        		+ " generalmessage boolean DEFAULT '1',"
        		+ " pendinginvite text DEFAULT NULL,"
        		+ " frozen boolean DEFAULT '1'"
        		+ ");";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabase0() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.ENTITYDATA.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " entity_uuid char(36) NOT NULL UNIQUE,"
        		+ " entity_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
        		+ " entity_type text"
        		+ ");";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.PLAYERDATA.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " player_uuid char(36) NOT NULL UNIQUE,"
        		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
        		+ " wallet_moneyflow_notification boolean,"
        		+ " bank_moneyflow_notification boolean,"
        		+ " unixtime bigint"
        		+ ");";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.ACCOUNT.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " account_name text,"
        		+ " account_type text,"
        		+ " account_category text,"
        		+ " account_currency text,"
        		+ " account_predefined boolean,"
        		+ " owner_uuid char(36) NOT NULL,"
        		+ " owner_type text,"
        		+ " owner_name text,"
        		+ " balance double"
        		+ " );";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.ACTION.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " unixtime bigint,"
        		+ " from_account_id int,"
        		+ " to_account_id int,"
        		+ " tax_account_id int,"
        		+ " orderer_type text,"
        		+ " orderer_uuid text,"
        		+ " orderer_plugin text,"
        		+ " amount_to_withdraw double,"
        		+ " amount_to_deposit double,"
        		+ " amount_to_tax double,"
        		+ " category text,"
        		+ " comment mediumtext"
        		+ ");";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseIV() 
	{
		 String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.TREND.getValue()
	        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
	        		+ " dates bigint,"
	        		+ " trend_type text,"
	        		+ " account_id text,"
	        		+ " relative_amount_change double,"
	        		+ " firstvalue double,"
	        		+ " lastvalue double"
	        		+ ");";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseV() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.STANDINGORDER.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " standing_order_name text,"
        		+ " owner_uuid text,"
        		+ " from_account int,"
        		+ " to_account int,"
        		+ " amount double,"
        		+ " amount_paid_so_far double,"
        		+ " amount_paid_to_tax double,"
        		+ " start_time bigint,"
        		+ " repeating_time bigint,"
        		+ " last_time bigint,"
        		+ " cancelled boolean,"
        		+ " paused boolean"
        		+ ");";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseVI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.LOAN.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " name text,"
        		+ " from_account int,"
        		+ " to_account int,"
        		+ " loan_owner text,"
        		+ " debtor text,"
        		+ " total_amount double,"
        		+ " amount_ratio double,"
        		+ " amount_paid_so_far double,"
        		+ " interest double,"
        		+ " start_time bigint,"
        		+ " repeating_time bigint,"
        		+ " last_time bigint,"
        		+ " end_time bigint,"
        		+ " forgiven boolean,"
        		+ " paused boolean,"
        		+ " finished boolean"
        		+ ");";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseVII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.LOGGERSETTINGSPRESET.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " slotid int,"
        		+ " player_uuid char(36),"
        		+ " banknumber text," //FIXME
        		+ " isaction boolean,"
        		+ " inventoryhandlertype text,"
        		+ " isdescending boolean,"
        		+ " ordertype text,"
        		+ " minimum double,"
        		+ " maximum double,"
        		+ " from_value text,"
        		+ " to_value text,"
        		+ " orderer text,"
        		+ " comment text,"
        		+ " firststand double,"
        		+ " laststand double"
        		+ ");";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseVIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.DEFAULTACCOUNT.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " player_uuid char(36) NOT NULL,"
        		+ " account_id int,"
        		+ " account_currency text,"
        		+ " account_category text,"
        		+ " );";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseIX() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.ACCOUNTMANAGEMENT.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " player_uuid char(36) NOT NULL,"
        		+ " account_id int,"
        		+ " account_management_type text"
        		+ " );";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseX() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.QUICKPAYACCOUNT.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " player_uuid char(36) NOT NULL,"
        		+ " account_id int,"
        		+ " account_currency text"
        		+ " );";
		baseSetup(data);
		return true;
	}
	
	private boolean baseSetup(String data) 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {
		    	  query = conn.prepareStatement(data);
		    	  query.execute();
		      } catch (SQLException e) 
		      {
		    	  e.printStackTrace();
		    	  AdvancedEconomyPlus.log.severe("Error creating tables! Error: " + e.getMessage());
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
				AdvancedEconomyPlus.log.warning("Connection failed. Reconnecting...");
				reConnect();
			}
			if (!conn.isValid(3))
			{
				AdvancedEconomyPlus.log.warning("Connection is idle or terminated. Reconnecting...");
				reConnect();
			}
			if (conn.isClosed() == true) 
			{
				AdvancedEconomyPlus.log.warning("Connection is closed. Reconnecting...");
				reConnect();
			}
		} catch (Exception e) 
		{
			AdvancedEconomyPlus.log.severe("Could not reconnect to Database! Error: " + e.getMessage());
		}
	}
	
	public boolean reConnect() 
	{
		boolean bool = false;
	    try
	    {
	    	// Load new Drivers for papermc
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	    	bool = true;
	    } catch (Exception e)
	    {
	    	bool = false;
	    } 
	    try
	    {
	    	if (bool == false)
	    	{
	    		// Load old Drivers for spigot
	    		Class.forName("com.mysql.jdbc.Driver");
	    	}           
            long start = 0;
			long end = 0;
			
		    start = System.currentTimeMillis();
		    AdvancedEconomyPlus.log.info("Attempting to establish a connection to the MySQL server!");
            Properties properties = new Properties();
            properties.setProperty("user", plugin.getYamlHandler().getConfig().getString("Mysql.User"));
            properties.setProperty("password", plugin.getYamlHandler().getConfig().getString("Mysql.Password"));
            properties.setProperty("autoReconnect", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.AutoReconnect", true) + "");
            properties.setProperty("verifyServerCertificate", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.VerifyServerCertificate", false) + "");
            properties.setProperty("useSSL", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false) + "");
            properties.setProperty("requireSSL", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false) + "");
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + plugin.getYamlHandler().getConfig().getString("Mysql.Host") 
            		+ ":" + plugin.getYamlHandler().getConfig().getInt("Mysql.Port", 3306) + "/" 
            		+ plugin.getYamlHandler().getConfig().getString("Mysql.DatabaseName"), properties);
		    end = System.currentTimeMillis();
		    AdvancedEconomyPlus.log.info("Connection to MySQL server established!");
		    AdvancedEconomyPlus.log.info("Connection took " + ((end - start)) + "ms!");
            return true;
		} catch (Exception e) 
		{
			AdvancedEconomyPlus.log.severe("Error re-connecting to the database! Error: " + e.getMessage());
			return false;
		}
	}
	
	public void closeConnection() 
	{
		try
		{
			AdvancedEconomyPlus.log.info("Closing database connection...");
			conn.close();
			conn = null;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}
