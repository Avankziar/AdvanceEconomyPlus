package me.avankziar.aep.bungee.api.economy;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.avankziar.aep.bungee.AEP;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AccountBungee;
import me.avankziar.aep.general.objects.EntityData;
import me.avankziar.aep.general.objects.TaxationCase;
import me.avankziar.aep.general.objects.TaxationSet;
import me.avankziar.ifh.bungee.economy.account.Account;
import me.avankziar.ifh.bungee.economy.currency.Currency;
import me.avankziar.ifh.bungee.economy.currency.CurrencyGradation;
import me.avankziar.ifh.bungee.economy.currency.EconomyCurrency;
import me.avankziar.ifh.bungee.economy.currency.Gradation;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.general.economy.currency.SIPrefix;

public class CurrencyHandler
{
	private AEP plugin;
	
	protected EconomyCurrency defaultDigitalCurrency;
	private EconomyCurrency defaultExperienceCurrency;
	private EconomyCurrency defaultItemStackCurrency;
	
	public static ArrayList<EconomyCurrency> allCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> digitalCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> expCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> itemCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> digitalExchangableCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> expExchangableCurrencies = new ArrayList<>();
	public ArrayList<EconomyCurrency> itemExchangableCurrencies = new ArrayList<>();
	
	public static LinkedHashMap<String, LinkedHashMap<TaxationCase, TaxationSet>> taxationMap = new LinkedHashMap<>();
	
	public CurrencyHandler(AEP plugin)
	{
		this.plugin = plugin;
	}
	
