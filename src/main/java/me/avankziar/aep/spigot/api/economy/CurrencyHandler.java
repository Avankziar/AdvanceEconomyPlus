package main.java.me.avankziar.aep.spigot.api.economy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aep.general.objects.EntityData;
import main.java.me.avankziar.aep.general.objects.TaxationCase;
import main.java.me.avankziar.aep.general.objects.TaxationSet;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.account.AccountType;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.general.economy.currency.SIPrefix;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.currency.Currency;
import main.java.me.avankziar.ifh.spigot.economy.currency.CurrencyGradation;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import main.java.me.avankziar.ifh.spigot.economy.currency.Gradation;

public class CurrencyHandler
{
	private AdvancedEconomyPlus plugin;
	
	protected EconomyCurrency defaultDigitalCurrency;
	private EconomyCurrency defaultExperienceCurrency;
	private EconomyCurrency defaultItemStackCurrency;
	
	public ArrayList<EconomyCurrency> allCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> digitalCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> expCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> itemCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> digitalExchangableCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> expExchangableCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> itemExchangableCurrencies = new ArrayList<>();
	
	public static LinkedHashMap<String, LinkedHashMap<TaxationCase, TaxationSet>> taxationMap = new LinkedHashMap<>();
	
	private static String d1 = "currencyhandler";
	
