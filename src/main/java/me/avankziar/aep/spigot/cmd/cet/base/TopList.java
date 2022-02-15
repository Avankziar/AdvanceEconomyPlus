package main.java.me.avankziar.aep.spigot.cmd.cet.base;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;

public class TopList extends ArgumentModule implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public TopList(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
	{
		super(ac);
		this.plugin = BaseConstructor.getPlugin();
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
	
	private void middlePart(Player player, String cmdstring, String[] args,
			int zero, int one)
	{
		if(args.length > one+1)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cmdstring)
					.replace("%amount%", String.valueOf(one+1))));
			return;
		}
		int page = 0;
		int quantity = 10;
		String pagenumber = args[zero];
		if(MatchApi.isInteger(pagenumber))
		{
			page = Integer.parseInt(pagenumber);
			if(!MatchApi.isPositivNumber(page))
			{
				page = 0;
			}
		}
		String currency = args[one];
		if(page < 0)
		{
			page = 0;
		}
		ArrayList<Account> top = ConvertHandler.convertListII(
				plugin.getMysqlHandler().getList(Type.ACCOUNT, "`balance`", page, quantity, "`account_currency` = ?", currency));
		if(top.size() <= 0)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Top.NotEnoughValues")));
			return;
		}
		int last = plugin.getMysqlHandler().lastID(Type.ACCOUNT);
		boolean lastpage = page*quantity >= last;
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<BaseComponent>();
		m1.add(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("Cmd.Top.Headline")
				.replace("%page%", String.valueOf(page))
				.replace("%currency%", currency)));
		msg.add(m1);
		for(Account ac : top)
		{
			ArrayList<BaseComponent> m2 = new ArrayList<BaseComponent>();
			m2.add(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Cmd.Top.TopLine")
					.replace("%place%", String.valueOf(page+1))
					.replace("%accountid%", String.valueOf(ac.getID()))
					.replace("%accountowner%", ac.getOwner().getName())
					.replace("%accountname%", ac.getAccountName())
					.replace("%format%", plugin.getIFHApi().format(ac.getBalance(), ac.getCurrency()))));
			msg.add(m2);
			page++;
		}
		LogHandler.pastNextPage(plugin, player, msg, page, lastpage, cmdstring, null, null);
	}
}