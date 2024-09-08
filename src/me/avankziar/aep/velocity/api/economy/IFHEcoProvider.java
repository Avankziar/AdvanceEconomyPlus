package me.avankziar.aep.velocity.api.economy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.velocitypowered.api.plugin.PluginDescription;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AccountVelo;
import me.avankziar.aep.general.objects.QuickPayAccount;
import me.avankziar.aep.velocity.AEP;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.velocity.economy.Economy;
import me.avankziar.ifh.velocity.economy.account.Account;
import me.avankziar.ifh.velocity.economy.currency.Currency;
import me.avankziar.ifh.velocity.economy.currency.EconomyCurrency;
import me.avankziar.ifh.velocity.economy.currency.Gradation;

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
	
	public IFHEcoProvider(AEP plugin)
	{
		this.plugin = plugin;
		accountHandler = new AccountHandler(plugin);
		currencyHandler = new CurrencyHandler(plugin);
		transactionHandler = new TransactionHandler(plugin);
	}
	
	public void saveAccount(Account... account)
	{
		for(Account a : account)
		{
			AccountVelo acc = new AccountVelo(a);
			plugin.getMysqlHandler().updateData(MysqlType.ACCOUNT_VELOCITY, acc, "`id` = ?", a.getID());
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
		AccountVelo acc = new AccountVelo(account);
		return accountHandler.createAccount(acc);
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
		return currencyHandler.registerCurrency(currency);
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
			BigDecimal result = new BigDecimal(amount);
			if(result.doubleValue() != 0.0 && divisor.doubleValue() != 0.0)
			{
				result = new BigDecimal(amount).divide(divisor, 10, roundingMode);
			}
			String si = null;
			if(useSIPrefix && defaultSIPrefix.containsKey(ec.getUniqueName()))
			{
				for(Entry<Double, String> e : defaultSIPrefix.get(ec.getUniqueName()).entrySet())
				{
					BigDecimal step1 = result.divide(new BigDecimal(e.getKey()), 10, roundingMode);
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
			BigDecimal resultWhile = result;
			if(result.doubleValue() != 0.0 && divisor.doubleValue() != 0.0)
			{
				resultWhile = result.divide(divisor);
			}
			if(highest == lowest)
			{
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
							break;
						}
					}
					result = resultForSI;
				} else
				{
					BigDecimal val = new BigDecimal(gr.getValueToBaseGradation());
					result = resultWhile.multiply(val)
							.subtract(new BigDecimal(resultWhile.toBigInteger()).multiply(val));
				}
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
	public String getAuthors()
	{
		PluginDescription pd = AEP.getPlugin().getServer().getPluginManager().getPlugin(plugin.pluginname.toLowerCase()).get().getDescription();
		return String.join(", ", pd.getAuthors());
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
		return plugin.pluginname;
	}

	@Override
	public boolean hasBankSupport()
	{
		return true;
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
