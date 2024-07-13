package main.java.me.avankziar.aep.spigot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;

public class MysqlSetup 
{
	private Connection conn = null;
	final private String host;
	final private int port;
	final private String database;
	final private String user;
	final private String password;
	final private boolean isAutoConnect;
	final private boolean isVerifyServerCertificate;
	final private boolean isSSLEnabled;
	
	public MysqlSetup(AdvancedEconomyPlus plugin, boolean adm, String path) 
	{
		if(adm)
		{
			AdvancedEconomyPlus.log.log(Level.INFO, "Using IFH Administration");
		}
		host = adm ? plugin.getAdministration().getHost(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Host");
		port = adm ? plugin.getAdministration().getPort(path)
				: plugin.getYamlHandler().getConfig().getInt("Mysql.Port", 3306);
		database = adm ? plugin.getAdministration().getDatabase(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.DatabaseName");
		user = adm ? plugin.getAdministration().getUsername(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.User");
		password = adm ? plugin.getAdministration().getPassword(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Password");
		isAutoConnect = adm ? plugin.getAdministration().isAutoReconnect(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.AutoReconnect", true);
		isVerifyServerCertificate = adm ? plugin.getAdministration().isVerifyServerCertificate(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.VerifyServerCertificate", false);
		isSSLEnabled = adm ? plugin.getAdministration().useSSL(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false);
		loadMysqlSetup();
	}
	
	public boolean connectToDatabase() 
	{
		AdvancedEconomyPlus.log.info("Connecting to the database...");
		Connection conn = getConnection();
		if(conn != null)
		{
			AdvancedEconomyPlus.log.info("Database connection successful!");
		} else
		{
			return false;
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
		try 
		{
			if (conn == null) 
			{
				reConnect();
			}
			if (!conn.isValid(3)) 
			{
				reConnect();
			}
			if (conn.isClosed() == true) 
			{
				reConnect();
			}
		} catch (Exception e) 
		{
			AdvancedEconomyPlus.log.severe("Could not (re)connect to Database! Error: " + e.getMessage());
		}
	}
	
	private Connection reConnect() 
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
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("autoReconnect", String.valueOf(isAutoConnect));
            properties.setProperty("verifyServerCertificate", String.valueOf(isVerifyServerCertificate));
            properties.setProperty("useSSL", String.valueOf(isSSLEnabled));
            properties.setProperty("requireSSL", String.valueOf(isSSLEnabled));
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, properties);
            return conn;
		} catch (Exception e) 
		{
			AdvancedEconomyPlus.log.severe("Error (re-)connecting to the database! Error: " + e.getMessage());
			return null;
		}
	}
	
	public void closeConnection() 
	{
		if(conn == null)
		{
			return;
		}
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
	
	private boolean baseSetup(String data) 
	{
		try (Connection conn = getConnection(); PreparedStatement query = conn.prepareStatement(data))
		{
			query.execute();
		} catch (SQLException e) 
		{
			AdvancedEconomyPlus.log.log(Level.WARNING, "Could not build data source. Or connection is null", e);
		}
		return true;
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
		if(!setupDatabase0())
		{
			return false;
		}
		if(!setupDatabaseI())
		{
			return false;
		}
		if(!setupDatabaseII())
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
        		+ " owner_uuid text NOT NULL,"
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
        		+ " end_time bigint,"
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
        		+ " loan_amount double,"
        		+ " amount_ratio double,"
        		+ " tax_in_decimal double,"
        		+ " amount_paid_so_far double,"
        		+ " amount_paid_to_tax double,"
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
        		+ " player_uuid text,"
        		+ " account_id int,"
        		+ " isaction boolean,"
        		+ " inventoryhandlertype text,"
        		+ " isdescending boolean,"
        		+ " ordertype text,"
        		+ " minimum double,"
        		+ " maximum double,"
        		+ " category text,"
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
        		+ " player_uuid text NOT NULL,"
        		+ " account_id int,"
        		+ " account_currency text,"
        		+ " account_category text"
        		+ " );";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseIX() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.ACCOUNTMANAGEMENT.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " player_uuid text NOT NULL,"
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
        		+ " player_uuid text NOT NULL,"
        		+ " account_id int,"
        		+ " account_currency text"
        		+ " );";
		baseSetup(data);
		return true;
	}
}