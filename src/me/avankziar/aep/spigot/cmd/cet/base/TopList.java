package me.avankziar.aep.spigot.cmd.cet.base;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.cmd.tree.CommandConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import me.avankziar.aep.spigot.handler.ConvertHandler;
import me.avankziar.aep.spigot.handler.LogHandler;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;

public class TopList extends ArgumentModule implements CommandExecutor
{
	private AEP plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public TopList(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
	{
		super(ac);
		this.plugin = AEP.getPlugin();
		this.cc = cc;
		this.ac = ac;
		this.cst = cst;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Cmd only for Players!");
			return false;
		}
		Player player = (Player) sender;
		if(cst == CommandStructurType.SINGLE)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			String cmdString = cc.getCommandString();
			int zero = 0;
			int one = 1;
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero, one);
				}
			}.runTaskAsynchronously(plugin);
		}
		return true;
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
		if(cst == CommandStructurType.NESTED)
		{
			if(!player.hasPermission(ac.getPermission()))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
			String cmdString = ac.getCommandString();
			int zero = 0+1;
			int one = 1+1;
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero, one);
				}
			}.runTaskAsynchronously(plugin);
		}
	}
	
	// aep toplist <currency> <page> <withvoidAndtax>
	
	private void middlePart(Player player, String cmdstring, String[] args,
			int zero, int one)
	{
		if(args.length < one)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cmdstring)
					.replace("%amount%", String.valueOf(one+1))));
			return;
		}
		int page = 0;
		int quantity = 10;
		
		if(args.length >= one+1)
		{
			String pagenumber = args[one];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
				if(!MatchApi.isPositivNumber(page))
				{
					page = 0;
				}
			}
		}
		
		String currency = args[zero];
		if(page < 0)
		{
			page = 0;
		}
		boolean withtaxAndvoid = false;
		if(args.length >= one+2)
		{
			withtaxAndvoid = true;
		}
		ArrayList<Account> top = new ArrayList<>();
		if(withtaxAndvoid)
		{
			top = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getList(MysqlType.ACCOUNT_SPIGOT, "`balance` DESC", page*quantity, quantity, "`account_currency` = ?", currency));
		} else
		{
			top = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getList(MysqlType.ACCOUNT_SPIGOT, "`balance` DESC", page*quantity, quantity, 
							"`account_currency` = ? AND `account_category` != ? AND `account_category` != ?", 
							currency, AccountCategory.TAX.toString(), AccountCategory.VOID.toString()));
		}
		
		if(top.size() <= 0)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Top.NotEnoughValues")));
			return;
		}
		int last = plugin.getMysqlHandler().lastID(MysqlType.ACCOUNT_SPIGOT);
		boolean lastpage = page*quantity >= last;
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<BaseComponent>();
		m1.add(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.Top.Headline")
				.replace("%page%", String.valueOf(page))
				.replace("%currency%", currency)));
		msg.add(m1);
		int i = (page*quantity)+1;
		for(Account ac : top)
		{
			ArrayList<BaseComponent> m2 = new ArrayList<BaseComponent>();
			m2.add(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("Cmd.Top.TopLine")
					.replace("%place%", String.valueOf(i))
					.replace("%accountid%", String.valueOf(ac.getID()))
					.replace("%owner%", ac.getOwner().getName())
					.replace("%accountname%", ac.getAccountName())
					.replace("%format%", plugin.getIFHApi().format(ac.getBalance(), ac.getCurrency()))));
			msg.add(m2);
			i++;
		}
		LogHandler.pastNextPage(plugin, player, msg, page, lastpage, cmdstring, currency, null);
	}
}