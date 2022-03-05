package main.java.me.avankziar.aep.bungee.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.aep.general.objects.TaxationCase;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import main.java.me.avankziar.aep.spigot.database.Language;
import main.java.me.avankziar.aep.spigot.database.Language.ISO639_2B;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> currencyKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfig();
		initCurrencyFile();
		initLanguage();
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
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
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
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInputBungee(net.md_5.bungee.config.Configuration yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
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
	
	public void initConfig() //INFO:Config
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
	}
	
	public void initLanguage()
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
	
	public void initCurrencyFile()
	{
		LinkedHashMap<String, Language> currencyKey = new LinkedHashMap<>();
		currencyKey.put("UniqueName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dollar"}));
		currencyKey.put("DefaultCurrency"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKey.put("StandartUnitWorth"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1.0}));
		currencyKey.put("Currency.Exchangable"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKey.put("Currency.TaxationBeforeExchange"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKey.put("Gradation.CurrencyType"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"DIGITAL"})); //DIGITAL, ITEMSTACk, EXPERIENCE
		currencyKey.put("Gradation.DIGITAL.Base.Plural"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Cents"}));
		currencyKey.put("Gradation.DIGITAL.Base.Singular"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Cent"}));
		currencyKey.put("Gradation.DIGITAL.Base.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"¢"}));
		currencyKey.put("Gradation.DIGITAL.Base.ValueToBaseGradation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1}));
		currencyKey.put("Gradation.DIGITAL.1.Plural"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Dollars"}));
		currencyKey.put("Gradation.DIGITAL.1.Singular"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Dollar"}));
		currencyKey.put("Gradation.DIGITAL.1.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"$"}));
		currencyKey.put("Gradation.DIGITAL.1.ValueToBaseGradation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				100}));
		currencyKey.put("WhenPlayerFirstJoin.CreateWallets"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKey.put("WhenPlayerFirstJoin.WalletsToCreate"
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
		currencyKey.put("WhenPlayerFirstJoin.CreateBanks"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKey.put("WhenPlayerFirstJoin.BanksToCreate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				AccountCategory.TAX.toString()+";true;"+0
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString(),
				AccountCategory.VOID.toString()+";true;"+0
				+";"+AccountManagementType.CAN_SEE_BALANCE.toString()
				+";"+AccountManagementType.CAN_SEE_LOG.toString()}));
		currencyKey.put("Commands.StructurType"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandStructurType.NESTED.toString()}));
		//CommandExecuteType;commands.yml Path
		currencyKey.put("Commands.SINGLE"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandExecuteType.BALANCE.toString()+";balance",
				CommandExecuteType.PAY.toString()+";pay",
				CommandExecuteType.PAY_THROUGH_GUI.toString()+";paythroughgui",
				CommandExecuteType.GIVE.toString()+";give",
				CommandExecuteType.GIVE_CONSOLE.toString()+";giveconsole",
				CommandExecuteType.TAKE.toString()+";take",
				CommandExecuteType.TAKE_CONSOLE.toString()+";takeconsole",
				CommandExecuteType.SET.toString()+";set",
				CommandExecuteType.SET_CONSOLE.toString()+";setconsole"}));
		currencyKey.put("Commands.NESTED"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				CommandExecuteType.BALANCE.toString()+";money",
				CommandExecuteType.PAY.toString()+";money_pay",
				CommandExecuteType.PAY_THROUGH_GUI.toString()+";money_paythroughgui",
				CommandExecuteType.GIVE.toString()+";money_give",
				CommandExecuteType.GIVE_CONSOLE.toString()+";money_giveconsole",
				CommandExecuteType.TAKE.toString()+";money_take",
				CommandExecuteType.TAKE_CONSOLE.toString()+";money_takeconsole",
				CommandExecuteType.SET.toString()+";money_set",
				CommandExecuteType.SET_CONSOLE.toString()+";money_setconsole"}));
		currencyKey.put("Taxation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				TaxationCase.TRANSACTION_BETWEEN_PLAYERS.toString()+";false;"+0.01,
				TaxationCase.CURRENCY_EXCHANGE.toString()+";false;"+0.05,
				TaxationCase.LOANLENDING+";false;"+0.01,
				TaxationCase.LOANREPAYING+";false;"+0.01,
				TaxationCase.STANDINGORDER+";false;"+0.01}));
		currencyKey.put("Format.GradationQuantity"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1}));
		currencyKey.put("Format.UseSIPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKey.put("Format.DecimalPlaces"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2}));
		currencyKey.put("Format.UseSymbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKey.put("Format.ThousandSeperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				","}));
		currencyKey.put("Format.DecimalSeperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"."}));
		//SIPrefix Enum;Shortcut that you which
		currencyKey.put("Format.SIPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"YOTTA;Y",
				"ZETTA;Z",
				"EXA;E",
				"PETA;P",
				"TERA;T",
				"GIGA;G",
				"MEGA;M",
				"KILO;k",}));
		currencyKeys.put("dollar", currencyKey);
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
				"Cents"}));
		currencyKeyII.put("Gradation.DIGITAL.Base.Singular"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Cent"}));
		currencyKeyII.put("Gradation.DIGITAL.Base.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"¢"}));
		currencyKeyII.put("Gradation.DIGITAL.Base.ValueToBaseGradation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1}));
		currencyKeyII.put("Gradation.DIGITAL.1.Plural"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Dollars"}));
		currencyKeyII.put("Gradation.DIGITAL.1.Singular"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Dollar"}));
		currencyKeyII.put("Gradation.DIGITAL.1.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"$"}));
		currencyKeyII.put("Gradation.DIGITAL.1.ValueToBaseGradation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				100}));
		currencyKeyII.put("WhenPlayerFirstJoin.CreateWallets"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyII.put("WhenPlayerFirstJoin.WalletsToCreate"
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
				CommandExecuteType.PAY_THROUGH_GUI.toString()+";money_paythroughgui",
				CommandExecuteType.GIVE.toString()+";money_give",
				CommandExecuteType.GIVE_CONSOLE.toString()+";money_giveconsole",
				CommandExecuteType.TAKE.toString()+";money_take",
				CommandExecuteType.TAKE_CONSOLE.toString()+";money_takeconsole",
				CommandExecuteType.SET.toString()+";money_set",
				CommandExecuteType.SET_CONSOLE.toString()+";money_setconsole"}));
		currencyKeyII.put("Taxation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				TaxationCase.TRANSACTION_BETWEEN_PLAYERS.toString()+";false;"+0.01,
				TaxationCase.CURRENCY_EXCHANGE.toString()+";false;"+0.05,
				TaxationCase.LOANLENDING+";false;"+0.01,
				TaxationCase.LOANREPAYING+";false;"+0.01,
				TaxationCase.STANDINGORDER+";false;"+0.01}));
		currencyKeyII.put("Format.GradationQuantity"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1}));
		currencyKeyII.put("Format.UseSIPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		currencyKeyII.put("Format.DecimalPlaces"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2}));
		currencyKeyII.put("Format.UseSymbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
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
