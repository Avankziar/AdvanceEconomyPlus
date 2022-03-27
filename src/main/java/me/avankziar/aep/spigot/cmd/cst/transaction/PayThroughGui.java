package main.java.me.avankziar.aep.spigot.cmd.cst.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.general.objects.AccountManagement;
import main.java.me.avankziar.aep.general.objects.TaxationCase;
import main.java.me.avankziar.aep.general.objects.TaxationSet;
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
import main.java.me.avankziar.aep.spigot.object.subs.GuiPay;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.general.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.general.economy.action.OrdererType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

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
	 * paythroughgui <ToPlayer> <amount> [category] [comment...]
	 */
	private void middlePart(Player player, String cmdString, String[] args,
			int zero, int one, int two, int three)
	{
		if(args.length < two)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cmdString)
					.replace("%amount%", String.valueOf(two))));
			return;
		}		
		String category = null;
		String comment = null;
		String as = null;
		double amount = 0.0;
		int catStart = two+1;
		as = Pay.convertDecimalSeperator(args[one]);
		String toName = args[zero];
		if(!MatchApi.isDouble(as))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[one])));
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
		if(args.length >= catStart+1)
		{
			String[] s = Pay.getCategoryAndComment(args, catStart);
			category = s[0];
			comment = s[1];
		}
		GuiPay gp = new GuiPay(player, toName, amount, category, comment);
		GuiPayListener.guiPayMap.put(player.getUniqueId().toString(), gp);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new RemovePlayerInGui(player.getUniqueId()), 20*60*2);
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
		if(!GuiPayListener.guiPayMap.containsKey(player.getUniqueId().toString()))
		{
			return;
		}
		GuiPay gp = GuiPayListener.guiPayMap.get(player.getUniqueId().toString());
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
				String r = ChatApi.tl(s.replace("%own%", ac.getOwner().getName())
						.replace("%owt%", plugin.getIFHApi().getEconomyEntityType(ac.getOwner().getType()))
						.replace("%acn%", ac.getAccountName())
						.replace("%acc%", plugin.getIFHApi().getAccountCategory(ac.getCategory()))
						.replace("%act%", plugin.getIFHApi().getAccountType(ac.getType()))
						);
				lore.add(r);
			}
			im.setLore(lore);
			is.setItemMeta(im);
			inv.addItem(is);
		}
		
		if(player.getOpenInventory() instanceof PlayerInventory)
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.getOpenInventory().getTopInventory().setContents(inv.getContents());
					player.updateInventory();
				}
			}.runTaskLater(plugin, 10*1);
		} else
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
			}.runTaskLater(plugin, 10*1);
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
		GuiPayListener.guiPayMap.remove(player.getUniqueId().toString());
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
		Pay.sendToOther(plugin, from, to, list, player.getUniqueId());
	}
	
	private class RemovePlayerInGui implements Runnable
	{
		private final UUID uuid;
		
		public RemovePlayerInGui(UUID uuid)
		{
			this.uuid = uuid;
		}
		
		@Override
        public void run() 
		{
            if (uuid != null) 
            {
            	GuiPayListener.guiPayMap.remove(uuid.toString());
            }
        }
	}
}