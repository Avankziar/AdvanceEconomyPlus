package main.java.me.avankziar.aep.spigot.object;

import java.util.ArrayList;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.YamlHandler;
import main.java.me.avankziar.aep.spigot.handler.BankAccountHandler;

public class EconomySettings
{
	private ArrayList<String> reservedNames;
	private BankAccountHandler.Type numberType;
	private boolean bungee;
	private boolean mysql;
	private boolean bank;
	private boolean playerAccount;
	private boolean standingOrder;
	private boolean loanRepayment;
	private String currencySingular;
	private String currencyPlural;
	private int moneyFormat;
	private String prefix;
	
	public static EconomySettings settings;
	
	public EconomySettings(String prefix,
			boolean bungee, boolean mysql, boolean playerAccount, boolean bank,
			boolean standingorder, boolean loanrepayment,
			String currencySingular, String currencyPlural, int moneyFormat,
			ArrayList<String> reservedNames, BankAccountHandler.Type numberType)
	{
		setPrefix(prefix);
		setBungee(bungee);
		setMysql(mysql);
		setPlayerAccount(playerAccount);
		setBank(bank);
		setStandingOrder(standingorder);
		setLoanRepayment(loanrepayment);
		setCurrencySingular(currencySingular);
		setCurrencyPlural(currencyPlural);
		setMoneyFormat(moneyFormat);
		setReservedNames(reservedNames);
		setNumberType(numberType);
	}
	
	public static void initSettings(AdvancedEconomyPlus plugin)
	{
		if(settings != null)
		{
			settings = null;
		}
		YamlHandler yh = plugin.getYamlHandler();
		String prefix = "&7[&2Economy&7] ";
		if(yh.getConfig().getString("Prefix") != null)
		{
			prefix = yh.getConfig().getString("Prefix");
		}
		boolean bungee = false;
		if(plugin.getYamlHandler().getConfig().get("Bungee") != null)
		{
			bungee = plugin.getYamlHandler().getConfig().getBoolean("Bungee");
		}
		boolean mysql = false;
		if(plugin.getMysqlSetup().getConnection() != null)
		{
			mysql = true;
		}
		boolean playerAccount = plugin.getYamlHandler().getConfig().getBoolean("UsePlayerAccount", false);
		boolean bank = yh.getConfig().getBoolean("UseBank", false);
		boolean standingorder = yh.getConfig().getBoolean("UseStandingOrder", false);
		boolean loanrepayment = yh.getConfig().getBoolean("UseLoanRepayment", false);
		String currencySingular = yh.getConfig().getString("CurrencyNameSingular","Euro");
		String currencyPlural = yh.getConfig().getString("CurrencyNamePlural","Euros");
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
		settings = new EconomySettings(prefix,
				bungee, mysql, playerAccount, bank,
				standingorder, loanrepayment,
				currencySingular, currencyPlural, format,
				reservedNames, numberType);
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

}
