package main.java.me.avankziar.aep.spigot;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.spigot.api.LoggerApi;
import main.java.me.avankziar.aep.spigot.api.economy.CurrencyCommandSetup;
import main.java.me.avankziar.aep.spigot.api.economy.IFHEcoProvider;
import main.java.me.avankziar.aep.spigot.api.economy.VaultEcoProvider;
import main.java.me.avankziar.aep.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.bstats.Metrics;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlSetup;
import main.java.me.avankziar.aep.spigot.database.YamlHandler;
import main.java.me.avankziar.aep.spigot.database.YamlManager;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.hook.ChestShopHook;
import main.java.me.avankziar.aep.spigot.hook.HeadDatabaseHook;
import main.java.me.avankziar.aep.spigot.hook.JobsHook;
import main.java.me.avankziar.aep.spigot.hook.QuickShopHook;
import main.java.me.avankziar.aep.spigot.listener.GuiPayListener;
import main.java.me.avankziar.aep.spigot.listener.PlayerListener;
import main.java.me.avankziar.aep.spigot.listenerhandler.LoggerSettingsListenerHandler;
import main.java.me.avankziar.ifh.spigot.administration.Administration;
import main.java.me.avankziar.ifh.spigot.tobungee.chatlike.MessageToBungee;

public class AdvancedEconomyPlus extends JavaPlugin
{
	private static AdvancedEconomyPlus plugin;
	public static Logger log;
	public String pluginName = "AdvancedEconomyPlus";
	private static YamlManager yamlManager;
	private static YamlHandler yamlHandler;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	private static Utility utility;
	private static BackgroundTask backgroundTask;
	
	private static VaultEcoProvider vaultProvider;
	private static IFHEcoProvider ifhProvider;
	private static MessageToBungee mtbConsumer = null;
	private static Administration administrationConsumer;
	
	public static boolean isPapiRegistered = false;
	
	private static LoggerApi loggerApi;
	
	private ArrayList<CommandConstructor> commandTree;
	private ArrayList<BaseConstructor> helpList;
	private LinkedHashMap<String, ArgumentModule> argumentMap;
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=AEP
		log.info("  █████╗ ███████╗██████╗  | API-Version: "+plugin.getDescription().getAPIVersion());
		log.info(" ██╔══██╗██╔════╝██╔══██╗ | Author: "+plugin.getDescription().getAuthors().toString());
		log.info(" ███████║█████╗  ██████╔╝ | Plugin Website: "+plugin.getDescription().getWebsite());
		log.info(" ██╔══██║██╔══╝  ██╔═══╝  | Depend Plugins: "+plugin.getDescription().getDepend().toString());
		log.info(" ██║  ██║███████╗██║      | SoftDepend Plugins: "+plugin.getDescription().getSoftDepend().toString());
		log.info(" ╚═╝  ╚═╝╚══════╝╚═╝      | LoadBefore: "+plugin.getDescription().getLoadBefore().toString());
		
		commandTree = new ArrayList<>();
		helpList = new ArrayList<>();
		argumentMap = new LinkedHashMap<>();
		
		setupIFH();
		setupIFHAdministration();
		
		try
		{
			yamlHandler = new YamlHandler(this);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		utility = new Utility(this);
		String path = plugin.getYamlHandler().getConfig().getString("IFHAdministrationPath");
		boolean adm = plugin.getAdministration() != null 
				&& plugin.getYamlHandler().getConfig().getBoolean("useIFHAdministration")
				&& plugin.getAdministration().isMysqlPathActive(path);
		if(adm || yamlHandler.getConfig().getBoolean("Mysql.Status", false) == true)
		{
			mysqlHandler = new MysqlHandler(this);
			mysqlSetup = new MysqlSetup(this, adm, path);
		} else
		{
			log.severe("MySQL is not set in the Plugin " + pluginName + "!");
			Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(this);
			return;
		}
		loggerApi = new LoggerApi(this);
		ConfigHandler.init(plugin);
		setupVault();
		ifhProvider.registerCurrencyFromFile();
		backgroundTask = new BackgroundTask(this);
		setupListener();
		setupBstats();
		setupExtraPermission();
		new CurrencyCommandSetup(plugin).setupCommand();
		setupPlaceholderAPI();
		setupMessageToBungee();		
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		log.info(pluginName + " is disabled!");
	}
	
	public AdvancedEconomyPlus()
	{
		plugin = this;
	}

