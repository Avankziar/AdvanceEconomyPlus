package me.avankziar.aep.spigot.cmd.cst.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.cmd.tree.CommandConstructor;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.Utility;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class GiveConsole extends ArgumentModule implements CommandExecutor
{
	private AEP plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public GiveConsole(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
	{
		super(ac);
		this.plugin = AEP.getPlugin();
		this.cc = cc;
		this.ac = ac;
		this.cst = cst;
	}
	
	@Override
	public boolean onCommand(CommandSender senders, Command cmd, String lable, String[] args) 
	{
		if(!(senders instanceof ConsoleCommandSender))
		{
			senders.sendMessage("Cmd only for Console!");
			return false;
		}
		ConsoleCommandSender sender = (ConsoleCommandSender) senders;
		if(cst == CommandStructurType.SINGLE)
		{
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
					middlePart(sender, cmdString, args, zero, one, two, three, four);
				}
			}.runTaskAsynchronously(plugin);
		}
		return true;
	}
	
	@Override
	public void run(CommandSender senders, String[] args) throws IOException
	{
		if(!(senders instanceof ConsoleCommandSender))
		{
			senders.sendMessage("Cmd only for Console!");
			return;
		}
		ConsoleCommandSender sender = (ConsoleCommandSender) senders;
		if(cst == CommandStructurType.NESTED)
		{
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
					middlePart(sender, cmdString, args, zero, one, two, three, four);
				}
			}.runTaskAsynchronously(plugin);
		}
	}
	
	private void middlePart(ConsoleCommandSender sender, String cmdString, String[] args,
			int zero, int one, int two, int three, int four)
	{
		if(args.length < three)
		{
			sender.sendMessage(ChatApiOld.tl(
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
					sender.sendMessage(ChatApiOld.tl(
							plugin.getYamlHandler().getLang().getString("EntityNotExist")));
					return;
				}
			}
			amount = Double.parseDouble(as);
			from = plugin.getIFHApi().getAccount(
					new EconomyEntity(EconomyEntity.EconomyType.PLAYER, fromuuid, fromName), fromAcName);
			if(from == null)
			{
				sender.sendMessage(ChatApiOld.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.StartAccountDontExist")));
				return;
			}
		} else
		{
			sender.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[two])));
			return;
		}
		if(from.getCurrency() == null)
		{
			sender.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", from.getAccountName()));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			sender.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", as)));
			return;
		}
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
		endpart(sender, from, category, comment, amount);
	}
	
	private void endpart(CommandSender sender, Account from, String category, String comment, double amount)
	{
		EconomyAction ea = null;
		if(category == null)
		{
			ea = plugin.getIFHApi().deposit(from, amount);
		} else
		{
			ea = plugin.getIFHApi().deposit(from, amount, 
					OrdererType.PLUGIN, "Console", category, comment);
		}
		if(!ea.isSuccess())
		{
			sender.sendMessage(ChatApiOld.tl(ea.getDefaultErrorMessage()));
			return;
		}
		ArrayList<String> list = new ArrayList<>();
		for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Give.Deposit"))
		{
			String a = s.replace("%formaccount%", from.getAccountName())
			.replace("%formatdeposit%", plugin.getIFHApi().format(ea.getDepositAmount(), from.getCurrency()))
			.replace("%category%", category != null ? category : "/")
			.replace("%comment%", comment != null ? comment : "/");
			list.add(a);
		}
		for(String s : list)
		{
			sender.sendMessage(ChatApiOld.tl(s));
		}
		Transfer.sendToOther(plugin, from, list);
	}
}