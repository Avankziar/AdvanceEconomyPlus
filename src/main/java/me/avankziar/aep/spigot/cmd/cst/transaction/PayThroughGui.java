package main.java.me.avankziar.aep.spigot.cmd.cst.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.api.economy.CurrencyHandler;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.listener.GuiPayListener;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.AccountManagement;
import main.java.me.avankziar.aep.spigot.object.TaxationCase;
import main.java.me.avankziar.aep.spigot.object.TaxationSet;
import main.java.me.avankziar.aep.spigot.object.subs.GuiPay;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.spigot.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;

public class PayThroughGui extends ArgumentModule implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public PayThroughGui(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
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
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero, one, two, three);
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
			String cmdString = ac.getCommandString();
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero, one, two, three);
				}
			}.runTaskAsynchronously(plugin);
		}
	}
	
	/*
	 * paythroughgui <amount> <ToPlayer> [category] [comment...]
	 */
	private void middlePart(Player player, String cmdString, String[] args,
			int zero, int one, int two, int three)
	{
		if(args.length < three)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cmdString)
					.replace("%amount%", String.valueOf(three))));
			return;
		}		
		String category = null;
		String comment = null;
		String as = null;
		double amount = 0.0;
		int catStart = three;
		as = args[zero];
		String toName = args[one];
		if(!MatchApi.isDouble(args[zero]))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[zero])));
			return;
		}
		amount = Double.parseDouble(as);
		AEPUser fromuser = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
		if(fromuser == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", as)));
			return;
		}
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
		GuiPay gp = new GuiPay(player, toName, amount, category, comment);
		GuiPayListener.guiPayMap.put(player.getUniqueId(), gp);
		try
		{
			openPayThroughGui(player);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void openPayThroughGui(Player player) throws IOException
	{
		if(!GuiPayListener.guiPayMap.containsKey(player.getUniqueId()))
		{
			return;
		}
		GuiPay gp = GuiPayListener.guiPayMap.get(player.getUniqueId());
		AdvancedEconomyPlus plugin = AdvancedEconomyPlus.getPlugin();
		//Inventory isnt open
		AEPUser aepu = null;
		if(gp.getStep() == 1)
		{
			aepu = (AEPUser) plugin.getMysqlHandler().getData(
					MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", player.getName());
		} else
		{
			aepu = (AEPUser) plugin.getMysqlHandler().getData(
					MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", gp.getToPlayer());
		}
		if(aepu == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		ArrayList<AccountManagement> aml = ConvertHandler.convertListIX(plugin.getMysqlHandler().getAllListAt(
				MysqlHandler.Type.ACCOUNTMANAGEMENT, "`id` ASC", "`player_uuid` = ? AND `account_management_type` = ?", 
				aepu.getUUID().toString(), AccountManagementType.CAN_WITHDRAW.toString()));
		Inventory inv = Bukkit.createInventory(null, 6*9, aepu.getName()+" Accounts");
		for(AccountManagement am : aml)
		{
			Account ac = plugin.getIFHApi().getAccount(am.getAccountID());
			if(ac == null)
			{
				continue;
			}
			ItemStack is = new ItemStack(getMat(ac.getCategory()));
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(String.valueOf(ac.getID()));
			ArrayList<String> lore = new ArrayList<>();
			for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.PayThroughGui.Lore"))
			{
				String r = s.replace("%own%", ac.getOwner().getName())
						.replace("%owt%", ac.getOwner().getType().toString())
						.replace("%acn%", ac.getAccountName())
						.replace("%acc%", ac.getCategory().toString())
						.replace("%act%", ac.getType().toString());
				lore.add(r);
			}
			im.setLore(lore);
			is.setItemMeta(im);
			inv.addItem(is);
		}
		if(player.getOpenInventory().getType() == InventoryType.CRAFTING)
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if(inv != null)
					{
						player.openInventory(inv);
					}
				}
			}.runTask(plugin);
		} else
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.getOpenInventory().getBottomInventory().setContents(inv.getContents());
					player.updateInventory();
				}
			}.runTask(plugin);
		}
	}
	
	private static Material getMat(AccountCategory acc)
	{
		switch(acc)
		{
		case CITY:
			return Material.BEACON;
		case DONATION:
			return Material.DIAMOND;
		case GROUP:
			return Material.DARK_OAK_BOAT;
		case GUILD:
			return Material.OAK_BOAT;
		case JOB:
			return Material.WOODEN_PICKAXE;
		case MAIN:
			return Material.IRON_DOOR;
		case PARTY:
			return Material.MINECART;
		case RENT:
			return Material.OAK_DOOR;
		case SAVING:
			return Material.CHEST;
		case SHAREDEPOSIT:
			return Material.CHEST_MINECART;
		case SHOP:
			return Material.OAK_SIGN;
		case TAX:
			return Material.GOLD_NUGGET;
		case VOID:
			return Material.STRUCTURE_VOID;
		default:
			return Material.BARRIER;
		}
	}
	
	public static void endPart(final Player player, final GuiPay gp)
	{
		GuiPayListener.guiPayMap.remove(player.getUniqueId());
		AdvancedEconomyPlus plugin = AdvancedEconomyPlus.getPlugin();
		Account from = plugin.getIFHApi().getAccount(gp.getFromAccountID());
		Account to = plugin.getIFHApi().getAccount(gp.getToAccountID());
		Account tax = plugin.getIFHApi().getAccount(gp.getTaxAccountID());
		String category = gp.getCategory();
		String comment = gp.getComment();
		double amount = gp.getAmount();
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
		Pay.sendToOther(plugin, to, list);
	}
}