package me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.DefaultAccount;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.spigot.economy.account.Account;

public class AccountSetDefault extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public AccountSetDefault(ArgumentConstructor ac)
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
	 * aep account setdefault <Spielername> <Accountname>
	 */
	private void middlePart(Player player, String[] args)
	{
		EconomyEntity ee =  plugin.getIFHApi().getEntity(args[2]);
		String acname = args[3];
		if(ee == null)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("EntityNotExist")));
			return;
		}
		Account ac = plugin.getIFHApi().getAccount(ee, acname);
		if(ac == null)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Pay.AccountDontExist")));
			return;
		}
		if(ac.getCurrency() == null)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", ac.getAccountName())));
			return;
		}
		if(!plugin.getIFHApi().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT)
				&& !player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_ACCOUNTMANAGEMENT)))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Account.SetDefault.CannotSetAsDefault")));
			return;
		}
		DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlType.DEFAULTACCOUNT,
				"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
				player.getUniqueId().toString(), ac.getCategory().toString(), ac.getCurrency().getUniqueName());
		if(dacc != null)
		{
			if(dacc.getAccountID() == ac.getID())
			{
				plugin.getMysqlHandler().deleteData(MysqlType.DEFAULTACCOUNT, 
						"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
						player.getUniqueId().toString(), ac.getCategory().toString(), ac.getCurrency().getUniqueName());
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.SetDefault.AccountNotDefault")
						.replace("%acname%", ac.getAccountName())
						.replace("%acowner%", ac.getOwner().getName())
						.replace("%cat%", plugin.getIFHApi().getAccountCategory(ac.getCategory()))));
				return;
			}
			dacc.setAccountID(ac.getID());
			plugin.getMysqlHandler().updateData(MysqlType.DEFAULTACCOUNT, dacc, 
					"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
					player.getUniqueId().toString(), ac.getCategory().toString(), ac.getCurrency().getUniqueName());
		} else
		{
			dacc = new DefaultAccount(player.getUniqueId(), ac.getID(), ac.getCurrency().getUniqueName(), ac.getCategory());
			plugin.getMysqlHandler().create(MysqlType.DEFAULTACCOUNT, dacc);
		}
		player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Account.SetDefault.SetDefaultAccount")
				.replace("%acname%", ac.getAccountName())
				.replace("%acid%", String.valueOf(ac.getID()))
				.replace("%acowner%", ac.getOwner().getName())
				.replace("%cat%", plugin.getIFHApi().getAccountCategory(ac.getCategory()))));
	}
}