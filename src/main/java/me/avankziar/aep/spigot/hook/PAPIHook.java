package main.java.me.avankziar.aep.spigot.hook;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PAPIHook extends PlaceholderExpansion
{
	private AdvancedEconomyPlus plugin;
	private List<AccountCategory> acy = new ArrayList<>();
	
	public PAPIHook(AdvancedEconomyPlus plugin)
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
		return plugin.pluginName.toLowerCase();
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
	 * playerbalance_<format>,<currencyuniquename>
	 * 
	 * >Gesamtes Guthaben des Spieler pro AccountCategory pro Währung.
	 * playerbalancecategory_<format>,<accountcategory>,<currencyuniquename>
	 * 
	 * >Gesamtes Guthaben des Spieler pro Währung. Mit Void und Steueraccounts
	 * playerbalancewithtaxvoid_<format>,<currencyuniquename>
	 * 
	 * >Guthaben des Defaultaccounts mit der AccountCategory und der Währung. Optional andere Variabeln erhältlich
	 * defaultaccount_<format>,<accountcategory>,<currencyuniquename>,[var]
	 * 
	 * >Guthaben des QuickpayAccounts. Mit der Währung. Optional andere Variabeln erhältlich
	 * quickpayaccount_<format>,<currencyuniquename>,[var]
	 * 
	 * >Gesamtes Guthaben im system per Währung
	 * totalbalance_<format>,<currencyuniquename>
	 * 
	 * >Gesamtes Guthaben im System, per Accountcategoriy und Währung.
	 * totalbalance_<format>,<accountcategory>,<currencyuniquename>
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
			EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[2]);
			if(ecy == null)
			{
				return null;
			}
			double amount = plugin.getMysqlHandler().getSum(Type.ACCOUNT, 
					"`owner_uuid` = ? AND `account_currency` = ?",
					player.getUniqueId().toString(), ecy.getUniqueName(), AccountCategory.TAX.toString(), AccountCategory.VOID.toString());
			return withformat ? plugin.getIFHApi().format(amount, ecy) : String.valueOf(amount);
		} else if(idf.startsWith("playerbalancecategory"))
		{
			String[] s = idf.split(",");
			String[] t = s[0].split("_");
			boolean withformat = t.length >= 2 ? t[1].equals("withformat") : false;
			AccountCategory acc = AccountCategory.MAIN;
			try
			{
				acc = AccountCategory.valueOf(s[2]);
			} catch(Exception e) {}
			EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[3]);
			if(ecy == null)
			{
				return null;
			}
			double amount = plugin.getMysqlHandler().getSum(Type.ACCOUNT, 
					"`owner_uuid` = ? AND `account_currency` = ?  AND `account_category` = ?",
					player.getUniqueId().toString(), ecy.getUniqueName(), acc.toString());
			return withformat ? plugin.getIFHApi().format(amount, ecy) : String.valueOf(amount);
		} else if(idf.startsWith("playerbalance"))
		{
			String[] s = idf.split(",");
			String[] t = s[0].split("_");
			boolean withformat = t.length >= 2 ? t[1].equals("withformat") : false;
			EconomyCurrency ecy = plugin.getIFHApi().getCurrency(s[2]);
			if(ecy == null)
			{
				return null;
			}
			double amount = plugin.getMysqlHandler().getSum(Type.ACCOUNT, 
					"`owner_uuid` = ? AND `account_currency` = ? AND `account_category` != ? AND `account_category` != ?",
					player.getUniqueId().toString(), ecy.getUniqueName(), AccountCategory.TAX.toString(), AccountCategory.VOID.toString());
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
				double amount = plugin.getMysqlHandler().getSum(Type.ACCOUNT, "`account_currency` = ?", ecy.getUniqueName());
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
						double amount = plugin.getMysqlHandler().getSum(Type.ACCOUNT, "`account_category` = ?`account_currency` = ?",
								acy.toString(), ecy.getUniqueName());
						return withformat ? plugin.getIFHApi().format(amount, ecy) : String.valueOf(amount);
					}
				}
			}			
		}		
		return null;
	}
}
