package main.java.me.avankziar.aep.spigot.api.economy;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.ifh.spigot.economy.Economy;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountType;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.spigot.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;
import main.java.me.avankziar.ifh.spigot.economy.currency.Currency;
import main.java.me.avankziar.ifh.spigot.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import main.java.me.avankziar.ifh.spigot.economy.currency.Gradation;

public class IFHApi implements Economy
{
	private AdvancedEconomyPlus plugin;
	protected AccountHandler accountHandler;
	protected CurrencyHandler currencyHandler;
	private TransactionHandler transactionHandler;
	
	protected LinkedHashMap<String, Integer> defaultGradationQuantity = new LinkedHashMap<>();
	protected LinkedHashMap<String, Boolean> defaultUseSIPrefix = new LinkedHashMap<>();
	protected LinkedHashMap<String, Integer> defaultDecimalPlaces = new LinkedHashMap<>();
	protected LinkedHashMap<String, Boolean> defaultUseSymbol = new LinkedHashMap<>();
	protected LinkedHashMap<String, String> defaultThousandSeperator = new LinkedHashMap<>();
	protected LinkedHashMap<String, String> defaultDecimalSeperator = new LinkedHashMap<>();
	protected LinkedHashMap<String, LinkedHashMap<Double, String>> defaultSIPrefix = new LinkedHashMap<>();
	
	public IFHApi(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
		accountHandler = new AccountHandler(plugin);
		currencyHandler = new CurrencyHandler(plugin);
		transactionHandler = new TransactionHandler(plugin);
	}
	
