package main.java.me.avankziar.aep.spigot;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.me.avankziar.aep.spigot.api.LoggerApi;
import main.java.me.avankziar.aep.spigot.api.economy.CurrencyCommandSetup;
import main.java.me.avankziar.aep.spigot.api.economy.IFHApi;
import main.java.me.avankziar.aep.spigot.api.economy.VaultApi;
import main.java.me.avankziar.aep.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.bstats.Metrics;
import main.java.me.avankziar.aep.spigot.cmd.AepCommandExecutor;
import main.java.me.avankziar.aep.spigot.cmd.TABCompletion;
import main.java.me.avankziar.aep.spigot.cmd._MoneyCommandExecutor;
import main.java.me.avankziar.aep.spigot.cmd.eco.ARGEcoDeleteLog;
import main.java.me.avankziar.aep.spigot.cmd.eco.ARGEcoPlayer;
import main.java.me.avankziar.aep.spigot.cmd.eco.ARGEcoReComment;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyFreeze;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyGive;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyGiveConsole;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyPay;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneySet;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneySetConsole;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyTake;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyTakeConsole;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyToggle;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyTop;
import main.java.me.avankziar.aep.spigot.cmd.money.action.ARGMoneyActionLog;
import main.java.me.avankziar.aep.spigot.cmd.money.loggersettings.ARGMoneyLoggerSettings;
import main.java.me.avankziar.aep.spigot.cmd.money.loggersettings.ARGMoneyLoggerSettings_GUI;
import main.java.me.avankziar.aep.spigot.cmd.money.loggersettings.ARGMoneyLoggerSettings_Other;
import main.java.me.avankziar.aep.spigot.cmd.money.loggersettings.ARGMoneyLoggerSettings_Text;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.ARGMoneyTrendLog;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlSetup;
import main.java.me.avankziar.aep.spigot.database.YamlHandler;
import main.java.me.avankziar.aep.spigot.database.YamlManager;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.hook.ChestShopHook;
import main.java.me.avankziar.aep.spigot.hook.HeadDatabaseHook;
import main.java.me.avankziar.aep.spigot.hook.JobsHook;
import main.java.me.avankziar.aep.spigot.hook.QuickShopHook;
import main.java.me.avankziar.aep.spigot.listener.PlayerListener;
import main.java.me.avankziar.aep.spigot.listenerhandler.LoggerSettingsListenerHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;

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
	
	private static VaultApi vaultApi;
	private static IFHApi ifhApi;
	
	private static LoggerApi loggerApi;
	
	private ArrayList<CommandConstructor> commandTree;
	private ArrayList<BaseConstructor> helpList;
	private LinkedHashMap<String, ArgumentModule> argumentMap;
	public static String baseCommandI = "eco"; //Pfad angabe + ürspungliches Commandname
	public static String baseCommandII = "money";
	public static String baseCommandV = "standingorder";
	
	public static String infoCommandPath = "CmdEco";
	public static String infoCommand = "/"; //InfoComamnd
	
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
		
		try
		{
			yamlHandler = new YamlHandler(this);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		utility = new Utility(this);
		if (yamlHandler.getConfig().getBoolean("Mysql.Status", false) == true)
		{
			mysqlHandler = new MysqlHandler(this);
			mysqlSetup = new MysqlSetup(this);
		} else
		{
			log.severe("MySQL is not set in the Plugin " + pluginName + "!");
			Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(this);
			return;
		}
		loggerApi = new LoggerApi(this);
		new BungeeBridge(this);
		backgroundTask = new BackgroundTask(this);
		setupIFH();
		setupVault();
		setupStrings();
		setupCommandTree();
		setupListener();
		setupBstats();
		ConfigHandler.init(plugin);
		//setupExtraPermission(); ADDME
		new CurrencyCommandSetup(plugin).setupCommand();
		//ADDME Spieler, welche über dem Überfälligen Zeit (Nr.2) sind, sollen gelöscht werden.
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		if (yamlHandler.getConfig().getBoolean("Mysql.Status", false) == true)
		{
			if (mysqlSetup.getConnection() != null) 
			{
				mysqlSetup.closeConnection();
			}
		}
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
	
	private void setupStrings()
	{	
		//Zuletzt infoCommand deklarieren
		infoCommand += plugin.getYamlHandler().getCom().getString(baseCommandI+".Name");
	}
	
	private void setupCommandTree()
	{		
		/*LinkedHashMap<Integer, ArrayList<String>> lhmmode = new LinkedHashMap<>(); 
		List<PluginUser.Mode> modes = new ArrayList<PluginUser.Mode>(EnumSet.allOf(PluginUser.Mode.class));
		ArrayList<String> modeList = new ArrayList<String>();
		for(PluginUser.Mode m : modes) {modeList.add(m.toString());}
		lhmmode.put(1, modeList);*/
		
		ArgumentConstructor deletelog = new ArgumentConstructor(yamlHandler, baseCommandI+"_deletelog", 0, 1, 1, false, null);
		ArgumentConstructor player = new ArgumentConstructor(yamlHandler, baseCommandI+"_player", 0, 1, 1, false, playerMapI);
		ArgumentConstructor recomment = new ArgumentConstructor(yamlHandler, baseCommandI+"_recomment", 0, 2, 999, false, null);
		
		CommandConstructor eco = new CommandConstructor(plugin, baseCommandI, false,
				deletelog, player, recomment);
		
		registerCommand(eco.getPath(), eco.getName());
		getCommand(eco.getName()).setExecutor(new AepCommandExecutor(plugin, eco));
		getCommand(eco.getName()).setTabCompleter(new TABCompletion(plugin));
		
		addingCommandHelps(
				eco, 
					deletelog, player, recomment);
		
		new ARGEcoDeleteLog(plugin, deletelog);
		new ARGEcoPlayer(plugin, player);
		new ARGEcoReComment(plugin, recomment);
		
		if(AEPSettings.settings.isPlayerAccount()) //FIXME Existiert nicht mehr
		{
			log.info("Activate PlayerAccounts...");
			ArgumentConstructor actionlog = new ArgumentConstructor(yamlHandler, baseCommandII+"_actionlog", 0, 0, 2, false, playerMapII);
			
			ArgumentConstructor freeze = new ArgumentConstructor(yamlHandler, baseCommandII+"_freeze", 0, 1, 1, false, playerMapI);
			ArgumentConstructor give = new ArgumentConstructor(yamlHandler, baseCommandII+"_give", 0, 2, 999, false, playerMapI);
			ArgumentConstructor giveconsole = new ArgumentConstructor(yamlHandler, baseCommandII+"_giveconsole", 0, 4, 999, true, playerMapI);
			
			ArgumentConstructor loggersettings_gui = new ArgumentConstructor(yamlHandler, baseCommandII+"_loggersettings_gui", 1, 1, 4, false, null);
			ArgumentConstructor loggersettings_other = new ArgumentConstructor(yamlHandler, baseCommandII+"_loggersettings_other", 1, 2, 2, false, null);
			ArgumentConstructor loggersettings_text = new ArgumentConstructor(yamlHandler, baseCommandII+"_loggersettings_text", 1, 2, 999, false, null);
			ArgumentConstructor loggersettings = new ArgumentConstructor(yamlHandler, baseCommandII+"_loggersettings", 0, 0, 0, false, null,
					loggersettings_gui, loggersettings_other, loggersettings_text);
			
			ArgumentConstructor pay = new ArgumentConstructor(yamlHandler, baseCommandII+"_pay", 0, 2, 999, false, playerMapI);
			ArgumentConstructor set = new ArgumentConstructor(yamlHandler, baseCommandII+"_set", 0, 2, 999, false, playerMapI);
			ArgumentConstructor setconsole = new ArgumentConstructor(yamlHandler, baseCommandII+"_setconsole", 0, 4, 999, true, playerMapI);
			
			ArgumentConstructor take = new ArgumentConstructor(yamlHandler, baseCommandII+"_take", 0, 2, 999, true, playerMapI);
			ArgumentConstructor takeconsole = new ArgumentConstructor(yamlHandler, baseCommandII+"_takeconsole", 0, 4, 999, true, playerMapI);
			ArgumentConstructor toggle = new ArgumentConstructor(yamlHandler, baseCommandII+"_toggle", 0, 0, 0, false, null);
			ArgumentConstructor top = new ArgumentConstructor(yamlHandler, baseCommandII+"_top", 0, 0, 1, false, null);
			
			ArgumentConstructor trendlog = new ArgumentConstructor(yamlHandler, baseCommandII+"_trendlog", 0, 0, 2, false, playerMapII);
			
			CommandConstructor money = new CommandConstructor(plugin, baseCommandII, false,
					freeze, give, giveconsole, loggersettings, pay, set, setconsole, take, takeconsole, toggle, top,
					actionlog, trendlog);
			
			LoggerSettingsHandler.loggerSettingsCommandString = loggersettings_gui.getCommandString();
			LoggerSettingsHandler.loggerSettingsTextCommandString = loggersettings_text.getCommandString();
			
			registerCommand(money.getPath(), money.getName());
			getCommand(money.getName()).setExecutor(new _MoneyCommandExecutor(plugin, money));
			getCommand(money.getName()).setTabCompleter(new TABCompletion(plugin));
			
			addingCommandHelps(
					money,
						actionlog, trendlog,
						loggersettings, loggersettings_gui, loggersettings_other, loggersettings_text,
						freeze, give, giveconsole, pay, set, setconsole, take, takeconsole, toggle, top);
			
			new ARGMoneyActionLog(plugin, actionlog);		
			
			new ARGMoneyFreeze(plugin, freeze);
			//new ARGMoneyGetTotal(plugin);
			new ARGMoneyGive(plugin, give);
			new ARGMoneyGiveConsole(plugin, giveconsole);
			
			new ARGMoneyLoggerSettings(plugin, loggersettings);
			new ARGMoneyLoggerSettings_GUI(plugin, loggersettings_gui);
			new ARGMoneyLoggerSettings_Other(plugin, loggersettings_other);
			new ARGMoneyLoggerSettings_Text(plugin, loggersettings_text);
			
			new ARGMoneyPay(plugin, pay);
			new ARGMoneySet(plugin, set);
			new ARGMoneySetConsole(plugin, setconsole);
			new ARGMoneyTake(plugin, take);
			new ARGMoneyTakeConsole(plugin, takeconsole);
			new ARGMoneyToggle(plugin, toggle);
			new ARGMoneyTop(plugin, top);
			new ARGMoneyTrendLog(plugin, trendlog);
		}		
	}
	
	public void setupListener()
	{
		PluginManager pm = getServer().getPluginManager();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "advancedeconomy:spigottobungee");
		pm.registerEvents(new PlayerListener(plugin), plugin);
		pm.registerEvents(new LoggerSettingsListenerHandler(plugin), plugin);
		if(existHook("ChestShop"))
		{
			log.info(pluginName+" hook with ChestShop");
			pm.registerEvents(new ChestShopHook(plugin), plugin);
		}
		if(existHook("HeadDatabase"))
		{
			log.info(pluginName+" hook with HeadDatabase");
			pm.registerEvents(new HeadDatabaseHook(plugin), plugin);
		}
		if(existHook("Jobs"))
		{
			log.info(pluginName+" hook with JobsReborn");
			pm.registerEvents(new JobsHook(plugin), plugin);
		}
		if(existHook("QuickShop"))
		{
			log.info(pluginName+" hook with QuickShop");
			pm.registerEvents(new QuickShopHook(plugin), plugin);
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
	
	public static VaultApi getVault()
	{
		return vaultApi;
	}
	
	public IFHApi getIFHApi()
	{
		return ifhApi;
	}

	private boolean setupVault() 
	{
		if (plugin.getServer().getPluginManager().isPluginEnabled("Vault")) 
		{
			vaultApi = new VaultApi(this);
            plugin.getServer().getServicesManager().register(
            		net.milkbowl.vault.economy.Economy.class,
            		vaultApi,
            		this,
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
		ifhApi = new IFHApi(plugin);
    	plugin.getServer().getServicesManager().register(
        main.java.me.avankziar.ifh.spigot.economy.Economy.class,
        ifhApi,
        this,
        ServicePriority.Normal);
    	log.info(pluginName + " detected InterfaceHub >>> Economy.class is provided!");
		return false;
	}
	
	public boolean existHook(String externPluginName)
	{
		if(plugin.getServer().getPluginManager().getPlugin(externPluginName) == null)
		{
			return false;
		}
		return true;
	}
	
	public void setupBstats()
	{
		int pluginId = 7665;
        new Metrics(this, pluginId);
	}
}