	public static AdvancedEconomyPlus getPlugin()
	{
		return plugin;
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
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public static BackgroundTask getBackgroundTask()
	{
		return backgroundTask;
	}
	
	public static LoggerApi getLoggerApi()
	{
		return loggerApi;
	}
	
	public void setupListener()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(plugin), plugin);
		pm.registerEvents(new LoggerSettingsListenerHandler(plugin), plugin);
		pm.registerEvents(new GuiPayListener(plugin), plugin);
		if(existHook("ChestShop") && plugin.getYamlHandler().getConfig().getBoolean("ChestShop.EnableHook", false))
		{
			log.info(pluginName+" hook with ChestShop");
			pm.registerEvents(new ChestShopHook(plugin), plugin);
		}
		if(existHook("HeadDatabase") && plugin.getYamlHandler().getConfig().getBoolean("HeadDatabase.EnableHook", false))
		{
			log.info(pluginName+" hook with HeadDatabase");
			pm.registerEvents(new HeadDatabaseHook(plugin), plugin);
		}
		if(existHook("Jobs") && plugin.getYamlHandler().getConfig().getBoolean("JobsReborn.EnableHook", false))
		{
			log.info(pluginName+" hook with JobsReborn");
			pm.registerEvents(new JobsHook(plugin), plugin);
		}
		if(existHook("QuickShop") && plugin.getYamlHandler().getConfig().getBoolean("QuickShop.EnableHook", false))
		{
			log.info(pluginName+" hook with QuickShop");
			pm.registerEvents(new QuickShopHook(plugin), plugin);
		}
	}
	
	public void setupExtraPermission()
	{
		List<ExtraPerm.Type> list = new ArrayList<ExtraPerm.Type>(EnumSet.allOf(ExtraPerm.Type.class));
		for(ExtraPerm.Type ept : list)
		{
			ExtraPerm.set(ept, plugin.getYamlHandler().getCom().getString("Bypass."+ept.toString().replace("_", "")));
		}
	}
	
	public ArrayList<BaseConstructor> getCommandHelpList()
	{
		return helpList;
	}
	
	public void addingCommandHelps(BaseConstructor... objects)
	{
		for(BaseConstructor bc : objects)
		{
			helpList.add(bc);
		}
	}
	
	public ArrayList<CommandConstructor> getCommandTree()
	{
		return commandTree;
	}
	
	public CommandConstructor getCommandFromPath(String commandpath)
	{
		CommandConstructor cc = null;
		for(CommandConstructor coco : getCommandTree())
		{
			if(coco.getPath().equalsIgnoreCase(commandpath))
			{
				cc = coco;
				break;
			}
		}
		return cc;
	}
	
	public CommandConstructor getCommandFromCommandString(String command)
	{
		CommandConstructor cc = null;
		for(CommandConstructor coco : getCommandTree())
		{
			if(coco.getName().equalsIgnoreCase(command))
			{
				cc = coco;
				break;
			}
		}
		return cc;
	}

	public LinkedHashMap<String, ArgumentModule> getArgumentMap()
	{
		return argumentMap;
	}
	
	public void registerCommand(String... aliases) 
	{
		PluginCommand command = getCommand(aliases[0], plugin);
	 
		command.setAliases(Arrays.asList(aliases));
		getCommandMap().register(plugin.getDescription().getName(), command);
	}
	 
	private static PluginCommand getCommand(String name, AdvancedEconomyPlus plugin) 
	{
		PluginCommand command = null;
	 
		try {
			Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);
	 
			command = c.newInstance(name, plugin);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	 
		return command;
	}
	 
	private static CommandMap getCommandMap() 
	{
		CommandMap commandMap = null;
	 
		try {
			if (Bukkit.getPluginManager() instanceof SimplePluginManager) 
			{
				Field f = SimplePluginManager.class.getDeclaredField("commandMap");
				f.setAccessible(true);
	 
				commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	 
		return commandMap;
	}
	
	public static VaultEcoProvider getVault()
	{
		return vaultProvider;
	}
	
	public IFHEcoProvider getIFHApi()
	{
		return ifhProvider;
	}

	private boolean setupVault() 
	{
		if (plugin.getServer().getPluginManager().isPluginEnabled("Vault")) 
		{
			vaultProvider = new VaultEcoProvider(plugin);
            plugin.getServer().getServicesManager().register(
            		net.milkbowl.vault.economy.Economy.class,
            		vaultProvider,
            		plugin,
                    ServicePriority.Normal);
            log.info(pluginName + " detected Vault. Hooking!");
            return true;
        }
		return false;
    }
	
	private boolean setupIFH()
	{
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
			log.severe("IFH is not set in the Plugin " + pluginName + "! Disable plugin!");
			Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(this);
	    	return false;
	    }
		ifhProvider = new IFHEcoProvider(plugin);
    	plugin.getServer().getServicesManager().register(
        main.java.me.avankziar.ifh.spigot.economy.Economy.class,
        ifhProvider,
        this,
        ServicePriority.Normal);
    	log.info(pluginName + " detected InterfaceHub >>> Economy.class is provided!");
		return false;
	}
	
	private void setupMessageToBungee() 
	{
        if(Bukkit.getPluginManager().getPlugin("InterfaceHub") == null) 
        {
            return;
        }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
						return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.tobungee.chatlike.MessageToBungee> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.spigot.tobungee.chatlike.MessageToBungee.class);
				    if(rsp == null) 
				    {
				    	//Check up to 20 seconds after the start, to connect with the provider
				    	i++;
				        return;
				    }
				    mtbConsumer = rsp.getProvider();
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}			    
			}
        }.runTaskTimer(plugin, 20L, 20*2);
	}
	
	public MessageToBungee getMtB()
	{
		return mtbConsumer;
	}
	
	public boolean existHook(String externPluginName)
	{
		if(plugin.getServer().getPluginManager().getPlugin(externPluginName) == null)
		{
			return false;
		}
		return true;
	}
	
	private boolean setupPlaceholderAPI()
	{
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
		{
            new main.java.me.avankziar.aep.spigot.hook.PAPIHook(plugin).register();
            return true;
		}
		return false;
	}
	
	public void setupBstats()
	{
		int pluginId = 7665;
        new Metrics(this, pluginId);
	}
	
	private void setupIFHAdministration()
	{ 
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
		try
	    {
	    	RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.administration.Administration> rsp = 
                     getServer().getServicesManager().getRegistration(Administration.class);
		    if (rsp == null) 
		    {
		        return;
		    }
		    administrationConsumer = rsp.getProvider();
		    log.info(pluginName + " detected InterfaceHub >>> Administration.class is consumed!");
	    } catch(NoClassDefFoundError e) 
	    {}
	}
	
	public Administration getAdministration()
	{
		return administrationConsumer;
	}
	
	/*private Economy ifheco;
	
	setupIFHEco() in der main ausführen
	
	public Economy getEco()
	{
		return ifheco;
	}
	
	private void test(Player player, Player twoplayer)
	{
		Account ac = ifheco.getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, ifheco.getDefaultCurrency(CurrencyType.DIGITAL));
		ifheco.withdraw(ac, 1000); //Nur wenn man etwas abziehen will.
		ifheco.deposit(ac, 1000); //Nur wenn man etwas deponieren will.
		String cat = "Die Kategorie, am besten ein Wort wie: Shop oder sowas ";
		String comment = "Die Notiz, eine Beschreibung was gemacht wurde.";
		ifheco.withdraw(ac, 1000, OrdererType.PLAYER, player.getUniqueId().toString(), cat, comment); //Wenn man etwas mit Notiz etc. abziehen will
		ifheco.deposit(ac, 1000, OrdererType.PLAYER, player.getUniqueId().toString(), cat, comment); //Wenn man etwas mit Notiz etc. deponieren will
		
		Account to = ifheco.getDefaultAccount(twoplayer.getUniqueId(), AccountCategory.MAIN, ifheco.getDefaultCurrency(CurrencyType.DIGITAL));
		ifheco.transaction(ac, to, 1000); //einfache transaktion zwischen 2 spieler
		ifheco.transaction(ac, to, 1000, OrdererType.PLAYER, player.getUniqueId().toString(), cat, comment); //transaktion zwischen 2 Spieler mit notiz
	}
	
	private void setupIFHEco() 
	{
        if (Bukkit.getPluginManager().getPlugin("InterfaceHub") == null) 
        {
            return;
        }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
			    if(i == 20)
			    {
					cancel();
					return;
			    }
			    RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.economy.Economy> rsp = 
	                             getServer().getServicesManager().getRegistration(Economy.class);
			    if (rsp == null) 
			    {
			    	i++;
			        return;
			    }
			    ifheco = rsp.getProvider();
			    cancel();
			}
        }.runTaskTimer(plugin, 20L, 20*2);
	}*/
}