package me.avankziar.aep.spigot.cmd.cst.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.TaxationCase;
import me.avankziar.aep.general.objects.TaxationSet;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.api.economy.CurrencyHandler;
import me.avankziar.aep.spigot.assistance.Utility;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.account.EconomyEntity;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class Pay extends ArgumentModule implements CommandExecutor
{
	private AEP plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public Pay(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
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
		String cmdString;
		if(cst == CommandStructurType.SINGLE)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			cmdString = cc.getCommandString();
			int zero = 0;
			int one = 1;
			int two = 2;
			int three = 3;
			int four = 4;
			int five = 5;
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero, one, two, three, four, five);
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
			int zero = 0+1;
			int one = 1+1;
			int two = 2+1;
			int three = 3+1;
			int four = 4+1;
			int five = 5+1;
			String cmdString = ac.getCommandString();
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero, one, two, three, four, five);
				}
			}.runTaskAsynchronously(plugin);
		}
	}
	
	/*
	 * pay <ToPlayer> <amount> [category] [comment...]
	 */
	private void middlePart(Player player, String cmdString, String[] args,
			int zero, int one, int two, int three, int four, int five)
	{
		if(args.length < two)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cmdString)
					.replace("%amount%", two+"")));
			return;
		}
		if(!MatchApi.isDouble(Transfer.convertDecimalSeperator(args[zero])))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[zero])));
			return;
		}
		Account from = null;
		
		String toName = args[one];
		UUID touuid = null;
		Account to = null;
		
		String category = null;
		String comment = null;
		String as = Transfer.convertDecimalSeperator(args[zero]);
		double amount = Double.parseDouble(as);
		int catStart = three;
		
		AEPUser fromuser = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlType.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
		if(fromuser == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		from = plugin.getIFHApi().getAccount(plugin.getIFHApi().getQuickPayAccount(
				plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL), player.getUniqueId()));
		if(from == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.ShortPayAccountDontExist")));
			return;
		}
		touuid = Utility.convertNameToUUID(toName, EconomyEntity.EconomyType.PLAYER);
		if(touuid == null)
		{
			touuid = Utility.convertNameToUUID(toName, EconomyEntity.EconomyType.ENTITY);
			if(touuid == null)
			{
				player.sendMessage(ChatApiOld.tl(
						plugin.getYamlHandler().getLang().getString("EntityNotExist")));
				return;
			}
		}
		AEPUser touser = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlType.PLAYERDATA, "`player_uuid` = ?", touuid.toString());
		if(touser == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		to = plugin.getIFHApi().getAccount(plugin.getIFHApi().getQuickPayAccount(
				plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL), touuid));
		if(to == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.TargetAccountDontExist")));
			return;
		}
		if(from.getCurrency() == null)
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", from.getAccountName()));
			return;
		}
		if(to.getCurrency() == null)
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", to.getAccountName()));
			return;
		}
		if(!plugin.getIFHApi().canManageAccount(from, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NoWithdrawRights")));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", as)));
			return;
		}
		if(from.getID() == to.getID())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Pay.SameAccount")));
			return;
		}
		Account tax = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.TAX, from.getCurrency());
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
		if(from.getCurrency().getUniqueName().toString().equalsIgnoreCase(to.getCurrency().getUniqueName()))
		{
			endpartSameCurrency(player, from, to, tax, category, comment, amount);
		} else
		{
			Account taxTo = plugin.getIFHApi().getDefaultAccount(to.getOwner().getUUID(), AccountCategory.TAX, to.getCurrency());
			endpartVariousCurrency(player, from, to, tax, taxTo, category, comment, amount);
		}		
	}
	
	private void endpartSameCurrency(Player player, Account from, Account to, Account tax, String category, String comment, double amount)
	{
		LinkedHashMap<TaxationCase, TaxationSet> map = CurrencyHandler.taxationMap.get(from.getCurrency().getUniqueName());		
		TaxationSet ts = map.containsKey(TaxationCase.TRANSACTION_BETWEEN_PLAYERS) ? map.get(TaxationCase.TRANSACTION_BETWEEN_PLAYERS) : null;
		double taxation = ts != null ? ts.getTaxInPercent() : 0.0;
		boolean taxAreExclusive = ts != null ? ts.isTaxAreExclusive() : true;
		EconomyAction ea = null;
		if(category == null)
		{
			ea = plugin.getIFHApi().transaction(from, to, amount, taxation, taxAreExclusive, tax);
		} else if(category != null)
		{
			ea = plugin.getIFHApi().transaction(from, to, amount, taxation, taxAreExclusive, tax, 
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		}
		if(!ea.isSuccess())
		{
			player.sendMessage(ChatApiOld.tl(ea.getDefaultErrorMessage()));
			return;
		}
		ArrayList<String> list = new ArrayList<>();
		String wformat = plugin.getIFHApi().format(ea.getWithDrawAmount(), from.getCurrency());
		String dformat = plugin.getIFHApi().format(ea.getDepositAmount(), from.getCurrency());
		String tformat = plugin.getIFHApi().format(ea.getTaxAmount(), from.getCurrency());
		for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Pay.Transaction"))
		{
			String a = s.replace("%fromaccount%", from.getAccountName())
			.replace("%toaccount%", to.getAccountName())
			.replace("%formatwithdraw%", wformat)
			.replace("%formatdeposit%", dformat)
			.replace("%formattax%", tformat)
			.replace("%category%", category != null ? category : "/")
			.replace("%comment%", comment != null ? comment : "/");
			list.add(a);
		}
		for(String s : list)
		{
			player.sendMessage(ChatApiOld.tl(s));
		}
		Transfer.sendToOther(plugin, from, to, list, player.getUniqueId());
	}
	
	private void endpartVariousCurrency(Player player, Account from, Account to,
			Account taxFrom, Account taxTo,
			String category, String comment, double amount)
	{
		LinkedHashMap<TaxationCase, TaxationSet> map = CurrencyHandler.taxationMap.get(from.getCurrency().getUniqueName());		
		TaxationSet ts = map.containsKey(TaxationCase.CURRENCY_EXCHANGE) ? map.get(TaxationCase.CURRENCY_EXCHANGE) : null;
		double taxation = ts != null ? ts.getTaxInPercent() : 0.0;
		boolean taxAreExclusive = ts != null ? ts.isTaxAreExclusive() : true;
		EconomyAction ea = null;
		if((taxFrom == null || taxTo == null) && category == null)
		{
			ea = plugin.getIFHApi().exchangeCurrencies(from, to, amount);
		} else if((taxFrom == null || taxTo == null) && category != null)
		{
			ea = plugin.getIFHApi().exchangeCurrencies(from, to, amount, OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		} else if(taxFrom != null && category == null)
		{
			ea = plugin.getIFHApi().exchangeCurrencies(from, to, amount, taxation, taxAreExclusive, taxFrom, taxTo);
		} else if(taxFrom != null && category != null)
		{
			ea = plugin.getIFHApi().exchangeCurrencies(from, to, amount, taxation, taxAreExclusive, taxFrom, taxTo,
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		}
		if(!ea.isSuccess())
		{
			player.sendMessage(ChatApiOld.tl(ea.getDefaultErrorMessage()));
			return;
		}
		ArrayList<String> list = new ArrayList<>();
		String wformat = plugin.getIFHApi().format(ea.getWithDrawAmount(), from.getCurrency());
		String dformat = plugin.getIFHApi().format(ea.getDepositAmount(), to.getCurrency());
		String tformat = plugin.getIFHApi().format(ea.getTaxAmount(),
				from.getCurrency().getTaxationBeforeExchange() ? from.getCurrency() : to.getCurrency());
		for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.Pay.Transaction"))
		{
			String a = s.replace("%fromaccount%", from.getAccountName())
			.replace("%toaccount%", to.getAccountName())
			.replace("%formatwithdraw%", wformat)
			.replace("%formatdeposit%", dformat)
			.replace("%formattax%", tformat)
			.replace("%category%", category != null ? category : "/")
			.replace("%comment%", comment != null ? comment : "/");
			list.add(a);
		}
		for(String s : list)
		{
			player.sendMessage(ChatApiOld.tl(s));
		}
		Transfer.sendToOther(plugin, from, to, list, player.getUniqueId());
	}
}