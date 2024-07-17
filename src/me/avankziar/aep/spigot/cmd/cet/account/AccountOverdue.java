package me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.TimeHandler;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.ConvertHandler;
import me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class AccountOverdue extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public AccountOverdue(ArgumentConstructor ac)
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
	 * aep account overdue
	 */
	private void middlePart(Player player, String[] args) throws IOException
	{
		int days = plugin.getYamlHandler().getConfig().getInt("Do.OverdueTimeInDays", 90);
		long overdate = System.currentTimeMillis()-(long) days*1000*60*60*24;
		int overdueac = plugin.getMysqlHandler().getCount(MysqlType.PLAYERDATA, "`unixtime` < ?", overdate);
		ArrayList<AEPUser> aepu = ConvertHandler.convertListI(plugin.getMysqlHandler().getFullList(
				MysqlType.PLAYERDATA, "`unixtime` ASC", "`unixtime` < ?", overdate));
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m0 = new ArrayList<>();
		m0.add(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Overdue.FirstLine")
				.replace("%count%", String.valueOf(overdueac))
				.replace("%days%", String.valueOf(days))));
		msg.add(m0);
		for(AEPUser u : aepu)
		{
				ArrayList<BaseComponent> m1 = new ArrayList<>();
				StringBuilder sb = new StringBuilder();
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Account.Overdue.LastLogin")
						.replace("%date%", TimeHandler.getTime(u.getLastTimeLogin())));
				sb.append("~!~");
				int account = plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_SPIGOT, "`owner_uuid` = ?", u.getUUID().toString());
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Account.Overdue.AccountCount")
						.replace("%count%", String.valueOf(account)));
				sb.append("~!~");
				ArrayList<Account> aclist = ConvertHandler.convertListII(plugin.getMysqlHandler().getFullList(
						MysqlType.ACCOUNT_SPIGOT, "`id` ASC", "`owner_uuid` = ?", u.getUUID().toString()));
				LinkedHashMap<String,Double> ecvalue = new LinkedHashMap<>();
				for(Account acc : aclist)
				{
					if(acc.getCurrency() == null)
					{
						player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", acc.getAccountName()));
						return;
					}
					if(ecvalue.containsKey(acc.getCurrency().getUniqueName()))
					{
						double amount = acc.getBalance()+ecvalue.get(acc.getCurrency().getUniqueName());
						ecvalue.put(acc.getCurrency().getUniqueName(), amount);
					} else
					{
						ecvalue.put(acc.getCurrency().getUniqueName(), acc.getBalance());
					}
				}
				int ii = 0;
				for(String ec : ecvalue.keySet())
				{
					String format = plugin.getIFHApi().format(ecvalue.get(ec), plugin.getIFHApi().getCurrency(ec));
					sb.append(format);
					ii++;
					if(ii < ecvalue.size())
					{
						sb.append("~!~");
					}
				}
				m1.add(ChatApiOld.hover("&e"+u.getName()+"&f, ", Action.SHOW_TEXT, sb.toString()));
				msg.add(m1);
		}
		for(ArrayList<BaseComponent> list : msg)
		{
			TextComponent tx = ChatApiOld.tctl("");
			tx.setExtra(list);
			player.spigot().sendMessage(tx);	
		}
	}
}