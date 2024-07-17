package me.avankziar.aep.spigot.cmd.cet.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.ConvertHandler;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class DeleteAllPlayerAccounts extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public DeleteAllPlayerAccounts(ArgumentConstructor ac)
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
	 * aep deleteallplayeraccounts <playername>
	 */
	private void middlePart(Player player, String[] args) throws IOException
	{
		String playername = args[1];
		AEPUser u = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlType.PLAYERDATA, "`player_name` = ?", playername);
		if(u == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		final int accamount = plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_SPIGOT, "`owner_uuid` = ?", u.getUUID().toString());
		ArrayList<Account> aclist = ConvertHandler.convertListII(plugin.getMysqlHandler().getFullList(
				MysqlType.ACCOUNT_SPIGOT, "`id` ASC", "`owner_uuid` = ?", u.getUUID().toString()));
		LinkedHashMap<String,Double> ecvalue = new LinkedHashMap<>();
		for(Account acc : aclist)
		{
			if(ecvalue.containsKey(acc.getCurrency().getUniqueName()))
			{
				double amount = acc.getBalance()+ecvalue.get(acc.getCurrency().getUniqueName());
				ecvalue.put(acc.getCurrency().getUniqueName(), amount);
			} else
			{
				ecvalue.put(acc.getCurrency().getUniqueName(), acc.getBalance());
			}
			plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNTMANAGEMENT, "`account_id` = ?", acc.getID());
		}
		StringBuilder sb = new StringBuilder();
		sb.append(plugin.getYamlHandler().getLang().getString("Cmd.DeleteAllPlayerAccounts.Hover"));
		for(String ec : ecvalue.keySet())
		{
			Double d = ecvalue.get(ec);
			EconomyCurrency ecu = plugin.getIFHApi().getCurrency(ec);
			if(ecu == null)
			{
				continue;
			}
			sb.append(plugin.getIFHApi().format(d, ecu)+"~!~");
		}
		plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_SPIGOT, "`owner_uuid` = ?", u.getUUID().toString());
		plugin.getMysqlHandler().deleteData(MysqlType.PLAYERDATA, "`player_uuid` = ?", u.getUUID().toString());
		player.spigot().sendMessage(ChatApiOld.hover(plugin.getYamlHandler().getLang().getString("Cmd.DeleteAllPlayerAccounts.Delete")
				.replace("%player%", playername)
				.replace("%amount%", String.valueOf(accamount)),
				Action.SHOW_TEXT,
				sb.toString()));
		return;
	}
}