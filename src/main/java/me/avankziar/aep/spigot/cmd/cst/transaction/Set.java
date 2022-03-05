package main.java.me.avankziar.aep.spigot.cmd.cst.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.spigot.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;

public class Set extends ArgumentModule implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public Set(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
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
			int two = 2;
			int three = 3;
			int four = 4;
			new BukkitRunnable()
			{
				
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero, one, two, three, four);
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
			int two = 2+1;
			int three = 3+1;
			int four = 4+1;
			new BukkitRunnable()
			{
				
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero, one, two, three, four);
				}
			}.runTaskAsynchronously(plugin);
		}
	}
	
	/*
	 * set <Player> <AccountName> <amount> [category] [comment...]
	 */
	private void middlePart(Player player, String cmdString, String[] args,
			int zero, int one, int two, int three, int four)
	{
		if(args.length < three)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cmdString)
					.replace("%amount%", three+" - "+four)));
			return;
		}
		String fromName = player.getName();
		UUID fromuuid = player.getUniqueId();
		String fromAcName = null;
		Account from = null;
		
		String category = null;
		String comment = null;
		String as = null;
		double amount = 0.0;
		int catStart = four;
		if(MatchApi.isDouble(args[three]))
		{
			if(args.length < four)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
						.replace("%cmd%", cmdString.trim())
						.replace("%amount%", String.valueOf(three))));
				return;
			}
			fromName = args[zero];
			fromuuid = Utility.convertNameToUUID(fromAcName, EconomyEntity.EconomyType.PLAYER);
			if(fromuuid == null)
			{
				fromuuid = Utility.convertNameToUUID(fromAcName, EconomyEntity.EconomyType.ENTITY);
				if(fromuuid == null)
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("EntityNotExist")));
					return;
				}
			}
			fromAcName = args[one];
			as = args[two];
			amount = Double.parseDouble(as);
			from = plugin.getIFHApi().getAccount(
					new EconomyEntity(EconomyEntity.EconomyType.PLAYER, fromuuid, fromName), fromAcName);
			if(from == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.StartAccountDontExist")));
				return;
			}
		} else
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[two])));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", as)));
			return;
		}
		Account voids = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.VOID, from.getCurrency());
		if(args.length >= catStart+2)
		{
			category = args[catStart];
			catStart++;
			StringBuilder sb = new StringBuilder();
			while(catStart < args.length)
			{
				sb.append(args[catStart]);
				if(catStart+1 != args.length)
				{
					sb.append(" ");
				}
				catStart++;
			}
			comment = sb.toString();
		}
		endpart(player, from, voids, category, comment, amount);
	}
	
	private void endpart(Player player, Account from, Account voids, String category, String comment, double amount)
	{
		EconomyAction ea = null;
		if(voids == null && category == null)
		{
			ea = plugin.getIFHApi().withdraw(from, from.getBalance());
		} else if(voids == null && category != null)
		{
			ea = plugin.getIFHApi().withdraw(from, from.getBalance(), OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		} else if(voids != null && category == null)
		{
			ea = plugin.getIFHApi().transaction(from, voids, from.getBalance());
		} else if(voids != null && category != null)
		{
			ea = plugin.getIFHApi().transaction(from, voids, from.getBalance(),
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		}
		if(!ea.isSuccess())
		{
			player.sendMessage(ChatApi.tl(ea.getDefaultErrorMessage()));
			return;
		}
		if(category == null)
		{
			ea = plugin.getIFHApi().deposit(from, amount);
		} else
		{
			ea = plugin.getIFHApi().deposit(from, amount, 
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		}
		if(!ea.isSuccess())
		{
			player.sendMessage(ChatApi.tl(ea.getDefaultErrorMessage()));
			return;
		}
		ArrayList<String> list = new ArrayList<>();
		if(voids == null)
		{
			for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Set.Setting"))
			{
				s.replace("%fromaccount%", from.getAccountName())
				.replace("%fromatdeposit%", plugin.getIFHApi().format(ea.getDepositAmount(), from.getCurrency()))
				.replace("%category%", category != null ? category : "/")
				.replace("%comment%", comment != null ? comment : "/");
				list.add(s);
			}
		} else
		{
			for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Set.Transaction"))
			{
				s.replace("%fromaccount%", from.getAccountName())
				.replace("%toaccount%", voids.getAccountName())
				.replace("%fromatwithdraw%", plugin.getIFHApi().format(ea.getWithDrawAmount(), from.getCurrency()))
				.replace("%fromatdeposit%", plugin.getIFHApi().format(ea.getDepositAmount(), from.getCurrency()))
				.replace("%category%", category != null ? category : "/")
				.replace("%comment%", comment != null ? comment : "/");
				list.add(s);
			}
		}
		for(String s : list)
		{
			player.sendMessage(ChatApi.tl(s));
		}
		Pay.sendToOther(plugin, from, list);
	}
}