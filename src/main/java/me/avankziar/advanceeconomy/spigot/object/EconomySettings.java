package main.java.me.avankziar.advanceeconomy.spigot.object;

import java.util.ArrayList;

import main.java.me.avankziar.advanceeconomy.spigot.database.YamlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;

public class EconomySettings
{
	private ArrayList<String> reservedNames;
	private BankAccount.Type numberType;
	private boolean bungee;
	private boolean mysql;
	private boolean bank;
	private boolean playerAccount;
	private String currencySingular;
	private String currencyPlural;
	private int moneyFormat;
	private String prefix;
	
	public static EconomySettings settings;
	
	public EconomySettings(String prefix, boolean bungee, boolean mysql, boolean playerAccount, boolean bank,
			String currencySingular, String currencyPlural, int moneyFormat,
			ArrayList<String> reservedNames, BankAccount.Type numberType)
	{
		setPrefix(prefix);
		setBungee(bungee);
		setMysql(mysql);
		setPlayerAccount(playerAccount);
		setBank(bank);
		setCurrencySingular(currencySingular);
		setCurrencyPlural(currencyPlural);
		setMoneyFormat(moneyFormat);
		setReservedNames(reservedNames);
		setNumberType(numberType);
	}
	
	public static void initSettings(AdvanceEconomy plugin)
	{
		if(settings != null)
		{
			settings = null;
		}
		YamlHandler yh = plugin.getYamlHandler();
		String prefix = "&7[&2Economy&7] ";
		if(yh.get().getString("Prefix") != null)
		{
			prefix = yh.get().getString("Prefix");
		}
		boolean bungee = false;
		if(plugin.getYamlHandler().get().get("Bungee") != null)
		{
			bungee = plugin.getYamlHandler().get().getBoolean("Bungee");
		}
		boolean mysql = false;
		if(plugin.getMysqlSetup().getConnection() != null)
		{
			mysql = true;
		}
		boolean playerAccount = false;
		if(yh.get().get("UsePlayerAccount") != null)
		{
			playerAccount = plugin.getYamlHandler().get().getBoolean("UsePlayerAccount");
		}
		boolean bank = false;
		if(yh.get().get("UseBank") != null)
		{
			bank = yh.get().getBoolean("UseBank");
		}
		String currencySingular = "Euro";
		if(yh.get().getString("CurrencyNameSingular") != null)
		{
			currencySingular = yh.get().getString("CurrencyNameSingular");
		}
		String currencyPlural = "Euros";
		if(yh.get().getString("CurrencyNamePlural") != null)
		{
			currencyPlural = yh.get().getString("CurrencyNamePlural");
		}
		int format = 0;
		if(yh.get().get("MoneyFormat") != null)
		{
			format = yh.get().getInt("MoneyFormat");
		}
		ArrayList<String> reservedNames = new ArrayList<String>();
		if(yh.get().getStringList("ReservedNames") != null)
		{
			reservedNames = (ArrayList<String>) yh.get().getStringList("ReservedNames");
		}
		BankAccount.Type numberType = BankAccount.Type.FOUR_DIGITS_TIMES_THREE;
		if(yh.get().getString("TrendLogger.ValueIsStabil") != null)
		{
			try
			{
				numberType = BankAccount.Type.valueOf(plugin.getYamlHandler().get().getString("TrendLogger.ValueIsStabil"));
			} catch (Exception e) {}
		}
		settings = new EconomySettings(prefix, bungee, mysql, playerAccount, bank,
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

	public BankAccount.Type getNumberType()
	{
		return numberType;
	}

	public void setNumberType(BankAccount.Type numberType)
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

}
