package main.java.me.avankziar.aep.spigot.cmd.cet.base;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler.Methode;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.aep.spigot.object.subs.ActionFilterSettings;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

public class ActionLog extends ArgumentModule implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	private static String d1 = "actionlog";
	
	public ActionLog(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
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
		ConfigHandler.debug(d1, "> Actionlog Begin");
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
			try
			{
				middlePart(player, cmdString, args, zero, one, two, three);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
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
			try
			{
				middlePart(player, cmdString, args, zero, one, two, three);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * actionlog
	 * actionlog <Player> [accountname] [Page] [actionlog category]
	 * aep actionlog
	 * aep actionlog <Player> [accountname] [Page] [actionlog category]
	 */
	private void middlePart(Player player, String cmdString, String[] args,
			int zero, int one, int two, int three) throws IOException
	{
		ConfigHandler.debug(d1, "> Middle part");
		String playerName = null;
		int accountID = 0;
		int page = 0;
		if(args.length == zero)
		{
			ConfigHandler.debug(d1, "> args.lenght == zero");
			AEPUser fromuser = (AEPUser) plugin.getMysqlHandler().getData(
					MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
			if(fromuser == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
				return;
			}
			accountID = plugin.getIFHApi().getQuickPayAccount(plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL), player.getUniqueId());
			if(accountID < 0)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.QuickPayDontExist")));
				return;
			}
			playerName = player.getName();
		} else if(args.length >= two)
		{
			ConfigHandler.debug(d1, "> args.lenght >= one");
			playerName = args[zero];
			ConfigHandler.debug(d1, "> args.lenght >= two");
			UUID uuid = Utility.convertNameToUUID(playerName, EconomyEntity.EconomyType.PLAYER);
			if(uuid == null)
			{
				uuid = Utility.convertNameToUUID(playerName, EconomyEntity.EconomyType.ENTITY);
				if(uuid == null)
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("EntityNotExist")));
					return;
				}
			}	
			Account ac = plugin.getIFHApi().getAccount(new EconomyEntity(EconomyEntity.EconomyType.PLAYER, uuid, playerName), args[one]);
			if(ac == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.TargetAccountDontExist")));
				return;
			}
			accountID = ac.getID();
		}
		if(args.length >= three)
		{
			ConfigHandler.debug(d1, "> args.lenght >= three");
			String pagenumber = args[two];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
				if(!MatchApi.isPositivNumber(page))
				{
					page = 0;
				}
			}
		}
		LoggerSettings fst = new LoggerSettings(accountID, player.getUniqueId(), page);
		fst.setAction(true);
		if(args.length >= three+1)
		{
			ConfigHandler.debug(d1, "> args.lenght >= four");
			ActionFilterSettings afs = new ActionFilterSettings();
			afs.setCategory(args[three]);
			fst.setActionFilter(afs);
		}
		if(LoggerSettingsHandler.getLoggerSettings().containsKey(player.getUniqueId()))
		{
			LoggerSettingsHandler.getLoggerSettings().replace(player.getUniqueId(), fst);
		} else
		{
			LoggerSettingsHandler.getLoggerSettings().put(player.getUniqueId(), fst);
		}
		new LoggerSettingsHandler(plugin).forwardingToOutput(player, fst, LoggerSettingsHandler.Access.COMMAND, Methode.LOG, page, CommandSuggest.get(null, CommandExecuteType.ACTIONLOG));
		return;
	}
}