package main.java.me.avankziar.aep.spigot.api.economy;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountType;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultApi implements Economy
{
	private AdvancedEconomyPlus plugin;

	public VaultApi(AdvancedEconomyPlus plugin)
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
				new Account(
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
				new Account(
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
		/*if(AEPSettings.settings.isLoanRepayment())
		{
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN,
					"`from_player` = ? AND `forgiven` = ? AND `paused` = ? AND `finished` = ? AND `endtime` < ?",
					player.getName(), false, false, false, System.currentTimeMillis()))
			{
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, 
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
		return plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN) != null ?
				plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN).getBalance() : 0.0;
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
	
	protected void retakeForLoan(LoanRepayment dr, double sum) //TODO muss ich schauen, wie ich es ändern.
	{
		String from = Utility.convertUUIDToName(dr.getFrom(), EconomyType.PLAYER);
		String to = Utility.convertUUIDToName(dr.getTo(), EconomyType.PLAYER);
		OLD_AEPUser ecofrom = _AEPUserHandler_OLD.getEcoPlayer(dr.getFrom());
		OLD_AEPUser ecoto = _AEPUserHandler_OLD.getEcoPlayer(dr.getTo());
		if(from == null || to == null)
		{
			return;
		}
		EconomyResponse er = AdvancedEconomyPlus.getVault().withdrawPlayer(
				Bukkit.getOfflinePlayer(UUID.fromString(dr.getFrom())), sum);
		if(!er.transactionSuccess())
		{
			return;
		}
		EconomyResponse err = AdvancedEconomyPlus.getVault().depositPlayer(
				Bukkit.getOfflinePlayer(UUID.fromString(dr.getTo())), sum);	
		if(!err.transactionSuccess())
		{
			AdvancedEconomyPlus.getVault().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(dr.getFrom())), sum);
			return;
		}
		double totalamountPaidSoFar = dr.getAmountPaidSoFar()+sum;
		dr.setAmountPaidSoFar(totalamountPaidSoFar);
		if(dr.getTotalAmount() <= dr.getAmountPaidSoFar())
		{
			dr.setFinished(true);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
		//FIXME
		/*
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(), dr.getFrom(), dr.getTo(),
				from, to, plugin.getYamlHandler().getLang().getString("LoanRepayment.Orderer"), dr.getAmountRatio(), 
				ActionLoggerEvent.Type.DEPOSIT_WITHDRAW, 
				plugin.getYamlHandler().getLang().getString("LoanRepayment.Comment").replace("%name%", dr.getName())));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
				LocalDate.now(), dr.getFrom(), -dr.getAmountRatio(), ecofrom.getBalance()));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), dr.getTo(), dr.getAmountRatio(), ecoto.getBalance()));
		*/
		
		Player player = Bukkit.getPlayer(ecofrom.getUUID());
		if(player != null)
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString(""));
		}
	}

}