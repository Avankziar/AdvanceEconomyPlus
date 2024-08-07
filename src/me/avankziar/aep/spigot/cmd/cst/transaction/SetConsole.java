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
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class SetConsole extends ArgumentModule implements CommandExecutor
{
	private AEP plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public SetConsole(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
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
		Account voids = plugin.getIFHApi().getDefaultAccount(fromuuid, AccountCategory.VOID, from.getCurrency());
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
		endpart(sender, from, voids, category, comment, amount);
	}
	
	private void endpart(ConsoleCommandSender sender, Account from, Account voids, String category, String comment, double amount)
	{
		EconomyAction eaw = null;
		if(voids == null && category == null)
		{
			eaw = plugin.getIFHApi().withdraw(from, from.getBalance());
		} else if(voids == null && category != null)
		{
			eaw = plugin.getIFHApi().withdraw(from, from.getBalance(), OrdererType.PLUGIN, "Console", category, comment);
		} else if(voids != null && category == null)
		{
			eaw = plugin.getIFHApi().transaction(from, voids, from.getBalance());
		} else if(voids != null && category != null)
		{
			eaw = plugin.getIFHApi().transaction(from, voids, from.getBalance(),
					OrdererType.PLUGIN, "Console", category, comment);
		}
		if(!eaw.isSuccess())
		{
			sender.sendMessage(ChatApiOld.tl(eaw.getDefaultErrorMessage()));
			return;
		}
		EconomyAction ead = null;
		if(category == null)
		{
			ead = plugin.getIFHApi().deposit(from, amount);
		} else
		{
			ead = plugin.getIFHApi().deposit(from, amount, 
					OrdererType.PLUGIN, "Console", category, comment);
		}
		if(!ead.isSuccess())
		{
			sender.sendMessage(ChatApiOld.tl(ead.getDefaultErrorMessage()));
			return;
		}
		ArrayList<String> list = new ArrayList<>();
		if(voids == null)
		{
			String format = plugin.getIFHApi().format(ead.getDepositAmount(), from.getCurrency());
			for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Set.Setting"))
			{
				String a = s.replace("%fromaccount%", from.getAccountName())
				.replace("%formatdeposit%", format)
				.replace("%category%", category != null ? category : "/")
				.replace("%comment%", comment != null ? comment : "/");
				list.add(a);
			}
		} else
		{
			String wformat = plugin.getIFHApi().format(eaw.getWithDrawAmount(), from.getCurrency());
			String dformat = plugin.getIFHApi().format(ead.getDepositAmount(), from.getCurrency());
			for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Set.Transaction"))
			{
				String a = s.replace("%fromaccount%", from.getAccountName())
				.replace("%toaccount%", voids.getAccountName())
				.replace("%formatwithdraw%", wformat)
				.replace("%formatdeposit%", dformat)
				.replace("%category%", category != null ? category : "/")
				.replace("%comment%", comment != null ? comment : "/");
				list.add(a);
			}
		}
		for(String s : list)
		{
			sender.sendMessage(ChatApiOld.tl(s));
		}
		Transfer.sendToOther(plugin, from, list);
	}
}