package main.java.me.avankziar.aep.spigot;

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
import main.java.me.avankziar.aep.spigot.cmd.money.action.ARGMoneyAction;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_Between;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_Comment;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_CommentAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_CommentDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_From;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_GreaterThan;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_LessThan;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_Orderer;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_SortAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_SortDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_FilterLog_To;
import main.java.me.avankziar.aep.spigot.cmd.money.action.diagram.ARGMoneyAction_Diagram_Log;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_Between;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_Comment;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_CommentAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_CommentDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_From;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_GreaterThan;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_LessThan;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_Orderer;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_SortAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_SortDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_FilterLog_To;
import main.java.me.avankziar.aep.spigot.cmd.money.action.filter.ARGMoneyAction_Log;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_Between;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_Comment;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_CommentAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_CommentDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_From;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_GreaterThan;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_LessThan;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_Orderer;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_SortAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_SortDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_FilterLog_To;
import main.java.me.avankziar.aep.spigot.cmd.money.action.grafic.ARGMoneyAction_Grafic_Log;
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
import main.java.me.avankziar.aep.spigot.cmd.money.trend.ARGMoneyTrend;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram_FilterLog;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram_FilterLog_Between;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram_FilterLog_GreaterThan;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram_FilterLog_LessThan;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram_FilterLog_SortAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram_FilterLog_SortDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram_FilterLog_StandAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram_FilterLog_StandDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.diagram.ARGMoneyTrend_Diagram_Log;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.filter.ARGMoneyTrend_FilterLog;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.filter.ARGMoneyTrend_FilterLog_Between;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.filter.ARGMoneyTrend_FilterLog_GreaterThan;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.filter.ARGMoneyTrend_FilterLog_LessThan;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.filter.ARGMoneyTrend_FilterLog_SortAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.filter.ARGMoneyTrend_FilterLog_SortDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.filter.ARGMoneyTrend_FilterLog_StandAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.filter.ARGMoneyTrend_FilterLog_StandDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.filter.ARGMoneyTrend_Log;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic_FilterLog;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic_FilterLog_Between;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic_FilterLog_GreaterThan;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic_FilterLog_LessThan;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic_FilterLog_SortAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic_FilterLog_SortDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic_FilterLog_StandAscending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic_FilterLog_StandDescending;
import main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic.ARGMoneyTrend_Grafic_Log;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlSetup;
import main.java.me.avankziar.aep.spigot.database.YamlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.hook.ChestShopHook;
import main.java.me.avankziar.aep.spigot.hook.HeadDatabaseHook;
import main.java.me.avankziar.aep.spigot.hook.JobsHook;
import main.java.me.avankziar.aep.spigot.hook.QuickShopHook;
import main.java.me.avankziar.aep.spigot.listener.LoggerListener;
import main.java.me.avankziar.aep.spigot.listener.PlayerListener;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class AdvancedEconomyPlus extends JavaPlugin
{
	public static Logger log;
	public String pluginName = "AdvancedEconomyPlus";
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
		
		yamlHandler = new YamlHandler(this);
		utility = new Utility(this);
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
		if (yamlHandler.get().getBoolean("Mysql.Status", false) == true)
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
		
		ArgumentConstructor action_diagram_fl_bw = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_between", 
				3, 5, 7, false, null);
		ArgumentConstructor action_diagram_fl_c = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_comment", 
				3, 4, 6, false, null);
		ArgumentConstructor action_diagram_fl_casc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_commentascending", 
				3, 4, 6, false, null);
		ArgumentConstructor action_diagram_fl_cdesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_commentdescending", 
				3, 4, 6, false, null);
		ArgumentConstructor action_diagram_fl_f = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_from", 
				3, 4, 6, false, null);
		ArgumentConstructor action_diagram_fl_gt = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_greaterthan", 
				3, 4, 6, false, null);
		ArgumentConstructor action_diagram_fl_lt = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_lessthan", 
				3, 4, 6, false, null);
		ArgumentConstructor action_diagram_fl_o = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_orderer", 
				3, 4, 6, false, null);
		ArgumentConstructor action_diagram_fl_sasc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_sortascending", 
				3, 3, 5, false, null);
		ArgumentConstructor action_diagram_fl_sdesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_sortdescending", 
				3, 3, 5, false, null);
		ArgumentConstructor action_diagram_fl_t = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog_to", 
				3, 4, 6, false, null);
		ArgumentConstructor action_diagram_fl = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_filterlog", 
				2, 2, 2, false, null,
				action_diagram_fl_bw, action_diagram_fl_c, action_diagram_fl_casc, action_diagram_fl_cdesc,
				action_diagram_fl_f, action_diagram_fl_gt, action_diagram_fl_lt, action_diagram_fl_o,
				action_diagram_fl_sasc, action_diagram_fl_sdesc, action_diagram_fl_t);
		ArgumentConstructor action_diagram_log = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram_log", 
				2, 2, 4, false, null);
		ArgumentConstructor action_diagram = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_diagram", 1, 1, 1, false, null,
				action_diagram_log, action_diagram_fl);
		
		ArgumentConstructor action_log = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_log", 1, 1, 3, false, playerMapIII);
		
		ArgumentConstructor action_fl_bw = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_between",
				2, 4, 6, false, playerMapV);
		ArgumentConstructor action_fl_c = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_comment",
				2, 3, 5, false, playerMapIV);
		ArgumentConstructor action_fl_casc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_commentasc",
				2, 3, 5, false, playerMapIV);
		ArgumentConstructor action_fl_cdesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_commentdesc",
				2, 3, 5, false, playerMapIV);
		ArgumentConstructor action_fl_f = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_from",
				2, 3, 5, false, playerMapIV);
		ArgumentConstructor action_fl_gt = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_greaterthan",
				2, 3, 5, false, playerMapIV);
		ArgumentConstructor action_fl_lt = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_lessthan",
				2, 3, 5, false, playerMapIV);
		ArgumentConstructor action_fl_o = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_orderer",
				2, 3, 5, false, playerMapIV);
		ArgumentConstructor action_fl_sasc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_sortasc",
				2, 2, 4, false, playerMapIII);
		ArgumentConstructor action_fl_sdesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_sortdesc",
				2, 2, 4, false, playerMapIII);
		ArgumentConstructor action_fl_t = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog_to",
				2, 3, 5, false, playerMapIV);
		ArgumentConstructor action_fl = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_filterlog", 1, 1, 1, false, null,
				action_fl_bw, action_fl_c, action_fl_casc, action_fl_cdesc,
				action_fl_f, action_fl_gt, action_fl_lt,
				action_fl_o, action_fl_sasc, action_fl_sdesc, action_fl_t);
		
		ArgumentConstructor action_grafic_fl_bw = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_between", 
				3, 5, 7, false, null);
		ArgumentConstructor action_grafic_fl_c = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_comment", 
				3, 4, 6, false, null);
		ArgumentConstructor action_grafic_fl_casc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_commentascending", 
				3, 4, 6, false, null);
		ArgumentConstructor action_grafic_fl_cdesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_commentdescending", 
				3, 4, 6, false, null);
		ArgumentConstructor action_grafic_fl_f = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_from", 
				3, 4, 6, false, null);
		ArgumentConstructor action_grafic_fl_gt = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_greaterthan", 
				3, 4, 6, false, null);
		ArgumentConstructor action_grafic_fl_lt = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_lessthan", 
				3, 4, 6, false, null);
		ArgumentConstructor action_grafic_fl_o = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_orderer", 
				3, 4, 6, false, null);
		ArgumentConstructor action_grafic_fl_sasc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_sortascending", 
				3, 3, 5, false, null);
		ArgumentConstructor action_grafic_fl_sdesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_sortdescending", 
				3, 3, 5, false, null);
		ArgumentConstructor action_grafic_fl_t = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog_to", 
				3, 4, 6, false, null);
		ArgumentConstructor action_grafic_fl = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_filterlog", 
				2, 2, 2, false, null,
				action_grafic_fl_bw, action_grafic_fl_c, action_grafic_fl_casc, action_grafic_fl_cdesc,
				action_grafic_fl_f, action_grafic_fl_gt, action_grafic_fl_lt, action_grafic_fl_o,
				action_grafic_fl_sasc, action_grafic_fl_sdesc, action_grafic_fl_t);
		ArgumentConstructor action_grafic_log = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic_log", 
				2, 2, 4, false, null);
		ArgumentConstructor action_grafic = new ArgumentConstructor(yamlHandler, baseCommandII+"_action_grafic", 1, 1, 1, false, null,
				action_grafic_log, action_grafic_fl);
		
		ArgumentConstructor action = new ArgumentConstructor(yamlHandler, baseCommandII+"_action", 0, 0, 0, false, null,
				action_diagram, action_log, action_fl, action_grafic);
		
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
		
		ArgumentConstructor take = new ArgumentConstructor(yamlHandler, baseCommandII+"_take", 0, 4, 999, true, playerMapI);
		ArgumentConstructor toggle = new ArgumentConstructor(yamlHandler, baseCommandII+"_toggle", 0, 0, 0, false, null);
		ArgumentConstructor top = new ArgumentConstructor(yamlHandler, baseCommandII+"_top", 0, 0, 1, false, null);
		
		
		ArgumentConstructor trend_diagram_fl_bw = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram_filterlog_between",
				3, 5, 7, false, null);
		ArgumentConstructor trend_diagram_fl_gt = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram_filterlog_greatherthan",
				3, 4, 6, false, null);
		ArgumentConstructor trend_diagram_fl_lt = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram_filterlog_lessthan",
				3, 4, 6, false, null);
		ArgumentConstructor trend_diagram_fl_soasc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram_filterlog_sortascending",
				3, 3, 5, false, null);
		ArgumentConstructor trend_diagram_fl_sodesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram_filterlog_sortdescending",
				3, 3, 5, false, null);
		ArgumentConstructor trend_diagram_fl_stasc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram_filterlog_standascending",
				3, 3, 5, false, null);
		ArgumentConstructor trend_diagram_fl_stdesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram_filterlog_standdescending",
				3, 3, 5, false, null);
		ArgumentConstructor trend_diagram_fl = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram_filterlog",
				2, 2, 2, false, null,
				trend_diagram_fl_bw, trend_diagram_fl_gt, trend_diagram_fl_lt, trend_diagram_fl_soasc, trend_diagram_fl_sodesc,
				trend_diagram_fl_stasc, trend_diagram_fl_stdesc);
		ArgumentConstructor trend_diagram_log = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram_log",
				2, 2, 4, false, playerMapIV);
		ArgumentConstructor trend_diagram = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_diagram",
				1, 1, 1, false, null,
				trend_diagram_log, trend_diagram_fl);
		
		ArgumentConstructor trend_fl_bw = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_filterlog_between",
				2, 4, 6, false, null);
		ArgumentConstructor trend_fl_gt = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_filterlog_greatherthan",
				2, 3, 5, false, null);
		ArgumentConstructor trend_fl_lt = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_filterlog_lessthan",
				2, 3, 5, false, null);
		ArgumentConstructor trend_fl_soasc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_filterlog_sortascending",
				2, 2, 4, false, null);
		ArgumentConstructor trend_fl_sodesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_filterlog_sortdescending",
				2, 2, 4, false, null);
		ArgumentConstructor trend_fl_stasc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_filterlog_standascending",
				2, 2, 4, false, null);
		ArgumentConstructor trend_fl_stdesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_filterlog_standdescending",
				2, 2, 4, false, null);
		ArgumentConstructor trend_fl = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_filterlog",
				1, 1, 1, false, null,
				trend_fl_bw, trend_fl_gt, trend_fl_lt, trend_fl_soasc, trend_fl_sodesc, trend_fl_stasc, trend_fl_stdesc);
		
		ArgumentConstructor trend_grafic_fl_bw = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic_filterlog_between",
				3, 5, 7, false, null);
		ArgumentConstructor trend_grafic_fl_gt = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic_filterlog_greatherthan",
				3, 4, 6, false, null);
		ArgumentConstructor trend_grafic_fl_lt = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic_filterlog_lessthan",
				3, 4, 6, false, null);
		ArgumentConstructor trend_grafic_fl_soasc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic_filterlog_sortascending",
				3, 3, 5, false, null);
		ArgumentConstructor trend_grafic_fl_sodesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic_filterlog_sortdescending",
				3, 3, 5, false, null);
		ArgumentConstructor trend_grafic_fl_stasc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic_filterlog_standascending",
				3, 3, 5, false, null);
		ArgumentConstructor trend_grafic_fl_stdesc = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic_filterlog_standdescending",
				3, 3, 5, false, null);
		ArgumentConstructor trend_grafic_fl = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic_filterlog",
				2, 2, 2, false, null,
				trend_grafic_fl_bw, trend_grafic_fl_gt, trend_grafic_fl_lt, trend_grafic_fl_soasc, trend_grafic_fl_sodesc,
				trend_grafic_fl_stasc, trend_grafic_fl_stdesc);
		ArgumentConstructor trend_grafic_log = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic_log",
				2, 2, 4, false, playerMapIV);
		ArgumentConstructor trend_grafic = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_grafic",
				1, 1, 1, false, null,
				trend_grafic_log, trend_grafic_fl);
		
		ArgumentConstructor trend_log = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend_log", 1, 1, 3, false, playerMapIII);
		ArgumentConstructor trend = new ArgumentConstructor(yamlHandler, baseCommandII+"_trend", 0, 0, 0, false, null,
				trend_diagram, trend_fl, trend_grafic, trend_log);
		
		CommandConstructor money = new CommandConstructor(plugin, baseCommandII, false,
				action, loan, loans, freeze, give, pay, set, storder, take, toggle, top, trend);
		
		CommandConstructor bank = new CommandConstructor(plugin, baseCommandIII, false);
		
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
					action,
						action_diagram, action_diagram_log, 
							action_diagram_fl, action_diagram_fl_bw, action_diagram_fl_c, action_diagram_fl_casc, action_diagram_fl_cdesc,
								action_diagram_fl_f, action_diagram_fl_gt, action_diagram_fl_lt,
								action_diagram_fl_o, action_diagram_fl_sasc, action_diagram_fl_sdesc, action_diagram_fl_t,
						action_log, 
						action_fl, action_fl_bw, action_fl_c, action_fl_casc, action_fl_cdesc,
							action_fl_f, action_fl_gt, action_fl_lt,
							action_fl_o, action_fl_sasc, action_fl_sdesc, action_fl_t,
						action_grafic, action_grafic_log, 
							action_grafic_fl, action_grafic_fl_bw, action_grafic_fl_c, action_grafic_fl_casc, action_grafic_fl_cdesc,
								action_grafic_fl_f, action_grafic_fl_gt, action_grafic_fl_lt,
								action_grafic_fl_o, action_grafic_fl_sasc, action_grafic_fl_sdesc, action_grafic_fl_t,
					loan, loan_accept, loan_amount, loan_cancel, loan_create, loan_forgive, loan_info, loan_inherit, loan_list,
						loan_pause, loan_payback, loan_reject, loan_repay, loan_send, loan_time, loan_transfer,
					loans,
					freeze, give, pay, set,
					storder, storder_amount, storder_cancel, storder_create, storder_delete, storder_info, storder_list,
						storder_pause, storder_rt, storder_st,
					take, toggle, top,
					trend, 
						trend_diagram, trend_diagram_log,
							trend_diagram_fl, trend_diagram_fl_bw, trend_diagram_fl_gt, trend_diagram_fl_lt,
								trend_diagram_fl_soasc, trend_diagram_fl_sodesc, trend_diagram_fl_stasc, trend_diagram_fl_stdesc,
						trend_fl, trend_fl_bw, trend_fl_gt, trend_fl_lt, trend_fl_soasc, trend_fl_sodesc, trend_fl_stasc, trend_fl_stdesc,
						trend_grafic, trend_grafic_log,
							trend_grafic_fl, trend_grafic_fl_bw, trend_grafic_fl_gt, trend_grafic_fl_lt,
								trend_grafic_fl_soasc, trend_grafic_fl_sodesc, trend_grafic_fl_stasc, trend_grafic_fl_stdesc,
						trend_log,
				bank);
		
		new ARGEcoDeleteLog(plugin, deletelog);
		new ARGEcoPlayer(plugin, player);
		new ARGEcoReComment(plugin, recomment);
		
		new ARGMoneyAction(plugin, action);
		
		new ARGMoneyAction_Diagram(plugin, action_diagram);
		new ARGMoneyAction_Diagram_FilterLog(plugin, action_diagram_fl);
		new ARGMoneyAction_Diagram_FilterLog_Between(plugin, action_diagram_fl_bw);
		new ARGMoneyAction_Diagram_FilterLog_Comment(plugin, action_diagram_fl_c);
		new ARGMoneyAction_Diagram_FilterLog_CommentAscending(plugin, action_diagram_fl_casc);
		new ARGMoneyAction_Diagram_FilterLog_CommentDescending(plugin, action_diagram_fl_cdesc);
		new ARGMoneyAction_Diagram_FilterLog_From(plugin, action_diagram_fl_f);
		new ARGMoneyAction_Diagram_FilterLog_GreaterThan(plugin, action_diagram_fl_gt);
		new ARGMoneyAction_Diagram_FilterLog_LessThan(plugin, action_diagram_fl_lt);
		new ARGMoneyAction_Diagram_FilterLog_Orderer(plugin, action_diagram_fl_o);
		new ARGMoneyAction_Diagram_FilterLog_SortAscending(plugin, action_diagram_fl_sasc);
		new ARGMoneyAction_Diagram_FilterLog_SortDescending(plugin, action_diagram_fl_sdesc);
		new ARGMoneyAction_Diagram_FilterLog_To(plugin, action_diagram_fl_t);
		new ARGMoneyAction_Diagram_Log(plugin, action_diagram_log);
		
		new ARGMoneyAction_FilterLog(plugin, action_fl);
		new ARGMoneyAction_FilterLog_Between(plugin, action_fl_bw);
		new ARGMoneyAction_FilterLog_Comment(plugin, action_fl_c);
		new ARGMoneyAction_FilterLog_CommentAscending(plugin, action_fl_casc);
		new ARGMoneyAction_FilterLog_CommentDescending(plugin, action_fl_cdesc);
		new ARGMoneyAction_FilterLog_From(plugin, action_fl_f);
		new ARGMoneyAction_FilterLog_GreaterThan(plugin, action_fl_gt);
		new ARGMoneyAction_FilterLog_LessThan(plugin, action_fl_lt);
		new ARGMoneyAction_FilterLog_Orderer(plugin, action_fl_o);
		new ARGMoneyAction_FilterLog_SortAscending(plugin, action_fl_sasc);
		new ARGMoneyAction_FilterLog_SortDescending(plugin, action_fl_sdesc);
		new ARGMoneyAction_FilterLog_To(plugin, action_fl_t);
		
		new ARGMoneyAction_Grafic(plugin, action_grafic);
		new ARGMoneyAction_Grafic_FilterLog(plugin, action_fl);
		new ARGMoneyAction_Grafic_FilterLog_Between(plugin, action_grafic_fl_bw);
		new ARGMoneyAction_Grafic_FilterLog_Comment(plugin, action_grafic_fl_c);
		new ARGMoneyAction_Grafic_FilterLog_CommentAscending(plugin, action_grafic_fl_casc);
		new ARGMoneyAction_Grafic_FilterLog_CommentDescending(plugin, action_grafic_fl_cdesc);
		new ARGMoneyAction_Grafic_FilterLog_From(plugin, action_grafic_fl_f);
		new ARGMoneyAction_Grafic_FilterLog_GreaterThan(plugin, action_grafic_fl_gt);
		new ARGMoneyAction_Grafic_FilterLog_LessThan(plugin, action_grafic_fl_lt);
		new ARGMoneyAction_Grafic_FilterLog_Orderer(plugin, action_grafic_fl_o);
		new ARGMoneyAction_Grafic_FilterLog_SortAscending(plugin, action_grafic_fl_sasc);
		new ARGMoneyAction_Grafic_FilterLog_SortDescending(plugin, action_grafic_fl_sdesc);
		new ARGMoneyAction_Grafic_FilterLog_To(plugin, action_grafic_fl_t);
		new ARGMoneyAction_Grafic_Log(plugin, action_grafic_log);
		
		new ARGMoneyAction_Log(plugin, action_log);
		
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
		
		new ARGMoneyTake(plugin, take);
		new ARGMoneyToggle(plugin, toggle);
		new ARGMoneyTop(plugin, top);
		new ARGMoneyTrend(plugin, trend);
		
		new ARGMoneyTrend_Diagram(plugin, trend_diagram);
		new ARGMoneyTrend_Diagram_Log(plugin, trend_diagram_log);
		new ARGMoneyTrend_Diagram_FilterLog(plugin, trend_diagram_fl);
		new ARGMoneyTrend_Diagram_FilterLog_Between(plugin, trend_diagram_fl_bw);
		new ARGMoneyTrend_Diagram_FilterLog_GreaterThan(plugin, trend_diagram_fl_gt);
		new ARGMoneyTrend_Diagram_FilterLog_LessThan(plugin, trend_diagram_fl_lt);
		new ARGMoneyTrend_Diagram_FilterLog_SortAscending(plugin, trend_diagram_fl_soasc);
		new ARGMoneyTrend_Diagram_FilterLog_SortDescending(plugin, trend_diagram_fl_sodesc);
		new ARGMoneyTrend_Diagram_FilterLog_StandAscending(plugin, trend_diagram_fl_stasc);
		new ARGMoneyTrend_Diagram_FilterLog_StandDescending(plugin, trend_diagram_fl_stdesc);
		
		new ARGMoneyTrend_FilterLog(plugin, trend_fl);
		new ARGMoneyTrend_FilterLog_Between(plugin, trend_fl_bw);
		new ARGMoneyTrend_FilterLog_GreaterThan(plugin, trend_fl_gt);
		new ARGMoneyTrend_FilterLog_LessThan(plugin, trend_fl_lt);
		new ARGMoneyTrend_FilterLog_SortAscending(plugin, trend_fl_soasc);
		new ARGMoneyTrend_FilterLog_SortDescending(plugin, trend_fl_sodesc);
		new ARGMoneyTrend_FilterLog_StandAscending(plugin, trend_fl_stasc);
		new ARGMoneyTrend_FilterLog_StandDescending(plugin, trend_fl_stdesc);
		
		new ARGMoneyTrend_Grafic(plugin, trend_grafic);
		new ARGMoneyTrend_Grafic_Log(plugin, trend_grafic_log);
		new ARGMoneyTrend_Grafic_FilterLog(plugin, trend_grafic_fl);
		new ARGMoneyTrend_Grafic_FilterLog_Between(plugin, trend_grafic_fl_bw);
		new ARGMoneyTrend_Grafic_FilterLog_GreaterThan(plugin, trend_grafic_fl_gt);
		new ARGMoneyTrend_Grafic_FilterLog_LessThan(plugin, trend_grafic_fl_lt);
		new ARGMoneyTrend_Grafic_FilterLog_SortAscending(plugin, trend_grafic_fl_soasc);
		new ARGMoneyTrend_Grafic_FilterLog_SortDescending(plugin, trend_grafic_fl_sodesc);
		new ARGMoneyTrend_Grafic_FilterLog_StandAscending(plugin, trend_grafic_fl_stasc);
		new ARGMoneyTrend_Grafic_FilterLog_StandDescending(plugin, trend_grafic_fl_stdesc);
		
		new ARGMoneyTrend_Log(plugin, trend_log);
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
		if(existHook("QuickShop"))
		{
			log.info(pluginName+" hook with QuickShop");
			pm.registerEvents(new QuickShopHook(plugin), plugin);
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
		ArrayList<EcoPlayer> cu = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.PLAYER,
						"`id`", 0,
						plugin.getMysqlHandler().lastID(MysqlHandler.Type.PLAYER)));
		ArrayList<String> cus = new ArrayList<>();
		for(EcoPlayer chus : cu) 
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