package main.java.me.avankziar.aep.spigot.api.economy;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.UUID;

import main.java.me.avankziar.aep.general.objects.ActionLogger;
import main.java.me.avankziar.aep.general.objects.TaxationCase;
import main.java.me.avankziar.aep.general.objects.TaxationSet;
import main.java.me.avankziar.aep.general.objects.TrendLogger;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.LoggerApi;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountType;
import main.java.me.avankziar.ifh.general.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.general.economy.action.EconomyAction.ErrorMessageType;
import main.java.me.avankziar.ifh.general.economy.action.OrdererType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.subinterfaces.CurrencyHandling.Exchange;
import main.java.me.avankziar.ifh.spigot.economy.subinterfaces.CurrencyHandling.Exchange.TaxToCurrency;

public class TransactionHandler
{
	private AdvancedEconomyPlus plugin;
	
	private String IS_NOT_ENABLED;
	private String HAS_NO_WALLET_SUPPORT;
	private String HAS_NO_BANK_SUPPORT;
	private String AMOUNT_IS_NEGATIVE;
	private String DEPOSIT_ACCOUNT_NOT_EXIST;
	private String WITHDRAW_ACCOUNT_NOT_EXIST;
	private String CURRENCYS_ARE_NOT_THE_SAME;
	private String CURRENCYS_ARE_NOT_EXCHANGEABLE;
	private String TAX_ACCOUNT_DONT_EXIST;
	private String TAX_IS_NEGATIVE;
	private String TAX_IS_HIGHER_OR_EQUAL_AS_100_PERCENT;
	private String WITHDRAW_HAS_NOT_ENOUGH;
	private String TA_SUCCESS;
	private String D_SUCCESS;
	private String W_SUCCESS;
	private String EX_SUCCESS;
	
	private String ACTIONLOG_TRANSACTION;
	private String ACTIONLOG_DEPOSIT;
	private String ACTIONLOG_WITHDRAW;
	private String ACTIONLOG_EXCHANGE;
	
	public TransactionHandler(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	public void init()
	{
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
		return plugin.getMysqlHandler().exist(MysqlHandler.Type.ACCOUNT, "`id` = ?", account.getID());
	}
	
	private void saveAccount(Account... account)
	{
		for(Account a : account)
		{
			if(a == null)
			{
				continue;
			}
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.ACCOUNT, a, "`id` = ?", a.getID());
		}
	}
	
