package main.java.me.avankziar.aep.spigot.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aep.general.objects.TaxationCase;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import main.java.me.avankziar.aep.spigot.database.Language.ISO639_2B;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.general.economy.account.AccountType;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> loggerSettingsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> currencyKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfig();
		initCommands();
		initLanguage();
		initFilterSettings();
		initCurrencyFile();
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, Language> getConfigKey()
	{
		return configKeys;
	}
	
	public LinkedHashMap<String, Language> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, Language> getLoggerSettingsKey()
	{
		return loggerSettingsKeys;
	}
	
	public LinkedHashMap<String, Language> getCurrencyKey(String uniquename)
	{
		if(currencyKeys.containsKey(uniquename))
		{
			return currencyKeys.get(uniquename);
		} else
		{
			return new LinkedHashMap<>();
		}
	}
	
	public void setFileInput(YamlConfiguration yml, LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	public void initConfig()
	{
		configKeys.put("useIFHAdministration"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("IFHAdministrationPath"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"aep"}));
		configKeys.put("Language"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENG"}));
		configKeys.put("Mysql.Status"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Mysql.Host"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"127.0.0.1"}));
		configKeys.put("Mysql.Port"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				3306}));
		configKeys.put("Mysql.DatabaseName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"mydatabase"}));
		configKeys.put("Mysql.SSLEnabled"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Mysql.AutoReconnect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Mysql.VerifyServerCertificate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Mysql.User"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"admin"}));
		configKeys.put("Mysql.Password"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"not_0123456789"}));
		
		configKeys.put("Identifier.Click"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"click"}));
		configKeys.put("Identifier.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hover"}));
		configKeys.put("Seperator.BetweenFunction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"~"}));
		configKeys.put("Seperator.WhithinFuction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"@"}));
		configKeys.put("Seperator.Space"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"+"}));
		configKeys.put("Seperator.HoverNewLine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"~!~"}));
		
		configKeys.put("EnableCommands.StandingOrder"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.Loan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Enable.ConvertFromBuildThree"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Enable.CreateAccountsDespiteTheFactThatThePlayerIsRegistered"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Enable.DebugMode"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Enable.CurrencyMayUseStandingOrder"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar",
				"taler"}));
		configKeys.put("Enable.CurrencyMayUseLoanRepayment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar",
				"taler"}));
		configKeys.put("Enable.PlayerCanDeletePredefineAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar;false",
				"taler;false"}));
		configKeys.put("Load.Currencies"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar",
				"taler"}));
		//Servername,true(DefaultServerEntity)
		configKeys.put("CreateEconomyEntityIfNotExist.Server"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Dollarus;true",
				"Talerius;false"}));
		//Entity(NPc, whatever)name,true(DefaultEntityEntity)
		configKeys.put("CreateEconomyEntityIfNotExist.Entity"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Mike;true",
				"King_Cobra;false"}));
		configKeys.put("Do.Default.ReplaceIDIfNull"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Void"}));
		configKeys.put("Do.Default.ReplaceLogSystemWithCategory"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Do.Default.ReplaceIfNull"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"System"}));
		configKeys.put("Do.Default.WalletMoneyFlowNotification"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Do.Default.BankMoneyFlowNotification"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Do.DebugMode"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"backgroundtaskGeneral",
				"backgroundtaskLoanRepayment",
				"backgroundtaskStandingOrder",
				"transaction",
				"transactioncmd",
				"ifhapiFormat",
				"currencyhandler",
				"actionlog",
				"trendlog",
				"loan"})); //ADDME
		configKeys.put("Do.ShowOverdueAccounts"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Do.OverdueTimeInDays"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				180}));
		configKeys.put("Do.DeleteAccountsDaysAfterOverdue"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				30}));
		configKeys.put("Do.DeleteLogsAfterDays"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				365}));
		configKeys.put("Do.OnDeath.MoneyInWalletLostInPercent"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar;500.0",
				"taler;100.0"}));
		configKeys.put("Do.Bankaccount.TimeToWithdrawAccountManagementFees"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"SUNDAYY-11:00"}));
		configKeys.put("Do.Bankaccount.AccountManagementFeesAsLumpSum"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar;500.0;MAIN",
				"taler;100.0;MAIN"}));
		configKeys.put("Do.Bankaccount.AccountManagementFeesAsPercent"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar;1.0;MAIN",
				"taler;1.0;MAIN"}));
		configKeys.put("Do.Bankaccount.TimeToDepositInterest"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"SUNDAY-11:00"}));
		configKeys.put("Do.Bankaccount.InterestAsPercent"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar;0.1;SAVING",
				"taler;0.12;SAVING"}));
		configKeys.put("Do.OpenAccount.CountWithAccountType"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Do.OpenAccount.CountWithAccountCategory"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Do.OpenAccount.CountPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ADDUP"}));
		
	
		configKeys.put("Cost.OpenAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar;BANK;SAVING;1000.0;"
				+AccountManagementType.CAN_WITHDRAW.toString()+";"
				+AccountManagementType.CAN_ADMINISTRATE_ACCOUNT.toString()+";"
				+AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()+";"
				+AccountManagementType.CAN_SEE_BALANCE.toString()+";"
				+AccountManagementType.CAN_SEE_LOG.toString()+";"
				+AccountManagementType.CAN_SET_OWNERSHIP.toString(),
				"taler;BANK;SAVING;5000.0;"
				+AccountManagementType.CAN_WITHDRAW.toString()+";"
				+AccountManagementType.CAN_ADMINISTRATE_ACCOUNT.toString()+";"
				+AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()+";"
				+AccountManagementType.CAN_SEE_BALANCE.toString()+";"
				+AccountManagementType.CAN_SEE_LOG.toString()+";"
				+AccountManagementType.CAN_SET_OWNERSHIP.toString()}));
		
		configKeys.put("TrendLogger.ValueIsStabil"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000.0}));
		configKeys.put("GraficSpaceSymbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ˉ"}));
		configKeys.put("GraficPointSymbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"x"}));
		configKeys.put("StandingOrder.TimeSpamProtection"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar;00-00:15",
				"taler;00-00:15"}));
		configKeys.put("StandingOrder.ValueSpamProtection"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar;500.0",
				"taler;500.0"}));
		configKeys.put("StandingOrder.DoPaymentTask"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("StandingOrder.RepeatTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				180}));
		configKeys.put("StandingOrder.DeleteAfterIsCancelledOrPausedInDays"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				180}));
		configKeys.put("Loan.RepaymentRepeatTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				180}));
		configKeys.put("Loan.ToLendingALoanPlayerMustBeTheOwnerOfTheAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		
		configKeys.put("ChestShop.EnableHook"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("HeadDatabase.EnableHook"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("JobsReborn.EnableHook"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("JobsReborn.HookTaskTimer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"0", "15", "30", "45"}));
		configKeys.put("QuickShop.EnableHook"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		commandsKeys.put(path+".Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				name}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		commandsKeys.put(path+".Argument"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				argument}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission+argument}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
	}
	
	public void initCommands()//Commands
	{
		comSingleNested();
		comBypass();
		
		comAEP();
		comAEPLogs();
		comAEPLoggerSettings();
		comAEPAccount();
		
		comMoney();
		
		comLoan();
		
		comStandingOrder();
	}
	
	private void comSingleNested()
	{
		commandsKeys.put("Commands.SINGLE"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandExecuteType.ACTIONLOG.toString()+";actionlog",
				CommandExecuteType.TRENDLOG.toString()+";trendlog",
				CommandExecuteType.WALLETNOTIFICATION.toString()+";walletnotification",
				CommandExecuteType.BANKNOTIFICATION.toString()+";banknotification",
				CommandExecuteType.GETTOTAL.toString()+";gettotal",
				CommandExecuteType.TOPLIST.toString()+";toplist"
				}));
		commandsKeys.put("Commands.NESTED"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandExecuteType.ACTIONLOG.toString()+";aep_actionlog",
				CommandExecuteType.TRENDLOG.toString()+";aep_trendlog",
				CommandExecuteType.WALLETNOTIFICATION.toString()+";aep_walletnotification",
				CommandExecuteType.BANKNOTIFICATION.toString()+";aep_banknotification",
				CommandExecuteType.GETTOTAL.toString()+";aep_gettotal",
				CommandExecuteType.TOPLIST.toString()+";aep_toplist"
				}));
	}
	
	private void comBypass()
	{
		List<ExtraPerm.Type> list = new ArrayList<ExtraPerm.Type>(EnumSet.allOf(ExtraPerm.Type.class));
		for(ExtraPerm.Type ept : list)
		{
			switch(ept)
			{
			case COUNT_ACCOUNT:
				commandsKeys.put("Bypass."+ept.toString().replace("_", "")
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"aep."+ept.toString().toLowerCase().replace("_", "")+"."}));
				break;
			default:
				commandsKeys.put("Bypass."+ept.toString().replace("_", "")
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"aep."+ept.toString().toLowerCase().replace("_", "")}));
				break;
			}
		}
	}
	
	private void comAEP()
	{
		commandsInput("aep", "aep", "aep.cmd.aep", "/aep [pagenumber]", "/aep ",
				"&c/aep &f| Infoseite für alle Befehle.",
				"&c/aep &f| Info page for all commands.");
		String basePermission = "aep.cmd.aep";
		argumentInput("aep_deletelog", "deletelog", basePermission,
				"/aep deletelog <id>", "/aep deletelog ",
				"&c/aep deletelog <id> &f| Löscht den Log-Eintrag. AdminBefehl.",
				"&c/aep deletelog <id> &f| Deletes the log entry. Admin command.");
		
		argumentInput("aep_deleteallplayeraccounts", "deleteallplayeraccounts", basePermission,
				"/aep deleteallplayeraccounts <playername>", "/aep deleteallplayeraccounts ",
				"&c/aep deleteallplayeraccounts <playername> &f| Löscht den Spieler und all seine Accounts.",
				"&c/aep deleteallplayeraccounts <playername> &f| Deletes the player and all his accounts.");
		
		argumentInput("aep_player", "player", basePermission,
				"/aep player <player>", "/aep player ",
				"&c/aep player <Spielername> &f| Zeigt alle Infos zum Spieler an.",
				"&c/aep player <player name> &f| Shows all information about the player.");
		
		argumentInput("aep_recomment", "recomment", basePermission,
				"/aep recomment <id> <message>", "/aep recomment ",
				"&c/aep recomment <Id> <Kategorie> <Note...> &f| Ändert die angehängte Notiz des Log-Eintrages.",
				"&c/aep recomment <Id> <category> <Note...> &f| Changes the attached note of the log entry.");
		
		argumentInput("aep_walletnotification", "walletnotification", basePermission,
				"/aep walletnotification", "/aep walletnotificatione ",
				"&c/aep walletnotification &f| Schaltet Nachrichten aus und ein, die als Überweisung auf euer Brieftaschenkonto eingehen.",
				"&c/aep walletnotification &f| Enables and disables messages that are sent to your wallet account as a transaction.");
		commandsInput("walletnotification", "walletnotification", basePermission+"walletnotification",
				"/walletnotification", "/walletnotificatione ",
				"&c/walletnotification &f| Schaltet Nachrichten aus und ein, die als Überweisung auf euer Brieftaschenkonto eingehen.",
				"&c/walletnotification &f| Enables and disables messages that are sent to your wallet account as a transaction.");
		argumentInput("aep_banknotification", "banknotification", basePermission,
				"/aep banknotification", "/aep banknotificatione ",
				"&c/aep banknotification &f| Schaltet Nachrichten aus und ein, die als Überweisung auf euer Bankkonto eingehen.",
				"&c/aep banknotification &f| Enables and disables messages that are sent to your bank account as a transaction.");
		commandsInput("banknotification", "banknotification", basePermission+"banknotification",
				"/banknotification", "/banknotificatione ",
				"&c/banknotification &f| Schaltet Nachrichten aus und ein, die als Überweisung auf euer bankkonto eingehen.",
				"&c/banknotification &f| Enables and disables messages that are sent to your bank account as a transaction.");
		
		argumentInput("aep_toplist", "toplist", basePermission,
				"/aep toplist <currencyname> <pagenumber>", "/aep toplist ",
				"&c/aep toplist <Währungsname> <Seitenzahl> &f| Zeigt die Liste der bestbetuchten Accounts der Währung an.",
				"&c/aep toplist <currencyname> <page number> &f| Shows the list of the best accounts of the currency.");
		commandsInput("toplist", "toplist", basePermission+"toplist",
				"/toplist [pagenumber]", "/toplist ",
				"&c/toplist [Seitenzahl] &f| Zeigt die Liste der bestbetuchten Spieler an.",
				"&c/toplist [page number] &f| Shows the list of the best players.");
		
		argumentInput("aep_gettotal", "gettotal", basePermission,
				"/aep gettotal", "/aep gettotal ",
				"&c/aep gettotal &f| Zeigt die gesamte Menge an Geld im System",
				"&c/aep gettotal &f| Shows the total amount of money in the system");
		commandsInput("gettotal", "gettotal", basePermission+"gettotal",
				"/gettotal", "/toplist ",
				"&c/gettotal &f| Zeigt die gesamte Menge an Geld im System.",
				"&c/gettotal &f| Shows the total amount of money in the system");
	}
	
	private void comMoney()
	{
		commandsInput("money", "money", "aep.cmd.money",
				"/money", "/money",
				"&c/money &f| Zeigt dein Guthaben an.",
				"&c/money &f| Shows your balance.");
		commandsInput("balance", "balance", "aep.cmd.balance",
				"/balance", "/",
				"&c/balance &f| Zeigt dein Guthaben an.",
				"&c/balance &f| Shows your balance.");
		String basePermission = "aep.cmd.";
		
		argumentInput("money_give", "give", basePermission,
				"/money give <player> <accountname> <amount> [category] [comment...]", "/money give ",
				"&c/money give <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Überweist den Betrag auf das Spielerkonto.",
				"&c/money give <player> <accountname> <amount> [category] [comment...] &f| Transfers the amount to the players balance.");
		commandsInput("give", "give", basePermission+"give",
				"/give <player> <accountname> <amount> [category] [comment...]", "/give ",
				"&c/give <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Überweist den Betrag auf das Spielerkonto.",
				"&c/give <player> <accountname> <amount> [category] [comment...] &f| Transfers the amount to the players balance.");
		
		argumentInput("money_giveconsole", "giveconsole", basePermission,
				"/money giveconsole <player> <accountname> <amount> [category] [comment...]", "/money giveconsole ",
				"&c/money giveconsole <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Überweist den Betrag auf das Spielerkonto.",
				"&c/money giveconsole <player> <accountname> <amount> [category] [comment...] &f| Transfers the amount to the players balance.");
		commandsInput("giveconsole", "giveconsole", basePermission+"giveconsole",
				"/giveconsole <player> <accountname> <amount> [category] [comment...]", "/money giveconsole ",
				"&c/giveconsole <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Überweist den Betrag auf das Spielerkonto.",
				"&c/giveconsole <player> <accountname> <amount> [category] [comment...] &f| Transfers the amount to the players balance.");
		
		argumentInput("money_pay", "pay", basePermission,
				"/money pay <amount> <toplayer> [category] [comment...]", "/money pay ",
				"&c/money pay <Betrag> <Empfänger> [Kategorie] [Notiz...] &f| Zahlt den Betrag vom Quickpay des ausführenden Spieler zu dem Quickpayaccount des Empfängers.",
				"&c/money pay <amount> <toplayer> [category] [comment...] &f| Pays the amount from the executing player's quickpay to the recipient's quickpay account.");
		commandsInput("pay", "pay", basePermission+"pay",
				"/pay <amount> <toplayer> [category] [comment...]", "/money pay ",
				"&c/pay <Betrag> <Empfänger> [Kategorie] [Notiz...] &f| Zahlt den Betrag vom Quickpay des ausführenden Spieler zu dem Quickpayaccount des Empfängers.",
				"&c/pay <amount> <toplayer> [category] [comment...] &f| Pays the amount from the executing player's quickpay to the recipient's quickpay account.");
		
		argumentInput("money_transfer", "transfer", basePermission,
				"/money transfer <fromplayer> <fromaccountname> <amount> <toplayer> <toaccountname> [category] [comment...]", "/money transfer ",
				"&c/money transfer <Sender> <SenderAccountname> <Betrag> <Empfänger> <EmpfängerAccountname> [Kategorie] [Notiz...] &f| Zahlt dem Spieler den Betrag.",
				"&c/money transfer <fromplayer> <fromaccountname> <amount> <toplayer> <toaccountname> [category] [comment...] &f| Pays the player the amount");
		commandsInput("transfer", "transfer", basePermission+"transfer",
				"/transfer <fromplayer> <fromaccountname> <amount> <toplayer> <toaccountname> [category] [comment...]", "/money transfer ",
				"&c/transfer <Sender> <SenderAccountname> <Betrag> <Empfänger> <EmpfängerAccountname> [Kategorie] [Notiz...] &f| Zahlt dem Spieler den Betrag.",
				"&c/transfer <fromplayer> <fromaccountname> <amount> <toplayer> <toaccountname> [category] [comment...] &f| Pays the player the amount");
		
		argumentInput("money_paythroughgui", "paythroughgui", basePermission,
				"/money paythroughgui <amount> <toplayer> [category] [comment...]", "/money paythroughgui ",
				"&c/money paythroughgui <Betrag> <Empfänger> [Kategorie] [Notiz...] &f| Zahlt dem Spieler den Betrag.",
				"&c/money paythroughgui <amount> <toplayer> [category] [comment...] &f| Pays the player the amount");
		commandsInput("paythroughgui", "paythroughgui", basePermission+"paythroughgui",
				"/paythroughgui <toplayer> <amount> [category] [comment...]", "/money paythroughgui ",
				"&c/paythroughgui <toplayer> <amount> [category] [comment...] &f| Zahlt dem Spieler den Betrag.",
				"&c/paythroughgui <toplayer> <amount> [category] [comment...] &f| Pays the player the amount");
		
		argumentInput("money_set", "set", basePermission,
				"/money set <player> <accountname> <amount> [category] [comment...]", "/money set ",
				"&c/money set <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Setzt das Guthaben des Spielers auf den gewünschten Betrag.",
				"&c/money set <player> <accountname> <amount> [category] [comment...] &f| Sets the players balance to the desired amount.");
		commandsInput("set", "set", basePermission+"set",
				"/set <player> <accountname> <amount> [category] [comment...]", "/money set ",
				"&c/set <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Setzt das Guthaben des Spielers auf den gewünschten Betrag.",
				"&c/set <player> <accountname> <amount> [category] [comment...] &f| Sets the players balance to the desired amount.");
		
		argumentInput("money_setconsole", "setconsole", basePermission,
				"/money setconsole <player> <accountname> <amount> [category] [comment...]", "/money setconsole ",
				"&c/money setconsole <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Setzt das Guthaben des Spielers auf den gewünschten Betrag.",
				"&c/money setconsole <player> <accountname> <amount> [category] [comment...] &f| Sets the players balance to the desired amount.");
		commandsInput("setconsole", "setconsole", basePermission+"setconsole",
				"/setconsole <player> <accountname> <amount> [category] [comment...]", "/setconsole ",
				"&c/setconsole <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Setzt das Guthaben des Spielers auf den gewünschten Betrag.",
				"&c/setconsole <player> <accountname> <amount> [category] [comment...] &f| Sets the players balance to the desired amount.");
		
		argumentInput("money_take", "take", basePermission,
				"/money take <player> <accountname> <amount> [category] [comment...]", "/money take ",
				"&c/money take <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Zieht den Betrag vom Spielerkonto ab.",
				"&c/money take <player> <accountname> <amount> [category] [comment...] &f| Deduct the amount from the player balance.");
		commandsInput("take", "take", basePermission+"take",
				"/take <player> <accountname> <amount> [category] [comment...]", "/take ",
				"&c/take <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Zieht den Betrag vom Spielerkonto ab.",
				"&c/take <player> <accountname> <amount> [category] [comment...] &f| Deduct the amount from the player balance.");
		
		argumentInput("money_takeconsole", "takeconsole", basePermission,
				"/money takeconsole <player> <accountname> <amount> [category] [comment...]", "/money takeconsole ",
				"&c/money takeconsole <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Zieht den Betrag vom Spielerkonto ab.",
				"&c/money takeconsole <player> <accountname> <amount> [category] [comment...] &f| Deduct the amount from the player balance.");
		commandsInput("takeconsole", "takeconsole", basePermission+"takeconsole",
				"/takeconsole <player> <accountname> <amount> [category] [comment...]", "/takeconsole ",
				"&c/takeconsole <Spieler> <Accountname> <Betrag> [Kategorie] [Notiz...] &f| Zieht den Betrag vom Spielerkonto ab.",
				"&c/takeconsole <player> <accountname> <amount> [category] [comment...] &f| Deduct the amount from the player balance.");
	}
	
	private void comAEPLogs()
	{
		String basePermission = "aep.cmd.money";
		argumentInput("aep_actionlog", "actionlog", basePermission,
				"/aep actionlog [playername] [accountname] [page] [category]", "/aep actionlog ",
				"&c/aep actionlog [Spielername] [Accountname] [Seitenzahl] [Kategorie] &f| Zeigt direkt den Aktionlog bei den aktuellen Einstellungen.",
				"&c/aep actionlog [playername] [accountname] [page] [category] &f| Shows direct the actionlog by the actual Settings.");
		commandsInput("actionlog", "actionlog", basePermission,
				"/actionlog [playername] [accountname] [page] [category]", "/actionlog ",
				"&c/actionlog [Spielername] [Accountname] [Seitenzahl] [Kategorie] &f| Zeigt direkt den Aktionlog bei den aktuellen Einstellungen.",
				"&c/actionlog [playername] [accountname] [page] [category] &f| Shows direct the actionlog by the actual Settings.");
		argumentInput("aep_trendlog", "trendlog", basePermission,
				"/aep trendlog [playername] [accountname] [page]", "/aep trendlog ",
				"&c/aep trendlog [Spielername] [Accountname] [Seitenzahl] &f| Zeigt direkt den Trendlog bei den aktuellen Einstellungen.",
				"&c/aep trendlog [playername] [accountname] [page] &f| Shows direct the trendlog by the actual Settings.");
		commandsInput("trendlog", "trendlog", basePermission,
				"/trendlog [playername] [accountname] [page]", "/trendlog ",
				"&c/trendlog [Spielername] [Accountname] [Seitenzahl] &f| Zeigt direkt den Trendlog bei den aktuellen Einstellungen.",
				"&c/trendlog [playername] [accountname] [page] &f| Shows direct the trendlog by the actual Settings.");
	}
	
	private void comAEPLoggerSettings()
	{
		String basePermission = "aep.cmd.money";
		argumentInput("aep_loggersettings", "loggersettings", basePermission,
				"/aep loggersetting", "/aep loggersetting ",
				"&c/aep loggersettings &f| Öffnet die Gui und mit angegebenen Argumenten gibt es die Daten in Form von Log, Diagram etc. aus.",
				"&c/aep loggersettings &f| Opens the gui and with given arguments it outputs the data in form of log, diagram etc.");
		
		basePermission = "aep.cmd.money.loggersettings";
		argumentInput("aep_loggersettings_gui", "gui", basePermission,
				"/aep loggersettings gui [<playername> <accountname>] [page] [methode]", "/aep loggersettings gui ",
				"&c/aep loggersettings gui [<Spielername> <Accountname>] [Seitenzahl] [Methode] &f| Öffnet die Gui und mit angegebenen Argumenten gibt es die Daten in Form von Log, Diagram etc. aus.",
				"&c/aep loggersettings gui [<playername> <accountname>] [page] [methode] &f| Opens the gui and with given arguments it outputs the data in form of log, diagram etc.");
		
		argumentInput("aep_loggersettings_other", "other", basePermission,
				"/aep loggersetting other [playername]", "/aep loggersettings other ",
				"&c/aep loggersettings other [Spielername] &f| Öffnet die Gui eines anderen Spielers.",
				"&c/aep loggersettings other [playername] &f| Opens the gui of another player.");
		
		argumentInput("aep_loggersettings_text", "text", basePermission,
				"/aep loggersettings text <Text...>", "/aep loggersettings text ",
				"&c/aep loggersettings text <Text...> &f| Texteditor für bestimmte Parameter der Gui.",
				"&c/aep loggersettings text <Text...> &f| Text editor for certain parameters of the Gui.");
	}
	
	private void comAEPAccount()
	{
		String basePermission = "aep.cmd.money";
		argumentInput("aep_account", "account", basePermission,
				"/aep account", "/aep account ",
				"&c/aep account &f| Zeigt alle Accounts wo man irgendwie Zugriff hat.",
				"&c/aep account &f| Shows all accounts where you have access somehow.");
		basePermission = "aep.cmd.money.account";
		argumentInput("aep_account_close", "close", basePermission,
				"/aep account close <accountownername> [accountname] [confirm]", "/aep account close ",
				"&c/aep account close <AccountEigentümerName> [Accountname] [bestätigen] &f| Schließt einen Account. Restliches Geld verschwindet!",
				"&c/aep account close <accountownername> [accountname] [confirm] &f| Closes an account. Remaining money disappears!");
		argumentInput("aep_account_manage", "manage", basePermission,
				"/aep account manage <playername> <accountname> <tomanageplayername> <managementtype>", "/aep account manage ",
				"&c/aep account manage <Spielername> <Accountname> <Spielername, welcher das Recht erhält> <Managementtype> &f| Gibt oder nimmt dem Spieler Rechte für den angegebenen Account.",
				"&c/aep account manage <playername> <accountname> <tomanageplayername> <managementtype> &f| Gives or takes away the player rights for the specified account.");
		argumentInput("aep_account_open", "open", basePermission,
				"/aep account open <currencyname> <playername/entity/server> <accountname> <accountcategory> [accounttype] [economyentitytype]", "/aep account open ",
				"&c/aep account open <Währungsname> <Spielername/NPC/Sserver> <Accountname> <Accountkategorie> [Accounttype] [Economyentitytype] &f| Eröffnet einen Account.",
				"&c/aep account open <currencyname> <playername/entity/server> <accountname> <accountcategory> [accounttype] [economyentitytype] &f| Opens a account.");
		argumentInput("aep_account_overdue", "overdue", basePermission,
				"/aep account overdue", "/aep account overdue ",
				"&c/aep account overdue &f| Zeigt alle Spieler mit ihren Konten an, welche seit längerem nicht mehr online waren.",
				"&c/aep account overdue &f| Shows all players with their accounts who have not been online for a long time.");
		argumentInput("aep_account_permissioninfo", "permissioninfo", basePermission,
				"/aep account permissioninfo [playername]", "/aep account permissioninfo ",
				"&c/aep account permissioninfo [Spielername] &f| Zeigt Accounts an, sowie per Hover wer dort welche Rechte hat.",
				"&c/aep account permissioninfo [playername] &f| Shows accounts, as well as via hover who has which rights there.");
		argumentInput("aep_account_setdefault", "setdefault", basePermission,
				"/aep account setdefault <playername> <accountname>", "/aep account setdefault ",
				"&c/aep account setdefault <Spielername> <Accountname> &f| Setzt den angegeben Accout als DefaultAccount(Externe Pluginszugriffsmöglichkeit) für seine AccountKategorie.",
				"&c/aep account setdefault <playername> <accountname> &f| Sets the specified Accout as DefaultAccount(External plugin accessibility) for its AccountCategory.");
		argumentInput("aep_account_setname", "setname", basePermission,
				"/aep account setname <playername> <accountname> <newname>", "/aep account setname ",
				"&c/aep account setname <Spielername> <Accountname> <Neuer Name> &f| Setzt einen neuen Namen für den Account.",
				"&c/aep account setname <playername> <accountname> <newname> &f| Sets a new name for the account.");
		argumentInput("aep_account_setowner", "setowner", basePermission,
				"/aep account setowner <playername> <accountname> <newowner>", "/aep account setowner ",
				"&c/aep account setowner <Spielername> <Accountname> <Neuer Eigentümer> &f| Setzt einen neuen Eigentümer für den Account.",
				"&c/aep account setowner <playername> <accountname> <newowner> &f| Sets a new owner for the account.");
		argumentInput("aep_account_setquickpay", "setquickpay", basePermission,
				"/aep account setquickpay <accountname>", "/aep account setquickpay ",
				"&c/aep account setquickpay <Accountname> &f| Setzt den eigenen angegebenen Account als QuickPayAccount.",
				"&c/aep account setquickpay <accountname> &f| Sets the own specified account as QuickPayAccount.");
	}
	
	private void comLoan()
	{		
		commandsInput("loan", "loan", "aep.cmd.loan", "/loan", "/loan ",
				"&c/loan [Seitenzahl] [Spielername] &f| Zeigt deine Kredite an.",
				"&c/loan [page] [playername] &f| Shows yours loans.");
		String basePermission = "aep.cmd.loan";
		argumentInput("loan_accept", "accept", basePermission,
				"/loan accept [confirm]", "/loan accept",
				"&c/loan accept [bestätigen] &f| Akzeptiert einen Kreditvorschlag.",
				"&c/loan accept [confirm] &f| Accept a loanproposal.");
		
		argumentInput("loan_amount", "amount", basePermission,
				"/loan amount <totalamount> <amountratio> <interest>", "/loan amount ",
				"&c/loan amount <gesamtbetrag> <ratenbetrag> <zinzen in %> &f| Setzt für den Gesamtbetrag, Ratenbetrag und die Zinsen für einen Kredit, welcher sich noch in Bearbeitung befindet.",
				"&c/loan amount  <totalamount> <amountratio> <interest in %> &f| Sets the total amount, installment amount and interest for a loan that is still being processed.");
		
		argumentInput("loan_cancel", "cancel", basePermission,
				"/loan cancel", "/loan cancel ",
				"&c/loan cancel &f| 0",
				"&c/loan cancel &f| Cancels the credit creation.");
		
		argumentInput("loan_create", "create", basePermission,
				"/loan create <name> <senderaccountid> <reciveraccountid> <debtorname>", "/loan create ",
				"&c/loan create <Name> <SenderAccountID> <EmpfängerAccountID> <Schuldnername> &f| Erstellt einen Kreditvorschlag.",
				"&c/loan create <name> <senderaccountid> <reciveraccountid> <debtorname> &f| Create a loanproposal.");
		
		argumentInput("loan_forgive", "forgive", basePermission,
				"/loan forgive <id> [confirm]", "/loan forgive",
				"&c/loan forgive <id> &f| Der Restbetrag des Kredits wird erlassen.",
				"&c/loan forgive <id> &f| The remaining amount of the loan will be remitted.");
		
		argumentInput("loan_info", "info", basePermission,
				"/loan info [id]", "/loan info ",
				"&c/loan info [id] &f| Zeigt alle Infos zu allen Krediten an. Ohne Id, wird der Kreditvorschlag angezeigt.",
				"&c/loan info [id] &f| Shows all information about all loans. Without Id, the loan proposal is displayed.");
		
		argumentInput("loan_inherit", "inherit", basePermission,
				"/loan inherit <id> <playername> <accountid>", "/loan inherit ",
				"&c/loan inherit <id> <Spielername> <accountid> &f| Lässt den Spieler den Kredit erben. Somit muss er nun zahlen. Adminbefehl um bei Betrugsfall mit einem 2. Account, diesen dann zu belasten.",
				"&c/loan inherit <id> <playername> <accountid> &f| Lets the player inherit the loan. So now he must pay. Admin command to debit a 2nd account in case of fraud with a 2nd account.");
		
		argumentInput("loan_list", "list", basePermission,
				"/loan list [page]", "/loan list ",
				"&c/loan list [Seitenzahl] &f| Zeigt seitenbasiert alle Kredite als Liste.",
				"&c/loan list [page] &f| Shows all loans in a page-based list.");
		
		argumentInput("loan_pause", "pause", basePermission,
				"/loan pause <id>", "/loan pause ",
				"&c/loan pause <id> &f| Pausiert oder nimmt die Zahlungen des Kredits wieder auf. Nur für den Krediteigentümer möglich!",
				"&c/loan pause <id> &f| Pauses or resumes payments on the loan. Only possible for the loan owner!");
		
		argumentInput("loan_payback", "payback", basePermission,
				"/loan payback <id>", "/loan payback",
				"&c/loan payback <id> &f| Zahlt dem Spieler den Rest des Kredites zurück als Admin.",
				"&c/loan payback <id> &f| Pay the player back the rest of the loan as admin.");
		
		argumentInput("loan_reject", "reject", basePermission,
				"/loan reject", "/loan reject",
				"&c/loan reject &f| Lehnt einen Kreditvorschlag ab.",
				"&c/loan reject &f| Rejects a loan proposal.");
		
		argumentInput("loan_repay", "repay", basePermission,
				"/loan repay <id> <amount>", "/loan repay",
				"&c/loan repay <id> <Betrag> &f| Zahlt einen Betrag vom Kredit ab.",
				"&c/loan repay <id> <amount> &f| Pays an amount off the loan.");
		
		argumentInput("loan_send", "send", basePermission,
				"/loan send <player>", "/loan send ",
				"&c/loan send <Spielername> &f| Sendet einen Kreditvorschlag einem Spieler.",
				"&c/loan send <player> &f| Sends a loan proposal to a player.");
		
		argumentInput("loan_time", "time", basePermission,
				"/loan time <starttime> <endtime> <repeatingtime>", "/loan time ",
				"&c/loan time <startdatum|dd.MM.yyyy-HH:mm> <enddatum|dd.MM.yyyy-HH:mm> <ratenzyklus|dd-HH:mm> &f| Setzt die Zeiten für den Kreditvorschlag.",
				"&c/loan time <starttime|dd.MM.yyyy-HH:mm> <endtime|dd.MM.yyyy-HH:mm> <repeatingtime|dd-HH:mm> &f| Sets the times for the loan proposal.");
		
		argumentInput("loan_transfer", "", basePermission,
				"/loan transfer <id> <player>", "/loan transfer ",
				"&c/loan transfer <id> <Spielername> &f| Überträgt den Eigentümerstatus und Rückzahlrecht an den Spieler.",
				"&c/loan transfer <id> <player> &f| Transfers the ownership status and repayment right to the player.");
	}
	
	private void comStandingOrder()
	{
		commandsInput("standingorder", "standingorder", "aep.cmd.standingorder", "/standingorder", "/standingorder ",
				"&c/standingorder [Seitenzahl] [Spielername] &f| Zeigt deine Daueraufträge an.",
				"&c/standingorder [page] [playername] &f| Shows yours standing orders.");
		String basePermission = "aep.cmd.standingorder";
		argumentInput("standingorder_amount", "amount", basePermission,
				"/standingorder amount <amount> [id]", "/standingorder amount",
				"&c/standingorder amount <Betrag> [id] &f| Setzt den Betrag für ein noch wartenden Dauerauftrag.",
				"&c/standingorder amount <Betrag> [id] &f| Sets the amount for a still waiting standing order.");
		
		argumentInput("standingorder_cancel", "cancel", basePermission,
				"/standingorder cancel", "/standingorder cancel",
				"&c/standingorder cancel &f| Bricht den noch wartenden Dauerauftrag ab.",
				"&c/standingorder cancel &f| Cancels the still waiting standing order.");
		
		argumentInput("standingorder_create", "create", basePermission,
				"/standingorder create <name> <senderaccoundID> <reciveraccountID>", "/standingorder create",
				"&c/standingorder create <name> <SenderaccountID> <EmpfängeraccountID> &f| Erstellt einen wartenden Dauerauftrag. Durch weitere Einstellung wird dieser finalisiert.",
				"&c/standingorder create <name> <senderaccountID> <reciveraccountID> &f| Creates a waiting standing order. This is finalized by further settings.");
		
		argumentInput("standingorder_delete", "delete", basePermission,
				"/standingorder delete <id>", "/standingorder delete",
				"&c/standingorder delete <id> &f| Löscht den schon existierenden Dauerauftrag.",
				"&c/standingorder delete <id> &f| Deletes the already existing standing order.");
		
		argumentInput("standingorder_info", "info", basePermission,
				"/standingorder info [id]", "/standingorder info",
				"&c/standingorder info [id] &f| Zeigt alle Info zu einem Dauerauftrag an. Bei keiner Angabe, zeigt es den noch wartenden Dauerauftrag.",
				"&c/standingorder info [id] &f| Shows all information about a standing order. If not specified, it shows the still waiting standing order.");
		
		argumentInput("standingorder_list", "list", basePermission,
				"/standingorder list [page]", "/standingorder list ",
				"&c/standingorder list [Seitenzahl] &f| Listet alle Daueraufträge von allen Spielern auf.",
				"&c/standingorder list [Seitenzahl] &f| Lists all standing orders from all players.");
		
		argumentInput("standingorder_pause", "pause", basePermission,
				"/standingorder pause <id>", "/standingorder pause ",
				"&c/standingorder pause <ID> &f| Pausiert den Dauerauftrag. Falls er vorher abgebrochen wurde, setzte er den Status zurück.",
				"&c/standingorder pause <ID> &f| Pauses the standing order. If it was canceled before, it resets the status.");
		
		argumentInput("standingorder_repeatingtime", "repeatingtime", basePermission,
				"/standingorder repeatingtime <dd-HH:mm value>", "/standingorder repeatingtime",
				"&c/standingorder repeatingtime <dd-HH:mm Wert> &f| Setzt eine Wiederholungsvariable, welche im dd-HH:mm Format geschrieben werden muss.",
				"&c/standingorder repeatingtime <dd-HH:mm Wert> &f| Sets a repeat variable, which must be written in dd-HH:mm format.");
		
		argumentInput("standingorder_endtime", "endtime", basePermission,
				"/standingorder endtime <dd.MM.yyyy-HH:mm value>  [standingordername]", "/standingorder endtime",
				"&c/standingorder endtime <dd.MM.yyyy-HH:mm Wert>  [Dauerauftragsname] &f| Setzt das Enddatum.",
				"&c/standingorder endtime <dd.MM.yyyy-HH:mm Wert> [standingordername] &f| Sets the enddate.");
		
		argumentInput("standingorder_starttime", "starttime", basePermission,
				"/standingorder starttime <dd.MM.yyyy-HH:mm value>", "/standingorder starttime",
				"&c/standingorder starttime <dd.MM.yyyy-HH:mm Wert> &f| Setzt das Startdatum. Es müssen vorher alle anderen Eigenschaften gesetzt sein, denn dieser Befehl startet auch gleichzeitig den Dauerauftrag!",
				"&c/standingorder starttime <dd.MM.yyyy-HH:mm Wert> &f| Sets the start date. All other properties must be set first, because this command also starts the standing order at the same time!");
	}
	
	public void initLanguage()
	{
		languageKeys.put("NoPermission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast dafür keine Rechte!",
				"&cYou have no rights!"}));
		languageKeys.put("EntityNotExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Spieler/Entity/Server existiert nicht!",
				"&cThe player/entity/server does not exist!"}));
		languageKeys.put("PlayerNotExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Spieler existiert nicht!",
				"&cThe player does not exist!"}));
		languageKeys.put("PlayerNotOnline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Spieler ist nicht online!",
				"&cThe player is not online!"}));
		languageKeys.put("IllegalArgument"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDas angegebene Argument ist keine Zahl!",
				"&cThe specified argument is not a number!"}));
		languageKeys.put("InputIsWrong"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDeine Eingabe ist fehlerhaft, klicke hier auf den Text um weitere Infos zu bekommen!",
				"&cYour input is incorrect, click here on the text to get more information!"}));
		languageKeys.put("NoNumber"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDas Argument §f%args% §cist keine Nummer!",
				"&cThe argument &f%args% &cis no number!"}));
		languageKeys.put("NumberIsNegativ"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Zahl &f%args% §cist Negativ! Benutze nur positive Zahlen!",
				"&cThe number &f%args% &cis negative! Use only positive numbers!"}));
		languageKeys.put("NoPlayerAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cSpielerkonten sind nicht aktiv!",
				"&cPlayer accounts are not active!"}));
		languageKeys.put("YourAccountIsFrozen"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDein Spielerkonto ist eingeforen! Du kannst nichts mehr überweisen!",
				"&cYour player account has been frozen! You can not transfer money anymore!"}));
		languageKeys.put("NoBank"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBankkonten sind nicht aktiv!",
				"&cBank accounts are not active!"}));
		languageKeys.put("NoLoan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cKredite sind nicht aktiv!",
				"&cDebts are not active!"}));
		languageKeys.put("NoStandingOrder"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDaueraufträge sind nicht aktiv!",
				"&cStandingOrder are not active!"}));
		languageKeys.put("NoBankAccountFree"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEs existiert keine freie Banknummer!",
				"&cThere is no free bank number!"}));
		languageKeys.put("NoBankOrPlayerAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEntweder sind die Spielerkonten oder die Bankkonten deaktiviert!",
				"&cEither the player accounts or the bank accounts are deactivated!"}));
		languageKeys.put("TransactionSuccess"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aTransaktion erfolgreich!",
				"&aTransaction successful!"}));
		languageKeys.put("SpecifyBankNumber"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Bankkontoname &f%name% &cexistiert mehrfach! Bitte spezifiziere die Angabe, durch die Eingabe der Banknummer!",
				"&cThe bank account name &f%name% &cexistiert multiple! Please specify the information by entering the bank number!"}));
		languageKeys.put("GeneralHover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKlick mich!",
				"&eClick me!"}));
		languageKeys.put("GeneralError"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEs passiert gerade ein genereller Fehler beim Befehl: &f%cmd% | %message%",
				"&cThere is a general error in the command right now: &f%cmd% | %message%"}));
		languageKeys.put("OtherCmd"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBitte nutze den Befehl, mit einem weiteren Argument aus der Tabliste!",
				"&cPlease use the command with another argument from the tab list!"}));
		languageKeys.put("InterestCantBeUnderMinus100"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eZinsen können nicht kleiner oder gleich -100 % sein!",
				"&eInterest cannot be less than or equal to -100 %!"}));
		
		languageKeys.put("AccountOpen.Category"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Accounteröffnung",
				"Accountopening"}));
		languageKeys.put("AccountOpen.Comment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eEröffnung des Accounts &f%accountname%(%owner%|%accounttype%|%accountcategory%)",
				"&eAccount opening &f%accountname%(%owner%|%accounttype%|%accountcategory%)"}));
		
		languageKeys.put("StandingOrder.Category"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Dauerauftrag",
				"StandingOrder"}));
		languageKeys.put("StandingOrder.Comment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDauerauftrag &f%name%~!~&dInsgesamt gezahlter Betrag: &f%format%",
				"&eStandingOrder &f%name%~!~&dTotal amount paid: &f%format%"}));
		
		languageKeys.put("LoanRepayment.CategoryI"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Kreditvergabe",
				"Loangranting"}));
		languageKeys.put("LoanRepayment.CategoryII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Kreditrückzahlung",
				"LoanRepayment"}));
		languageKeys.put("LoanRepayment.CommentI"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKredit &f%name% &evergeben von &f%lender% &ean %debtor%&e, Account &f%owner% %accountname%(%id%)",
				"&eLoan &f%name% &given from &f%lender% to %debtor%, Account &f%owner% %accountname%(%id%)"}));
		languageKeys.put("LoanRepayment.CommentII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKredit &f%name%~!~&dInsgesamt gezahlter Betrag: &f%totalpaid%~!~&5Ausstehender Betrag: &f%waitingamount%",
				"&eLoan &f%name%~!~&dTotal amount paid: &f%totalpaid%~!~&5Outstanding amount: &f%waitingamount%"}));
		languageKeys.put("LoanRepayment.CommentIII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKredit &f%name%~!~&eRückzahlung der noch fälligen &f%payback%&e.",
				"&eLoan &f%name%~!~&eRepayment of the &f%payback% &estill due."}));
		
		languageKeys.put("OnDeath.Category"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Spielertod",
				"Playerdeath"}));
		languageKeys.put("OnDeath.Comment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cGeldverlust wegen des Todes des Spielers.",
				"&cLoss of money due to the death of the player."}));
		
		languageKeys.put("Bankaccount.AccountManagementFees.CategoryI"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Kontoführungsgebühren",
				"AccountManagementFees"}));
		languageKeys.put("Bankaccount.AccountManagementFees.CommentI"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cKontoführungsgebühren für das Bankkonto als Pauschalbetrag.",
				"&cBank account maintenance fees as a lump sum."}));
		languageKeys.put("Bankaccount.AccountManagementFees.CategoryII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Kontoführungsgebühren",
				"AccountManagementFees"}));
		languageKeys.put("Bankaccount.AccountManagementFees.CommentII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cKontoführungsgebühren für das Bankkonto als Prozentbetrag.",
				"&cBank account maintenance fees as a percentamount."}));
		
		languageKeys.put("ChestShopHook.Category"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"ChestShop",
				"ChestShop"}));
		languageKeys.put("ChestShopHook.Sell"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eShop: &f%amount% &6x &b%item% &ean &f%player% &averkauft&e!",
				"&eShop: &f%amount% &6x &b%item% &eto &f%player% &asold&e!"}));
		languageKeys.put("ChestShopHook.Buy"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eShop: &f%amount% &6x &b%item% &evon &f%player% &cgekauft&e!",
				"&eShop: &f%amount% &6x &b%item% &efrom &f%player% &cpurchased&e!"}));
		
		languageKeys.put("JobsRebornHook.Category"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Jobs",
				"Jobs"}));
		languageKeys.put("JobsRebornHook.Comment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDein Gehalt vom Job &d%job%.",
				"&eYour salary from the job &d%job%."}));
		
		languageKeys.put("HeadDatabase.Category"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"HeadDB",
				"HeadDB"}));
		languageKeys.put("HeadDatabase.Comment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"%head% &egekauft!",
				"%head% &epurchased!"}));
		
		languageKeys.put("QuickShopHook.Category"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"QuickShop",
				"QuickShop"}));
		languageKeys.put("QickShopHook.Sell"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eShop: &f%amount% &6x &b%item% &ean &f%player% &averkauft&e!",
				"&eShop: &f%amount% &6x &b%item% &eto &f%player% &asold&e!"}));
		languageKeys.put("QickShopHook.Buy"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eShop: &f%amount% &6x &b%item% &evon &f%player% &cgekauft&e!",
				"&eShop: &f%amount% &6x &b%item% &efrom &f%player% &cpurchased&e!"}));
		languageKeys.put("JoinListener.OverdueAccounts"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAchtung! &eEs sind &f%amount% &eSpieler schon länger als &f%days% &eTagen nicht aktiv. Wenn diese Acounts vererbt werden soll, setzte bitte einen neuen Eigentümer. &cSonst werden sie nach weitern &f%deletedays% &cTagen gelöscht!",
				"&cAttention. &eThere are &f%amount% &eplayers longer than &f%days% &edays not active. If they accounts should be inherited, please set a new owner. &cOtherwise they will be deleted after further &f%deletedays% &cdays!"}));
		languageKeys.put("JoinListener.OverdueAccountsWithoutDelete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAchtung! &eEs sind &f%amount% &eSpieler schon länger als &f%days% &eTagen nicht aktiv. Wenn diese vererbt werden soll, setzte bitte einen neuen Eigentümer. &cBedenke, die Löschfunktion ist nicht aktiv!",
				"&cAttention. &eThere are &f%amount% &eplayers longer than &f%days% &edays not active. If they should be inherited, please set a new owner. &cRemember, the delete function is not active!"}));
		languageKeys.put("DeathListener.MoneyLost"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAchtung! &eDu bist gerade gestorben. Alle &fBrieftaschen &eAccount(&f%account%) &everlieren ein Teil ihres Inhaltes!",
				"&cAttention. &eYou just died. All &fwallets &eAccount(&f%account%) &elose a part of their contents!"}));
		languageKeys.put("DeathListener.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cVerluste aufgerechnet pro Währung:",
				"&cLosses offset per currency:"}));
		
		//INFO:language
		langEnumReplacer();
		langTransactionHandler();
		langVaultApi();
		langLog();
		langCurrency();
		langCmd();
		langLoan();
		langStandingOrder();
		
		/*languageKeys.put(""
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"",
				""}));*/
	}
	
	private void langEnumReplacer()
	{
		languageKeys.put("Boolean.Replacer.true"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&a✔"}));
		languageKeys.put("Boolean.Replacer.false"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&c✖"}));
		List<EconomyEntity.EconomyType> eeetl = new ArrayList<EconomyEntity.EconomyType>(EnumSet.allOf(EconomyEntity.EconomyType.class));
		for(EconomyEntity.EconomyType eeet : eeetl)
		{
			languageKeys.put("EconomyEntityEconomyType.Replacer."+eeet.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					eeet.toString()}));
		}
		
		List<AccountType> actl = new ArrayList<AccountType>(EnumSet.allOf(AccountType.class));
		for(AccountType act : actl)
		{
			languageKeys.put("AccountType.Replacer."+act.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					act.toString()}));
		}
		
		List<AccountCategory> accl = new ArrayList<AccountCategory>(EnumSet.allOf(AccountCategory.class));
		for(AccountCategory acc : accl)
		{
			languageKeys.put("AccountCategory.Replacer."+acc.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					acc.toString()}));
		}
		
		List<AccountManagementType> amtl = new ArrayList<AccountManagementType>(EnumSet.allOf(AccountManagementType.class));
		for(AccountManagementType amt : amtl)
		{
			languageKeys.put("AccountManagementType.Replacer."+amt.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					amt.toString()}));
		}
	}
	
	private void langTransactionHandler()
	{
		String base = "TransactionHandler.";
		languageKeys.put(base+"IS_NOT_ENABLED"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cWirtschaftsaktionen sind ist ausgeschaltet!",
				"&cEconomic actions are switched off!"}));
		languageKeys.put(base+"HAS_NO_WALLET_SUPPORT"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Plugineinstellungen erlauben keine Brieftaschen Accounts!",
				"&cThe plugin settings do not allow wallet accounts!"}));
		languageKeys.put(base+"HAS_NO_BANK_SUPPORT"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Plugineinstellungen erlauben keine Bankaccounts!",
				"&cThe plugin settings do not allow bank accounts!"}));
		languageKeys.put(base+"AMOUNT_IS_NEGATIVE"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer abzuziehende Betrag ist negativ!",
				"&cDer abzuziehende Betrag ist negativ!"}));
		languageKeys.put(base+"DEPOSIT_ACCOUNT_NOT_EXIST"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Account, wo der Betrag einzuzahlen ist, existiert nicht!",
				"&cThe account where the amount is to be deposited does not exist!"}));
		languageKeys.put(base+"WITHDRAW_ACCOUNT_NOT_EXIST"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Account, wo der Betrag abzuziehen ist, existiert nicht!",
				"&cThe account where the amount is to be deducted does not exist!"}));
		languageKeys.put(base+"CURRENCYS_ARE_NOT_THE_SAME"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Währungen sind nicht gleich!",
				"&cThe currencies are not the same!"}));
		languageKeys.put(base+"CURRENCYS_ARE_NOT_EXCHANGEABLE"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEiner der Währungen ist nicht umtauschbar!",
				"&cOne of the currencies is not exchangeable!"}));
		languageKeys.put(base+"TAX_ACCOUNT_DONT_EXIST"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer angegebene Steueraccount existiert nicht!",
				"&cThe specified tax account does not exist!"}));
		languageKeys.put(base+"TAX_IS_NEGATIVE"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie angegebenen Steuerprozente sind negativ!",
				"&cThe indicated tax percentages are negative!"}));
		languageKeys.put(base+"TAX_IS_HIGHER_OR_EQUAL_AS_100_PERCENT"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie angegebenen Steuerprozente sind größer als 100 %!",
				"&cThe indicated tax percentages are greater than 100%!"}));
		languageKeys.put(base+"WITHDRAW_HAS_NOT_ENOUGH"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer abgebende Account hat nicht genug Geld!",
				"&cThe giving account does not have enough money!"}));
		languageKeys.put(base+"TA_SUCCESS"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eTransaktion erfolgreich!",
				"&eTransaction successful!"}));
		languageKeys.put(base+"D_SUCCESS"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eEinzahlung erfolgreich!",
				"&eDeposit successful!"}));
		languageKeys.put(base+"W_SUCCESS"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eGeldeinzug erfolgreich!",
				"&eWithdraw successful!"}));
		languageKeys.put(base+"EX_SUCCESS"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eWährungsumtausch erfolgreich!",
				"&eCurrency exchange successful!"}));
		languageKeys.put(base+"ACTIONLOG_TRANSACTION"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Transaktion erfolgt...",
				"Transaction is made"}));
		languageKeys.put(base+"ACTIONLOG_DEPOSIT"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Einzahlung erfolgt...",
				"Deposit is made..."}));
		languageKeys.put(base+"ACTIONLOG_WITHDRAW"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Geldeinzug erfolgt...",
				"Withdraw is made..."}));
		languageKeys.put(base+"ACTIONLOG_EXCHANGE"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Währungsumtausch erfolgt...",
				"Currency exchange takes place..."}));
	}
	
	private void langVaultApi()
	{
		languageKeys.put("UseIFH"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Zugriffsmethode über Vault ist unzureichend, es ist anzuraten für Bankkonten InterfaceHub zu nutzen!",
				"&cThe access method via Vault is insufficient, it is recommended to use InterfaceHub for bank accounts!"}));
		languageKeys.put("Wallet.Withdraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDas Geld wurde dem Account abgezogen!",
				"&eThe money was deducted from the account!"}));
		languageKeys.put("Wallet.Deposit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDas Geld wurde dem Account überwiesen!",
				"&eThe money was transferred to the account!"}));
		languageKeys.put("Bank.NameAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Bankaccount mit dem angegebenen Namen existiert schon!",
				"&cThe bank account with the specified name already exists!"}));
		languageKeys.put("Bank.Create.IsSuccided"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Bankaccount ist erfolgreich erstellt in der Kategorie MAIN.",
				"&eThe bank account is successfully created in the MAIN category."}));
	}
	
	private void langLog()
	{
		String base = "Log.";
		languageKeys.put(base+"AccountDontExit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Account existiert nicht!",
				"&cThe account dont exit!"}));
		languageKeys.put(base+"Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&f=====&cAEP &eBefehle&f=====",
				"&f=====&cAEP &eCommands&f====="}));
		languageKeys.put(base+"Next"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e&nnächste Seite &e==>",
				"&e&nnext page &e==>"}));
		languageKeys.put(base+"Past"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e<== &nvorherige Seite",
				"&e<== &nprevious page"}));
		languageKeys.put(base+"ActionLog.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2ActionLog&b: &f%accountid%-%accountowner%-%accountname%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2ActionLog&b: &f%accountid%-%accountowner%-%accountname%&7 | Log-Quantity: %amount%&7]&e====="}));
		languageKeys.put(base+"ActionLog.MainMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				  "&7[&e%date%&7] "
				+ "%fromcolor%%fromaccountid%:%fromaccountname%~hover@SHOW_TEXT@Log.ActionLog.FromAccountHover "
				+ "&6>> "
				+ "%tocolor%%toaccountid%:%toaccountname%~hover@SHOW_TEXT@Log.ActionLog.ToAccountHover "
				+ ": "
				+ "&a%format%~hover@SHOW_TEXT@Log.ActionLog.ElseHover"}));
		languageKeys.put(base+"ActionLog.Positive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&a",
				"&a"}));
		languageKeys.put(base+"ActionLog.Neutral"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7",
				"&7"}));
		languageKeys.put(base+"ActionLog.Negative"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&c",
				"&c"}));
		languageKeys.put(base+"ActionLog.FromAccountHover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bEigentümer: %fromaccountowner%",
				"&bOwner: %fromaccountowner%"}));
		languageKeys.put(base+"ActionLog.ToAccountHover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bEigentümer: %toaccountowner%",
				"&bOwner: %toaccountowner%"}));
		languageKeys.put(base+"ActionLog.ElseHover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bAuftraggeber: &f%orderer%~!~"
				+ "&bKategorie: %category%~!~"
				+ "&bNotiz: %comment%~!~"
				+ "&bAbgezogener Betrag: %withdraw%~!~"
				+ "&bSteuerbetrag: %tax%~!~"
				+ "&bEingezahlter Betrag: %deposit%",
				"&bOrderer: &f%orderer%~!~"
				+ "&bCategory: %category%~!~"
				+ "&bComment: %comment%~!~"
				+ "&bWithdraw amount: %withdraw%~!~"
				+ "&bTax amount: %tax%~!~"
				+ "&bDeposit amount: %deposit%"}));
		languageKeys.put(base+"ActionLog.Edit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&2✏~click@SUGGEST_COMMAND@%cmd%+%id%+~hover@SHOW_TEXT@Log.ActionLog.HoverRecomment",
				"&2✏~click@SUGGEST_COMMAND@%cmd%+%id%+~hover@SHOW_TEXT@Log.ActionLog.HoverRecomment"}));
		languageKeys.put(base+"ActionLog.Delete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&c✖~click@SUGGEST_COMMAND@%cmd%+%id%~hover@SHOW_TEXT@Log.ActionLog.HoverDelete",
				"&c✖~click@SUGGEST_COMMAND@%cmd%+%id%~hover@SHOW_TEXT@Log.ActionLog.HoverDelete"}));
		languageKeys.put(base+"ActionLog.HoverDelete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKlicke hier um dem den Log-Eintrag zu löschen!",
				"&eClick here to delete the log entry!"}));
		languageKeys.put(base+"ActionLog.HoverRecomment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKlicke hier um die Notiz des Log-Eintrages zu ändern.",
				"&eClick here to change the note of the log entry."}));
		
		languageKeys.put(base+"TrendLog.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&bTrendLog: &f%accountid%-%accountowner%-%accountname%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&bTrendLog: &f%accountid%-%accountowner%-%accountname%&7 | Log-Quantity: %amount%&7]&e====="}));
		languageKeys.put(base+"TrendLog.ChangeNegativ"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7] &b%first% &fbis &c%last%",
				"&7[&e%date%&7] &b%first% &fto &c%last%"}));
		languageKeys.put(base+"TrendLog.ChangeNeutral"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7] &b%first% &fbis &b%last%",
				"&7[&e%date%&7] &b%first% &fto &b%last%"}));
		languageKeys.put(base+"TrendLog.ChangePositiv"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7] &b%first% &fbis &a%last%",
				"&7[&e%date%&7] &b%first% &fto &a%last%"}));
		languageKeys.put(base+"TrendLog.Positiv"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Relative Änderung: &a%relativ%",
				"Relative change: &a%relativ%"}));
		languageKeys.put(base+"TrendLog.Negativ"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Relative Änderung: &c%relativ%",
				"Relative change: &c%relativ%"}));
		
		languageKeys.put(base+"Diagram.NotEnoughValues"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEs gibt nicht genügend Werte für diese Seitenzahl!",
				"&cThere are not enough values for this page number!"}));
		languageKeys.put(base+"Diagram.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bTrenddiagramm: &f%accountname%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2Economy &bTrenddiagram: &f%accountname%&7 | Log-Quantity: %amount%&7]&e====="}));
		languageKeys.put(base+"Diagram.HeadlineII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bAktiondiagramm: &f%accountname%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2Economy &bActiondiagram: &f%accountname%&7 | Log-Quantity: %amount%&7]&e====="}));
		languageKeys.put(base+"Diagram.Infoline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cMin %min% &f)|( &aMax %max% &f>> &6Mittelwert/line: &e%median%",
				"&cMin %min% &f)|( &aMax %max% &f>> &6average value/line: &e%median%"}));
		languageKeys.put(base+"Diagram.Positiv"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Relative Änderung: &a%relativ% &f| &a%percent% %",
				"Relative change: &a%relativ% &f| &a%percent% %"}));
		languageKeys.put(base+"Diagram.Negativ"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Relative Änderung: &c%relativ% &f| &c%percent% %",
				"Relative change: &c%relativ% &f| &c%percent% %"}));
		
		languageKeys.put(base+"Grafic.NotEnoughValues"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEs gibt nicht genügend Werte für diese Seitenzahl!",
				"&cThere are not enough values for this number of pages!"}));
		languageKeys.put(base+"Grafic.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bTrendgrafik: &f%accountname%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2Economy &bTrendgrafic: &f%accountname%&7 | Log-Quantity: %amount%&7]&e====="}));
		languageKeys.put(base+"Grafic.HeadlineII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bAktiongrafik: &f%accountname%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2Economy &bActiongrafic: &f%accountname%&7 | Log-Quantity: %amount%&7]&e====="}));
		
		languageKeys.put(base+"NoLoggerSettingsFound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast keinen LoggerSetting ausgewählt!",
				"&cYou have not selected a LoggerSetting!"}));
		languageKeys.put(base+"NoOtherLoggerSettingsFound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Spieler hat keinen LoggerSetting ausgewählt!",
				"&cThe player has not selected a LoggerSetting!"}));
		languageKeys.put(base+"LoggerSettingsTextOnlyThroughGUI"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBitte den Befehl nur nutzen, wenn man durch die GUI aufgefordert wird!",
				"&cPlease use the command only if you are prompted by the GUI prompts you to do so!"}));
		languageKeys.put(base+"LoggerSettingsTextSuggest"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKlicke hier auf die Nachricht, um einen Befehlsvorschlag zu erhalten. Tippe dann deinen Suchtext ein und drücke &b>Enter<",
				"&eClick here on the message to get a command suggestion. Then type your search text and press &b>Enter<"}));
		
		languageKeys.put(base+"BarChart.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=======&7[&2Economy &bBarChart: &f%player%&7 | Log-Anzahl: %amount%&7]&e=======",
				"&e=======&7[&2Economy &bBarChart: &f%player%&7 | Log-Quantity: %amount%&7]&e======="}));
		languageKeys.put(base+"BarChart.Infoline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aEinnahmen &7[in %-Anteil vom Jahr] &e| &cAusgaben &7[in %-Anteil vom Jahr]",
				"&aIncome &7[in % share of year] &e| &cExpenditure &7[in % share of year]"}));
		languageKeys.put(base+"BarChart.HoverMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aEinnahmen in Monat = &f%positivvalue%~!~&aProzentualer Anteil der Einnahmen = &f%percentP% %~!~&cAusgaben im Monat = &f%negativvalue%~!~&cProzentualer Anteil der Ausgaben = &f%percentN% %~!~&eGesamtveränderung im Monat = &r%totalvalue%",
				"&aIncome in month = &f%positivevalue%~!~&aPercentage share of income = &f%percentP% %~!~&cExpenditure in month = &f%negativevalue%~!~&cPercentage share of expenditure = &f%percentN% %~!~&eTotal change in month = &r%totalvalue%"}));
		languageKeys.put(base+"BarChart.HoverMessageII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aEinnahmen in letzten Jahr = &f%positivvalue%~!~&cAusgaben im letzten Jahr = &f%negativvalue%~!~&eGesamtveränderung im letzten Jahr = &r%totalvalue%",
				"&aIncome in last year = &f%positivevalue%~!~&cExpenditure in last year = &f%negativevalue%~!~&eTotal change in last year = &r%totalvalue%"}));
		languageKeys.put(base+"BarChart.Month"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e -    %month%    - &r",
				"&e -    %month%    - &r"}));
		languageKeys.put(base+"BarChart.LastYear"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e - Letztes Jahr - &r",
				"&e -  Last Year  - &r"}));
	}
	
	private void langCurrency()
	{
		String base = "Cmd.";
		languageKeys.put(base+"Exchange.CurrencyDontAllowExchange"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Währung &f%currency% &cerlaubt keinen Umtausch in eine andere Währung!",
				"&cThe currency &f%currency% &cdoes not allow exchange into another currency!"}));
	}
	
	private void langCmd()
	{
		String base = "Cmd.";
		languageKeys.put(base+"AccountDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Account %account% existiert nicht!",
				"&cThe account %account% does not exist!"}));
		languageKeys.put(base+"CurrencyNoLoaded"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Währung des Accounts &f%acn% &cist nicht geladen! Alle Zugriffe, egal welcher Art, werden auf diesem Server verweigert!",
				"&cThe currency of the account &f%acn% &cis not loaded! All access, no matter what kind, will be denied on this server!"}));
		languageKeys.put(base+"NoWithdrawRights"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast kein Recht von dem Account Geld abzuheben!",
				"&cYou have no right to withdraw money from the account!"}));
		
		languageKeys.put(base+"Account.YouAreNotTheOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Account gehört dir nicht!",
				"&cThe account is not yours!"}));
		languageKeys.put(base+"Account.IsPredefine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Account vordefiniert! Das Schließen eines solchen Account bedarf zusätzlicherer Rechte!",
				"&cThe account predefined! Closing such an account requires additional rights!"}));
		languageKeys.put(base+"Account.PleaseConfirmPredefine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Account vordefiniert! Bitte bestätige am ende des Befehls mit &fbestätigen&c!",
				"&cThe account predefined! Please confirm at the end of the command with &fconfirm&c!"}));
		languageKeys.put(base+"Account.Close.AccountIsClosed"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Account &f%acname% (%acowner%) &ewurde geschlossen! Gelöschte Geldbetrag, der auf dem Konto verblieb: &r%format%",
				"&eThe account &f%acname% (%acowner%) &ehas been closed! Deleted amount of money that remained on the account: &r%format%"}));
		languageKeys.put(base+"Account.Close.BalanceMoreThanZero"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAuf dem Account &f%acname% (%acowner%) &cliegt noch Geld! Nutze &fbestätigen &cum den Account trotzdem zu löschen.",
				"&cThere is still money on the account &f%acname% (%acowner%) &c! Use &fconfirm &cto delete the account anyway."}));
		languageKeys.put(base+"Account.Manage.AMTDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer AccountManagementType &f%amt% &cexistiert nicht!",
				"&cThe AccountManagementType &f%amt% &cexists not!"}));
		languageKeys.put(base+"Account.Manage.YouCannotManageTheAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu darfst den angegeben Account nicht administrieren!",
				"&cYou are not allowed to administrate the given account!"}));
		languageKeys.put(base+"Account.Manage.AMTWasRemoved"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Spieler &f%player% &everlor den AccountManagementType &f%amt% &ebei dem Account &f%acname%(%acowner%)",
				"&eThe player &f%player% &elost the AccountManagementType &f%amt% &eat the account &f%acname%(%acowner%)"}));
		languageKeys.put(base+"Account.Manage.AMTWasAdded"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Spieler &f%player% &eerhält das Recht des AccountManagementType &f%amt% &ebei dem Account &f%acname%(%acowner%)",
				"&eThe player &f%player% &ereceives the right of the AccountManagementType &f%amt% &eat the account &f%acname%(%acowner%)"}));
		languageKeys.put(base+"Account.Open.CurrencyDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Währung %currency% existiert nicht!",
				"&cThe currency %currency% dont exist!"}));
		languageKeys.put(base+"Account.Open.DontAllowOpening"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Servereinstellungen erlauben keinen Account mit den angegeben Parameter!",
				"&cThe server settings do not allow an account with the specified parameters!"}));
		languageKeys.put(base+"Account.Open.EconomyTypeIncorrect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie EconomyEntityType %eeet% existiert nicht!",
				"&cThe economyentitytype %eeet% dont exist!"}));
		languageKeys.put(base+"Account.Open.AccountTypeIncorrect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie AccountType %act% existiert nicht!",
				"&cThe accounttype %act% dont exist!"}));
		languageKeys.put(base+"Account.Open.AccountCategoryIncorrect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie AccountKategorie %acc% existiert nicht!",
				"&cThe accountcategory %acc% dont exist!"}));
		languageKeys.put(base+"Account.Open.AccountAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Account mit den angegeben Parameter existiert schon!",
				"&cThe account with the specified parameters already exists!"}));
		languageKeys.put(base+"Account.Open.TooManyAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast schon genug Accounts!",
				"&cYou already have enough accounts!"}));
		languageKeys.put(base+"Account.Open.YouCannotOpenAAccountForSomeone"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu kannst keinen Account für jemanden anderes erstellen.",
				"&cYou cannot create an account for someone else."}));
		languageKeys.put(base+"Account.Open.OpenCostExistButNoMainAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu kannst keinen Account erstellen, da die Erstellung Geld kosten, jedoch kann es von keinem Account der Kategorie MAIN abgezogen werden!",
				"&cYou can not create an account, because the creation cost money, but it can not be deducted from any account of the category MAIN!"}));
		languageKeys.put(base+"Account.Open.NotEnoughMoney"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast die Accounteröffnung nicht genug Geld. Benötigt sind &r%format%&c.",
				"&cYou do not have the account opening enough money. Needed are &r%format%&c."}));
		languageKeys.put(base+"Account.Open.AccountOpen"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Account &f%acid% | %acowner% | %eeet% | %acname% | %act% | %acc% | %ec% &ewurde für &r%format% &eerstellt.",
				"&eThe account &f%acid% | %acowner% | %eeet% | %acname% | %act% | %acc% | %ec% &ewas for &r%format% &ereated!"}));
		languageKeys.put(base+"Account.Overdue.FirstLine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eEs wurden &f%count% &eSpieler gefunden, welche seid mindestens &f%days% &eTagen nicht mehr online waren.",
				"&eFound &f%count% &eplayers who have not been online for at least &f%days% &edays."}));
		languageKeys.put(base+"Account.Overdue.LastLogin"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cLetzer Login: &f%date%",
				"&cLast Login: &f%date%"}));
		languageKeys.put(base+"Account.Overdue.AccountCount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAnzahl gehörende Accounts: &f%count%",
				"&cNumber of owned accounts: &f%count%"}));
		languageKeys.put(base+"Account.Overdue.AccountValue"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cInsgesamte Geldbeträge:",
				"&cTotal amount of money:"}));
		languageKeys.put(base+"Account.PermissionInfo.NoAccountFound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cKeine Accounts vorhanden!",
				"&cNo accounts available!"}));
		languageKeys.put(base+"Account.PermissionInfo.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&cPermissionInfo &b%player%&7]&e=====",
				"&e=====&7[&cPermissionInfo &b%player%&7]&e====="}));
		languageKeys.put(base+"Account.PermissionInfo.HoverInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e>>> &fHover Erklärung &e<<<",
				"&e>>> &fHover Explanation &e<<<"}));
		languageKeys.put(base+"Account.SetDefault.CannotSetAsDefault"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu kannst diesen Account nicht als DefaultAccount setzten!",
				"&cYou can not set this account as DefaultAccount!"}));
		languageKeys.put(base+"Account.SetDefault.CannotSetAsDefaultPerPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu könntest diesen Account zwar als DefaultAccount setzten, jedoch hast du für diese AccountCategory die Permission nicht!",
				"&cYou could set this account as DefaultAccount, but you dont have the permission for this AccountCategory!"}));
		languageKeys.put(base+"Account.SetDefault.AccountNotDefault"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Account &f%acname%(%acowner%) &eist nun kein DefaultAccount in der Kategorie &f%cat% &emehr!",
				"&eThe account &f%acname%(%acowner%) &eis now no defaultaccount in the category &f%cat% &eanymore!"}));
		languageKeys.put(base+"Account.SetDefault.SetDefaultAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Account &f%acname%(%acowner%) &ewurde als DefaultAccount in der Kategorie &f%cat% &eeingesetzt.",
				"&eThe account &f%acname%(%acowner%) &ehas been set as the default account in the category &f%cat%&e."}));
		languageKeys.put(base+"Account.SetName.NameAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEin Account names %newname% für die angegebene Entity existiert schon!",
				"&cAn account names %newname% for the specified entity already exists!"}));
		languageKeys.put(base+"Account.SetName.SetName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Account &f%acname%(%acowner%) &ewurde in &f%newname% &eumbenannt.",
				"&eThe account &f%acname%(%acowner%) &ehas been renamed to &f%newname%&e."}));
		languageKeys.put(base+"Account.SetOwner.SetOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Account &f%acname%(%acowner%) &ehat einen neuen Eigentümer &f%newowner%(%newownertype%)&e.",
				"&eThe account &f%acname%(%acowner%) &ehas a new owner &f%newowner%(%newownertype%)&e."}));
		languageKeys.put(base+"Account.SetQuickPay.SetQuickPay"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Account &f%acname% &ewurde als QuickPayAccount für die Währung &f%currency% &egesetzt.",
				"&eThe account &f%acname%(%acowner%) &ehas a new owner &f%newowner%(%newownertype%)&e."}));
		
		languageKeys.put(base+"DeleteLog.LogNotExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Log-Eintrag existiert nicht!",
				"&cThe log entry does not exist!"}));
		languageKeys.put(base+"DeleteLog.LogWasDeleted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Log-Eintrag Nummer %id% wurde &cgelöscht&e!",
				"&eThe log entry number %id% was &cdeleted&e!"}));
		
		languageKeys.put(base+"DeleteAllPlayerAccounts.Delete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Spieler &f%player% &ewurde mit seinen &f%amount% &egelöscht.",
				"&eThe player &f%player% &ehas been &deleted with his &f%amount%&e."}));
		languageKeys.put(base+"DeleteAllPlayerAccounts.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eGelöschtes Geld:~!~",
				"&eDeleted money:~!~"}));
		
		languageKeys.put(base+"Player.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2AEP &b%player%&7]&e=====",
				"&e=====&7[&2AEP &b%player%&7]&e====="}));
		languageKeys.put(base+"Player.AccountColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&b%account%"}));
		languageKeys.put(base+"Player.AccountSeperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7, "}));
		languageKeys.put(base+"Player.AccountID"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&cID: &f%id%"}));
		languageKeys.put(base+"Player.AccountOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEigentümer: &f%owner%",
				"&cOwner: &f%owner%"}));
		languageKeys.put(base+"Player.AccountBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cKontostand: &f%balance%",
				"&cBalance: &f%balance%"}));
		languageKeys.put(base+"Player.AccountTypeAndCategory"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cType: &f%type% &r, &cKategorie: &f%category%",
				"Type: &f%type% &r, &cCategory: &f%category%"}));
		languageKeys.put(base+"Player.AccountPredefined"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cVordefiniert: &f%predefined%",
				"&cPredefined: &f%predefined%"}));
		languageKeys.put(base+"Player.AccountDefault"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDefault Account: &f%default%",
				"&cDefault Account: &f%default%"}));
		languageKeys.put(base+"Player.AccountQuickPay"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cQuickPay: &f%quickpay%",
				"&cQuickPay: &f%quickpay%"}));
		languageKeys.put(base+"Player."+AccountManagementType.CAN_ADMINISTRATE_ACCOUNT.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&c◆ &f- Kann den Account administrieren.",
				"&c◆ &f- Can administer the account."}));
		languageKeys.put(base+"Player."+AccountManagementType.CAN_SET_OWNERSHIP.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&4● &f- Kann den Account einem anderen Spieler übertragen.",
				"&4● &f- Can transfer the account to another player."}));
		languageKeys.put(base+"Player."+AccountManagementType.CAN_WITHDRAW.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6◈ &f- Kann Geld vom Account abheben.",
				"&6◈ &f- Can withdraw money from the account."}));
		languageKeys.put(base+"Player."+AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e◉ &f- Kann diesen Account als Default in seiner Kategorie setzten.",
				"&e◉ &f- Can set this account as default in its category."}));
		languageKeys.put(base+"Player."+AccountManagementType.CAN_SEE_LOG.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&9◌ &f- Kann den Account Action und Trendlog einsehen.",
				"&9◌ &f- Can view the account action and trend log."}));
		languageKeys.put(base+"Player."+AccountManagementType.CAN_SEE_BALANCE.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&d▢ &f- Kann den Kontostand einsehen.",
				"&d▢ &f- Can view the account balance."}));
		languageKeys.put(base+"Player."+AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7◬ &f- Kann Benachrichtungen erhalten, wenn Geldtransaktionen durchgeführt werden.",
				"&7◬ &f- Can receive notifications when money transactions are made."}));
		
		languageKeys.put(base+"Player.Only."+AccountManagementType.CAN_ADMINISTRATE_ACCOUNT.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&c◆",
				"&c◆"}));
		languageKeys.put(base+"Player.Only."+AccountManagementType.CAN_SET_OWNERSHIP.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&4●",
				"&4●"}));
		languageKeys.put(base+"Player.Only."+AccountManagementType.CAN_WITHDRAW.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6◈",
				"&6◈"}));
		languageKeys.put(base+"Player.Only."+AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e◉",
				"&e◉"}));
		languageKeys.put(base+"Player.Only."+AccountManagementType.CAN_SEE_LOG.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&9◌",
				"&9"}));
		languageKeys.put(base+"Player.Only."+AccountManagementType.CAN_SEE_BALANCE.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&d▢",
				"&d▢"}));
		languageKeys.put(base+"Player.Only."+AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7◬",
				"&7◬"}));
		
		languageKeys.put(base+"ReComment.NoOrderer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu bist nicht der Auftraggeber des Logs!",
				"&cYou are not order of the log!"}));
		languageKeys.put(base+"ReComment.LogNotExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Log-Eintrag existiert nicht!",
				"&cThe log entry does not exist!"}));
		languageKeys.put(base+"ReComment.CommentWasChange"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDie Notiz vom Log &f#%id% &ewurde geändert in: &r%category% %comment%",
				"&eThe note from log &f#%id% &was changed to: &r%category% %comment%"}));
		languageKeys.put(base+"NotEnoughArguments"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cFür den Befehl &f%cmd% &cmuss mindestens %amount% Argumente angegeben werden!",
				"&cFor the command &f%cmd% &cmust be specified at least %amount% arguments!"}));
		languageKeys.put(base+"QuickPayDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cFür die Währung existiert kein QuickPayAccount!",
				"&cFor the currency do not exist a QuickPayAccount!"}));
		languageKeys.put(base+"Balance.NotCorrectEconomyEntityEconomyType"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDas Argument &f%arg% &cist kein EconomyEntityType!",
				"&cThe argument &f%arg% &cis not an EconomyEntityType!"}));
		languageKeys.put(base+"Balance.Cmd.Balance.UUIDIsNull.ENTITY"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDas Entity mit dem &f%name% &cexistiert nicht!",
				"&cThe entity with the &f%name% &cexists not!"}));
		languageKeys.put(base+"Balance.Cmd.Balance.UUIDIsNull.SERVER"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDas Server mit dem &f%name% &cexistiert nicht!",
				"&cThe server with the &f%name% &cexists not!"}));
		languageKeys.put(base+"Balance.Cmd.Balance.UUIDIsNull.PLAYER"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDas Spieler mit dem &f%name% &cexistiert nicht!",
				"&cThe player with the &f%name% &cexists not!"}));
		languageKeys.put(base+"Balance.HaveNotOneAccountToSeeTheBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast keinen einzigen Account, wo du das Guthaben einsehen kannst!",
				"&cYou dont have a single account where you can see the balance!"}));
		languageKeys.put(base+"Balance.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&f=== &eKonten, welche &c%player% &eeinsehen kann &f===",
				"&f=== &eAccounts that &c%player% &ecan see &f==="}));
		languageKeys.put(base+"Balance.AccountDisplay.IsOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				" &6%account%&7: &f%balance%",
				" &6%account%&7: &f%balance%"}));
		languageKeys.put(base+"Balance.AccountDisplay.IsAdmin"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				" &d%account%&7: &f%balance%",
				" &d%account%&7: &f%balance%"}));
		languageKeys.put(base+"Balance.AccountDisplay.CanSeeLog"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				" &b%account%&7: &f%balance%",
				" &b%account%&7: &f%balance%"}));
		languageKeys.put(base+"Balance.AccountDisplay.CanSeeBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				" &7%account%&7: &f%balance%",
				" &7%account%&7: &f%balance%"}));
		languageKeys.put(base+"Balance.AccountDisplay.CannotSeeBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				" &c%account%&7",
				" &c%account%&7"}));
		languageKeys.put(base+"Balance.AccountDisplay.Info"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eⒾ~click@RUN_COMMAND@%cmd%~hover@SHOW_TEXT@&eKlicke+hier+für+die+Account+Infos!",
				"&eⒾ~click@RUN_COMMAND@%cmd%~hover@SHOW_TEXT@&eClick+here+for+the+infos+of+the+account!"}));
		languageKeys.put(base+"Balance.AccountDisplay.InfoDeny"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7Ⓘ~hover@SHOW_TEXT@&cFür+die+Account+Infos+hast+du+keine+Rechte!",
				"&7Ⓘ~hover@SHOW_TEXT@&cFor+the+account+info+you+have+no+rights!"}));
		languageKeys.put(base+"Balance.AccountDisplay.ActionLog"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&dⒶ~click@RUN_COMMAND@%cmd%+%player%+%account%+~hover@SHOW_TEXT@&eKlicke+hier+für+den+Account+Actionlog!",
				"&dⒶ~click@RUN_COMMAND@%cmd%+%player%+%account%+~hover@SHOW_TEXT@&eClick+here+for+the+actionlog+of+the+account!"}));
		languageKeys.put(base+"Balance.AccountDisplay.ActionLogDeny"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7Ⓐ~hover@SHOW_TEXT@&cFür+den+Actionlog+hast+du+keine+Rechte!",
				"&7Ⓐ~hover@SHOW_TEXT@&cFor+the+actionlog+you+have+no+rights!"}));
		languageKeys.put(base+"Balance.AccountDisplay.TrendLog"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&9Ⓣ~click@RUN_COMMAND@%cmd%+%player%+%account%+~hover@SHOW_TEXT@&eKlicke+hier+für+den+Account+Trendlog!",
				"&9Ⓣ~click@RUN_COMMAND@%cmd%+%player%+%account%+~hover@SHOW_TEXT@&eClick+here+for+the+trendlog+of+the+account!"}));
		languageKeys.put(base+"Balance.AccountDisplay.TrendLogDeny"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7Ⓣ~hover@SHOW_TEXT@&cFür+den+Trendlog+hast+du+keine+Rechte!",
				"&7Ⓣ~hover@SHOW_TEXT@&eFor+the+trendlog+you+have+no+rights!"}));
		languageKeys.put(base+"Pay.PlayerIsNotRegistered"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Spieler ist nicht in der Datenbank registriert!",
				"&cThe player is not registered in the database!"}));
		languageKeys.put(base+"Pay.ShortPayAccountDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDein festgelegter QuickPay-Account existiert nicht!",
				"&cYour specified QuickPay account does not exist!"}));
		languageKeys.put(base+"Pay.AccountDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer angegbene Aaccount existiert nicht!",
				"&cThe specified account does not exist!"}));
		languageKeys.put(base+"Pay.TargetAccountDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Zielaccount existiert nicht!",
				"&cThe target account does not exist!"}));
		languageKeys.put(base+"Pay.SameAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBeide angesteuerten Account sind der selbe Account!",
				"&cBoth controlled account are the same account!"}));
		languageKeys.put(base+"Pay.NotSameCurrency"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBeide angesteuerten Account teilen nicht die selbe Währung!",
				"&cBoth controlled account do not share the same currency!"}));
		languageKeys.put(base+"Pay.StartAccountDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer angesteuerte StartAccount existiert nicht!",
				"&cThe controlled StartAccount does not exist!"}));
		languageKeys.put(base+"Pay.Transaction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Es wurden von dem Konto &e%fromaccount% &f%formatwithdraw% &6abgezogen und &f%formatdeposit% &6an &e%toaccount% &6überwiesen.",
				"&eGezahlte Steuern&7: &f%formattax%",
				"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
				"&6It was deducted from the account &e%fromaccount% &f%formatwithdraw% &6and transferred &f%formatdeposit% &6to &e%toaccount%&6.",
				"&eTaxes paid&7: &f%formattax%",
				"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
		languageKeys.put(base+"Give.Deposit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Es wurden auf das Konto &e%formaccount% &f%formatdeposit% &6eingezahlt.",
				"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
				"&6There were deposited on the account &f%formaccount% &f%formatdeposit%&6.",
				"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
		languageKeys.put(base+"Take.Withdraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Es wurden von dem Konto &e%fromaccount% &f%formatwithdraw% &6abgezogen.",
				"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
				"&6There were deducted from the account &f%fromaccount% &f%formatwithdraw%&6.",
				"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
		languageKeys.put(base+"Take.Transaction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Es wurden von dem Konto &e%fromaccount% &f%formatwithdraw% &6abgezogen und &f%formatdeposit% &6an &e%toaccount% &6überwiesen.",
				"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
				"&6It was deducted from the account &e%fromaccount% &f%formatwithdraw% &6and transferred &f%formatdesposit% &6to &e%toaccount%&6.",
				"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
		languageKeys.put(base+"Set.Setting"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Das Konto &e%fromaccount% &6wurde auf &f%formatwithdraw% &6gesetzt.",
				"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
				"&6The &e%fromaccount% &6was set to &f%formatwithdraw%&6.",
				"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
		languageKeys.put(base+"Set.Transaction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Das Konto &e%fromaccount% &6wurde auf &f%formatdeposit% &6gesetzt. &f%formatwithdraw% &6wurde an &e%toaccount% &6überwiesen.",
				"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
				"&6The &e%fromaccount% &6was set to &f%formatdeposit%&6. &f%formatwithdraw% &6has been transferred to &e%toaccount%&6.",
				"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
		languageKeys.put(base+"WalletNotification.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu bekommst nun keine Benachrichtigung, wenn Transaktionen auf Accounts(Wallet) stattfinden, auf den du Benachrichtigungsrechte hast.",
				"&eYou will now not receive notifications when transactions take place on accounts (wallet) to which you have notification rights."}));
		languageKeys.put(base+"WalletNotification.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu bekommst nun Benachrichtigung, wenn Transaktionen auf Accounts(Wallet) stattfinden, auf den du Benachrichtigungsrechte hast.",
				"&eYou will now receive notification when transactions take place on accounts (wallet) to which you have notification rights."}));
		languageKeys.put(base+"BankNotification.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu bekommst nun keine Benachrichtigung, wenn Transaktionen auf Accounts(Bank) stattfinden, auf den du Benachrichtigungsrechte hast.",
				"&eYou will now not receive notifications when transactions take place on accounts (bank) to which you have notification rights."}));
		languageKeys.put(base+"BankNotification.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu bekommst nun Benachrichtigung, wenn Transaktionen auf Accounts(Bank) stattfinden, auf den du Benachrichtigungsrechte hast.",
				"&eYou will now receive notifications when transactions take place on accounts (bank) to which you have notification rights."}));
		languageKeys.put(base+"Top.NotEnoughValues"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEs gibt nicht genügend Werte für diese Seitenzahl!",
				"&cThere are not enough values for this page number!"}));
		languageKeys.put(base+"Top.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=======&7[&2AEP %currency% &bTop &eSeite %page%&7]&e=======",
				"&e=======&7[&2AEP %currency% &bTop &epage %page%&7]&e======="}));
		languageKeys.put(base+"Top.TopLine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e%place%. &8%accountid%-&b%owner%&f-&6%accountname%: &r%format%",
				"&e%place%. &8%accountid%-&b%owner%&f-&6%accountname%: &r%format%"}));
		languageKeys.put(base+"GetTotal.PreLine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eIm Geldsystem befindet sich folgende Mengen:",
				"&eThe following quantities are in the monetary system:"}));
		languageKeys.put(base+"GetTotal.Line"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6%currency%&b: %format%",
				"&6%currency%&b: %format%"}));
		languageKeys.put(base+"PayThroughGui.Lore"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Accountname: &f%acn%",
				"&dKontostand: &f%bal%",
				"&4Eigentümer: &f%own%",
				"&eAccountKategorie: &f%acc%",
				"&cTyp: &f%owt%",
				"&dAccountType: &f%act%",
				"&6Accountname: &f%acn%",
				"&dBalance: &f%bal%",
				"&4Owner: &f%own%",
				"&eAccountCategory: &f%acc%",
				"&cTyp: &f%owt%",
				"&dAccountType: &f%act%"}));
		languageKeys.put(base+"PayThroughGui.LoreWithOutBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Accountname: &f%acn%",
				"&4Eigentümer: &f%own%",
				"&eAccountKategorie: &f%acc%",
				"&cTyp: &f%owt%",
				"&dAccountType: &f%act%",
				"&6Accountname: &f%acn%",
				"&4Owner: &f%own%",
				"&eAccountCategory: &f%acc%",
				"&cTyp: &f%owt%",
				"&dAccountType: &f%act%"}));
		languageKeys.put(base+"PayThroughGui.Sender"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Absender: ",
				"Sender: "}));
		languageKeys.put(base+"PayThroughGui.Receiver"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Empfänger: ",
				"Receiver: "}));
		languageKeys.put(base+"PayCategorySuggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Überweisung",
				"Sparen",
				"Kredittilgung",
				"Auftrag",
				"transfer",
				"saving",
				"loanrepayment",
				"assignment"}));
	}
	
	private void langLoan()
	{
		String base = "Cmd.Loan.";
		languageKeys.put(base+"CurrencyDontAllowLoan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Währung &f%currency% &cerlaubt keine Kredite!",
				"&cThe currency &f%currency% &cdont allow loans!"}));
		languageKeys.put(base+"NoLoans"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast keine Kredite!",
				"&cYou dont have loans!"}));
		languageKeys.put(base+"ConfirmTerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"bestätigen",
				"confirm"}));
		languageKeys.put(base+"PleaseConfirm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBitte+bestätige+mit+&f%cmd%&c,+oder+klicke+hier!~click@SUGGEST_COMMAND@%cmd%",
				"&cPlease+confirm+with+&f%cmd%&c,+or+click+here!~click@SUGGEST_COMMAND@%cmd%"}));
		languageKeys.put(base+"LoanDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Kredit existiert nicht!",
				"&cThe loan does not exist!"}));
		languageKeys.put(base+"NoToAcceptLoan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDir hat niemand ein Kredit vorgeschlagen!",
				"&cNo one has suggested a loan to you!"}));
		languageKeys.put(base+"AlreadyHavingProposal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cSpieler hat schon einen Kreditvorschlag bekommen. Dieser muss er zuerst ablehenen.",
				"&cSpieler has already received a loan proposal. He has to reject it first."}));
		languageKeys.put(base+"NoWaitingLoanProposal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast keinen Kredit in Bearbeitung!",
				"&cYou have no loan in process!"}));
		languageKeys.put(base+"AlreadyWaitingLoanProposal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast schon einen Kredit in Bearbeitung!",
				"&cYou already have a loan in process!"}));
		languageKeys.put(base+"ThereAreNoPlayers"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie angegebenen Spieler müssen auch existieren!",
				"&cThe specified players must also exist!"}));
		languageKeys.put(base+"YouCantBeCreateALoanIfYouHaveNoWithdrawRight"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu kannst keinen Kredit erstellen, wenn du vom Kreditgeberaccount keine Abhebungsrechte hast!",
				"&cYou cant create a loan if you don't have withdrawal rights from the lender account!"}));
		languageKeys.put(base+"YouMustBeTheAccountOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu musst der Eigentümer des Accountes sein, welcher den Kredit vergibt!",
				"&cYou must be the owner of the account that grants the credit!"}));
		languageKeys.put(base+"YouCantBeTheOwnerOfYourOwnLoan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu kannst nicht der Krediteigentümer sein, wenn der Kredit an dich geht!",
				"&cYou cannot be the loan owner if the loan goes to you!"}));
		languageKeys.put(base+"ContractAtTheExpenseOfThirdParties"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu darfst keine Kredite zu Lasten Dritter beschließen!",
				"&cYou may not decide on loans at the expense of third parties!"}));
		languageKeys.put(base+"LoanAlreadyPaidOff"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cKredit ist schon abbezahlt!",
				"&cCredit is already paid off!"}));
		languageKeys.put(base+"LoanAlreadyForgiven"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cRestkredit ist schon vergeben worden!",
				"&cRestloan has already been forgiven!"}));
		languageKeys.put(base+"HoverInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eID: &f%id%~!~&eName: &f%name%~!~&eKreditnehmer: &f%fromaccount% (%fromowner%)~!~&eKreditgeber: &f%toaccount% (%toowner%)~!~&eKrediteigentümer: &f%owner%~!~&eSchuldner: &f%debtor%~!~&eStartdatum: &f%st%~!~&eEnddatum: &f%et%~!~&eRatenzyklus: &f%rt%~!~&eBetrag: &r%apsf%&r/&3%ta% (%apsfip% &r%)~!~&eRatenbetrag: &f%ar%~!~&eZinssatz: &f%in% %~!~&eBisher gezahlte Steuern: &f%tax%~!~&eIst Pausiert: &f%pa%~!~&eIst Vergeben: &e%fo%~!~&eIst Abbezahlt: &f%fi%",
				"&eID: &f%id%~!~&eName: &f%name%~!~&eBorrower: &f%fromaccount% (%fromowner%)~!~&eLenders: &f%toaccount% (%toowner%)~!~&eLoanOwner: &f%owner%~!~&eDebtor: &f%debtor%~!~&eStartdate: &f%st%~!~&eEnddate: &f%et%~!~&eRatecycle: &f%rt%~!~&eAmount: &r%apsf%&r/&3%ta% (%apsfip% &r%)~!~&eInstalmentamount: &f%ar%~!~&eInterestrate: &f%in% %~!~&eTaxes paid to date: &f%tax%~!~&eIs Paused: &f%pa%~!~&eIs forgiven: &e%fo%~!~&eIs paid off: &f%fi%"}));
		languageKeys.put(base+"Accept.YouHaveAccepted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast das Kreditangebot von &6%drowner% &f%name% &eangenommen!",
				"&You have accepted the credit offer of &6%drowner% &f%name%."}));
		languageKeys.put(base+"Accept.PayerHasAccepted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Spieler &f%player% &ehast das Kreditangebot &e%name% &evon &f%toplayer% &eangenommen.",
				"&eThe player &f%player% &ehas accepted the credit offer &e%name% &f%toplayer%."}));
		languageKeys.put(base+"Amount.SetsAmounts"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Kredit &f%name% &ewurde bearbeitet.",
				"&eThe loan &f%name% &ewas processed."}));
		languageKeys.put(base+"Amount.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKredit: &r%la%~!~&b(Betrag ohne Zinsen und Steuern, wird an den Schuldner überwiesen)~!~&eZinsen: &r%in% %~!~Steuern: &r%tax% %~!~Gesamtbetrag: &r%ta%~!~&b(Zinsen und Steuern schon mit eingerechnet, muss der Schuldner an Verleiher und Server zurückzahlen~!~&bDer Server bekommt von der Raten, die Prozent welche als Steuern hinterlegt wurde.)~!~&eRaten: &r%am%~!~Vorraussichtlich Anzahl an Zahlungen: &r%min%",
				"&ETotal amount: &r%ta% %currency% (interest already included)~!~&eRates: &r%am% %currency%~! &eInterest: &r%in% %~! &r%min%"}));
		languageKeys.put(base+"Cancel.IsCancelled"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast die Krediterstellung abgebrochen!",
				"&eYou have cancelled the credit creation!"}));
		languageKeys.put(base+"Create.isCreate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast einen Kredit erstellt. Bitte setzte zuerst die anderen Einstellungen, bevor du ihn abschickst.",
				"&eYou have created a loan. Please set the other settings first before you send it."}));
		languageKeys.put(base+"Forgive.CanBeUndone"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Schulden des Kredit sind schon vergeben worden. Es kann nicht rückgängig gemacht werden!",
				"&cThe debts of the loan have already been forgiven. It cannot be reversed!"}));
		languageKeys.put(base+"Forgive.LoanIsForgiven"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer verbleibende Kreditbetrag von &r%dif%&e, welcher Spieler &f%debtor% %owner% &eschuldet wurde von Spieler &f%player% &evergeben.",
				"&eThe remaining credit amount of &r%dif%&e, which player &f%debtor% %owner% &eowed by player &f%player% &was &credited by player"}));
		languageKeys.put(base+"Info.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e===&7[&bKreditinfo &3%id%&f-&6%name%&7]&e===",
				"&e===&7[&bLoaninfo &3%id%&f-&6%name%&7]&e==="}));
		languageKeys.put(base+"Info.Participants"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&c%debtor% &ehat geliehen von &a%owner%",
				"&c%debtor% &has borrowed from &a%owner%"}));
		languageKeys.put(base+"Info.Accounts"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&c%accountfrom% &f(&c%fromowner%&f:&c%fromid%&f) &a%accountto% &f(&a%toowner%&f:&a%toid%&f)",
				"&c%accountfrom% &f(&c%fromowner%&f:&c%fromid%&f) &a%accountto% &f(&a%toowner%&f:&a%toid%&f)"}));
		languageKeys.put(base+"Info.Amounts"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBeträge: &a%amountpaidsofar%&f/&4%totalamount%",
				"&cAmounts: &a%amountpaidsofar%&f/&4%totalamount%"}));
		languageKeys.put(base+"Info.Interest"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cZinssatz: &f%interest% % &1| &cZinsen: &r%interestamount%",
				"&cInterest rate: &f%interest% % &1| &cInterest: &r%interestamount%"}));
		languageKeys.put(base+"Info.Tax"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cSteuersatz: &f%taxrate% % &1| &cBisher an Steuern gezahlt: &r%amountpaidtotax%",
				"&cTaxrate: &f%taxrate% % &1| &cTaxes paid so far: &r%amountpaidtotax%"}));
		languageKeys.put(base+"Info.Ratio"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cZeitzyklus: &r%repeatingtime% &1| &cRatebetrag: &r%amountratio% (&cDavon als Steuer: &r%tax%)",
				"&cTime cycle: &r%repeatingtime% &1| &cRate amount: &r%amountratio% (&cThereof as tax: &r%tax%)"}));
		languageKeys.put(base+"Info.Times"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cStartzeit: &r%starttime% &1| &cEndzeit: &r%endtime%",
				"&cStart time: &r%starttime% &1| &cEnd Time: &r%endtime%"}));
		languageKeys.put(base+"Info.TheoreticalNumberOfRates"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cTheoretische Anzahl an noch benötigten Zahlungen: %r%theoreticalnumber%",
				"&cTheoretical number of payments still needed: %r%theoreticalnumber%"}));
		languageKeys.put(base+"Inherit.SomeoneInherit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast dem Spieler &f%newplayer% &eden Kredit &f%id% %e| &f%name% &evon &f%oldplayer% &eerben lassen.",
				"&eYou have herited the player &f%newplayer% &the loan &f%id% %e| &f%name% &eof &f%oldplayer%."}));
		languageKeys.put(base+"Inherit.YouInherit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Spieler &f%player% &ehat dich den Kredit &f%id% &e| &f%name% &evon &f%oldplayer% &eerben lassen.",
				"&eThe player &f%player% &ehas left you the loan &f%id% &e| &f%name% &eof &f%oldplayer% &leave you an inheritance"}));
		languageKeys.put(base+"List.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Kredite von &b%player%&7]&e=====",
				"&e=====&7[&2Loans von &b%player%&7]&e====="}));
		languageKeys.put(base+"Pause.Unpaused"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Spieler &f%player% &ehat den Kredit &f%name% &ewieder aufgenommen. Zahlungen werden bald erfolgen!",
				"&EThe player &f%player% &ehas taken out the loan &f%name% &eresumed. Payments will be made soon!"}));
		languageKeys.put(base+"Pause.Pause"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Spieler &f%player% &ehat den Kredit &f%name% &epausiert. Zahlungen werden ausbleiben solange pausiert wird!",
				"&eThe player &f%player% &ehas the loan &f%name% &epaused. Payments will not be made as long as the player is paused!"}));
		languageKeys.put(base+"Payback.IsAlreadyPaidOff"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Kredit kann nicht zurückgezahlt werden, da er schon vollständig beglichen ist!",
				"&cThe loan cannot be repaid because it is already fully paid!"}));
		languageKeys.put(base+"Payback.IsPayedBack"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Kredit &f%id% &e| &f%name% &ewurde von &f%player% &ean &f%to% &ezurückgezahlt.",
				"&cThe loan &f%id% &e| &f%name% &ewas repaid by &f%player% &ean &f%to%."}));
		languageKeys.put(base+"Reject.isRejecting"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&f%player% &ehat das Kreditangebot &f%name% &evon &f%to% &eabgelehnt!",
				"&f%player% &ehas rejected the loan offer &f%name% &of &f%to% &rejected!"}));
		languageKeys.put(base+"Reject.isCancelled"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAngebot wegen Ablehnung abgebrochen!",
				"&cOffer cancelled due to rejection!"}));
		languageKeys.put(base+"Repay.IsNotYourLoan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAchtung! &eDu bist dabei einen Kredit abzuzahlen, welcher nicht deiner ist!",
				"&cAttention! &You are about to pay off a loan that is not yours!"}));
		languageKeys.put(base+"Repay.RepayMoreThanNeeded"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu wolltest mehr abbezahlen als noch ausstand. Bezahlt wurde nur &f%dif% &eanstatt &r%amount% %currency%&e. Damit ist die Kredit &f%name% &eabgezahlt.",
				"&eYou wanted to pay off more than what was outstanding. You only paid &f%dif% &einstead of &r%amount% %currency%&e. So the loan &f%name% &eis &epaid off."}));
		languageKeys.put(base+"Repay.RepayedAmount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast vom Kredit &f%name% %amount% &r%currency% abbezahlt.",
				"&eYou have paid off the loan &f%name% %amount% &r%currency%."}));
		languageKeys.put(base+"Send.YourProposal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast dem Spieler &f%player% &eeinen Kreditvorschlag gesendet. Hover mit der Maus über diese Nachricht um alle Einzelheiten angezeigt zu bekommen.",
				"&eYou have sent &f%player% &aloan proposal to the player. Hover with your mouse over this message to get all the details."}));
		languageKeys.put(base+"Send.AProposal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Spieler &f%player% &ehat dir einen Kreditvorschlag gesendet. Hover mit der Maus über diese Nachricht um alle Einzelheiten angezeigt zu bekommen.",
				"&eThe player &f%player% &ehas sent you a loan proposal. Hover with your mouse over this message to get all the details."}));
		languageKeys.put(base+"Send.AcceptReject"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aAkzeptieren~click@SUGGEST_COMMAND@%acceptcmd% &f| &cAblehnen~click@SUGGEST_COMMAND@%rejectcmd%",
				"&eAccept~click@SUGGEST_COMMAND@%acceptcmd% &f| &cReject~click@SUGGEST_COMMAND@%rejectcmd%"}));
		languageKeys.put(base+"Times.SetTimes"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Kredit &f%name% &ewurde bearbeitet.",
				"&eThe loan &f%name% &ewas processed."}));
		languageKeys.put(base+"Times.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eStartdatum: &f%st%~!~&eEnddatum: &f%et%~!~&eRatenzyklus: &f%rt%",
				"&eStartdate: &f%st%~!~&eEnddate: &f%et%~!~&eRate cycle: &f%rt%"}));
		languageKeys.put(base+"Transfer.YouHasTransfered"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast dem Spieler &f%player% &eden Kredit &f%id% %e| &f%name% &eübertragen. &cEr/Sie ist nun der Krediteigentümer, welcher die Zahlungen erhält!",
				"&eYou have transferred the loan &f%id% %e| &f%name% &to the player &f%player%. &cHe/She is now the credit owner who receives the payments!"}));
		languageKeys.put(base+"Transfer.YouHasBecomeLoanOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Spieler &f%player% &ehat dir den Kredit &f%id% %e| &f%name% &eübertragen. &aAb jetzt bekommst du die Begleichszahlungen!",
				"&eThe player &f%player% &ehas given you the loan &f%id% %e| &f%name% &transferred. &aFrom now on you will get the payments!"}));
	}
	
	private void langStandingOrder()
	{
		String base = "Cmd.StandingOrder.";
		languageKeys.put(base+"NoStandingOrders"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast keine Daueraufträge!",
				"&cYou have no standing orders!"}));
		languageKeys.put(base+"CurrencyDontMayUseStandingOrder"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDie Währung %currency% darf keine Dauerauftrage nutzen.",
				"&cThe %currency% currency may not use standing orders."}));
		languageKeys.put(base+"ContractAtTheExpenseOfThirdParties"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu darfst keinen Dauerauftrag zu lasten Dritten erstellen!",
				"&cYou may not create a standing order at the expense of third parties!"}));
		languageKeys.put(base+"ContractAtTheExpenseOfOthersInYourFavour"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu darfst keinen Dauerauftrag zu lasten Dritter und zu deiner Gunst erstellen!",
				"&cYou may not create a standing order at the expense of third parties and for your own benefit!"}));
		languageKeys.put(base+"AlreadyPendingOrder"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast schon einen noch wartenden Dauerauftrag erstellt. Bitte schließe diesen zuerst ab oder breche ihn ab.",
				"&cYou have already created a waiting standing order. Please complete or cancel it first."}));
		languageKeys.put(base+"NoPendingOrder"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast keine noch wartenden Dauerauftrag!",
				"&cYou have no waiting standing order!"}));
		languageKeys.put(base+"OrderDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer angegebende Dauerauftrag existiert nicht!",
				"&cThe specified standing order does not exist!"}));
		languageKeys.put(base+"NotOrderer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu bist nicht der Auftraggeber!",
				"&cYou are not the orderer!"}));
		languageKeys.put(base+"Amount.SetAmount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eBetrag des noch wartenden Dauerauftrages &f%name% &ezu &r%format% &egeändert.",
				"&eAmount of the pending standing order &f%name% &eto &r%format% &echanged."}));
		languageKeys.put(base+"Amount.ChangeAmount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eBetrag des schon existierenden Dauerauftrages &f%name% &eauf &f%format% &egeändert.",
				"&eAmount of the already existing standing order &f%name% &echanged to &f%format%&e."}));
		languageKeys.put(base+"Cancel.IsCancelled"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast deinen noch wartenden Dauerauftrag abgebrochen!",
				"&eYou have cancelled your still waiting standing order!"}));
		languageKeys.put(base+"Create.OrderCreated"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast einen Dauerauftrag erstellt. Bitte setzte nun alle weiteren Einstellungen. &7Klicke hier zum Einsehen aller Informationen.",
				"&eYou have created a standing order. Please set all other settings now. &7Click here to view all information."}));
		languageKeys.put(base+"Delete.IsDeleted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDauerauftrag gelöscht!",
				"&cStanding order deleted!"}));
		languageKeys.put(base+"Info.NoInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast nicht das Recht, die Informationen zu dem Dauerauftrag einzusehen!",
				"&cYou do not have the right to view the information about the standing order!"}));
		languageKeys.put(base+"Info.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e===&7[&bDauerauftrag &f%id% &c✖~click@RUN_COMMAND@%cmd%~hover@SHOW_TEXT@&eBricht+den+noch+wartenden+Dauerauftrag+ab! &4✖~click@SUGGEST_COMMAND@%cmdII%+%id%~hover@SHOW_TEXT@&eLöscht+den+Dauerauftrag! &7]&e===",
				"&e===&7[&bStanding Order &f%id% &c✖~click@RUN_COMMAND@%cmd%~hover@SHOW_TEXT@&eCancels+the+still+waiting+standing+order! &4✖~click@SUGGEST_COMMAND@%cmdII%+%id%~hover@SHOW_TEXT@&eDelete+the+standing+order! &7]&e==="}));
		languageKeys.put(base+"Info.Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cName: &f%name%",
				"&cName: &f%name%"}));
		languageKeys.put(base+"Info.Owner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEigentümer: &f%owner%",
				"&cOwner: &f%owner%"}));
		languageKeys.put(base+"Info.From"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAbsender: &f%accountname% [%id%] (%ownername%)",
				"&cSender: &f%accountname% [%id%] (%ownername%)"}));
		languageKeys.put(base+"Info.To"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEmpfänger: &f%accountname% [%id%] (%ownername%)",
				"&cReciver: &f%%accountname% [%id%] (%ownername%)"}));
		languageKeys.put(base+"Info.Amount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBetrag: &f%format% &2✏~click@SUGGEST_COMMAND@%cmd%~hover@SHOW_TEXT@&eKlicke+hier+um+den+Betrag+zu+ändern!",
				"&cAmount: &f%format% &2✏~click@SUGGEST_COMMAND@%cmd%~hover@SHOW_TEXT@&eClick+here+to+change+the+amount!"}));
		languageKeys.put(base+"Info.AmountPaidSoFar"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBisher gezahlter Betrag: &f%format%",
				"&cAmount paid so far: &f%format%"}));
		languageKeys.put(base+"Info.AmountPaidToTax"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBisher gezahlte Steuern: &f%format%",
				"&cTaxAmount paid so far: &f%format%"}));
		languageKeys.put(base+"Info.StartTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cStartdatum: &f%starttime% &2✏~click@SUGGEST_COMMAND@%cmd%~hover@SHOW_TEXT@&eNur+noch+wartende+Daueraufträge+können+verändert+werden!",
				"&cStartdate: &f%starttime% &2✏~click@SUGGEST_COMMAND@%cmd%~hover@SHOW_TEXT@&eOnly+waiting+standing+orders+can+be+changed!"}));
		languageKeys.put(base+"Info.EndTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEnddatum: &f%endtime%",
				"&cEnddate: &f%endtime%"}));
		languageKeys.put(base+"Info.RepeatingTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cWiederholungszyklus: &f%repeatingtime% &2✏~click@SUGGEST_COMMAND@%cmd%~hover@SHOW_TEXT@&eKlicke+hier+um+die+Wiederholungszeit+zu+ändern!",
				"&cRepeatingtime: &f%repeatingtime% &2✏~click@SUGGEST_COMMAND@%cmd%~hover@SHOW_TEXT@&eClick+here+to+change+the+repeatingtime!"}));
		languageKeys.put(base+"Info.LastTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cZu letzt gezahlter Zeitpunkt: &f%lasttime%",
				"&cLast paid date: &f%lasttime%"}));
		languageKeys.put(base+"Info.isCancelled"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cUnterbrochen: &f%cancelled%",
				"&cInterrupted: &f%cancelled%"}));
		languageKeys.put(base+"Info.isPaused"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cPausierd: &f%paused%",
				"&cPaused: &f%paused%"}));
		languageKeys.put(base+"List.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Daueraufträge von &b%player%&7]&e=====",
				"&e=====&7[&2StandingOrders von &b%player%&7]&e====="}));
		languageKeys.put(base+"Pause.WasCancelled"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Dauerauftrag wurde abgebrochen! Falls du ihn pausieren möchtest führe den Befehl erneut aus!",
				"&eThe standing order was cancelled! If you want to pause it, execute the command again!"}));
		languageKeys.put(base+"Pause.IsUnpaused"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDauerauftrag &f%id% &eist wiederaufgenommen! Die erste Zahlung wird bald erfolgen! Folgende Zahlungen dann wieder nach dem Zyklus.",
				"&eStanding order &f%id% &mostly resumed! The first payment will be made soon! Subsequent payments then again after the cycle."}));
		languageKeys.put(base+"Pause.IsPaused"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDauerauftrag &f%id% &eist pausiert! Die Zahlungen erst weiter, wenn er wiederaufgenommen wird!",
				"&eStanding order &f%id% &eis paused! The payments only continue when it is resumed!"}));
		languageKeys.put(base+"RepeatingTime.SetRepeatingTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDen Wiederholungszyklus auf &f%rt% &egesetzt.",
				"&eSet the repetition cycle to &f%rt% &set."}));
		languageKeys.put(base+"RepeatingTime.WrongSyntax"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBitte nutze die richtige Zeit Syntax <&fdd-HH:mm&c>!",
				"&cPlease use the right time syntax <&fdd-HH:mm&c>!"}));
		languageKeys.put(base+"StartTime.Amount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu+hast+noch+keinen+Betrag+festgesetzt!+&f%amountcmd%+<Betrag>+&ceingeben+oder+hier+klicken!~click@SUGGEST_COMMAND@%amountcmd%+<Betrag>",
				"&cYou+have+not+yet+an+amount+set!+&f%amountcmd%+<Amount>&enter+or+click here!~click@SUGGEST_COMMAND/%amountcmd%+<Amount>"}));
		languageKeys.put(base+"StartTime.RepeatingTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu+hast+noch+keinen+Wiederholungszyklus+festgesetzt!+&f%repeatingtimecmd%+<dd-HH:mm+Wert>+&ceingeben+oder+hier+klicken!~click@SUGGEST_COMMAND@%repeatingtimecmd%+<dd-HH:mm+Wert>",
				"&cYou+have+no+repeat+cycle+set!+&f%repeatingtimecmd%+<dd-HH:mm+value>+enter+or+click here!~click@SUGGEST_COMMAND@%repeatingtimecmd%+<dd-HH:mm+value>"}));
		languageKeys.put(base+"StartTime.SetStartTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eStartdatum &f%starttime% &egesetzt! Sobald der Zeitpunkt erreicht wird, beginnen die Zahlungen nach dem gesetzten Zyklus.",
				"&eStart date &f%starttime% &set! As soon as the time is reached, payments will start according to the set cycle."}));
		languageKeys.put(base+"StartTime.WrongSyntax"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBitte nutze die richtige Zeit Syntax <&fdd.MM.yyyy-HH:mm:ss&c>!",
				"&cPlease use the right time syntax <&fdd.MM.yyyy-HH:mm:ss&c>!"}));
		languageKeys.put(base+"StartTime.SpamProtection"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAchtung! Spamschutz Warnung! Du unterschreitest die minimale Wiederholungszeit &f(%repeatingtime%) &csowie den Minimalbetrag &f(%amount%)&c! Bitte ändere eine der beiden Variabeln!",
				"&cAttention! Spam protection warning! You fall below the minimum repetition time &f(%repeatingtime%) &cand the minimum amount&f(%amount%)&c! Please change one of the two variables!"}));
		languageKeys.put(base+"EndTime.SetEndTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eEnddatum &f%endtime% &egesetzt! Sobald der Zeitpunkt erreicht wird, endet die Zahlungen.",
				"&eEnddate &f%endtime% &set! As soon as the time is reached, payments will end."}));
		languageKeys.put(base+"Transaction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Es wurden von dem Konto &e%fromaccount% &f%formatwithdraw% &6abgezogen und &f%formatdeposit% &6an &e%toaccount% &6als Dauerauftrag überwiesen.",
				"&eGezahlte Steuern&7: &f%formattax%",
				"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
				"&6It was deducted from the account &e%fromaccount% &f%formatwithdraw% &6and transferred as standingorder &f%formatdeposit% &6to &e%toaccount%&6.",
				"&eTaxes paid&7: &f%formattax%",
				"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
	}
	
	@SuppressWarnings("unused")
	public void initFilterSettings()
	{
		ActualParameter:
		{
			loggerSettingsKeys.put("ActualParameter"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"      §7-------------------------",
					"§eAktuelle Werte:",
					"§bAccountnummer: §f%number%",
					"§bAccountname: §f%accountname%",
					"§bOrderSpalte: §f%ordercolumn%",
					"§bAuftraggeber: §f%orderer%",
					"§bKategorie: §f%category%",
					"§bKommentar: §f%comment%",
					"§bMin > §f%min% | §bMax < §f%max%",
					"§bErstStand > §f%firststand%",
					"§bLetzterStand < §f%laststand%",
					"§bistAbsteigend: §f%descending%",
					
					"      §7-------------------------",
					"§eActual Values:",
					"§bAccountnumber: §f%number%",
					"§bAccountname: §f%accountname%",
					"§bOrderColumn: §f%ordercolumn%",
					"§bOrderer: §f%orderer%",
					"§bCategory: §f%category%",
					"§bComment: §f%comment%",
					"§bMin > §f%min% §f| §bMax < §f%max%",
					"§bFirstStand > §f%firststand%",
					"§bLastStand < §f%laststand%",
					"§bisDescending: §f%descending%",
					}));
			
			loggerSettingsKeys.put("Preset"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§eAktuelle Werte:",
					"§cKeine Voreinstellung vorhanden.",
					"§eActual Values:",
					"§cNo preset available.",
					}));
		}
		Output:
		{
			loggerSettingsKeys.put("31.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&4Ausgabe &6Json zu Excel",
					"&4Output &6Json to Excel"}));
			loggerSettingsKeys.put("31.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"SOUL_CAMPFIRE"}));
			loggerSettingsKeys.put("31.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§4Achtung! §cHier wird ein JSON Text ausgegeben.",
					"§cDieser muss noch durch ein externe Webseite",
					"§cin Excel-Format konvertiert werden!",
					"§9Linksklick §ezur Ausgabe des JSON-Text des","§eAktionlogs nach den jetzigen Parametern.",
					"§9Rechtsklick §ezur Ausgabe des JSON-Text des","§eTrendlogs nach den jetzigen Parametern.",
					"§9Left-click §eto output the JSON-text of the","§eaction log according to the current parameters.",
					"§9Right-click §eto output the JSON-text of the","§etrend log according to the current parameters.",
					"§4Attention! §cHere a JSON text is output.",
					"§cThis still has to be converted into CSV or Excel",
					"§cformat by an external website!",}));
			
			loggerSettingsKeys.put("40.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&4Ausgabe &6BarChart",
					"&4Output &6BarChart"}));
			loggerSettingsKeys.put("40.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"BIRCH_DOOR"}));
			loggerSettingsKeys.put("40.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§9Klick §ezum Aufrufen des BarCharts des","§eAktionlogs nach den jetzigen Parametern.",
					"§9Click §eto call the barchart of the","§eaction log according to the current parameters."}));
			
			loggerSettingsKeys.put("48.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&4Ausgabe &6Diagramm",
					"&4Output &6Diagram"}));
			loggerSettingsKeys.put("48.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"OAK_DOOR"}));
			loggerSettingsKeys.put("48.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§9Linksklick §ezum Aufrufen des Diagramms des","§eAktionlogs nach den jetzigen Parametern.",
					"§9Rechtsklick §ezum Aufrufen des Diagramms des","§eTrendlogs nach den jetzigen Parametern.",
					"§9Left-click §eto call the diagram of the","§eaction log according to the current parameters.",
					"§9Right-click §eto call the diagram of the","§etrend log according to the current parameters.",}));
			
			loggerSettingsKeys.put("49.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&4Ausgabe &6Log",
					"&4Output &6Log"}));
			loggerSettingsKeys.put("49.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"SPRUCE_DOOR"}));
			loggerSettingsKeys.put("49.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§9Linksklick §ezum Aufrufen des Logs des","§eAktionlogs nach den jetzigen Parametern.",
					"§9Rechtsklick §ezum Aufrufen des Logs des","§eTrendlogs nach den jetzigen Parametern.",
					"§9Left-click §eto call the log of the","§eaction log according to the current parameters.",
					"§9Right-click §eto call the log of the","§etrend log according to the current parameters."}));
			
			loggerSettingsKeys.put("50.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&4Ausgabe &6Grafik",
					"&4Output &6Grafic"}));
			loggerSettingsKeys.put("50.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"DARK_OAK_DOOR"}));
			loggerSettingsKeys.put("50.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§9Linksklick §ezum Aufrufen der Grafik des","§eAktionlogs nach den jetzigen Parametern.",
					"§9Rechtsklick §ezum Aufrufen der Grafik des","§eTrendlogs nach den jetzigen Parametern.",
					"§9Left-click §eto call the grafic of the","§eaction log according to the current parameters.",
					"§9Right-click §eto call the grafic of the","§etrend log according to the current parameters."}));
		}
		PreSet:
		{
			loggerSettingsKeys.put("36.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&6Vor&beinstellung &f1",
					"&6Pre&bSet &f1"}));
			loggerSettingsKeys.put("36.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("36.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§aLinksklick §ezum Laden des Voreinstellung.",
					"§9Rechtsklick §ezum Speichern der Voreinstellung.",
					"§4Shiftklick §ezum Löschen des Voreinstellung.",
					"§aLeft-click §eto load the preset.",
					"§9Right-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			loggerSettingsKeys.put("44.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&6Vor&beinstellung &f2",
					"&6Pre&bSet &f2"}));
			loggerSettingsKeys.put("44.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("44.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§aLinksklick §ezum Laden des Voreinstellung.",
					"§9Rechtsklick §ezum Speichern der Voreinstellung.",
					"§4Shiftklick §ezum Löschen des Voreinstellung.",
					"§aLeft-click §eto load the preset.",
					"§9Right-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			loggerSettingsKeys.put("45.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&6Vor&beinstellung &f3",
					"&6Pre&bSet &f3"}));
			loggerSettingsKeys.put("45.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("45.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§aLinksklick §ezum Laden des Voreinstellung.",
					"§9Rechtsklick §ezum Speichern der Voreinstellung.",
					"§4Shiftklick §ezum Löschen des Voreinstellung.",
					"§aLeft-click §eto load the preset.",
					"§9Right-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			loggerSettingsKeys.put("53.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&6Vor&beinstellung &f4",
					"&6Pre&bSet &f4"}));
			loggerSettingsKeys.put("53.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("53.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§aLinksklick §ezum Laden des Voreinstellung.",
					"§9Rechtsklick §ezum Speichern der Voreinstellung.",
					"§4Shiftklick §ezum Löschen des Voreinstellung.",
					"§aLeft-click §eto load the preset.",
					"§9Right-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			
		}
		Min:
		{
			loggerSettingsKeys.put("0.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eMin &a+&c-&f1 | &a+&c-&f50",
					"&eMin &a+&c-&f1 | &a+&c-&f50"}));
			loggerSettingsKeys.put("0.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"CYAN_WOOL"}));
			loggerSettingsKeys.put("0.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAlsBetrag< (Aktion)§f und §b>Zwischen<",
					"§fsowie §b>KleinerAlsRevativerBetrag< (Trend)§f genutzt.",
					"§aLinksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1§e.",
					"§aRechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50§e.",
					"§cShift+Links §ezum Verringern des","§eMinimum-Parameters um §c1§e.",
					"§cShift+Rechts §ezum Verringern des","§eMinimum-Parameters um §c50§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the specifications", "§b>LessThanAmount< (Action)§f and §b>Between<",
					"§fand §b>LessThanRelativeAmount< (Trend)",
					"§aLeftclick §eto increase the","§eMinimum-Parameters by §a1§e.",
					"§aRightclick §eto increase the","§eMinimum-Parameters by §a50§e.",
					"§cShift+Left §eto decrease the","§eMinimum-Parameters by §c1§e.",
					"§cShift+Right §eto decrease the","§eMinimum-Parameters by §c50§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
			loggerSettingsKeys.put("9.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eMin &a+&c-&f1k | &a+&c-&f50k",
					"&eMin &a+&c-&f1k | &a+&c-&f50k"}));
			loggerSettingsKeys.put("9.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"CYAN_WOOL"}));
			loggerSettingsKeys.put("9.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAlsBetrag< (Aktion)§f und §b>Zwischen<",
					"§fsowie §b>KleinerAlsRevativerBetrag< (Trend)§f genutzt.",
					"§aLinksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1.000§e.",
					"§aRechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50.000§e.",
					"§cShift+Links §ezum Verringern des","§eMinimum-Parameters um §c1.000§e.",
					"§cShift+Rechts §ezum Verringern des","§eMinimum-Parameters um §c50.000§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the specifications", "§b>LessThanAmount< (Action)§f and §b>Between<",
					"§fand §b>LessThanRelativeAmount< (Trend)",
					"§aLeftclick §eto increase the","§eminimum-Parameters by §a1,000§e.",
					"§aRightclick §eto increase the","§eminimum-Parameters by §a150,000§e.",
					"§cShift+Left §eto decrease the","§eminimum-Parameters by §c1,000§e.",
					"§cShift+Right §eto decrease the","§eminimum-Parameters by §c50,000§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
			loggerSettingsKeys.put("18.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eMin &a+&c-&f1M | &a+&c-&f50M",
					"&eMin &a+&c-&f1M | &a+&c-&f50M"}));
			loggerSettingsKeys.put("18.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"CYAN_WOOL"}));
			loggerSettingsKeys.put("18.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAlsBetrag< (Aktion)§f und §b>Zwischen<",
					"§fsowie §b>KleinerAlsRevativerBetrag< (Trend)§f genutzt.",
					"§aLinksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1.000.000§e.",
					"§aRechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50.000.000§e.",
					"§cShift+Links §ezum Verringern des","§eMinimum-Parameters um §c1.000.000§e.",
					"§cShift+Rechts §ezum Verringern des","§eMinimum-Parameters um §c50.000.000§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the specifications", "§b>LessThanAmount< (Action)§f and §b>Between<",
					"§fand §b>LessThanRelativeAmount< (Trend)",
					"§aLeftclick §eto increase the","§eminimum-Parameters by §a1,000,000§e.",
					"§aRightclick §eto increase the","§eminimum-Parameters by §a50,000,000§e.",
					"§cShift+Left §eto decrease the","§eminimum-Parameters by §c1,000,000§e.",
					"§cShift+Right §eto decrease the","§eminimum-Parameters by §c50,000,000§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
		}
		
		Max:
		{
			loggerSettingsKeys.put("8.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eMax &a+&c-&f1 | &a+&c-&f50",
					"&eMax &a+&c-&f1 | &a+&c-&f50"}));
			loggerSettingsKeys.put("8.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"BLUE_WOOL"}));
			loggerSettingsKeys.put("8.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAlsBetrag< (Aktion)§f und §b>Zwischen<",
					"§fsowie §b>GrößerAlsRevativerBetrag< (Trend)§f genutzt.",
					"§aLinksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1§e.",
					"§aRechtsklick §ezum Erhöhen des","§eMaximum-Parameters um §a50§e.",
					"§cShift+Links §ezum Verringern des","§eMaximum-Parameters um §c1§e.",
					"§cShift+Rechts §ezum Verringern des","§eMaximum-Parameters um §c50§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the specifications", "§b>GreatherThanAmount<(Action)§f and §b>Between<",
					"§fand §b>GreatherThanRelativeAmount<(Trend)",
					"§aLeftclick §eto increase the","§emaximum-parameter by §a1§e.",
					"§aRightclick §eto increase the","§emaximum-parameter by §a50§e.",
					"§cShift+Left §eto decrease the","§emaximum-parameter by §c1§e.",
					"§cShift+Right §eto decrease the","§emaximum-parameter by §c50§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
			loggerSettingsKeys.put("17.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eMax &a+&c-&f1k | &a+&c-&f50k",
					"&eMax &a+&c-&f1k | &a+&c-&f50k"}));
			loggerSettingsKeys.put("17.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"BLUE_WOOL"}));
			loggerSettingsKeys.put("17.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAlsBetrag< (Aktion)§f und §b>Zwischen<",
					"§fsowie §b>GrößerAlsRelativerBetrag< (Trend)§f genutzt.",
					"§aLinksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1.000§e.",
					"§aRechtsklick §ezum Erhöhen des","§eMaximum-Parameters um §a50.000§e.",
					"§cShift+Links §ezum Verringern des","§eMaximum-Parameters um §c1.000§e.",
					"§cShift+Rechts §ezum Verringern des","§eMaximum-Parameters um §c50.000§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the specifications", "§b>GreatherThanAmount< (Action)§f and §b>Between<",
					"§fand §b>GreatherThanRelativeAmount< (Trend)",
					"§aLeftclick §eto increase the","§emaximum-parameter by §a1,000§e.",
					"§aRightclick §eto increase the","§emaximum-parameter by §a50,000§e.",
					"§cShift+Left §eto decrease the","§emaximum-parameter by §c1,000§e.",
					"§cShift+Right §eto decrease the","§emaximum-parameter by §c50,000§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
			loggerSettingsKeys.put("26.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eMax &a+&c-&f1M | &a+&c-&f50M",
					"&eMax &a+&c-&f1M | &a+&c-&f50M"}));
			loggerSettingsKeys.put("26.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"BLUE_WOOL"}));
			loggerSettingsKeys.put("26.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAlsBetrag< (Aktion)§f und §b>Zwischen<",
					"§fsowie §b>GrößerAlsRelativerBetrag< (Trend)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1.000.000§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMaximum-Parameters um §a50.000.000§e.",
					"§cShift+Links §ezum Verringern des","§eMaximum-Parameters um §c1.000.000§e.",
					"§cShift+Rechts §ezum Verringern des","§eMaximum-Parameters um §c50.000.000§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the specifications", "§b>GreatherThanAmount< (Action)§f and §b>Between<",
					"§fand §b>GreatherThanRelativeAmount< (Trend)",
					"§aLeftclick §eto increase the","§emaximum-parameter by §a1,000,000§e.",
					"§aRightclick §eto increase the","§emaximum-parameter by §a50,000,000§e.",
					"§cShift+Left §eto decrease the","§emaximum-parameter by §c1,000,000§e.",
					"§cShift+Right §eto decrease the","§emaximum-parameter by §c50,000,000§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
		}
		
		FirstStand:
		{
			loggerSettingsKeys.put("29.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eAnfangsstand &a+&c-&f1k | &a+&c-&f50k",
					"&eFirstStand &a+&c-&f1k | &a+&c-&f50k"}));
			loggerSettingsKeys.put("29.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"PURPLE_WOOL"}));
			loggerSettingsKeys.put("29.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Anfangsstand< (nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1.000§e.",
					"§aRechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50.000§e.",
					"§cShift+Links §ezum Verringern des","§eMinimum-Parameters um §c1.000§e.",
					"§cShift+Rechts §ezum Verringern des","§eMinimum-Parameters um §c50.000§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§aLeftclick §eto increase the","§eMinimum-Parameters by §a1,000§e.",
					"§aRightclick §eto increase the","§eMinimum-Parameters by §a50,000§e.",
					"§cShift+Left §eto decrease the","§eMinimum-Parameters by §c1,000§e.",
					"§cShift+Right §eto decrease the","§eMinimum-Parameters by §c50,000§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
			loggerSettingsKeys.put("38.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eAnfangsstand &a+&c-&f1M | &a+&c-&f50M",
					"&eFirstStand &a+&c-&f1M | &a+&c-&f50M"}));
			loggerSettingsKeys.put("38.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"PURPLE_WOOL"}));
			loggerSettingsKeys.put("38.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Anfangsstand< (nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1.000.000§e.",
					"§aRechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50.000.000§e.",
					"§cShift+Links §ezum Verringern des","§eMinimum-Parameters um §c1.000.000§e.",
					"§cShift+Rechts §ezum Verringern des","§eMinimum-Parameters um §c50.000.000§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§aLeftclick §eto increase the","§eminimum-Parameters by §a1,000,000§e.",
					"§aRightclick §eto increase the","§eminimum-Parameters by §a50,000,000§e.",
					"§cShift+Left §eto decrease the","§eminimum-Parameters by §c1,000,000§e.",
					"§cShift+Right §eto decrease the","§eminimum-Parameters by §c50,000,000§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
		}
		
		LastStand:
		{
			loggerSettingsKeys.put("33.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eLetzterstand &a+&c-&f1k | &a+&c-&f50k",
					"&eLastStand &a+&c-&f1k | &a+&c-&f50k"}));
			loggerSettingsKeys.put("33.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"MAGENTA_WOOL"}));
			loggerSettingsKeys.put("33.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Letzterstand< (nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1.000§e.",
					"§aRechtsklick §ezum Erhöhen des","§eMaximum-Parameters um §a50.000§e.",
					"§cShift+Links §ezum Verringern des","§eMaximum-Parameters um §c1.000§e.",
					"§cShift+Rechts §ezum Verringern des","§eMaximum-Parameters um §c50.000§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the information","§b>LastStand< (Only Trendlog)§f.",
					"§aLeftclick §eto increase the","§emaximum-Parameters by §a1,000§e.",
					"§aRightclick §eto increase the","§emaximum-Parameters by §a50,000§e.",
					"§cShift+Left §eto decrease the","§emaximum-Parameters by §c1,000§e.",
					"§cShift+Right §eto decrease the","§emaximum-Parameters by §c50,000§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
			loggerSettingsKeys.put("42.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&eLetzterstand &a+&c-&f1M | &a+&c-&f50M",
					"&eFirstStand &a+&c-&f1M | &a+&c-&f50M"}));
			loggerSettingsKeys.put("42.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"MAGENTA_WOOL"}));
			loggerSettingsKeys.put("42.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Letzterstand< (nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1.000.000§e.",
					"§aRechtsklick §ezum Erhöhen des","§eMaximum-Parameters um §a50.000.000§e.",
					"§cShift+Links §ezum Verringern des","§eMaximum-Parameters um §c1.000.000§e.",
					"§cShift+Rechts §ezum Verringern des","§eMaximum-Parameters um §c50.000.000§e.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the information","§b>LastStand< (Only Trendlog)§f.",
					"§aLeftclick §eto increase the","§emaximum-Parameters by §a1,000,000§e.",
					"§aRightclick §eto increase the","§emaximum-Parameters by §a50,000,000§e.",
					"§cShift+Left §eto decrease the","§emaximum-Parameters by §c1,000,000§e.",
					"§cShift+Right §eto decrease the","§emaximum-Parameters by §c50,000,000§e.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
		}
		
		TARGETEditor:
		{
			loggerSettingsKeys.put("3.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&cAccount-Parameter",
					"&cAccount-parameter"}));
			loggerSettingsKeys.put("3.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("3.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Account<§f genutzt.",
					"§aKlicke §ezur Eingabe eines Accountnummer","§eoder der Accounteigentümername und Accountname.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the","§einformation §b>Account<§f.",
					"§eClicks to enter a accountnumber","§eor accountownername and accountname.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
			
			loggerSettingsKeys.put("4.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&cAuftraggeber-Parameter",
					"&cOrderer-parameter"}));
			loggerSettingsKeys.put("4.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("4.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Auftraggeber<§f genutzt.",
					"§aKlicke §ezur Eingabe eines Spielernamens","§eoder einer Bankkontonummer.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the","§einformation §b>Orderer<§f.",
					"§eClicks to enter a player name","§eor bank account number.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
			
			loggerSettingsKeys.put("5.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&cKategorie-Parameter",
					"&cCategory-parameter"}));
			loggerSettingsKeys.put("5.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("5.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Kategorie< (nur Aktionlog)§f genutzt.",
					"§aKlicke §ezur Eingabe einer Kategorie",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the","§einformation §b>Category< (only Actionlog)§f.",
					"§eClicks to enter a catogry",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
			
			loggerSettingsKeys.put("13.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&cKommentar-Parameter",
					"&cComment-parameter"}));
			loggerSettingsKeys.put("13.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("13.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Kommentar< (nur Aktionlog)§f genutzt.",
					"§aKlicke §ezur Eingabe eines Kommentars.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the","§einformation §b>Comment< (only Actionlog)§f.",
					"§eClicks to enter a comment.",
					"§4Q §eto reset the parameter!",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
		}
		
		Booleans:
		{
			loggerSettingsKeys.put("11.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&cAuf/Absteigend&f-Parameter",
					"&cA/Descending&f-parameter"}));
			loggerSettingsKeys.put("11.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"REDSTONE_TORCH"}));
			loggerSettingsKeys.put("11.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§e§b>ID<§f oder §b>Betrag< §fgenutzt.",
					"§9Linksklick §ewählt §b>Aufsteigend<§e aus.",
					"§9Rechtsklick §ewählt §b>Absteigend<§e aus.",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the","§einformation §b>ID<§f or §b>Amount<.",
					"§9Left-click §eto §b>Ascending<§e set.",
					"§9Right-click §eto §b>Descending<§e set.",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
		}
		
		Enums:
		{
			loggerSettingsKeys.put("15.Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"&cID oder Betrags&f-Parameter",
					"&cID or Amount&f-parameter"}));
			loggerSettingsKeys.put("15.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"REDSTONE_TORCH"}));
			loggerSettingsKeys.put("15.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter stellt die zu nutzende Spalte ein.","§eEntweder §b>ID< §eoder §b>Betrag<§f.",
					"§9Linksklick §ewählt §b>ID<§e aus.",
					"§9Rechtsklick §ewählt §b>Betrag<§e aus.",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter sets the column to be used.","§eEither §b>Id< §eor §b>Amount<§f.",
					"§9Left-click §eto §b>ID<§e set.",
					"§9Right-click §eto §b>Amount<§e set.",
					"§4Ctrl+Q §eto reset §4all §eparameter!"}));
		}
	}
	
	public void initCurrencyFile()
	{
		LinkedHashMap<String, Language> currencyKeyI = new LinkedHashMap<>();
		currencyKeyI.put("UniqueName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar"}));
		currencyKeyI.put("DefaultCurrency"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyI.put("StandartUnitWorth"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1.0}));
		currencyKeyI.put("Currency.Exchangable"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyI.put("Currency.TaxationBeforeExchange"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyI.put("Gradation.CurrencyType"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"DIGITAL"})); //DIGITAL, ITEMSTACk, EXPERIENCE
		currencyKeyI.put("Gradation.DIGITAL.Base.Plural"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7Cents"}));
		currencyKeyI.put("Gradation.DIGITAL.Base.Singular"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7Cent"}));
		currencyKeyI.put("Gradation.DIGITAL.Base.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7¢"}));
		currencyKeyI.put("Gradation.DIGITAL.Base.ValueToBaseGradation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1}));
		currencyKeyI.put("Gradation.DIGITAL.1.Plural"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&2Dollars"}));
		currencyKeyI.put("Gradation.DIGITAL.1.Singular"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&2Dollar"}));
		currencyKeyI.put("Gradation.DIGITAL.1.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&2$"}));
		currencyKeyI.put("Gradation.DIGITAL.1.ValueToBaseGradation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				100}));
		currencyKeyI.put("WhenPlayerFirstJoin.CreateWallets"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyI.put("WhenPlayerFirstJoin.WalletsToCreate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				//The First Account are created as shortpay account
				//AccountType;DefaultAccount;StartMoney;ManagementType...
				AccountCategory.MAIN.toString()+";true;"+1000
				+";"+AccountManagementType.CAN_ADMINISTRATE_ACCOUNT.toString()
				+";"+AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString()
				+";"+AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT.toString()
				+";"+AccountManagementType.CAN_WITHDRAW.toString(),
				AccountCategory.SAVING.toString()+";true;"+0
				+";"+AccountManagementType.CAN_ADMINISTRATE_ACCOUNT.toString()
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString()
				+";"+AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT.toString()
				+";"+AccountManagementType.CAN_WITHDRAW.toString()}));
		currencyKeyI.put("WhenPlayerFirstJoin.CreateBanks"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyI.put("WhenPlayerFirstJoin.BanksToCreate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				AccountCategory.TAX.toString()+";true;"+0
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString(),
				AccountCategory.VOID.toString()+";true;"+0
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString()}));
		currencyKeyI.put("Commands.StructurType"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandStructurType.NESTED.toString()}));
		//CommandExecuteType;commands.yml Path
		currencyKeyI.put("Commands.SINGLE"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandExecuteType.BALANCE.toString()+";balance",
				CommandExecuteType.PAY.toString()+";pay",
				CommandExecuteType.TRANSFER.toString()+";transfer",
				CommandExecuteType.PAY_THROUGH_GUI.toString()+";paythroughgui",
				CommandExecuteType.GIVE.toString()+";give",
				CommandExecuteType.GIVE_CONSOLE.toString()+";giveconsole",
				CommandExecuteType.TAKE.toString()+";take",
				CommandExecuteType.TAKE_CONSOLE.toString()+";takeconsole",
				CommandExecuteType.SET.toString()+";set",
				CommandExecuteType.SET_CONSOLE.toString()+";setconsole",
				CommandExecuteType.TRANSFER.toString()+";transfer"}));
		currencyKeyI.put("Commands.NESTED"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandExecuteType.BALANCE.toString()+";money",
				CommandExecuteType.PAY.toString()+";money_pay",
				CommandExecuteType.TRANSFER.toString()+";money_transfer",
				CommandExecuteType.PAY_THROUGH_GUI.toString()+";money_paythroughgui",
				CommandExecuteType.GIVE.toString()+";money_give",
				CommandExecuteType.GIVE_CONSOLE.toString()+";money_giveconsole",
				CommandExecuteType.TAKE.toString()+";money_take",
				CommandExecuteType.TAKE_CONSOLE.toString()+";money_takeconsole",
				CommandExecuteType.SET.toString()+";money_set",
				CommandExecuteType.TRANSFER.toString()+";money_transfer"}));
		currencyKeyI.put("Taxation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				TaxationCase.TRANSACTION_BETWEEN_PLAYERS.toString()+";false;"+1.0,
				TaxationCase.CURRENCY_EXCHANGE.toString()+";false;"+5.0,
				TaxationCase.LOANLENDING+";false;"+1.0,
				TaxationCase.LOANREPAYING+";false;"+1.0,
				TaxationCase.STANDINGORDER+";false;"+1.0}));
		currencyKeyI.put("Format.OutputFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&r%number% %siprefix% %gradation% "}));
		currencyKeyI.put("Format.GradationQuantity"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1}));
		currencyKeyI.put("Format.UseSIPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyI.put("Format.DecimalPlaces"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2}));
		currencyKeyI.put("Format.UseSymbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyI.put("Format.ThousandSeperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				","}));
		currencyKeyI.put("Format.DecimalSeperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"."}));
		//SIPrefix Enum;Shortcut that you which
		currencyKeyI.put("Format.SIPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"YOTTA;&4Y",
				"ZETTA;&cZ",
				"EXA;&6E",
				"PETA; &eQuadrill.",
				"TERA; &9Trill.",
				"GIGA; &dBill.",
				"MEGA; &7Mio.",
				"KILO;&fk",}));
		currencyKeys.put("dollar", currencyKeyI);
		LinkedHashMap<String, Language> currencyKeyII = new LinkedHashMap<>();
		currencyKeyII.put("UniqueName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"taler"}));
		currencyKeyII.put("DefaultCurrency"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		currencyKeyII.put("StandartUnitWorth"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1.25}));
		currencyKeyII.put("Currency.Exchangable"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyII.put("Currency.TaxationBeforeExchange"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyII.put("Gradation.CurrencyType"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"DIGITAL"})); //DIGITAL, ITEMSTACk, EXPERIENCE
		currencyKeyII.put("Gradation.DIGITAL.Base.Plural"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#8E402AKupferstücke"}));
		currencyKeyII.put("Gradation.DIGITAL.Base.Singular"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#8E402AKupferstück"}));
		currencyKeyII.put("Gradation.DIGITAL.Base.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#8E402ACu"}));
		currencyKeyII.put("Gradation.DIGITAL.Base.ValueToBaseGradation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1}));
		currencyKeyII.put("Gradation.DIGITAL.1.Plural"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#C0C0C0Silbertaler"}));
		currencyKeyII.put("Gradation.DIGITAL.1.Singular"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#C0C0C0Silbertaler"}));
		currencyKeyII.put("Gradation.DIGITAL.1.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#C0C0C0Ag"}));
		currencyKeyII.put("Gradation.DIGITAL.1.ValueToBaseGradation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				100}));
		currencyKeyII.put("Gradation.DIGITAL.2.Plural"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#FFD700Goldmünzen"}));
		currencyKeyII.put("Gradation.DIGITAL.2.Singular"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#FFD700Goldmünze"}));
		currencyKeyII.put("Gradation.DIGITAL.2.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#FFD700Au"}));
		currencyKeyII.put("Gradation.DIGITAL.2.ValueToBaseGradation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				10000}));
		currencyKeyII.put("WhenPlayerFirstJoin.CreateWallets"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyII.put("WhenPlayerFirstJoin.WalletsToCreate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				//The First Account are created as shortpay account
				//AccountType;DefaultAccount;StartMoney;ManagementType...
				AccountCategory.MAIN.toString()+";true;"+11000
				+";"+AccountManagementType.CAN_ADMINISTRATE_ACCOUNT.toString()
				+";"+AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString()
				+";"+AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT.toString()
				+";"+AccountManagementType.CAN_WITHDRAW.toString(),
				AccountCategory.SAVING.toString()+";true;"+0
				+";"+AccountManagementType.CAN_ADMINISTRATE_ACCOUNT.toString()
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString()
				+";"+AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT.toString()
				+";"+AccountManagementType.CAN_WITHDRAW.toString()}));
		currencyKeyII.put("WhenPlayerFirstJoin.CreateBanks"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyII.put("WhenPlayerFirstJoin.BanksToCreate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				AccountCategory.TAX.toString()+";true;"+0
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString(),
				AccountCategory.VOID.toString()+";true;"+0
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString()}));
		currencyKeyII.put("Commands.StructurType"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandStructurType.NESTED.toString()}));
		//CommandExecuteType;commands.yml Path
		currencyKeyII.put("Commands.SINGLE"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandExecuteType.BALANCE.toString()+";balance",
				CommandExecuteType.PAY.toString()+";pay",
				CommandExecuteType.TRANSFER.toString()+";transfer",
				CommandExecuteType.PAY_THROUGH_GUI.toString()+";paythroughgui",
				CommandExecuteType.GIVE.toString()+";give",
				CommandExecuteType.GIVE_CONSOLE.toString()+";giveconsole",
				CommandExecuteType.TAKE.toString()+";take",
				CommandExecuteType.TAKE_CONSOLE.toString()+";takeconsole",
				CommandExecuteType.SET.toString()+";set",
				CommandExecuteType.SET_CONSOLE.toString()+";setconsole"}));
		currencyKeyII.put("Commands.NESTED"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandExecuteType.BALANCE.toString()+";money",
				CommandExecuteType.PAY.toString()+";money_pay",
				CommandExecuteType.TRANSFER.toString()+";money_transfer",
				CommandExecuteType.PAY_THROUGH_GUI.toString()+";money_paythroughgui",
				CommandExecuteType.GIVE.toString()+";money_give",
				CommandExecuteType.GIVE_CONSOLE.toString()+";money_giveconsole",
				CommandExecuteType.TAKE.toString()+";money_take",
				CommandExecuteType.TAKE_CONSOLE.toString()+";money_takeconsole",
				CommandExecuteType.SET.toString()+";money_set",
				CommandExecuteType.SET_CONSOLE.toString()+";money_setconsole"}));
		currencyKeyII.put("Taxation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				TaxationCase.TRANSACTION_BETWEEN_PLAYERS.toString()+";true;"+1.0,
				TaxationCase.CURRENCY_EXCHANGE.toString()+";true;"+5.0,
				TaxationCase.LOANLENDING+";true;"+1.0,
				TaxationCase.LOANREPAYING+";true;"+1.0,
				TaxationCase.STANDINGORDER+";true;"+1.0}));
		currencyKeyII.put("Format.OutputFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&r%number% %gradation% "}));
		currencyKeyII.put("Format.GradationQuantity"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2}));
		currencyKeyII.put("Format.UseSIPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		currencyKeyII.put("Format.DecimalPlaces"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2}));
		currencyKeyII.put("Format.UseSymbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		currencyKeyII.put("Format.ThousandSeperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				","}));
		currencyKeyII.put("Format.DecimalSeperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"."}));
		//SIPrefix Enum;Shortcut that you which
		currencyKeyII.put("Format.SIPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"YOTTA;Y",
				"ZETTA;Z",
				"EXA;E",
				"PETA;P",
				"TERA;T",
				"GIGA;G",
				"MEGA;M",
				"KILO;k",}));
		currencyKeys.put("taler", currencyKeyII);
	}
}
