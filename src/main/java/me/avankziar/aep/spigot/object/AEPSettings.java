package main.java.me.avankziar.aep.spigot.object;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.YamlHandler;
import main.java.me.avankziar.aep.spigot.handler.BankAccountHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;

public class AEPSettings
{
	private ArrayList<String> reservedNames;
	private BankAccountHandler.Type numberType;
	private boolean bungee;
	private boolean mysql;
	private boolean debug;
	private boolean bank;
	private boolean playerAccount;
	private boolean standingOrder;
	private boolean loanRepayment;
	private String currencySingular;
	private String currencyPlural;
	private int moneyFormat;
	private String prefix;
	private boolean executeStandingOrderPayment;
	private boolean executeLoanPayment;
	private long standingOrderSpamProtection;
	private double standingOrderValueProtection;
	private LinkedHashMap<String, String> commands = new LinkedHashMap<>(); //To save commandstrings
	
	public static AEPSettings settings;
	
	public AEPSettings()
	{
		
	}
	
	public AEPSettings(String prefix,
			boolean bungee, boolean mysql, boolean debug,
			boolean playerAccount, boolean bank,
			boolean standingorder, boolean loanrepayment,
			String currencySingular, String currencyPlural, int moneyFormat,
			ArrayList<String> reservedNames, BankAccountHandler.Type numberType,
			boolean executeStandingOrderPayment, boolean executeLoanPayment,
			long standingOrderSpamProtection, double standingOrderValueProtection)
	{
		setPrefix(prefix);
		setBungee(bungee);
		setMysql(mysql);
		setDebug(debug);
		setPlayerAccount(playerAccount);
		setBank(bank);
		setStandingOrder(standingorder);
		setLoanRepayment(loanrepayment);
		setCurrencySingular(currencySingular);
		setCurrencyPlural(currencyPlural);
		setMoneyFormat(moneyFormat);
		setReservedNames(reservedNames);
		setNumberType(numberType);
		setExecuteStandingOrderPayment(executeStandingOrderPayment);
		setExecuteLoanPayment(executeLoanPayment);
		setStandingOrderSpamProtection(standingOrderSpamProtection);
		setStandingOrderValueProtection(standingOrderValueProtection);
	}
	
	public static void initSettings(AdvancedEconomyPlus plugin)
	{
		YamlHandler yh = plugin.getYamlHandler();
		String prefix = yh.getConfig().getString("Prefix", "&7[&2AdvancedEconomyPlus&7] &r");
		boolean bungee = plugin.getYamlHandler().getConfig().getBoolean("Bungee", false);
		boolean mysql = false;
		if(plugin.getMysqlSetup().getConnection() != null)
		{
			mysql = true;
		}
		boolean debug = yh.getConfig().getBoolean("Use.DebuggingMode", false);
		boolean playerAccount = yh.getConfig().getBoolean("Use.PlayerAccount", false);
		boolean bank = false; //yh.getConfig().getBoolean("Use.Bank", false);
		boolean standingorder = yh.getConfig().getBoolean("Use.StandingOrder", false);
		boolean loanrepayment = false;//FIXME yh.getConfig().getBoolean("Use.LoanRepayment", false);
		boolean executeStandingOrderPayment = yh.getConfig().getBoolean("Exceute.StandingOrderPayments", false);
		boolean executeLoanPayment = yh.getConfig().getBoolean("Exceute.LoanPayments", false);
		String currencySingular = yh.getConfig().getString("CurrencyNameSingular","Euro");
		String currencyPlural = yh.getConfig().getString("CurrencyNamePlural","Euros");
		long standingOrderSpamProtection = TimeHandler.getRepeatingTime(yh.getConfig().getString("StandingOrderTimeSpamProtection", "00-00:15")); 
		double standingOrderValueProtection = yh.getConfig().getDouble("StandingOrderValueSpamProtection", 1000.0);
		int format = yh.getConfig().getInt("MoneyFormat", 0);
		ArrayList<String> reservedNames = new ArrayList<String>();
		if(yh.getConfig().getStringList("ReservedNames") != null)
		{
			reservedNames = (ArrayList<String>) yh.getConfig().getStringList("ReservedNames");
		}
		BankAccountHandler.Type numberType = BankAccountHandler.Type.FOUR_DIGITS_TIMES_THREE;
		if(yh.getConfig().getString("TrendLogger.ValueIsStabil") != null)
		{
			try
			{
				numberType = BankAccountHandler.Type.valueOf(plugin.getYamlHandler().getConfig().getString("TrendLogger.ValueIsStabil"));
			} catch (Exception e) {}
		}
		settings = new AEPSettings(prefix,
				bungee, mysql, debug,
				playerAccount, bank,
				standingorder, loanrepayment,
				currencySingular, currencyPlural, format,
				reservedNames, numberType,
				executeStandingOrderPayment, executeLoanPayment,
				standingOrderSpamProtection, standingOrderValueProtection);
		plugin.getLogger().info("Economy Settings init...");
	}
	
