package main.java.me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;

public class AccountClose extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public AccountClose(ArgumentConstructor ac)
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
	 * aep account close <Accountname> [confirm(If Account is Predefine)]
	 * aep account close <AccountOwnername> <Accountname> [confirm(If Account is Predefine)]
	 */
	private void middlePart(Player player, String[] args)
	{
		Account ac = null;
		if(args.length == 3)
		{
			ac = plugin.getIFHApi().getAccount(plugin.getIFHApi().getEntity(player.getUniqueId(), EconomyType.PLAYER), args[2]);
		} else if(args.length == 4)
		{
			ac = plugin.getIFHApi().getAccount(plugin.getIFHApi().getEntity(args[2]), args[3]);
		}
		if(ac == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.AccountDontExist")));
			return;
		}
		if(!ac.getOwner().toString().equals(player.getUniqueId().toString())
				&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_ACCOUNTMANAGEMENT)))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Account.YouAreNotTheOwner")));
			return;
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
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("Cmd.Account.IsPredefine")));
					return;
				}
			}			
			if(!args[args.length-1].equalsIgnoreCase("confirm") && !args[args.length-1].equalsIgnoreCase("bestätigen"))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Account.PleaseConfirmPredefine")));
				return;
			}
		}
		final String oname = ac.getOwner().getName();
		final String acname = ac.getAccountName();
		final String amount = plugin.getIFHApi().format(ac.getBalance(), ac.getCurrency());
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT, "`id` = ?", ac.getID());
		player.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getLang().getString("Cmd.Account.Close.AccountIsClosed")
				.replace("%acname%", acname)
				.replace("%acowner%", oname)
				.replace("%format%", amount)));
		return;
	}
}