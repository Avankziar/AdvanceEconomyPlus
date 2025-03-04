package me.avankziar.aep.spigot.api.economy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.scheduler.BukkitRunnable;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AccountSpigot;
import me.avankziar.aep.general.objects.QuickPayAccount;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.handler.ConfigHandler;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.Economy;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.economy.currency.Currency;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import me.avankziar.ifh.spigot.economy.currency.Gradation;

public class IFHEcoProvider implements Economy
{
	private AEP plugin;
	protected AccountHandler accountHandler;
	protected CurrencyHandler currencyHandler;
	private TransactionHandler transactionHandler;
	
	protected LinkedHashMap<String, Integer> defaultGradationQuantity = new LinkedHashMap<>();
	protected LinkedHashMap<String, Boolean> defaultUseSIPrefix = new LinkedHashMap<>();
	protected LinkedHashMap<String, Integer> defaultDecimalPlaces = new LinkedHashMap<>();
	protected LinkedHashMap<String, Boolean> defaultUseSymbol = new LinkedHashMap<>();
	protected LinkedHashMap<String, String> defaultThousandSeperator = new LinkedHashMap<>();
	protected LinkedHashMap<String, String> defaultDecimalSeperator = new LinkedHashMap<>();
	protected LinkedHashMap<String, RoundingMode> defaultRoundingMode = new LinkedHashMap<>();
	protected LinkedHashMap<String, LinkedHashMap<Double, String>> defaultSIPrefix = new LinkedHashMap<>();
	
	private static String difhapi1 = "ifhapiFormat";
	