	private EconomyAction preCheck(int i, boolean normalCurrencyCheck, Account withdraw, Account deposit, double amount)
	{
		if(!plugin.getIFHApi().isEnabled())
		{
			return new EconomyAction(amount, 0.0, 0.0, false, IS_NOT_ENABLED, ErrorMessageType.IS_NOT_ENABLED, 0.0, 0.0);
		}
		if(i == 1)
		{
			if(withdraw == null || !existAccount(withdraw))
			{
				return new EconomyAction(amount, 0.0, 0.0, false, WITHDRAW_ACCOUNT_NOT_EXIST, ErrorMessageType.WITHDRAW_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
			if(!plugin.getIFHApi().hasWalletSupport() 
					&& withdraw.getType() == AccountType.WALLET)
			{
				return new EconomyAction(amount, 0.0, 0.0, false, HAS_NO_WALLET_SUPPORT, ErrorMessageType.HAS_NO_WALLET_SUPPORT, 0.0, 0.0);
			}
			if(!plugin.getIFHApi().hasBankSupport() 
					&& withdraw.getType() == AccountType.BANK)
			{
				return new EconomyAction(amount, 0.0, 0.0, false, HAS_NO_BANK_SUPPORT, ErrorMessageType.HAS_NO_BANK_SUPPORT, 0.0, 0.0);
			}
		} else if(i == 2)
		{
			if(deposit == null || !existAccount(deposit))
			{
				return new EconomyAction(amount, 0.0, 0.0, false, DEPOSIT_ACCOUNT_NOT_EXIST, ErrorMessageType.DEPOSIT_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
			if(!plugin.getIFHApi().hasWalletSupport() 
					&& deposit.getType() == AccountType.WALLET)
			{
				return new EconomyAction(amount, 0.0, 0.0, false, HAS_NO_WALLET_SUPPORT, ErrorMessageType.HAS_NO_WALLET_SUPPORT, 0.0, 0.0);
			}
			if(!plugin.getIFHApi().hasBankSupport() 
					&& deposit.getType() == AccountType.BANK)
			{
				return new EconomyAction(amount, 0.0, 0.0, false, HAS_NO_BANK_SUPPORT, ErrorMessageType.HAS_NO_BANK_SUPPORT, 0.0, 0.0);
			}
		} else
		{
			if(withdraw == null || !existAccount(withdraw))
			{
				return new EconomyAction(amount, 0.0, 0.0, false, WITHDRAW_ACCOUNT_NOT_EXIST, ErrorMessageType.WITHDRAW_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
			if(deposit == null || !existAccount(deposit))
			{
				return new EconomyAction(amount, 0.0, 0.0, false, DEPOSIT_ACCOUNT_NOT_EXIST, ErrorMessageType.DEPOSIT_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
			if(!plugin.getIFHApi().hasWalletSupport() 
					&& (withdraw.getType() == AccountType.WALLET
					|| deposit.getType() == AccountType.WALLET))
			{
				return new EconomyAction(amount, 0.0, 0.0, false, HAS_NO_WALLET_SUPPORT, ErrorMessageType.HAS_NO_WALLET_SUPPORT, 0.0, 0.0);
			}
			if(!plugin.getIFHApi().hasBankSupport() 
					&& (withdraw.getType() == AccountType.BANK
					|| deposit.getType() == AccountType.BANK))
			{
				return new EconomyAction(amount, 0.0, 0.0, false, HAS_NO_BANK_SUPPORT, ErrorMessageType.HAS_NO_BANK_SUPPORT, 0.0, 0.0);
			}
			if(normalCurrencyCheck)
			{
				if(!withdraw.getCurrency().getUniqueName().equals(deposit.getCurrency().getUniqueName()))
				{
					return new EconomyAction(amount, 0.0, 0.0, false, CURRENCYS_ARE_NOT_THE_SAME, ErrorMessageType.CURRENCYS_ARE_NOT_THE_SAME, withdraw.getBalance(), deposit.getBalance());
				}
			} else
			{
				if(!withdraw.getCurrency().isExchangeable() || !deposit.getCurrency().isExchangeable())
				{
					return new EconomyAction(amount, 0.0, 0.0, false, CURRENCYS_ARE_NOT_EXCHANGEABLE, ErrorMessageType.CURRENCYS_ARE_NOT_EXCHANGEABLE, withdraw.getBalance(), deposit.getBalance());
				}
			}
		}
		if(amount < 0.0)
		{
			return new EconomyAction(amount, 0.0, 0.0, false, AMOUNT_IS_NEGATIVE, ErrorMessageType.AMOUNT_IS_NEGATIVE, 0.0, 0.0);
		}
		return null;
	}
	
	private EconomyAction preCheckTransaction(int i, boolean normalCurrencyCheck, Account withdraw, Account deposit, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
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
				return new EconomyAction(amount, 0.0, 0.0, false, TAX_ACCOUNT_DONT_EXIST, ErrorMessageType.TAX_ACCOUNT_DONT_EXIST, 0.0, 0.0);
			}
			if(!withdraw.getCurrency().getUniqueName().equals(taxDepot.getCurrency().getUniqueName()))
			{
				return new EconomyAction(amount, 0.0, 0.0, false, CURRENCYS_ARE_NOT_THE_SAME, ErrorMessageType.CURRENCYS_ARE_NOT_THE_SAME,
						withdraw != null ? withdraw.getBalance() : 0.0,
						deposit != null ? deposit.getBalance() : 0.0);
			}
			if(taxInPercent < 0.0)
			{
				return new EconomyAction(amount, 0.0, 0.0, false, TAX_IS_NEGATIVE, ErrorMessageType.TAX_IS_NEGATIVE,
						withdraw != null ? withdraw.getBalance() : 0.0,
						deposit != null ? deposit.getBalance() : 0.0);
			}
			if(taxInPercent >= 1.0 && !taxAreExclusive)
			{
				return new EconomyAction(amount, 0.0, 0.0, false, TAX_IS_HIGHER_OR_EQUAL_AS_100_PERCENT, ErrorMessageType.TAX_IS_HIGHER_OR_EQUAL_AS_100_PERCENT,
						withdraw != null ? withdraw.getBalance() : 0.0,
						deposit != null ? deposit.getBalance() : 0.0);
			}
			double amountToWithdraw = 0.0;
			double amountToDeposit = 0.0;
			if(taxInPercent <= 0)
			{
				amountToWithdraw = amount;
				amountToDeposit = amount;
			} else if(taxAreExclusive)
			{
				amountToWithdraw = amount + amount*taxInPercent/100;
				amountToDeposit = amount;
			} else
			{
				amountToWithdraw = amount;
				amountToDeposit = amount - amount*taxInPercent/100;
			}
			double amountToTax = amountToWithdraw - amountToDeposit;
			if(withdraw != null && withdraw.getBalance() < amountToWithdraw)
			{
				return new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, false, WITHDRAW_HAS_NOT_ENOUGH, ErrorMessageType.WITHDRAW_HAS_NOT_ENOUGH,
						withdraw != null ? withdraw.getBalance() : 0.0,
						deposit != null ? deposit.getBalance() : 0.0);
			}
		} else
		{
			if(withdraw != null && withdraw.getBalance() < amount)
			{
				return new EconomyAction(amount, 0.0, 0.0, false, WITHDRAW_HAS_NOT_ENOUGH, ErrorMessageType.WITHDRAW_HAS_NOT_ENOUGH,
						withdraw != null ? withdraw.getBalance() : 0.0,
						deposit != null ? deposit.getBalance() : 0.0);
			}
		}
		return null;
	}

	public EconomyAction transaction(Account withdraw, Account deposit, double amount)
	{
		final EconomyAction preCheck = preCheckTransaction(0, true, withdraw, deposit, amount, -1.0, true, null);
		if(preCheck != null)
		{
			return preCheck;
		}
		OrdererType ot = OrdererType.PLUGIN;
		String ordererUUIDOrPlugin = "AEP";
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(), 0,
				ot,
				ot == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				ot == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amount, amount, 0.0,
				"default",
				ACTIONLOG_TRANSACTION));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amount, withdraw.getBalance(), withdraw.getBalance()-amount));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amount, deposit.getBalance(), deposit.getBalance()+amount));
		withdraw.setBalance(withdraw.getBalance()-amount);
		deposit.setBalance(deposit.getBalance()+amount);
		final EconomyAction ea = new EconomyAction(amount, amount, 0.0, true, TA_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		saveAccount(withdraw, deposit);
		return ea;
	}
	
