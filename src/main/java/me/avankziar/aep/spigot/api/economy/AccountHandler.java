package main.java.me.avankziar.aep.spigot.api.economy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.general.objects.AccountManagement;
import main.java.me.avankziar.aep.general.objects.DefaultAccount;
import main.java.me.avankziar.aep.general.objects.EntityData;
import main.java.me.avankziar.aep.general.objects.QuickPayAccount;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.general.economy.account.AccountType;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class AccountHandler
{
	private AdvancedEconomyPlus plugin;
	protected EconomyEntity defaultServer;
	protected EconomyEntity defaultEntity;
	
	private static String d1 = "accounthandler";
	
	public AccountHandler(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	public static void createAllCurrencyAccounts(Player player)
	{
		ConfigHandler.debug(d1, "> createAllCurrencyAccounts start : "+player.getName());
		AdvancedEconomyPlus plugin = AdvancedEconomyPlus.getPlugin();
		for(EconomyCurrency ec : plugin.getIFHApi().getCurrencies(CurrencyType.DIGITAL))
		{
			ConfigHandler.debug(d1, "> Currency : "+ec.getUniqueName());
			EconomyEntity.EconomyType eeet = EconomyType.PLAYER;
			EconomyEntity ee = new EconomyEntity(eeet, player.getUniqueId(), player.getName());
			YamlConfiguration y = plugin.getYamlHandler().getCurrency(ec.getUniqueName());
			boolean quickpayAlreadyCreated = false;
			if(y.getBoolean("WhenPlayerFirstJoin.CreateWallets", false))
			{
				ConfigHandler.debug(d1, "> CreateWallets");
				AccountType at = AccountType.WALLET;
				for(String a : y.getStringList("WhenPlayerFirstJoin.WalletsToCreate"))
				{
					ConfigHandler.debug(d1, "> WalletsToCreate : "+a);
					String[] s = a.split(";");
					if(s.length < 3)
					{
						ConfigHandler.debug(d1, "> s.length < 3 : continue");
						continue;
					}
					String acname = s[0]
							.replace("%player%", player.getName())
							.replace(" ", "");
					AccountCategory acy = AccountCategory.valueOf(s[1]);
					if(acy == null)
					{
						acy = AccountCategory.MAIN;
					}
					int j = 0;
					while(plugin.getMysqlHandler().exist(MysqlHandler.Type.ACCOUNT, 
							"`owner_uuid` = ? AND `account_name` = ?", ee.getUUID().toString(), acname))
					{
						acname = s[0].replace("%player%", player.getName())+j;
						j++;
					}
					boolean defaultac = MatchApi.isBoolean(s[2]) ? Boolean.valueOf(s[2]) : false;
					double startamount = MatchApi.isDouble(s[3]) ? Double.parseDouble(s[3]) : 0.0;
					ArrayList<AccountManagementType> amtl = new ArrayList<>();
					for(int i = 4; i < s.length; i++)
					{
						try
						{
							AccountManagementType amt = AccountManagementType.valueOf(s[i]);
							amtl.add(amt);
						} catch(Exception e)
						{
							ConfigHandler.debug(d1, "> AccountManagementType Exception : "+s[i]+" | Exit");
							continue;
						}
					}
					Account ac = new Account(acname, at, acy, ec, ee, startamount, true);
					if(plugin.getIFHApi().existAccount(ee.getUUID(), ec, ac.getType(), acy, eeet))
					{
						ConfigHandler.debug(d1, "> Account already exist, Exit : "
								+ee.getUUID().toString()+", "+ec.getUniqueName()+", "+ac.getType()+", "+acy.toString()+", "+eeet.toString());
						continue;
					}
					ConfigHandler.debug(d1, "> Account is created!");
					plugin.getMysqlHandler().create(Type.ACCOUNT, ac);
					ac = plugin.getIFHApi().getAccount(ee, acname, at, acy, ec);
					if(defaultac)
					{
						ConfigHandler.debug(d1, "> Create Defaultaccount");
						plugin.getIFHApi().setDefaultAccount(ee.getUUID(), ac, acy);
					}
					for(AccountManagementType amt : amtl)
					{
						if(!plugin.getIFHApi().canManageAccount(ac, ee.getUUID(), amt))
						{
							plugin.getIFHApi().addManagementTypeToAccount(ac, ee.getUUID(), amt);
						}
					}
					if(!quickpayAlreadyCreated)
					{
						QuickPayAccount qpa = new QuickPayAccount(player.getUniqueId(), ac.getID(), ec.getUniqueName());
						plugin.getMysqlHandler().create(MysqlHandler.Type.QUICKPAYACCOUNT, qpa);
						quickpayAlreadyCreated = true;
					}
				}
			}
			if(y.getBoolean("WhenPlayerFirstJoin.CreateBanks", false))
			{
				AccountType at = AccountType.BANK;
				for(String a : y.getStringList("WhenPlayerFirstJoin.BanksToCreate"))
				{
					String[] s = a.split(";");
					if(s.length < 3)
					{
						continue;
					}
					String acname = s[0]
							.replace("%player%", player.getName())
							.replace(" ", "");
					AccountCategory acy = AccountCategory.valueOf(s[1]);
					if(acy == null)
					{
						acy = AccountCategory.MAIN;
					}
					int j = 0;
					while(plugin.getMysqlHandler().exist(MysqlHandler.Type.ACCOUNT, 
							"`owner_uuid` = ? AND `account_name` = ?", ee.getUUID().toString(), acname))
					{
						acname = s[0].replace("%player%", player.getName())+j;
						j++;
					}
					boolean defaultac = MatchApi.isBoolean(s[2]) ? Boolean.valueOf(s[2]) : false;
					double startamount = MatchApi.isDouble(s[3]) ? Double.parseDouble(s[3]) : 0.0;
					ArrayList<AccountManagementType> amtl = new ArrayList<>();
					for(int i = 4; i < s.length; i++)
					{
						try
						{
							AccountManagementType amt = AccountManagementType.valueOf(s[i]);
							amtl.add(amt);
						} catch(Exception e)
						{
							continue;
						}
					}
					Account ac = new Account(acname, at, acy, ec, ee, startamount, defaultac);
					if(plugin.getIFHApi().existAccount(ee.getUUID(), ec, ac.getType(), acy, eeet))
					{
						continue;
					}
					plugin.getMysqlHandler().create(Type.ACCOUNT, ac);
					ac = plugin.getIFHApi().getAccount(ee, acname, at, acy, ec);
					if(defaultac)
					{
						plugin.getIFHApi().setDefaultAccount(ee.getUUID(), ac, acy);
					}
					for(AccountManagementType amt : amtl)
					{
						if(!plugin.getIFHApi().canManageAccount(ac, ee.getUUID(), amt))
						{
							plugin.getIFHApi().addManagementTypeToAccount(ac, ee.getUUID(), amt);
						}
					}
					if(!quickpayAlreadyCreated)
					{
						QuickPayAccount qpa = new QuickPayAccount(player.getUniqueId(), ac.getID(), ec.getUniqueName());
						plugin.getMysqlHandler().create(MysqlHandler.Type.QUICKPAYACCOUNT, qpa);
						quickpayAlreadyCreated = true;
					}
				}
			}
		}
	}
	
	public EconomyEntity getEntity(UUID uuid)
	{
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", uuid.toString()))
		{
			AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", uuid.toString());
			return user == null ? null : new EconomyEntity(EconomyEntity.EconomyType.PLAYER, user.getUUID(), user.getName());
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.SERVER.toString()))
		{
			EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
					"`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.SERVER.toString());
			return ed == null ? null : new EconomyEntity(EconomyEntity.EconomyType.SERVER, ed.getUUID(), ed.getName());
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.ENTITY.toString()))
		{
			EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
					"`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.ENTITY.toString());
			return ed == null ? null : new EconomyEntity(EconomyEntity.EconomyType.ENTITY, ed.getUUID(), ed.getName());
		}
		return null;
	}
	
	public EconomyEntity getEntity(UUID uuid, EconomyEntity.EconomyType type)
	{
		EconomyEntity ee = null;
		switch(type)
		{
		case ENTITY:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.SERVER.toString()))
			{
				EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
						"`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.SERVER.toString());
				if(ed != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.ENTITY, ed.getUUID(), ed.getName());
				}
			}
			break;
		case PLAYER:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", uuid.toString()))
			{
				AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", uuid.toString());
				if(user != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.PLAYER, user.getUUID(), user.getName());
				}
			}
			break;
		case SERVER:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.ENTITY.toString()))
			{
				EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
						"`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.ENTITY.toString());
				if(ed != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.SERVER, ed.getUUID(), ed.getName());
				}
			}
		}
		return ee;
	}

	public EconomyEntity getEntity(String name)
	{
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", name))
		{
			AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", name);
			return user == null ? null : new EconomyEntity(EconomyEntity.EconomyType.PLAYER, user.getUUID(), name);
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.SERVER.toString()))
		{
			EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
					"`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.SERVER.toString());
			return ed == null ? null : new EconomyEntity(EconomyEntity.EconomyType.SERVER, ed.getUUID(), name);
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.ENTITY.toString()))
		{
			EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
					"`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.ENTITY.toString());
			return ed == null ? null : new EconomyEntity(EconomyEntity.EconomyType.ENTITY, ed.getUUID(), name);
		}
		return null;
	}
	
	public EconomyEntity getEntity(String name, EconomyEntity.EconomyType type)
	{
		EconomyEntity ee = null;
		switch(type)
		{
		case ENTITY:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.ENTITY.toString()))
			{
				EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
						"`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.ENTITY.toString());
				if(ed != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.ENTITY, ed.getUUID(), ed.getName());
				}
			}
			break;
		case PLAYER:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", name))
			{
				AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", name);
				if(user != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.PLAYER, user.getUUID(), user.getName());
				}
			}
			break;
		case SERVER:
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.SERVER.toString()))
			{
				EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlHandler.Type.ENTITYDATA,
						"`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.SERVER.toString());
				if(ed != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.SERVER, ed.getUUID(), ed.getName());
				}
			}
		}
		return ee;
	}
	
	public ArrayList<EconomyEntity> getEntitys(EconomyEntity.EconomyType type)
	{
		try
		{
			switch(type)
			{
			case ENTITY:
			case SERVER:
				return ConvertHandler.convertList0(plugin.getMysqlHandler().getAllListAt(Type.ENTITYDATA, "`id` ASC", "`entity_type` = ?", type.toString()));
			case PLAYER:
				return ConvertHandler.convertList0(plugin.getMysqlHandler().getAllListAt(Type.ACCOUNT, "`id` ASC", "1"));
			}
		} catch(Exception e)
		{
			return new ArrayList<>();
		}
		return new ArrayList<>();
	}
	
	public EconomyEntity getDefaultServer()
	{
		return defaultServer;
	}
	
	public EconomyEntity getDefaultEntity() 
	{
		return defaultEntity;
	}
	
	public boolean existAccount(UUID uuid, String accountName)
	{
		return plugin.getMysqlHandler().exist(MysqlHandler.Type.ACCOUNT, 
				"`owner_uuid` = ? AND `account_name` = ?",
				uuid.toString(), accountName);
	}
	
	public boolean existAccount(UUID uuid, String accountName, EconomyCurrency currency)
	{
		return plugin.getMysqlHandler().exist(MysqlHandler.Type.ACCOUNT, 
				"`owner_uuid` = ? AND `account_currency` = ? AND `account_name` = ?",
				uuid.toString(), currency.getUniqueName(), accountName);
	}
	
	public boolean existAccount(UUID uuid, EconomyCurrency currency, AccountType type, AccountCategory category,
			EconomyEntity.EconomyType entityType)
	{
		return plugin.getMysqlHandler().exist(MysqlHandler.Type.ACCOUNT, 
				"`owner_uuid` = ? AND `account_currency` = ? AND `account_type` = ? AND `account_category` = ? AND `owner_type` = ?",
				uuid.toString(), currency.getUniqueName(), type.toString(), category.toString(), entityType.toString());
	}
	
	public boolean existAccount(UUID uuid, String accountName, EconomyCurrency currency, AccountType type, AccountCategory category,
			EconomyEntity.EconomyType entityType)
	{
		return plugin.getMysqlHandler().exist(MysqlHandler.Type.ACCOUNT, 
				"`owner_uuid` = ? AND `account_name` = ? AND `account_currency` = ? AND `account_type` = ? AND `account_category` = ? AND `owner_type` = ?",
				uuid.toString(), accountName, currency.getUniqueName(), type.toString(), category.toString(), entityType.toString());
	}
	
	public boolean createAccount(Account account)
	{
		if(existAccount(account.getOwner().getUUID(), account.getAccountName(), account.getCurrency(), account.getType(), account.getCategory(), account.getOwner().getType()))
		{
			return false;
		}
		return plugin.getMysqlHandler().create(MysqlHandler.Type.ACCOUNT, account);
	}
	
	public boolean createAccount(String accountName, AccountType type, AccountCategory category, EconomyCurrency currency,
			EconomyEntity owner, double balance)
	{
		if(existAccount(owner.getUUID(), currency, type, category, owner.getType()))
		{
			return false;
		}
		return plugin.getMysqlHandler().create(MysqlHandler.Type.ACCOUNT, 
				new Account(accountName, type, category, currency, owner, balance, false));
	}
	
	public int deleteAccount(Account account)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`owner_uuid` = ? AND `account_currency` = ? AND `account_type` = ? AND `account_category` = ? AND `owner_type` = ?",
				account.getOwner().getUUID().toString(), account.getCurrency(), account.getType().toString(), account.getCategory().toString(), account.getOwner().getType().toString());
	}
	
	public int deleteAllAccounts(EconomyCurrency currency)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`account_currency` = ?",
				currency.getUniqueName());
	}
	
	public int deleteAllAccounts(UUID uuid)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`owner_uuid` = ?",
				uuid);
	}

	public int deleteAllAccounts(UUID uuid, AccountType type)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`owner_uuid` = ? AND `account_type` = ?",
				uuid.toString(), type);
	}
	
	public int deleteAllAccounts(UUID uuid, EconomyEntity.EconomyType entityType)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`owner_uuid` = ? AND `owner_type` = ?",
				uuid.toString(), entityType);
	}
	
	public int deleteAllAccounts(UUID uuid, EconomyEntity.EconomyType entityType, AccountType type)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`owner_uuid` = ? AND `account_type` = ? AND `owner_type` = ?",
				uuid.toString(), type.toString(), entityType.toString());
	}
	
	public int deleteAllAccounts(AccountType type)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`account_type` = ?",
				type.toString());
	}

	public int deleteAllAccounts(AccountCategory category)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`account_category` = ?",
				category);
	}

	public int deleteAllAccounts(EconomyEntity.EconomyType entityType)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`owner_type` = ?",
				entityType.toString());
	}

	public int deleteAllAccounts(EconomyEntity.EconomyType entityType, AccountType type)
	{
		return plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT,
				"`account_type` = ? AND `owner_type` = ?",
				type.toString(), entityType.toString());
	}
	
	public Account getAccount(int id)
	{
		Object o = plugin.getMysqlHandler().getData(MysqlHandler.Type.ACCOUNT, "`id` = ?", id);
		return o == null ? null : (Account) o;
	}
	
	public Account getAccount(UUID ownerUUID, String accountName)
	{
		if(!existAccount(ownerUUID, accountName))
		{
			return null;
		}
		Object o = plugin.getMysqlHandler().getData(MysqlHandler.Type.ACCOUNT,
				  "`owner_uuid` = ? AND `account_name` = ?",
				ownerUUID.toString(), accountName);
		return o == null ? null : (Account) o;
	}
	
	public Account getAccount(UUID ownerUUID, String accountName, EconomyEntity.EconomyType ownerEntityType, AccountType type, AccountCategory category,
			EconomyCurrency currency)
	{
		Object o = plugin.getMysqlHandler().getData(MysqlHandler.Type.ACCOUNT,
				  "`owner_uuid` = ? AND `account_name` = ? AND `account_currency` = ? AND `account_type` = ? AND "
				+ "`account_category` = ? AND `owner_type` = ?",
				ownerUUID.toString(), accountName, currency.getUniqueName(), type.toString(), category.toString(), ownerEntityType.toString());
		return o == null ? null : (Account) o;
	}
	
	public Account getAccount(EconomyEntity owner, String accountName, AccountType type, AccountCategory category, EconomyCurrency currency)
	{
		Object o = plugin.getMysqlHandler().getData(MysqlHandler.Type.ACCOUNT,
				  "`owner_uuid` = ? AND `account_name` = ? AND `account_currency` = ? AND `account_type` = ? AND "
				+ "`account_category` = ? AND `owner_type` = ? ",
				owner.getUUID().toString(), accountName, currency.getUniqueName(), type.toString(), category.toString(), owner.getType().toString());
		return o == null ? null : (Account) o;
	}
	
	public ArrayList<Account> getAccounts()
	{
		try
		{
			return ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCOUNT,
					"`id` ASC", "1"));
		} catch (IOException e)
		{
			return new ArrayList<>();
		}
	}
	
	public ArrayList<Account> getAccounts(EconomyEntity owner)
	{
		try
		{
			return ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCOUNT,
					"`id` ASC", "`owner_uuid` = ? AND `owner_type` = ? AND `owner_name` = ?",
					owner.getUUID().toString(), owner.getType().toString(), owner.getName()));
		} catch (IOException e)
		{
			return new ArrayList<>();
		}
	}
	
	public ArrayList<Account> getAccounts(AccountType type)
	{
		try
		{
			return ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCOUNT,
					"`id` ASC", "`account_type` = ?", type.toString()));
		} catch (IOException e)
		{
			return new ArrayList<>();
		}
	}
	
	public ArrayList<Account> getAccounts(EconomyEntity.EconomyType ownerType)
	{
		try
		{
			return ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCOUNT,
					"`id` ASC", "`owner_type` = ?", ownerType.toString()));
		} catch (IOException e)
		{
			return new ArrayList<>();
		}
	}
	
	public ArrayList<Account> getAccounts(AccountCategory category)
	{
		try
		{
			return ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCOUNT,
					"`id` ASC", "`account_category` = ?", category.toString()));
		} catch (IOException e)
		{
			return new ArrayList<>();
		}
	}
	
	public ArrayList<Account> getAccounts(AccountType type, EconomyEntity.EconomyType ownerType)
	{
		try
		{
			return ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCOUNT,
					"`id` ASC", "`account_type` = ? AND `owner_type` = ?", type.toString(), ownerType.toString()));
		} catch (IOException e)
		{
			return new ArrayList<>();
		}
	}
	
	public ArrayList<Account> getAccounts(AccountType type, EconomyEntity.EconomyType ownerType, AccountCategory category)
	{
		try
		{
			return ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCOUNT,
					"`id` ASC", "`account_type` = ? AND `owner_type` = ? AND `account_category` = ?",
					type.toString(), ownerType.toString(), category.toString()));
		} catch (IOException e)
		{
			return new ArrayList<>();
		}
	}
	
	public Account getDefaultAccount(UUID ownerUUID)
	{
		return getDefaultAccount(ownerUUID, AccountCategory.MAIN);
	}

	public Account getDefaultAccount(UUID ownerUUID, AccountCategory category)
	{
		return getDefaultAccount(ownerUUID, category, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
	}
	
	public Account getDefaultAccount(UUID ownerUUID, AccountCategory category, EconomyCurrency currency)
	{
		DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEFAULTACCOUNT,
				"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
				ownerUUID.toString(), category.toString(), currency.getUniqueName());
		return dacc != null ? getAccount(dacc.getAccountID()) : null;
	}
	
	public void setDefaultAccount(UUID ownerUUID, Account account, AccountCategory category)
	{
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.DEFAULTACCOUNT,
				"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
				ownerUUID.toString(), category.toString(), account.getCurrency().getUniqueName()))
		{
			DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEFAULTACCOUNT,
					"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
					ownerUUID.toString(), category.toString(), account.getCurrency().getUniqueName());
			dacc.setAccountID(account.getID());
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.DEFAULTACCOUNT, dacc, 
					"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
					ownerUUID.toString(), category.toString(), account.getCurrency().getUniqueName());
		} else
		{
			DefaultAccount dacc = new DefaultAccount(ownerUUID, account.getID(), account.getCurrency().getUniqueName(), category);
			plugin.getMysqlHandler().create(MysqlHandler.Type.DEFAULTACCOUNT, dacc);
		}
	}
	
	public boolean addManagementTypeToAccount(Account account, UUID uuid, AccountManagementType accountManagementType)
	{
		if(canManageAccount(account, uuid, accountManagementType))
		{
			return true;
		}
		plugin.getMysqlHandler().create(MysqlHandler.Type.ACCOUNTMANAGEMENT,
				new AccountManagement(account.getID(), uuid, accountManagementType));
		return true;
	}

	public boolean removeManagementTypeFromAccount(Account account, UUID uuid, AccountManagementType accountManagementType)
	{
		if(canManageAccount(account, uuid, accountManagementType))
		{
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNTMANAGEMENT,
					"`player_uuid` = ? AND `account_id` = ? AND `account_management_type` = ?",
					uuid.toString(), account.getID(), accountManagementType.toString());
		}
		return true;
	}
	
	public boolean removeManagementTypeFromAccount(int accountID)
	{
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNTMANAGEMENT,
				"`account_id` = ?",
				accountID);
		return true;
	}
	
	public boolean canManageAccount(Account account, UUID uuid, AccountManagementType accountManagementType)
	{
		return canManageAccount(account.getID(), uuid, accountManagementType);
	}
	
	public boolean canManageAccount(int accountID, UUID uuid, AccountManagementType accountManagementType)
	{
		return plugin.getMysqlHandler().exist(MysqlHandler.Type.ACCOUNTMANAGEMENT,
				"`player_uuid` = ? AND `account_id` = ? AND `account_management_type` = ?",
				uuid.toString(), accountID, accountManagementType.toString());
	}
}
