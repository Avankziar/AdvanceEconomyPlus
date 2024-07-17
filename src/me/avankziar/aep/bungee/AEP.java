package me.avankziar.aep.bungee;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.avankziar.aep.bungee.api.economy.IFHEcoProvider;
import me.avankziar.aep.bungee.database.MysqlHandler;
import me.avankziar.aep.bungee.database.MysqlSetup;
import me.avankziar.aep.general.database.YamlHandler;
import me.avankziar.aep.general.database.YamlManager;
import me.avankziar.ifh.bungee.IFH;
import me.avankziar.ifh.bungee.administration.Administration;
import me.avankziar.ifh.bungee.plugin.RegisteredServiceProvider;
import me.avankziar.ifh.bungee.plugin.ServicePriority;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class AEP extends Plugin
{
	public static Logger logger;
	public String pluginName = "AdvancedEconomyPlus";
	private static AEP plugin;
	private static YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	
	private IFHEcoProvider ifhProvider;
	private Administration administrationConsumer;
	
	public void onEnable() 
	{
		plugin = this;
		logger = getLogger();
		
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=AEP
		logger.info("  █████╗ ███████╗██████╗  | Version: "+plugin.getDescription().getVersion());
		logger.info(" ██╔══██╗██╔════╝██╔══██╗ | Author: "+plugin.getDescription().getAuthor());
		logger.info(" ███████║█████╗  ██████╔╝ | Plugin Website: https://www.spigotmc.org/resources/advanced-economy-plus.79828/");
		logger.info(" ██╔══██║██╔══╝  ██╔═══╝  | Depend Plugins: "+plugin.getDescription().getDepends().toString());
		logger.info(" ██║  ██║███████╗██║      | SoftDepend Plugins: "+plugin.getDescription().getSoftDepends().toString());
		logger.info(" ╚═╝  ╚═╝╚══════╝╚═╝      | Have Fun^^");
		
		setupIFHAdministration();
		
		yamlHandler = new YamlHandler(YamlManager.Type.BUNGEE, pluginName, logger, plugin.getDataFolder().toPath(),
        		(plugin.getAdministration() == null ? null : plugin.getAdministration().getLanguage()));
        setYamlManager(yamlHandler.getYamlManager());
		
		String path = plugin.getYamlHandler().getConfig().getString("IFHAdministrationPath");
		boolean adm = plugin.getAdministration() != null 
				&& plugin.getYamlHandler().getConfig().getBoolean("useIFHAdministration")
				&& plugin.getAdministration().isMysqlPathActive(path);
		if(adm || yamlHandler.getConfig().getBoolean("Mysql.Status", false))
		{
			mysqlSetup = new MysqlSetup(plugin, adm, path);
			mysqlHandler = new MysqlHandler(plugin);
		} else
		{
			logger.severe("MySQL is not enabled! "+pluginName+" wont work correctly!");
			this.onDisable();
			return;
		}
		setupIFHProvider();
	}
	
	public void onDisable()
	{
		getProxy().getScheduler().cancel(this);		
		logger.info(pluginName + " is disabled!");
	}
	
	public static AEP getPlugin()
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
		AEP.yamlManager = yamlManager;
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
	private boolean setupIFHProvider()
	{
		me.avankziar.ifh.bungee.IFH ifh = IFH.getPlugin();
		if(ifh == null) 
	    {
			logger.severe("IFH is not set in the Plugin " + pluginName + "! Disable plugin!");
			plugin.getExecutorService().shutdownNow();
	    	return false;
	    }
		ifhProvider = new IFHEcoProvider(plugin);
    	ifh.getServicesManager().register(
        me.avankziar.ifh.bungee.economy.Economy.class,
        ifhProvider,
        this,
        ServicePriority.Normal);
    	logger.info(pluginName + " detected InterfaceHub >>> Economy.class is provided!");
		return false;
	}
	
	public IFHEcoProvider getIFHApi()
	{
		return ifhProvider;
	}
	
	private void setupIFHAdministration()
	{ 
		Plugin plugin = getProxy().getPluginManager().getPlugin("InterfaceHub");
        if (plugin == null) 
        {
            return;
        }
        IFH ifh = (IFH) plugin;
        try
		{
			RegisteredServiceProvider<Administration> rsp = ifh
	        		.getServicesManager()
	        		.getRegistration(Administration.class);
	        if (rsp == null) 
	        {
	            return;
	        }
	        administrationConsumer = rsp.getProvider();
	        if(administrationConsumer != null)
	        {
	    		logger.info(pluginName + " detected InterfaceHub >>> Administration.class is consumed!");
	        }
		} catch(NoClassDefFoundError e)
		{}
        return;
	}
	
	public Administration getAdministration()
	{
		return administrationConsumer;
	}
}
