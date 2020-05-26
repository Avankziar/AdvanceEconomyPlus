package main.java.me.avankziar.advanceeconomy.spigot.api;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.Utility;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.object.BankAccount;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultApi implements Economy
{
	private AdvanceEconomy plugin;
	//return new EconomyResponse(Utility.getMoneyFormat(amount), 0.0, EconomyResponse.ResponseType.SUCCESS, "");

	public VaultApi(AdvanceEconomy plugin)
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
		if(isMysql() && useBank())
		{
			return true;
		}
		return false;
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
		return "TheNewEconomy";
	}
	
	@Override
	public EconomyResponse createBank(String name, OfflinePlayer player)
	{
		if(isMysql() && useBank())
		{
			String banknumber = BankAccount.getFreeBankAccountNumber();
			if(banknumber == null)
			{
				return null;
			}
			OfflinePlayer offlineplayer = player.getPlayer();
			BankAccount.createBankAccount(plugin,banknumber, name, offlineplayer.getUniqueId().toString());
			//Du hast ein Bankkonto mit dem Namen &f%name% &eerstellt!
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,
					plugin.getYamlHandler().getL().getString("CmdBank.Create.IsSuccided")
					.replace("%name%", name));
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
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
		if(isMysql() && useBank())
		{
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString(
								"CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			////%name% &eGuthaben: &f%balance% %currency%
			return new EconomyResponse(0.0, ba.getBalance(), EconomyResponse.ResponseType.SUCCESS,
					plugin.getYamlHandler().getL().getString("CmdBank.BankBalance")
					.replace("%name%", name)
					.replace("%balance%", format(ba.getBalance()))
					.replace("%currency%", currencyNamePlural()));
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount)
	{
		if(isMysql() && useBank())
		{
			if(!MatchApi.isPositivNumber(amount))
			{
				return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("NumberIsNegativ")
						.replace("%args%", format(amount)));
			}
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
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
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}

	@Override
	public EconomyResponse bankHas(String name, double amount)
	{
		if(isMysql() && useBank())
		{
			if(!MatchApi.isPositivNumber(amount))
			{
				return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("NumberIsNegativ")
						.replace("%args%", format(amount)));
			}
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			if(ba.getBalance() >= amount)
			{
				//%name% &eGuthaben: &f%balance% %currency%
				return new EconomyResponse(0.0, ba.getBalance(), EconomyResponse.ResponseType.SUCCESS,
						plugin.getYamlHandler().getL().getString("CmdBank.BankBalance")
						.replace("%name%", name)
						.replace("%balance%", format(ba.getBalance()))
						.replace("%currency%", currencyNamePlural()));
			} else
			{
				//%name% &eGuthaben: &f%balance% %currency%
				return new EconomyResponse(0.0, ba.getBalance(), EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankBalance")
						.replace("%name%", name)
						.replace("%balance%", format(ba.getBalance()))
						.replace("%currency%", currencyNamePlural()));
			}
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount)
	{
		if(isMysql() && useBank())
		{
			if(!MatchApi.isPositivNumber(amount))
			{
				return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("NumberIsNegativ")
						.replace("%args%", format(amount)));
			}
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			if(ba.getBalance() >= amount)
			{
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
			} else
			{
				return new EconomyResponse(0.0, ba.getBalance(), EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankBalance")
						.replace("%name%", name)
						.replace("%balance%", format(ba.getBalance()))
						.replace("%currency%", currencyNamePlural()));
			}
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName)
	{
		if(isMysql() && useBank())
		{
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			
			if(ba.getMemberUUID().contains(Utility.convertNameToUUID(playerName)))
			{
				return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,null);
			}
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,null);
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}

	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer player)
	{
		if(isMysql() && useBank())
		{
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			if(ba.getMemberUUID().contains(player.getUniqueId().toString()))
			{
				return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,null);
			}
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,null);
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}
	
	public EconomyResponse isBankVice(String name, String playerName)
	{
		if(isMysql() && useBank())
		{
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			if(ba.getViceUUID().contains(Utility.convertNameToUUID(playerName)))
			{
				return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,null);
			}
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,null);
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}

	public EconomyResponse isBankVice(String name, OfflinePlayer player)
	{
		if(isMysql() && useBank())
		{
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			if(ba.getViceUUID().contains(player.getUniqueId().toString()))
			{
				return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,null);
			}
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,null);
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName)
	{
		if(isMysql() && useBank())
		{
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			if(ba.getOwnerUUID().equals(Utility.convertNameToUUID(playerName)))
			{
				return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,null);
			}
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,null);
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}

	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player)
	{
		if(isMysql() && useBank())
		{
			BankAccount ba = null;
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			if(ba.getOwnerUUID().equals(player.getUniqueId().toString()))
			{
				return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,null);
			}
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,null);
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}
	
	@Override
	public List<String> getBanks()
	{
		List<String> list = new ArrayList<>();
		int end = plugin.getMysqlHandler().lastID(MysqlHandler.Type.BANKACCOUNT);
		for(BankAccount ba : BankAccount.convertList(
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
			if(MatchApi.isBankAccountNumber(name))
			{
				ba = BankAccount.getBankAccount(name);
			} else if(!BankAccount.existMoreBankAccountsWithTheSameName(name))
			{
				ba = BankAccount.getBankAccountFromName(name);
			} else
			{
				//Es existieren mehrere Banken mit dem Namen! Oder die angegebene Kontonummer ist nicht gültig!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.NameExistMoreThanTwice"));
			}
			if(ba == null)
			{
				//Die angegebene Bank existiert nicht!
				new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
						plugin.getYamlHandler().getL().getString("CmdBank.BankNotExist"));
			}
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.BANKACCOUNT, "`id` = ?", ba.getId());
			new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.SUCCESS,
					plugin.getYamlHandler().getL().getString("CmdBank.BankDeleted")
					.replace("%name%", name));
		}
		//Bankkontos sind nicht aktiv!
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
				plugin.getYamlHandler().getL().getString("NoBank"));
	}

	@Override
	public boolean hasAccount(OfflinePlayer player)
	{
		if(EcoPlayer.getEcoPlayer(player) != null)
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
			if(EcoPlayer.getEcoPlayer(player) != null)
			{
				return false;
			}
			double amount = 0.0;
			if(plugin.getYamlHandler().get().get("StartMoney") != null)
			{
				amount = plugin.getYamlHandler().get().getDouble("StartMoney");
			}
			EcoPlayer eco = new EcoPlayer(0, player.getUniqueId().toString(), player.getName(),
					amount, new ArrayList<String>(), true, true, true, null);
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
	public EconomyResponse depositPlayer(OfflinePlayer player, double amount)
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
		EcoPlayer eco = EcoPlayer.getEcoPlayer(player.getUniqueId().toString());
		if(eco == null)
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoPlayerAccount"));
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
		EcoPlayer eco = EcoPlayer.getEcoPlayer(player);
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
			EcoPlayer eco = EcoPlayer.getEcoPlayer(player);
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
		EcoPlayer eco = EcoPlayer.getEcoPlayer(player);
		if(eco == null)
		{
			return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE,
					plugin.getYamlHandler().getL().getString("NoPlayerAccount"));
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

}