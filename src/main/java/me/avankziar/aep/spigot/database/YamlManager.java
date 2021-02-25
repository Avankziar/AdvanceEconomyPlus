package main.java.me.avankziar.aep.spigot.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aep.spigot.database.Language.ISO639_2B;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> loggerSettingsKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfig();
		initCommands();
		initLanguage();
		initFilterSettings();
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
	
	@SuppressWarnings("unused")
	public void initConfig()
	{
		Base:
		{
			configKeys.put("Language"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"ENG"}));
			configKeys.put("Prefix"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"&7[&2AdvancedEconomyPlus&7] &r"}));
			configKeys.put("Bungee"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
		}
		Mysql:
		{
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
			configKeys.put("Mysql.TableNameI"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"economyPlayerData"}));
			configKeys.put("Mysql.TableNameII"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"economyBankData"}));
			configKeys.put("Mysql.TableNameIII"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"economyActionLogger"}));
			configKeys.put("Mysql.TableNameIV"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"economyTrendLogger"}));
			configKeys.put("Mysql.TableNameV"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"economyStandingOrder"}));
			configKeys.put("Mysql.TableNameVI"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"economyLoan"}));
			configKeys.put("Mysql.TableNameVII"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"economyLoggerSettingsPreset"}));
		}
		Generator:
		{
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
		}
		MechanicSettings:
		{
			configKeys.put("Use.DebuggingMode"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configKeys.put("Use.PlayerAccount"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configKeys.put("Use.Bank"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configKeys.put("Use.StandingOrder"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configKeys.put("Use.Loan"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
		}
		EconomySettings:
		{
			configKeys.put("Exceute.StandingOrderPayments"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configKeys.put("Exceute.LoanPayments"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configKeys.put("StartMoney"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					0.0}));
			configKeys.put("CurrencyNameSingular"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"Euro"}));
			configKeys.put("CurrencyNamePlural"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"Euros"}));
			configKeys.put("TrendLogger.ValueIsStabil"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					1000.0}));
			configKeys.put("GraficSpaceSymbol"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"ˉ"}));
			configKeys.put("GraficPointSymbol"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"x"}));
			configKeys.put("StandingOrderTimeSpamProtection"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"00-00:15"}));
			configKeys.put("StandingOrderValueSpamProtection"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					500.0}));
			
		}
		JobsRebornHook:
		{
			configKeys.put("JobsRebornHookTaskTimer"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"0", "15", "30", "45"}));
		}
		BankSettings:
		{
			configKeys.put("ReservedNames"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"YourSever", "YourServerMk2"}));
			configKeys.put("BankAccountFromat"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FOUR_DIGITS_TIMES_THREE"}));
		}
		RepeatingTimes:
		{
			configKeys.put("StandingOrderRepeatTime"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					180}));
			configKeys.put("LoanRepaymentRepeatTime"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					180}));
		}
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish, String explanation)
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
		commandsKeys.put(path+".Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				explanation}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish, String explanation)
	{
		commandsKeys.put(path+".Argument"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				argument}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission+"."+argument}));
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
		commandsKeys.put(path+".Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				explanation}));
	}
	
	@SuppressWarnings("unused") //TODO:Commands
	public void initCommands()
	{
		comBypass();
		Econ:
		{
			comEcon();
		}
		
		Money:
		{			
			comMoney();
			comMoneyLogs();
			comMoneyLoggerSettings();
		}
		
		Loan:
		{
			comLoan();
		}
		
		StandingOrder:
		{
			comStandingOrder();
		}
	}
	
	private void comBypass()
	{
		commandsKeys.put("Bypass.LoggerSettingsLogOther"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.money.loggersettingslogother"}));
		commandsKeys.put("Bypass.Recomment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.recomment"}));
		commandsKeys.put("Bypass.LogOther"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.bypass.logother"}));
		commandsKeys.put("Bypass.StandingOrderCreate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.standingorder.create"}));
		commandsKeys.put("Bypass.StandingOrderInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.standingorder.info"}));
		commandsKeys.put("Bypass.StandingOrderDelete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.standingorder.delete"}));
		commandsKeys.put("Bypass.StandingOrderPause"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.standingorder.pause"}));
		commandsKeys.put("Bypass.StandingOrderList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.standingorder.list"}));
		commandsKeys.put("Bypass.LoanCreate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.loan.create"}));
		commandsKeys.put("Bypass.LoanForgive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.loan.forgive"}));
		commandsKeys.put("Bypass.LoanInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.loan.info"}));
		commandsKeys.put("Bypass.LoanList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.loan.list"}));
		commandsKeys.put("Bypass.LoanPause"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.loan.pause"}));
		commandsKeys.put("Bypass.LoanTransfer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"eco.cmd.bypass.loan.transfer"}));
	}
	
	private void comEcon()
	{
		commandsInput("eco", "econ", "eco.cmd.eco", "/econ [pagenumber]", "/econ ",
				"&c/econ &f| Infoseite für alle Befehle.",
				"&c/econ &f| Info page for all commands.",
				"Base and Info Command");
		String basePermission = "eco.cmd.eco";
		argumentInput("eco_deletelog", "deletelog", basePermission,
				"/econ deletelog <id>", "/econ deletelog ",
				"&c/econ deletelog &f| Löscht den Log-Eintrag. AdminBefehl.",
				"&c/econ deletelog &f| Deletes the log entry. Admin command.",
				"Delete the log with the <id>");
		
		argumentInput("eco_player", "player", basePermission,
				"/econ player <player>", "/econ player ",
				"&c/econ player <Spielername> &f| Zeigt alle Infos zum Spieler an.",
				"&c/econ player <player name> &f| Shows all information about the player.",
				"Show the players balance uuid etc.");
		
		argumentInput("eco_recomment", "recomment", basePermission,
				"/econ recomment <id> <message>", "/econ recomment ",
				"&c/econ recomment <Id> <Note> &f| Ändert die angehängte Notiz des Log-Eintrages.",
				"&c/econ recomment <Id> <Note> &f| Changes the attached note of the log entry.",
				"Rewrite a log entry.");
	}
	
	private void comMoney()
	{
		commandsInput("money", "money", "eco.cmd.money", "/money", "/money",
				"&c/money &f| Zeigt dein Guthaben an.",
				"&c/money &f| Shows your balance.",
				"Display your balance");
		String basePermission = "eco.cmd.money";
		argumentInput("money_freeze", "freeze", basePermission,
				"/money freete <player>", "/money freeze ",
				"&c/money freeze <Spielername> &f| Friert das Spielerkonto ein oder gibt es frei.",
				"&c/money freeze <player name> &f| Freezes or releases the player account.",
				"Freeze the playeraccount");
		
		argumentInput("money_give", "give", basePermission,
				"/money give <player> <value> [note]", "/money give ",
				"&c/money give <Spielername> <Betrag> [Notiz] &f| Überweist den Betrag auf das Spielerkonto.",
				"&c/money give <player name> <amount>  [note] &f| Transfers the amount to the players balance.",
				"Give the player from the void money");
		
		argumentInput("money_giveconsole", "giveconsole", basePermission,
				"/money giveconsole <player> <value> <customFrom> <customOrderer> [note]", "/money giveconsole ",
				"&c/money giveconsole <Spielername> <Betrag> <CustomSender> <CustomAuftraggeber> [Notiz] &f| Überweist den Betrag auf das Spielerkonto.",
				"&c/money giveconsole <player name> <amount> customFrom> <customOrderer> [note] &f| Transfers the amount to the players balance.",
				"Give the player from the void money" + 
				" customFrom = The fake Player/Npc/whatever from the money may comes. (It comes always from the void btw.^^)" + 
				" customOrderer = The fake Player/Npc/whatever where the orderer of this is. Can you use by console use^^.");
		
		argumentInput("money_pay", "pay", basePermission,
				"/money pay <player> <value> [note]", "/money pay ",
				"&c/money pay <Spielername> <Betrag> [Notiz] &f| Zahlt dem Spieler den Betrag.",
				"&c/money pay <player name> [note] &f| Pays the player the amount",
				"Player to player paying.");
		
		argumentInput("money_set", "set", basePermission,
				"/money set <player> <value> [note]", "/money set ",
				"&c/money set <Spielername> <Betrag> [Notiz] &f| Setzt das Guthaben des Spielers auf den gewünschten Betrag.",
				"&c/money set <playername> <amount> [note] &f| Sets the players balance to the desired amount.",
				"Set the balance of the player.");
		
		argumentInput("money_setconsole", "setconsole", basePermission,
				"/money setconsole <player> <value> <customTo> <customOrderer> [note]", "/money setconsole ",
				"&c/money setconsole <Spielername> <Betrag> <customEmpfänger> <customAuftraggeber> [Notiz] &f| Setzt das Guthaben des Spielers auf den gewünschten Betrag.",
				"&c/money setconsole <playername> <amount> <customTo> <customOrderer> [note] &f| Sets the players balance to the desired amount.",
				"Set the balance of the player." + 
				" customTo = The fake Player/Npc/whatever where the money may go/comes." +
				" (It goes/comes always in the void btw.^^)" + 
				" customOrderer = The fake Player/Npc/whatever where the orderer of this is. Can you use by console use^^.");
		
		argumentInput("money_take", "take", basePermission,
				"/money take <player> <value> [note]", "/money take ",
				"&c/money take <Spielername> <Betrag> [Notiz] &f| Zieht den Betrag vom Spielerkonto ab.",
				"&c/money take <playername> <amount> [note] &f| Deduct the amount from the player balance.",
				"Take the money from a player in the void");
		
		argumentInput("money_takeconsole", "takeconsole", basePermission,
				"/money takeconsole <player> <value> <customTo> <customOrderer> [note]", "/money takeconsole ",
				"&c/money takeconsole <Spielername> <Betrag> <customEmpfänger> <customAuftraggeber> [Notiz] &f| Zieht den Betrag vom Spielerkonto ab.",
				"&c/money takeconsole <playername> <amount> <customTo> <customOrderer> [note] &f| Deduct the amount from the player balance.",
				"Take the money from a player in the void" + 
				" customTo = The fake Player/Npc/whatever where the money may go. (It goes always in the void btw.^^)" + 
				" customOrderer = The fake Player/Npc/whatever where the orderer of this is. Can you use by console use^^.");
		
		argumentInput("money_toggle", "toggle", basePermission,
				"/money toggle", "/money toggle ",
				"&c/money toggle &f| Schaltet Nachrichten aus und ein, die als Überweisung auf euer Spielerkonto eingehen.",
				"&c/money toggle &f| Enables and disables messages that are sent to your player account as a bank transfer.",
				"Toggle to see money payment messages");
		
		argumentInput("money_top", "top", basePermission,
				"/money top [pagenumber]", "/money top ",
				"&c/money top [Seitenzahl] &f| Zeigt die Liste der bestbetuchten Spieler an.",
				"&c/money top [page number] &f| Shows the list of the best players.",
				"Show the top balance players.");
	}
	
	private void comMoneyLogs()
	{
		String basePermission = "eco.cmd.money";
		argumentInput("money_actionlog", "actionlog", basePermission,
				"/money actionlog [page] [playername]", "/money actionlog ",
				"&c/money actionlog [Seitenzahl] [Spielername] &f| Zeigt direkt den Aktionlog bei den aktuellen Einstellungen.",
				"&c/money actionlog [page] [playername] &f| Shows direct the actionlog by the actual Settings.",
				"Shows direct the actionlog by the actual Settings.");
		argumentInput("money_trendlog", "trendlog", basePermission,
				"/money trendlog [page] [playername]", "/money trendlog ",
				"&c/money trendlog [Seitenzahl] [Spielername] &f| Zeigt direkt den Trendlog bei den aktuellen Einstellungen.",
				"&c/money trendlog [page] [playername] &f| Shows direct the trendlog by the actual Settings.",
				"Shows direct the trendlog by the actual Settings.");
	}
	
	private void comMoneyLoggerSettings()
	{
		String basePermission = "eco.cmd.money";
		argumentInput("money_loggersettings", "loggersettings", basePermission,
				"/money loggersetting", "/money loggersetting ",
				"&c/money loggersettings &f| Öffnet die Gui und mit angegebenen Argumenten gibt es die Daten in Form von Log, Diagram etc. aus.",
				"&c/money loggersettings &f| Opens the gui and with given arguments it outputs the data in form of log, diagram etc.",
				"Open the Gui for the loggersettings for Action- and trendlog");
		
		basePermission = "eco.cmd.money.loggersettings";
		argumentInput("money_loggersettings_gui", "gui", basePermission,
				"/money loggersettings gui [playername] [page] [methode]", "/money loggersettings gui ",
				"&c/money loggersettings gui [Spielername] [Seitenzahl] [Methode] &f| Öffnet die Gui und mit angegebenen Argumenten gibt es die Daten in Form von Log, Diagram etc. aus.",
				"&c/money loggersettings gui [playername] [page] [methode] &f| Opens the gui and with given arguments it outputs the data in form of log, diagram etc.",
				"Open the Gui for the loggersettings for Action- and trendlog. And display choosen settings as log, diagram etc.");
		
		argumentInput("money_loggersettings_other", "other", basePermission,
				"/money loggersetting other [playername]", "/money loggersettings other ",
				"&c/money loggersettings other [Spielername] &f| Öffnet die Gui eines anderen Spielers.",
				"&c/money loggersettings other [playername] &f| Opens the gui of another player.",
				"Open the Gui for the loggersettings of a other player for Action- and trendlog");
		
		argumentInput("money_loggersettings_text", "text", basePermission,
				"/money loggersettings text <Text...>", "/money loggersettings text ",
				"&c/money loggersettings text <Text...> &f| Texteditor für bestimmte Parameter der Gui.",
				"&c/money loggersettings text <Text...> &f| Text editor for certain parameters of the Gui.",
				"Set a Searchtext for the comment, from, to or orderer value. (Always gui first!)");
	}
	
	private void comLoan() //TODO:ComLoan
	{		
		commandsInput("loan", "loan", "eco.cmd.loan", "/loan", "/loan ",
				"&c/loan [Seitenzahl] [Spielername] &f| Zeigt deine Kredite an.",
				"&c/loan [page] [playername] &f| Shows yours loans.",
				"Display all of yours loans.");
		String basePermission = "eco.cmd.loan";
		argumentInput("loan_accept", "accept", basePermission,
				"/loan accept [confirm]", "/loan accept",
				"&c/loan accept [bestätigen] &f| Akzeptiert einen Kreditvorschlag.",
				"&c/loan accept [confirm] &f| Accept a loanproposal.",
				"Accept the loanproposal from the other player.");
		
		argumentInput("loan_amount", "amount", basePermission,
				"/loan amount <totalamount> <amountratio> <interest>", "/loan amount ",
				"&c/loan amount <gesamtbetrag> <ratenbetrag> <zinzen in %> &f| Setzt für den Gesamtbetrag, Ratenbetrag und die Zinsen für einen Kredit, welcher sich noch in Bearbeitung befindet.",
				"&c/loan amount  <totalamount> <amountratio> <interest in %> &f| Sets the total amount, installment amount and interest for a loan that is still being processed.",
				"Set to a loan in workprogress the totalamount, amountratio and interest.");
		
		argumentInput("loan_cancel", "cancel", basePermission,
				"/loan cancel", "/loan cancel ",
				"&c/loan cancel &f| Bricht die Krediterstellung ab.",
				"&c/loan cancel &f| Cancels the credit creation.",
				"Cancel the loan work in progress.");
		
		argumentInput("loan_create", "create", basePermission,
				"/loan create <name> <sender> <reciver>", "/loan create ",
				"&c/loan create <Name> <Sender> <Empfänger> &f| Erstellt einen Kreditvorschlag.",
				"&c/loan create <name> <sender> <reciver> &f| Create a loanproposal.",
				"Create a work in progress loanproposal");
		
		argumentInput("loan_info", "info", basePermission,
				"/loan info [id]", "/loan info ",
				"&c/loan info [id] &f| Zeigt alle Infos zu allen Krediten an. Ohne Id, wird der Kreditvorschlag angezeigt.",
				"&c/loan info [id] &f| Shows all information about all loans. Without Id, the loan proposal is displayed.",
				"Show all info to a loan. By no id, than is the work in progress loan");
		
		argumentInput("loan_inherit", "inherit", basePermission,
				"/loan inherit <id> <playername>", "/loan inherit ",
				"&c/loan inherit <id> <Spielername> &f| Lässt den Spieler den Kredit erben. Somit muss er nun zahlen. Adminbefehl um bei Betrugsfall mit einem 2. Account, diesen dann zu belasten.",
				"&c/loan inherit <id> <playername> &f| Lets the player inherit the loan. So now he must pay. Admin command to debit a 2nd account in case of fraud with a 2nd account.",
				"Let the player inherit the loans to pay. Admincommand to inherit loans to player whitch cheats with a second acc.");
		
		argumentInput("loan_list", "list", basePermission,
				"/loan list [page]", "/loan list ",
				"&c/loan list [Seitenzahl] &f| Zeigt seitenbasiert alle Kredite als Liste.",
				"&c/loan list [page] &f| Shows all loans in a page-based list.",
				"Show a list of all loans of all the player");
		
		argumentInput("loan_pause", "pause", basePermission,
				"/loan pause <id>", "/loan pause ",
				"&c/loan pause <id> &f| Pausiert oder nimmt die Zahlungen des Kredits wieder auf. Nur für den Krediteigentümer möglich!",
				"&c/loan pause <id> &f| Pauses or resumes payments on the loan. Only possible for the loan owner!",
				"Pause or unpause a loanrepayment");
		
		argumentInput("loan_payback", "payback", basePermission,
				"/loan payback <id>", "/loan payback",
				"&c/loan payback <id> &f| Zahlt dem Spieler den Rest des Kredites zurück als Admin.",
				"&c/loan payback <id> &f| Pay the player back the rest of the loan as admin.",
				"As admin payback the rest amount to the player.");
		
		argumentInput("loan_reject", "reject", basePermission,
				"/loan reject", "/loan reject",
				"&c/loan reject &f| Lehnt einen Kreditvorschlag ab.",
				"&c/loan reject &f| Rejects a loan proposal.",
				"Reject a loan proposal");
		
		argumentInput("loan_remit", "remit", basePermission,
				"/loan forgive <id> [confirm]", "/loan remit",
				"&c/loan remit <id> &f| Der Restbetrag des Kredits wird erlassen.",
				"&c/loan remit <id> &f| The remaining amount of the loan will be remitted.",
				"Remit the restamount of the loan.");
		
		argumentInput("loan_repay", "repay", basePermission,
				"/loan repay <id> <amount>", "/loan repay",
				"&c/loan repay <id> <Betrag> &f| Zahlt einen Betrag vom Kredit ab.",
				"&c/loan repay <id> <amount> &f| Pays an amount off the loan.",
				"Repay the amount to a loan.");
		
		argumentInput("loan_send", "send", basePermission,
				"/loan send <player>", "/loan send ",
				"&c/loan send <spielername> &f| Sendet einen Kreditvorschlag einem Spieler.",
				"&c/loan send <player> &f| Sends a loan proposal to a player.",
				"Send the player a loan proposal");
		
		argumentInput("loan_time", "time", basePermission,
				"/loan time <starttime> <endtime> <repeatingtime>", "/loan time ",
				"&c/loan time <startdatum|dd.MM.yyyy-HH:mm> <entdatum|dd.MM.yyyy-HH:mm> <ratenzyklus|dd-HH:mm> &f| Setzt die Zeiten für den Kreditvorschlag.",
				"&c/loan time <starttime|dd.MM.yyyy-HH:mm> <endtime|dd.MM.yyyy-HH:mm> <repeatingtime|dd-HH:mm> &f| Sets the times for the loan proposal.",
				"Set the times. Starttime and Endtime in <dd.MM.yyyy-HH:mm> and RepeatingTime in <dd-HH:mm> format");
		
		argumentInput("loan_transfer", "", basePermission,
				"/loan transfer <id> <player>", "/loan transfer ",
				"&c/loan transfer <id> <Spielername> &f| Überträgt den Eigentümerstatus und Rückzahlrecht an den Spieler.",
				"&c/loan transfer <id> <player> &f| Transfers the ownership status and repayment right to the player.",
				"Transfer the ownerstatus of your loan to the other player.");
	}
	
	private void comStandingOrder() //TODO:ComStandingOrder
	{
		commandsInput("standingorder", "standingorder", "eco.cmd.standingorder", "/standingorder", "/standingorder ",
				"&c/standingorder [Seitenzahl] [Spielername] &f| Zeigt deine Daueraufträge an.",
				"&c/standingorder [page] [playername] &f| Shows yours standing orders.",
				"Display all of yours standing orders.");
		String basePermission = "eco.cmd.standingorder";
		argumentInput("standingorder_amount", "amount", basePermission,
				"/standingorder amount <amount>", "/standingorder amount",
				"&c/standingorder amount <Betrag> &f| Setzt den Betrag für ein noch wartenden Dauerauftrag.",
				"&c/standingorder amount <Betrag> &f| Sets the amount for a still waiting standing order.",
				"Set to a waiting standingorder the amount");
		
		argumentInput("standingorder_cancel", "cancel", basePermission,
				"/standingorder cancel", "/standingorder cancel",
				"&c/standingorder cancel &f| Bricht den noch wartenden Dauerauftrag ab.",
				"&c/standingorder cancel &f| Cancels the still waiting standing order.",
				"Cancel the waiting standing order.");
		
		argumentInput("standingorder_create", "create", basePermission,
				"/standingorder create <name> <sender> <reciver>", "/standingorder create",
				"&c/standingorder create <name> <Sender> <Empfänger> &f| Erstellt einen wartenden Dauerauftrag. Durch weitere Einstellung wird dieser finalisiert.",
				"&c/standingorder create <name> <sender> <reciver> &f| Creates a waiting standing order. This is finalized by further settings.",
				"Create a Standingorder. Additional Settings must be set.");
		
		argumentInput("standingorder_delete", "delete", basePermission,
				"/standingorder delete <id>", "/standingorder delete",
				"&c/standingorder delete <id> &f| Löscht den schon existierenden Dauerauftrag.",
				"&c/standingorder delete <id> &f| Deletes the already existing standing order.",
				"Delete the standing order.");
		
		argumentInput("standingorder_info", "info", basePermission,
				"/standingorder info [id]", "/standingorder info",
				"&c/standingorder info [id] &f| Zeigt alle Info zu einem Dauerauftrag an. Bei keiner Angabe, zeigt es den noch wartenden Dauerauftrag.",
				"&c/standingorder info [id] &f| Shows all information about a standing order. If not specified, it shows the still waiting standing order.",
				"Show Infos about standingorder.");
		
		argumentInput("standingorder_list", "list", basePermission,
				"/standingorder list [page]", "/standingorder list ",
				"&c/standingorder list [Seitenzahl] &f| Listet alle Daueraufträge von allen Spielern auf.",
				"&c/standingorder list [Seitenzahl] &f| Lists all standing orders from all players.",
				"Show a list of all standingorders from all players.");
		
		argumentInput("standingorder_pause", "pause", basePermission,
				"/standingorder pause <id>", "/standingorder pause ",
				"&c/standingorder pause <ID> &f| Pausiert den Dauerauftrag. Falls er vorher abgebrochen wurde, setzte er den Status zurück.",
				"&c/standingorder pause <ID> &f| Pauses the standing order. If it was canceled before, it resets the status.",
				"Paused a standingorder or cancelled the cancelstatus.");
		
		argumentInput("standingorder_repeatingtime", "repeatingtime", basePermission,
				"/standingorder repeatingtime <dd-HH:mm value>", "/standingorder repeatingtime",
				"&c/standingorder repeatingtime <dd-HH:mm Wert> &f| Setzt eine Wiederholungsvariable, welche im dd-HH:mm Format geschrieben werden muss.",
				"&c/standingorder repeatingtime <dd-HH:mm Wert> &f| Sets a repeat variable, which must be written in dd-HH:mm format.",
				"Set the repeating time of the waiting standing order. Must use the specific >dd-HH:mm< Pattern. For example 01-23:50 for 1 day, 23 hours and 50 seconds.");
		
		argumentInput("standingorder_starttime", "starttime", basePermission,
				"/standingorder starttime <dd.MM.yyyy-HH:mm value>", "/standingorder starttime",
				"&c/standingorder starttime <dd.MM.yyyy-HH:mm Wert> &f| Setzt das Startdatum. Es müssen vorher alle anderen Eigenschaften gesetzt sein, denn dieser Befehl startet auch gleichzeitig den Dauerauftrag!",
				"&c/standingorder starttime <dd.MM.yyyy-HH:mm Wert> &f| Sets the start date. All other properties must be set first, because this command also starts the standing order at the same time!",
				"Set the starttime in <dd.MM.yyyy-HH:mm> format. And Starts the standing order!");
	}
	
	public void initLanguage() //TODO:Languages
	{
		languageKeys.put("NoPermission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast dafür keine Rechte!",
				"&cYou have no rights!"}));
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
		
		languageKeys.put("StandingOrder.Orderer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Dauerauftrag",
				"StandingOrder"}));
		languageKeys.put("StandingOrder.Comment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDauerauftrag &f%name%~!~&dInsgesamt gezahlter Betrag: &f%totalpaid% %currency%",
				"&eStandingOrder &f%name%~!~&dTotal amount paid: &f%totalpaid% %currency%"}));
		
		languageKeys.put("LoanRepayment.Orderer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Kreditrückzahlung",
				"DebtRepayment"}));
		languageKeys.put("LoanRepayment.Comment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKredit &f%name%~!~&dInsgesamt gezahlter Betrag: &f%totalpaid% %currency%~!~&5Ausstehender Betrag: &f%waitingamount% %currency%",
				"&eLoan &f%name%~!~&dTotal amount paid: &f%totalpaid% %currency%~!~&5Outstanding amount: &f%waitingamount% %currency%"}));
		
		languageKeys.put("ChestShopHook.Sell"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eShop: &f%amount% &6x &b%item% &ean &f%player% &averkauft&e!",
				"&eShop: &f%amount% &6x &b%item% &eto &f%player% &asold&e!"}));
		languageKeys.put("ChestShopHook.Buy"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eShop: &f%amount% &6x &b%item% &evon &f%player% &cgekauft&e!",
				"&eShop: &f%amount% &6x &b%item% &efrom &f%player% &cpurchased&e!"}));
		
		languageKeys.put("JobsRebornHook.UUID"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Jobs",
				"Jobs"}));
		languageKeys.put("JobsRebornHook.Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Jobs",
				"Jobs"}));
		languageKeys.put("JobsRebornHook.Orderer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Jobs",
				"Jobs"}));
		languageKeys.put("JobsRebornHook.Comment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDein Gehalt vom Job &d%job%.",
				"&eYour salary from the job &d%job%."}));
		
		languageKeys.put("HeadDatabase.UUID"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"HeadDB",
				"HeadDB"}));
		languageKeys.put("HeadDatabase.Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"HeadDB",
				"HeadDB"}));
		languageKeys.put("HeadDatabase.Orderer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"HeadDB",
				"HeadDB"}));
		languageKeys.put("HeadDatabase.Comment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"%head% &egekauft!",
				"%head% &epurchased!"}));
		
		languageKeys.put("QickShopHook.Sell"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eShop: &f%amount% &6x &b%item% &ean &f%player% &averkauft&e!",
				"&eShop: &f%amount% &6x &b%item% &eto &f%player% &asold&e!"}));
		languageKeys.put("QickShopHook.Buy"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eShop: &f%amount% &6x &b%item% &evon &f%player% &cgekauft&e!",
				"&eShop: &f%amount% &6x &b%item% &efrom &f%player% &cpurchased&e!"}));
		
		langEco();
		langMoney();
		langLoan();
		langStandingOrder();
		
		/*languageKeys.put(""
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"",
				""}));*/
	}
	
	private void langEco() //TODO:LangEco
	{
		String base = "CmdEco.";
		languageKeys.put(base+"BaseInfo.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bInfo&7]&e=====",
				"&e=====&7[&2Economy &bInfo&7]&e====="}));
		languageKeys.put(base+"BaseInfo.Next"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e&nnächste Seite &e==>",
				"&e&nnext page &e==>"}));
		languageKeys.put(base+"BaseInfo.Past"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e<== &nvorherige Seite",
				"&e<== &nprevious page"}));
		
		languageKeys.put(base+"DeleteLog.LogNotExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Log-Eintrag existiert nicht!",
				"&cThe log entry does not exist!"}));
		languageKeys.put(base+"DeleteLog.LogWasDeleted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDer Log-Eintrag Nummer %id% wurde &cgelöscht&e!",
				"&eThe log entry number %id% was &cdeleted&e!"}));
		
		languageKeys.put(base+"Player.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &b%player%&7]&e=====",
				"&e=====&7[&2Economy &b%player%&7]&e====="}));
		languageKeys.put(base+"Player.UUID"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eSpielerUUID: &f%uuid%",
				"&ePlayerUUID: &f%uuid%"}));
		languageKeys.put(base+"Player.Balance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eGuthaben: &6%balance% &2%currency%",
				"&eBalance: &6%balance% &2%currency%"}));
		languageKeys.put(base+"Player.BankAccount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eZugehörige Bankkonten: &f%bankaccount%",
				"&eRelated bank accounts: &f%bankaccount%"}));
		
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
				"&eDie Notiz vom Log &f#%id% &ewurde geändert in: &r%comment%",
				"&eThe note from log &f#%id% &was changed to: &r%comment%"}));
	}
	
	private void langMoney() //TODO:LangMoney
	{
		String base = "CmdMoney.";
		languageKeys.put(base+"PlayerBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[%time%] &eDein Guthaben: &6%balance% &2%currency%",
				"&7[%time%] &eYour balance: &6%balance% &2%currency%"}));
		languageKeys.put(base+"PlayerDeposit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&f%amount% &e%currency% wurden auf %name% überwiesen. Aktuelles Guthaben: &2%balance%",
				"&f%amount% &e%currency% were transferred to %name%. Current balance: &2%balance%"}));
		languageKeys.put(base+"PlayerWithDraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&f%amount% &e%currency% wurden vom %name% abgezogen. Aktuelles Guthaben: &2%balance%",
				"&f%amount% &e%currency% were deducted from the %name%. Current balance: &2%balance%"}));
		languageKeys.put(base+"BankPay.DepositWithDraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6%amount% &2%currency% &ewurden von &6%name1% &ean &6%number2%&f|&6%name2% &eüberwiesen. &eAktuelles Guthaben von &6%name%&e: &2%balance%",
				"&6%amount% &2%currency% &ewere transferred from &6%name1% &eto &6%number2%&f|&6%name2% &e. &eCurrent balance of &6%name%&e: &2%balance%"}));
		
		base = "CmdMoney.BarChart.";
		languageKeys.put(base+"Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=======&7[&2Economy &bBarChart: &f%player%&7 | Log-Anzahl: %amount%&7]&e=======",
				"&e=======&7[&2Economy &bBarChart: &f%player%&7 | Log-Quantity: %amount%&7]&e======="}));
		languageKeys.put(base+"Infoline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aEinnahmen &7[in %-Anteil vom Jahr] &e| &cAusgaben &7[in %-Anteil vom Jahr]",
				"&aIncome &7[in % share of year] &e| &cExpenditure &7[in % share of year]"}));
		languageKeys.put(base+"HoverMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aEinnahmen in Monat = &f%positivvalue% %currency%~!~&aProzentualer Anteil der Einnahmen = &f%percentP% %~!~&cAusgaben im Monat = &f%negativvalue% %currency%~!~&cProzentualer Anteil der Ausgaben = &f%percentN% %~!~&eGesamtveränderung im Monat = &r%totalvalue% %currency%",
				"&aIncome in month = &f%positivevalue% %currency%~!~&aPercentage share of income = &f%percentP% %~!~&cExpenditure in month = &f%negativevalue% %currency%~!~&cPercentage share of expenditure = &f%percentN% %~!~&eTotal change in month = &r%totalvalue% %currency%"}));
		languageKeys.put(base+"HoverMessageII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aEinnahmen in letzten Jahr = &f%positivvalue% %currency%~!~&cAusgaben im letzten Jahr = &f%negativvalue% %currency%~!~&eGesamtveränderung im letzten Jahr = &r%totalvalue% %currency%",
				"&aIncome in last year = &f%positivevalue% %currency%~!~&cExpenditure in last year = &f%negativevalue% %currency%~!~&eTotal change in last year = &r%totalvalue% %currency%"}));
		languageKeys.put(base+"Month"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e -    %month%    - &r",
				"&e -    %month%    - &r"}));
		languageKeys.put(base+"LastYear"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e - Letztes Jahr - &r",
				"&e -  Last Year  - &r"}));
		
		base = "CmdMoney.";
		languageKeys.put(base+"Give.DepositWithDraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6%amount% &2%currency% &ewurden vom &6%name1% &ean &6%name2% &eüberwiesen.",
				"&6%amount% &2%currency% &ewere transferred from &6%name1% &eto &6%name2%&e."}));
		languageKeys.put(base+"Give.DepositWithDrawBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eAktuelles Guthaben von &6%name%&e: &6%balance% &2%currency%",
				"&eCurrent balance of &6%name%&e: &6%balance% &2%currency%"}));
		
		base = "CmdMoney.Log.";
		languageKeys.put(base+"Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=======&7[&2Economy &bLog: &f%name%&7 | Log-Anzahl: %amount%&7]&e=======",
				"&e=======&7[&2Economy &bLog: &f%name%&7 | Log-Quantity: %amount%&7]&e======="}));
		languageKeys.put(base+"LoggerBToPPositivAll"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&7%fromnumber%&f|&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete",
				"&7[&e%date%&7]+&7%fromnumber%&f|&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete"}));
		languageKeys.put(base+"LoggerBToPPositivDelete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&7%fromnumber%&f|&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete",
				"&7[&e%date%&7]+&7%fromnumber%&f|&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete"}));
		languageKeys.put(base+"LoggerBToPPositivRe"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&7%fromnumber%&f|&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment",
				"&7[&e%date%&7]+&7%fromnumber%&f|&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment"}));
		languageKeys.put(base+"LoggerBToPPositiv"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&7%fromnumber%&f|&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote",
				"&7[&e%date%&7]+&7%fromnumber%&f|&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote"}));
		languageKeys.put(base+"LoggerPToBNegativAll"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%tonumber%&f|&7%toname%&b:+&c%amount% &6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete",
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%tonumber%&f|&7%toname%&b:+&c%amount% &6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete"}));
		languageKeys.put(base+"LoggerPToBNegativDelete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%tonumber%&f|&7%toname%&b:+&c%amount% &6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete",
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%tonumber%&f|&7%toname%&b:+&c%amount% &6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete"}));
		languageKeys.put(base+"LoggerPToBNegativRe"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%tonumber%&f|&7%toname%&b:+&c%amount% &6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment",
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%tonumber%&f|&7%toname%&b:+&c%amount% &6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment"}));
		languageKeys.put(base+"LoggerPToBNegativ"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%tonumber%&f|&7%toname%&b:+&c%amount% &6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote",
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%tonumber%&f|&7%toname%&b:+&c%amount% &6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote"}));
		languageKeys.put(base+"LoggerPToPNegativAll"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%toname%&b:+&c%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete",
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%toname%&b:+&c%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete"}));
		languageKeys.put(base+"LoggerPToPNegativDelete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%toname%&b:+&c%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete",
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%toname%&b:+&c%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete"}));
		languageKeys.put(base+"LoggerPToPNegativRe"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%toname%&b:+&c%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment",
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%toname%&b:+&c%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment"}));
		languageKeys.put(base+"LoggerPToPNegativ"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%toname%&b:+&c%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote",
				"&7[&e%date%&7]+&c%fromname%+&6>>+&7%toname%&b:+&c%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote"}));
		languageKeys.put(base+"LoggerPToPPositivAll"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete",
				"&7[&e%date%&7]+&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete"}));
		languageKeys.put(base+"LoggerPToPPositivDelete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete",
				"&7[&e%date%&7]+&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &c✖~click@RUN_COMMAND@/econ+deletelog+%id%~hover@SHOW_TEXT@CmdMoney.Log.HoverDelete"}));
		languageKeys.put(base+"LoggerPToPPositivRe"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment",
				"&7[&e%date%&7]+&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote | &2✏~click@SUGGEST_COMMAND@/econ+recomment+%id%+~hover@SHOW_TEXT@CmdMoney.Log.HoverRecomment"}));
		languageKeys.put(base+"LoggerPToPPositiv"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7]+&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote",
				"&7[&e%date%&7]+&7%fromname%+&6>>+&a%toname%&b:+&a%amount%+&6%currency%~hover@SHOW_TEXT@CmdMoney.Log.LoggerOrdererNote"}));
		languageKeys.put(base+"LoggerOrdererNote"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bAuftraggeber: &f%orderer%~!~&bNotiz: &r%comment%",
				"&bOrderer: &f%orderer%~!~&bComment: &r%comment%"}));
		languageKeys.put(base+"HoverDelete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKlicke hier um dem den Log-Eintrag zu löschen!",
				"&eClick here to delete the log entry!"}));
		languageKeys.put(base+"HoverRecomment"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKlicke hier um die Notiz des Log-Eintrages zu ändern.",
				"&eClick here to change the note of the log entry."}));
		languageKeys.put(base+"Next"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e&nnächste Seite &e==>",
				"&e&nnext page &e==>"}));
		languageKeys.put(base+"Past"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e<== &nvorherige Seite",
				"&e<== &nprevious page"}));
		languageKeys.put(base+"NoLoggerSettingsFound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast keinen LoggerSetting ausgewählt!",
				"&cYou have not selected a LoggerSetting!"}));
		languageKeys.put(base+"NoOtherLoggerSettingsFound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDer Spieler hat keinen LoggerSetting ausgewählt!",
				"&cThe player has not selected a LoggerSetting!"}));
		languageKeys.put(base+"LoggerSettingsTextSuggest"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eKlicke hier auf die Nachricht, um einen Befehlsvorschlag zu erhalten. Tippe dann dein Suchtext ein und drücke &b>Enter<",
				"&eClick here on the message to get a command suggestion. Then type in your search text and press &b>Enter<"}));
		languageKeys.put(base+"LoggerSettingsTextOnlyThroughGUI"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBitte den Befehl nur nutzten, wenn man durch die GUI aufgefordert wird!",
				"&cPlease use this command only when prompted by the GUI!"}));
		languageKeys.put(base+"LoggerSettingsJSONOutput"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6Klicke hier um die JSON-Ausgabe zu kopieren.",
				"&eClick here to copy the JSON output"}));
		languageKeys.put(base+"LoggerSettingsJSONWebsiteText"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eBitte gebe nun die Ausgabe in einen Json-CSV Konverter ein oder klicke auf &f>hier<&e. Danach downloade einfach die Datei und öffne sie in Excel.",
				"&eNow please enter the output into a Json-CSV converter or click &f>here<&e. After that just download the file and open it in excel."}));
		languageKeys.put(base+"LoggerSettingsJSONWebsite"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"http://convertcsv.com/json-to-csv.htm",
				"http://convertcsv.com/json-to-csv.htm"}));
		
		base = "CmdMoney.Freeze.";
		languageKeys.put(base+"YouFrozenSomeone"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast das Spielerkonto von &f%player% &eeingefroren! &cAchtung! Externe Plugins können bei schlechter Abfrage zu Fehlern führen!",
				"&eYou have frozen the player account of %player%! &cAttention! External plugins can lead to errors if the query is bad!"}));
		languageKeys.put(base+"YourAccountWasFrozen"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDein Spielerkonto wurden von &f%player% &ceingefroren! Du kannst nun keine Geld-Transaktion ausführen!",
				"&cYour player account has been frozen by %player%! You can now not perform any money related activities!"}));
		languageKeys.put(base+"YouDefrozenSomeone"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDu hast das Spielerkonto von &f%player% &ewieder freigegeben! Der Spieler kann nun wieder Geld-Transaktion ausführen!",
				"&eYou have released the player account of %player% again! The player can now carry out money-related activities again!"}));
		languageKeys.put(base+"YourAccountWasDefrozen"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aDer Spieler &f%player% &ahat dein Spielerkonto wieder freigegeben! Dir ist es nun wieder erlaubt Geld-Transaktion durchführen!",
				"'&aThe player &f%player% &has released your account again! You are now allowed to pursue activities concerning money again!"}));
		languageKeys.put(base+"TheAccountIsFrozen"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDas Spielerkonto ist eingefroren!",
				"&cThe player account is frozen! You can not transfer money to it!"}));
		languageKeys.put(base+"YourAccountIsFrozen"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDein Spielerkonto ist eingefroren!",
				"&cYour account is frozen! You can nott transfer money to anyone!"}));
		
		base = "CmdMoney.Pay.";
		languageKeys.put(base+"NotEnoughBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&f%amount% &e%currency% &cübersteigt dein Guthaben!",
				"&f%amount% &e%currency% &cexceeds your balance!"}));
		languageKeys.put(base+"SelfPay"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7Du steckst das Geld von der einen Tasche in die andere!",
				"&7You put the money from one pocket in the other!"}));
		languageKeys.put(base+"DepositWithDraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6%amount% &2%currency% &ewurden von &6%name1% &ean &6%name2% &eüberwiesen.",
				"&6%amount% &2%currency% &ewere transferred from &6%name1% &eto &6%name2%&e."}));
		languageKeys.put(base+"DepositWithDrawBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eAktuelles Guthaben von &6%name%&e: &6%balance% &2%currency%",
				"&eCurrent balance of &6%name%&e: &6%balance% &2%currency%"}));
		
		languageKeys.put(base+"Set.BalanceIsSet"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eGuthaben von &f%name% &eauf %amount% &2%currency% &egesetzt.",
				"&eBalance from &f%name% &to %amount% &2%currency% &set."}));
		
		
		
		base = "CmdMoney.";
		languageKeys.put(base+"Take.DepositWithDraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&6%amount% &2%currency% &ewurde von &6%name1% &ean &6%name2% &eüberwiesen.",
				"&6%amount% &2%currency% &ewas transferred from &6%name1% &eto &6%name2%&e."}));
		languageKeys.put(base+"Take.DepositWithDrawBalance"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eAktuelles Guthaben von &6%name%&e: &6%balance% &2%currency%",
				"&eCurrent balance of &6%name%&e: &6%balance% &2%currency%"}));
		languageKeys.put(base+"Take.NoFullAmount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAchtung, der Spieler hatte nicht die erforderlichen &f%amount% &2%currency%! Die Transaktion wurde nicht durchgeführt!",
				"&cAttention, the player did not have the required &f%amount% &2%currency%! The transaction was not completed!"}));
		
		languageKeys.put(base+"Toggle.SetOn"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eNachrichten die das Spielerkonto betreffen, werden nun &aangezeigt&e!",
				"&eMessages concerning the player account are now &adisplayed&e!"}));
		languageKeys.put(base+"Toggle.SetOff"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eNachrichten die das Spielerkonto betreffen, werden nun &causgeblendet&e!",
				"&eMessages concerning the player account are now &chidden&e!"}));
		
		languageKeys.put(base+"Top.NotEnoughValues"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEs gibt nicht genügend Werte für diese Seitenzahl!",
				"&cThere are not enough values for this page number!"}));
		languageKeys.put(base+"Top.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=======&7[&2Economy &bTop &eSeite %page%&7]&e=======",
				"&e=======&7[&2Economy &bTop &epage %page%&7]&e======="}));
		languageKeys.put(base+"Top.TopLine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e%place%. &b%player%: &6%balance% &2%currency%",
				"&e%place%. &b%player%: &6%balance% &2%currency%"}));
		
		languageKeys.put(base+"TrendDiagram.NotEnoughValues"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEs gibt nicht genügend Werte für diese Seitenzahl!",
				"&cThere are not enough values for this page number!"}));
		languageKeys.put(base+"TrendDiagram.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bTrenddiagramm: &f%player%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2Economy &bTrenddiagram: &f%name%&7 | Log-Quantity: %amount%&7]&e====="}));
		languageKeys.put(base+"TrendDiagram.HeadlineII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bAktiondiagramm: &f%player%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2Economy &bActiondiagram: &f%player%&7 | Log-Quantity: %amount%&7]&e====="}));
		languageKeys.put(base+"TrendDiagram.Infoline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cMin %min% &f)|( &aMax %max% &f>> &6Mittelwert/line: &e%median%",
				"&cMin %min% &f)|( &aMax %max% &f>> &6average value/line: &e%median%"}));
		languageKeys.put(base+"TrendDiagram.Positiv"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Relative Änderung: &a%relativ% &f| &a%percent% %",
				"Relative change: &a%relativ% &f| &a%percent% %"}));
		languageKeys.put(base+"TrendDiagram.Negativ"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Relative Änderung: &c%relativ% &f| &c%percent% %",
				"Relative change: &c%relativ% &f| &c%percent% %"}));
		
		languageKeys.put(base+"TrendGrafic.NotEnoughValues"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEs gibt nicht genügend Werte für diese Seitenzahl!",
				""}));
		languageKeys.put(base+"TrendGrafic.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bTrendgrafik: &f%player%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2Economy &bTrendgrafic: &f%player%&7 | Log-Quantity: %amount%&7]&e====="}));
		languageKeys.put(base+"TrendGrafic.HeadlineII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bAktiongrafik: &f%player%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2Economy &bActiongrafic: &f%player%&7 | Log-Quantity: %amount%&7]&e====="}));
		
		languageKeys.put(base+"TrendLog.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e=====&7[&2Economy &bTrendLog: &f%player%&7 | Log-Anzahl: %amount%&7]&e=====",
				"&e=====&7[&2Economy &bTrendLog: &f%player%&7 | Log-Quantity: %amount%&7]&e====="}));
		languageKeys.put(base+"TrendLog.ChangeNegativ"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7] &a%first% &fbis &c%last%",
				"&7[&e%date%&7] &a%first% &fto &c%last%"}));
		languageKeys.put(base+"TrendLog.ChangeNeutral"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7] &b%first% &fbis &b%last%",
				"&7[&e%date%&7] &b%first% &fto &b%last%"}));
		languageKeys.put(base+"TrendLog.ChangePositiv"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&7[&e%date%&7] &c%first% &fbis &a%last%",
				"&7[&e%date%&7] &c%first% &fto &a%last%"}));
		languageKeys.put(base+"TrendLog.Positiv"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Relative Änderung: &a%relativ%",
				"Relative change: &a%relativ%"}));
		languageKeys.put(base+"TrendLog.Negativ"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"Relative Änderung: &c%relativ%",
				"Relative change: &c%relativ%"}));
	}
	
	private void langLoan() //TODO:LangLoan
	{
		String base = "CmdLoan.";
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
				"&eID: &f%id%~!~&eName: &f%name%~!~&eKreditnehmer: &f%from%~!~&eKreditgeber: &f%to%~!~&eKrediteigentümer: &f%owner%~!~&eStartdatum: &f%st%~!~&eEnddatum: &f%et%~!~&eRatenzyklus: &f%rt%~!~&eBetrag: &r%apsf%&r/&3%ta%~!~&eRatenbetrag: &f%ar%~!~&eZinssatz: &f%in% %~!~&eIst Pausiert: &f%pa%~!~&eIst Vergeben: &e%fo%~!~&eIst Abbezahlt: &f%fi%",
				"&eID: &f%id%~!~&eName: &f%name%~!~&eBorrower: &f%from%~!~&eLenders: &f%to%~!~&eLoanOwner: &f%owner%~!~&eStartdate: &f%st%~!~&eEnddate: &f%et%~!~&eRatecycle: &f%rt%~!~&eAmount: &r%apsf%&r/&3%ta%~!~&eInstalmentamount: &f%ar%~!~&eInterestrate: &f%in% %~!~&eIs Paused: &f%pa%~!~&eIs forgiven: &e%fo%~!~&eIs paid off: &f%fi%"}));
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
				"&eGesamtbetrag: &r%ta% %currency% (Zinsen schon mit eingerechnet)~!~&eRaten: &r%am% %currency%~!~&eZinsen: &r%in% %~!~Vorraussichtlich Anzahl an Zahlungen: &r%min%",
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
				"&eDer verbleibende Kreditbetrag von &r%dif% %currency%&e, welcher Spieler &f%from% %to% &eschuldet wurde von Spieler &f%player% &evergeben.",
				"&eThe remaining credit amount of &r%dif% %currency%&e, which player &f%from% %to% &eowed by player &f%player% &was &credited by player"}));
		languageKeys.put(base+"Info.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&e===&7[&bKreditinfo &3%id%&f-&6%name%&7]&e===",
				"&e===&7[&bLoaninfo &3%id%&f-&6%name%&7]&e==="}));
		languageKeys.put(base+"Info.Participants"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&c%form% &ehat geliehen von &a%to% &1| &2Eigentümer: &f%Loanowner%",
				"&c%form% &has borrowed from &a%to% &1| &2Owner: &f%Loanowner%"}));
		languageKeys.put(base+"Info.Amounts"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBeträge: &a%amountpaidsofar%&f/&4%totalamount% &r%currency%",
				"&cAmounts: &a%amountpaidsofar%&f/&4%totalamount% &r%currency%"}));
		languageKeys.put(base+"Info.Interest"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cZinssatz: &f%interest% % &1| &cZinsen: &r%interestamount% %currency%",
				"&cInterest rate: &f%interest% % &1| &cInterest: &r%interestamount% %currency%"}));
		languageKeys.put(base+"Info.Ratio"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cZeitzyklus: &r%repeatingtime% &1| &cRatebetrag: &r%amountratio%",
				"&cTime cycle: &r%repeatingtime% &1| &cRate amount: &r%amountratio%"}));
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
	
	private void langStandingOrder() //TODO:LangStandingOrder
	{
		String base = "CmdStandingOrder.";
		languageKeys.put(base+"NoStandingOrders"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cDu hast keine Daueraufträge!",
				"&cYou have no standing orders!"}));
		languageKeys.put(base+"BankNotImplemented"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBanken sind nicht implementiert! Wähle 2 Spieler aus.",
				"&cBanks are not implemented! Select 2 players."}));
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
				"&eBetrag des noch wartenden Dauerauftrages &f%name% &ezu &f%amount% &r%currency% &egeändert.",
				"&eAmount of the pending standing order &f%name% &eto &f%amount% &r%currency% &echanged."}));
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
				"&e===&7[&bDauerauftrag &f%id% &c✖~click@RUN_COMMAND@%cancelcmd%~hover@SHOW_TEXT@&eBricht+den+noch+wartenden+Dauerauftrag+ab! &4✖~click@SUGGEST_COMMAND@%deletecmd%+%id%~hover@SHOW_TEXT@&eLöscht+den+Dauerauftrag! &7]&e===",
				"&e===&7[&bStanding Order &f%id% &c✖~click@RUN_COMMAND@%cancelcmd%~hover@SHOW_TEXT@&eCancels+the+still+waiting+standing+order! &4✖~click@SUGGEST_COMMAND@%deletecmd%+%id%~hover@SHOW_TEXT@&eDelete+the+standing+order! &7]&e==="}));
		languageKeys.put(base+"Info.Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cName: &f%name%",
				"&cName: &f%name%"}));
		languageKeys.put(base+"Info.From"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAbsender: &f%from%",
				"&cSender: &f%from%"}));
		languageKeys.put(base+"Info.To"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cEmpfänger: &f%to%",
				"&cReciver: &f%to%"}));
		languageKeys.put(base+"Info.Amount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBetrag: &f%amount% &2✏~click@SUGGEST_COMMAND@%amountcmd%~hover@SHOW_TEXT@&eNur+noch+wartende+Daueraufträge+können+verändert+werden!",
				"&cAmount: &f%amount% &2✏~click@SUGGEST_COMMAND@%amountcmd%~hover@SHOW_TEXT@&eOnly+waiting+standing+orders+can+be+changed!"}));
		languageKeys.put(base+"Info.AmountPaidSoFar"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cBisher gezahlter Betrag: &f%amountpaidsofar%",
				"&cAmount paid so far: &f%amountpaidsofar%"}));
		languageKeys.put(base+"Info.StartTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cStartdatum: &f%starttime% &2✏~click@SUGGEST_COMMAND@%starttimecmd%~hover@SHOW_TEXT@&eNur+noch+wartende+Daueraufträge+können+verändert+werden!",
				"&cStartdate: &f%starttime% &2✏~click@SUGGEST_COMMAND@%starttimecmd%~hover@SHOW_TEXT@&eOnly+waiting+standing+orders+can+be+changed!"}));
		languageKeys.put(base+"Info.RepeatingTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cWiederholungszyklus: &f%repeatingtime% &2✏~click@SUGGEST_COMMAND@%repeatingtimecmd%~hover@SHOW_TEXT@&eNur+noch+wartende+Daueraufträge+können+verändert+werden!",
				"&cRepeatingtime: &f%repeatingtime% &2✏~click@SUGGEST_COMMAND@%repeatingtimecmd%~hover@SHOW_TEXT@&eOnly+waiting+standing+orders+can+be+changed!"}));
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
				"&cBitte nutze die richtige Zeit Syntax &f<dd-HH:mm>&c!",
				"&cPlease use the right time syntax &f<dd-HH:mm>&c!"}));
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
				"&cBitte nutze die richtige Zeit Syntax &f<dd.MM.yyyy-HH:mm:ss>&c!",
				"&cPlease use the right time syntax &f<dd.MM.yyyy-HH:mm:ss>&c!"}));
		languageKeys.put(base+"StartTime.SpamProtection"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&cAchtung! Spamschutz Warnung! Du unterschreitest die minimale Wiederholungszeit &f(%repeatingtime%) &csowie den Minimalbetrag &f(%amount%)&c! Bitte ändere eine der beiden Variabeln!",
				"&cAttention! Spam protection warning! You fall below the minimum repetition time &f(%repeatingtime%) &cand the minimum amount&f(%amount%)&c! Please change one of the two variables!"}));
	}
	
	@SuppressWarnings("unused") //TODO:FilterSettings
	public void initFilterSettings()
	{
		ActualParameter:
		{
			loggerSettingsKeys.put("ActualParameter"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"      §7-------------------------",
					"§eAktuelle Werte:",
					"§bSpieler: §f%uuid%",
					"§bBanknummber: §f%number%",
					"§bOrderSpalte: §f%ordercolumn%",
					"§bVon: §f%from% §f| §bZu: §f%to%",
					"§bAuftraggeber: §f%orderer%",
					"§bKommentar: §f%comment%",
					"§bMin > §f%min% | §bMax < §f%max%",
					"§bErstStand > §f%firststand%",
					"§bLetzterStand < §f%laststand%",
					"§bistAbsteigend: §f%descending%",
					
					"      §7-------------------------",
					"§eActual Values:",
					"§bPlayer: §f%uuid%",
					"§bBanknumber: §f%number%",
					"§bOrderColumn: §f%ordercolumn%",
					"§bFrom: §f%from% §f| §bTo: §f%to%",
					"§bOrderer: §f%orderer%",
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
					"&cAbsender-Parameter",
					"&cSender-parameter"}));
			loggerSettingsKeys.put("3.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("3.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Absender<§f genutzt.",
					"§aKlicke §ezur Eingabe eines Spielernamens","§eoder einer Bankkontonummer.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the","§einformation §b>Sender<§f.",
					"§eClicks to enter a player name","§eor bank account number.",
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
					"&cEmpfänger-Parameter",
					"&cReciver-parameter"}));
			loggerSettingsKeys.put("5.Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("5.Lore"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					"§fParameter wird für die Angaben","§b>Empfänger<§f genutzt.",
					"§aKlicke §ezur Eingabe eines Spielernamens","§eoder einer Bankkontonummer.",
					"§4Q §ezum Zurücksetzen des Wertes!",
					"§4Strg+Q §ezum Zurücksetzen §4aller §eWerte!",
					"§fParameter is used for the","§einformation §b>Reciver<§f.",
					"§eClicks to enter a player name","§eor bank account number.",
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
}
