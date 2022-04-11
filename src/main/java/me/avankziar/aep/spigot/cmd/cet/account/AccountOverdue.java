package main.java.me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class AccountOverdue extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public AccountOverdue(ArgumentConstructor ac)
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
		int overdueac = plugin.getMysqlHandler().getCount(Type.PLAYERDATA, "`unixtime` < ?", overdate);
		ArrayList<AEPUser> aepu = ConvertHandler.convertListI(plugin.getMysqlHandler().getAllListAt(
				MysqlHandler.Type.PLAYERDATA, "`unixtime` ASC", "`unixtime` < ?", overdate));
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m0 = new ArrayList<>();
		m0.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Overdue.FirstLine")
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
				int account = plugin.getMysqlHandler().getCount(MysqlHandler.Type.ACCOUNT, "`owner_uuid` = ?", u.getUUID().toString());
				sb.append(plugin.getYamlHandler().getLang().getString("Cmd.Account.Overdue.AccountCount")
						.replace("%count%", String.valueOf(account)));
				sb.append("~!~");
				ArrayList<Account> aclist = ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(
						MysqlHandler.Type.ACCOUNT, "`id` ASC", "`owner_uuid` = ?", u.getUUID().toString()));
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
				m1.add(ChatApi.hoverEvent("&e"+u.getName()+"&f, ", Action.SHOW_TEXT, sb.toString()));
				msg.add(m1);
		}
		for(ArrayList<BaseComponent> list : msg)
		{
			TextComponent tx = ChatApi.tctl("");
			tx.setExtra(list);
			player.spigot().sendMessage(tx);	
		}
	}
}