	public IFHEcoProvider(AEP plugin)
	{
		this.plugin = plugin;
		accountHandler = new AccountHandler(plugin);
		currencyHandler = new CurrencyHandler(plugin);
		transactionHandler = new TransactionHandler(plugin);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				transactionHandler.init();
			}
		}.runTaskLater(plugin, 20*5);
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
			plugin.getMysqlHandler().updateData(MysqlType.ACCOUNT_SPIGOT, new AccountSpigot(a), "`id` = ?", a.getID());
		}
	}
	
	//INFO AccountHandling
	
	public int getQuickPayAccount(EconomyCurrency economyCurrency, UUID uuid)
	{
		QuickPayAccount qpa = (QuickPayAccount) plugin.getMysqlHandler().getData(MysqlType.QUICKPAYACCOUNT, "`account_currency` = ? AND `player_uuid` = ?",
				economyCurrency.getUniqueName(), uuid.toString());
		return qpa != null ? qpa.getAccountID() : -1;
	}
	
	
	
	@Override
	public EconomyEntity getEntity(UUID uuid)
	{
		return accountHandler.getEntity(uuid);
	}
	
	@Override
	public EconomyEntity getEntity(UUID uuid, EconomyEntity.EconomyType type)
	{
		return accountHandler.getEntity(uuid, type);
	}

	@Override
	public EconomyEntity getEntity(String name)
	{
		return accountHandler.getEntity(name);
	}
	
	@Override
	public EconomyEntity getEntity(String name, EconomyEntity.EconomyType type)
	{
		return accountHandler.getEntity(name, type);
	}
	
	@Override
	public ArrayList<EconomyEntity> getEntitys(EconomyEntity.EconomyType type)
	{
		return accountHandler.getEntitys(type);
	}
	
	@Override
	public EconomyEntity getDefaultServer()
	{
		return accountHandler.getDefaultServer();
	}
	
	@Override
	public EconomyEntity getDefaultEntity() 
	{
		return accountHandler.getDefaultEntity();
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
	public ArrayList<Account> getAccounts(EconomyEntity owner)
	{
		return accountHandler.getAccounts(owner);
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
	public boolean removeManagementTypeFromAccount(Account account, UUID uuid, AccountManagementType acountManagementType)
	{
		return accountHandler.removeManagementTypeFromAccount(account, uuid, acountManagementType);
	}
	
	@Override
	public boolean removeManagementTypeFromAccount(int accountID)
	{
		return accountHandler.removeManagementTypeFromAccount(accountID);
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
		return registerCurrency(currency, 0, false, 0, false, ",", ".", new LinkedHashMap<>());
	}
	
	public void registerCurrencyFromFile()
	{
		for(String s : plugin.getYamlHandler().getCurrency().keySet())
		{
			registerCurrencyFromFile(plugin.getYamlHandler().getCurrency(s));
		}
	}
	
	public void registerCurrencyFromFile(YamlDocument c)
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
		return transactionHandler.transaction(withdraw, deposit, amount, false);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount, boolean withdrawCanGoNegativ)
	{
		return transactionHandler.transaction(withdraw, deposit, amount, withdrawCanGoNegativ);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.transaction(withdraw, deposit, amount, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment, false);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment, boolean withdrawCanGoNegativ)
	{
		return transactionHandler.transaction(withdraw, deposit, amount,
				type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment, withdrawCanGoNegativ);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
	{
		return transactionHandler.transaction(withdraw, deposit, amount, taxInPercent, taxAreExclusive, taxDepot, false);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount,
			double taxInPercent, boolean taxAreExclusive, Account taxDepot, boolean withdrawCanGoNegativ)
	{
		return transactionHandler.transaction(withdraw, deposit, amount, taxInPercent, taxAreExclusive, taxDepot, withdrawCanGoNegativ);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.transaction(withdraw, deposit, amount,
				taxInPercent, taxAreExclusive, taxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment, false);
	}
	
	@Override
	public EconomyAction transaction(Account withdraw, Account deposit, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment, boolean withdrawCanGoNegativ)
	{
		return transactionHandler.transaction(withdraw, deposit, amount,
				taxInPercent, taxAreExclusive, taxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment, withdrawCanGoNegativ);
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
		return transactionHandler.withdraw(holder, amount, false);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, boolean withdrawCanGoNegativ)
	{
		return transactionHandler.withdraw(holder, amount, withdrawCanGoNegativ);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, 
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.withdraw(holder, amount, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment, false);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, 
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment, boolean withdrawCanGoNegativ)
	{
		return transactionHandler.withdraw(holder, amount, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment, withdrawCanGoNegativ);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
	{
		return transactionHandler.withdraw(holder, amount, taxInPercent, taxAreExclusive, taxDepot, false);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount,
			double taxInPercent, boolean taxAreExclusive, Account taxDepot, boolean withdrawCanGoNegativ)
	{
		return transactionHandler.withdraw(holder, amount, taxInPercent, taxAreExclusive, taxDepot, withdrawCanGoNegativ);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.withdraw(holder, amount,
				taxInPercent, taxAreExclusive, taxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment, false);
	}
	
	@Override
	public EconomyAction withdraw(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment, boolean withdrawCanGoNegativ)
	{
		return transactionHandler.withdraw(holder, amount,
				taxInPercent, taxAreExclusive, taxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment, withdrawCanGoNegativ);
	}
	
	/*@Override
	public EconomyAction withdraw(Account holder, double amount, LinkedHashMap<ItemStack, Double> possibleItemsWithRelatedValue,
			double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		return transactionHandler.withdraw(holder, amount, possibleItemsWithRelatedValue, taxInPercent, taxAreExclusive, taxDepot, type, ordererUUIDOrPlugin, actionLogCategory, actionLogComment);
	}*/
	
	
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
				getDefaultDecimalSeperator(economyCurrency),
				getDefaultRoundingMode(economyCurrency));
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
				getDefaultDecimalSeperator(economyCurrency),
				getDefaultRoundingMode(economyCurrency));
	}

	public String format(double amount, @Nonnull EconomyCurrency ec, int gradationQuantity, int decimalPlaces, 
			boolean useSIPrefix, boolean useSymbol, String thousandSeperator, String decimalSeperator)
	{
		if(ec == null)
		{
			return null;
		}
		return format(amount, ec,
				gradationQuantity,
				decimalPlaces,
				useSIPrefix,
				useSymbol,
				thousandSeperator,
				decimalSeperator,
				getDefaultRoundingMode(ec));
	}
	
	public String format(double amount, @Nonnull EconomyCurrency ec, int gradationQuantity, int decimalPlaces, 
			boolean useSIPrefix, boolean useSymbol, String thousandSeperator, String decimalSeperator, RoundingMode roundingMode)
	{
		ConfigHandler.debug(difhapi1, "> Format Begin");
		if(ec == null)
		{
			ConfigHandler.debug(difhapi1, "> Format: ec == null");
			return null;
		}
		ConfigHandler.debug(difhapi1, "> Format Info: gQ = "+gradationQuantity
												+" | dP = "+decimalPlaces
												+" | useSIP: "+useSIPrefix
												+" | useS: "+useSymbol
												+" | ts: "+thousandSeperator
												+" | ds: "+decimalSeperator);
		String ts = thousandSeperator;
		String ds = decimalSeperator;
		if(ts == null)
		{
			ts = getDefaultThousandSeperator(ec);
			ConfigHandler.debug(difhapi1, "> Format: ts == null >> ts = "+ts+" (Default)");
		}
		if(ds == null)
		{
			ds = getDefaultDecimalSeperator(ec);
			ConfigHandler.debug(difhapi1, "> Format: ds == null >> ds = "+ds+" (Default)");
		}
		StringBuilder sb = new StringBuilder();
		if(gradationQuantity == 0)
		{
			ConfigHandler.debug(difhapi1, "> Format: gQ == 0");
			Gradation gr = ec.getCurrencyGradation().getHighestGradation();
			ConfigHandler.debug(difhapi1, "> Format: gradation(highest) : "
					+gr.getSingular()+", "+gr.getPlural()+", "+gr.getSymbol()+", "+gr.getValueToBaseGradation());
			BigDecimal divisor = new BigDecimal(gr.getValueToBaseGradation());
			BigDecimal result = new BigDecimal(amount);
			if(result.doubleValue() != 0.0 && divisor.doubleValue() != 0.0)
			{
				result = new BigDecimal(amount).divide(divisor, 10, RoundingMode.HALF_EVEN);
			}
			ConfigHandler.debug(difhapi1, "> Format: %a%/%d% = %res%"
					.replace("%a%", String.valueOf(amount))
					.replace("%d%", String.valueOf(divisor.doubleValue()))
					.replace("%res%", String.valueOf(result.doubleValue())));
			String si = null;
			if(useSIPrefix && defaultSIPrefix.containsKey(ec.getUniqueName()))
			{
				ConfigHandler.debug(difhapi1, "> Format: useSIPrefix");
				for(Entry<Double, String> e : defaultSIPrefix.get(ec.getUniqueName()).entrySet())
				{
					BigDecimal step1 = result.divide(new BigDecimal(e.getKey()), 10, roundingMode);
					ConfigHandler.debug(difhapi1, "> Format: step : %s% = %r%/%k%"
							.replace("%s%", String.valueOf(step1.doubleValue()))
							.replace("%r%", String.valueOf(result.doubleValue()))
							.replace("%k%", String.valueOf(new BigDecimal(e.getKey()).doubleValue())));
					if(amount >= 0)
					{
						if(step1.doubleValue() >= 1.0)
						{
							si = e.getValue();
							result = step1;
							break;
						}
					} else
					{
						if(step1.doubleValue() <= -1.0)
						{
							si = e.getValue();
							result = step1;
							break;
						}
					}
				}
			}
			ConfigHandler.debug(difhapi1, "> Format: res = "+result.doubleValue());
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

			symbols.setGroupingSeparator(thousandSeperator.charAt(0));
			symbols.setDecimalSeparator(decimalSeperator.charAt(0));
			formatter.setMaximumFractionDigits(decimalPlaces);
			formatter.setMinimumFractionDigits(decimalPlaces);
			formatter.setDecimalFormatSymbols(symbols);
			String format = plugin.getYamlHandler().getCurrency(ec.getUniqueName()).getString("Format.OutputFormat")
					.replace("%number%", formatter.format(result))
					.replace("%siprefix%", si != null ? si : "")
					.replace("%gradation%", useSymbol ? gr.getSymbol() : 
						((result.doubleValue() >= 1 && result.doubleValue() < 2) ? gr.getSingular() : gr.getPlural()));
			sb.append(format.trim());
			ConfigHandler.debug(difhapi1, "> Format: End = "+sb.toString());
			return sb.toString();
		}
		int highest = 0;
		int lowest = 0;
		if(gradationQuantity > 0)
		{
			ConfigHandler.debug(difhapi1, "> Format: gQ > 0");
			highest = ec.getCurrencyGradation().getHighestGradationNumber();
			lowest = highest-gradationQuantity;
			if(lowest < 0) lowest = 0;
		} else if(gradationQuantity < 0)
		{
			ConfigHandler.debug(difhapi1, "> Format: gQ < 0");
			highest = ec.getCurrencyGradation().getHighestGradationNumber()+gradationQuantity;
			lowest = 0;
			if(highest < 0) highest = 0;
		}
		ConfigHandler.debug(difhapi1, "> Format: highest = %h% | lowest = %l%"
				.replace("%h%", String.valueOf(highest))
				.replace("%l%", String.valueOf(lowest)));
		BigDecimal result = new BigDecimal(amount);
		ConfigHandler.debug(difhapi1, "> Format: amount = "+result.doubleValue());
		while(highest >= lowest)
		{
			ConfigHandler.debug(difhapi1, "> Format: while("+highest+" >= "+lowest+")");
			Gradation gr = ec.getCurrencyGradation().getGradation(highest);
			ConfigHandler.debug(difhapi1, "> Format: gradation("+highest+") : "
											+gr.getSingular()+", "+gr.getPlural()+", "+gr.getSymbol()+", "+gr.getValueToBaseGradation());
			BigDecimal divisor = new BigDecimal(gr.getValueToBaseGradation());
			BigDecimal resultWhile = result;
			if(result.doubleValue() != 0.0 && divisor.doubleValue() != 0.0)
			{
				resultWhile = result.divide(divisor);
			}
			ConfigHandler.debug(difhapi1, "> Format: %a%/%d% = %res%"
					.replace("%a%", String.valueOf(result.doubleValue()))
					.replace("%d%", String.valueOf(divisor.doubleValue()))
					.replace("%res%", String.valueOf(resultWhile.doubleValue())));
			if(highest == lowest)
			{
				ConfigHandler.debug(difhapi1, "> Format: highest == lowest");
				String si = null;
				if(useSIPrefix && defaultSIPrefix.containsKey(ec.getUniqueName())
						//&& gradationQuantity == highest
						)
				{
					for(Entry<Double, String> e : defaultSIPrefix.get(ec.getUniqueName()).entrySet())
					{
						BigDecimal step1 = resultWhile;
						if(resultWhile.doubleValue() != 0.0 && new BigDecimal(e.getKey()).doubleValue() != 0.0)
						{
							step1 = resultWhile.divide(new BigDecimal(e.getKey()), 10, roundingMode);
						}
						ConfigHandler.debug(difhapi1, "> Format: step : %s% = %r%/%k%"
								.replace("%s%", String.valueOf(step1.doubleValue()))
								.replace("%r%", String.valueOf(resultWhile.doubleValue()))
								.replace("%k%", String.valueOf(new BigDecimal(e.getKey()).doubleValue())));
						if(amount >= 0)
						{
							if(step1.doubleValue() >= 1.0)
							{
								si = e.getValue();
								resultWhile = step1;
								break;
							}
						} else
						{
							if(step1.doubleValue() <= -1.0)
							{
								si = e.getValue();
								resultWhile = step1;
								break;
							}
						}
					}
				}
				ConfigHandler.debug(difhapi1, "> Format: res = "+resultWhile.doubleValue());
				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

				symbols.setGroupingSeparator(thousandSeperator.charAt(0));
				symbols.setDecimalSeparator(decimalSeperator.charAt(0));
				formatter.setMaximumFractionDigits(decimalPlaces);
				formatter.setMinimumFractionDigits(decimalPlaces);
				formatter.setDecimalFormatSymbols(symbols);
				String format = plugin.getYamlHandler().getCurrency(ec.getUniqueName()).getString("Format.OutputFormat")
						.replace("%number%", formatter.format(resultWhile))
						.replace("%siprefix%", si != null ? si : "")
						.replace("%gradation%", useSymbol ? gr.getSymbol() : 
							((resultWhile.doubleValue() >= 1 && resultWhile.doubleValue() < 2) ? gr.getSingular() : gr.getPlural()));
				sb.append(format.trim());
				ConfigHandler.debug(difhapi1, "> Format: End = "+sb.toString());
				return sb.toString();
			} else
			{
				BigDecimal resultForSI = new BigDecimal(0.0);
				String si = null;
				if(useSIPrefix && defaultSIPrefix.containsKey(ec.getUniqueName())
						//&& gradationQuantity == highest
						)
				{
					for(Entry<Double, String> e : defaultSIPrefix.get(ec.getUniqueName()).entrySet())
					{
						BigDecimal val = new BigDecimal(e.getKey());
						BigDecimal step1 = resultWhile;
						if(resultWhile.doubleValue() != 0.0 && val.doubleValue() != 0.0)
						{
							step1 = resultWhile.divide(val, 10, roundingMode);
						}
						ConfigHandler.debug(difhapi1, "> Format: step : %s% = %r%/%k%"
								.replace("%s%", String.valueOf(step1.doubleValue()))
								.replace("%r%", String.valueOf(resultWhile.doubleValue()))
								.replace("%k%", String.valueOf(val.doubleValue())));
						if(amount >= 0 && step1.doubleValue() >= 1.0)
						{
							si = e.getValue();
							//cancel decimal number
							resultWhile = new BigDecimal(step1.toBigInteger());
							
							/*
							 * step1 = 5,9xxx
							 * resultwhile = 5,0
							 */
							resultForSI = step1.subtract(resultWhile);
							
							//Multiply to orignal value dimension
							resultForSI = resultForSI.multiply(val).multiply(new BigDecimal(gr.getValueToBaseGradation()));
							
							ConfigHandler.debug(difhapi1, "> Format: resultForSI : %si% = (%s%*%v%)-(%rw%*%v%)"
									.replace("%si%", String.valueOf(resultForSI.doubleValue()))
									.replace("%s%", String.valueOf(step1.doubleValue()))
									.replace("%v%", String.valueOf(val.doubleValue()))
									.replace("%gr%", String.valueOf(gr.getValueToBaseGradation()))
									.replace("%rw%", String.valueOf(resultWhile.doubleValue())));
							break;
						} else if(amount < 0 && step1.doubleValue() <= -1.0)
						{
							si = e.getValue();
							//cancel decimal number
							resultWhile = new BigDecimal(step1.toBigInteger());
							
							/*
							 * step1 = 5,9xxx
							 * resultwhile = 5,0
							 */
							resultForSI = step1.subtract(resultWhile);
							
							//Multiply to orignal value dimension
							resultForSI = resultForSI.multiply(val).multiply(new BigDecimal(gr.getValueToBaseGradation()));
							ConfigHandler.debug(difhapi1, "> Format: resultForSI : %si% = (%s%*%v%)-(%rw%*%v%)"
									.replace("%si%", String.valueOf(resultForSI.doubleValue()))
									.replace("%s%", String.valueOf(step1.doubleValue()))
									.replace("%v%", String.valueOf(val.doubleValue()))
									.replace("%gr%", String.valueOf(gr.getValueToBaseGradation()))
									.replace("%rw%", String.valueOf(resultWhile.doubleValue())));
							break;
						}
					}
					result = resultForSI;
				} else
				{
					BigDecimal val = new BigDecimal(gr.getValueToBaseGradation());
					result = resultWhile.multiply(val)
							.subtract(new BigDecimal(resultWhile.toBigInteger()).multiply(val));
					ConfigHandler.debug(difhapi1, "> Format: resultForSI : %s% = (%rw%*%gr%)-(%rwi%*%gr%)"
							.replace("%s%", String.valueOf(result.doubleValue()))
							.replace("%gr%", String.valueOf(gr.getValueToBaseGradation()))
							.replace("%rw%", String.valueOf(resultWhile.doubleValue()))
							.replace("%rwi%", String.valueOf(new BigDecimal(resultWhile.toBigInteger()).doubleValue())));
				}
				ConfigHandler.debug(difhapi1, "> Format: res = "+resultWhile.doubleValue());
				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
				formatter.setMaximumFractionDigits(0);
				formatter.setMinimumFractionDigits(0);
				symbols.setGroupingSeparator(thousandSeperator.charAt(0));
				symbols.setDecimalSeparator(decimalSeperator.charAt(0));
				
				formatter.setDecimalFormatSymbols(symbols);
				YamlDocument yd = plugin.getYamlHandler().getCurrency(ec.getUniqueName());
				String format = yd.getString("Format.OutputFormat")
						.replace("%number%", formatter.format(resultWhile))
						.replace("%siprefix%", si != null ? si : "")
						.replace("%gradation%", useSymbol ? gr.getSymbol() : 
							((resultWhile.doubleValue() >= 1 && resultWhile.doubleValue() < 2) ? gr.getSingular() : gr.getPlural()));
				sb.append(format);
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
	public RoundingMode getDefaultRoundingMode(EconomyCurrency currency)
	{
		return defaultRoundingMode.containsKey(currency.getUniqueName()) ? defaultRoundingMode.get(currency.getUniqueName()) : RoundingMode.HALF_EVEN;
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
	
	public String getBoolean(boolean boo)
	{
		return plugin.getYamlHandler().getLang().getString("Boolean.Replacer."+String.valueOf(boo));
	}
	
	public String getEconomyEntityType(EconomyEntity.EconomyType eeet)
	{
		if(plugin.getYamlHandler().getLang().get("EconomyEntityEconomyType.Replacer."+eeet.toString()) != null)
		{
			String acts = plugin.getYamlHandler().getLang().getString("EconomyEntityEconomyType.Replacer."+eeet.toString());
			if(acts != null)
			{ 
				return acts;
			}
		}
		return EconomyEntity.EconomyType.PLAYER.toString();
	}
	
	public EconomyEntity.EconomyType getEconomyEntityType(String s)
	{
		List<EconomyEntity.EconomyType> eeetl = new ArrayList<EconomyEntity.EconomyType>(EnumSet.allOf(EconomyEntity.EconomyType.class));
		for(EconomyEntity.EconomyType eeet : eeetl)
		{
			if(plugin.getYamlHandler().getLang().get("EconomyEntityEconomyType.Replacer."+eeet.toString()) != null)
			{
				String accs = plugin.getYamlHandler().getLang().getString("EconomyEntityEconomyType.Replacer."+eeet.toString());
				if(s.equalsIgnoreCase(accs))
				{ 
					return eeet;
				}
			}
		}
		return EconomyEntity.EconomyType.PLAYER;
	}
	
	public String getAccountType(AccountType act)
	{
		if(plugin.getYamlHandler().getLang().get("AccountType.Replacer."+act.toString()) != null)
		{
			String acts = plugin.getYamlHandler().getLang().getString("AccountType.Replacer."+act.toString());
			if(acts != null)
			{ 
				return acts;
			}
		}
		return AccountType.WALLET.toString();
	}
	
	public AccountType getAccountType(String s)
	{
		List<AccountType> actl = new ArrayList<AccountType>(EnumSet.allOf(AccountType.class));
		for(AccountType act : actl)
		{
			if(plugin.getYamlHandler().getLang().get("AccountType.Replacer."+act.toString()) != null)
			{
				String accs = plugin.getYamlHandler().getLang().getString("AccountType.Replacer."+act.toString());
				if(s.equalsIgnoreCase(accs))
				{ 
					return act;
				}
			}
		}
		return AccountType.WALLET;
	}
	
	public String getAccountCategory(AccountCategory acc)
	{
		if(plugin.getYamlHandler().getLang().get("AccountCategory.Replacer."+acc.toString()) != null)
		{
			String accs = plugin.getYamlHandler().getLang().getString("AccountCategory.Replacer."+acc.toString());
			if(accs != null)
			{ 
				return accs;
			}
		}
		return AccountCategory.MAIN.toString();
	}
	
	public AccountCategory getAccountCategory(String s)
	{
		List<AccountCategory> accl = new ArrayList<AccountCategory>(EnumSet.allOf(AccountCategory.class));
		for(AccountCategory acc : accl)
		{
			if(plugin.getYamlHandler().getLang().get("AccountCategory.Replacer."+acc.toString()) != null)
			{
				String accs = plugin.getYamlHandler().getLang().getString("AccountCategory.Replacer."+acc.toString());
				if(s.equalsIgnoreCase(accs))
				{ 
					return acc;
				}
			}
		}
		return AccountCategory.MAIN;
	}
	
	public String getAccountManagementType(AccountManagementType amt)
	{
		if(plugin.getYamlHandler().getLang().get("AccountManagementType.Replacer."+amt.toString()) != null)
		{
			String amts = plugin.getYamlHandler().getLang().getString("AccountManagementType.Replacer."+amt.toString());
			if(amts != null)
			{ 
				return amts;
			}
		}
		return AccountManagementType.CAN_SEE_BALANCE.toString();
	}
	
	public AccountManagementType getAccountManagementType(String s)
	{
		List<AccountManagementType> amtl = new ArrayList<AccountManagementType>(EnumSet.allOf(AccountManagementType.class));
		for(AccountManagementType amt : amtl)
		{
			if(plugin.getYamlHandler().getLang().get("AccountManagementType.Replacer."+amt.toString()) != null)
			{
				String atms = plugin.getYamlHandler().getLang().getString("AccountManagementType.Replacer."+amt.toString());
				if(s.equalsIgnoreCase(atms))
				{ 
					return amt;
				}
			}
		}
		return AccountManagementType.CAN_SEE_BALANCE;
	}
}