	//If you want to know how the exchange with tax are calculated, copy this method and change the values.
	/*public static void main(String[] args)
	{
		boolean taxationBeforeExchange = false;
		boolean taxAreExclusive = false;
		double f = 0.5;
		double t = 1.5;
		/*
		 * 1:  true, true,   1.5, 0.5 +
		 * 2:  true, true,   0.5, 1.5 +
		 * 3:  true, true,   1.5, 1.5 +
		 * 4:  true, false,  1.5, 0.5 +
		 * 5:  true, false,  0.5, 1.5 +
		 * 6:  true, false,  1.5, 1.5 +
		 * 7:  false, true,  1.5, 0.5 +
		 * 8:  false, true,  0.5, 1.5 +
		 * 9:  false, true,  1.5, 1.5 +
		 * 10: false, false, 1.5, 0.5 +
		 * 11: false, false, 0.5, 1.5 +
		 * 12: false, false, 1.5, 1.5 +
		 */
		/*System.out.println("beforeExchangeTaxation : "+taxationBeforeExchange+" | true == bedeutet, Steuern wird vor der Umwandlung abgezogen");
		System.out.println();
		double amount = 300.0;
		
		if(taxationBeforeExchange)
		{
			
			double amountToWithdraw = 0.0;
			double amountToDeposit = 0.0;
			double taxInPercent = 0.1;
			if(taxAreExclusive)
			{
				amountToWithdraw = amount + amount*taxInPercent;
				amountToDeposit = amount;
			} else
			{
				amountToWithdraw = amount;
				amountToDeposit = amount - amount*taxInPercent;
			}
			double amountToTax = amountToWithdraw - amountToDeposit;
			
			System.out.println("Tax = "+taxInPercent*100+" % | real Wert : "+taxInPercent);
			System.out.println("taxAreExclusive : "+taxAreExclusive+" | == true bedeutet, werden die Steuern auf den toWithdraw draufgerechnet (exklusiv) oder nicht (inklusiv)");
			System.out.println("ToWithdraw = "+amountToWithdraw);
			System.out.println("ToDeposit = "+amountToDeposit);
			System.out.println("ToTax = "+amountToTax);
			System.out.println();
			System.out.println("Umwandlung beginnt...");
			System.out.println();
			
			double i = 0.0;
			System.out.println("fromAccount Wert = "+f);
			System.out.println("toAccount Wert = "+t);
			System.out.println();
			double a = amountToDeposit;
			System.out.println("amount = "+a);
			if(f > t)
			{
				
				System.out.println("fromAccount > toAccount : f ist mehr wert == amountToWithdraw < amountToDeposit");
				i = a * (Math.max(f, t)/Math.min(f, t));
				System.out.println(a+" * "+(Math.max(f, t)/Math.min(f, t))+" = "+i);
			} else if(f < t)
			{
				System.out.println("fromAccount < toAccount : f ist weniger wert == amountToWithdraw > amountToDeposit");
				i = a / (Math.max(f, t)/Math.min(f, t));
				System.out.println(a+" / "+(Math.max(f, t)/Math.min(f, t))+" = "+i);
			} else
			{
				System.out.println("fromAccount == toAccount : f ist gleich wert == amountToWithdraw = amountToDeposit");
				i = a;
				System.out.println(a+" = "+i);
			}
			System.out.println();
			System.out.println("amountToWithdraw nach Tax = "+amountToWithdraw+" | für fromAccount abziehen");
			System.out.println("amountToDeposit nach Tax = "+i+" | für toAccount draufrechnen");
		} else
		{
			double i = 0.0;
			System.out.println("fromAccount Wert = "+f);
			System.out.println("toAccount Wert = "+t);
			System.out.println();
			double a = amount;
			System.out.println("amount = "+a);
			if(f > t)
			{
				System.out.println("fromAccount > toAccount : f ist mehr wert == amountToWithdraw < amountToDeposit");
				i = a * (Math.max(f, t)/Math.min(f, t));
				System.out.println(a+" * "+(Math.max(f, t)/Math.min(f, t))+" = "+i);
			} else if(f < t)
			{
				System.out.println("fromAccount < toAccount : f ist weniger wert == amountToWithdraw > amountToDeposit");
				i = a / (Math.max(f, t)/Math.min(f, t));
				System.out.println(a+" / "+(Math.max(f, t)/Math.min(f, t))+" = "+i);
			} else
			{
				System.out.println("fromAccount == toAccount : f ist gleich wert == amountToWithdraw = amountToDeposit");
				i = a;
				System.out.println(a+" = "+i);
			}
			
			System.out.println();
			System.out.println("Umwandlung beginnt...");
			System.out.println();
			
			double amountToWithdraw = 0.0;
			double amountToDeposit = 0.0;
			double taxInPercent = 0.1;
			if(taxAreExclusive)
			{
				amountToWithdraw = a + a*taxInPercent;
				amountToDeposit = i;
			} else
			{
				amountToWithdraw = a;
				amountToDeposit = i - i*taxInPercent;
			}
			double amountToTax = i - amountToDeposit;
			
			System.out.println("Tax = "+taxInPercent*100+" % | real Wert : "+taxInPercent);
			System.out.println("taxAreExclusive : "+taxAreExclusive+" | == true bedeutet, werden die Steuern auf den toWithdraw draufgerechnet (exklusiv) oder nicht (inklusiv)");
			System.out.println("ToWithdraw = "+amountToWithdraw);
			System.out.println("ToDeposit = "+amountToDeposit);
			System.out.println("ToTax = "+amountToTax);
			
			System.out.println();
			System.out.println("amountToWithdraw nach Tax = "+amountToWithdraw+" | für fromAccount abziehen");
			System.out.println("amountToDeposit nach Tax = "+amountToDeposit+" | für toAccount draufrechnen");
		}
	}*/
	
	//Short Version
	/*public static void main(String[] args)
	{
		boolean taxationBeforeExchange = false;
		boolean taxAreExclusive = false;
		double f = 0.5;
		double t = 1.5;
		
		double amount = 300.0;
		
		if(taxationBeforeExchange)
		{
			double amountToWithdraw = 0.0;
			double amountToDeposit = 0.0;
			double taxInPercent = 0.1;
			if(taxAreExclusive)
			{
				amountToWithdraw = amount + amount*taxInPercent;
				amountToDeposit = amount;
			} else
			{
				amountToWithdraw = amount;
				amountToDeposit = amount - amount*taxInPercent;
			}
			double amountToTax = amountToWithdraw - amountToDeposit;
			
			double i = 0.0;
			if(f > t)
			{
				i = amountToDeposit * (Math.max(f, t)/Math.min(f, t));
			} else if(f < t)
			{
				i = amountToDeposit / (Math.max(f, t)/Math.min(f, t));
			} else
			{
				i = amountToDeposit;
			}
			System.out.println("amountToWithdraw nach Tax = "+amountToWithdraw+" | für fromAccount abziehen C1");
			System.out.println("amountToDeposit nach Tax = "+i+" | für toAccount draufrechnen C2");
			System.out.println("amountToTax = "+amountToTax+" | für TaxAccount in C1");
		} else
		{
			double i = 0.0;
			
			if(f > t)
			{
				i = amount * (Math.max(f, t)/Math.min(f, t));
			} else if(f < t)
			{
				i = amount / (Math.max(f, t)/Math.min(f, t));
			} else
			{
				i = amount;
			}
			
			double amountToWithdraw = 0.0;
			double amountToDeposit = 0.0;
			double taxInPercent = 0.1;
			if(taxAreExclusive)
			{
				amountToWithdraw = amount + amount*taxInPercent;
				amountToDeposit = i;
			} else
			{
				amountToWithdraw = amount;
				amountToDeposit = i - i*taxInPercent;
			}
			double amountToTax = i - amountToDeposit;
			System.out.println("amountToWithdraw nach Tax = "+amountToWithdraw+" | für fromAccount abziehen C1");
			System.out.println("amountToDeposit nach Tax = "+amountToDeposit+" | für toAccount draufrechnen C2");
			System.out.println("amountToTax = "+amountToTax+" | für TaxAccount in C2");
		}
	}*/
	
