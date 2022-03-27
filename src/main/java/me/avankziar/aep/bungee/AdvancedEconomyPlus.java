package main.java.me.avankziar.aep.bungee;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.me.avankziar.aep.bungee.api.economy.IFHApi;
import main.java.me.avankziar.aep.bungee.database.MysqlHandler;
import main.java.me.avankziar.aep.bungee.database.MysqlSetup;
import main.java.me.avankziar.aep.bungee.database.YamlHandler;
import main.java.me.avankziar.aep.bungee.database.YamlManager;
import main.java.me.avankziar.ifh.bungee.InterfaceHub;
import main.java.me.avankziar.ifh.bungee.plugin.ServicePriority;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class AdvancedEconomyPlus extends Plugin
{
	public static Logger log;
	public String pluginName = "AdvancedEconomyPlus";
	private static AdvancedEconomyPlus plugin;
	private static YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	
	private IFHApi ifhApi;
	
	public void onEnable() 
	{
		plugin = this;
		log = getLogger();
		yamlHandler = new YamlHandler(plugin);
		
		if(yamlHandler.getConfig().getBoolean("Mysql.Status", false))
		{
			mysqlHandler = new MysqlHandler(plugin);
			mysqlSetup = new MysqlSetup(plugin);
		} else
		{
			log.severe("MySQL is not enabled! "+pluginName+" wont work correctly!");
			this.onDisable();
			return;
		}
		setupIFH();
	}
	
	public void onDisable()
	{
		getProxy().getScheduler().cancel(this);		
		log.info(pluginName + " is disabled!");
	}
	
	public static AdvancedEconomyPlus getPlugin()
	{
		return plugin;
	}
	
	@SuppressWarnings("deprecation")
	public void disablePlugin()
	{
		Plugin plugin = (Plugin) ProxyServer.getInstance().getPluginManager().getPlugin(pluginName);
	       
		try
		{
			plugin.onDisable();
			for (Handler handler : plugin.getLogger().getHandlers())
			{
				handler.close();
			}
		} catch (Throwable t) 
		{
			getLogger().log(Level.SEVERE, "Exception disabling plugin " + plugin.getDescription().getName(), t);
		}
		ProxyServer.getInstance().getPluginManager().unregisterCommands(plugin);
		ProxyServer.getInstance().getPluginManager().unregisterListeners(plugin);
		ProxyServer.getInstance().getScheduler().cancel(plugin);
		plugin.getExecutorService().shutdownNow();
	}
	
	public YamlHandler getYamlHandler() 
	{
		return yamlHandler;
	}
	
	public YamlManager getYamlManager()
	{
		return yamlManager;
	}
	
	public void setYamlManager(YamlManager yamlManager)
	{
		AdvancedEconomyPlus.yamlManager = yamlManager;
	}
	
	public MysqlSetup getMysqlSetup() 
	{
		return mysqlSetup;
	}
	
	public MysqlHandler getMysqlHandler()
	{
		return mysqlHandler;
	}
	
	@SuppressWarnings("deprecation")
	private boolean setupIFH()
	{
		InterfaceHub ifh = InterfaceHub.plugin;
		if(ifh == null) 
	    {
			log.severe("IFH is not set in the Plugin " + pluginName + "! Disable plugin!");
			plugin.getExecutorService().shutdownNow();
	    	return false;
	    }
		ifhApi = new IFHApi(plugin);
    	ifh.getServicesManager().register(
        main.java.me.avankziar.ifh.bungee.economy.Economy.class,
        ifhApi,
        this,
        ServicePriority.Normal);
    	log.info(pluginName + " detected InterfaceHub >>> Economy.class is provided!");
		return false;
	}
	
	public IFHApi getIFHApi()
	{
		return ifhApi;
	}
}
