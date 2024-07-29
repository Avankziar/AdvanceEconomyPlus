package me.avankziar.aep.velocity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import me.avankziar.aep.general.database.YamlHandler;
import me.avankziar.aep.general.database.YamlManager;
import me.avankziar.aep.velocity.api.economy.IFHEcoProvider;
import me.avankziar.aep.velocity.database.MysqlHandler;
import me.avankziar.aep.velocity.database.MysqlSetup;
import me.avankziar.ifh.velocity.IFH;
import me.avankziar.ifh.velocity.administration.Administration;
import me.avankziar.ifh.velocity.plugin.RegisteredServiceProvider;
import me.avankziar.ifh.velocity.plugin.ServicePriority;

@Plugin(
		id = "advancedeconomyplus", 
		name = "AdvancedEconomyPlus", 
		version = "4-7-0",
		url = "https://www.spigotmc.org/resources/advanced-economy-plus.79828/",
		dependencies = {
				@Dependency(id = "interfacehub", optional = false)
		},
		description = "A Economy Plugin",
		authors = {"Avankziar"}
)
public class AEP
{
	private static AEP plugin;
    private final ProxyServer server;
    private Logger logger = null;
    private Path dataDirectory;
    public String pluginname = "AdvancedEconomyPlus";
    private YamlHandler yamlHandler;
    private YamlManager yamlManager;
    private MysqlSetup mysqlSetup;
    private MysqlHandler mysqlHandler;
   	private static Administration administrationConsumer;
   	
   	private IFHEcoProvider ifhProvider;
    
    @Inject
    public AEP(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) 
    {
    	AEP.plugin = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) 
    {
    	logger = Logger.getLogger("AEP");
    	PluginDescription pd = server.getPluginManager().getPlugin(pluginname.toLowerCase()).get().getDescription();
        List<String> dependencies = new ArrayList<>();
        pd.getDependencies().stream().allMatch(x -> dependencies.add(x.getId()));
        //https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=SCC
		logger.info("  █████╗ ███████╗██████╗  | Id: "+pd.getId());
		logger.info(" ██╔══██╗██╔════╝██╔══██╗ | Version: "+pd.getVersion().get());
		logger.info(" ███████║█████╗  ██████╔╝ | Author: ["+String.join(", ", pd.getAuthors())+"]");
		logger.info(" ██╔══██║██╔══╝  ██╔═══╝  | Description: "+(pd.getDescription().isPresent() ? pd.getDescription().get() : "/"));
		logger.info(" ██║  ██║███████╗██║      | Plugin Website:"+pd.getUrl().get().toString());
		logger.info(" ╚═╝  ╚═╝╚══════╝╚═╝      | Dependencies Plugins: ["+String.join(", ", dependencies)+"]");
        
		setupIFHAdministration();
		
        yamlHandler = new YamlHandler(YamlManager.Type.VELO, pluginname, logger, dataDirectory,
        		(plugin.getAdministration() == null ? null : plugin.getAdministration().getLanguage()));
        setYamlManager(yamlHandler.getYamlManager());
        
        String path = plugin.getYamlHandler().getConfig().getString("IFHAdministrationPath");
		boolean adm = plugin.getAdministration() != null
				&& plugin.getYamlHandler().getConfig().getBoolean("useIFHAdministration")
				&& plugin.getAdministration().isMysqlPathActive(path);
		if(adm || yamlHandler.getConfig().getBoolean("Mysql.Status", false) == true)
		{
			mysqlSetup = new MysqlSetup(plugin, adm, path);
			mysqlHandler = new MysqlHandler(plugin);
		}
		setupIFHProvider();
    }
    
    public static AEP getPlugin()
    {
    	return AEP.plugin;
    }
    
    public ProxyServer getServer()
    {
    	return server;
    }
    
    public Logger getLogger()
    {
    	return logger;
    }
    
    public Path getDataDirectory()
    {
    	return dataDirectory;
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
    	this.yamlManager = yamlManager;
    }
    
    public MysqlSetup getMysqlSetup()
    {
    	return mysqlSetup;
    }
    
    public MysqlHandler getMysqlHandler()
    {
    	return mysqlHandler;
    }
    
	private void setupIFHProvider()
	{
		Optional<PluginContainer> ifhp = getServer().getPluginManager().getPlugin("interfacehub");
		Optional<PluginContainer> plugin = getServer().getPluginManager().getPlugin(pluginname.toLowerCase());
        if (ifhp.isEmpty()) 
        {
        	logger.info(pluginname + " dont find InterfaceHub!");
            return;
        }
        me.avankziar.ifh.velocity.IFH ifh = IFH.getPlugin();
        try
        {
    		IFHEcoProvider cp = new IFHEcoProvider(getPlugin());
            ifh.getServicesManager().register(
             		me.avankziar.ifh.velocity.economy.Economy.class,
             		cp, plugin.get(), ServicePriority.Normal);
            logger.info(pluginname + " detected InterfaceHub >>> Economy.class is provided!");
    		
        } catch(NoClassDefFoundError e) {}
	}
	
	public IFHEcoProvider getIFHApi()
	{
		return ifhProvider;
	}
	
	private void setupIFHAdministration()
	{ 
		Optional<PluginContainer> ifhp = plugin.getServer().getPluginManager().getPlugin("interfacehub");
        if (ifhp.isEmpty()) 
        {
        	logger.info(pluginname + " dont find InterfaceHub!");
            return;
        }
        me.avankziar.ifh.velocity.IFH ifh = IFH.getPlugin();
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
    		logger.info(pluginname + " detected InterfaceHub >>> Administration.class is consumed!");
        }
        return;
	}
	
	public Administration getAdministration()
	{
		return administrationConsumer;
	}
}