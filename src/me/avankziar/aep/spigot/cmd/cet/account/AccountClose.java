package me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.ifh.spigot.economy.account.Account;

public class AccountClose extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public AccountClose(ArgumentConstructor ac)
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
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
	 * aep account close <AccountOwnername> <Accountname> [confirm(If Account has more than 0 money)]
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
		if(!ac.getOwner().getUUID().toString().equals(player.getUniqueId().toString())
				&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_ACCOUNTMANAGEMENT)))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Account.YouAreNotTheOwner")));
			return;
		}
		if(ac.getCurrency() == null)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", ac.getAccountName())));
			return;
		}
		if(ac.getBalance() > 1.0)
		{
			if(!args[args.length-1].equalsIgnoreCase("confirm") && !args[args.length-1].equalsIgnoreCase("bestätigen"))
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Close.BalanceMoreThanZero")
						.replace("%acowner%", ac.getOwner().getName())
						.replace("%acname%", ac.getAccountName())));
				return;
			}
		}
		if(ac.isPredefinedAccount())
		{
			boolean canDeletePredefineAccount = false;
			for(String unsp : plugin.getYamlHandler().getConfig().getStringList("Enable.PlayerCanDeletePredefineAccount"))
			{
				String[] sp = unsp.split(";");
				if(sp.length != 2)
				{
					continue;
				}
				if(ac.getCurrency().getUniqueName().equals(sp[0]))
				{
					if(MatchApi.isBoolean(sp[1]))
					{
						canDeletePredefineAccount = Boolean.parseBoolean(sp[1]);
						break;
					}
				}
			}
			if(!canDeletePredefineAccount)
			{
				if(!player.hasPermission(ExtraPerm.get(Type.BYPASS_ACCOUNTMANAGEMENT)))
				{
					player.sendMessage(ChatApiOld.tl(
							plugin.getYamlHandler().getLang().getString("Cmd.Account.IsPredefine")));
					return;
				}
			}			
		}
		final int id = ac.getID();
		final String oname = ac.getOwner().getName();
		final String acname = ac.getAccountName();
		final String amount = plugin.getIFHApi().format(ac.getBalance(), ac.getCurrency());
		plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_SPIGOT, "`id` = ?", id);
		plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNTMANAGEMENT, "`account_id` = ?", id);
		plugin.getMysqlHandler().deleteData(MysqlType.QUICKPAYACCOUNT, "`account_id` = ?", id);
		plugin.getMysqlHandler().deleteData(MysqlType.DEFAULTACCOUNT, "`account_id` = ?", id);
		player.sendMessage(ChatApiOld.tl(
				plugin.getYamlHandler().getLang().getString("Cmd.Account.Close.AccountIsClosed")
				.replace("%acname%", acname)
				.replace("%acowner%", oname)
				.replace("%format%", amount)));
		return;
	}
}