	public EconomyAction transaction(Account withdraw, Account deposit, double amount,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(0, true, withdraw, deposit, amount, -1.0, true, null);
		if(preCheck != null)
		{
			return preCheck;
		}
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(), 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amount, amount, 0.0,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_TRANSACTION));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amount, withdraw.getBalance(), withdraw.getBalance()-amount));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amount, deposit.getBalance(), deposit.getBalance()+amount));
		withdraw.setBalance(withdraw.getBalance()-amount);
		deposit.setBalance(deposit.getBalance()+amount);
		final EconomyAction ea = new EconomyAction(amount, amount, 0.0, true, TA_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		saveAccount(withdraw, deposit);
		return ea;
	}
	
	public EconomyAction transaction(Account withdraw, Account deposit, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
	{
		final EconomyAction preCheck = preCheckTransaction(0, true, withdraw, deposit, amount, taxInPercent, taxAreExclusive, taxDepot);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
		if(taxInPercent <= 0)
		{
			amountToWithdraw = amount;
			amountToDeposit = amount;
		} else if(taxAreExclusive)
		{
			amountToWithdraw = amount + amount*taxInPercent/100;
			amountToDeposit = amount;
		} else
		{
			amountToWithdraw = amount;
			amountToDeposit = amount - amount*taxInPercent/100;
		}		
		double amountToTax = amountToWithdraw - amountToDeposit;
		OrdererType ot = OrdererType.PLUGIN;
		String ordererUUIDOrPlugin = "AEP";
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(), taxDepot != null ? taxDepot.getID() : 0,
				ot,
				ot == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				ot == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, amountToTax,
				"default",
				ACTIONLOG_TRANSACTION));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amount, withdraw.getBalance(), withdraw.getBalance()-amountToWithdraw));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amount, deposit.getBalance(), deposit.getBalance()+amountToDeposit));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				taxDepot.getID(), amountToTax, taxDepot.getBalance(), taxDepot.getBalance()+amountToTax));
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
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(0, true, withdraw, deposit, amount, taxInPercent, taxAreExclusive, taxDepot);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
		if(taxInPercent <= 0)
		{
			amountToWithdraw = amount;
			amountToDeposit = amount;
		} else if(taxAreExclusive)
		{
			amountToWithdraw = amount + amount*taxInPercent/100;
			amountToDeposit = amount;
		} else
		{
			amountToWithdraw = amount;
			amountToDeposit = amount - amount*taxInPercent/100;
		}
		double amountToTax = amountToWithdraw - amountToDeposit;
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(), taxDepot != null ? taxDepot.getID() : 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, amountToTax,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_TRANSACTION));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amountToWithdraw, withdraw.getBalance(), withdraw.getBalance()-amountToWithdraw));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amountToDeposit, deposit.getBalance(), deposit.getBalance()+amountToDeposit));
		if(taxDepot != null && amountToTax > 0.0)
		{
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					taxDepot.getID(), amountToTax, taxDepot.getBalance(), taxDepot.getBalance()+amountToTax));
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
		}
		withdraw.setBalance(withdraw.getBalance()-amountToWithdraw);
		deposit.setBalance(deposit.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, TA_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		saveAccount(withdraw, deposit, taxDepot);
		return ea;
	}

	public EconomyAction deposit(Account holder, double amount)
	{
		final EconomyAction preCheck = preCheckTransaction(2, true, null, holder, amount, -1.0, true, null);
		if(preCheck != null)
		{
			return preCheck;
		}
		OrdererType ot = OrdererType.PLUGIN;
		String ordererUUIDOrPlugin = "AEP";
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				0, holder.getID(), 0,
				ot,
				ot == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				ot == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amount, amount, 0.0,
				"default",
				ACTIONLOG_DEPOSIT));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), amount, holder.getBalance(), holder.getBalance()+amount));
		holder.setBalance(holder.getBalance()+amount);
		final EconomyAction ea = new EconomyAction(0.0, amount, 0.0, true, D_SUCCESS, ErrorMessageType.SUCCESS,
				0.0, holder.getBalance());
		saveAccount(holder);
		return ea;
	}
	
	public EconomyAction deposit(Account holder, double amount, OrdererType type, String ordererUUIDOrPlugin,
			String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(2, true, null, holder, amount, -1.0, true, null);
		if(preCheck != null)
		{
			return preCheck;
		}
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				0, holder.getID(), 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				0.0, amount, 0.0,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_DEPOSIT));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), amount, holder.getBalance(), holder.getBalance()+amount));
		holder.setBalance(holder.getBalance()+amount);
		final EconomyAction ea = new EconomyAction(0.0, amount, 0.0, true, D_SUCCESS, ErrorMessageType.SUCCESS,
				0.0, holder.getBalance());
		saveAccount(holder);
		return ea;
	}
	
	public EconomyAction deposit(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
	{
		final EconomyAction preCheck = preCheckTransaction(2, true, null, holder, amount, taxInPercent, taxAreExclusive, taxDepot);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
		if(taxInPercent <= 0)
		{
			amountToWithdraw = amount;
			amountToDeposit = amount;
		} else if(taxAreExclusive)
		{
			amountToWithdraw = amount + amount*taxInPercent/100;
			amountToDeposit = amount;
		} else
		{
			amountToWithdraw = amount;
			amountToDeposit = amount - amount*taxInPercent/100;
		}
		double amountToTax = amountToWithdraw - amountToDeposit;
		OrdererType ot = OrdererType.PLUGIN;
		String ordererUUIDOrPlugin = "AEP";
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				0, holder.getID(), taxDepot != null ? taxDepot.getID() : 0,
				ot,
				ot == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				ot == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToDeposit, amountToDeposit, amountToTax,
				"default",
				ACTIONLOG_DEPOSIT));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), amountToDeposit, holder.getBalance(), holder.getBalance()+amountToDeposit));
		if(taxDepot != null)
		{
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					taxDepot.getID(), amountToTax, taxDepot.getBalance(), taxDepot.getBalance()+amountToTax));
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
		}
		saveAccount(holder, taxDepot);
		holder.setBalance(holder.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, D_SUCCESS, ErrorMessageType.SUCCESS,
				0.0, holder.getBalance());
		return ea;
	}
	
	public EconomyAction deposit(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(2, true, null, holder, amount, taxInPercent, taxAreExclusive, taxDepot);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
		if(taxAreExclusive)
		{
			amountToWithdraw = amount + amount*taxInPercent/100;
			amountToDeposit = amount;
		} else
		{
			amountToWithdraw = amount;
			amountToDeposit = amount - amount*taxInPercent/100;
		}
		double amountToTax = amountToWithdraw - amountToDeposit;
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				0, holder.getID(), taxDepot != null ? taxDepot.getID() : 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToDeposit, amountToDeposit, amountToTax,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_DEPOSIT));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), amountToDeposit, holder.getBalance(), holder.getBalance()+amountToDeposit));
		if(taxDepot != null && amountToTax > 0.0)
		{
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					taxDepot.getID(), amountToTax, taxDepot.getBalance(), taxDepot.getBalance()+amountToTax));
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
		}
		holder.setBalance(holder.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, D_SUCCESS, ErrorMessageType.SUCCESS,
				0.0, holder.getBalance());
		saveAccount(holder, taxDepot);
		return ea;
	}
	
	public EconomyAction withdraw(Account holder, double amount)
	{
		final EconomyAction preCheck = preCheckTransaction(1, true, holder, null, amount, -1.0, true, null);
		if(preCheck != null)
		{
			return preCheck;
		}
		OrdererType ot = OrdererType.PLUGIN;
		String ordererUUIDOrPlugin = "AEP";
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				holder.getID(), 0, 0,
				ot,
				ot == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				ot == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amount, amount, 0.0,
				"default",
				ACTIONLOG_WITHDRAW));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), -amount, holder.getBalance(), holder.getBalance()-amount));
		holder.setBalance(holder.getBalance()-amount);
		final EconomyAction ea = new EconomyAction(amount, 0.0, 0.0, true, W_SUCCESS, ErrorMessageType.SUCCESS,
				holder.getBalance(), 0.0);
		saveAccount(holder);
		return ea;
	}
	
	public EconomyAction withdraw(Account holder, double amount, 
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(1, true, holder, null, amount, -1.0, true, null);
		if(preCheck != null)
		{
			return preCheck;
		}
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				holder.getID(), 0, 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amount, 0.0, 0.0,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_WITHDRAW));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), -amount, holder.getBalance(), holder.getBalance()-amount));
		holder.setBalance(holder.getBalance()-amount);
		final EconomyAction ea = new EconomyAction(amount, 0.0, 0.0, true, W_SUCCESS, ErrorMessageType.SUCCESS,
				holder.getBalance(), 0.0);
		saveAccount(holder);
		return ea;
	}
	
	public EconomyAction withdraw(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot)
	{
		final EconomyAction preCheck = preCheckTransaction(1, true, holder, null, amount, taxInPercent, taxAreExclusive, taxDepot);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
		if(taxAreExclusive)
		{
			amountToWithdraw = amount + amount*taxInPercent/100;
			amountToDeposit = amount;
		} else
		{
			amountToWithdraw = amount;
			amountToDeposit = amount - amount*taxInPercent/100;
		}
		double amountToTax = amountToWithdraw - amountToDeposit;
		OrdererType ot = OrdererType.PLUGIN;
		String ordererUUIDOrPlugin = "AEP";
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				holder.getID(), 0, taxDepot != null ? taxDepot.getID() : 0,
				ot,
				ot == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				ot == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, amountToTax,
				"default",
				ACTIONLOG_WITHDRAW));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), -amountToWithdraw, holder.getBalance(), holder.getBalance()-amountToWithdraw));
		if(taxDepot != null)
		{
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					taxDepot.getID(), amountToTax, taxDepot.getBalance(), taxDepot.getBalance()+amountToTax));
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
		}
		holder.setBalance(holder.getBalance()-amountToWithdraw);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, W_SUCCESS, ErrorMessageType.SUCCESS,
				holder.getBalance(), 0.0);
		saveAccount(holder, taxDepot);
		return ea;
	}
	
	public EconomyAction withdraw(Account holder, double amount, double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(1, true, holder, null, amount, taxInPercent, taxAreExclusive, taxDepot);
		if(preCheck != null)
		{
			return preCheck;
		}
		double amountToWithdraw = 0.0;
		double amountToDeposit = 0.0;
		if(taxAreExclusive)
		{
			amountToWithdraw = amount + amount*taxInPercent/100;
			amountToDeposit = amount;
		} else
		{
			amountToWithdraw = amount;
			amountToDeposit = amount - amount*taxInPercent/100;
		}
		double amountToTax = amountToWithdraw - amountToDeposit;
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				holder.getID(), 0, taxDepot != null ? taxDepot.getID() : 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, amountToTax,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_WITHDRAW));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				holder.getID(), -amountToWithdraw, holder.getBalance(), holder.getBalance()-amountToWithdraw));
		if(taxDepot != null && amountToTax > 0.0)
		{
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					taxDepot.getID(), amountToTax, taxDepot.getBalance(), taxDepot.getBalance()+amountToTax));
			taxDepot.setBalance(taxDepot.getBalance()+amountToTax);
		}
		holder.setBalance(holder.getBalance()-amountToWithdraw);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, W_SUCCESS, ErrorMessageType.SUCCESS,
				holder.getBalance(), 0.0);
		saveAccount(holder, taxDepot);
		return ea;
	}
	
	/*public EconomyAction withdraw(Account holder, double amount, LinkedHashMap<ItemStack, Double> possibleItemsWithRelatedValue,
			double taxInPercent, boolean taxAreExclusive, Account taxDepot,
			OrdererType type, String ordererUUIDOrPlugin, String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(1, true, holder, null, amount, taxInPercent, taxAreExclusive, taxDepot);
		if(preCheck != null)
		{
			return preCheck;
		}
		//TODO withdraw for Itembased Currency
		final EconomyAction ea = new EconomyAction(0.0, 0.0, 0.0, false, "", ErrorMessageType.SUCCESS,
				holder.getBalance(), 0.0);
		return ea;
	}*/
	
	public EconomyAction exchangeCurrencies(Account withdraw, Account deposit, double amount)
	{
		final EconomyAction preCheck = preCheckTransaction(0, false, withdraw, deposit, amount, -1.0, true, null);
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
		saveAccount(withdraw, deposit);
		return ea;
	}
	
	public EconomyAction exchangeCurrencies(Account withdraw, Account deposit, double amount,
			OrdererType type, String ordererUUIDOrPlugin,
			String actionLogCategory, String actionLogComment)
	{
		final EconomyAction preCheck = preCheckTransaction(0, false, withdraw, deposit, amount, -1.0, true, null);
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
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(), 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, 0.0,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_EXCHANGE));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amount, withdraw.getBalance(), withdraw.getBalance()-amount));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amount, deposit.getBalance(), deposit.getBalance()+amount));
		withdraw.setBalance(withdraw.getBalance()-amountToWithdraw);
		deposit.setBalance(deposit.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, 0.0, true, EX_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		saveAccount(withdraw, deposit);
		return ea;
	}
	
	public EconomyAction exchangeCurrencies(Account withdraw, Account deposit, double amount,
			double taxInPercent, boolean taxAreExclusive, Account withdrawAccounttaxDepot, Account depositAccounttaxDepot)
	{
		final EconomyAction preCheck = preCheckTransaction(0, false, withdraw, deposit, amount, taxInPercent, taxAreExclusive, withdrawAccounttaxDepot);
		if(preCheck != null)
		{
			return preCheck;
		}
		final EconomyAction preCheckII = preCheckTransaction(0, false, withdraw, deposit, amount, taxInPercent, taxAreExclusive, depositAccounttaxDepot);
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
		final EconomyAction preCheck = preCheckTransaction(0, false, withdraw, deposit, amount, taxInPercent, taxAreExclusive, withdrawAccounttaxDepot);
		if(preCheck != null)
		{
			return preCheck;
		}
		final EconomyAction preCheckII = preCheckTransaction(0, false, withdraw, deposit, amount, taxInPercent, taxAreExclusive, depositAccounttaxDepot);
		if(preCheckII != null)
		{
			return preCheckII;
		}		
		Exchange ex = plugin.getIFHApi().getExchangeOfCurrency(amount, withdraw.getCurrency(), deposit.getCurrency(), taxInPercent,
				taxAreExclusive, withdraw.getCurrency().getTaxationBeforeExchange());
		double amountToWithdraw = ex.toWithdrawAccount;
		double amountToDeposit = ex.toDepositAccount;
		double amountToTax = ex.toTaxAccount;
		LoggerApi.addActionLogger(new ActionLogger(0, System.currentTimeMillis(),
				withdraw.getID(), deposit.getID(),
				amountToTax > 0.0 ? 
						(ex.taxTo == TaxToCurrency.TO_WITHDRAW_CURRENCY 
						? (withdrawAccounttaxDepot != null ? withdrawAccounttaxDepot.getID() : 0) 
						: (depositAccounttaxDepot != null ? depositAccounttaxDepot.getID() : 0)) 
						: 0,
				type,
				type == OrdererType.PLAYER ? UUID.fromString(ordererUUIDOrPlugin) : null,
				type == OrdererType.PLUGIN ? ordererUUIDOrPlugin : null,
				amountToWithdraw, amountToDeposit, amountToTax,
				actionLogCategory != null ? actionLogCategory : "default",
				actionLogComment != null ? actionLogComment : ACTIONLOG_EXCHANGE));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				withdraw.getID(), -amountToWithdraw, withdraw.getBalance(), withdraw.getBalance()-amountToWithdraw));
		LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
				deposit.getID(), amountToDeposit, deposit.getBalance(), deposit.getBalance()+amountToDeposit));
		if(withdrawAccounttaxDepot != null && amountToTax > 0.0 && ex.taxTo == TaxToCurrency.TO_WITHDRAW_CURRENCY)
		{
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					withdrawAccounttaxDepot.getID(), amountToTax, withdrawAccounttaxDepot.getBalance(),
					withdrawAccounttaxDepot.getBalance()+amountToTax));
			withdrawAccounttaxDepot.setBalance(withdrawAccounttaxDepot.getBalance()+amountToTax);
		} else if(depositAccounttaxDepot != null && amountToTax > 0.0 && ex.taxTo == TaxToCurrency.TO_DEPOSIT_CURRENCY)
		{
			LoggerApi.addTrendLogger(new TrendLogger(LocalDate.now(), TrendLogger.Type.STABIL,
					depositAccounttaxDepot.getID(), amountToTax, depositAccounttaxDepot.getBalance(), depositAccounttaxDepot.getBalance()+amountToTax));
			depositAccounttaxDepot.setBalance(depositAccounttaxDepot.getBalance()+amountToTax);
		}
		withdraw.setBalance(withdraw.getBalance()-amountToWithdraw);
		deposit.setBalance(deposit.getBalance()+amountToDeposit);
		final EconomyAction ea = new EconomyAction(amountToWithdraw, amountToDeposit, amountToTax, true, EX_SUCCESS, ErrorMessageType.SUCCESS,
				withdraw.getBalance(), deposit.getBalance());
		saveAccount(withdraw, deposit, withdrawAccounttaxDepot, depositAccounttaxDepot);
		return ea;
	}
}