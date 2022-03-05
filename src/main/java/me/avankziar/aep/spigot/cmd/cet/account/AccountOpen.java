package main.java.me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.object.AccountManagement;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountType;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.spigot.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class AccountOpen extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public AccountOpen(ArgumentConstructor ac)
	{
		super(ac);
		this.plugin = BaseConstructor.getPlugin();
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
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.CurrencyDontExist")
					.replace("%currency%", cur)));
			return;
		}
		EconomyEntity.EconomyType eeet = EconomyType.PLAYER;
		if(args.length >= 8)
		{
			try
			{
				eeet = EconomyType.valueOf(args[7]);
			} catch(Exception e)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.EconomyTypeIncorrect")
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("EntityNotExist")));
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.YouCannotOpenAAccountForSomeone")));
				return;
			}
		}
		AccountType act = AccountType.WALLET;
		if(args.length >= 7)
		{
			try
			{
				act = AccountType.valueOf(args[6]);
			} catch(Exception e)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.AccountTypeIncorrect")
						.replace("%act%", args[6])));
				return;
			}
		}
		AccountCategory acc = AccountCategory.MAIN;
		try
		{
			acc = AccountCategory.valueOf(accat);
		} catch(Exception e)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.AccountCategoryIncorrect")
					.replace("%acc%", accat)));
			return;
		}
		if(plugin.getIFHApi().existAccount(ee.getUUID(), acname, ec, act, acc, eeet))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.AccountAlreadyExist")));
			return;
		}
		if(ee.getUUID().toString().equals(player.getUniqueId().toString()))
		{
			if(!countAccounts(player, act))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.TooManyAccount")));
				return;
			}
		}		
		Account ac = new Account(0, acname, act, acc, ec, ee, 0, false);
		double amount = 0.0;
		for(String unsp : plugin.getYamlHandler().getConfig().getStringList("Cost.OpenAccount"))
		{
			String[] sp = unsp.split(";");
			if(sp.length != 4)
			{
				continue;
			}
			if(!ac.getCurrency().getUniqueName().equals(sp[0]))
			{
				continue;
			}
			AccountType acta;
			AccountCategory acca;
			try
			{
				acta = AccountType.valueOf(sp[1]);
				acca = AccountCategory.valueOf(sp[2]);
			} catch(Exception e)
			{
				continue;
			}
			if(ac.getType() != acta)
			{
				continue;
			}
			if(ac.getCategory() != acca)
			{
				continue;
			}
			if(MatchApi.isDouble(sp[3]))
			{
				amount = Double.parseDouble(sp[3]);
				break;
			}
		}
		if(amount > 0.0 && ee.getType() == EconomyType.PLAYER)
		{
			Account main = plugin.getIFHApi().getDefaultAccount(ee.getUUID(), AccountCategory.MAIN, ec);
			if(main == null)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.OpenCostExistButNoMainAccount")));
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
						ac, amount,
						OrdererType.PLAYER, ac.getOwner().getUUID().toString(), category, comment);
			} else if(tax != null)
			{
				ea = plugin.getIFHApi().transaction(
						ac, tax, amount,
						OrdererType.PLAYER, ac.getOwner().getUUID().toString(), category, comment);
			}
			if(!ea.isSuccess())
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.NotEnoughMoney")
						.replace("%format%", plugin.getIFHApi().format(amount, ec))));
				return;
			}
		}
		plugin.getMysqlHandler().create(MysqlHandler.Type.ACCOUNT, ac);
		List<AccountManagementType> atms = new ArrayList<AccountManagementType>(EnumSet.allOf(AccountManagementType.class));
		ac = plugin.getIFHApi().getAccount(ee, acname, act, acc, ec);
		for(AccountManagementType atm : atms)
		{
			AccountManagement am = new AccountManagement(ac.getID(), ee.getUUID(), atm);
			if(!plugin.getIFHApi().canManageAccount(ac, ee.getUUID(), atm))
			{
				plugin.getMysqlHandler().create(Type.ACCOUNTMANAGEMENT, am);
			}
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Open.AccountOpen")
				.replace("%acid%", String.valueOf(ac.getID()))
				.replace("%acowner%", ee.getName())
				.replace("%eeet%", eeet.toString())
				.replace("%acname%", ac.getAccountName())
				.replace("%act%", ee.getType().toString())
				.replace("%acc%", act.toString())
				.replace("%ec%", ec.getUniqueName())
				));
		return;
	}
	
	private boolean countAccounts(Player player, AccountType act)
	{
		int c = plugin.getMysqlHandler().getCount(MysqlHandler.Type.ACCOUNT, "`account_predefined` = ?", false);
		for(int i = 500; i > 0; i--)
		{
			if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.COUNT_ACCOUNT)+act.toString().toLowerCase()+"."+i))
			{
				return c < i;
			}
		}
		return false;
	}
}