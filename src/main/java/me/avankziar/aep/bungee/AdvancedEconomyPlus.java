package main.java.me.avankziar.aep.bungee;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.me.avankziar.aep.bungee.api.economy.IFHEcoProvider;
import main.java.me.avankziar.aep.bungee.database.MysqlHandler;
import main.java.me.avankziar.aep.bungee.database.MysqlSetup;
import main.java.me.avankziar.aep.bungee.database.YamlHandler;
import main.java.me.avankziar.aep.bungee.database.YamlManager;
import main.java.me.avankziar.ifh.bungee.InterfaceHub;
import main.java.me.avankziar.ifh.bungee.administration.Administration;
import main.java.me.avankziar.ifh.bungee.plugin.RegisteredServiceProvider;
import main.java.me.avankziar.ifh.bungee.plugin.ServicePriority;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class AdvancedEconomyPlus extends Plugin
{
	public static Logger log;
	public String pluginName = "AdvancedEconomyPlus";
	private static AdvancedEconomyPlus plugin;
	private static YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	
	private IFHEcoProvider ifhProvider;
	private Administration adminstrationConsumer;
	
	private ScheduledTask administrationRun;
	
	public void onEnable() 
	{
		plugin = this;
		log = getLogger();
		
		setupIFHAdministration();
		
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
		ifhProvider = new IFHEcoProvider(plugin);
    	ifh.getServicesManager().register(
        main.java.me.avankziar.ifh.bungee.economy.Economy.class,
        ifhProvider,
        this,
        ServicePriority.Normal);
    	log.info(pluginName + " detected InterfaceHub >>> Economy.class is provided!");
		return false;
	}
	
	public IFHEcoProvider getIFHApi()
	{
		return ifhProvider;
	}
	
	private void setupIFHAdministration()
	{ 
		Plugin plugin = BungeeCord.getInstance().getPluginManager().getPlugin("InterfaceHub");
        if (plugin == null) 
        {
            return;
        }
        InterfaceHub ifh = (InterfaceHub) plugin;
        administrationRun = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					RegisteredServiceProvider<Administration> rsp = ifh
			        		.getServicesManager()
			        		.getRegistration(Administration.class);
			        if (rsp == null) 
			        {
			            return;
			        }
			        adminstrationConsumer = rsp.getProvider();
			        if(adminstrationConsumer != null)
			        {
			    		log.info(pluginName + " detected InterfaceHub >>> Administration.class is consumed!");
			    		administrationRun.cancel();
			        }
				} catch(NoClassDefFoundError e)
				{
					administrationRun.cancel();
				}
			}
		}, 15L*1000, 25L, TimeUnit.MILLISECONDS);
        return;
	}
	
	public Administration getAdministration()
	{
		return adminstrationConsumer;
	}
}