	public void saveAccount(Account... account)
	{
		for(Account a : account)
		{
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.ACCOUNT, a, "`id` = ?", a.getID());
		}
	}
	
	//INFO AccountHandling
	
	@Override
	public EconomyEntity getEntity(UUID uuid)
	{
		return accountHandler.getEntity(uuid);
	}

	@Override
	public EconomyEntity getEntity(String name)
	{
		return accountHandler.getEntity(name);
	}
	
	@Override
	public boolean existAccount(UUID uuid, String accountName)
	{
		return accountHandler.existAccount(uuid, accountName);
	}
	
	@Override
	public boolean existAccount(UUID uuid, String accountName, EconomyCurrency accountCurrency)
	{
		return accountHandler.existAccount(uuid, accountName, accountCurrency);
	}
	
	@Override
	public boolean existAccount(UUID uuid, EconomyCurrency accountCurrency, AccountType accountType, AccountCategory accountCategory,
			EconomyEntity.EconomyType entityType)
	{
		return accountHandler.existAccount(uuid, accountCurrency, accountType, accountCategory, entityType);
	}
	
	@Override
	public boolean existAccount(UUID uuid, String accountName, EconomyCurrency accountCurrency, AccountType accountType, AccountCategory accountCategory,
			EconomyEntity.EconomyType entityType)
	{
		return accountHandler.existAccount(uuid, accountName, accountCurrency, accountType, accountCategory, entityType);
	}
	
	@Override
	public boolean createAccount(Account account)
	{
		return accountHandler.createAccount(account);
	}
	
	@Override
	public boolean createAccount(String accountName, AccountType type, AccountCategory accountCategory, EconomyCurrency accountCurrency,
			EconomyEntity owner, double balance)
	{
		return accountHandler.createAccount(accountName, type, accountCategory, accountCurrency, owner, balance);
	}
	
	@Override
	public int deleteAccount(Account account)
	{
		return accountHandler.deleteAccount(account);
	}
	
	@Override
	public int deleteAllAccounts(EconomyCurrency accountCurrency)
	{
		return accountHandler.deleteAllAccounts(accountCurrency);
	}
	
	@Override
	public int deleteAllAccounts(UUID uuid)
	{
		return accountHandler.deleteAllAccounts(uuid);
	}
	
	@Override
	public int deleteAllAccounts(UUID uuid, AccountType accountType)
	{
		return accountHandler.deleteAllAccounts(uuid, accountType);
	}
	
	@Override
	public int deleteAllAccounts(UUID uuid, EconomyEntity.EconomyType entityType)
	{
		return accountHandler.deleteAllAccounts(uuid, entityType);
	}
	
	@Override
	public int deleteAllAccounts(UUID uuid, EconomyEntity.EconomyType entityType, AccountType accountType)
	{
		return accountHandler.deleteAllAccounts(uuid, entityType, accountType);
	}
	
	@Override
	public int deleteAllAccounts(AccountType accountType)
	{
		return accountHandler.deleteAllAccounts(accountType);
	}
	
	@Override
	public int deleteAllAccounts(AccountCategory accountCategory)
	{
		return accountHandler.deleteAllAccounts(accountCategory);
	}
	
	@Override
	public int deleteAllAccounts(EconomyEntity.EconomyType entityType)
	{
		return accountHandler.deleteAllAccounts(entityType);
	}
	
	@Override
	public int deleteAllAccounts(EconomyEntity.EconomyType entityType, AccountType accountType)
	{
		return accountHandler.deleteAllAccounts(entityType, accountType);
	}
	
	@Override
	public Account getAccount(int id)
	{
		return accountHandler.getAccount(id);
	}
	
	@Override
	public Account getAccount(EconomyEntity owner, String accountName)
	{
		if(owner == null || accountName == null)
		{
			return null;
		}
		return accountHandler.getAccount(owner.getUUID(), accountName);
	}
	
	@Override
	public Account getAccount(UUID ownerUUID, String accountName, EconomyEntity.EconomyType ownerEntityType, AccountType accountType, AccountCategory accountCategory,
			EconomyCurrency accountCurrency)
	{
		return accountHandler.getAccount(ownerUUID, accountName, ownerEntityType, accountType, accountCategory, accountCurrency);
	}
	
	@Override
	public Account getAccount(EconomyEntity owner, String accountName, AccountType accountType, AccountCategory accountCategory, EconomyCurrency accountCurrency)
	{
		return accountHandler.getAccount(owner, accountName, accountType, accountCategory, accountCurrency);
	}
	
	@Override
	public ArrayList<Account> getAccounts()
	{
		return accountHandler.getAccounts();
	}
	
	@Override
	public ArrayList<Account> getAccounts(AccountType accountType)
	{
		return accountHandler.getAccounts(accountType);
	}
	
	@Override
	public ArrayList<Account> getAccounts(EconomyEntity.EconomyType ownerType)
	{
		return accountHandler.getAccounts(ownerType);
	}
	
	@Override
	public ArrayList<Account> getAccounts(AccountCategory accountCategory)
	{
		return accountHandler.getAccounts(accountCategory);
	}
	
	@Override
	public ArrayList<Account> getAccounts(AccountType accountType, EconomyEntity.EconomyType ownerType)
	{
		return accountHandler.getAccounts(accountType, ownerType);
	}
	
	@Override
	public ArrayList<Account> getAccounts(AccountType accountType, EconomyEntity.EconomyType ownerType, AccountCategory accountCategory)
	{
		return accountHandler.getAccounts(accountType, ownerType, accountCategory);
	}
	
	@Override
	public Account getDefaultAccount(UUID ownerUUID)
	{
		return accountHandler.getDefaultAccount(ownerUUID);
	}
	
	@Override
	public Account getDefaultAccount(UUID ownerUUID, AccountCategory accountCategory)
	{
		return accountHandler.getDefaultAccount(ownerUUID, accountCategory);
	}
	
	@Override
	public Account getDefaultAccount(UUID ownerUUID, AccountCategory accountCategory, EconomyCurrency currency)
	{
		return accountHandler.getDefaultAccount(ownerUUID, accountCategory, currency);
	}
	
	@Override
	public void setDefaultAccount(UUID ownerUUID, Account account, AccountCategory accountCategory)
	{
		accountHandler.setDefaultAccount(ownerUUID, account, accountCategory);
	}
	
	@Override
	public boolean addManagementTypeToAccount(Account account, UUID uuid, AccountManagementType acountManagementType)
	{
		return accountHandler.addManagementTypeToAccount(account, uuid, acountManagementType);
	}
	
	@Override
	public boolean removeManagementTypeToAccount(Account account, UUID uuid, AccountManagementType acountManagementType)
	{
		return accountHandler.removeManagementTypeToAccount(account, uuid, acountManagementType);
	}
	
	@Override
	public boolean canManageAccount(Account account, UUID uuid, AccountManagementType accountManagementType)
	{
		return accountHandler.canManageAccount(account, uuid, accountManagementType);
	}
	
	@Override
	public boolean canManageAccount(int accountID, UUID uuid, AccountManagementType accountManagementType)
	{
		return accountHandler.canManageAccount(accountID, uuid, accountManagementType);
	}
	
	//INFO currencyHandling
	
	@Override
	public boolean registerCurrency(Currency currency)
	{
		return currencyHandler.registerCurrency(currency);
	}
	
	public void registerCurrencyFromFile(YamlConfiguration c)
	{
		currencyHandler.registerCurrencyFromFile(c);
	}
	
	@Override
	public boolean registerCurrency(Currency currency, int gradationQuantity, boolean useSIPrefix, int decimalPlaces, boolean useSymbol,
			String thousandSeperator, String decimalSeperator, LinkedHashMap<Double, String> siPrefix)
	{
		return currencyHandler.registerCurrency(currency, gradationQuantity, useSIPrefix, decimalPlaces, useSymbol, thousandSeperator, decimalSeperator, siPrefix);
	}

	@Override
	public boolean existsCurrency(String uniqueName)
	{
		return currencyHandler.existsCurrency(uniqueName);
	}

	@Override
	public EconomyCurrency getCurrency(String uniqueName)
	{
		return currencyHandler.getCurrency(uniqueName);
	}

	@Override
	public EconomyCurrency getDefaultCurrency(CurrencyType type)
	{
		return currencyHandler.getDefaultCurrency(type);
	}

	@Override
	public ArrayList<EconomyCurrency> getCurrencies(CurrencyType type)
	{
		return currencyHandler.getCurrencies(type);
	}

	@Override
	public ArrayList<EconomyCurrency> getExchangableCurrencies(CurrencyType type)
	{
		return currencyHandler.getExchangableCurrencies(type);
	}

	@Override
	public double getTotalMoneyInTheSystem(EconomyCurrency currency)
	{
		return currencyHandler.getTotalMoneyInTheSystem(currency);
	}

	@Override
	public double getTotalMoneyInTheSystem(EconomyCurrency currency, AccountType type)
	{
		return currencyHandler.getTotalMoneyInTheSystem(currency, type);
	}

	@Override
	public double getTotalMoneyInTheSystem(EconomyCurrency currency, EconomyType entityType)
	{
		return currencyHandler.getTotalMoneyInTheSystem(currency, entityType);
	}

	@Override
	public double getTotalMoneyInTheSystem(EconomyCurrency currency, AccountType type, EconomyType entityType)
	{
		return currencyHandler.getTotalMoneyInTheSystem(currency, type, entityType);
	}

	@Override
	public boolean setDefaultCurrency(EconomyCurrency currency)
	{
		return currencyHandler.setDefaultCurrency(currency);
	}
	
	//INFO Transaction Handling

	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount)
	{
		return transactionHandler.transaction(withdraw, deposit, amount);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.transaction(withdraw, deposit, amount, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
	{
		return transactionHandler.transaction(withdraw, deposit, amount, taxInPercent, taxAreExclusive, taxDepot);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.transaction(withdraw, deposit, amount, taxInPercent, taxAreExclusive, taxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}

	@Override
	public EconomyAction deposit(Account holder, double amount)
	{
		return transactionHandler.deposit(holder, amount);
	}
	
	@Override
	public EconomyAction deposit(Account holder, double amount, OrdererType type, String ordererUUIDOrPlugin,
			String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.deposit(holder, amount, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}
	
	@Override
	public EconomyAction deposit(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
	{
		return transactionHandler.deposit(holder, amount, taxInPercent, taxAreExclusive, taxDepot);
	}
	
	@Override
	public EconomyAction deposit(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.deposit(holder, amount, taxInPercent, taxAreExclusive, taxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount)
	{
		return transactionHandler.withdraw(holder, amount);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, 
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.withdraw(holder, amount, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
	{
		return transactionHandler.withdraw(holder, amount, taxInPercent, taxAreExclusive, taxDepot);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.withdraw(holder, amount, taxInPercent, taxAreExclusive, taxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, LinkedHashMap<ItemStack, Double> possibleItemsWithRelatedValue,
			double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.withdraw(holder, amount, possibleItemsWithRelatedValue, taxInPercent, taxAreExclusive, taxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}
	
	
	@Override
	public EconomyAction exchangeCurrencies(Account withdrawAccount, Account depositAccount, double amountFromWithdrawAccount)
	{
		return transactionHandler.exchangeCurrencies(withdrawAccount, depositAccount, amountFromWithdrawAccount);
	}
	
	@Override
	public EconomyAction exchangeCurrencies(Account withdrawAccount, Account depositAccount, double amountFromWithdrawAccount,  
			OrdererType type, String ordererUUIDOrPlugin,
			String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.exchangeCurrencies(withdrawAccount, depositAccount, amountFromWithdrawAccount, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}
	
	@Override
	public EconomyAction exchangeCurrencies(Account withdrawAccount, Account depositAccount, double amountFromWithdrawAccount,
			double taxInPercent, boolean taxAreExclusive, Account withdrawAccounttaxDepot, Account depositAccounttaxDepot)
	{
		return transactionHandler.exchangeCurrencies(withdrawAccount, depositAccount, amountFromWithdrawAccount, taxInPercent, taxAreExclusive, withdrawAccounttaxDepot, depositAccounttaxDepot);
	}
	
	@Override
	public EconomyAction exchangeCurrencies(Account withdrawAccount, Account depositAccount, double amountFromWithdrawAccount,
			double taxInPercent, boolean taxAreExclusive, Account withdrawAccounttaxDepot, Account depositAccounttaxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.exchangeCurrencies(withdrawAccount, depositAccount, amountFromWithdrawAccount, taxInPercent, taxAreExclusive, withdrawAccounttaxDepot, depositAccounttaxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}
	
	// INFO economy

	@Override
	public String format(double amount, EconomyCurrency economyCurrency)
	{
		if(economyCurrency == null)
		{
			return null;
		}
		return format(amount, economyCurrency,
				getDefaultGradationQuantity(economyCurrency),
				getDefaultDecimalPlaces(economyCurrency),
				getDefaultUseSIPrefix(economyCurrency),
				getDefaultUseSymbol(economyCurrency),
				getDefaultThousandSeperator(economyCurrency),
				getDefaultDecimalSeperator(economyCurrency));
	}

	@Override
	public String format(double amount, @Nonnull EconomyCurrency economyCurrency, int gradationQuantity, int decimalPlaces, 
			boolean useSIPrefix, boolean useSymbol)
	{
		if(economyCurrency == null)
		{
			return null;
		}
		return format(amount, economyCurrency,
				gradationQuantity,
				decimalPlaces,
				useSIPrefix,
				useSymbol,
				getDefaultThousandSeperator(economyCurrency),
				getDefaultDecimalSeperator(economyCurrency));
	}

	@Override
	public String format(double amount, @Nonnull EconomyCurrency ec, int gradationQuantity, int decimalPlaces, 
			boolean useSIPrefix, boolean useSymbol, String thousandSeperator, String decimalSeperator)
	{
		if(ec == null)
		{
			return null;
		}
		String ts = thousandSeperator;
		String ds = decimalSeperator;
		if(ts == null)
		{
			ts = getDefaultThousandSeperator(ec);
		}
		if(ds == null)
		{
			ds = getDefaultDecimalSeperator(ec);
		}
		StringBuilder sb = new StringBuilder();
		if(gradationQuantity == 0)
		{
			Gradation gr = ec.getCurrencyGradation().getHighestGradation();
			BigDecimal divisor = new BigDecimal(gr.getValueToBaseGradation());
			BigDecimal result = new BigDecimal(amount).divide(divisor);
			String si = null;
			if(useSIPrefix && defaultSIPrefix.containsKey(ec.getUniqueName()))
			{
				for(Entry<Double, String> e : defaultSIPrefix.get(ec.getUniqueName()).entrySet())
				{
					BigDecimal step1 = result.divide(new BigDecimal(e.getKey()));
					if(step1.doubleValue() > 1.0)
					{
						si = e.getValue();
						result = step1;
						break;
					}
				}
				result = result.setScale(decimalPlaces);
			}
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

			symbols.setGroupingSeparator(thousandSeperator.charAt(0));
			symbols.setDecimalSeparator(decimalSeperator.charAt(0));
			formatter.setDecimalFormatSymbols(symbols);
			sb.append(formatter.format(result.doubleValue()));
			if(si != null)
			{
				sb.append(si+" ");
			} else
			{
				sb.append(" ");
			}
			sb.append(useSymbol ? gr.getSymbol() : gr.getPlural());
			return sb.toString();
		}
		int highest = 0;
		int lowest = 0;
		if(gradationQuantity > 0)
		{
			highest = ec.getCurrencyGradation().getHighestGradationNumber();
			lowest = highest-gradationQuantity;
			if(lowest < 0) lowest = 0;
		} else if(gradationQuantity < 0)
		{
			highest = ec.getCurrencyGradation().getHighestGradationNumber()+gradationQuantity;
			lowest = 0;
			if(highest < 0) highest = 0;
		}
		BigDecimal result = new BigDecimal(amount);
		while(highest >= lowest)
		{
			Gradation gr = ec.getCurrencyGradation().getGradation(highest);
			BigDecimal divisor = new BigDecimal(gr.getValueToBaseGradation());
			BigDecimal resultWhile = result.divide(divisor);
			if(highest == lowest)
			{
				String si = null;
				if(useSIPrefix && defaultSIPrefix.containsKey(ec.getUniqueName()))
				{
					for(Entry<Double, String> e : defaultSIPrefix.get(ec.getUniqueName()).entrySet())
					{
						BigDecimal step1 = resultWhile.divide(new BigDecimal(e.getKey()));
						if(step1.doubleValue() > 1.0)
						{
							si = e.getValue();
							resultWhile = step1;
							break;
						}
					}
					resultWhile = resultWhile.setScale(decimalPlaces);
				}
				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

				symbols.setGroupingSeparator(thousandSeperator.charAt(0));
				symbols.setDecimalSeparator(decimalSeperator.charAt(0));
				formatter.setDecimalFormatSymbols(symbols);
				sb.append(formatter.format(resultWhile.doubleValue()));
				if(si != null)
				{
					sb.append(si+" ");
				} else
				{
					sb.append(" ");
				}
				sb.append(useSymbol ? gr.getSymbol() : gr.getPlural());
			} else
			{
				BigDecimal resultForSI = resultWhile;
				String si = null;
				if(useSIPrefix && defaultSIPrefix.containsKey(ec.getUniqueName()))
				{
					for(Entry<Double, String> e : defaultSIPrefix.get(ec.getUniqueName()).entrySet())
					{
						BigDecimal step1 = resultWhile.divide(new BigDecimal(e.getKey()));
						if(step1.doubleValue() > 1.0)
						{
							si = e.getValue();
							resultWhile = step1;
							break;
						}
					}
					resultWhile = resultWhile.setScale(0);
				}
				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

				symbols.setGroupingSeparator(thousandSeperator.charAt(0));
				symbols.setDecimalSeparator(decimalSeperator.charAt(0));
				formatter.setDecimalFormatSymbols(symbols);
				sb.append(formatter.format(resultWhile.doubleValue()));
				if(si != null)
				{
					sb.append(si+" ");
				} else
				{
					sb.append(" ");
				}
				sb.append(useSymbol ? gr.getSymbol() : gr.getPlural());
				sb.append(" ");
				result = result.subtract(resultForSI.setScale(0).multiply(divisor));
			}
			highest--;
		}
		return sb.toString();
	}

	@Override
	public String[] getAuthors()
	{
		return (String[]) plugin.getDescription().getAuthors().toArray();
	}

	@Override
	public int getDefaultDecimalPlaces(EconomyCurrency currency)
	{
		return defaultDecimalPlaces.containsKey(currency.getUniqueName()) ? defaultDecimalPlaces.get(currency.getUniqueName()) : 0;
	}

	@Override
	public String getDefaultDecimalSeperator(EconomyCurrency currency)
	{
		return defaultDecimalSeperator.containsKey(currency.getUniqueName()) ? defaultDecimalSeperator.get(currency.getUniqueName()) : ".";
	}

	@Override
	public int getDefaultGradationQuantity(EconomyCurrency currency)
	{
		return defaultGradationQuantity.containsKey(currency.getUniqueName()) ? defaultGradationQuantity.get(currency.getUniqueName()) : 0;
	}

	@Override
	public String getDefaultThousandSeperator(EconomyCurrency currency)
	{
		return defaultThousandSeperator.containsKey(currency.getUniqueName()) ? defaultThousandSeperator.get(currency.getUniqueName()) : ",";
	}

	@Override
	public boolean getDefaultUseSIPrefix(EconomyCurrency currency)
	{
		return defaultUseSIPrefix.containsKey(currency.getUniqueName()) ? defaultUseSIPrefix.get(currency.getUniqueName()) : false;
	}

	@Override
	public boolean getDefaultUseSymbol(EconomyCurrency currency)
	{
		return defaultUseSymbol.containsKey(currency.getUniqueName()) ? defaultUseSymbol.get(currency.getUniqueName()) : false;
	}

	@Override
	public String getName()
	{
		return plugin.pluginName;
	}

	@Override
	public boolean hasBankSupport()
	{
		return true;
	}

	@Override
	public boolean hasItemStackCurrencySupport()
	{
		//TODO
		return false;
	}

	@Override
	public boolean hasPlayerExperienceCurrencySupport()
	{
		//TODO
		return false;
	}

	@Override
	public boolean hasWalletSupport()
	{
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public boolean isMultiCurrency()
	{
		return true;
	}
}
