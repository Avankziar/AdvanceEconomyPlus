package me.avankziar.aep.velocity.api.economy;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.UUID;

import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.ActionLogger;
import me.avankziar.aep.general.objects.TaxationCase;
import me.avankziar.aep.general.objects.TaxationSet;
import me.avankziar.aep.general.objects.TrendLogger;
import me.avankziar.aep.velocity.AEP;
import me.avankziar.aep.velocity.api.LoggerApi;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.EconomyAction.ErrorMessageType;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.velocity.economy.account.Account;
import me.avankziar.ifh.velocity.economy.subinterfaces.CurrencyHandling;
import me.avankziar.ifh.velocity.economy.subinterfaces.CurrencyHandling.Exchange;
import me.avankziar.ifh.velocity.economy.subinterfaces.CurrencyHandling.Exchange.TaxToCurrency;

public class TransactionHandler
{
	private AEP plugin;
	
	private final String IS_NOT_ENABLED;
	private final String HAS_NO_WALLET_SUPPORT;
	private final String HAS_NO_BANK_SUPPORT;
	private final String AMOUNT_IS_NEGATIVE;
	private final String DEPOSIT_ACCOUNT_NOT_EXIST;
	private final String WITHDRAW_ACCOUNT_NOT_EXIST;
	private final String CURRENCYS_ARE_NOT_THE_SAME;
	private final String CURRENCYS_ARE_NOT_EXCHANGEABLE;
	private final String TAX_ACCOUNT_DONT_EXIST;
	private final String TAX_IS_NEGATIVE;
	private final String TAX_IS_HIGHER_OR_EQUAL_AS_100_PERCENT;
	private final String WITHDRAW_HAS_NOT_ENOUGH;
	private final String TA_SUCCESS;
	private final String D_SUCCESS;
	private final String W_SUCCESS;
	private final String EX_SUCCESS;
	
	private final String ACTIONLOG_TRANSACTION;
	private final String ACTIONLOG_DEPOSIT;
	private final String ACTIONLOG_WITHDRAW;
	private final String ACTIONLOG_EXCHANGE;
	
	public TransactionHandler(AEP plugin)
	{
		this.plugin = plugin;
		String base = "TransactionHandler.";
		IS_NOT_ENABLED = plugin.getYamlHandler().getLang().getString(base+"IS_NOT_ENABLED");
		HAS_NO_WALLET_SUPPORT = plugin.getYamlHandler().getLang().getString(base+"HAS_NO_WALLET_SUPPORT");
		HAS_NO_BANK_SUPPORT = plugin.getYamlHandler().getLang().getString(base+"HAS_NO_BANK_SUPPORT");
		AMOUNT_IS_NEGATIVE = plugin.getYamlHandler().getLang().getString(base+"AMOUNT_IS_NEGATIVE");
		DEPOSIT_ACCOUNT_NOT_EXIST = plugin.getYamlHandler().getLang().getString(base+"DEPOSIT_ACCOUNT_NOT_EXIST");
		WITHDRAW_ACCOUNT_NOT_EXIST = plugin.getYamlHandler().getLang().getString(base+"WITHDRAW_ACCOUNT_NOT_EXIST");
		CURRENCYS_ARE_NOT_THE_SAME = plugin.getYamlHandler().getLang().getString(base+"CURRENCYS_ARE_NOT_THE_SAME");
		CURRENCYS_ARE_NOT_EXCHANGEABLE = plugin.getYamlHandler().getLang().getString(base+"CURRENCYS_ARE_NOT_EXCHANGEABLE");
		TAX_ACCOUNT_DONT_EXIST = plugin.getYamlHandler().getLang().getString(base+"TAX_ACCOUNT_DONT_EXIST");
		TAX_IS_NEGATIVE = plugin.getYamlHandler().getLang().getString(base+"TAX_IS_NEGATIVE");
		TAX_IS_HIGHER_OR_EQUAL_AS_100_PERCENT = plugin.getYamlHandler().getLang().getString(base+"TAX_IS_HIGHER_OR_EQUAL_AS_100_PERCENT");
		WITHDRAW_HAS_NOT_ENOUGH = plugin.getYamlHandler().getLang().getString(base+"WITHDRAW_HAS_NOT_ENOUGH");
		TA_SUCCESS = plugin.getYamlHandler().getLang().getString(base+"TA_SUCCESS");
		D_SUCCESS = plugin.getYamlHandler().getLang().getString(base+"D_SUCCESS");
		W_SUCCESS = plugin.getYamlHandler().getLang().getString(base+"W_SUCCESS");
		EX_SUCCESS = plugin.getYamlHandler().getLang().getString(base+"EX_SUCCESS");
		
		ACTIONLOG_TRANSACTION = plugin.getYamlHandler().getLang().getString(base+"ACTIONLOG_TRANSACTION");
		ACTIONLOG_DEPOSIT = plugin.getYamlHandler().getLang().getString(base+"ACTIONLOG_DEPOSIT");
		ACTIONLOG_WITHDRAW = plugin.getYamlHandler().getLang().getString(base+"ACTIONLOG_WITHDRAW");
		ACTIONLOG_EXCHANGE = plugin.getYamlHandler().getLang().getString(base+"ACTIONLOG_EXCHANGE");
	}
	
