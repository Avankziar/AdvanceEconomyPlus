package main.java.me.avankziar.aep.spigot;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import main.java.me.avankziar.aep.spigot.api.VaultApi;
import main.java.me.avankziar.aep.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.bstats.Metrics;
import main.java.me.avankziar.aep.spigot.cmd.EcoCommandExecutor;
import main.java.me.avankziar.aep.spigot.cmd.MoneyCommandExecutor;
import main.java.me.avankziar.aep.spigot.cmd.TABCompletion;
import main.java.me.avankziar.aep.spigot.cmd.eco.ARGEcoDeleteLog;
import main.java.me.avankziar.aep.spigot.cmd.eco.ARGEcoPlayer;
import main.java.me.avankziar.aep.spigot.cmd.eco.ARGEcoReComment;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyFreeze;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyGive;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyPay;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneySet;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyTake;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyToggle;
import main.java.me.avankziar.aep.spigot.cmd.money.ARGMoneyTop;
import main.java.me.avankziar.aep.spigot.cmd.money.action.ARGMoneyActionLog;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Accept;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Amount;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Cancel;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Create;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Forgive;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Info;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Inherit;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_List;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Pause;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Payback;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Reject;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Repay;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Send;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Time;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoan_Transfer;
import main.java.me.avankziar.aep.spigot.cmd.money.loan.ARGMoneyLoans;
import main.java.me.avankziar.aep.spigot.cmd.money.loggersettings.ARGMoneyLoggerSettings;
import main.java.me.avankziar.aep.spigot.cmd.money.loggersettings.ARGMoneyLoggerSettings_GUI;
import main.java.me.avankziar.aep.spigot.cmd.money.loggersettings.ARGMoneyLoggerSettings_Other;
import main.java.me.avankziar.aep.spigot.cmd.money.loggersettings.ARGMoneyLoggerSettings_Text;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder_Amount;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder_Cancel;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder_Create;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder_Delete;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder_Info;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder_List;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder_Pause;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder_Repeatingtime;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrder_Starttime;
import main.java.me.avankziar.aep.spigot.cmd.money.standingorder.ARGMoneyStandingOrders;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.ARGMoneyTrendLog;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlSetup;
import main.java.me.avankziar.aep.spigot.database.YamlHandler;
import main.java.me.avankziar.aep.spigot.database.YamlManager;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.hook.ChestShopHook;
import main.java.me.avankziar.aep.spigot.hook.HeadDatabaseHook;
import main.java.me.avankziar.aep.spigot.hook.JobsHook;
import main.java.me.avankziar.aep.spigot.hook.QuickShopHook;
import main.java.me.avankziar.aep.spigot.listener.LoggerListener;
import main.java.me.avankziar.aep.spigot.listener.PlayerListener;
import main.java.me.avankziar.aep.spigot.listenerhandler.LoggerSettingsListenerHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class AdvancedEconomyPlus extends JavaPlugin
{
	public static Logger log;
	public String pluginName = "AdvancedEconomyPlus";
	private static YamlManager yamlManager;
	private static YamlHandler yamlHandler;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	private static Utility utility;
	private static AdvancedEconomyPlus plugin;
	private static VaultApi vaultApi;
	private static BackgroundTask backgroundTask;
	private static LoggerApi loggerApi;
	
	private ArrayList<String> players = new ArrayList<>();
	
	private ArrayList<CommandConstructor> commandTree;
	private ArrayList<BaseConstructor> helpList;
	private LinkedHashMap<String, ArgumentModule> argumentMap;
	public static String baseCommandI = "eco"; //Pfad angabe + ürspungliches Commandname
	public static String baseCommandII = "money";
	public static String baseCommandIII = "bank";
	
	public static String baseCommandIName = ""; //CustomCommand name
	public static String baseCommandIIName = "";
	public static String baseCommandIIIName = "";
	
	public static String infoCommandPath = "CmdEco";
	public static String infoCommand = "/"; //InfoComamnd
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		
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
		EconomySettings.initSettings(plugin); //TODO Weil EcoPlayerHandler die Methode generaliesiert wurde, müssen alle Übergabe Werte kontrolliert werden
		backgroundTask = new BackgroundTask(this);
		loggerApi = new LoggerApi(this);
		new BungeeBridge(this);
		setupEconomy();
		setupStrings();
		setupCommandTree();
		ListenerSetup();
		setupBstats();
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
		//Hier baseCommands
		baseCommandIName = plugin.getYamlHandler().getCom().getString(baseCommandI+".Name");
		baseCommandIIName = plugin.getYamlHandler().getCom().getString(baseCommandII+".Name");
		baseCommandIIIName = plugin.getYamlHandler().getCom().getString(baseCommandIII+".Name");
		
		//Zuletzt infoCommand deklarieren
		infoCommand += baseCommandIName;
	}
	
	private void setupCommandTree()
	{
		LinkedHashMap<Integer, ArrayList<String>> playerMapI = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> playerMapII = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> playerMapIII = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> playerMapIV = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> playerMapV = new LinkedHashMap<>();
		
		setupPlayers();
		ArrayList<String> playerarray = getPlayers();
		
		Collections.sort(playerarray);
		playerMapI.put(1, playerarray);
		playerMapII.put(2, playerarray);
		playerMapIII.put(3, playerarray);
		playerMapIV.put(4, playerarray);
		playerMapV.put(5, playerarray);
		
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
		
		ArgumentConstructor actionlog = new ArgumentConstructor(yamlHandler, baseCommandII+"_actionlog", 0, 0, 2, false, playerMapII);
		
		ArgumentConstructor freeze = new ArgumentConstructor(yamlHandler, baseCommandII+"_freeze", 0, 1, 1, false, playerMapI);
		ArgumentConstructor give = new ArgumentConstructor(yamlHandler, baseCommandII+"_give", 0, 4, 999, true, playerMapI);
		
		ArgumentConstructor loan_accept = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_accept", 1, 1, 2, false, null);
		ArgumentConstructor loan_amount = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_amount", 1, 4, 4, false, null);
		ArgumentConstructor loan_cancel = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_cancel", 1, 1, 1, false, null);
		ArgumentConstructor loan_create = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_create", 1, 4, 4, false, playerMapIV);
		ArgumentConstructor loan_forgive = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_forgive", 1, 2, 3, false, null);
		ArgumentConstructor loan_info = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_info", 1, 2, 2, false, null);
		ArgumentConstructor loan_inherit = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_inherit", 1, 4, 4, false, playerMapIV);
		ArgumentConstructor loan_list = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_list", 1, 1, 2, false, null);
		ArgumentConstructor loan_pause = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_pause", 1, 2, 2, false, null);
		ArgumentConstructor loan_payback = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_payback", 1, 2, 2, false, null);
		ArgumentConstructor loan_reject = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_reject", 1, 1, 1, false, null);
		ArgumentConstructor loan_repay = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_repay", 1, 3, 4, false, null);
		ArgumentConstructor loan_send = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_send", 1, 2, 2, false, null);
		ArgumentConstructor loan_time = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_time", 1, 4, 4, false, null);
		ArgumentConstructor loan_transfer = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan_transfer", 1, 4, 4, false, playerMapIV);
		ArgumentConstructor loan = new ArgumentConstructor(yamlHandler, baseCommandII+"_loan", 0, 0, 0, false, null,
				loan_accept, loan_amount, loan_cancel, loan_create, loan_forgive, loan_info, loan_inherit, 
				loan_list, loan_pause, loan_payback, loan_reject, loan_repay, loan_send, loan_time, loan_transfer);
		
		ArgumentConstructor loans = new ArgumentConstructor(yamlHandler, baseCommandII+"_loans", 0, 0, 2, false, null);
		
		
		ArgumentConstructor loggersettings_gui = new ArgumentConstructor(yamlHandler, baseCommandII+"_loggersettings_gui", 1, 1, 4, false, null);
		ArgumentConstructor loggersettings_other = new ArgumentConstructor(yamlHandler, baseCommandII+"_loggersettings_other", 1, 2, 2, false, null);
		ArgumentConstructor loggersettings_text = new ArgumentConstructor(yamlHandler, baseCommandII+"_loggersettings_text", 1, 2, 999, false, null);
		ArgumentConstructor loggersettings = new ArgumentConstructor(yamlHandler, baseCommandII+"_loggersettings", 0, 0, 0, false, null,
				loggersettings_gui, loggersettings_other, loggersettings_text);
		
		ArgumentConstructor pay = new ArgumentConstructor(yamlHandler, baseCommandII+"_pay", 0, 2, 999, false, playerMapI);
		ArgumentConstructor set = new ArgumentConstructor(yamlHandler, baseCommandII+"_set", 0, 2, 999, true, playerMapI);
		
		ArgumentConstructor storder_amount = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder_amount", 1, 2, 2, false, null);
		ArgumentConstructor storder_cancel = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder_cancel", 1, 1, 1, false, null);
		ArgumentConstructor storder_create = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder_create", 1, 4, 4, false, null);
		ArgumentConstructor storder_delete = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder_delete", 1, 2, 2, false, null);
		ArgumentConstructor storder_info = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder_info", 1, 1, 2, false, null);
		ArgumentConstructor storder_list = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder_list", 1, 1, 3, false, null);
		ArgumentConstructor storder_pause = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder_pause", 1, 2, 2, false, null);
		ArgumentConstructor storder_rt = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder_repeatingtime", 1, 2, 2, false, null);
		ArgumentConstructor storder_st = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder_starttime", 1, 2, 2, false, null);
		ArgumentConstructor storder = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorder", 0, 0, 0, false, null,
				storder_amount, storder_cancel, storder_create, storder_delete, storder_info,
				storder_list, storder_pause, storder_rt, storder_st);
		
		ArgumentConstructor storders = new ArgumentConstructor(yamlHandler, baseCommandII+"_standingorders", 0, 0, 0, false, null);
		
		ArgumentConstructor take = new ArgumentConstructor(yamlHandler, baseCommandII+"_take", 0, 4, 999, true, playerMapI);
		ArgumentConstructor toggle = new ArgumentConstructor(yamlHandler, baseCommandII+"_toggle", 0, 0, 0, false, null);
		ArgumentConstructor top = new ArgumentConstructor(yamlHandler, baseCommandII+"_top", 0, 0, 1, false, null);
		
		ArgumentConstructor trendlog = new ArgumentConstructor(yamlHandler, baseCommandII+"_trendlog", 0, 0, 2, false, playerMapII);
		
		CommandConstructor money = new CommandConstructor(plugin, baseCommandII, false,
				freeze, give, loggersettings, pay, set, take, toggle, top,
				actionlog, trendlog,
				loan, loans,
				storder, storders);
		
		LoggerSettingsHandler.loggerSettingsCommandString = loggersettings_gui.getCommandString();
		LoggerSettingsHandler.loggerSettingsTextCommandString = loggersettings_text.getCommandString();
		
		//CommandConstructor bank = new CommandConstructor(plugin, baseCommandIII, false);
		
		registerCommand(eco.getPath(), eco.getName());
		getCommand(eco.getName()).setExecutor(new EcoCommandExecutor(plugin, eco));
		getCommand(eco.getName()).setTabCompleter(new TABCompletion(plugin));
		
		registerCommand(money.getPath(), money.getName());
		getCommand(money.getName()).setExecutor(new MoneyCommandExecutor(plugin, money));
		getCommand(money.getName()).setTabCompleter(new TABCompletion(plugin));
		
		//registerCommand(bank.getPath(), bank.getName());
		//getCommand(bank.getName()).setExecutor(new BankCommandExecutor(plugin, bank));
		//getCommand(bank.getName()).setTabCompleter(new TABCompletion(plugin));
		
		//TODO wenn bestimmte parts deaktiviert sind, diese auch nicht bei /econ auftauchen, sowie tabcompleter
		addingHelps(
				eco, 
					deletelog, player, recomment,
				money,
					actionlog, trendlog,
					freeze, give, loggersettings, loggersettings_gui, loggersettings_other, loggersettings_text, pay, set,
					take, toggle, top//,
				//bank
				);
		if(EconomySettings.settings.isLoanRepayment())
		{
			addingHelps(
				loan, loan_accept, loan_amount, loan_cancel, loan_create, loan_forgive, loan_info, loan_inherit, loan_list,
					loan_pause, loan_payback, loan_reject, loan_repay, loan_send, loan_time, loan_transfer,
				loans);
		}
		if(EconomySettings.settings.isStandingOrder())
		{
			addingHelps(
				storder, storder_amount, storder_cancel, storder_create, storder_delete, storder_info, storder_list,
					storder_pause, storder_rt, storder_st,
				storders);
		}
		
		new ARGEcoDeleteLog(plugin, deletelog);
		new ARGEcoPlayer(plugin, player);
		new ARGEcoReComment(plugin, recomment);
		
		new ARGMoneyActionLog(plugin, actionlog);
		
		new ARGMoneyLoan(plugin, loan);
		new ARGMoneyLoan_Accept(plugin, loan_accept);
		new ARGMoneyLoan_Amount(plugin, loan_amount);
		new ARGMoneyLoan_Cancel(plugin, loan_cancel);
		new ARGMoneyLoan_Create(plugin, loan_create);
		new ARGMoneyLoan_Forgive(plugin, loan_forgive);
		new ARGMoneyLoan_Info(plugin, loan_info);
		new ARGMoneyLoan_Inherit(plugin, loan_inherit);
		new ARGMoneyLoan_List(plugin, loan_list);
		new ARGMoneyLoan_Pause(plugin, loan_pause);
		new ARGMoneyLoan_Payback(plugin, loan_payback);
		new ARGMoneyLoan_Reject(plugin, loan_reject);
		new ARGMoneyLoan_Repay(plugin, loan_repay);
		new ARGMoneyLoan_Send(plugin, loan_send);
		new ARGMoneyLoan_Time(plugin, loan_time);
		new ARGMoneyLoan_Transfer(plugin, loan_transfer);
		
		new ARGMoneyLoans(plugin, loans);
		
		new ARGMoneyFreeze(plugin, freeze);
		//new ARGMoneyGetTotal(plugin);
		new ARGMoneyGive(plugin, give);
		
		new ARGMoneyLoggerSettings(plugin, loggersettings);
		new ARGMoneyLoggerSettings_GUI(plugin, loggersettings_gui);
		new ARGMoneyLoggerSettings_Other(plugin, loggersettings_other);
		new ARGMoneyLoggerSettings_Text(plugin, loggersettings_text);
		
		new ARGMoneyPay(plugin, pay);
		new ARGMoneySet(plugin, set);
		
		new ARGMoneyStandingOrder(plugin, storder);
		new ARGMoneyStandingOrder_Amount(plugin, storder_amount);
		new ARGMoneyStandingOrder_Cancel(plugin, storder_cancel);
		new ARGMoneyStandingOrder_Create(plugin, storder_create);
		new ARGMoneyStandingOrder_Delete(plugin, storder_delete);
		new ARGMoneyStandingOrder_Info(plugin, storder_info);
		new ARGMoneyStandingOrder_List(plugin, storder_list);
		new ARGMoneyStandingOrder_Pause(plugin, storder_pause);
		new ARGMoneyStandingOrder_Repeatingtime(plugin, storder_rt);
		new ARGMoneyStandingOrder_Starttime(plugin, storder_st);
		
		new ARGMoneyStandingOrders(plugin, storders);
		
		new ARGMoneyTake(plugin, take);
		new ARGMoneyToggle(plugin, toggle);
		new ARGMoneyTop(plugin, top);
		
		new ARGMoneyTrendLog(plugin, trendlog);
	}
	
	public void ListenerSetup()
	{
		PluginManager pm = getServer().getPluginManager();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "advanceeconomy:spigottobungee");
		pm.registerEvents(new PlayerListener(plugin), plugin);
		pm.registerEvents(new LoggerListener(), plugin);
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
	
	public boolean reload()
	{
		try
		{
			if(!yamlHandler.loadYamlHandler())
			{
				return false;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(yamlHandler.getConfig().getBoolean("Mysql.Status", false))
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
	
	public ArrayList<BaseConstructor> getHelpList()
	{
		return helpList;
	}
	
	public void addingHelps(BaseConstructor... objects)
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
	
	public ArrayList<String> getPlayers()
	{
		return players;
	}

	public void setPlayers(ArrayList<String> players)
	{
		this.players = players;
	}
	
	public void setupPlayers()
	{
		ArrayList<AEPUser> cu = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.PLAYER,
						"`id`", 0,
						plugin.getMysqlHandler().lastID(MysqlHandler.Type.PLAYER)));
		ArrayList<String> cus = new ArrayList<>();
		for(AEPUser chus : cu) 
		{
			cus.add(chus.getName());	
		}
		setPlayers(cus);
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