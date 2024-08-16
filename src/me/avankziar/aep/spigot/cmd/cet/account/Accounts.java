package me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.AccountManagement;
import me.avankziar.aep.general.objects.DefaultAccount;
import me.avankziar.aep.general.objects.QuickPayAccount;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.ConfigHandler;
import me.avankziar.aep.spigot.handler.ConvertHandler;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class Accounts extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	private static String d1 = "aep_account";
	
	public Accounts(ArgumentConstructor ac)
	{
		super(ac);
		ConfigHandler.debug(d1, "> /aep account inti");
		this.plugin = AEP.getPlugin();
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(args.length > 1)
		{
			ConfigHandler.debug(d1, "> runs /aep account ...");
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("OtherCmd")));
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
				MysqlType.PLAYERDATA, "`player_name` = ?", playername);
		if(aepu == null)
		{
			ConfigHandler.debug(d1, "> aepu == null");
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		ArrayList<AccountManagement> aml = ConvertHandler.convertListIX(plugin.getMysqlHandler().getFullList(
				MysqlType.ACCOUNTMANAGEMENT, "`id` ASC", "`player_uuid` = ?", aepu.getUUID().toString()));
		if(aml.isEmpty())
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Account.HaveNoAccounts")));
			return;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Player.Headline").replace("%player%", playername)));
		msg.add(m1);
		LinkedHashMap<Integer, ArrayList<AccountManagementType>> accountIDs = new LinkedHashMap<>();
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
			if(ac.getCurrency() == null) //If currency is not loaded
			{
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
			if(amt.contains(AccountManagementType.CAN_SEE_BALANCE))
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountBalance")
						.replace("%balance%", plugin.getIFHApi().format(ac.getBalance(), ac.getCurrency())));
				sb.append("~!~");
			}
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountTypeAndCategory")
					.replace("%type%", plugin.getIFHApi().getAccountType(ac.getType()))
					.replace("%category%", plugin.getIFHApi().getAccountCategory(ac.getCategory())));
			sb.append("~!~");
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountPredefined")
					.replace("%predefined%", plugin.getIFHApi().getBoolean(ac.isPredefinedAccount())));
			sb.append("~!~");
			DefaultAccount dacc = (DefaultAccount) plugin.getMysqlHandler().getData(MysqlType.DEFAULTACCOUNT,
					"`player_uuid` = ? AND `account_category` = ? AND `account_currency` = ?",
					aepu.getUUID().toString(), ac.getCategory().toString(), ac.getCurrency().getUniqueName());
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountDefault")
					.replace("%default%", plugin.getIFHApi().getBoolean(dacc != null)));
			sb.append("~!~");
			QuickPayAccount qpa = (QuickPayAccount) plugin.getMysqlHandler().getData(MysqlType.QUICKPAYACCOUNT,
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
			ml.add(ChatApiOld.hover(
					plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountColor").replace("%account%", ac.getAccountName())
					, Action.SHOW_TEXT, sb.toString()));
			ml.add(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Player.AccountSeperator")));
		}
		msg.add(ml);
		for(List<BaseComponent> m : msg)
		{
			TextComponent n = ChatApiOld.tctl("");
			n.setExtra(m);
			player.spigot().sendMessage(n);
		}
		return;
	}
}