	private boolean existAccount(Account account)
	{
		if(account == null)
		{
			return false;
		}
		return plugin.getMysqlHandler().exist(MysqlType.ACCOUNT_VELOCITY, 
				"`id` = ?");
	}
	
	private void saveAccount(Account... account)
	{
		for(Account a : account)
		{
			if(a == null)
			{
				continue;
			}
			plugin.getMysqlHandler().updateData(MysqlType.ACCOUNT_VELOCITY, a, "`id` = ?", a.getID());
		}
	}
	
	private EconomyAction preCheck(int i, boolean normalCurrencyCheck, Account withdraw, Account deposit, double amount)
	{
		if(!plugin.getIFHApi().isEnabled())
		{
			return new EconomyAction(amount, 0.0, 0.0, true, IS_NOT_ENABLED, ErrorMessageType.IS_NOT_ENABLED, 0.0, 0.0);
		}
		if(i == 1)
		{
			if(withdraw == null || !existAccount(withdraw))
			{
				return new EconomyAction(amount, 0.0, 0.0, true, WITHDRAW_ACCOUNT_NOT_EXIST, ErrorMessageType.WITHDRAW_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
		} else if(i == 2)
		{
			if(deposit == null || !existAccount(deposit))
			{
				return new EconomyAction(amount, 0.0, 0.0, true, DEPOSIT_ACCOUNT_NOT_EXIST, ErrorMessageType.DEPOSIT_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
		} else
		{
			if(withdraw == null || !existAccount(withdraw))
			{
				return new EconomyAction(amount, 0.0, 0.0, true, WITHDRAW_ACCOUNT_NOT_EXIST, ErrorMessageType.WITHDRAW_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
			if(deposit == null || !existAccount(deposit))
			{
				return new EconomyAction(amount, 0.0, 0.0, true, DEPOSIT_ACCOUNT_NOT_EXIST, ErrorMessageType.DEPOSIT_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
		}
		if(!plugin.getIFHApi().hasWalletSupport() 
				&& (withdraw.getType() == AccountType.WALLET
				|| deposit.getType() == AccountType.WALLET))
		{
			return new EconomyAction(amount, 0.0, 0.0, true, HAS_NO_WALLET_SUPPORT, ErrorMessageType.HAS_NO_WALLET_SUPPORT, 0.0, 0.0);
		}
		if(!plugin.getIFHApi().hasBankSupport() 
				&& (withdraw.getType() == AccountType.BANK
				|| deposit.getType() == AccountType.BANK))
		{
			return new EconomyAction(amount, 0.0, 0.0, true, HAS_NO_BANK_SUPPORT, ErrorMessageType.HAS_NO_BANK_SUPPORT, 0.0, 0.0);
		}
		if(amount < 0.0)
		{
			return new EconomyAction(amount, 0.0, 0.0, true, AMOUNT_IS_NEGATIVE, ErrorMessageType.AMOUNT_IS_NEGATIVE, 0.0, 0.0);
		}
		if(normalCurrencyCheck)
		{
			if(!withdraw.getCurrency().getUniqueName().equals(deposit.getCurrency().getUniqueName()))
			{
				return new EconomyAction(amount, 0.0, 0.0, true, CURRENCYS_ARE_NOT_THE_SAME, ErrorMessageType.CURRENCYS_ARE_NOT_THE_SAME, withdraw.getBalance(), deposit.getBalance());
			}
		} else
		{
			if(!withdraw.getCurrency().isExchangeable() || !deposit.getCurrency().isExchangeable())
			{
				return new EconomyAction(amount, 0.0, 0.0, true, CURRENCYS_ARE_NOT_EXCHANGEABLE, ErrorMessageType.CURRENCYS_ARE_NOT_EXCHANGEABLE, withdraw.getBalance(), deposit.getBalance());
			}
		}
		return null;
	}
	
	private EconomyAction preCheckTransaction(int i, boolean normalCurrencyCheck, Account withdraw, Account deposit,
			double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot, boolean withdrawCanGoNegativ)
	{
		final EconomyAction preCheck = preCheck(i, normalCurrencyCheck, withdraw, deposit, amount);
		if(preCheck != null)
		{
			return preCheck;
		}
		if(taxDepot != null)
		{
			if(!existAccount(taxDepot))
			{
				return new EconomyAction(amount, 0.0, 0.0, true, TAX_ACCOUNT_DONT_EXIST, ErrorMessageType.TAX_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
			if(!withdraw.getCurrency().getUniqueName().equals(taxDepot.getCurrency().getUniqueName()))
			{
				return new EconomyAction(amount, 0.0, 0.0, true, CURRENCYS_ARE_NOT_THE_SAME, ErrorMessageType.CURRENCYS_ARE_NOT_THE_SAME, withdraw.getBalance(), deposit.getBalance());
			}
			if(taxInPercent < 0.0)
			{
				return new EconomyAction(amount, 0.0, 0.0, true, TAX_IS_NEGATIVE, ErrorMessageType.TAX_IS_NEGATIVE, withdraw.getBalance(), deposit.getBalance());
			}
			if(taxInPercent >= 1.0 && !taxAreExclusive)
			{
				return new EconomyAction(amount, 0.0, 0.0, true, TAX_IS_HIGHER_OR_EQUAL_AS_100_PERCENT, ErrorMessageType.TAX_IS_HIGHER_OR_EQUAL_AS_100_PERCENT, withdraw.getBalance(), deposit.getBalance());
			}
			double amountToWithdraw = 0.0;
			double amountToDeposit = 0.0;
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
			if(!withdrawCanGoNegativ && withdraw.getBalance() < amountToWithdraw)
			{
				return new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, WITHDRAW_HAS_NOT_ENOUGH, ErrorMessageType.WITHDRAW_HAS_NOT_ENOUGH, withdraw.getBalance(), deposit.getBalance());
			}
		} else
		{
			if(!withdrawCanGoNegativ && withdraw.getBalance() < amount)
			{
				return new EconomyAction(amount, 0.0, 0.0, true, WITHDRAW_HAS_NOT_ENOUGH, ErrorMessageType.WITHDRAW_HAS_NOT_ENOUGH, withdraw.getBalance(), deposit.getBalance());
			}
		}
		return null;
	}

	public EconomyAction transaction(Account withdraw, Account deposit, double amount, boolean withdrawCanGoNegativ)
	{
		final EconomyAction preCheck = preCheckTransaction(0, true, withdraw, deposit, amount, -1.0, true, null, withdrawCanGoNegativ);
		if(preCheck != null)
		{
			return preCheck;
		}
		withdraw.setBalance(withdraw.getBalance()-amount);
		deposit.setBalance(deposit.getBalance()+amount);
		final EconomyAction ea = new EconomyAction(amount, amount, 0.0, true, TA_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		saveAccount(withdraw, deposit);
		return ea;
	}
	
	public EconomyAction transaction(Account withdraw, Account deposit, double amount,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment, boolean withdrawCanGoNegativ)
	{
		final EconomyAction preCheck = preCheckTransaction(0, true, withdraw, deposit, amount, -1.0, true, null, withdrawCanGoNegativ);
		if(preCheck != null)
		{
			return preCheck;
		}
		withdraw.setBalance(withdraw.getBalance()-amount);
		deposit.setBalance(deposit.getBalance()+amount);
		final EconomyAction ea = new EconomyAction(amount, amount, 0.0, true, TA_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(), 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amount, amount, 0.0,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_TRANSACTION));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amount, withdraw.getBalance(), withdraw.getBalance()));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amount, deposit.getBalance(), deposit.getBalance()));
		saveAccount(withdraw, deposit);
		return ea;
	}
	
	public EconomyAction transaction(Account withdraw, Account deposit, double amount,
			double taxInPercent, boolean taxAreExclusive, Account taxDepot, boolean withdrawCanGoNegativ)
	{
		final EconomyAction preCheck = preCheckTransaction(0, true, withdraw, deposit, amount,
				taxInPercent, taxAreExclusive, taxDepot, withdrawCanGoNegativ);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
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
		withdraw.setBalance(withdraw.getBalance()-amountToWithdraw);
		deposit.setBalance(deposit.getBalance()+amountToDeposit);
		if(taxDepot != null)
		{
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
		}
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, TA_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		saveAccount(withdraw, deposit, taxDepot);
		return ea;
	}
	
	public EconomyAction transaction(Account withdraw, Account deposit, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment, boolean withdrawCanGoNegativ)
	{
		final EconomyAction preCheck = preCheckTransaction(0, true, withdraw, deposit, amount,
				taxInPercent, taxAreExclusive, taxDepot, withdrawCanGoNegativ);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
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
		withdraw.setBalance(withdraw.getBalance()-amountToWithdraw);
		deposit.setBalance(deposit.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, TA_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(), amountToTax > 0.0 ? taxDepot.getID() : 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, amountToTax,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_TRANSACTION));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amountToWithdraw, withdraw.getBalance(), withdraw.getBalance()));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amountToDeposit, deposit.getBalance(), deposit.getBalance()));
		if(taxDepot != null && amountToTax > 0.0)
		{
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					taxDepot.getID(), amountToTax, taxDepot.getBalance(), taxDepot.getBalance()));
		}
		saveAccount(withdraw, deposit, taxDepot);
		return ea;
	}

	public EconomyAction deposit(Account holder, double amount)
	{
		final EconomyAction preCheck = preCheckTransaction(2, true, null, holder, amount, -1.0, true, null, false);
		if(preCheck != null)
		{
			return preCheck;
		}
		holder.setBalance(holder.getBalance()+amount);
		final EconomyAction ea = new EconomyAction(0.0, amount, 0.0, true, D_SUCCESS, ErrorMessageType.SUCCESS,
				0.0, holder.getBalance());
		saveAccount(holder);
		return ea;
	}
	
	public EconomyAction deposit(Account holder, double amount, OrdererType type, String ordererUUIDOrPlugin,
			String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(2, true, null, holder, amount, -1.0, true, null, false);
		if(preCheck != null)
		{
			return preCheck;
		}
		holder.setBalance(holder.getBalance()+amount);
		final EconomyAction ea = new EconomyAction(0.0, amount, 0.0, true, D_SUCCESS, ErrorMessageType.SUCCESS,
				0.0, holder.getBalance());
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				0, holder.getID(), 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				0.0, amount, 0.0,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_DEPOSIT));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), amount, holder.getBalance(), holder.getBalance()));
		saveAccount(holder);
		return ea;
	}
	
	public EconomyAction deposit(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
	{
		final EconomyAction preCheck = preCheckTransaction(2, true, null, holder, amount, taxInPercent, taxAreExclusive, taxDepot, false);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
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
		holder.setBalance(holder.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, D_SUCCESS, ErrorMessageType.SUCCESS,
				0.0, holder.getBalance());
		if(taxDepot != null)
		{
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
		}
		saveAccount(holder, taxDepot);
		return ea;
	}
	
	public EconomyAction deposit(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(2, true, null, holder, amount, taxInPercent, taxAreExclusive, taxDepot, false);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
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
		holder.setBalance(holder.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, D_SUCCESS, ErrorMessageType.SUCCESS,
				0.0, holder.getBalance());
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				0, holder.getID(), amountToTax > 0.0 ? taxDepot.getID() : 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToDeposit, amountToDeposit, amountToTax,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_DEPOSIT));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), amountToDeposit, holder.getBalance(), holder.getBalance()));
		if(taxDepot != null && amountToTax > 0.0)
		{
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					taxDepot.getID(), amountToTax, taxDepot.getBalance(), taxDepot.getBalance()));
		}
		saveAccount(holder, taxDepot);
		return ea;
	}
	
	public EconomyAction withdraw(Account holder, double amount, boolean withdrawCanGoNegativ)
	{
		final EconomyAction preCheck = preCheckTransaction(1, true, holder, null, amount, -1.0, true, null, withdrawCanGoNegativ);
		if(preCheck != null)
		{
			return preCheck;
		}
		holder.setBalance(holder.getBalance()-amount);
		final EconomyAction ea = new EconomyAction(amount, 0.0, 0.0, true, W_SUCCESS, ErrorMessageType.SUCCESS,
				holder.getBalance(), 0.0);
		saveAccount(holder);
		return ea;
	}
	
	public EconomyAction withdraw(Account holder, double amount, 
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment, boolean withdrawCanGoNegativ)
	{
		final EconomyAction preCheck = preCheckTransaction(1, true, holder, null, amount, -1.0, true, null, withdrawCanGoNegativ);
		if(preCheck != null)
		{
			return preCheck;
		}
		holder.setBalance(holder.getBalance()-amount);
		final EconomyAction ea = new EconomyAction(amount, 0.0, 0.0, true, W_SUCCESS, ErrorMessageType.SUCCESS,
				holder.getBalance(), 0.0);
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				holder.getID(), 0, 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amount, 0.0, 0.0,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_WITHDRAW));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), -amount, holder.getBalance(), holder.getBalance()));
		saveAccount(holder);
		return ea;
	}
	
	public EconomyAction withdraw(Account holder, double amount,
			double taxInPercent, boolean taxAreExclusive, Account taxDepot, boolean withdrawCanGoNegativ)
	{
		final EconomyAction preCheck = preCheckTransaction(1, true, holder, null, amount, taxInPercent, taxAreExclusive, taxDepot, withdrawCanGoNegativ);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
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
		holder.setBalance(holder.getBalance()-amountToWithdraw);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, W_SUCCESS, ErrorMessageType.SUCCESS,
				holder.getBalance(), 0.0);
		if(taxDepot != null)
		{
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
		}
		saveAccount(holder, taxDepot);
		return ea;
	}
	
	public EconomyAction withdraw(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment, boolean withdrawCanGoNegativ)
	{
		final EconomyAction preCheck = preCheckTransaction(1, true, holder, null, amount, taxInPercent, taxAreExclusive, taxDepot, withdrawCanGoNegativ);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
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
		holder.setBalance(holder.getBalance()-amountToWithdraw);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, W_SUCCESS, ErrorMessageType.SUCCESS,
				holder.getBalance(), 0.0);
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				holder.getID(), 0, amountToTax > 0.0 ? taxDepot.getID() : 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, amountToTax,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_WITHDRAW));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), -amountToWithdraw, holder.getBalance(), holder.getBalance()));
		if(taxDepot != null && amountToTax > 0.0)
		{
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					taxDepot.getID(), amountToTax, taxDepot.getBalance(), taxDepot.getBalance()));
		}
		saveAccount(holder, taxDepot);
		return ea;
	}
	
	public EconomyAction exchangeCurrencies(Account withdraw, Account deposit, double amount)
	{
		final EconomyAction preCheck = preCheckTransaction(0, false, withdraw, deposit, amount, -1.0, true, null, false);
		if(preCheck != null)
		{
			return preCheck;
		}
		boolean taxAreExclusive = false;
		double taxInPercent = 0.0;
		if(CurrencyHandler.taxationMap.containsKey(withdraw.getCurrency().getUniqueName())
				&& CurrencyHandler.taxationMap.get(withdraw.getCurrency().getUniqueName()).containsKey(TaxationCase.CURRENCY_EXCHANGE))
		{
			LinkedHashMap<TaxationCase, TaxationSet> map = CurrencyHandler.taxationMap.get(withdraw.getCurrency().getUniqueName());
			TaxationSet ts = map.get(TaxationCase.CURRENCY_EXCHANGE);
			taxAreExclusive = ts.isTaxAreExclusive();
			taxInPercent = ts.getTaxInPercent();
		}
		CurrencyHandling.Exchange ex = plugin.getIFHApi().getExchangeOfCurrency(amount, withdraw.getCurrency(), deposit.getCurrency(), taxInPercent,
				taxAreExclusive, withdraw.getCurrency().getTaxationBeforeExchange());
		double amountToWithdraw = ex.toWithdrawAccount;
		double amountToDeposit = ex.toDepositAccount;
		withdraw.setBalance(withdraw.getBalance()-amountToWithdraw);
		deposit.setBalance(deposit.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, 0.0, true, EX_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		saveAccount(withdraw, deposit);
		return ea;
	}
	
	public EconomyAction exchangeCurrencies(Account withdraw, Account deposit, double amount,
			OrdererType type, String ordererUUIDOrPlugin,
			String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(0, false, withdraw, deposit, amount, -1.0, true, null, false);
		if(preCheck != null)
		{
			return preCheck;
		}
		boolean taxAreExclusive = false;
		double taxInPercent = 0.0;
		if(CurrencyHandler.taxationMap.containsKey(withdraw.getCurrency().getUniqueName())
				&& CurrencyHandler.taxationMap.get(withdraw.getCurrency().getUniqueName()).containsKey(TaxationCase.CURRENCY_EXCHANGE))
		{
			LinkedHashMap<TaxationCase, TaxationSet> map = CurrencyHandler.taxationMap.get(withdraw.getCurrency().getUniqueName());
			TaxationSet ts = map.get(TaxationCase.CURRENCY_EXCHANGE);
			taxAreExclusive = ts.isTaxAreExclusive();
			taxInPercent = ts.getTaxInPercent();
		}
		
		Exchange ex = plugin.getIFHApi().getExchangeOfCurrency(amount, withdraw.getCurrency(), deposit.getCurrency(), taxInPercent,
				taxAreExclusive, withdraw.getCurrency().getTaxationBeforeExchange());
		double amountToWithdraw = ex.toWithdrawAccount;
		double amountToDeposit = ex.toDepositAccount;
		withdraw.setBalance(withdraw.getBalance()-amountToWithdraw);
		deposit.setBalance(deposit.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, 0.0, true, EX_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(), 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, 0.0,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_EXCHANGE));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amount, withdraw.getBalance(), withdraw.getBalance()));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amount, deposit.getBalance(), deposit.getBalance()));
		saveAccount(withdraw, deposit);
		return ea;
	}
	
	public EconomyAction exchangeCurrencies(Account withdraw, Account deposit, double amount,
			double taxInPercent, boolean taxAreExclusive, Account withdrawAccounttaxDepot, Account depositAccounttaxDepot)
	{
		final EconomyAction preCheck = preCheckTransaction(0, false, withdraw, deposit, amount,
				taxInPercent, taxAreExclusive, withdrawAccounttaxDepot, false);
		if(preCheck != null)
		{
			return preCheck;
		}
		final EconomyAction preCheckII = preCheckTransaction(0, false, withdraw, deposit, amount,
				taxInPercent, taxAreExclusive, depositAccounttaxDepot, false);
		if(preCheckII != null)
		{
			return preCheckII;
		}
		
		Exchange ex = plugin.getIFHApi().getExchangeOfCurrency(amount, withdraw.getCurrency(), deposit.getCurrency(), taxInPercent,
				taxAreExclusive, withdraw.getCurrency().getTaxationBeforeExchange());
		double amountToWithdraw = ex.toWithdrawAccount;
		double amountToDeposit = ex.toDepositAccount;
		double amountToTax = ex.toTaxAccount;
		withdraw.setBalance(withdraw.getBalance()-amountToWithdraw);
		deposit.setBalance(deposit.getBalance()+amountToDeposit);
		if(withdrawAccounttaxDepot != null && amountToTax > 0.0 && ex.taxTo == TaxToCurrency.TO_WITHDRAW_CURRENCY)
		{
			withdrawAccounttaxDepot.setBalance(withdrawAccounttaxDepot.getBalance()+amountToTax);
		} else if(depositAccounttaxDepot != null && amountToTax > 0.0 && ex.taxTo == TaxToCurrency.TO_DEPOSIT_CURRENCY)
		{
			depositAccounttaxDepot.setBalance(depositAccounttaxDepot.getBalance()+amountToTax);
		}
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, EX_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		saveAccount(withdraw, deposit, withdrawAccounttaxDepot, depositAccounttaxDepot);
		return ea;
	}
	
	public EconomyAction exchangeCurrencies(Account withdraw, Account deposit, double amount,
			double taxInPercent, boolean taxAreExclusive, Account withdrawAccounttaxDepot, Account depositAccounttaxDepot,  
			OrdererType type, String ordererUUIDOrPlugin, 
			String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(0, false, withdraw, deposit, amount,
				taxInPercent, taxAreExclusive, withdrawAccounttaxDepot, false);
		if(preCheck != null)
		{
			return preCheck;
		}
		final EconomyAction preCheckII = preCheckTransaction(0, false, withdraw, deposit, amount,
				taxInPercent, taxAreExclusive, depositAccounttaxDepot, false);
		if(preCheckII != null)
		{
			return preCheckII;
		}		
		Exchange ex = plugin.getIFHApi().getExchangeOfCurrency(amount, withdraw.getCurrency(), deposit.getCurrency(), taxInPercent,
				taxAreExclusive, withdraw.getCurrency().getTaxationBeforeExchange());
		double amountToWithdraw = ex.toWithdrawAccount;
		double amountToDeposit = ex.toDepositAccount;
		double amountToTax = ex.toTaxAccount;
		withdraw.setBalance(withdraw.getBalance()-amountToWithdraw);
		deposit.setBalance(deposit.getBalance()+amountToDeposit);
		
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, EX_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(),
				amountToTax > 0.0 ? 
						(ex.taxTo == TaxToCurrency.TO_WITHDRAW_CURRENCY ? withdrawAccounttaxDepot.getID() : depositAccounttaxDepot.getID()) 
						: 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, amountToTax,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_EXCHANGE));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amountToWithdraw, withdraw.getBalance(), withdraw.getBalance()));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amountToDeposit, deposit.getBalance(), deposit.getBalance()));
		if(withdrawAccounttaxDepot != null && amountToTax > 0.0 && ex.taxTo == TaxToCurrency.TO_WITHDRAW_CURRENCY)
		{
			withdrawAccounttaxDepot.setBalance(withdrawAccounttaxDepot.getBalance()+amountToTax);
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					withdrawAccounttaxDepot.getID(), amountToTax, withdrawAccounttaxDepot.getBalance(), withdrawAccounttaxDepot.getBalance()));
		} else if(depositAccounttaxDepot != null && amountToTax > 0.0 && ex.taxTo == TaxToCurrency.TO_DEPOSIT_CURRENCY)
		{
			depositAccounttaxDepot.setBalance(depositAccounttaxDepot.getBalance()+amountToTax);
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					depositAccounttaxDepot.getID(), amountToTax, depositAccounttaxDepot.getBalance(), depositAccounttaxDepot.getBalance()));
		}
		saveAccount(withdraw, deposit, withdrawAccounttaxDepot, depositAccounttaxDepot);
		return ea;
	}
}