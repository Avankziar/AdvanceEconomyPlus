package me.avankziar.aep.velocity.api.economy;

import java.util.ArrayList;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.AccountManagement;
import me.avankziar.aep.general.objects.AccountVelo;
import me.avankziar.aep.general.objects.DefaultAccount;
import me.avankziar.aep.general.objects.EntityData;
import me.avankziar.aep.velocity.AEP;
import me.avankziar.aep.velocity.handler.ConvertHandler;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.velocity.economy.account.Account;
import me.avankziar.ifh.velocity.economy.currency.EconomyCurrency;

public class AccountHandler
{
	private AEP plugin;
	protected EconomyEntity defaultServer;
	protected EconomyEntity defaultEntity;
	
	public AccountHandler(AEP plugin)
	{
		this.plugin = plugin;
	}
	
	public static void createAllCurrencyAccounts(Player player, boolean convert)
	{
		AEP plugin = AEP.getPlugin();
		for(EconomyCurrency ec : plugin.getIFHApi().getCurrencies(CurrencyType.DIGITAL))
		{
			EconomyEntity.EconomyType eeet = EconomyType.PLAYER;
			EconomyEntity ee = new EconomyEntity(eeet, player.getUniqueId(), player.getUsername());
			YamlDocument y = plugin.getYamlHandler().getCurrency(ec.getUniqueName());
			if(y.getBoolean("WhenPlayerFirstJoin.CreateWallets", false))
			{
				AccountType at = AccountType.WALLET;
				for(String a : y.getStringList("WhenPlayerFirstJoin.WalletsToCreate"))
				{
					String[] s = a.split(";");
					if(s.length < 3)
					{
						continue;
					}
					AccountCategory acy = AccountCategory.valueOf(s[0]);
					String acname = acy == AccountCategory.MAIN ? player.getUsername() : acy.toString().toLowerCase(); 
					boolean defaultac = MatchApi.isBoolean(s[1]) ? Boolean.valueOf(s[1]) : false;
					double startamount = MatchApi.isDouble(s[2]) ? Double.parseDouble(s[2]) : 0.0;
					ArrayList<AccountManagementType> amtl = new ArrayList<>();
					for(int i = 3; i < s.length; i++)
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
					/*if(convert && acy == AccountCategory.MAIN)
					{
						OLD_AEPUser old = _AEPUserHandler_OLD.getEcoPlayer(player);
						if(old != null)
						{
							startamount += old.getBalance();
							plugin.getMysqlHandler().deleteData(Type.OLDPLAYER, "`id` = ?", old.getId());
						}
					}*/
					Account ac = new Account(acname, at, acy, ec, ee, startamount, defaultac);
					if(plugin.getIFHApi().existAccount(ee.getUUID(), ec, ac.getType(), acy, eeet))
					{
						continue;
					}
					plugin.getMysqlHandler().create(MysqlType.ACCOUNT_VELOCITY, ac);
					for(AccountManagementType amt : amtl)
					{
						if(!plugin.getIFHApi().canManageAccount(ac, ee.getUUID(), amt))
						{
							plugin.getIFHApi().addManagementTypeToAccount(ac, ee.getUUID(), amt);
						}
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
					AccountCategory acy = AccountCategory.valueOf(s[0]);
					String acname = acy == AccountCategory.MAIN ? player.getUsername() : acy.toString().toLowerCase(); 
					boolean defaultac = MatchApi.isBoolean(s[1]) ? Boolean.valueOf(s[1]) : false;
					double startamount = MatchApi.isDouble(s[2]) ? Double.parseDouble(s[2]) : 0.0;
					ArrayList<AccountManagementType> amtl = new ArrayList<>();
					for(int i = 3; i < s.length; i++)
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
					plugin.getMysqlHandler().create(MysqlType.ACCOUNT_VELOCITY, ac);
					for(AccountManagementType amt : amtl)
					{
						if(!plugin.getIFHApi().canManageAccount(ac, ee.getUUID(), amt))
						{
							plugin.getIFHApi().addManagementTypeToAccount(ac, ee.getUUID(), amt);
						}
					}
				}
			}
		}
	}
	
	public EconomyEntity getEntity(UUID uuid)
	{
		if(plugin.getMysqlHandler().exist(MysqlType.PLAYERDATA, "`player_uuid` = ?", uuid.toString()))
		{
			AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(MysqlType.PLAYERDATA, "`player_uuid` = ?", uuid.toString());
			return user == null ? null : new EconomyEntity(EconomyEntity.EconomyType.PLAYER, user.getUUID(), user.getName());
		}
		if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.SERVER.toString()))
		{
			EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
					"`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.SERVER.toString());
			return ed == null ? null : new EconomyEntity(EconomyEntity.EconomyType.SERVER, ed.getUUID(), ed.getName());
		}
		if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.ENTITY.toString()))
		{
			EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
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
			if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.SERVER.toString()))
			{
				EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
						"`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.SERVER.toString());
				if(ed != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.ENTITY, ed.getUUID(), ed.getName());
				}
			}
			break;
		case PLAYER:
			if(plugin.getMysqlHandler().exist(MysqlType.PLAYERDATA, "`player_uuid` = ?", uuid.toString()))
			{
				AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(MysqlType.PLAYERDATA, "`player_uuid` = ?", uuid.toString());
				if(user != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.PLAYER, user.getUUID(), user.getName());
				}
			}
			break;
		case SERVER:
			if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_uuid` = ? AND `entity_type` = ?", uuid.toString(), EconomyEntity.EconomyType.ENTITY.toString()))
			{
				EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
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
		if(plugin.getMysqlHandler().exist(MysqlType.PLAYERDATA, "`player_name` = ?", name))
		{
			AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(MysqlType.PLAYERDATA, "`player_name` = ?", name);
			return user == null ? null : new EconomyEntity(EconomyEntity.EconomyType.PLAYER, user.getUUID(), name);
		}
		if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.SERVER.toString()))
		{
			EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
					"`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.SERVER.toString());
			return ed == null ? null : new EconomyEntity(EconomyEntity.EconomyType.SERVER, ed.getUUID(), name);
		}
		if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.ENTITY.toString()))
		{
			EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
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
			if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.SERVER.toString()))
			{
				EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
						"`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.SERVER.toString());
				if(ed != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.ENTITY, ed.getUUID(), ed.getName());
				}
			}
			break;
		case PLAYER:
			if(plugin.getMysqlHandler().exist(MysqlType.PLAYERDATA, "`player_name` = ?", name))
			{
				AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(MysqlType.PLAYERDATA, "`player_name` = ?", name);
				if(user != null)
				{
					ee = new EconomyEntity(EconomyEntity.EconomyType.PLAYER, user.getUUID(), user.getName());
				}
			}
			break;
		case SERVER:
			if(plugin.getMysqlHandler().exist(MysqlType.ENTITYDATA, "`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.ENTITY.toString()))
			{
				EntityData ed = (EntityData) plugin.getMysqlHandler().getData(MysqlType.ENTITYDATA,
						"`entity_name` = ? AND `entity_type` = ?", name, EconomyEntity.EconomyType.ENTITY.toString());
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
				return ConvertHandler.convertList0(plugin.getMysqlHandler().getFullList(MysqlType.ENTITYDATA, "`id` ASC", "`entity_type` = ?",
						type.toString()));
			case PLAYER:
				return ConvertHandler.convertList0(plugin.getMysqlHandler().getFullList(MysqlType.ACCOUNT_VELOCITY, "`id` ASC", "1"));
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
		return plugin.getMysqlHandler().exist(MysqlType.ACCOUNT_VELOCITY, 
				"`owner_uuid` = ? AND `account_name` = ?",
				uuid.toString(), accountName);
	}
	
	public boolean existAccount(UUID uuid, String accountName, EconomyCurrency currency)
	{
		return plugin.getMysqlHandler().exist(MysqlType.ACCOUNT_VELOCITY, 
				"`owner_uuid` = ? AND `account_currency` = ? AND `account_name` = ?",
				uuid.toString(), currency.getUniqueName(), accountName);
	}
	
	public boolean existAccount(UUID uuid, EconomyCurrency currency, AccountType type, AccountCategory category,
			EconomyEntity.EconomyType entityType)
	{
		return plugin.getMysqlHandler().exist(MysqlType.ACCOUNT_VELOCITY, 
				"`owner_uuid` = ? AND `account_currency` = ? AND `account_type` = ? AND `account_category` = ? AND `owner_type` = ?",
				uuid.toString(), currency.getUniqueName(), type.toString(), category.toString(), entityType.toString());
	}
	
	public boolean existAccount(UUID uuid, String accountName, EconomyCurrency currency, AccountType type, AccountCategory category,
			EconomyEntity.EconomyType entityType)
	{
		return plugin.getMysqlHandler().exist(MysqlType.ACCOUNT_VELOCITY, 
				"`owner_uuid` = ? AND `account_name` = ? AND `account_currency` = ? AND `account_type` = ? AND `account_category` = ? AND `owner_type` = ?",
				uuid.toString(), accountName, currency.getUniqueName(), type.toString(), category.toString(), entityType.toString());
	}
	
	public boolean createAccount(Account account)
	{
		if(existAccount(account.getOwner().getUUID(), account.getAccountName(), account.getCurrency(), account.getType(), account.getCategory(), account.getOwner().getType()))
		{
			return false;
		}
		return plugin.getMysqlHandler().create(MysqlType.ACCOUNT_VELOCITY, new AccountVelo(account));
	}
	
	public boolean createAccount(String accountName, AccountType type, AccountCategory category, EconomyCurrency currency,
			EconomyEntity owner, double balance)
	{
		if(existAccount(owner.getUUID(), currency, type, category, owner.getType()))
		{
			return false;
		}
		return plugin.getMysqlHandler().create(MysqlType.ACCOUNT_VELOCITY, 
				new AccountVelo(accountName, type, category, currency, owner, balance, false));
	}
	
	public int deleteAccount(Account account)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`owner_uuid` = ? AND `account_currency` = ? AND `account_type` = ? AND `account_category` = ? AND `owner_type` = ?",
				account.getOwner().getUUID().toString(), account.getCurrency(), account.getType().toString(), account.getCategory().toString(), account.getOwner().getType().toString());
	}
	
	public int deleteAllAccounts(EconomyCurrency currency)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`account_currency` = ?",
				currency.getUniqueName());
	}
	
	public int deleteAllAccounts(UUID uuid)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`owner_uuid` = ?",
				uuid);
	}

	public int deleteAllAccounts(UUID uuid, AccountType type)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`owner_uuid` = ? AND `account_type` = ?",
				uuid.toString(), type);
	}
	
	public int deleteAllAccounts(UUID uuid, EconomyEntity.EconomyType entityType)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`owner_uuid` = ? AND `owner_type` = ?",
				uuid.toString(), entityType);
	}
	
	public int deleteAllAccounts(UUID uuid, EconomyEntity.EconomyType entityType, AccountType type)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`owner_uuid` = ? AND `account_type` = ? AND `owner_type` = ?",
				uuid.toString(), type.toString(), entityType.toString());
	}
	
	public int deleteAllAccounts(AccountType type)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`account_type` = ?",
				type.toString());
	}

	public int deleteAllAccounts(AccountCategory category)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`account_category` = ?",
				category);
	}

	public int deleteAllAccounts(EconomyEntity.EconomyType entityType)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`owner_type` = ?",
				entityType.toString());
	}

	public int deleteAllAccounts(EconomyEntity.EconomyType entityType, AccountType type)
	{
		return plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_VELOCITY,
				"`account_type` = ? AND `owner_type` = ?",
				type.toString(), entityType.toString());
	}
	
	public Account getAccount(int id)
	{
		return (Account) plugin.getMysqlHandler().getData(MysqlType.ACCOUNT_VELOCITY, "`id` = ?", id);
	}
	
	public Account getAccount(UUID ownerUUID, String accountName)
	{
		if(!existAccount(ownerUUID, accountName))
		{
			return null;
		}
		return (Account) plugin.getMysqlHandler().getData(MysqlType.ACCOUNT_VELOCITY,
				  "`owner_uuid` = ? AND `account_name` = ?",
				ownerUUID.toString(), accountName);
	}
	
	public Account getAccount(UUID ownerUUID, String accountName, EconomyEntity.EconomyType ownerEntityType, AccountType type, AccountCategory category,
			EconomyCurrency currency)
	{
		return (Account) plugin.getMysqlHandler().getData(MysqlType.ACCOUNT_VELOCITY,
				  "`owner_uuid` = ? AND `account_name` = ? AND `account_currency` = ? AND `account_type` = ? AND "
				+ "`account_category` = ? AND `owner_type` = ? AND `server` = ? AND `world` = ?",
				ownerUUID.toString(), accountName, currency.getUniqueName(), type.toString(), category.toString(), ownerEntityType.toString());
	}
	
	public Account getAccount(EconomyEntity owner, String accountName, AccountType type, AccountCategory category, EconomyCurrency currency)
	{
		return (Account) plugin.getMysqlHandler().getData(MysqlType.ACCOUNT_VELOCITY,
				  "`owner_uuid` = ? AND `account_name` = ? AND `account_currency` = ? AND `account_type` = ? AND "
				+ "`account_category` = ? AND `owner_type` = ? AND `server` = ? AND `world` = ?",
				owner.getUUID().toString(), accountName, currency.getUniqueName(), type.toString(), category.toString(), owner.getType().toString());
	}
	
	public ArrayList<Account> getAccounts()
	{
		return ConvertHandler.convertListII(plugin.getMysqlHandler().getFullList(MysqlType.ACCOUNT_VELOCITY,
				"`id` ASC", "1"));
	}
	
	public ArrayList<Account> getAccounts(AccountType type)
	{
		return ConvertHandler.convertListII(plugin.getMysqlHandler().getFullList(MysqlType.ACCOUNT_VELOCITY,
				"`id` ASC", "`account_type` = ?", type.toString()));
	}
	
	public ArrayList<Account> getAccounts(EconomyEntity.EconomyType ownerType)
	{
		return ConvertHandler.convertListII(plugin.getMysqlHandler().getFullList(MysqlType.ACCOUNT_VELOCITY,
				"`id` ASC", "`owner_type` = ?", ownerType.toString()));
	}
	
	public ArrayList<Account> getAccounts(AccountCategory category)
	{
		return ConvertHandler.convertListII(plugin.getMysqlHandler().getFullList(MysqlType.ACCOUNT_VELOCITY,
				"`id` ASC", "`account_category` = ?", category.toString()));
	}
	
	public ArrayList<Account> getAccounts(AccountType type, EconomyEntity.EconomyType ownerType)
	{
		return ConvertHandler.convertListII(plugin.getMysqlHandler().getFullList(MysqlType.ACCOUNT_VELOCITY,
				"`id` ASC", "`account_type` = ? AND `owner_type` = ?", type.toString(), ownerType.toString()));
	}
	
	public ArrayList<Account> getAccounts(AccountType type, EconomyEntity.EconomyType ownerType, AccountCategory category)
	{
		return ConvertHandler.convertListII(plugin.getMysqlHandler().getFullList(MysqlType.ACCOUNT_VELOCITY,
				"`id` ASC", "`account_type` = ? AND `owner_type` = ? AND `account_category` = ?",
				type.toString(), ownerType.toString(), category.toString()));
	}
	
	public Account getDefaultAccount(UUID ownerUUID)
	{
		return getDefaultAccount(ownerUUID, AccountCategory.MAIN);
	}

	public Account getDefaultAccount(UUID ownerUUID, AccountCategory category)
	{
		DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlType.DEFAULTACCOUNT,
				"`player_uuid` = ? AND `account_category` = ?",
				ownerUUID.toString(), category.toString());
		return dacc != null ? getAccount(dacc.getAccountID()) : null;
	}
	
	public Account getDefaultAccount(UUID ownerUUID, AccountCategory category, EconomyCurrency currency)
	{
		DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlType.DEFAULTACCOUNT,
				"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
				ownerUUID.toString(), category.toString(), currency.getUniqueName());
		return dacc != null ? getAccount(dacc.getAccountID()) : null;
	}
	
	public void setDefaultAccount(UUID ownerUUID, Account account, AccountCategory category)
	{
		if(plugin.getMysqlHandler().exist(MysqlType.DEFAULTACCOUNT,
				"`player_uuid` = ? `account_category` = ?",
				ownerUUID.toString(), category.toString()))
		{
			DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlType.DEFAULTACCOUNT,
					"`player_uuid` = ? AND `account_category` = ?",
					ownerUUID.toString(), category.toString());
			dacc.setAccountID(account.getID());
			plugin.getMysqlHandler().updateData(MysqlType.DEFAULTACCOUNT, dacc, 
					"`player_uuid` = ? AND `account_category` = ?",
					ownerUUID.toString(), category.toString());
		} else
		{
			DefaultAccount dacc = new DefaultAccount(ownerUUID, account.getID(), account.getCurrency().getUniqueName(), category);
			plugin.getMysqlHandler().create(MysqlType.DEFAULTACCOUNT, dacc);
		}
	}
	
	public boolean addManagementTypeToAccount(Account account, UUID uuid, AccountManagementType accountManagementType)
	{
		if(canManageAccount(account, uuid, accountManagementType))
		{
			return true;
		}
		plugin.getMysqlHandler().create(MysqlType.ACCOUNTMANAGEMENT,
				new AccountManagement(account.getID(), uuid, accountManagementType));
		return true;
	}

	public boolean removeManagementTypeFromAccount(Account account, UUID uuid, AccountManagementType accountManagementType)
	{
		if(canManageAccount(account, uuid, accountManagementType))
		{
			plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNTMANAGEMENT,
					"`player_uuid` = ? AND `account_id` = ? AND `account_management_type` = ?",
					uuid.toString(), account.getID(), accountManagementType);
		}
		return true;
	}
	
	public boolean removeManagementTypeFromAccount(int accountID)
	{
		plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNTMANAGEMENT,
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
		return plugin.getMysqlHandler().exist(MysqlType.ACCOUNTMANAGEMENT,
				"`player_uuid` = ? AND `account_id` = ? AND `account_management_type` = ?",
				uuid.toString(), accountID, accountManagementType);
	}
}
