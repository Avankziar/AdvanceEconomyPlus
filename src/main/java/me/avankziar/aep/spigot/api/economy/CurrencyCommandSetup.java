package main.java.me.avankziar.aep.spigot.api.economy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.AepCommandExecutor;
import main.java.me.avankziar.aep.spigot.cmd.LoanCommandExecutor;
import main.java.me.avankziar.aep.spigot.cmd.StandingOrderCommandExecutor;
import main.java.me.avankziar.aep.spigot.cmd.TABCompletion;
import main.java.me.avankziar.aep.spigot.cmd.cet.account.AccountClose;
import main.java.me.avankziar.aep.spigot.cmd.cet.account.AccountManage;
import main.java.me.avankziar.aep.spigot.cmd.cet.account.AccountOpen;
import main.java.me.avankziar.aep.spigot.cmd.cet.account.AccountOverdue;
import main.java.me.avankziar.aep.spigot.cmd.cet.account.AccountSetDefault;
import main.java.me.avankziar.aep.spigot.cmd.cet.account.AccountSetName;
import main.java.me.avankziar.aep.spigot.cmd.cet.account.AccountSetOwner;
import main.java.me.avankziar.aep.spigot.cmd.cet.account.AccountSetQuickPay;
import main.java.me.avankziar.aep.spigot.cmd.cet.account.Accounts;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.ActionLog;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.BankNotification;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.DeleteAllPlayerAccounts;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.DeleteLog;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.GetTotal;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.Players;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.Recomment;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.TopList;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.TrendLog;
import main.java.me.avankziar.aep.spigot.cmd.cet.base.WalletNotification;
import main.java.me.avankziar.aep.spigot.cmd.cet.loggersettings.LoggerSettings;
import main.java.me.avankziar.aep.spigot.cmd.cet.loggersettings.LoggerSettingsGui;
import main.java.me.avankziar.aep.spigot.cmd.cet.loggersettings.LoggerSettingsOther;
import main.java.me.avankziar.aep.spigot.cmd.cet.loggersettings.LoggerSettingsText;
import main.java.me.avankziar.aep.spigot.cmd.cst.Balance;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.Give;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.GiveConsole;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.Pay;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.PayThroughGui;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.Set;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.SetConsole;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.Take;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.TakeConsole;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanAccept;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanAmount;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanCancel;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanCreate;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanInfo;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanInherit;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanList;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanPause;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanPayback;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanReject;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanForgive;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanRepay;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanSend;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanTime;
import main.java.me.avankziar.aep.spigot.cmd.loan.LoanTransfer;
import main.java.me.avankziar.aep.spigot.cmd.standingorder.StandingOrderCancel;
import main.java.me.avankziar.aep.spigot.cmd.standingorder.StandingOrderCreate;
import main.java.me.avankziar.aep.spigot.cmd.standingorder.StandingOrderDelete;
import main.java.me.avankziar.aep.spigot.cmd.standingorder.StandingOrderInfo;
import main.java.me.avankziar.aep.spigot.cmd.standingorder.StandingOrderList;
import main.java.me.avankziar.aep.spigot.cmd.standingorder.StandingOrderPause;
import main.java.me.avankziar.aep.spigot.cmd.standingorder.StandingOrderRepeatingtime;
import main.java.me.avankziar.aep.spigot.cmd.standingorder.StandingOrderStarttime;
import main.java.me.avankziar.aep.spigot.cmd.standingorder.StandingOrderAmount;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.object.ne_w.AEPUser;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class CurrencyCommandSetup
{
	private AdvancedEconomyPlus plugin;
	private ArrayList<String> playerarray = new ArrayList<>();
	private LinkedHashMap<Integer, ArrayList<String>> pMapI = new LinkedHashMap<>();
	private LinkedHashMap<Integer, ArrayList<String>> pMapII = new LinkedHashMap<>();
	private LinkedHashMap<Integer, ArrayList<String>> pMapIII = new LinkedHashMap<>();
	private LinkedHashMap<Integer, ArrayList<String>> pMapIV = new LinkedHashMap<>();
	private LinkedHashMap<Integer, ArrayList<String>> pMapV = new LinkedHashMap<>();
	
	public CurrencyCommandSetup(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
		setupPlayers();
		playerarray = getPlayers();
		
		Collections.sort(playerarray);
		pMapI.put(1, playerarray);
		pMapII.put(2, playerarray);
		pMapIII.put(3, playerarray);
		pMapIV.put(4, playerarray);
		pMapV.put(5, playerarray);
	}
	
	public void setupCommand()
	{		
		for(EconomyCurrency ec : plugin.getIFHApi().currencyHandler.digitalCurrencies)
		{
			YamlConfiguration y = plugin.getYamlHandler().getCurrency(ec.getUniqueName());
			CommandStructurType cst;
			try
			{
				cst = CommandStructurType.valueOf(y.getString("Commands.StructurType"));
			} catch(Exception e)
			{
				continue;
			}
			if(cst == CommandStructurType.SINGLE)
			{
				setupSingle(ec, y);
			} else
			{
				setupNested(ec, y);
			}
		}
		if(plugin.getYamlHandler().getCom().get("Commands.SINGLE") != null)
		{
			CommandStructurType cst = CommandStructurType.SINGLE;
			for(String s : plugin.getYamlHandler().getCom().getStringList("Commands.SINGLE"))
			{
				String[] sp = s.split(";");
				if(sp.length != 2)
				{
					return;
				}
				CommandExecuteType cet;
				String cmdpath;
				try
				{
					cet = CommandExecuteType.valueOf(sp[0]);
					cmdpath = sp[1];
				} catch(Exception e)
				{
					return;
				}
				CommandConstructor cmd = new CommandConstructor(cet, cmdpath, false);
				plugin.registerCommand(cmd.getPath(), cmd.getName());
				switch(cet)
				{
				default:
					break;
				case ACTIONLOG:
					plugin.getCommand(cmd.getName()).setExecutor(new ActionLog(cmd, null, cst));
					break;
				case TRENDLOG:
					plugin.getCommand(cmd.getName()).setExecutor(new TrendLog(cmd, null, cst));
					break;
				case WALLETNOTIFICATION:
					plugin.getCommand(cmd.getName()).setExecutor(new WalletNotification(cmd, null, cst));
					break;
				case BANKNOTIFICATION:
					plugin.getCommand(cmd.getName()).setExecutor(new BankNotification(cmd, null, cst));
					break;
				case CURRENCY_INFO:
					break;
				case CURRENCY_EXCHANGE:
					break;
				case GETTOTAL:
					plugin.getCommand(cmd.getName()).setExecutor(new GetTotal(cmd, null, cst));
					break;
				case TOPLIST:
					plugin.getCommand(cmd.getName()).setExecutor(new TopList(cmd, null, cst));
					break;
				}
				plugin.getCommand(cmd.getName()).setTabCompleter(new TABCompletion());
			}
			
		}
		ArrayList<ArgumentConstructor> arglist = new ArrayList<>();
		if(plugin.getYamlHandler().getCom().get("Commands.NESTED") != null)
		{
			CommandStructurType cst = CommandStructurType.NESTED;
			for(String s : plugin.getYamlHandler().getCom().getStringList("Commands.NESTED"))
			{
				String[] sp = s.split(";");
				if(sp.length != 2)
				{
					return;
				}
				CommandExecuteType cet;
				String cmdpath;
				try
				{
					cet = CommandExecuteType.valueOf(sp[0]);
					cmdpath = sp[1];
				} catch(Exception e)
				{
					return;
				}
				ArgumentConstructor arg = null;
				switch(cet)
				{
				default:
					break;
				case ACTIONLOG:
					arg = new ArgumentConstructor(cet, cmdpath, 0, 0, 4, false, null);
					new ActionLog(null, arg, cst);
					break;
				case TRENDLOG:
					arg = new ArgumentConstructor(cet, cmdpath, 0, 0, 4, false, null);
					new TrendLog(null, arg, cst);
					break;
				case WALLETNOTIFICATION:
					arg = new ArgumentConstructor(cet, cmdpath, 0, 0, 0, false, null);
					new WalletNotification(null, arg, cst);
					break;
				case BANKNOTIFICATION:
					arg = new ArgumentConstructor(cet, cmdpath, 0, 0, 0, false, null);
					new BankNotification(null, arg, cst);
					break;
				case CURRENCY_INFO:
					break;
				case CURRENCY_EXCHANGE:
					break;
				case GETTOTAL:
					arg = new ArgumentConstructor(cet, cmdpath, 0, 0, 0, false, null);
					new GetTotal(null, arg, cst);
					break;
				case TOPLIST:
					arg = new ArgumentConstructor(cet, cmdpath, 0, 0, 0, false, null);
					new TopList(null, arg, cst);
					break;
				}
				arglist.add(arg);
			}
		}
		addBaseArgumentConstructor(arglist);
		addLoggerSettingsArgumentConstructor(arglist);
		addAccountArgumentConstructor(arglist);
		
		CommandConstructor aep = new CommandConstructor(CommandExecuteType.AEP, "aep", false, (ArgumentConstructor[]) arglist.toArray());
		plugin.registerCommand(aep.getPath(), aep.getName());
		plugin.getCommand(aep.getName()).setExecutor(new AepCommandExecutor(aep));
		plugin.getCommand(aep.getName()).setTabCompleter(new TABCompletion());
		
		if(ConfigHandler.isLoanEnabled())
		{
			addLoanArgumentConstructor();
		}
		if(ConfigHandler.isStandingOrderEnabled())
		{
			addStandingOrderArgumentConstructor();
		}
	}
	
	private void addBaseArgumentConstructor(ArrayList<ArgumentConstructor> arglist)
	{		
		ArgumentConstructor deletelog = new ArgumentConstructor(
				CommandExecuteType.DELETELOG, "aep_deletelog", 0, 1, 1, false, null);
		new DeleteLog(deletelog);
		arglist.add(deletelog);
		
		ArgumentConstructor deleteallplayeraccounts = new ArgumentConstructor(
				CommandExecuteType.DELETEALLPLAYERACCOUNTS, "aep_deleteallplayeraccounts", 0, 1, 1, false, null);
		new DeleteAllPlayerAccounts(deleteallplayeraccounts);
		arglist.add(deleteallplayeraccounts);
		
		ArgumentConstructor player = new ArgumentConstructor(
				CommandExecuteType.PLAYER, "aep_player", 0, 0, 2, false, null);
		new Players(player);
		arglist.add(player);
		
		ArgumentConstructor recomment = new ArgumentConstructor(
				CommandExecuteType.RECOMMENT, "aep_recomment", 0, 3, 999, false, null);
		new Recomment(recomment);
		arglist.add(recomment);
	}
	
	private void addLoggerSettingsArgumentConstructor(ArrayList<ArgumentConstructor> arglist)
	{
		ArgumentConstructor lsgui = new ArgumentConstructor(
				CommandExecuteType.LOGGERSETTINGS_GUI, "aep_loggersettings_gui", 1, 2, 5, false, null);
		new LoggerSettingsGui(lsgui);
		
		ArgumentConstructor lsother = new ArgumentConstructor(
				CommandExecuteType.LOGGERSETTINGS_OTHER, "aep_loggersettings_other", 1, 2, 2, false, null);
		new LoggerSettingsOther(lsother);
		
		ArgumentConstructor lstext = new ArgumentConstructor(
				CommandExecuteType.LOGGERSETTINGS_TEXT, "aep_loggersettings_text", 1, 2, 999, false, null);
		new LoggerSettingsText(lstext);
		
		ArgumentConstructor ls = new ArgumentConstructor(
				CommandExecuteType.LOGGERSETTINGS, "aep_loggersettings", 0, 0, 0, false, null,
				lstext, lsother, lsgui);
		arglist.add(ls);
		new LoggerSettings(ls);
	}
	
	private void addAccountArgumentConstructor(ArrayList<ArgumentConstructor> arglist)
	{
		ArgumentConstructor accountclose = new ArgumentConstructor(
				CommandExecuteType.ACCOUNT_CLOSE, "aep_account_close", 1, 2, 4, false, null);
		ArgumentConstructor accountmanage = new ArgumentConstructor(
				CommandExecuteType.ACCOUNT_MANAGE, "aep_account_manage", 1, 6, 6, false, null);
		ArgumentConstructor accountopen = new ArgumentConstructor(
				CommandExecuteType.ACCOUNT_OPEN, "aep_account_open", 1, 6, 8, false, null);
		ArgumentConstructor accountoverdue = new ArgumentConstructor(
				CommandExecuteType.ACCOUNT_OVERDUE, "aep_account_overdue", 1, 1, 1, false, null);
		ArgumentConstructor accountsetdefault = new ArgumentConstructor(
				CommandExecuteType.ACCOUNT_SETDEFAULT, "aep_account_setdefault", 1, 4, 4, false, null);
		ArgumentConstructor accountsetname = new ArgumentConstructor(
				CommandExecuteType.ACCOUNT_SETNAME, "aep_account_setname", 1, 5, 5, false, null);
		ArgumentConstructor accountsetowner = new ArgumentConstructor(
				CommandExecuteType.ACCOUNT_SETOWNER, "aep_account_setowner", 1, 5, 5, false, null);
		ArgumentConstructor accountsetquickpay = new ArgumentConstructor(
				CommandExecuteType.ACCOUNT_SETQUICKPAY, "aep_account_setquickpay", 1, 3, 3, false, null);
		
		ArgumentConstructor account = new ArgumentConstructor(
				CommandExecuteType.ACCOUNT, "aep_account", 0, 0, 0, false, null,
				accountclose, accountmanage, accountopen, accountoverdue, accountsetdefault, accountsetname,
				accountsetowner, accountsetquickpay);
		arglist.add(account);
		new Accounts(account);
		new AccountClose(accountclose);
		new AccountManage(accountmanage);
		new AccountOpen(accountopen);
		new AccountOverdue(accountoverdue);
		new AccountSetDefault(accountsetdefault);
		new AccountSetName(accountsetname);
		new AccountSetOwner(accountsetowner);
		new AccountSetQuickPay(accountsetquickpay);
	}
	
	private void addLoanArgumentConstructor()
	{
		String bcmdIV = "loan";
		ArgumentConstructor accept = new ArgumentConstructor(CommandExecuteType.LOAN_ACCEPT, bcmdIV+"_accept", 0, 0, 1, false, null);
		ArgumentConstructor amount = new ArgumentConstructor(CommandExecuteType.LOAN_AMOUNT, bcmdIV+"_amount", 0, 3, 3, false, null);
		ArgumentConstructor cancel = new ArgumentConstructor(CommandExecuteType.LOAN_CANCEL, bcmdIV+"_cancel", 0, 0, 0, false, null);
		ArgumentConstructor create = new ArgumentConstructor(CommandExecuteType.LOAN_CREATE, bcmdIV+"_create", 0, 4, 4, false, pMapIII);
		ArgumentConstructor forgive = new ArgumentConstructor(CommandExecuteType.LOAN_FORGIVE, bcmdIV+"_forgive", 0, 1, 2, false, null);
		ArgumentConstructor info = new ArgumentConstructor(CommandExecuteType.LOAN_INFO, bcmdIV+"_info", 0, 1, 1, false, null);
		ArgumentConstructor inherit = new ArgumentConstructor(CommandExecuteType.LOAN_INHERIT, bcmdIV+"_inherit", 0, 4, 4, false, pMapIII);
		ArgumentConstructor list = new ArgumentConstructor(CommandExecuteType.LOAN_LIST, bcmdIV+"_list", 0, 0, 1, false, null);
		ArgumentConstructor pause = new ArgumentConstructor(CommandExecuteType.LOAN_PAUSE, bcmdIV+"_pause", 0, 1, 1, false, null);
		ArgumentConstructor payback = new ArgumentConstructor(CommandExecuteType.LOAN_PAYBACK, bcmdIV+"_payback", 0, 1, 1, false, null);
		ArgumentConstructor reject = new ArgumentConstructor(CommandExecuteType.LOAN_REJECT, bcmdIV+"_reject", 0, 0, 0, false, null);
		
		ArgumentConstructor repay = new ArgumentConstructor(CommandExecuteType.LOAN_REPAY, bcmdIV+"_repay", 0, 2, 3, false, null);
		ArgumentConstructor send = new ArgumentConstructor(CommandExecuteType.LOAN_SEND, bcmdIV+"_send", 0, 1, 1, false, null);
		ArgumentConstructor time = new ArgumentConstructor(CommandExecuteType.LOAN_TIME, bcmdIV+"_time", 0, 3, 3, false, null);
		ArgumentConstructor transfer = new ArgumentConstructor(CommandExecuteType.LOAN_TRANSFER, bcmdIV+"_transfer", 0, 4, 4, false, pMapIII);
				
		CommandConstructor loan = new CommandConstructor(CommandExecuteType.LOAN, bcmdIV, false,
				accept, amount, cancel, create, info, inherit, 
				list, pause, payback, reject, forgive, repay, send, time, transfer);	
		
		plugin.registerCommand(loan.getPath(), loan.getName());
		plugin.getCommand(loan.getName()).setExecutor(new LoanCommandExecutor(loan));
		plugin.getCommand(loan.getName()).setTabCompleter(new TABCompletion());
		
		new LoanAccept(accept);
		new LoanAmount(amount);
		new LoanCancel(cancel);
		new LoanCreate(create);
		new LoanInfo(info);
		new LoanInherit(inherit);
		new LoanList(list);
		new LoanPause(pause);
		new LoanPayback(payback);
		new LoanReject(reject);
		new LoanForgive(forgive);
		new LoanRepay(repay);
		new LoanSend(send);
		new LoanTime(time);
		new LoanTransfer(transfer);
	}
	
	private void addStandingOrderArgumentConstructor()
	{
		LinkedHashMap<Integer, ArrayList<String>> playerMapII_III = new LinkedHashMap<>();			
		playerMapII_III.put(2, playerarray);
		playerMapII_III.put(3, playerarray);
		
		String bcmdV = "standingorder";
		ArgumentConstructor amount = new ArgumentConstructor(CommandExecuteType.STORDER_AMOUNT, bcmdV+"_amount", 0, 1, 2, false, null);
		ArgumentConstructor cancel = new ArgumentConstructor(CommandExecuteType.STORDER_CANCEL, bcmdV+"_cancel", 0, 0, 0, false, null);
		ArgumentConstructor create = new ArgumentConstructor(CommandExecuteType.STORDER_CREATE, bcmdV+"_create", 0, 3, 3, false, null);
		ArgumentConstructor delete = new ArgumentConstructor(CommandExecuteType.STORDER_DELETE, bcmdV+"_delete", 0, 1, 1, false, null);
		ArgumentConstructor info = new ArgumentConstructor(CommandExecuteType.STORDER_INFO, bcmdV+"_info", 0, 0, 1, false, null);
		ArgumentConstructor list = new ArgumentConstructor(CommandExecuteType.STORDER_LIST, bcmdV+"_list", 0, 0, 2, false, pMapII);
		ArgumentConstructor pause = new ArgumentConstructor(CommandExecuteType.STORDER_PAUSE, bcmdV+"_pause", 0, 1, 1, false, null);
		ArgumentConstructor rt = new ArgumentConstructor(CommandExecuteType.STORDER_REPEATINGTIME, bcmdV+"_repeatingtime", 0, 1, 2, false, null);
		ArgumentConstructor st = new ArgumentConstructor(CommandExecuteType.STORDER_STARTTIME, bcmdV+"_starttime", 0, 1, 1, false, null);
		
		CommandConstructor standingorder = new CommandConstructor(CommandExecuteType.STORDER, bcmdV, false,
				amount, cancel, create, delete, info,
				list, pause, rt, st);
		
		plugin.registerCommand(standingorder.getPath(), standingorder.getName());
		plugin.getCommand(standingorder.getName()).setExecutor(new StandingOrderCommandExecutor(standingorder));
		plugin.getCommand(standingorder.getName()).setTabCompleter(new TABCompletion());
		
		new StandingOrderAmount(amount);
		new StandingOrderCancel(cancel);
		new StandingOrderCreate(create);
		new StandingOrderDelete(delete);
		new StandingOrderInfo(info);
		new StandingOrderList(list);
		new StandingOrderPause(pause);
		new StandingOrderRepeatingtime(rt);
		new StandingOrderStarttime(st);
	}
	
	public void setupSingle(EconomyCurrency ec, YamlConfiguration y)
	{
		if(y.get("Commands.SINGLE") == null)
		{
			return;
		}
		CommandStructurType cst = CommandStructurType.SINGLE;
		for(String s : y.getStringList("Commands.SINGLE"))
		{
			String[] sp = s.split(";");
			if(sp.length != 2)
			{
				return;
			}
			CommandExecuteType cet;
			String cmdpath;
			try
			{
				cet = CommandExecuteType.valueOf(sp[0]);
				cmdpath = sp[1];
			} catch(Exception e)
			{
				return;
			}
			CommandConstructor cmd = new CommandConstructor(cet, cmdpath, false);
			plugin.registerCommand(cmd.getPath(), cmd.getName());
			switch(cet)
			{
			case BALANCE:
				plugin.getCommand(cmd.getName()).setExecutor(new Balance(cmd, cst));
				break;
			case PAY:
				plugin.getCommand(cmd.getName()).setExecutor(new Pay(cmd, null, cst));
				break;
			case PAY_THROUGH_GUI:
				plugin.getCommand(cmd.getName()).setExecutor(new PayThroughGui(cmd, null, cst));
				break;
			case GIVE:
				plugin.getCommand(cmd.getName()).setExecutor(new Give(cmd, null, cst));
				break;
			case GIVE_CONSOLE:
				plugin.getCommand(cmd.getName()).setExecutor(new GiveConsole(cmd, null, cst));
				break;
			case TAKE:
				plugin.getCommand(cmd.getName()).setExecutor(new Take(cmd, null, cst));
				break;
			case TAKE_CONSOLE:
				plugin.getCommand(cmd.getName()).setExecutor(new TakeConsole(cmd, null, cst));
				break;
			case SET:
				plugin.getCommand(cmd.getName()).setExecutor(new Set(cmd, null, cst));
				break;
			case SET_CONSOLE:
				plugin.getCommand(cmd.getName()).setExecutor(new SetConsole( cmd, null, cst));
				break;
			default:
				break;
			}
			plugin.getCommand(cmd.getName()).setTabCompleter(new TABCompletion());
		}
	}
	
	public void setupNested(EconomyCurrency ec, YamlConfiguration y)
	{
		if(y.get("Commands.NESTED") == null)
		{
			return;
		}
		ArrayList<ArgumentConstructor> arglist = new ArrayList<>();
		CommandStructurType cst = CommandStructurType.NESTED;
		for(String s : y.getStringList("Commands.NESTED"))
		{
			String[] sp = s.split(";");
			if(sp.length != 2)
			{
				return;
			}
			CommandExecuteType cet;
			String cmdpath;
			try
			{
				cet = CommandExecuteType.valueOf(sp[0]);
				cmdpath = sp[1];
			} catch(Exception e)
			{
				return;
			}
			ArgumentConstructor arg = null;
			switch(cet)
			{
			case PAY:
				arg = new ArgumentConstructor(cet, cmdpath, 0, 3, 999, false, null);
				new Pay(null, arg, cst);
				break;
			case PAY_THROUGH_GUI:
				arg = new ArgumentConstructor(cet, cmdpath, 0, 3, 999, false, null);
				new PayThroughGui(null, arg, cst);
				break;
			case GIVE:
				arg = new ArgumentConstructor(cet, cmdpath, 0, 3, 999, false, null);
				new Give(null, arg, cst);
				break;
			case GIVE_CONSOLE:
				arg = new ArgumentConstructor(cet, cmdpath, 0, 3, 999, true, null);
				new GiveConsole(null, arg, cst);
				break;
			case SET:
				arg = new ArgumentConstructor(cet, cmdpath, 0, 3, 999, false, null);
				new Set(null, arg, cst);
				break;
			case SET_CONSOLE:
				arg = new ArgumentConstructor(cet, cmdpath, 0, 3, 999, true, null);
				new SetConsole(null, arg, cst);
				break;
			case TAKE:
				arg = new ArgumentConstructor(cet, cmdpath, 0, 3, 999, false, null);
				new Take(null, arg, cst);
				break;
			case TAKE_CONSOLE:
				arg = new ArgumentConstructor(cet, cmdpath, 0, 3, 999, true, null);
				new TakeConsole(null, arg, cst);
				break;
			default:
				break;
			}
			arglist.add(arg);
		}
		
		for(String s : y.getStringList("Commands.NESTED"))
		{
			String[] sp = s.split(";");
			if(sp.length != 2)
			{
				return;
			}
			CommandExecuteType cet;
			String cmdpath;
			try
			{
				cet = CommandExecuteType.valueOf(sp[0]);
				cmdpath = sp[1];
			} catch(Exception e)
			{
				return;
			}
			CommandConstructor cmd = new CommandConstructor(cet, cmdpath, false, (ArgumentConstructor[]) arglist.toArray());
			plugin.registerCommand(cmd.getPath(), cmd.getName());
			switch(cet)
			{
			default:
				break;
			case BALANCE:
				plugin.getCommand(cmd.getName()).setExecutor(new Balance(cmd, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TABCompletion());
				CommandSuggest.set(ec, cet, cmd.getCommandString());
				break;
			}
		}
	}
	
	private void setupPlayers()
	{
		ArrayList<AEPUser> cu = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.PLAYERDATA,
						"`id`", 0,
						plugin.getMysqlHandler().lastID(MysqlHandler.Type.PLAYERDATA)));
		ArrayList<String> cus = new ArrayList<>();
		for(AEPUser chus : cu) 
		{
			cus.add(chus.getName());	
		}
		Collections.sort(cus);
		setPlayers(cus);
	}
	
	public ArrayList<String> getPlayers()
	{
		return playerarray;
	}
	
	public void setPlayers(ArrayList<String> players)
	{
		this.playerarray = players;
	}
}
