package main.java.me.avankziar.aep.spigot.api;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler.BankAccountHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.aep.spigot.object.BankAccount;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultApi implements Economy
{
	private AdvancedEconomyPlus plugin;

	public VaultApi(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	private boolean isMysql()
	{
		return EconomySettings.settings.isMysql();
	}
	
	private boolean usePlayerAccount()
	{
		return EconomySettings.settings.usePlayerAccount();
	}
	
	private boolean useBank()
	{
		return EconomySettings.settings.useBank();
	}
	
	@Override
	public boolean isEnabled()
	{
		if(isMysql())
		{
			if(usePlayerAccount() || useBank())
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasBankSupport()
	{
		if(!isMysql() || !useBank())
		{
			return false;
		}
		return true;
	}
	
	@Override
	public String currencyNamePlural()
	{
		return EconomySettings.settings.getCurrencyPlural();
	}

	@Override
	public String currencyNameSingular()
	{
		return EconomySettings.settings.getCurrencySingular();
	}
	
	@Override
	public String format(double amount)
	{
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		DecimalFormat df = new DecimalFormat("#,##0.00");
		df.setDecimalFormatSymbols(symbols);
		return df.format(amount);
	}

	@Override
	public int fractionalDigits()
	{
		return EconomySettings.settings.getMoneyFormat();
	}
	
	@Override
	public String getName()
	{
		return "AdvanceEconomy";
	}
	
	@Override
	public EconomyResponse createBank(String name, OfflinePlayer player)
	{
		if(!isMysql() || !useBank())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoBank"));
		}
		String banknumber = BankAccountHandler.getFreeBankAccountNumber();
		if(banknumber == null)
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,
					plugin.getYamlHandler().getL().getString("CmdBank.Create.NoBankAccountFree")
					.replace("%name%", name));
		}
		OfflinePlayer offlineplayer = player.getPlayer();
		BankAccountHandler.createBankAccount(plugin,banknumber, name, offlineplayer.getUniqueId().toString());
		//Du hast ein Bankkonto mit dem Namen &f%name% &eerstellt!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getL().getString("CmdBank.Create.IsSuccided")
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
		if(!isMysql() || !useBank())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoBank"));
		}
		BankAccount ba = null;
		Object o = getBank(ba, name);
		if(o instanceof EconomyResponse) return (EconomyResponse) o;
		ba = (BankAccount) o;
		////%name% &eGuthaben: &f%balance% %currency%
		return new EconomyResponse(0.0, ba.getBalance(), EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getL().getString("CmdBank.BankBalance")
				.replace("%name%", name)
				.replace("%balance%", format(ba.getBalance()))
				.replace("%currency%", currencyNamePlural()));
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount)
	{
		if(!isMysql() || !useBank())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoBank"));
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", format(amount)));
		}
		BankAccount ba = null;
		Object o = getBank(ba, name);
		if(o instanceof EconomyResponse) return (EconomyResponse) o;
		ba = (BankAccount) o;
		double newbalance = ba.getBalance() + amount;
		ba.setBalance(newbalance);
		plugin.getMysqlHandler().updateData(Type.BANKACCOUNT, ba, "`id` = ? AND `accountnumber` = ?",
				ba.getId(), ba.getaccountNumber());
		//%amount% &e%currency% wurde auf %name% überwiesen.
		return new EconomyResponse(amount, ba.getBalance(), EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getL().getString("CmdBank.BankDeposit")
				.replace("%name%", name)
				.replace("%balance%", format(ba.getBalance()))
				.replace("%currency%", currencyNamePlural())
				.replace("%amount%", format(amount)));
	}

	@Override
	public EconomyResponse bankHas(String name, double amount)
	{
		if(!isMysql() || !useBank())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoBank"));
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", format(amount)));
		}
		BankAccount ba = null;
		Object o = getBank(ba, name);
		if(o instanceof EconomyResponse) return (EconomyResponse) o;
		ba = (BankAccount) o;
		if(ba.getBalance() < amount)
		{
			//%name% &eGuthaben: &f%balance% %currency%
			return new EconomyResponse(0.0, ba.getBalance(), EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("CmdBank.BankBalance")
					.replace("%name%", name)
					.replace("%balance%", format(ba.getBalance()))
					.replace("%currency%", currencyNamePlural()));
			
		}
		//%name% &eGuthaben: &f%balance% %currency%
		return new EconomyResponse(0.0, ba.getBalance(), EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getL().getString("CmdBank.BankBalance")
				.replace("%name%", name)
				.replace("%balance%", format(ba.getBalance()))
				.replace("%currency%", currencyNamePlural()));
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount)
	{
		if(!isMysql() || !useBank())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoBank"));
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", format(amount)));
		}
		BankAccount ba = null;
		Object o = getBank(ba, name);
		if(o instanceof EconomyResponse) return (EconomyResponse) o;
		ba = (BankAccount) o;
		if(ba.getBalance() < amount)
		{
			return new EconomyResponse(0.0, ba.getBalance(), EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("CmdBank.BankBalance")
					.replace("%name%", name)
					.replace("%balance%", format(ba.getBalance()))
					.replace("%currency%", currencyNamePlural()));
		}
		double newbalance = ba.getBalance() - amount;
		ba.setBalance(newbalance);
		plugin.getMysqlHandler().updateData(Type.BANKACCOUNT, ba, "`id` = ? AND `accountnumber` = ?",
				ba.getId(), ba.getaccountNumber());
		return new EconomyResponse(amount, ba.getBalance(), EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getL().getString("CmdBank.BankWithDraw")
				.replace("%name%", name)
				.replace("%balance%", format(ba.getBalance()))
				.replace("%currency%", currencyNamePlural())
				.replace("%amount%", format(amount)));
	}
	
	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer player)
	{
		if(!isMysql() || !useBank())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoBank"));
		}
		BankAccount ba = null;
		Object o = getBank(ba, name);
		if(o instanceof EconomyResponse) return (EconomyResponse) o;
		ba = (BankAccount) o;
		if(ba.getMemberUUID().contains(player.getUniqueId().toString()))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,null);
		}
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,null);
	}

	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse isBankMember(String name, String playerName)
	{
		return isBankMember(name,Bukkit.getOfflinePlayer(playerName));
	}
	
	public EconomyResponse isBankVice(String name, OfflinePlayer player)
	{
		if(!isMysql() || !useBank())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoBank"));
		}
		BankAccount ba = null;
		Object o = getBank(ba, name);
		if(o instanceof EconomyResponse) return (EconomyResponse) o;
		ba = (BankAccount) o;
		if(ba.getViceUUID().contains(player.getUniqueId().toString()))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,null);
		}
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,null);
	}
	
	@SuppressWarnings("deprecation")
	public EconomyResponse isBankVice(String name, String playerName)
	{
		return isBankVice(name,Bukkit.getOfflinePlayer(playerName));
	}
	
	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player)
	{
		if(!isMysql() || !useBank())
		{
			//Bankkontos sind nicht aktiv!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoBank"));
		}
		BankAccount ba = null;
		Object o = getBank(ba, name);
		if(o instanceof EconomyResponse) return (EconomyResponse) o;
		ba = (BankAccount) o;
		if(ba.getOwnerUUID().equals(player.getUniqueId().toString()))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,null);
		}
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,null);
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
		int end = plugin.getMysqlHandler().lastID(MysqlHandler.Type.BANKACCOUNT);
		for(BankAccount ba : ConvertHandler.convertListII(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.BANKACCOUNT,
				"`id`", 1, end)))
		{
			if(ba != null)
			{
				list.add(ba.getaccountNumber());
			}
		}
		return list;
	}
	
	@Override
	public EconomyResponse deleteBank(String name)
	{
		if(isMysql() && useBank())
		{
			BankAccount ba = null;
			Object o = getBank(ba, name);
			if(o instanceof EconomyResponse) return (EconomyResponse) o;
			ba = (BankAccount) o;
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.BANKACCOUNT, "`id` = ?", ba.getId());
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,
					plugin.getYamlHandler().getL().getString("CmdBank.BankDeleted")
					.replace("%name%", name));
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}
	
	private Object getBank(BankAccount bank, String name)
	{
		if(MatchApi.isBankAccountNumber(name))
		{
			bank = BankAccountHandler.getBankAccount(name);
		} else if(!BankAccountHandler.existMoreBankAccountsWithTheSameName(name))
		{
			bank = BankAccountHandler.getBankAccountFromName(name);
		} else
		{
			//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
		}
		if(bank == null)
		{
			//Die angegebene Bank existiert nicht!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
		}
		return bank;
	}

	@Override
	public boolean hasAccount(OfflinePlayer player)
	{
		if(EcoPlayerHandler.getEcoPlayer(player) != null)
		{
			return true;
		}
		return false;
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
		if(isMysql() && usePlayerAccount())
		{
			if(EcoPlayerHandler.getEcoPlayer(player) != null)
			{
				return false;
			}
			double amount = 0.0;
			if(plugin.getYamlHandler().getConfig().get("StartMoney") != null)
			{
				amount = plugin.getYamlHandler().getConfig().getDouble("StartMoney");
			}
			EcoPlayer eco = new EcoPlayer(0, player.getUniqueId().toString(), player.getName(),
					amount, new ArrayList<String>(), true, true, true, null, false);
			plugin.getMysqlHandler().create(Type.PLAYER, eco);
			return true;
		}
		return false;
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
		if(!isMysql() || !usePlayerAccount())
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoPlayerAccount"));
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", format(amount)));
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(player.getUniqueId().toString());
		if(eco == null)
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoPlayerAccount"));
		}
		if(eco.isFrozen())
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("CmdMoney.Freeze.TheAccountIsFrozen"));
		}
		if(EconomySettings.settings.isLoanRepayment())
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
		}
		double newbalance = eco.getBalance() + amount;
		eco.setBalance(newbalance);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYER, eco, "`id` = ?", eco.getId());
		//%amount% &e%currency% wurde auf %name% überwiesen.
		return new EconomyResponse(amount, eco.getBalance(), EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getL().getString("CmdMoney.PlayerDeposit")
				.replace("%name%", eco.getName())
				.replace("%balance%", format(eco.getBalance()))
				.replace("%currency%", currencyNamePlural())
				.replace("%amount%", format(amount)));
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
		if(!isMysql() || !usePlayerAccount())
		{
			return 0;
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(player);
		if(eco == null)
		{
			return 0.0;
		}
		return eco.getBalance();
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
		if(isMysql() && usePlayerAccount())
		{
			EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(player);
			if(eco == null)
			{
				return false;
			}
			if(eco.getBalance() >= amount)
			{
				return true;
			}
		}
		return false;
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
		if(!isMysql() || !usePlayerAccount())
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoPlayerAccount"));
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(player);
		if(eco == null)
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoPlayerAccount"));
		}
		if(eco.isFrozen())
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("CmdMoney.Freeze.TheAccountIsFrozen"));
		}
		double newbalance = eco.getBalance() - amount;
		eco.setBalance(newbalance);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYER, eco, "`id` = ?", eco.getId());
		//%amount% &e%currency% wurde vom %name% abgezogen. Aktuelles Guthaben: &2%balance%'
		return new EconomyResponse(amount, eco.getBalance(), EconomyResponse.ResponseType.SUCCESS,
				plugin.getYamlHandler().getL().getString("CmdMoney.PlayerWithDraw")
				.replace("%name%", player.getName())
				.replace("%balance%", format(eco.getBalance()))
				.replace("%currency%", currencyNamePlural())
				.replace("%amount%", format(amount)));
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
	
	private void retakeForLoan(LoanRepayment dr, double sum)
	{
		String from = "";
		String to = "";
		EcoPlayer ecofrom = EcoPlayerHandler.getEcoPlayer(dr.getFrom());
		EcoPlayer ecoto = EcoPlayerHandler.getEcoPlayer(dr.getTo());
		try
		{
			from = Utility.convertUUIDToName(dr.getFrom());
			to = Utility.convertUUIDToName(dr.getTo());
		} catch (IOException e)
		{
			return;
		}
		if(from == null || to == null)
		{
			return;
		}
		EconomyResponse er = AdvancedEconomyPlus.getVaultApi().withdrawPlayer(
				Bukkit.getOfflinePlayer(UUID.fromString(dr.getFrom())), sum);
		if(!er.transactionSuccess())
		{
			return;
		}
		EconomyResponse err = AdvancedEconomyPlus.getVaultApi().depositPlayer(
				Bukkit.getOfflinePlayer(UUID.fromString(dr.getTo())), sum);	
		if(!err.transactionSuccess())
		{
			AdvancedEconomyPlus.getVaultApi().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(dr.getFrom())), sum);
			return;
		}
		double totalamountPaidSoFar = dr.getAmountPaidSoFar()+sum;
		dr.setAmountPaidSoFar(totalamountPaidSoFar);
		if(dr.getTotalAmount() <= dr.getAmountPaidSoFar())
		{
			dr.setFinished(true);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(), dr.getFrom(), dr.getTo(),
				from, to, plugin.getYamlHandler().getL().getString("LoanRepayment.Orderer"), dr.getAmountRatio(), 
				ActionLoggerEvent.Type.DEPOSIT_WITHDRAW, 
				plugin.getYamlHandler().getL().getString("LoanRepayment.Comment").replace("%name%", dr.getName())));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
				LocalDate.now(), dr.getFrom(), -dr.getAmountRatio(), ecofrom.getBalance()));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), dr.getTo(), dr.getAmountRatio(), ecoto.getBalance()));
		
		Player player = Bukkit.getPlayer(ecofrom.getUUID());
		if(player != null)
		{
			player.sendMessage(plugin.getYamlHandler().getL().getString(""));
		}
	}

}