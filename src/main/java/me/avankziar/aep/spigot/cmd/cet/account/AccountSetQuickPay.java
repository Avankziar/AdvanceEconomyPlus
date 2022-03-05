package main.java.me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.QuickPayAccount;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

public class AccountSetQuickPay extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public AccountSetQuickPay(ArgumentConstructor ac)
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
	 * aep account setquickpay <Accountname>
	 */
	private void middlePart(Player player, String[] args)
	{
		String acname = args[2];
		final Account ac = plugin.getIFHApi().getAccount(new EconomyEntity(EconomyType.PLAYER, player.getUniqueId(), player.getName()), acname);
		if(ac == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Pay.AccountDontExist")));
			return;
		}
		if(!plugin.getIFHApi().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NoWithdrawRights")));
			return;
		}
		QuickPayAccount qpa = (QuickPayAccount) plugin.getMysqlHandler().getData(
				MysqlHandler.Type.QUICKPAYACCOUNT, "`account_currency` = ? AND `player_uuid` = ?",
				ac.getCurrency().getUniqueName(), ac.getOwner().getUUID().toString());
		if(qpa != null)
		{
			qpa.setAccountID(ac.getID());
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.QUICKPAYACCOUNT, qpa, 
					"`account_currency` = ? AND `player_uuid` = ?",
					ac.getCurrency().getUniqueName(), ac.getOwner().getUUID().toString());
		} else
		{
			qpa = new QuickPayAccount(ac.getOwner().getUUID(), ac.getID(), ac.getCurrency().getUniqueName());
			plugin.getMysqlHandler().create(MysqlHandler.Type.QUICKPAYACCOUNT, qpa);
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.SetQuickPay.SetQuickPay")
				.replace("%acname%", acname)
				.replace("%currency%", ac.getCurrency().getUniqueName())));
		return;
	}
}