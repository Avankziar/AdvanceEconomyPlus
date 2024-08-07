package me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AccountManagement;
import me.avankziar.aep.general.objects.AccountSpigot;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.ConfigHandler;
import me.avankziar.aep.spigot.handler.ConfigHandler.CountType;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class AccountOpen extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	private static String d1 = "accountopen";
	
	public AccountOpen(ArgumentConstructor ac)
	{
		super(ac);
		this.plugin = AEP.getPlugin();
		this.ac = ac;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Cmd only for Players!");
			return;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(ac.getPermission()))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				middlePart(player, args);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	/*
	 * aep account open <currencyuniquename> <Spielername/Entity/Server> <Accountname> <AccountCategory> [AccountType] [EconomyEntityType]
	 */
	private void middlePart(Player player, String[] args)
	{
		String cur = args[2];
		String ename = args[3];
		String acname = args[4];
		String accat = args[5];
		
		EconomyCurrency ec = plugin.getIFHApi().getCurrency(cur);
		if(ec == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.CurrencyDontExist")
					.replace("%currency%", cur)));
			return;
		}
		EconomyEntity.EconomyType eeet = EconomyType.PLAYER;
		if(args.length >= 8)
		{
			try
			{
				eeet = plugin.getIFHApi().getEconomyEntityType(args[7]);
			} catch(Exception e)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.EconomyTypeIncorrect")
						.replace("%eeet%", args[7])));
				return;
			}
		}
		EconomyEntity ee = null;
		if(eeet == EconomyType.PLAYER)
		{
			ee = plugin.getIFHApi().getEntity(ename, eeet);
			if(ee == null)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("EntityNotExist")));
				return;
			}
		} else
		{
			ee = plugin.getIFHApi().getEntity(ename, eeet);
			if(ee == null)
			{
				ee = new EconomyEntity(eeet, null, ename).generateUUID();
			}
		}
		if(!ee.getUUID().toString().equals(player.getUniqueId().toString()))
		{
			if(!player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_ACCOUNTOPEN)))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.YouCannotOpenAAccountForSomeone")));
				return;
			}
		}
		AccountType act = AccountType.WALLET;
		if(args.length >= 7)
		{
			try
			{
				act = plugin.getIFHApi().getAccountType(args[6]);
			} catch(Exception e)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.AccountTypeIncorrect")
						.replace("%act%", args[6])));
				return;
			}
		}
		AccountCategory acc = AccountCategory.MAIN;
		try
		{
			acc = plugin.getIFHApi().getAccountCategory(accat);
		} catch(Exception e)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.AccountCategoryIncorrect")
					.replace("%acc%", accat)));
			return;
		}
		if(plugin.getIFHApi().existAccount(ee.getUUID(), acname, ec, act, acc, eeet))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.AccountAlreadyExist")));
			return;
		}
		if(ee.getUUID().toString().equals(player.getUniqueId().toString()))
		{
			if(!countAccounts(player, act, acc))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.TooManyAccount")));
				return;
			}
		}		
		Account ac = new Account(0, acname, act, acc, ec, ee, 0, false);
		double amount = -1.0;
		ArrayList<AccountManagementType> amtlist = new ArrayList<>();
		ConfigHandler.debug(d1, "> Cost.OpenAccount Begin");
		for(String unsp : plugin.getYamlHandler().getConfig().getStringList("Cost.OpenAccount"))
		{
			ConfigHandler.debug(d1, "> unsp : "+unsp);
			String[] sp = unsp.split(";");
			if(!ac.getCurrency().getUniqueName().equals(sp[0]))
			{
				ConfigHandler.debug(d1, "> Currency !equal "+sp[0]);
				continue;
			}
			AccountType acta = AccountType.WALLET;
			AccountCategory acca = AccountCategory.MAIN;
			try
			{
				acta = AccountType.valueOf(sp[1]);
				acca = AccountCategory.valueOf(sp[2]);
			} catch(Exception e)
			{
				ConfigHandler.debug(d1, "> Type || category !valueOf "+sp[1]+" "+sp[2]);
				continue;
			}
			if(act != acta)
			{
				ConfigHandler.debug(d1, "> Type != : "+act+" "+sp[1]);
				continue;
			}
			if(acc != acca)
			{
				ConfigHandler.debug(d1, "> Category != "+acc+" "+sp[2]);
				continue;
			}
			if(MatchApi.isDouble(sp[3]))
			{
				amount = Double.parseDouble(sp[3]);
				if(amount < 0)
				{
					amount = 0;
				}
			} else
			{
				ConfigHandler.debug(d1, "> amount "+sp[3]);
				continue;
			}
			for(int i = 4; i < sp.length; i++)
			{
				try
				{
					AccountManagementType amt = AccountManagementType.valueOf(sp[i]);
					amtlist.add(amt);
				} catch(Exception e)
				{
					ConfigHandler.debug(d1, "> AMT !equal "+sp[i]);
					continue;
				}				
			}
			break;
		}
		if(amount < 0)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.DontAllowOpening")));
			return;
		}
		if(amount > 0.0 && ee.getType() == EconomyType.PLAYER)
		{
			Account main = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, ec);
			if(main == null)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.OpenCostExistButNoMainAccount")));
				return;
			}
			Account tax = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.TAX, ec);
			String category = plugin.getYamlHandler().getLang().getString("AccountOpen.Category");
			String comment = plugin.getYamlHandler().getLang().getString("AccountOpen.Comment")
					.replace("%accountname%", ac.getAccountName())
					.replace("%owner%", ac.getOwner().getName())
					.replace("%accounttype%", ac.getType().toString())
					.replace("%accountcategory%", ac.getCategory().toString());
			EconomyAction ea = null;
			if(tax == null)
			{
				ea = plugin.getIFHApi().withdraw(
						main, amount,
						OrdererType.PLAYER, main.getOwner().getUUID().toString(), category, comment);
			} else if(tax != null)
			{
				ea = plugin.getIFHApi().transaction(
						main, tax, amount,
						OrdererType.PLAYER, main.getOwner().getUUID().toString(), category, comment);
			}
			if(!ea.isSuccess())
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.NotEnoughMoney")
						.replace("%format%", plugin.getIFHApi().format(amount, ec))));
				return;
			}
		}
		plugin.getMysqlHandler().create(MysqlType.ACCOUNT_SPIGOT, new AccountSpigot(ac));
		ac = plugin.getIFHApi().getAccount(ee, acname, act, acc, ec);
		for(AccountManagementType atm : amtlist)
		{
			AccountManagement am = new AccountManagement(ac.getID(), ee.getUUID(), atm);
			if(!plugin.getIFHApi().canManageAccount(ac, ee.getUUID(), atm))
			{
				plugin.getMysqlHandler().create(MysqlType.ACCOUNTMANAGEMENT, am);
			}
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.AccountOpen")
				.replace("%acid%", String.valueOf(ac.getID()))
				.replace("%acowner%", ee.getName())
				.replace("%eeet%", plugin.getIFHApi().getEconomyEntityType(eeet))
				.replace("%acname%", ac.getAccountName())
				.replace("%act%", plugin.getIFHApi().getAccountType(act))
				.replace("%acc%", plugin.getIFHApi().getAccountCategory(acc))
				.replace("%ec%", ec.getUniqueName())
				.replace("%format%", plugin.getIFHApi().format(amount, ec))
				));
		return;
	}
	
	private boolean countAccounts(Player player, AccountType act, AccountCategory acc)
	{
		boolean useact = plugin.getYamlHandler().getConfig().getBoolean("Do.OpenAccount.CountWithAccountType", false);
		boolean useacc = plugin.getYamlHandler().getConfig().getBoolean("Do.OpenAccount.CountWithAccountCategory", false);
		int c = 0;
		if(!useact && !useacc)
		{
			c = plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_SPIGOT, 
					"`account_predefined` = ? AND `owner_uuid` = ?",
					false, player.getUniqueId().toString());
		} else if(useact && !useacc)
		{
			c = plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_SPIGOT, 
					"`account_predefined` = ? AND `owner_uuid` = ? AND `account_type` = ?",
					false, player.getUniqueId().toString(), act.toString());
		} else if(!useact && useacc)
		{
			c = plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_SPIGOT, 
					"`account_predefined` = ? AND `owner_uuid` = ? AND `account_category` = ?",
					false, player.getUniqueId().toString(), acc.toString());
		} else if(useact && useacc)
		{
			c = plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_SPIGOT, 
					"`account_predefined` = ? AND `owner_uuid` = ? AND `account_type` = ? AND `account_category` = ?",
					false, player.getUniqueId().toString(), act.toString(), acc.toString());
		}
		CountType ct = new ConfigHandler().getCountPermType();
		switch(ct)
		{
		case ADDUP:
			int a = 0;
			for(int i = 500; i >= 0; i--)
			{
				if(useact && useacc)
				{
					if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.COUNT_ACCOUNT)+act.toString().toLowerCase()+"."+
																						acc.toString().toLowerCase()+"."+i))
					{
						a += i;
					}
				} else if(useact && !useacc)
				{
					if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.COUNT_ACCOUNT)+act.toString().toLowerCase()+"."+i))
					{
						a += i;
					}
				} else if(!useact && useacc)
				{
					if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.COUNT_ACCOUNT)+acc.toString().toLowerCase()+"."+i))
					{
						a += i;
					}
				} else
				{
					if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.COUNT_ACCOUNT)+i))
					{
						a += i;
					}
				}
			}
			return c < a;
		case HIGHEST:
			for(int i = 500; i >= 0; i--)
			{
				if(useact && useacc)
				{
					if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.COUNT_ACCOUNT)+act.toString().toLowerCase()+"."+
																						acc.toString().toLowerCase()+"."+i))
					{
						return c < i;
					}
				} else if(useact && !useacc)
				{
					if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.COUNT_ACCOUNT)+act.toString().toLowerCase()+"."+i))
					{
						return c < i;
					}
				} else if(!useact && useacc)
				{
					if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.COUNT_ACCOUNT)+acc.toString().toLowerCase()+"."+i))
					{
						return c < i;
					}
				} else
				{
					if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.COUNT_ACCOUNT)+i))
					{
						return c < i;
					}
				}
			}
			break;
		}
		return false;
	}
}