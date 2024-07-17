package me.avankziar.aep.spigot.api.economy;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AccountSpigot;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultEcoProvider implements Economy
{
	private AEP plugin;

	public VaultEcoProvider(AEP plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean isEnabled()
	{
		return plugin.getIFHApi().isEnabled();
	}
	
	@Override
	public boolean hasBankSupport()
	{
		return plugin.getIFHApi().hasBankSupport();
	}
	
	@Override
	public String currencyNamePlural()
	{
		return plugin.getIFHApi().currencyHandler.defaultDigitalCurrency.getCurrencyGradation().getHighestGradation().getPlural();
	}

	@Override
	public String currencyNameSingular()
	{
		return plugin.getIFHApi().currencyHandler.defaultDigitalCurrency.getCurrencyGradation().getHighestGradation().getSingular();
	}
	
	@Override
	public String format(double amount)
	{
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setGroupingSeparator(
				plugin.getIFHApi().defaultThousandSeperator.get(
						plugin.getIFHApi().currencyHandler.defaultDigitalCurrency.getUniqueName()).charAt(0));
		symbols.setDecimalSeparator(
				plugin.getIFHApi().defaultDecimalSeperator.get(
						plugin.getIFHApi().currencyHandler.defaultDigitalCurrency.getUniqueName()).charAt(0));
		DecimalFormat df = new DecimalFormat("#,##0.00");
		df.setDecimalFormatSymbols(symbols);
		return df.format(amount);
	}

	@Override
	public int fractionalDigits()
	{
		return plugin.getIFHApi().defaultDecimalPlaces.get(plugin.getIFHApi().currencyHandler.defaultDigitalCurrency.getUniqueName());
	}
	
	@Override
	public String getName()
	{
		return plugin.pluginName;
	}
	
	@Override
	public EconomyResponse createBank(String name, OfflinePlayer player)
	{
		if(!isEnabled())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("TransactionHandler.IS_NOT_ENABLED"));
		}
		if(plugin.getIFHApi().existAccount(player.getUniqueId(), name, plugin.getIFHApi().currencyHandler.defaultDigitalCurrency))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("Bank.NameAlreadyExist"));
		}
		plugin.getIFHApi().createAccount(
				new AccountSpigot(
						name,
						AccountType.BANK,
						AccountCategory.MAIN,
						plugin.getIFHApi().currencyHandler.defaultDigitalCurrency,
						new EconomyEntity(EconomyType.PLAYER, player.getUniqueId(), player.getName()),
						0.0, 
						false));
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getLang().getString("Bank.Create.IsSuccided")
				.replace("%name%", name));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse createBank(String name, String player)
	{
		return createBank(name, Bukkit.getOfflinePlayer(player));
	}
	
	@Override
	public EconomyResponse bankBalance(String name)
	{
		if(!isEnabled())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("TransactionHandler.IS_NOT_ENABLED"));
		}
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getLang().getString("UseIFH"));
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount)
	{
		if(!isEnabled())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("TransactionHandler.IS_NOT_ENABLED"));
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", format(amount)));
		}
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getLang().getString("UseIFH"));
	}

	@Override
	public EconomyResponse bankHas(String name, double amount)
	{
		if(!isEnabled())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("TransactionHandler.IS_NOT_ENABLED"));
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", format(amount)));
		}
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getLang().getString("UseIFH"));
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount)
	{
		if(!isEnabled())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("TransactionHandler.IS_NOT_ENABLED"));
		}
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getLang().getString("UseIFH"));
	}
	
	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer player)
	{
		if(!isEnabled())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("TransactionHandler.IS_NOT_ENABLED"));
		}
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getLang().getString("UseIFH"));
	}

	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse isBankMember(String name, String playerName)
	{
		return isBankMember(name,Bukkit.getOfflinePlayer(playerName));
	}
	
	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player)
	{
		if(!isEnabled())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("TransactionHandler.IS_NOT_ENABLED"));
		}
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getLang().getString("UseIFH"));
	}

	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse isBankOwner(String name, String playerName)
	{
		return isBankOwner(name, Bukkit.getOfflinePlayer(playerName));
	}
	
	@Override
	public List<String> getBanks()
	{
		List<String> list = new ArrayList<>();
		for(Account a : plugin.getIFHApi().getAccounts(AccountType.BANK))
		{
			list.add(a.getAccountName());
		}
		return list;
	}
	
	@Override
	public EconomyResponse deleteBank(String name)
	{
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getLang().getString("UseIFH"));
	}

	@Override
	public boolean hasAccount(OfflinePlayer player)
	{
		return plugin.getIFHApi().existAccount(
				player.getUniqueId(), 
				plugin.getIFHApi().currencyHandler.defaultDigitalCurrency,
				AccountType.WALLET, 
				AccountCategory.MAIN,
				EconomyType.PLAYER);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean hasAccount(String playerName)
	{
		return hasAccount(Bukkit.getOfflinePlayer(playerName));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean hasAccount(String playerName, String worldName)
	{
		return hasAccount(Bukkit.getOfflinePlayer(playerName));
	}

	@Override
	public boolean hasAccount(OfflinePlayer player, String worldName)
	{
		return hasAccount(player);
	}
	
	@Override
	public boolean createPlayerAccount(OfflinePlayer player)
	{
		if(!isEnabled())
		{
			return false;
		}
		if(plugin.getIFHApi().existAccount(player.getUniqueId(), player.getName(), plugin.getIFHApi().currencyHandler.defaultDigitalCurrency))
		{
			return false;
		}
		plugin.getIFHApi().createAccount(
				new AccountSpigot(
						player.getName(),
						AccountType.WALLET,
						AccountCategory.MAIN,
						plugin.getIFHApi().currencyHandler.defaultDigitalCurrency,
						new EconomyEntity(EconomyType.PLAYER, player.getUniqueId(), player.getName()),
						0.0, 
						false));
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean createPlayerAccount(String playerName)
	{
		return createPlayerAccount(Bukkit.getOfflinePlayer(playerName));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean createPlayerAccount(String playerName, String worldName)
	{
		return createPlayerAccount(Bukkit.getOfflinePlayer(playerName));
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player, String worldName)
	{
		return createPlayerAccount(player);
	}
	
	@Override
	public EconomyResponse depositPlayer(final OfflinePlayer player, final double amount)
	{
		if(!isEnabled())
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("TransactionHandler.IS_NOT_ENABLED"));
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", format(amount)));
		}
		Account account = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN);
		if(account == null)
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("NoPlayerAccount"));
		}
		/*if(ConfigHandler.isLoanRetakeEnabled()) FIXME
		{
			if(plugin.getMysqlHandler().exist(MysqlType.LOAN,
					"`from_player` = ? AND `forgiven` = ? AND `paused` = ? AND `finished` = ? AND `endtime` < ?", // from_player existiert nicht
					player.getName(), false, false, false, System.currentTimeMillis()))
			{
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlType.LOAN, 
								"`from_player` = ? AND `forgiven` = ? AND `paused` = ? AND `finished` = ? AND `endtime` < ?",
							player.getName(), false, false, false, System.currentTimeMillis());
						double topay = amount;
						double sum = dr.getTotalAmount()-dr.getAmountPaidSoFar();
						if(sum < topay)
						{
							topay = sum;
						}
						retakeForLoan(dr, sum);
					}
				}.runTaskLater(plugin, 20L*2);
			}
		}*/
		account.setBalance(account.getBalance()+amount);
		double b = account.getBalance();
		plugin.getIFHApi().saveAccount(account);
		return new EconomyResponse(amount, b, EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getLang().getString("Wallet.Deposit"));
	}

	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse depositPlayer(String playerName, double amount)
	{
		return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
	}

	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount)
	{
		return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount)
	{
		return depositPlayer(player, amount);
	}

	@Override
	public double getBalance(OfflinePlayer player)
	{
		if(!isEnabled())
		{
			return 0;
		}
		Account acc = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN);
		return acc != null ? acc.getBalance() : 0.0;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public double getBalance(String playerName)
	{
		return getBalance(Bukkit.getOfflinePlayer(playerName));
	}

	@SuppressWarnings("deprecation")
	@Override
	public double getBalance(String playerName, String world)
	{
		return getBalance(Bukkit.getOfflinePlayer(playerName));
	}

	@Override
	public double getBalance(OfflinePlayer player, String world)
	{
		return getBalance(player);
	}
	
	@Override
	public boolean has(OfflinePlayer player, double amount)
	{
		return plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN) != null ?
				(plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN).getBalance() >= amount
				? true : false) : false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean has(String playerName, double amount)
	{
		return has(Bukkit.getOfflinePlayer(playerName), amount);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean has(String playerName, String worldName, double amount)
	{
		return has(Bukkit.getOfflinePlayer(playerName), amount);
	}

	@Override
	public boolean has(OfflinePlayer player, String worldName, double amount)
	{
		return has(player, amount);
	}
	
	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount)
	{
		if(!isEnabled())
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("TransactionHandler.IS_NOT_ENABLED"));
		}
		Account account = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN);
		if(account == null)
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getLang().getString("NoPlayerAccount"));
		}
		account.setBalance(account.getBalance()-amount);
		double b = account.getBalance();
		plugin.getIFHApi().saveAccount(account);
		return new EconomyResponse(amount, b, EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getLang().getString("Wallet.Withdraw"));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse withdrawPlayer(String playerName, double amount)
	{
		return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount)
	{
		return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount)
	{
		return withdrawPlayer(player, amount);
	}
	
	protected void retakeForLoan(LoanRepayment lr, double amount) //TODO muss ich schauen, wie ich es ändern.
	{
		Account from = plugin.getIFHApi().getAccount(lr.getAccountFromID());
		Account to = plugin.getIFHApi().getAccount(lr.getAccountToID());
		if(from == null || to == null)
		{
			return;
		}
		String category = plugin.getYamlHandler().getLang().getString("LoanRepayment.CategoryII", null);
		String comment = plugin.getYamlHandler().getLang().getString("LoanRepayment.CommentII", null);
		if(comment != null)
		{
			comment = comment.replace("%name%", lr.getName())
					.replace("%totalpaid%", plugin.getIFHApi().format(
							lr.getAmountPaidSoFar()+lr.getLoanAmount(), from.getCurrency()))
					.replace("%waitingamount%", plugin.getIFHApi().format(
							lr.getTotalAmount()-lr.getAmountPaidSoFar()-lr.getLoanAmount(), from.getCurrency()));
		}
		Account tax = plugin.getIFHApi().getDefaultAccount(to.getOwner().getUUID(), AccountCategory.TAX, to.getCurrency());
		double taxation = lr.getTaxInDecimal();
		boolean taxAreExclusive = (lr.getLoanAmount()+lr.getLoanAmount()*lr.getInterest()+lr.getLoanAmount()*taxation) > lr.getTotalAmount() ? true : false;
		
		EconomyAction ea = null;
		if(from.getCurrency().getUniqueName().equals(to.getCurrency().getUniqueName()))
		{
			if(tax == null && category != null)
			{
				ea = plugin.getIFHApi().transaction(
						from, to, amount,
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			} else if(tax != null && category != null)
			{
				ea = plugin.getIFHApi().transaction(
						from, to, amount,
						taxation, taxAreExclusive, tax, 
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			}
			if(!ea.isSuccess())
			{
				return;
			}
			plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", lr.getId());
		} else
		{
			if(!from.getCurrency().isExchangeable() || !to.getCurrency().isExchangeable())
			{
				return;
			}
			Account taxII = plugin.getIFHApi().getDefaultAccount(to.getOwner().getUUID(), AccountCategory.TAX, to.getCurrency());
			if(tax == null && taxII == null && category != null)
			{
				ea = plugin.getIFHApi().exchangeCurrencies(
						from, to, amount,
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			} else if(tax != null && taxII == null && category != null)
			{
				ea = plugin.getIFHApi().exchangeCurrencies(
						from, to, amount,
						taxation, taxAreExclusive, tax, taxII,
						OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
			}
			plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", lr.getId());
		}
		
		lr.setAmountPaidSoFar(lr.getAmountPaidSoFar()+lr.getAmountRatio()-lr.getTaxInDecimal()*lr.getAmountRatio());
		lr.setAmountPaidToTax(lr.getAmountPaidToTax()+lr.getTaxInDecimal()*lr.getAmountRatio());
		
		if(lr.getTotalAmount() <= lr.getAmountPaidSoFar()+lr.getAmountPaidToTax())
		{
			lr.setFinished(true);
		}
		plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", lr.getId());
	}

}