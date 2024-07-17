package me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class AccountManage extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public AccountManage(ArgumentConstructor ac)
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
	 * aep account manage <Spielername> <Accountname> <Spielername> <ManagementType>
	 */
	private void middlePart(Player player, String[] args)
	{
		Account ac = plugin.getIFHApi().getAccount(plugin.getIFHApi().getEntity(args[2]), args[3]);
		if(ac == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.AccountDontExist")));
			return;
		}
		if(ac.getCurrency() == null)
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", ac.getAccountName()));
			return;
		}
		EconomyEntity ee = plugin.getIFHApi().getEntity(args[4], EconomyType.PLAYER);
		if(ee == null)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("EntityNotExist")));
			return;
		}
		AccountManagementType amt;
		try
		{
			amt = plugin.getIFHApi().getAccountManagementType(args[5]);
		} catch(Exception e)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Manage.AMTDontExist")
					.replace("%amt%", args[5])));
			return;
		}
		if(!player.hasPermission(ExtraPerm.get(Type.BYPASS_ACCOUNTMANAGEMENT)))
		{
			if(amt == AccountManagementType.CAN_SET_OWNERSHIP
					&& !plugin.getIFHApi().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_SET_OWNERSHIP))
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Manage.YouCannotManageTheAccount")));
				return;
			} else if(amt == AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT
					&& !plugin.getIFHApi().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT))
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Manage.YouCannotManageTheAccount")));
				return;
			} else if(!plugin.getIFHApi().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_ADMINISTRATE_ACCOUNT))
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Manage.YouCannotManageTheAccount")));
				return;
			}
		}
		if(plugin.getIFHApi().canManageAccount(ac, ee.getUUID(), amt))
		{
			plugin.getIFHApi().removeManagementTypeFromAccount(ac, ee.getUUID(), amt);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Manage.AMTWasRemoved")
					.replace("%acname%", ac.getAccountName())
					.replace("%acowner%", ac.getOwner().getName())
					.replace("%player%", ee.getName())
					.replace("%amt%", plugin.getIFHApi().getAccountManagementType(amt))));
			return;
		} else
		{
			plugin.getIFHApi().addManagementTypeToAccount(ac, ee.getUUID(), amt);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Manage.AMTWasAdded")
					.replace("%acname%", ac.getAccountName())
					.replace("%acowner%", ac.getOwner().getName())
					.replace("%player%", ee.getName())
					.replace("%amt%", plugin.getIFHApi().getAccountManagementType(amt))));
		}
	}
}