package main.java.me.avankziar.aep.spigot.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aep.spigot.database.LanguageObject.LanguageType;

public class YamlManager
{
	private LanguageType languageType = LanguageType.GERMAN;
	private LanguageType defaultLanguageType = LanguageType.GERMAN;
	private static LinkedHashMap<String, LanguageObject> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LanguageObject> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LanguageObject> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LanguageObject> loggerSettingsKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfig();
		initCommands();
		initLanguage();
		initFilterSettings();
	}
	
	public LanguageType getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(LanguageType languageType)
	{
		this.languageType = languageType;
	}
	
	public LanguageType getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, LanguageObject> getConfigKey()
	{
		return configKeys;
	}
	
	public LinkedHashMap<String, LanguageObject> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, LanguageObject> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, LanguageObject> getLoggerSettingsKey()
	{
		return loggerSettingsKeys;
	}
	
	public void setFileInput(YamlConfiguration yml, LinkedHashMap<String, LanguageObject> keyMap, String key, LanguageType languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap
				.get(key)
				.languageValues
				.get(languageType)
				.length == 1)
		{
			yml.set(key,
				keyMap
				.get(key)
				.languageValues
				.get(languageType)[0]);
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
						stringList.add((String) o);
					} else
					{
						stringList.add(o.toString());
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
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"English"}));
			configKeys.put("Prefix"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"&7[&2AdvancedEconomyPlus&7] &r"}));
			configKeys.put("Bungee"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
		}
		Mysql:
		{
			configKeys.put("Mysql.Status"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("Mysql.Host"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"127.0.0.1"}));
			configKeys.put("Mysql.Port"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					3306}));
			configKeys.put("Mysql.DatabaseName"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"mydatabase"}));
			configKeys.put("Mysql.SSLEnabled"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("Mysql.AutoReconnect"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					true}));
			configKeys.put("Mysql.VerifyServerCertificate"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("Mysql.User"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"admin"}));
			configKeys.put("Mysql.Password"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"not_0123456789"}));
			configKeys.put("Mysql.TableNameI"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyPlayerData"}));
			configKeys.put("Mysql.TableNameII"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyBankData"}));
			configKeys.put("Mysql.TableNameIII"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyActionLogger"}));
			configKeys.put("Mysql.TableNameIV"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyTrendLogger"}));
			configKeys.put("Mysql.TableNameV"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyStandingOrder"}));
			configKeys.put("Mysql.TableNameVI"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyLoan"}));
			configKeys.put("Mysql.TableNameVII"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyLoggerSettingsPreset"}));
		}
		Generator:
		{
			configKeys.put("Identifier.Click"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"click"}));
			configKeys.put("Identifier.Hover"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"hover"}));
			configKeys.put("Seperator.BetweenFunction"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"~"}));
			configKeys.put("Seperator.WhithinFuction"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"@"}));
			configKeys.put("Seperator.Space"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"+"}));
			configKeys.put("Seperator.HoverNewLine"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"~!~"}));
		}
		MechanicSettings:
		{
			configKeys.put("Use.PlayerAccount"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					true}));
			configKeys.put("Use.Bank"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("Use.StandingOrder"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("Use.Loan"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
		}
		EconomySettings:
		{
			configKeys.put("StartMoney"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					0.0}));
			configKeys.put("CurrencyNameSingular"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"Euro"}));
			configKeys.put("CurrencyNamePlural"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"Euros"}));
			configKeys.put("TrendLogger.ValueIsStabil"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					1000.0}));
			configKeys.put("GraficSpaceSymbol"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"ˉ"}));
			configKeys.put("GraficPointSymbol"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"x"}));
		}
		JobsRebornHook:
		{
			configKeys.put("JobsRebornHookTaskTimer"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"0", "15", "30", "45"}));
		}
		BankSettings:
		{
			configKeys.put("ReservedNames"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"YourSever", "YourServerMk2"}));
			configKeys.put("BankAccountFromat"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"FOUR_DIGITS_TIMES_THREE"}));
		}
		RepeatingTimes:
		{
			configKeys.put("StandingOrderRepeatTime"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					180}));
			configKeys.put("LoanRepaymentRepeatTime"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					180}));
		}
	}
	
	@SuppressWarnings("unused") //TODO:Commands
	public void initCommands()
	{
		String path = "";
		Econ:
		{
			commandsInput("eco", "econ", "eco.cmd.eco", "/econ [pagenumber]", "/econ ",
					"&c/econ &f| Infoseite für alle Befehle.",
					"&c/econ &f| Info page for all commands.",
					"Base and Info Command");
			comEcon();
		}
		Money:
		{
			commandsInput("money", "money", "eco.cmd.money", "/money", "/money",
					"&c/money &f| Zeigt dein Guthaben an.",
					"&c/money &f| Shows your balance.",
					"Display your balance");			
			comMoney();
			comMoneyLoggerSettings();
			comMoneyLoan();
			comMoneyStandingOrder();
		}
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish, String explanation)
	{
		commandsKeys.put(path+".Name"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				name}));
		commandsKeys.put(path+".Permission"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				basePermission}));
		commandsKeys.put(path+".Suggestion"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".Explanation"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				explanation}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish, String explanation)
	{
		commandsKeys.put(path+".Argument"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				argument}));
		commandsKeys.put(path+".Permission"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				basePermission+"."+argument}));
		commandsKeys.put(path+".Suggestion"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".Explanation"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				explanation}));
	}
	
	private void comEcon()
	{
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
		String basePermission = "eco.cmd.money";
		argumentInput("money_freeze", "freeze", basePermission,
				"/money freete <player>", "/money freeze ",
				"&c/money freeze <Spielername> &f| Friert das Spielerkonto ein oder gibt es frei.",
				"&c/money freeze <player name> &f| Freezes or releases the player account.",
				"Freeze the playeraccount");
		
		argumentInput("money_give", "give", basePermission,
				"/money give <player> <value> <customTo> <customOrderer> [note]", "/money give ",
				"&c/money give <Spielername> <Betrag> &f| Überweist den Betrag auf das Spielerkonto.",
				"&c/money give <player name> <amount> &f| Transfers the amount to the players balance.",
				"Give the player from the void money" + 
				" customTo = The fake Player/Npc/whatever from the money may comes. (It comes always from the void btw.^^)" + 
				" customOrderer = The fake Player/Npc/whatever where the orderer of this is. Can you use by console use^^.");
		
		argumentInput("money_pay", "pay", basePermission,
				"/money pay <player> <value> [note]", "/money pay ",
				"&c/money pay <Spielername> <Betrag> &f| Zahlt dem Spieler den Betrag.",
				"&c/money pay <player name> &f| Pays the player the amount",
				"Player to player paying.");
		
		argumentInput("money_set", "set", basePermission,
				"/money set <player> <value> <customTo> <customOrderer> [note]", "/money set ",
				"&c/money set <Spielername> <Betrag> &f| Setzt das Guthaben des Spielers auf den gewünschten Betrag.",
				"&c/money set <playername> <amount> &f| Sets the players balance to the desired amount.",
				"Set the balance of the player." + 
				" customTo = The fake Player/Npc/whatever where the money may go/comes." +
				" (It goes/comes always in the void btw.^^)" + 
				" customOrderer = The fake Player/Npc/whatever where the orderer of this is. Can you use by console use^^.");
		
		argumentInput("money_take", "take", basePermission,
				"/money take <player> <value> <customTo> <customOrderer> [note]", "/money take ",
				"&c/money take <Spielername> <Betrag> &f| Zieht den Betrag vom Spielerkonto ab.",
				"&c/money take <playername> <amount> &f| Deduct the amount from the player balance.",
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
	
	private void comMoneyLoggerSettings()
	{
		String basePermission = "eco.cmd.money";
		argumentInput("money_loggersettings", "loggersettings", basePermission,
				"/money loggersetting", "/money loggersetting ",
				"&c/money loggersettings &f| ",
				"&c/money loggersettings &f| ",
				"Open the Gui for the loggersettings for Action- and trendlog");
		
		basePermission = "eco.cmd.money.loggersettings";
		argumentInput("money_loggersettings_gui", "gui", basePermission,
				"/money loggersetting gui [playername] [page] [methode]", "/money loggersetting gui ",
				"&c/money loggersetting gui [Spielername] [Seitenzahl] [Methode] &f| Öffnet die Gui und mit angegebenen Argumenten gibt es die Daten in Form von Log, Diagram etc. aus.",
				"&c/money loggersetting gui [playername] [page] [methode] &f| Opens the gui and with given arguments it outputs the data in form of log, diagram etc.",
				"Open the Gui for the loggersettings for Action- and trendlog. And display choosen settings as log, diagram etc.");
		
		argumentInput("money_loggersettings_other", "other", basePermission,
				"/money loggersetting other [playername]", "/money loggersetting other ",
				"&c/money loggersetting other [Spielername] &f| Öffnet die Gui eines anderen Spielers.",
				"&c/money loggersetting other [playername] &f| Opens the gui of another player.",
				"Open the Gui for the loggersettings of a other player for Action- and trendlog");
		
		argumentInput("money_loggersettings_text", "text", basePermission,
				"/money loggersetting text <Text...>", "/money loggersetting text ",
				"&c/money loggersetting text <Text...> &f| Texteditor für bestimmte Parameter der Gui.",
				"&c/money loggersetting text <Text...> &f| Text editor for certain parameters of the Gui.",
				"Set a Searchtext for the comment, from, to or orderer value. (Always gui first!)");
	}
	
	private void comMoneyLoan()
	{		
		String basePermission = "eco.cmd.money";
		argumentInput("money_loan", "loan", basePermission,
				"/money loan", "/money loan ",
				"&c/money loan &f| Zwischenbefehl",
				"&c/money loan &f| Default command",
				"Default Command");
		
		argumentInput("money_loans", "loans", basePermission,
				"/money loans [page] [player]", "/money loans ",
				"&c/money Loans [Seitenzahl] [Spielername] &f| Zeigt alle Kredite des Spielers an.",
				"&c/money loans [pagenumber] [playername] &f| Shows all loans of the player.",
				"Show loans of the player");
		
		basePermission = "eco.cmd.money.loan";
		argumentInput("money_loan_accept", "accept", basePermission,
				"/money loan accept [confirm]", "/money loan accept ",
				"&c/money loan accept [bestätigen] &f| Akzeptiert einen Kreditvorschlag.",
				"&c/money loan accept [confirm] &f| Accept a loanproposal.",
				"Accept the loanproposal from the other player.");
		
		argumentInput("money_loan_amount", "amount", basePermission,
				"/money loan amount <totalamount> <amountratio> <interest>", "/money loan amount ",
				"&c/money loan amount <gesamtbetrag> <ratenbetrag> <zinzen in %> &f| Setzt für den Gesamtbetrag, Ratenbetrag und die Zinsen für einen Kredit, welcher sich noch in Bearbeitung befindet.",
				"&c/money loan amount  <totalamount> <amountratio> <interest in %> &f| Sets the total amount, installment amount and interest for a loan that is still being processed.",
				"Set to a loan in workprogress the totalamount, amountratio and interest.");
		
		argumentInput("money_loan_cancel", "cancel", basePermission,
				"/money loan cancel", "/money loan cancel ",
				"&c/money loan cancel &f| Bricht die Krediterstellung ab.",
				"&c/money loan cancel &f| Cancels the credit creation.",
				"Cancel the loan work in progress.");
		
		argumentInput("money_loan_create", "create", basePermission,
				"/money loan create <name> <from> <to>", "/money loan create ",
				"&c/money loan create <Name> <Von> <Zu> &f| Erstellt einen Kreditvorschlag.",
				"&c/money loan create <name> <from> <to> &f| Create a loanproposal.",
				"Create a work in progress loanproposal");
		
		argumentInput("money_loan_forgive", "forgive", basePermission,
				"/money loan forgive <id> [confirm]", "/money loan forgive ",
				"&c/money loan forgive <id> &f| Der Restbetrag des Kredits wird vergeben.",
				"&c/money loan forgive <id> &f| The remaining amount of the loan will be forgiven.",
				"Forgive the restamount of the loan.");
		
		argumentInput("money_loan_info", "info", basePermission,
				"/money loan info [id]", "/money loan info ",
				"&c/money loan info [id] &f| Zeigt alle Infos zu allen Krediten an. Ohne Id, wird der Kreditvorschlag angezeigt.",
				"&c/money loan info [id] &f| Shows all information about all loans. Without Id, the loan proposal is displayed.",
				"Show all info to a loan. By no id, than is the work in progress loan");
		
		argumentInput("money_loan_inherit", "inherit", basePermission,
				"/money loan inherit <id> <playername>", "/money loan inherit ",
				"&c/money loan inherit <id> <Spielername> &f| Lässt den Spieler den Kredit erben. Somit muss er nun zahlen. Adminbefehl um bei Betrugsfall mit einem 2. Account, diesen dann zu belasten.",
				"&c/money loan inherit <id> <playername> &f| Lets the player inherit the loan. So now he must pay. Admin command to debit a 2nd account in case of fraud with a 2nd account.",
				"Let the player inherit the loans to pay. Admincommand to inherit loans to player whitch cheats with a second acc.");
		
		argumentInput("money_loan_list", "list", basePermission,
				"/money loan list [page]", "/money loan list ",
				"&c/money loan list [Seitenzahl] &f| Zeigt seitenbasiert alle Kredite als Liste.",
				"&c/money loan list [page] &f| Shows all loans in a page-based list.",
				"Show a list of all loans of all the player");
		
		argumentInput("money_loan_pause", "pause", basePermission,
				"/money loan pause <id>", "/money loan pause ",
				"&c/money loan pause <id> &f| Pausiert oder nimmt die Zahlungen des Kredits wieder auf. Nur für den Krediteigentümer möglich!",
				"&c/money loan pause <id> &f| Pauses or resumes payments on the loan. Only possible for the loan owner!",
				"Pause or unpause a loanrepayment");
		
		
		argumentInput("money_loan_payback", "payback", basePermission,
				"/money loan payback <id>", "/money loan payback ",
				"&c/money loan payback <id> &f| Zahlt dem Spieler den Rest des Kredites zurück als Admin.",
				"&c/money loan payback <id> &f| Pay the player back the rest of the loan as admin.",
				"As admin payback the rest amount to the player.");
		
		argumentInput("money_loan_reject", "reject", basePermission,
				"/money loan reject", "/money loan reject ",
				"&c/money loan reject &f| Lehnt einen Kreditvorschlag ab.",
				"&c/money loan reject &f| Rejects a loan proposal.",
				"Reject a loan proposal");
		
		argumentInput("money_loan_repay", "repay", basePermission,
				"/money loan repay <id> <amount>", "/money loan repay ",
				"&c/money loan repay <id> <Betrag> &f| Zahlt einen Betrag vom Kredit ab.",
				"&c/money loan repay <id> <amount> &f| Pays an amount off the loan.",
				"Repay the amount to a loan.");
		
		argumentInput("money_loan_send", "send", basePermission,
				"/money loan send <player>", "/money loan send ",
				"&c/money loan send <spielername> &f| Sendet einen Kreditvorschlag einem Spieler.",
				"&c/money loan send <player> &f| Sends a loan proposal to a player.",
				"Send the player a loan proposal");
		
		argumentInput("money_loan_time", "time", basePermission,
				"/money loan time <starttime> <endtime> <repeatingtime>", "/money loan time ",
				"&c/money loan time <startdatum|dd.MM.yyyy-HH:mm> <entdatum|dd.MM.yyyy-HH:mm> <ratenzyklus|dd-HH:mm> &f| Setzt die Zeiten für den Kreditvorschlag.",
				"&c/money loan time <starttime|dd.MM.yyyy-HH:mm> <endtime|dd.MM.yyyy-HH:mm> <repeatingtime|dd-HH:mm> &f| Sets the times for the loan proposal.",
				"Set the times. Starttime and Endtime in <dd.MM.yyyy-HH:mm> and RepeatingTime in <dd-HH:mm> format");
		
		argumentInput("money_loan_transfer", "", basePermission,
				"/money loan transfer <id> <player>", "/money loan transfer ",
				"&c/money loan transfer <id> <Spielername> &f| Überträgt den Eigentümerstatus und Rückzahlrecht an den Spieler.",
				"&c/money loan transfer <id> <player> &f| Transfers the ownership status and repayment right to the player.",
				"Transfer the ownerstatus of your loan to the other player.");
		
		
	}
	
	private void comMoneyStandingOrder()
	{
		String basePermission = "eco.cmd.money";
		argumentInput("money_standingorder", "standingorder", basePermission,
				"/money standingorder", "/money standingorder ",
				"&c/money standingorder &f| Zwischenbefehl",
				"",
				"Default Command");
		
		argumentInput("money_standingorders", "standingorders", basePermission,
				"/money standingorders [page] [playername]", "/money standingorders ",
				"&c/money standingorders [Seitenzahl] [Spielername] &f| Listet alle Daueraufträge des Spielers auf.",
				"",
				"Shows all standingorders of a Player.");
		
		basePermission = "eco.cmd.money.standingorder";
		argumentInput("money_standingorder_amount", "amount", basePermission,
				"/money standingorder amount <amount>", "/money standingorder amount ",
				"&c/money standingorder amount <Betrag> &f| Setzt den Betrag für ein noch wartenden Dauerauftrag.",
				"",
				"Set to a waiting standingorder the amount");
		
		argumentInput("money_standingorder_cancel", "cancel", basePermission,
				"/money standingorder cancel", "/money standingorder cancel ",
				"&c/money standingorder cancel &f| Bricht den noch wartenden Dauerauftrag ab.",
				"",
				"Cancel the waiting standing order.");
		
		argumentInput("money_standingorder_create", "create", basePermission,
				"/money standingorder create <name> <from> <to>", "/money standingorder create ",
				"&c/money standingorder create <name> <Von> <Zu> &f| Erstellt einen wartenden Dauerauftrag. Durch weitere Einstellung wird dieser finalisiert.",
				"",
				"Create a Standingorder. Additional Settings must be set.");
		
		argumentInput("money_standingorder_delete", "delete", basePermission,
				"/money standingorder delete <id>", "/money standingorder delete ",
				"&c/money standingorder delete <id> &f| Löscht den schon existierenden Dauerauftrag.",
				"",
				"Delete the standing order.");
		
		argumentInput("money_standingorder_info", "info", basePermission,
				"/money standingorder info [id]", "/money standingorder info ",
				"&c/money standingorder info [id] &f| Zeigt alle Info zu einem Dauerauftrag an. Bei keiner Angabe, zeigt es den noch wartenden Dauerauftrag.",
				"",
				"Show Infos about standingorder.");
		
		argumentInput("money_standingorder_list", "list", basePermission,
				"/money standingorder list [page]", "/money standingorder list ",
				"&c/money standingorder list [Seitenzahl] &f| Listet alle Daueraufträge von allen Spielern auf.",
				"",
				"Show a list of all standingorders from all players.");
		
		argumentInput("money_standingorder_pause", "pause", basePermission,
				"/money standingorder pause <id>", "/money standingorder pause ",
				"&c/money standingorder pause <ID> &f| Pausiert den Dauerauftrag. Falls er vorher abgebrochen wurde, setzte er den Status zurück.",
				"",
				"Paused a standingorder or cancelled the cancelstatus.");
		
		argumentInput("money_standingorder_repeatingtime", "repeatingtime", basePermission,
				"/money standingorder repeatingtime <dd-HH:mm value>", "/money standingorder repeatingtime ",
				"&c/money standingorder repeatingtime <dd-HH:mm Wert> &f| Setzt eine Wiederholungsvariable, welche im dd-HH:mm Format geschrieben werden muss.",
				"",
				"Set the repeating time of the waiting standing order. Must use the specific >dd-HH:mm< Pattern. For example 01-23:50 for 1 day, 23 hours and 50 seconds.");
		
		argumentInput("money_standingorder_starttime", "starttime", basePermission,
				"/money standingorder starttime <dd.MM.yyyy-HH:mm value>", "/money standingorder starttime ",
				"&c/money standingorder starttime <dd.MM.yyyy-HH:mm Wert> &f| Setzt das Startdatum. Es müssen vorher alle anderen Eigenschaften gesetzt sein, denn dieser Befehl startet auch gleichzeitig den Dauerauftrag!",
				"",
				"Set the starttime in <dd.MM.yyyy-HH:mm> format. And Starts the standing order!");
	}
	
	public void initLanguage()
	{
		languageKeys.put("NoPermission"
			, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
			"&cDu hast dafür keine Rechte!",
			"&cYou have no rights!"}));
		/*languageKeys.put(""
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
				"",
				""}));*/
	}
	
	@SuppressWarnings("unused") //TODO:FilterSettings
	public void initFilterSettings()
	{
		ActualParameter:
		{
			loggerSettingsKeys.put("ActualParameter"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
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
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§eAktuelle Werte:",
					"§cKeine Voreinstellung vorhanden.",
					"§eActual Values:",
					"§cNo preset available.",
					}));
		}
		Output:
		{
			loggerSettingsKeys.put("40.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&4Ausgabe &6BarChart",
					"&4Ausgabe &6BarChart"}));
			loggerSettingsKeys.put("40.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BIRCH_DOOR"}));
			loggerSettingsKeys.put("40.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§9Klick §ezum Aufrufen des BarCharts des","§eAktionlogs nach den jetzigen Parametern.",
					"§9Click §eto call the barchart of the","§eaction log according to the current parameters."}));
			
			loggerSettingsKeys.put("48.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&4Ausgabe &6Diagramm",
					"&4Ausgabe &6Diagram"}));
			loggerSettingsKeys.put("48.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"OAK_DOOR"}));
			loggerSettingsKeys.put("48.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§9Linksklick §ezum Aufrufen des Diagramms des","§eAktionlogs nach den jetzigen Parametern.",
					"§9Rechtsklick §ezum Aufrufen des Diagramms des","§eTrendlogs nach den jetzigen Parametern.",
					"§9Left-click §eto call the diagram of the","§eaction log according to the current parameters.",
					"§9Right-click §eto call the diagram of the","§etrend log according to the current parameters.",}));
			
			loggerSettingsKeys.put("49.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&4Ausgabe &6Log",
					"&4Ausgabe &6Log"}));
			loggerSettingsKeys.put("49.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"SPRUCE_DOOR"}));
			loggerSettingsKeys.put("49.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§9Linksklick §ezum Aufrufen des Logs des","§eAktionlogs nach den jetzigen Parametern.",
					"§9Rechtsklick §ezum Aufrufen des Logs des","§eTrendlogs nach den jetzigen Parametern.",
					"§9Left-click §eto call the log of the","§eaction log according to the current parameters.",
					"§9Right-click §eto call the log of the","§etrend log according to the current parameters."}));
			
			loggerSettingsKeys.put("50.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&4Ausgabe &6Grafik",
					"&4Ausgabe &6Grafic"}));
			loggerSettingsKeys.put("50.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"DARK_OAK_DOOR"}));
			loggerSettingsKeys.put("50.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§9Linksklick §ezum Aufrufen der Grafik des","§eAktionlogs nach den jetzigen Parametern.",
					"§9Rechtsklick §ezum Aufrufen der Grafik des","§eTrendlogs nach den jetzigen Parametern.",
					"§9Left-click §eto call the grafic of the","§eaction log according to the current parameters.",
					"§9Right-click §eto call the grafic of the","§etrend log according to the current parameters."}));
		}
		PreSet:
		{
			loggerSettingsKeys.put("36.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Vor&beinstellung &f1",
					"&6Pre&bSet &f1"}));
			loggerSettingsKeys.put("36.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("36.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Laden des Voreinstellung.",
					"§9Rechtsklick §ezum Speichern der Voreinstellung.",
					"§4Shiftklick §ezum Löschen des Voreinstellung.",
					"§aLeft-click §eto load the preset.",
					"§9Right-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			loggerSettingsKeys.put("44.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Vor&beinstellung &f2",
					"&6Pre&bSet &f2"}));
			loggerSettingsKeys.put("44.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("44.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Laden des Voreinstellung.",
					"§9Rechtsklick §ezum Speichern der Voreinstellung.",
					"§4Shiftklick §ezum Löschen des Voreinstellung.",
					"§aLeft-click §eto load the preset.",
					"§9Right-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			loggerSettingsKeys.put("45.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Vor&beinstellung &f3",
					"&6Pre&bSet &f3"}));
			loggerSettingsKeys.put("45.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("45.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Laden des Voreinstellung.",
					"§9Rechtsklick §ezum Speichern der Voreinstellung.",
					"§4Shiftklick §ezum Löschen des Voreinstellung.",
					"§aLeft-click §eto load the preset.",
					"§9Right-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			loggerSettingsKeys.put("53.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Vor&beinstellung &f4",
					"&6Pre&bSet &f4"}));
			loggerSettingsKeys.put("53.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("53.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
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
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMin +&f1&f | &a+&f50",
					"&aMin +&f1&f | &a+&f50"}));
			loggerSettingsKeys.put("0.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("0.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>KleinerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>LessThanAmount<(Action)§f and §b>Between<",
					"§fand §b>LessThanRelativeAmount<(Trend)",
					"§9Left-click §eto increase the","§eMinimum-Parameters by §a1§e.",
					"§9Right-click §eto increase the","§eMinimum-Parameters by §a50§e.",
					"§9Shift-click §eto reset the parameter!"}));
			loggerSettingsKeys.put("1.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMin &c-&f1&f | &c-&f50",
					"&cMin &c-&f1&f | &c-&f50"}));
			loggerSettingsKeys.put("1.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("1.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben ","§b>KleinerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>KleinerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMinimum-Parameters um §c1§e.",
					"§9Rechtsklick §ezum Verringern des","§eMinimum-Parameters um §c50§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>LessThanAmount<(Action)§f and §b>Between<",
					"§fand §b>LessThanRelativeAmount<(Trend)",
					"§9Left-click §eto decrease the","§eMinimum-Parameters by §c1§e.",
					"§9Right-click §eto decrease the","§eMinimum-Parameters by §c50§e.",
					"§9Shift-click §eto reset the parameter!"}));
			
			loggerSettingsKeys.put("9.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMin +&f1.000&f | &a+&f50.000",
					"&aMin +&f1,000&f | &a+&f50,000"}));
			loggerSettingsKeys.put("9.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("9.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>KleinerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1.000§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>LessThanAmount<(Action)§f and §b>Between<",
					"§fand §b>LessThanRelativeAmount<(Trend)",
					"§9Left-click §eto increase the","§eMinimum-Parameters by §a1,000§e.",
					"§9Right-click §eto increase the","§eMinimum-Parameters by §a50,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("10.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMin &c-&f1.000&f | &c-&f50.000",
					"&cMin &c-&f1,000&f | &c-&f50,000"}));
			loggerSettingsKeys.put("10.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("10.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>KleinerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMinimum-Parameters um §c1.000§e.",
					"§9Rechtsklick §ezum Verringern des","§eMinimum-Parameters um §c50.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>LessThanAmount<(Action)§f and §b>Between<",
					"§fand §b>LessThanRelativeAmount<(Trend)",
					"§9Left-click §eto decrease the","§eMinimum-Parameters by §c50.000§e.",
					"§9Right-click §eto decrease the","§eMinimum-Parameters by §c50,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("18.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMin +&f1.000.000&f | &a+&f50.000.000",
					"&aMin +&f1,000,000&f | &a+&f50,000,000"}));
			loggerSettingsKeys.put("18.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("18.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>KleinerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1.000.000§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50.000.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>LessThanAmount<(Action)§f and §b>Between<",
					"§fand §b>LessThanRelativeAmount<(Trend)",
					"§9Left-click §eto increase the","§eMinimum-Parameters by §a1,000,000§e.",
					"§9Right-click §eto increase the","§eMinimum-Parameters by §a50,000,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("19.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMin &c-&f1.000.000&f | &c-&f50.000.000",
					"&cMin &c-&f1,000,000&f | &c-&f50,000,000"}));
			loggerSettingsKeys.put("19.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("19.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>KleinerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMinimum-Parameters um §c1.000.000§e.",
					"§9Rechtsklick §ezum Verringern des","§eMinimum-Parameters um §c50.000.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>LessThanAmount<(Action)§f and §b>Between<",
					"§fand §b>LessThanRelativeAmount<(Trend)",
					"§9Left-click §eto decrease the","§eMinimum-Parameters by §c1,000,000§e.",
					"§9Right-click §eto decrease the","§eMinimum-Parameters by §c50,000,000§e.",
					"§9Shift-click §to reset the parameter!"}));
		}
		
		Max:
		{
			loggerSettingsKeys.put("7.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMax +&f1&f | &a+&f50",
					"&aMax +&f1&f | &a+&f50"}));
			loggerSettingsKeys.put("7.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("7.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>GrößerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>GreatherThanAmount<(Action)§f and §b>Between<",
					"§fand §b>GreatherThanRelativeAmount<(Trend)",
					"§9Left-click §eto increase the","§emaximum-parameter by §a1§e.",
					"§9Right-click §eto increase the","§emaximum-parameter by §a50§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("8..Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMax &c-&f1&f | &c-&f50",
					"&cMax &c-&f1&f | &c-&f50"}));
			loggerSettingsKeys.put("8..Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("8..Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>GrößerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMaximum-Parameters um §c1§e.",
					"§9Rechtsklick §ezum Verringern des","§eMaximum-Parameters um §c50§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>GreatherThanAmount<(Action)§f and §b>Between<",
					"§fand §b>GreatherThanRelativeAmount<(Trend)",
					"§9Left-click §eto decrease the","§emaximum-parameter by §c1§e.",
					"§9Right-click §eto decrease the","§emaximum-parameter by §c50§e.",
					"§9Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("16.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMax +&f1.000&f | &a+&f50.000",
					"&aMax +&f1,000&f | &a+&f50,000"}));
			loggerSettingsKeys.put("16.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("16.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>GrößerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1.000§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMaximum-Parameters um §a50.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>GreatherThanAmount<(Action)§f and §b>Between<",
					"§fand §b>GreatherThanRelativeAmount<(Trend)",
					"§9Left-click §eto increase the","§emaximum-parameter by §a1.000§e.",
					"§9Right-click §eto increase the","§emaximum-parameter by §a50,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("17.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMax &c-&f1.000&f | &c-&f50.000",
					"&cMax &c-&f1,000&f | &c-&f50,000"}));
			loggerSettingsKeys.put("17.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("17.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>GrößerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMaximum-Parameters um §c1.000§e.",
					"§9Rechtsklick §ezum Verringern des","§eMaximum-Parameters um §c50.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>GreatherThanAmount<(Action)§f and §b>Between<",
					"§fand §b>GreatherThanRelativeAmount<(Trend)",
					"§9Left-click §eto decrease the","§emaximum-parameter by §c1,000§e.",
					"§9Right-click §eto decrease the","§emaximum-parameter by §c50,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("25.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMax +&f1.000.000&f | &a+&f50.000.000",
					"&aMax +&f1.000,000&f | &a+&f50,000,000"}));
			loggerSettingsKeys.put("25.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("25.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>GrößerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1.000.000§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMaximum-Parameters um §a50.000.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>GreatherThanAmount<(Action)§f and §b>Between<",
					"§fand §b>GreatherThanRelativeAmount<(Trend)",
					"§9Left-click §eto increase the","§emaximum-parameter by §a1,000,000§e.",
					"§9Right-click §eto increase the","§emaximum-parameter by §a50,000,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("26.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMax &c-&f1.000.000&f | &c-&f50.000.000",
					"&cMax &c-&f1,000,000&f | &c-&f50,000,000"}));
			loggerSettingsKeys.put("26.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("26.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAlsBetrag<(Aktion)§f und §b>Zwischen<",
					"§fsowie §b>GrößerAlsRevativerBetrag<(Trend)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMaximum-Parameters um §c1.000.000§e.",
					"§9Rechtsklick §ezum Verringern des","§eMaximum-Parameters um §c50.000.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the specifications", "§b>GreatherThanAmount<(Action)§f and §b>Between<",
					"§fand §b>GreatherThanRelativeAmount<(Trend)",
					"§9Left-click §eto decrease the","§emaximum-parameter by §c1,000,000§e.",
					"§9Right-click §eto decrease the","§emaximum-parameter by §c50,000,000§e.",
					"§9Shift-click §to reset the parameter!"}));
		}
		
		FirstStand:
		{
			loggerSettingsKeys.put("29.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aAnfangsstand +&f1.000&f | &a+&f50.000",
					"&aFirstStand +&f1,000&f | &a+&f50,000"}));
			loggerSettingsKeys.put("29.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_TERRACOTTA"}));
			loggerSettingsKeys.put("29.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Anfangsstand< (nur Trendlog)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1.000§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§9Left-click §eto increase the","§eMinimum-Parameters by §a1,000§e.",
					"§9Right-click §eto increase the","§eMinimum-Parameters by §a50,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("30.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cAnfangsstand &c-&f1.000&f | &c-&f50.000",
					"&cFirstStand &c-&f1,000&f | &c-&f50,000"}));
			loggerSettingsKeys.put("30.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_TERRACOTTA"}));
			loggerSettingsKeys.put("30.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Anfangsstand< (nur Trendlog)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMinimum-Parameters um §c1.000§e.",
					"§9Rechtsklick §ezum Verringern des","§eMinimum-Parameters um §c50.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§9Left-click §eto decrease the","§eMinimum-Parameters by §c1,000§e.",
					"§9Right-click §eto decrease the","§eMinimum-Parameters by §c50,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("38.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aAnfangsstand +&f1.000.000&f | &a+&f50.000.000",
					"&aFirstStand +&f1,000,000&f | &a+&f50,000,000"}));
			loggerSettingsKeys.put("38.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_TERRACOTTA"}));
			loggerSettingsKeys.put("38.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Anfangsstand< (nur Trendlog)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMinimum-Parameters um §a1.000.000§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMinimum-Parameters um §a50.000.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§9Left-click §eto increase the","§eMinimum-Parameters by §a1,000,000§e.",
					"§9Right-click §eto increase the","§eMinimum-Parameters by §a50,000,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("39.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cAnfangsstand &c-&f1.000.000&f | &c-&f50.000.000",
					"&cFirstStand &c-&f1,000,000&f | &c-&f50,000,000"}));
			loggerSettingsKeys.put("39.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_TERRACOTTA"}));
			loggerSettingsKeys.put("39.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Anfangsstand< (nur Trendlog)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMinimum-Parameters um §a1.000.000§e.",
					"§9Rechtsklick §ezum Verringern des","§eMinimum-Parameters um §a50.000.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§9Left-click §eto decrease the","§eMinimum-Parameters by §a1,000,000§e.",
					"§9Right-click §eto decrease the","§eMinimum-Parameters by §a50,000,000§e.",
					"§9Shift-click §to reset the parameter!"}));
		}
		
		LastStand:
		{
			loggerSettingsKeys.put("32.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aLetzterstand +&f1.000&f | &a+&f50.000",
					"&aLastStand +&f1,000&f | &a+&f50,000"}));
			loggerSettingsKeys.put("32.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_TERRACOTTA"}));
			loggerSettingsKeys.put("32.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Letzterstand< (nur Trendlog)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1.000§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMaximum-Parameters um §a50.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the information","§b>LastStand< (Only Trendlog)§f.",
					"§9Left-click §eto increase the","§emaximum-parameter by §a1,000§e.",
					"§9Right-click §eto increase the","§emaximum-parameter by §a50,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("33.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cLetzterstand &c-&f1.000&f | &c-&f50.000",
					"&cFirstStand &c-&f1,000&f | &c-&f50,000"}));
			loggerSettingsKeys.put("33.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_TERRACOTTA"}));
			loggerSettingsKeys.put("33.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Letzterstand< (nur Trendlog)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMaximum-Parameters um §c1.000§e.",
					"§9Rechtsklick §ezum Verringern des","§eMaximum-Parameters um §c50.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the information","§b>LastStand< (Only Trendlog)§f.",
					"§9Left-click §eto decrease the","§emaximum-parameter by §c1,000§e.",
					"§9Right-click §eto decrease the","§emaximum-parameter by §c50,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("41.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aLetzterstand +&f1.000.000&f | &a+&f50.000.000",
					"&aFirstStand +&f1,000,000&f | &a+&f50,000,000"}));
			loggerSettingsKeys.put("41.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_TERRACOTTA"}));
			loggerSettingsKeys.put("41.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Letzterstand< (nur Trendlog)§f genutzt.",
					"§9Linksklick §ezum Erhöhen des","§eMaximum-Parameters um §a1.000.000§e.",
					"§9Rechtsklick §ezum Erhöhen des","§eMaximum-Parameters um §a50.000.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the information","§b>LastStand< (Only Trendlog)§f.",
					"§9Left-click §eto increase the","§emaximum-parameter by §a1,000,000§e.",
					"§9Right-click §eto increase the","§emaximum-parameter by §a50,000,000§e.",
					"§9Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("42.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cLetzterstand &c-&f1.000.000&f | &c-&f50.000.000",
					"&cFirstStand &c-&f1,000,000&f | &c-&f50,000,000"}));
			loggerSettingsKeys.put("42.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_TERRACOTTA"}));
			loggerSettingsKeys.put("42.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Letzterstand< (nur Trendlog)§f genutzt.",
					"§9Linksklick §ezum Verringern des","§eMaximum-Parameters um §c1.000.000§e.",
					"§9Rechtsklick §ezum Verringern des","§eMaximum-Parameters um §c50.000.000§e.",
					"§9Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the information","§b>LastStand< (Only Trendlog)§f.",
					"§9Left-click §eto decrease the","§emaximum-parameter by §fc1,000,000§e.",
					"§9Right-click §eto decrease the","§emaximum-parameter by §c50,000,000§e.",
					"§9Shift-click §to reset the parameter!"}));
		}
		
		TARGETEditor:
		{
			loggerSettingsKeys.put("3.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cVon-Parameter",
					"&cFrom-parameter"}));
			loggerSettingsKeys.put("3.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("3.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Von<§f genutzt.",
					"§aKlicke §ezur Eingabe eines Spielernamens","§eoder einer Bankkontonummer.",
					"§4Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the","§einformation §b>From<§f.",
					"§eClicks to enter a player name","§eor bank account number.",
					"§9Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("4.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cAuftraggeber-Parameter",
					"&cOrderer-parameter"}));
			loggerSettingsKeys.put("4.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("4.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Auftraggeber<§f genutzt.",
					"§aKlicke §ezur Eingabe eines Spielernamens","§eoder einer Bankkontonummer.",
					"§4Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the","§einformation §b>Orderer<§f.",
					"§eClicks to enter a player name","§eor bank account number.",
					"§9Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("5.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cZu-Parameter",
					"&cTo-parameter"}));
			loggerSettingsKeys.put("5.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("5.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Zu<§f genutzt.",
					"§aKlicke §ezur Eingabe eines Spielernamens","§eoder einer Bankkontonummer.",
					"§4Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the","§einformation §b>To<§f.",
					"§eClicks to enter a player name","§eor bank account number.",
					"§9Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("13.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cKommentar-Parameter",
					"&cComment-parameter"}));
			loggerSettingsKeys.put("13.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"TARGET"}));
			loggerSettingsKeys.put("13.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>Kommentar< (nur Aktionlog)§f genutzt.",
					"§aKlicke §ezur Eingabe eines Kommentars.",
					"§4Shiftklick §ezum Zurücksetzen des Wertes!",
					"§fParameter is used for the","§einformation §b>Comment< (only Actionlog)§f.",
					"§eClicks to enter a comment.",
					"§9Shift-click §to reset the parameter!"}));
		}
		
		Booleans:
		{
			loggerSettingsKeys.put("11.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cAuf/Absteigend&f-Parameter",
					"&cA/Descending&f-parameter"}));
			loggerSettingsKeys.put("11.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"REDSTONE_TORCH"}));
			loggerSettingsKeys.put("11.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§e§b>ID<§f oder §b>Betrag< §fgenutzt.",
					"§9Linksklick §ewählt §b>Aufsteigend<§e aus.",
					"§9Rechtsklick §ewählt §b>Absteigend<§e aus.",
					"§fParameter is used for the","§einformation §b>ID<§f or §b>Amount<.",
					"§9Left-click §eto §b>Ascending<§e set.",
					"§9Right-click §eto §b>Descending<§e set."}));
		}
		
		Enums:
		{
			loggerSettingsKeys.put("15.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cID oder Betrags&f-Parameter",
					"&cID or Amount&f-parameter"}));
			loggerSettingsKeys.put("15.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"REDSTONE_TORCH"}));
			loggerSettingsKeys.put("15.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter stellt die zu nutzende Spalte ein.","§eEntweder §b>ID< §eoder §b>Betrag<§f.",
					"§9Linksklick §ewählt §b>ID<§e aus.",
					"§9Rechtsklick §ewählt §b>Betrag<§e aus.",
					"§fParameter sets the column to be used.","§eEither §b>Id< §eor §b>Amount<§f.",
					"§9Left-click §eto §b>ID<§e set.",
					"§9Right-click §eto §b>Amount<§e set."}));
		}
	}
}