	public void registerCurrencyFromFile(YamlDocument c)
	{
		if(c.get("UniqueName") == null 
				|| c.get("Gradation.CurrencyType") == null)
		{
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
				gr.add(new Gradation(
							c.getString("Gradation.DIGITAL."+i+".Plural"),
							c.getString("Gradation.DIGITAL."+i+".Singular"),
							c.getString("Gradation.DIGITAL."+i+".Symbol"),
							c.getInt("Gradation.DIGITAL."+i+".ValueToBaseGradation", i+1)));
			}
			if(!gr.isEmpty())
			{
				cg = new CurrencyGradation(
						new Gradation(
								c.getString("Gradation.DIGITAL.Base.Plural"),
								c.getString("Gradation.DIGITAL.Base.Singular"),
								c.getString("Gradation.DIGITAL.Base.Symbol"),
								1),
						(Gradation[]) gr.toArray());
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
			LinkedHashMap<TaxationCase, TaxationSet> map = new LinkedHashMap<>();
			for(String s : c.getStringList("Taxation"))
			{
				String[] sp = s.split(";");
				if(sp.length != 3)
				{
					continue;
				}
				try
				{
					TaxationCase tc = TaxationCase.valueOf(sp[0]);
					if(!map.containsKey(tc) && Boolean.getBoolean(sp[1]) && MatchApi.isDouble(sp[2]))
					{
						map.put(tc, new TaxationSet(Boolean.getBoolean(sp[1]), Double.parseDouble(sp[2])));
					}
				} catch(Exception e)
				{
					continue;
				}
			}
			if(!taxationMap.containsKey(uniquename))
			{
				taxationMap.put(uniquename, map);
			}
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
			defaultDigitalCurrency = cu.toCurrency();
		}
		plugin.getIFHApi().defaultGradationQuantity.put(uniquename, c.getInt("Format.GradationQuantity"));
		plugin.getIFHApi().defaultUseSIPrefix.put(uniquename, c.getBoolean("UseSIPrefix"));
		plugin.getIFHApi().defaultDecimalPlaces.put(uniquename, c.getInt("DecimalPlaces"));
		plugin.getIFHApi().defaultUseSymbol.put(uniquename, c.getBoolean("UseSymbol"));
		plugin.getIFHApi().defaultThousandSeperator.put(uniquename, c.getString("ThousandSeperator"));
		plugin.getIFHApi().defaultDecimalSeperator.put(uniquename, c.getString("DecimalSeperator"));
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
				.sorted(Map.Entry.comparingByKey())
				.forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
			plugin.getIFHApi().defaultSIPrefix.put(uniquename, sorted);
		}
	}
	
	public void registerServerAndEntityAccountIfNotExist()
	{
		EconomyType et = EconomyType.SERVER;
		for(String sp : plugin.getYamlHandler().getConfig().getStringList("CreateEconomyEntityIfNotExist.Server"))
		{
			String[] split = sp.split(";");
			String s = split[0];
			EconomyEntity ee = plugin.getIFHApi().getEntity(s, et);
			if(ee == null)
			{
				ee = new EconomyEntity(et, null, s).generateUUID();
				plugin.getMysqlHandler().create(MysqlType.ENTITYDATA, new EntityData(ee.getUUID(), s, ee.getType()));
			}
			List<AccountCategory> cat = new ArrayList<AccountCategory>(EnumSet.allOf(AccountCategory.class));
			for(EconomyCurrency ec : plugin.getIFHApi().getCurrencies(CurrencyType.DIGITAL))
			{
				for(AccountCategory c : cat)
				{
					Account ac = plugin.getIFHApi().getDefaultAccount(ee.getUUID(), c, ec);
					if(ac == null)
					{
						AccountBungee acc = new AccountBungee(s+c.toString(), AccountType.BANK, c, ec, ee, 0, true);
						plugin.getMysqlHandler().create(MysqlType.ACCOUNT_BUNGEE, acc);
						plugin.getIFHApi().setDefaultAccount(ee.getUUID(), acc, c);
					}
				}
			}
			if(split.length == 2)
			{
				boolean b = Boolean.parseBoolean(split[1]);
				if(b)
				{
					plugin.getIFHApi().accountHandler.defaultServer = ee;
				}
			}
		}
		et = EconomyType.ENTITY;
		for(String sp : plugin.getYamlHandler().getConfig().getStringList("CreateEconomyEntityIfNotExist.Entity"))
		{
			String[] split = sp.split(";");
			String s = split[0];
			EconomyEntity ee = plugin.getIFHApi().getEntity(s, EconomyType.SERVER);
			if(ee == null)
			{
				ee = new EconomyEntity(EconomyType.SERVER, null, s).generateUUID();
				plugin.getMysqlHandler().create(MysqlType.ENTITYDATA, new EntityData(ee.getUUID(), s, ee.getType()));
			}
			List<AccountCategory> cat = new ArrayList<AccountCategory>(EnumSet.allOf(AccountCategory.class));
			for(EconomyCurrency ec : plugin.getIFHApi().getCurrencies(CurrencyType.DIGITAL))
			{
				for(AccountCategory c : cat)
				{
					Account ac = plugin.getIFHApi().getDefaultAccount(ee.getUUID(), c, ec);
					if(ac == null)
					{
						AccountBungee acc = new AccountBungee(s+c.toString(), AccountType.BANK, c, ec, ee, 0, true);
						plugin.getMysqlHandler().create(MysqlType.ACCOUNT_BUNGEE, acc);
						plugin.getIFHApi().setDefaultAccount(ee.getUUID(), acc, c);
					}
				}
			}
			if(split.length == 2)
			{
				boolean b = Boolean.parseBoolean(split[1]);
				if(b)
				{
					plugin.getIFHApi().accountHandler.defaultEntity = ee;
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
	
	public boolean registerCurrency(Currency currency)
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
		return plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_BUNGEE, "`id`", "`account_currency` = ?", currency.getUniqueName());
	}

	public double getTotalMoneyInTheSystem(EconomyCurrency currency, AccountType type)
	{
		return plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_BUNGEE, "`id`",
				"`account_currency` = ? AND `account_type` = ?",
				currency.getUniqueName(), type.toString());
	}

	public double getTotalMoneyInTheSystem(EconomyCurrency currency, EconomyType entityType)
	{
		return plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_BUNGEE, "`id`",
				"`account_currency` = ? AND `owner_type` = ?",
				currency.getUniqueName(), entityType.toString());
	}

	public double getTotalMoneyInTheSystem(EconomyCurrency currency, AccountType type, EconomyType entityType)
	{
		return plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_BUNGEE, "`id`",
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