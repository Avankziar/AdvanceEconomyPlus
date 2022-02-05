package main.java.me.avankziar.aep.spigot.cmd.cst.transaction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.api.economy.CurrencyHandler;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.CommandStructurType;
import main.java.me.avankziar.aep.spigot.object.TaxationCase;
import main.java.me.avankziar.aep.spigot.object.TaxationSet;
import main.java.me.avankziar.aep.spigot.object.ne_w.AEPUser;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.spigot.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;

public class Pay implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public Pay(AdvancedEconomyPlus plugin, CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
	{
		this.plugin = plugin;
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
		String cmdString;
		if(cst == CommandStructurType.SINGLE)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			cmdString = cc.getCommandString();
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
		} else
		{
			if(!player.hasPermission(ac.getPermission()))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			int zero = 0+1;
			int one = 1+1;
			int two = 2+1;
			int three = 3+1;
			int four = 4+1;
			cmdString = ac.getCommandString();
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
	
	private void middlePart(Player player, String cmdString, String[] args,
			int zero, int one, int two, int three, int four)
	{
		if(args.length < three)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cc.getCommandString())
					.replace("%amount%", three+" - "+four)));
			return;
		}
		String fromName = player.getName();
		UUID fromuuid = player.getUniqueId();
		String fromAcName = null;
		Account from = null;
		
		String toName = player.getName();
		UUID touuid = player.getUniqueId();
		String toAcName = null;
		Account to = null;
		
		String category = null;
		String comment = null;
		String as = null;
		double amount = 0.0;
		int catStart = three;
		if(MatchApi.isDouble(args[zero]))
		{
			as = args[zero];
			toName = args[one];
			toAcName = args[two];
			amount = Double.parseDouble(as);
			AEPUser fromuser = (AEPUser) plugin.getMysqlHandler().getData(
					MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", fromuuid.toString());
			if(fromuser == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
				return;
			}
			from = plugin.getIFHApi().getAccount(fromuser.getShortPayAccountID());
			if(from == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.ShortPayAccountDontExist")));
				return;
			}
			touuid = Utility.convertNameToUUID(toAcName, EconomyEntity.EconomyType.PLAYER);
			if(touuid == null)
			{
				touuid = Utility.convertNameToUUID(toAcName, EconomyEntity.EconomyType.ENTITY);
				if(touuid == null)
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("EntityNotExist")));
					return;
				}
			}	
			to = plugin.getIFHApi().getAccount(new EconomyEntity(EconomyEntity.EconomyType.PLAYER, touuid, toName), toAcName);
			if(to == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.TargetAccountDontExist")));
				return;
			}
		} else if(MatchApi.isDouble(args[one]))
		{
			if(args.length < four)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
						.replace("%cmd%", cmdString.trim())
						.replace("%amount%", String.valueOf(four))));
				return;
			}
			fromAcName = args[zero];
			as = args[one];
			toName = args[two];
			toAcName = args[three];
			amount = Double.parseDouble(as);
			catStart = four;
			from = plugin.getIFHApi().getAccount(
					new EconomyEntity(EconomyEntity.EconomyType.PLAYER, fromuuid, fromName), fromAcName);
			if(from == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.StartAccountDontExist")));
				return;
			}
			touuid = Utility.convertNameToUUID(toAcName, EconomyEntity.EconomyType.PLAYER);
			if(touuid == null)
			{
				touuid = Utility.convertNameToUUID(toAcName, EconomyEntity.EconomyType.ENTITY);
				if(touuid == null)
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("EntityNotExist")));
					return;
				}
			}	
			to = plugin.getIFHApi().getAccount(new EconomyEntity(EconomyEntity.EconomyType.PLAYER, touuid, toName), toAcName);
			if(to == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.TargetAccountDontExist")));
				return;
			}
		} else if(MatchApi.isDouble(args[two]))
		{
			fromAcName = args[zero];
			toName = player.getName();
			toAcName = args[one];
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
			to = plugin.getIFHApi().getAccount(new EconomyEntity(EconomyEntity.EconomyType.PLAYER, touuid, toName), toAcName);
			if(to == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.TargetAccountDontExist")));
				return;
			}
		} else
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[zero]+"/"+args[one]+"/"+args[two])));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", as)));
			return;
		}
		if(!from.getCurrency().toString().equalsIgnoreCase(to.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.NotSameCurrency")));
			return;
		}
		Account tax = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.TAX, from.getCurrency());
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
		}
		endpart(player, from, to, tax, category, comment, amount);
	}
	
	private void endpart(Player player, Account from, Account to, Account tax, String category, String comment, double amount)
	{
		LinkedHashMap<TaxationCase, TaxationSet> map = CurrencyHandler.taxationMap.get(from.getCurrency().getUniqueName());
		TaxationSet ts = map.containsKey(TaxationCase.TRANSACTION_BETWEEN_PLAYERS) ? map.get(TaxationCase.TRANSACTION_BETWEEN_PLAYERS) : null;
		double taxation = ts != null ? ts.getTaxInPercent() : 0.0;
		boolean taxAreExclusive = ts != null ? ts.isTaxAreExclusive() : true;
		EconomyAction ea = null;
		if(tax == null && category == null)
		{
			ea = plugin.getIFHApi().transaction(from, to, amount);
		} else if(tax == null && category != null)
		{
			ea = plugin.getIFHApi().transaction(from, to, amount, OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		} else if(tax != null && category == null)
		{
			ea = plugin.getIFHApi().transaction(from, to, amount, taxation, taxAreExclusive, tax);
		} else if(tax != null && category != null)
		{
			ea = plugin.getIFHApi().transaction(from, to, amount, taxation, taxAreExclusive, tax, 
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		}
		if(!ea.isSuccess())
		{
			player.sendMessage(ChatApi.tl(ea.getDefaultErrorMessage()));
			return;
		}
		ArrayList<String> list = new ArrayList<>();
		for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Pay.Transaction"))
		{
			s.replace("%fromaccount%", from.getAccountName())
			.replace("%toaccount%", to.getAccountName())
			.replace("%fromatwithdraw%", plugin.getIFHApi().format(ea.getWithDrawAmount(), from.getCurrency()))
			.replace("%fromatdeposit%", plugin.getIFHApi().format(ea.getDepositAmount(), from.getCurrency()))
			.replace("%fromattax%", String.valueOf(ea.getTaxAmount()))
			.replace("%category%", category != null ? category : "/")
			.replace("%comment%", comment != null ? comment : "/");
			list.add(s);
		}
		for(String s : list)
		{
			player.sendMessage(ChatApi.tl(s));
		}
		//ADDME Other Owner toAccount | Sound? | Moneyflow beachten
	}
}