	public CurrencyHandler(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	public void registerCurrencyFromFile(YamlConfiguration c)
	{
		ConfigHandler.debug(d1, "> registerCurrencyFromFile start : "+c.getCurrentPath());
		if(c.get("UniqueName") == null 
				|| c.get("Gradation.CurrencyType") == null)
		{
			ConfigHandler.debug(d1, "> c.get(UniqueName) == null");
			return;
		}
		CurrencyType ct = CurrencyType.valueOf(c.getString("Gradation.CurrencyType", "DIGITAL"));
		CurrencyGradation cg = null;
		switch(ct)
		{
		case DIGITAL:
			ArrayList<Gradation> gr = new ArrayList<>();
			for(int i = 1; i < 50; i++)
			{
				if(c.get("Gradation.DIGITAL."+i+".Plural") == null)
				{
					break;
				}
				int vtbg = c.getInt("Gradation.DIGITAL."+i+".ValueToBaseGradation", i) == 0 
						? 1 : c.getInt("Gradation.DIGITAL."+i+".ValueToBaseGradation", i);
				gr.add(new Gradation(
							c.getString("Gradation.DIGITAL."+i+".Plural"),
							c.getString("Gradation.DIGITAL."+i+".Singular"),
							c.getString("Gradation.DIGITAL."+i+".Symbol"),
							vtbg));
			}
			if(!gr.isEmpty())
			{
				cg = new CurrencyGradation(
						new Gradation(
								c.getString("Gradation.DIGITAL.Base.Plural"),
								c.getString("Gradation.DIGITAL.Base.Singular"),
								c.getString("Gradation.DIGITAL.Base.Symbol"),
								1),
						gr.toArray(new Gradation[0]));
			} else
			{
				cg = new CurrencyGradation(
						new Gradation(
								c.getString("Gradation.DIGITAL.Base.Plural"),
								c.getString("Gradation.DIGITAL.Base.Singular"),
								c.getString("Gradation.DIGITAL.Base.Symbol"),
								1));
			}
			break;
		case EXPERIENCE:
			//ADDME
			return;
		case ITEMSTACK:
			//ADDME
			return;
		}
		if(cg == null)
		{
			return;
		}
		String uniquename = c.getString("UniqueName");
		if(c.get("Taxation") != null)
		{
			ConfigHandler.debug(d1, "> Taxation != null");
			LinkedHashMap<TaxationCase, TaxationSet> map = new LinkedHashMap<>();
			for(String s : c.getStringList("Taxation"))
			{
				String[] sp = s.split(";");
				if(sp.length != 3)
				{
					ConfigHandler.debug(d1, "> sp.lenght != 3");
					continue;
				}
				ConfigHandler.debug(d1, "> tc: "+sp[0]+" | boolean: '"+sp[1]+"'("+Boolean.parseBoolean(sp[1])+") | double: "+sp[2]);
				try
				{
					TaxationCase tc = TaxationCase.valueOf(sp[0]);
					if(!map.containsKey(tc) && MatchApi.isBoolean(sp[1]) && MatchApi.isDouble(sp[2]))
					{
						ConfigHandler.debug(d1, "> map.containsKey(tc) "+map.containsKey(tc)
							+" || boolean "+Boolean.parseBoolean(sp[1])+" || MatchApi.isDouble "+MatchApi.isDouble(sp[2]));
						map.put(tc, new TaxationSet(Boolean.parseBoolean(sp[1]), Double.parseDouble(sp[2])));
					}
				} catch(Exception e)
				{
					ConfigHandler.debug(d1, "> !tc.valueOf || !boolean || !MatchApi.isDouble");
					continue;
				}
			}
			CurrencyHandler.taxationMap.put(uniquename, map);
		}
		Currency cu = new Currency()
				.setUnique(uniquename)
				.setStandartUnitWorth(c.getDouble("StandartUnitWorth", 1.0))
				.setExchangeable(c.getBoolean("Currency.Exchangable", false))
				.setTaxationBeforeExchange(c.getBoolean("Currency.TaxationBeforeExchange", false))
				.setCurrencyType(CurrencyType.valueOf(c.getString("Gradation.CurrencyType", "DIGITAL")))
				.setCurrencyGradation(cg);
		registerCurrency(cu);
		if(c.getBoolean("DefaultCurrency"))
		{
			if(defaultDigitalCurrency == null)
			{
				defaultDigitalCurrency = cu.toCurrency();
			}			
		}
		plugin.getIFHApi().defaultGradationQuantity.put(uniquename, c.getInt("Format.GradationQuantity"));
		plugin.getIFHApi().defaultUseSIPrefix.put(uniquename, c.getBoolean("Format.UseSIPrefix"));
		plugin.getIFHApi().defaultDecimalPlaces.put(uniquename, c.getInt("Format.DecimalPlaces"));
		plugin.getIFHApi().defaultUseSymbol.put(uniquename, c.getBoolean("Format.UseSymbol"));
		plugin.getIFHApi().defaultThousandSeperator.put(uniquename, c.getString("Format.ThousandSeperator"));
		plugin.getIFHApi().defaultDecimalSeperator.put(uniquename, c.getString("Format.DecimalSeperator"));
		if(c.get("Format.SIPrefix") != null)
		{
			LinkedHashMap<Double, String> map = new LinkedHashMap<>();
			for(String s : c.getStringList("Format.SIPrefix"))
			{
				String[] sp = s.split(";");
				if(sp.length != 2)
				{
					continue;
				}
				try
				{
					SIPrefix si = SIPrefix.valueOf(sp[0]);
					String sc = sp[1];
					map.put(si.getDecimal(), sc);
				} catch(Exception e)
				{
					continue;
				}
			}
			LinkedHashMap<Double, String> sorted = new LinkedHashMap<>();
			map.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
				.forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
			plugin.getIFHApi().defaultSIPrefix.put(uniquename, sorted);
		}
	}
	
	public static void registerServerAndEntityAccountIfNotExist()
	{
		AdvancedEconomyPlus plugin = AdvancedEconomyPlus.getPlugin();
		EconomyType et = EconomyType.SERVER;
		for(String sp : plugin.getYamlHandler().getConfig().getStringList("CreateEconomyEntityIfNotExist.Server"))
		{
			String[] split = sp.split(";"); //Name,EC,ACN,ACT,ACC,AccountDefault,ServerDefault,Global
			if(split.length < 5)
			{
				return;
			}
			String s = split[0];
			EconomyEntity ee = plugin.getIFHApi().getEntity(s, et);
			if(ee == null)
			{
				ee = new EconomyEntity(et, null, s).generateUUID();
				plugin.getMysqlHandler().create(Type.ENTITYDATA, new EntityData(ee.getUUID(), s, ee.getType()));
			}
			EconomyCurrency ec = plugin.getIFHApi().getCurrency(split[1]);
			if(ec == null)
			{
				continue;
			}
			String acn = split[2];
			AccountType act = MatchApi.isAccountType(split[3]) ? AccountType.valueOf(split[3]) : null;
			if(act == null) {continue;}
			AccountCategory acc = MatchApi.isAccountCategory(split[4]) ? AccountCategory.valueOf(split[4]) : null;
			if(acc == null) {continue;}
			Account ac = plugin.getIFHApi().getAccount(ee, acn, act, acc, ec);
			if(ac == null)
			{
				ac = new Account(acn, act, acc, ec, ee, 0, true);
				plugin.getMysqlHandler().create(Type.ACCOUNT, ac);
				if(split.length >= 6)
				{
					boolean b = MatchApi.isBoolean(split[5]) ? Boolean.parseBoolean(split[5]) : false;
					if(b)
					{
						plugin.getIFHApi().setDefaultAccount(ee.getUUID(), ac, acc);
					}
				}
			}
			if(split.length >= 7)
			{
				boolean b = MatchApi.isBoolean(split[6]) ? Boolean.parseBoolean(split[6]) : false;
				if(b)
				{
					plugin.getIFHApi().accountHandler.defaultServer = ee;
				}
			}
			if(split.length >= 8)
			{
				boolean b = MatchApi.isBoolean(split[8]) ? Boolean.parseBoolean(split[8]) : false;
				if(b)
				{
					//TODO Global Account
				}
			}
		}
		et = EconomyType.ENTITY;
		for(String sp : plugin.getYamlHandler().getConfig().getStringList("CreateEconomyEntityIfNotExist.Entity"))
		{
			String[] split = sp.split(";"); //Name,EC,ACN,ACT,ACC,AccountDefault,ServerDefault,Global
			if(split.length < 5)
			{
				return;
			}
			String s = split[0];
			EconomyEntity ee = plugin.getIFHApi().getEntity(s, et);
			if(ee == null)
			{
				ee = new EconomyEntity(et, null, s).generateUUID();
				plugin.getMysqlHandler().create(Type.ENTITYDATA, new EntityData(ee.getUUID(), s, ee.getType()));
			}
			EconomyCurrency ec = plugin.getIFHApi().getCurrency(split[1]);
			if(ec == null)
			{
				continue;
			}
			String acn = split[2];
			AccountType act = MatchApi.isAccountType(split[3]) ? AccountType.valueOf(split[3]) : null;
			if(act == null) {continue;}
			AccountCategory acc = MatchApi.isAccountCategory(split[4]) ? AccountCategory.valueOf(split[4]) : null;
			if(acc == null) {continue;}
			Account ac = plugin.getIFHApi().getAccount(ee, acn, act, acc, ec);
			if(ac == null)
			{
				ac = new Account(acn, act, acc, ec, ee, 0, true);
				plugin.getMysqlHandler().create(Type.ACCOUNT, ac);
				if(split.length >= 6)
				{
					boolean b = MatchApi.isBoolean(split[5]) ? Boolean.parseBoolean(split[5]) : false;
					if(b)
					{
						plugin.getIFHApi().setDefaultAccount(ee.getUUID(), ac, acc);
					}
				}
			}
			if(split.length >= 7)
			{
				boolean b = MatchApi.isBoolean(split[6]) ? Boolean.parseBoolean(split[6]) : false;
				if(b)
				{
					plugin.getIFHApi().accountHandler.defaultServer = ee;
				}
			}
			if(split.length >= 8)
			{
				boolean b = MatchApi.isBoolean(split[8]) ? Boolean.parseBoolean(split[8]) : false;
				if(b)
				{
					//TODO Global Account
				}
			}
		}
	}
	
	public boolean registerCurrency(Currency currency, int gradationQuantity, boolean useSIPrefix, int decimalPlaces, boolean useSymbol,
			String thousandSeperator, String decimalSeperator, LinkedHashMap<Double, String> siPrefix)
	{
		
		registerCurrency(currency);
		String uniquename = currency.toCurrency().getUniqueName();
		plugin.getIFHApi().defaultGradationQuantity.put(uniquename, gradationQuantity);
		plugin.getIFHApi().defaultUseSIPrefix.put(uniquename, useSIPrefix);
		plugin.getIFHApi().defaultDecimalPlaces.put(uniquename, decimalPlaces);
		plugin.getIFHApi().defaultUseSymbol.put(uniquename, useSymbol);
		plugin.getIFHApi().defaultThousandSeperator.put(uniquename, thousandSeperator);
		plugin.getIFHApi().defaultDecimalSeperator.put(uniquename, decimalSeperator);
		LinkedHashMap<Double, String> sorted = new LinkedHashMap<>();
		siPrefix.entrySet()
			.stream()
			.sorted(Map.Entry.comparingByKey())
			.forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
		plugin.getIFHApi().defaultSIPrefix.put(uniquename, sorted);
		return true;
	}

	public boolean existsCurrency(String uniqueName)
	{
		for(EconomyCurrency c : allCurrencies)
		{
			if(c.getUniqueName().equals(uniqueName))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean registerCurrency(Currency currency)
	{
		EconomyCurrency c = currency.toCurrency();
		if(existsCurrency(c.getUniqueName()))
		{
			return false;
		}
		allCurrencies.add(c);
		switch(c.getCurrencyType())
		{
		case DIGITAL:
			digitalCurrencies.add(c);
			if(c.isExchangeable())
			{
				digitalExchangableCurrencies.add(c);
			}
			break;
		case EXPERIENCE:
			expCurrencies.add(c);
			if(c.isExchangeable())
			{
				expExchangableCurrencies.add(c);
			}
			break;
		case ITEMSTACK:
			itemCurrencies.add(c);
			if(c.isExchangeable())
			{
				itemExchangableCurrencies.add(c);
			}
			break;
		}
		return true;
	}

	public EconomyCurrency getCurrency(String uniqueName)
	{
		for(EconomyCurrency c : allCurrencies)
		{
			if(c.getUniqueName().equals(uniqueName))
			{
				return c;
			}
		}
		return null;
	}

	public EconomyCurrency getDefaultCurrency(CurrencyType type)
	{
		switch(type)
		{
		case DIGITAL:
			return this.defaultDigitalCurrency;
		case EXPERIENCE:
			return this.defaultExperienceCurrency;
		case ITEMSTACK:
			return this.defaultItemStackCurrency;
		}
		return null;
	}

	public ArrayList<EconomyCurrency> getCurrencies(CurrencyType type)
	{
		switch(type)
		{
		case DIGITAL:
			return digitalCurrencies;
		case EXPERIENCE:
			return expCurrencies;
		case ITEMSTACK:
			return itemCurrencies;
		}
		return new ArrayList<>();
	}

	public ArrayList<EconomyCurrency> getExchangableCurrencies(CurrencyType type)
	{
		switch(type)
		{
		case DIGITAL:
			return digitalExchangableCurrencies;
		case EXPERIENCE:
			return expExchangableCurrencies;
		case ITEMSTACK:
			return itemExchangableCurrencies;
		}
		return new ArrayList<>();
	}

	public double getTotalMoneyInTheSystem(EconomyCurrency currency)
	{
		return plugin.getMysqlHandler().getCount(MysqlHandler.Type.ACCOUNT, "`id`", "`account_currency` = ?", currency.getUniqueName());
	}

	public double getTotalMoneyInTheSystem(EconomyCurrency currency, AccountType type)
	{
		return plugin.getMysqlHandler().getCount(MysqlHandler.Type.ACCOUNT, "`id`",
				"`account_currency` = ? AND `account_type` = ?",
				currency.getUniqueName(), type.toString());
	}

	public double getTotalMoneyInTheSystem(EconomyCurrency currency, EconomyType entityType)
	{
		return plugin.getMysqlHandler().getCount(MysqlHandler.Type.ACCOUNT, "`id`",
				"`account_currency` = ? AND `owner_type` = ?",
				currency.getUniqueName(), entityType.toString());
	}

	public double getTotalMoneyInTheSystem(EconomyCurrency currency, AccountType type, EconomyType entityType)
	{
		return plugin.getMysqlHandler().getCount(MysqlHandler.Type.ACCOUNT, "`id`",
				"`account_currency` = ? AND `account_type` = ? AND `owner_type` = ?",
				currency.getUniqueName(), type.toString(), entityType.toString());
	}

	public boolean setDefaultCurrency(EconomyCurrency currency)
	{
		switch(currency.getCurrencyType())
		{
		case DIGITAL:
			this.defaultDigitalCurrency = currency;
			break;
		case EXPERIENCE:
			this.defaultExperienceCurrency = currency;
			break;
		case ITEMSTACK:
			this.defaultItemStackCurrency = currency;
			break;
		}
		return true;
	}

}
