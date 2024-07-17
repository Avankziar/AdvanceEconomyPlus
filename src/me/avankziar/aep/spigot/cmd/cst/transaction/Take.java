package me.avankziar.aep.spigot.cmd.cst.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.cmd.tree.CommandConstructor;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.Utility;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class Take extends ArgumentModule implements CommandExecutor
{
	private AEP plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public Take(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
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
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
	 * take <Player> <AccountName> <amount> [category] [comment...]
	 */
	private void middlePart(Player player, String cmdString, String[] args,
			int zero, int one, int two, int three, int four)
	{
		if(args.length < three)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cmdString)
					.replace("%amount%", three+"")));
			return;
		}
		String fromName = args[zero];
		UUID fromuuid;
		String fromAcName = args[one];
		Account from = null;
		
		String category = null;
		String comment = null;
		String as = Transfer.convertDecimalSeperator(args[two]);
		double amount = 0.0;
		int catStart = four;
		if(MatchApi.isDouble(as))
		{
			fromName = args[zero];
			fromuuid = Utility.convertNameToUUID(fromName, EconomyEntity.EconomyType.PLAYER);
			if(fromuuid == null)
			{
				fromuuid = Utility.convertNameToUUID(fromName, EconomyEntity.EconomyType.ENTITY);
				if(fromuuid == null)
				{
					player.sendMessage(ChatApiOld.tl(
							plugin.getYamlHandler().getLang().getString("EntityNotExist")));
					return;
				}
			}
			amount = Double.parseDouble(as);
			from = plugin.getIFHApi().getAccount(
					new EconomyEntity(EconomyEntity.EconomyType.PLAYER, fromuuid, fromName), fromAcName);
			if(from == null)
			{
				player.sendMessage(ChatApiOld.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.StartAccountDontExist")));
				return;
			}
		} else
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[two])));
			return;
		}
		if(from.getCurrency() == null)
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", from.getAccountName()));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", as)));
			return;
		}
		Account voids = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.VOID, from.getCurrency());
		if(args.length >= catStart+1)
		{
			String[] s = Transfer.getCategoryAndComment(args, catStart);
			category = s[0];
			comment = s[1];
		} else if(args.length >= catStart)
		{
			category = Transfer.getCategory(args, catStart);
			comment = "N/A";
		} else
		{
			category = "N/A";
			comment = "N/A";
		}
		endpart(player, from, voids, category, comment, amount);
	}
	
	private void endpart(Player player, Account from, Account voids, String category, String comment, double amount)
	{
		EconomyAction ea = null;
		if(voids == null && category == null)
		{
			ea = plugin.getIFHApi().withdraw(from, amount);
		} else if(voids == null && category != null)
		{
			ea = plugin.getIFHApi().withdraw(from, amount, OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		} else if(voids != null && category == null)
		{
			ea = plugin.getIFHApi().transaction(from, voids, amount);
		} else if(voids != null && category != null)
		{
			ea = plugin.getIFHApi().transaction(from, voids, amount, 
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		}
		if(!ea.isSuccess())
		{
			player.sendMessage(ChatApiOld.tl(ea.getDefaultErrorMessage()));
			return;
		}
		ArrayList<String> list = new ArrayList<>();
		if(voids == null)
		{
			for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Take.Withdraw"))
			{
				String a = s.replace("%fromaccount%", from.getAccountName())
				.replace("%formatwithdraw%", plugin.getIFHApi().format(ea.getWithDrawAmount(), from.getCurrency()))
				.replace("%category%", category != null ? category : "/")
				.replace("%comment%", comment != null ? comment : "/");
				list.add(a);
			}
		} else
		{
			for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Take.Transaction"))
			{
				String a = s.replace("%fromaccount%", from.getAccountName())
				.replace("%toaccount%", voids.getAccountName())
				.replace("%formatwithdraw%", plugin.getIFHApi().format(ea.getWithDrawAmount(), from.getCurrency()))
				.replace("%formatdeposit%", plugin.getIFHApi().format(ea.getDepositAmount(), from.getCurrency()))
				.replace("%category%", category != null ? category : "/")
				.replace("%comment%", comment != null ? comment : "/");
				list.add(a);
			}
		}
		for(String s : list)
		{
			player.sendMessage(ChatApiOld.tl(s));
		}
		Transfer.sendToOther(plugin, from, list, player.getUniqueId());
	}
}