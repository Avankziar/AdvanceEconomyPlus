package me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AccountSpigot;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.spigot.economy.account.Account;

public class AccountSetName extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public AccountSetName(ArgumentConstructor ac)
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
	 * aep account setname <spielername> <accountname> <newname>
	 */
	private void middlePart(Player player, String[] args)
	{
		EconomyEntity ee = plugin.getIFHApi().getEntity(args[2]);
		String acname = args[3];
		String nname = args[4];
		if(ee == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("EntityNotExist")));
			return;
		}
		Account ac = plugin.getIFHApi().getAccount(ee, acname);
		if(ac == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Pay.AccountDontExist")));
			return;
		}
		if(ac.getCurrency() == null)
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", ac.getAccountName()));
			return;
		}
		if(!player.hasPermission(ExtraPerm.get(Type.BYPASS_ACCOUNTMANAGEMENT)))
		{
			if(!ac.getOwner().getUUID().toString().equals(player.getUniqueId().toString())
					&& !plugin.getIFHApi().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_ADMINISTRATE_ACCOUNT))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Manage.YouCannotManageTheAccount")));
				return;
			}
		}
		boolean newNameExist = plugin.getMysqlHandler().exist(MysqlType.ACCOUNT_SPIGOT, 
				"`owner_uuid` = ? AND `account_name` = ?", ee.getUUID().toString(), nname);
		if(newNameExist)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.SetName.NameAlreadyExist")
					.replace("%newname%", nname)));
			return;
		}
		final String oldname = ac.getAccountName();
		ac.setAccountName(nname);
		plugin.getMysqlHandler().updateData(MysqlType.ACCOUNT_SPIGOT, new AccountSpigot(ac), "`id` = ?", ac.getID());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.SetName.SetName")
				.replace("%acname%", oldname)
				.replace("%acowner%", ac.getOwner().getName())
				.replace("%newname%", nname)));
		return;
	}
}