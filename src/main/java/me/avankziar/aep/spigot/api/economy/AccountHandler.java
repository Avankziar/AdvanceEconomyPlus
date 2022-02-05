package main.java.me.avankziar.aep.spigot.api.economy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.object.ne_w.AEPUser;
import main.java.me.avankziar.aep.spigot.object.ne_w.AccountManagement;
import main.java.me.avankziar.aep.spigot.object.ne_w.DefaultAccount;
import main.java.me.avankziar.aep.spigot.object.ne_w.EntityData;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountType;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class AccountHandler
{
	private AdvancedEconomyPlus plugin;
	
	public AccountHandler(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	public static void createAllCurrencyAccounts(Player player)
	{
		//ADDME
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
		return (Account) plugin.getMysqlHandler().getData(MysqlHandler.Type.ACCOUNT, "`id` = ?", id);
	}
	
	public Account getAccount(UUID ownerUUID, String accountName)
	{
		if(!existAccount(ownerUUID, accountName))
		{
			return null;
		}
		return (Account) plugin.getMysqlHandler().getData(MysqlHandler.Type.ACCOUNT,
				  "`owner_uuid` = ? AND `account_name` = ?",
				ownerUUID.toString(), accountName);
	}
	
	public Account getAccount(UUID ownerUUID, String accountName, EconomyEntity.EconomyType ownerEntityType, AccountType type, AccountCategory category,
			EconomyCurrency currency)
	{
		return (Account) plugin.getMysqlHandler().getData(MysqlHandler.Type.ACCOUNT,
				  "`owner_uuid` = ? AND `account_name` = ? AND `account_currency` = ? AND `account_type` = ? AND "
				+ "`account_category` = ? AND `owner_type` = ? AND `server` = ? AND `world` = ?",
				ownerUUID.toString(), accountName, currency.getUniqueName(), type.toString(), category.toString(), ownerEntityType.toString());
	}
	
	public Account getAccount(EconomyEntity owner, String accountName, AccountType type, AccountCategory category, EconomyCurrency currency)
	{
		return (Account) plugin.getMysqlHandler().getData(MysqlHandler.Type.ACCOUNT,
				  "`owner_uuid` = ? AND `account_name` = ? AND `account_currency` = ? AND `account_type` = ? AND "
				+ "`account_category` = ? AND `owner_type` = ? AND `server` = ? AND `world` = ?",
				owner.getUUID().toString(), accountName, currency.getUniqueName(), type.toString(), category.toString(), owner.getType().toString());
	}
	
	public ArrayList<Account> getAccounts()
	{
		try
		{
			return ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCOUNT,
					"`id` ASC", "1"));
		} catch (IOException e)
		{
			return null;
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
			return null;
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
			return null;
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
			return null;
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
			return null;
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
			return null;
		}
	}
	
	public Account getDefaultAccount(UUID ownerUUID)
	{
		return getDefaultAccount(ownerUUID, AccountCategory.MAIN);
	}

	public Account getDefaultAccount(UUID ownerUUID, AccountCategory category)
	{
		DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEFAULTACCOUNT,
				"`player_uuid` = ? AND `account_category` = ?",
				ownerUUID.toString(), category.toString());
		return dacc != null ? getAccount(dacc.getAccountID()) : null;
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
				"`player_uuid` = ? `account_category` = ?",
				ownerUUID.toString(), category.toString()))
		{
			DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEFAULTACCOUNT,
					"`player_uuid` = ? AND `account_category` = ?",
					ownerUUID.toString(), category.toString());
			dacc.setAccountID(account.getID());
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.DEFAULTACCOUNT, dacc, 
					"`player_uuid` = ? AND `account_category` = ?",
					ownerUUID.toString(), category.toString());
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

	public boolean removeManagementTypeToAccount(Account account, UUID uuid, AccountManagementType accountManagementType)
	{
		if(canManageAccount(account, uuid, accountManagementType))
		{
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNTMANAGEMENT,
					"`uuid` = ? AND `account_id` = ? AND `account_management_type` = ?",
					uuid.toString(), account.getID(), accountManagementType);
		}
		return true;
	}
	
	public boolean canManageAccount(Account account, UUID uuid, AccountManagementType accountManagementType)
	{
		return canManageAccount(account.getID(), uuid, accountManagementType);
	}
	
	public boolean canManageAccount(int accountID, UUID uuid, AccountManagementType accountManagementType)
	{
		return plugin.getMysqlHandler().exist(MysqlHandler.Type.ACCOUNTMANAGEMENT,
				"`uuid` = ? AND `account_id` = ? AND `account_management_type` = ?",
				uuid.toString(), accountID, accountManagementType);
	}
}
