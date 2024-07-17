package me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.QuickPayAccount;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class AccountSetQuickPay extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public AccountSetQuickPay(ArgumentConstructor ac)
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
	 * aep account setquickpay <Accountname>
	 */
	private void middlePart(Player player, String[] args)
	{
		String acname = args[2];
		final Account ac = plugin.getIFHApi().getAccount(new EconomyEntity(EconomyType.PLAYER, player.getUniqueId(), player.getName()), acname);
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
		if(!plugin.getIFHApi().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NoWithdrawRights")));
			return;
		}
		QuickPayAccount qpa = (QuickPayAccount) plugin.getMysqlHandler().getData(
				MysqlType.QUICKPAYACCOUNT, "`account_currency` = ? AND `player_uuid` = ?",
				ac.getCurrency().getUniqueName(), ac.getOwner().getUUID().toString());
		if(qpa != null)
		{
			qpa.setAccountID(ac.getID());
			plugin.getMysqlHandler().updateData(MysqlType.QUICKPAYACCOUNT, qpa, 
					"`account_currency` = ? AND `player_uuid` = ?",
					ac.getCurrency().getUniqueName(), ac.getOwner().getUUID().toString());
		} else
		{
			qpa = new QuickPayAccount(ac.getOwner().getUUID(), ac.getID(), ac.getCurrency().getUniqueName());
			plugin.getMysqlHandler().create(MysqlType.QUICKPAYACCOUNT, qpa);
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.SetQuickPay.SetQuickPay")
				.replace("%acname%", acname)
				.replace("%currency%", ac.getCurrency().getUniqueName())));
		return;
	}
}