package main.java.me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AccountManagement;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class AccountPermissionInfo extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public AccountPermissionInfo(ArgumentConstructor ac)
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
	 * aep account permissioninfo [Spielername]
	 */
	private void middlePart(Player player, String[] args)
	{
		String playername = player.getName();
		if(args.length >= 3)
		{
			playername = args[2];
		}
		ArrayList<Account> list = plugin.getIFHApi().getAccounts(plugin.getIFHApi().getEntity(playername));
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Account.PermissionInfo.NoAccountFound")));
			return;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.PermissionInfo.Headline").replace("%player%", playername)));
		msg.add(m1);
		ArrayList<BaseComponent> m2 = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for(AccountManagementType a : new ArrayList<AccountManagementType>(EnumSet.allOf(AccountManagementType.class)))
		{
			sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Player."+a.toString()));
			sb.append("~!~");
		}
		m2.add(ChatApi.hoverEvent(
				plugin.getYamlHandler().getLang().getString("Cmd.Account.PermissionInfo.HoverInfo"),
				Action.SHOW_TEXT, sb.toString()));
		msg.add(m2);
		for(Account ac : list)
		{
			if(ac.getCurrency() == null)
			{
				continue;
			}
			LinkedHashMap<UUID, ArrayList<AccountManagementType>> accountsAM = new LinkedHashMap<>();
			ArrayList<AccountManagement> aml = new ArrayList<>();
			try
			{
				aml = ConvertHandler.convertListIX(plugin.getMysqlHandler().getAllListAt(
						MysqlHandler.Type.ACCOUNTMANAGEMENT, "`id` ASC", "`account_id` = ?", ac.getID()));
			} catch(Exception e)
			{
				continue;
			}
			for(AccountManagement am : aml)
			{
				if(accountsAM.containsKey(am.getUUID()))
				{
					ArrayList<AccountManagementType> amt = accountsAM.get(am.getUUID());
					if(!amt.contains(am.getMaganagementType()))
					{
						amt.add(am.getMaganagementType());
					}
					accountsAM.put(am.getUUID(), amt);
				} else
				{
					ArrayList<AccountManagementType> amt = new ArrayList<>();
					amt.add(am.getMaganagementType());
					accountsAM.put(am.getUUID(), amt);
				}
			}
			ArrayList<BaseComponent> m3 = new ArrayList<>();
			StringBuilder sbu = new StringBuilder();
			if(accountsAM.isEmpty())
			{
				sbu.append("&c"+ac.getID()+" &f| &4"+ac.getOwner().getName()+"~!~");
			} else
			{
				sbu.append("&c"+ac.getID()+" &f| &4"+ac.getOwner().getName()+"~!~");
				for(UUID uuid : accountsAM.keySet())
				{
					sbu.append("&#0047AB"+plugin.getIFHApi().getEntity(uuid).getName()+" ");
					for(AccountManagementType amt : accountsAM.get(uuid))
					{
						sbu.append(plugin.getYamlHandler().getLang().getString("Cmd.Player.Only."+amt.toString())+" ");
					}
					sbu.append("~!~");
				}
			}
			m3.add(ChatApi.hoverEvent("&#FF6E4A"+ac.getAccountName()+"&r, ", Action.SHOW_TEXT, sbu.toString()));
			msg.add(m3);
		}
		for(List<BaseComponent> m : msg)
		{
			TextComponent n = ChatApi.tctl("");
			n.setExtra(m);
			player.spigot().sendMessage(n);
		}
	}
}