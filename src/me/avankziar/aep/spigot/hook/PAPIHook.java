package me.avankziar.aep.spigot.hook;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.Utility;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class PAPIHook extends me.clip.placeholderapi.expansion.PlaceholderExpansion
{
	private AEP plugin;
	private List<AccountCategory> acy = new ArrayList<>();
	
	public PAPIHook(AEP plugin)
	{
		this.plugin = plugin;
		acy = new ArrayList<AccountCategory>(EnumSet.allOf(AccountCategory.class));
	}
	
	@Override
	public boolean persist()
	{
		return true;
	}
	
	@Override
	public boolean canRegister()
	{
		return true;
	}
	
	@Override
	public String getAuthor()
	{
		return plugin.getDescription().getAuthors().toString();
	}
	
	@Override
	public String getIdentifier()
	{
		return "aep";
	}
	
	@Override
	public String getVersion()
	{
		return plugin.getDescription().getVersion();
	}
	
	/*
	 * [var] : balance(if nothing is specified), accountid, accountname, accountcategory, accounttype, 
	 * <format> : withoutformat(if nothing is specified), withformat
	 * 
	 * >Gesamtes Guthaben des Spieler pro Währung. Ohne Void und Steueraccounts
	 * aep_playerbalance_<format>,<currencyuniquename>
	 * 
	 * >Gesamtes Guthaben des Spieler pro AccountCategory pro Währung.
	 * aep_playerbalancecategory_<format>,<accountcategory>,<currencyuniquename>
	 * 
	 * >Gesamtes Guthaben des Spieler pro Währung. Mit Void und Steueraccounts
	 * aep_playerbalancewithtaxvoid_<format>,<currencyuniquename>
	 * 
	 * >Guthaben des Defaultaccounts mit der AccountCategory und der Währung. Optional andere Variabeln erhältlich
	 * aep_defaultaccount_<format>,<accountcategory>,<currencyuniquename>,[var]
	 * 
	 * >Guthaben des QuickpayAccounts. Mit der Währung. Optional andere Variabeln erhältlich
	 * aep_quickpayaccount_<format>,<currencyuniquename>,[var]
	 * 
	 * >Gesamtes Guthaben im system per Währung
	 * aep_totalbalance_<format>,<currencyuniquename>
	 * 
	 * >Gesamtes Guthaben im System, per Accountcategoriy und Währung.
	 * aep_totalbalance_<format>,<accountcategory>,<currencyuniquename>
	 * 
	 * >Top 10 Spielerguthaben ohne TAX und VOID für die default Digitale Währung. Mit und ohne Format.
	 * Ausgabe NamedesSpielers:Guthaben.
	 * aep_top_<1 bis 10>_<format>
	 */
	
	@Override
	public String onPlaceholderRequest(Player player, String idf)
	{
		if(player == null)
		{
			return "";
		}
		final UUID uuid = player.getUniqueId();
		if(idf.startsWith("playerbalancewithtaxvoid"))
		{
			String[] s = idf.split(",");
			String[] t = s[0].split("_");
			boolean withformat = t.length >= 2 ? t[1].equals("withformat") : false;
			EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[1]);
			if(ecy == null)
			{
				return null;
			}
			double amount = plugin.getMysqlHandler().getSumII(MysqlType.ACCOUNT_SPIGOT,
					"`balance`",
					"`owner_uuid` = ? AND `account_currency` = ?",
					player.getUniqueId().toString(), ecy.getUniqueName(),
					AccountCategory.TAX.toString(), AccountCategory.VOID.toString());
			return withformat ? plugin.getIFHApi().format(amount, ecy) : String.valueOf(amount);
		} else if(idf.startsWith("playerbalancecategory"))
		{
			String[] s = idf.split(",");
			String[] t = s[0].split("_");
			boolean withformat = t.length >= 2 ? t[1].equals("withformat") : false;
			AccountCategory acc = AccountCategory.MAIN;
			try
			{
				acc = AccountCategory.valueOf(s[1]);
			} catch(Exception e) {}
			EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[2]);
			if(ecy == null)
			{
				return null;
			}
			double amount = plugin.getMysqlHandler().getSumII(MysqlType.ACCOUNT_SPIGOT,
					"`balance`",
					"`owner_uuid` = ? AND `account_currency` = ? AND `account_category` = ?",
					player.getUniqueId().toString(), ecy.getUniqueName(), acc.toString());
			return withformat ? plugin.getIFHApi().format(amount, ecy) : String.valueOf(amount);
		} else if(idf.startsWith("playerbalance"))
		{
			String[] s = idf.split(",");
			String[] t = s[0].split("_");
			boolean withformat = t.length >= 2 ? t[1].equals("withformat") : false;
			EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[1]);
			if(ecy == null)
			{
				return null;
			}
			double amount = plugin.getMysqlHandler().getSumII(MysqlType.ACCOUNT_SPIGOT, 
					"`balance`",
					"`owner_uuid` = ? AND `account_currency` = ? AND `account_category` != ? AND `account_category` != ?",
					player.getUniqueId().toString(), ecy.getUniqueName(),
					AccountCategory.TAX.toString(), AccountCategory.VOID.toString());
			return withformat ? plugin.getIFHApi().format(amount, ecy) : String.valueOf(amount);
		} else if(idf.startsWith("defaultaccount"))
		{
			String[] s = idf.split(",");
			String var = "balance";
			if(s.length == 4)
			{
				var = s[3];
			} 
			String[] t = s[0].split("_");
			boolean withformat = t.length >= 2 ? t[1].equals("withformat") : false;
			EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[2]);
			if(ecy == null)
			{
				return null;
			}
			for(AccountCategory acy : this.acy)
			{
				if(s[1].equalsIgnoreCase(acy.toString()))
				{
					Account ac = plugin.getIFHApi().getDefaultAccount(uuid, acy, ecy);
					if(ac == null)
					{
						return null;
					}
					switch(var)
					{
					case "balance":
						return withformat ? plugin.getIFHApi().format(ac.getBalance(), ecy) : String.valueOf(ac.getBalance());
					case "accountid":
						return String.valueOf(ac.getID());
					case "accountname":
						return ac.getAccountName();
					case "accountcategory":
						return ac.getCategory().toString();
					case "accounttype":
						return ac.getType().toString();
					}
				}
			}
		} else if(idf.startsWith("quickpayaccount"))
		{
			String[] s = idf.split(",");
			String var = "balance";
			if(s.length == 3)
			{
				var = s[2];
			} 
			String[] t = s[0].split("_");
			boolean withformat = t.length >= 2 ? t[1].equals("withformat") : false;
			EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[1]);
			if(ecy == null)
			{
				return null;
			}
			Account ac = plugin.getIFHApi().getAccount(plugin.getIFHApi().getQuickPayAccount(ecy, uuid));
			if(ac == null)
			{
				return null;
			}
			switch(var)
			{
			case "balance":
				return withformat ? plugin.getIFHApi().format(ac.getBalance(), ecy) : String.valueOf(ac.getBalance());
			case "accountid":
				return String.valueOf(ac.getID());
			case "accountname":
				return ac.getAccountName();
			case "accountcategory":
				return ac.getCategory().toString();
			case "accounttype":
				return ac.getType().toString();
			}
		} else if(idf.startsWith("totalbalance"))
		{
			String[] s = idf.split(",");
			if(s.length == 2)
			{
				String[] t = s[0].split("_");
				boolean withformat = t.length >= 2 ? t[1].equals("withformat") : false;
				EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[1]);
				if(ecy == null)
				{
					return null;
				}
				double amount = plugin.getMysqlHandler().getSumII(MysqlType.ACCOUNT_SPIGOT, "`balance`", "`account_currency` = ?", ecy.getUniqueName());
				return withformat ? plugin.getIFHApi().format(amount, ecy) : String.valueOf(amount);
			} else if(s.length == 3)
			{
				String[] t = s[0].split("_");
				boolean withformat = t.length >= 2 ? t[1].equals("withformat") : false;
				EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[2]);
				if(ecy == null)
				{
					return null;
				}
				for(AccountCategory acy : this.acy)
				{
					if(s[1].equalsIgnoreCase(acy.toString()))
					{
						double amount = plugin.getMysqlHandler().getSumII(MysqlType.ACCOUNT_SPIGOT, "`balance`", "`account_category` = ?`account_currency` = ?",
								acy.toString(), ecy.getUniqueName());
						return withformat ? plugin.getIFHApi().format(amount, ecy) : String.valueOf(amount);
					}
				}
			}			
		} else if(idf.startsWith("top"))
		{
			String[] s = idf.split("_");
			int place = s.length >= 2 ? (MatchApi.isInteger(s[1]) ? Integer.valueOf(s[1]) : 1) : 1;
			if(place > 10 || place < 1)
			{
				place = 1;
			}
			boolean withformat = s.length >= 3 ? s[2].equals("withformat") : false;
			EconomyCurrency currency = plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL);
			ArrayList<Object[]> l = plugin.getMysqlHandler().getTop10Balance(currency.getUniqueName());
			if(l.size() < place)
			{
				return "/";
			}
			Object[] o = l.get(place-1);
			String uuidplace = (String) o[0];
			String name = Utility.convertUUIDToName(uuidplace, EconomyType.PLAYER);
			double tb = (double) o[1];
			return withformat ? name+":"+plugin.getIFHApi().format(tb, currency) : name+":"+tb;
		}
		return null;
	}
}