	public static void debug(AdvancedEconomyPlus plugin, String s)
	{
		if(AEPSettings.settings != null && AEPSettings.settings.isDebug())
		{
			if(plugin != null)
			{
				plugin.getLogger().info(s);
			}
		}
	}

	public ArrayList<String> getReservedNames()
	{
		return reservedNames;
	}

	public void setReservedNames(ArrayList<String> reservedNames)
	{
		this.reservedNames = reservedNames;
	}

	public BankAccountHandler.Type getNumberType()
	{
		return numberType;
	}

	public void setNumberType(BankAccountHandler.Type numberType)
	{
		this.numberType = numberType;
	}

	public boolean isMysql()
	{
		return mysql;
	}

	public void setMysql(boolean mysql)
	{
		this.mysql = mysql;
	}

	public boolean useBank()
	{
		return bank;
	}

	public void setBank(boolean bank)
	{
		this.bank = bank;
	}

	public boolean usePlayerAccount()
	{
		return playerAccount;
	}

	public void setPlayerAccount(boolean playerAccount)
	{
		this.playerAccount = playerAccount;
	}

	public String getCurrencySingular()
	{
		return currencySingular;
	}

	public void setCurrencySingular(String currencySingular)
	{
		this.currencySingular = currencySingular;
	}

	public String getCurrencyPlural()
	{
		return currencyPlural;
	}

	public void setCurrencyPlural(String currencyPlural)
	{
		this.currencyPlural = currencyPlural;
	}

	public int getMoneyFormat()
	{
		return moneyFormat;
	}

	public void setMoneyFormat(int moneyFormat)
	{
		this.moneyFormat = moneyFormat;
	}

	public boolean isBank()
	{
		return bank;
	}

	public boolean isPlayerAccount()
	{
		return playerAccount;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public boolean isBungee()
	{
		return bungee;
	}

	public void setBungee(boolean bungee)
	{
		this.bungee = bungee;
	}

	public boolean isStandingOrder()
	{
		return standingOrder;
	}

	public void setStandingOrder(boolean standingOrder)
	{
		this.standingOrder = standingOrder;
	}

	public boolean isLoanRepayment()
	{
		return loanRepayment;
	}

	public void setLoanRepayment(boolean loanRepayment)
	{
		this.loanRepayment = loanRepayment;
	}

	public boolean isExecuteStandingOrderPayment()
	{
		return executeStandingOrderPayment;
	}

	public void setExecuteStandingOrderPayment(boolean executeStandingOrderPayment)
	{
		this.executeStandingOrderPayment = executeStandingOrderPayment;
	}

	public boolean isExecuteLoanPayment()
	{
		return executeLoanPayment;
	}

	public void setExecuteLoanPayment(boolean executeLoanPayment)
	{
		this.executeLoanPayment = executeLoanPayment;
	}

	public boolean isDebug()
	{
		return debug;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	public String getCommands(String key)
	{
		return commands.get(key);
	}

	public void setCommands(LinkedHashMap<String, String> commands)
	{
		this.commands = commands;
	}
	
	public void addCommands(String key, String commandString)
	{
		if(commands.containsKey(key))
		{
			commands.replace(key, commandString);
		} else
		{
			commands.put(key, commandString);
		}
	}

	public long getStandingOrderSpamProtection()
	{
		return standingOrderSpamProtection;
	}

	public void setStandingOrderSpamProtection(long standingOrderSpamProtection)
	{
		this.standingOrderSpamProtection = standingOrderSpamProtection;
	}

	public double getStandingOrderValueProtection()
	{
		return standingOrderValueProtection;
	}

	public void setStandingOrderValueProtection(double standingOrderValueProtection)
	{
		this.standingOrderValueProtection = standingOrderValueProtection;
	}
}
