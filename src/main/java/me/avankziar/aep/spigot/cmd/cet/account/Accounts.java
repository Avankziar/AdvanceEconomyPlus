package main.java.me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.general.objects.AccountManagement;
import main.java.me.avankziar.aep.general.objects.DefaultAccount;
import main.java.me.avankziar.aep.general.objects.QuickPayAccount;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class Accounts extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	private static String d1 = "aep_account";
	
	public Accounts(ArgumentConstructor ac)
	{
		super(ac);
		ConfigHandler.debug(d1, "> /aep account inti");
		this.plugin = BaseConstructor.getPlugin();
		this.ac = ac;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		ConfigHandler.debug(d1, "> /aep account Begin");
		if(!(sender instanceof Player))
		{
			ConfigHandler.debug(d1, "> is not Player");
			sender.sendMessage("Cmd only for Players!");
			return;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(ac.getPermission()))
		{
			ConfigHandler.debug(d1, "> has not Permission");
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(args.length > 1)
		{
			ConfigHandler.debug(d1, "> runs /aep account ...");
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("OtherCmd")));
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				try
				{
					middlePart(player, args);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	/*
	 * aep account
	 */
	private void middlePart(Player player, String[] args) throws IOException
	{
		ConfigHandler.debug(d1, "> MiddlePart Begin");
		String playername = player.getName();
		AEPUser aepu = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", playername);
		if(aepu == null)
		{
			ConfigHandler.debug(d1, "> aepu == null");
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Player.Headline").replace("%player%", playername)));
		msg.add(m1);
		LinkedHashMap<Integer, ArrayList<AccountManagementType>> accountIDs = new LinkedHashMap<>();
		ArrayList<AccountManagement> aml = ConvertHandler.convertListIX(plugin.getMysqlHandler().getAllListAt(
				MysqlHandler.Type.ACCOUNTMANAGEMENT, "`id` ASC", "`player_uuid` = ?", aepu.getUUID().toString()));
		for(AccountManagement am : aml)
		{
			if(accountIDs.containsKey(am.getAccountID()))
			{
				ArrayList<AccountManagementType> amt = accountIDs.get(am.getAccountID());
				if(!amt.contains(am.getMaganagementType()))
				{
					amt.add(am.getMaganagementType());
				}
				accountIDs.put(am.getAccountID(), amt);
			} else
			{
				ArrayList<AccountManagementType> amt = new ArrayList<>();
				amt.add(am.getMaganagementType());
				accountIDs.put(am.getAccountID(), amt);
			}
		}
		ArrayList<BaseComponent> ml = new ArrayList<>();
		for(Entry<Integer, ArrayList<AccountManagementType>> e : accountIDs.entrySet())
		{
			Account ac = plugin.getIFHApi().getAccount(e.getKey());
			if(ac == null)
			{
				plugin.getIFHApi().removeManagementTypeFromAccount(e.getKey());
				continue;
			}
			ArrayList<AccountManagementType> amt = e.getValue();
			StringBuilder sb = new StringBuilder();
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountID")
					.replace("%id%", String.valueOf(ac.getID())));
			sb.append("~!~");
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountOwner")
					.replace("%owner%", ac.getOwner().getName()));
			sb.append("~!~");
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountBalance")
					.replace("%balance%", plugin.getIFHApi().format(ac.getBalance(), ac.getCurrency())));
			sb.append("~!~");
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountTypeAndCategory")
					.replace("%type%", plugin.getIFHApi().getAccountType(ac.getType()))
					.replace("%category%", plugin.getIFHApi().getAccountCategory(ac.getCategory())));
			sb.append("~!~");
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountPredefined")
					.replace("%predefined%", plugin.getIFHApi().getBoolean(ac.isPredefinedAccount())));
			sb.append("~!~");
			DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEFAULTACCOUNT,
					"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
					aepu.getUUID().toString(), ac.getCategory().toString(), ac.getCurrency().getUniqueName());
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountDefault")
					.replace("%default%", plugin.getIFHApi().getBoolean(dacc != null)));
			sb.append("~!~");
			QuickPayAccount qpa = (QuickPayAccount) plugin.getMysqlHandler().getData(MysqlHandler.Type.QUICKPAYACCOUNT,
					"`player_uuid` = ? AND `account_id` = ?", aepu.getUUID().toString(), ac.getID());
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountQuickPay")
					.replace("%quickpay%", plugin.getIFHApi().getBoolean(qpa != null)));
			sb.append("~!~");
			if(amt.contains(AccountManagementType.CAN_SET_OWNERSHIP))
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player."+AccountManagementType.CAN_SET_OWNERSHIP.toString()));
				sb.append("~!~");
			}
			if(amt.contains(AccountManagementType.CAN_ADMINISTRATE_ACCOUNT))
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player."+AccountManagementType.CAN_ADMINISTRATE_ACCOUNT.toString()));
				sb.append("~!~");
			}
			if(amt.contains(AccountManagementType.CAN_WITHDRAW))
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player."+AccountManagementType.CAN_WITHDRAW.toString()));
				sb.append("~!~");
			}
			if(amt.contains(AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT))
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player."+AccountManagementType.CAN_SET_AS_DEFAULT_ACCOUNT.toString()));
				sb.append("~!~");
			}
			if(amt.contains(AccountManagementType.CAN_SEE_LOG))
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player."+AccountManagementType.CAN_SEE_LOG.toString()));
				sb.append("~!~");
			}
			if(amt.contains(AccountManagementType.CAN_SEE_BALANCE))
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player."+AccountManagementType.CAN_SEE_BALANCE.toString()));
				sb.append("~!~");
			}
			if(amt.contains(AccountManagementType.CAN_RECEIVES_NOTIFICATIONS))
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player."+AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()));
				sb.append("~!~");
			}
			//sb.delete(sb.length()-3, sb.length());
			ml.add(ChatApi.hoverEvent(
					plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountColor").replace("%account%", ac.getAccountName())
					, Action.SHOW_TEXT, sb.toString()));
			ml.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountSeperator")));
		}
		msg.add(ml);
		LogHandler.pastNextPage(plugin, player, msg, 0, false, ac.getCommandString(), null, null);
		return;
	}
}