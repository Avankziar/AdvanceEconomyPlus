package main.java.me.avankziar.advanceeconomy.spigot;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.me.avankziar.advanceeconomy.spigot.api.LoggerApi;
import main.java.me.avankziar.advanceeconomy.spigot.api.VaultApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.Utility;
import main.java.me.avankziar.advanceeconomy.spigot.bstats.Metrics;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandHelper;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.commands.MultipleCommandExecutor;
import main.java.me.avankziar.advanceeconomy.spigot.commands.TABCompletion;
import main.java.me.avankziar.advanceeconomy.spigot.commands.eco.ARGEcoDeleteLog;
import main.java.me.avankziar.advanceeconomy.spigot.commands.eco.ARGEcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.commands.eco.ARGEcoReComment;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogBetween;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogComment;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogCommentAscending;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogCommentDescending;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogFrom;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogGreaterThan;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogLessThan;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogOrderer;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogSortAscending;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogSortDescending;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFilterLogTo;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyFreeze;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyGive;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyLog;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyPay;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyTake;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyToggle;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyTop;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyTrendDiagram;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyTrendGrafic;
import main.java.me.avankziar.advanceeconomy.spigot.commands.money.ARGMoneyTrendLog;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlSetup;
import main.java.me.avankziar.advanceeconomy.spigot.database.YamlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.hook.ChestShopHook;
import main.java.me.avankziar.advanceeconomy.spigot.hook.HeadDatabaseHook;
import main.java.me.avankziar.advanceeconomy.spigot.hook.JobsHook;
import main.java.me.avankziar.advanceeconomy.spigot.listener.LoggerListener;
import main.java.me.avankziar.advanceeconomy.spigot.listener.PlayerListener;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;

public class AdvanceEconomy extends JavaPlugin
{
	public static Logger log;
	public String pluginName = "AdvanceEconomyPlus";
	private static YamlHandler yamlHandler;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	private static Utility utility;
	private static AdvanceEconomy plugin;
	private static VaultApi vaultApi;
	private static BackgroundTask backgroundTask;
	private static CommandHelper commandHelper;
	private static LoggerApi loggerApi;
	public static LinkedHashMap<String, CommandModule> moneyarguments;
	public static LinkedHashMap<String, CommandModule> bankarguments;
	public static LinkedHashMap<String, CommandModule> ecoarguments;
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		yamlHandler = new YamlHandler(this);
		utility = new Utility(this);
		moneyarguments = new LinkedHashMap<String, CommandModule>();
		bankarguments = new LinkedHashMap<String, CommandModule>();
		ecoarguments = new LinkedHashMap<String, CommandModule>();
		if (yamlHandler.get().getBoolean("Mysql.Status", false) == true)
		{
			mysqlHandler = new MysqlHandler(this);
			mysqlSetup = new MysqlSetup(this);
		} else
		{
			log.severe("MySQL is not set in the Plugin " + pluginName + "!");
			Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(this);
			return;
		}
		EconomySettings.initSettings(this);
		commandHelper = new CommandHelper(this);
		backgroundTask = new BackgroundTask(this);
		loggerApi = new LoggerApi(this);
		new BungeeBridge(this);
		setupEconomy();
		CommandSetup();
		ListenerSetup();
		setupBstats();
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		moneyarguments.clear();
		bankarguments.clear();
		ecoarguments.clear();
		HandlerList.unregisterAll(this);
		if (yamlHandler.get().getBoolean("Mysql.Status", false) == true)
		{
			if (mysqlSetup.getConnection() != null) 
			{
				mysqlSetup.closeConnection();
			}
		}
		log.info(pluginName + " is disabled!");
	}
	
	public AdvanceEconomy()
	{
		plugin = this;
	}

	public static AdvanceEconomy getPlugin()
	{
		return plugin;
	}
	
	public YamlHandler getYamlHandler() 
	{
		return yamlHandler;
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

	public CommandHelper getCommandHelper()
	{
		return commandHelper;
	}
	
	public static LoggerApi getLoggerApi()
	{
		return loggerApi;
	}

	public void CommandSetup()
	{
		new ARGEcoDeleteLog(plugin);
		new ARGEcoPlayer(plugin);
		new ARGEcoReComment(plugin);
		getCommand("econ").setExecutor(new MultipleCommandExecutor(plugin));
		getCommand("econ").setTabCompleter(new TABCompletion());
		
		//new ARGMoneyBankPay(this); Not implemented, because the bank system is following
		new ARGMoneyFilterLogBetween(plugin);
		new ARGMoneyFilterLogComment(plugin);
		new ARGMoneyFilterLogCommentAscending(plugin);
		new ARGMoneyFilterLogCommentDescending(plugin);
		new ARGMoneyFilterLogFrom(plugin);
		new ARGMoneyFilterLogGreaterThan(plugin);
		new ARGMoneyFilterLogLessThan(plugin);
		new ARGMoneyFilterLogOrderer(plugin);
		new ARGMoneyFilterLogSortAscending(plugin);
		new ARGMoneyFilterLogSortDescending(plugin);
		new ARGMoneyFilterLogTo(plugin);
		new ARGMoneyFreeze(plugin);
		//new ARGMoneyGetTotal(plugin);
		new ARGMoneyGive(plugin);
		new ARGMoneyLog(plugin);
		new ARGMoneyPay(plugin);
		new ARGMoneyTake(plugin);
		new ARGMoneyToggle(plugin);
		new ARGMoneyTop(plugin);
		new ARGMoneyTrendDiagram(plugin);
		new ARGMoneyTrendGrafic(plugin);
		new ARGMoneyTrendLog(plugin);
		getCommand("money").setExecutor(new MultipleCommandExecutor(plugin));
		getCommand("money").setTabCompleter(new TABCompletion());
	}
	
	public void ListenerSetup()
	{
		PluginManager pm = getServer().getPluginManager();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "advanceeconomy:spigottobungee");
		pm.registerEvents(new PlayerListener(plugin), plugin);
		pm.registerEvents(new LoggerListener(), plugin);
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
	}
	
	public boolean reload()
	{
		if(!yamlHandler.loadYamlHandler())
		{
			return false;
		}
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlSetup.closeConnection();
			if(!mysqlHandler.loadMysqlHandler())
			{
				return false;
			}
			if(!mysqlSetup.loadMysqlSetup())
			{
				return false;
			}
		} else
		{
			return false;
		}
		EconomySettings.initSettings(this);
		return true;
	}
	
	public static VaultApi getVaultApi()
	{
		return vaultApi;
	}

	private boolean setupEconomy() 
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
		log.severe("Vault is not set in the Plugin " + pluginName + "!");
		Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(this);
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