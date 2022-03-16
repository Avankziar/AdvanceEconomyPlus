package main.java.me.avankziar.aep.spigot.cmd.cst.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.general.objects.AccountManagement;
import main.java.me.avankziar.aep.general.objects.TaxationCase;
import main.java.me.avankziar.aep.general.objects.TaxationSet;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.api.economy.CurrencyHandler;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.general.economy.account.AccountType;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.general.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.general.economy.action.OrdererType;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

public class Pay extends ArgumentModule implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public Pay(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
	 * pay <amount> <ToPlayer> <ToAccountname> [category] [comment...]
	 * pay <FromAccountname(Own)> <amount> <ToAccountName(Own)> [category] [comment...]
	 * pay <FromPlayer> <FromAccountName> <amount> <ToPlayer> <ToAccountname> [category] [comment...]
	 */
	private void middlePart(Player player, String cmdString, String[] args,
			int zero, int one, int two, int three, int four, int five)
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
		
		String toName = player.getName();
		UUID touuid = player.getUniqueId();
		String toAcName = null;
		Account to = null;
		
		String category = null;
		String comment = null;
		String as = null;
		double amount = 0.0;
		int catStart = four;
		if(MatchApi.isDouble(convertDecimalSeperator(args[zero])))
		{
			as = convertDecimalSeperator(args[zero]);
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
			from = plugin.getIFHApi().getAccount(plugin.getIFHApi().getQuickPayAccount(plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL), player.getUniqueId()));
			if(from == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.ShortPayAccountDontExist")));
				return;
			}
			touuid = Utility.convertNameToUUID(toName, EconomyEntity.EconomyType.PLAYER);
			if(touuid == null)
			{
				touuid = Utility.convertNameToUUID(toName, EconomyEntity.EconomyType.ENTITY);
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
		} else if(MatchApi.isDouble(convertDecimalSeperator(args[two])))
		{
			if(args.length < five)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
						.replace("%cmd%", cmdString.trim())
						.replace("%amount%", String.valueOf(four))));
				return;
			}
			fromName = args[zero];
			fromAcName = args[one];
			as = convertDecimalSeperator(args[two]);
			toName = args[three];
			toAcName = args[four];
			amount = Double.parseDouble(as);
			catStart = five+1;
			from = plugin.getIFHApi().getAccount(
					new EconomyEntity(EconomyEntity.EconomyType.PLAYER, fromuuid, fromName), fromAcName);
			if(from == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.StartAccountDontExist")));
				return;
			}
			touuid = Utility.convertNameToUUID(toName, EconomyEntity.EconomyType.PLAYER);
			if(touuid == null)
			{
				touuid = Utility.convertNameToUUID(toName, EconomyEntity.EconomyType.ENTITY);
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
		} else if(MatchApi.isDouble(convertDecimalSeperator(args[one])))
		{
			fromAcName = args[zero];
			toName = player.getName();
			as = convertDecimalSeperator(args[one]);
			toAcName = args[two];			
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
		if(!plugin.getIFHApi().canManageAccount(from, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NoWithdrawRights")));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", as)));
			return;
		}
		if(from.getID() == to.getID())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Pay.SameAccount")));
			return;
		}
		if(!from.getCurrency().getUniqueName().toString().equalsIgnoreCase(to.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Pay.NotSameCurrency")));
			return;
		}
		Account tax = plugin.getIFHApi().getDefaultAccount(player.getUniqueId(), AccountCategory.TAX, from.getCurrency());
		if(args.length >= catStart+1)
		{
			String[] s = Pay.getCategoryAndComment(args, catStart);
			category = s[0];
			comment = s[1];
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
			player.sendMessage(ChatApi.tl(s));
		}
		sendToOther(plugin, to, list, player.getUniqueId());
	}
	
	public static String convertDecimalSeperator(String s)
	{
		String a = s.replace(",", ".");
		return a;
	}
	
	public static String[] getCategoryAndComment(String[] args, int catStart)
	{
		String[] s = new String[2];
		catStart--;
		s[0] = args[catStart];
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
		s[1] = sb.toString();
		return s;
	}
	
	public static void sendToOther(AdvancedEconomyPlus plugin, Account to, ArrayList<String> list, UUID...exceptions)
	{
		if(plugin.getMtB() != null)
		{
			ArrayList<AccountManagement> mana = new ArrayList<>();
			try
			{
				mana = ConvertHandler.convertListIX(plugin.getMysqlHandler().getAllListAt(
						MysqlHandler.Type.ACCOUNTMANAGEMENT, "`id` ASC", "`account_id` = ? AND `account_management_type` = ?",
						to.getID(), AccountManagementType.CAN_RECEIVES_NOTIFICATIONS.toString()));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			if(mana.isEmpty())
			{
				return;
			}
			ArrayList<UUID> ul = new ArrayList<>();
			for(AccountManagement acm : mana)
			{
				AEPUser u = (AEPUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", acm.getUUID().toString());
				if(u == null)
				{
					continue;
				}
				if(exceptions != null)
				{
					boolean ex = false;
					for(UUID uuid : exceptions)
					{
						if(uuid.toString().equals(u.getUUID().toString()))
						{
							ex = true;
							break;
						}
					}
					if(ex)
					{
						continue;
					}
				}
				if(to.getType() == AccountType.BANK)
				{
					if(u.isBankMoneyFlowNotification())
					{
						ul.add(u.getUUID());
					}
				} else
				{
					if(u.isWalletMoneyFlowNotification())
					{
						ul.add(u.getUUID());
					}
				}
			}
			String[] la = list.toArray(new String[0]);
			plugin.getMtB().sendMessage(ul, la);
		}
	}
}