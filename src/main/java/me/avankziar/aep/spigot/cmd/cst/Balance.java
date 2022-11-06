package main.java.me.avankziar.aep.spigot.cmd.cst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AccountManagement;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandStructurType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class Balance implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private CommandConstructor cc;
	private CommandStructurType cst;
	
	public Balance(CommandConstructor cc, CommandStructurType cst)
	{
		this.plugin = BaseConstructor.getPlugin();
		this.cc = cc;
		this.cst = cst;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(args.length == 0 || (args.length > 0 && MatchApi.isInteger(args[0])))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("Cmd only for Players!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(cc.getPermission()))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					balance(player, args);
				}
			}.runTaskAsynchronously(plugin);
			return true;
		}
		if(cst == CommandStructurType.SINGLE)
		{
			return false;
		}
		int length = args.length-1;
		ArrayList<ArgumentConstructor> aclist = cc.subcommands;
		for(int i = 0; i <= length; i++)
		{
			for(ArgumentConstructor ac : aclist)
			{
				if(args[i].equalsIgnoreCase(ac.getName()))
				{
					if(length >= ac.minArgsConstructor && length <= ac.maxArgsConstructor)
					{
						ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
						if(ac.canConsoleAccess() && !(sender instanceof Player))
						{
							if(am != null)
							{
								try
								{
									am.run(sender, args);
								} catch (IOException e)
								{
									e.printStackTrace();
								}
							} else
							{
								AdvancedEconomyPlus.log.info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName()));
								return false;
							}
							return false;
						} else if(sender instanceof Player)
						{
							Player player = (Player) sender;
							if(player.hasPermission(ac.getPermission()))
							{
								if(am != null)
								{
									try
									{
										am.run(sender, args);
									} catch (IOException e)
									{
										e.printStackTrace();
									}
								} else
								{
									AdvancedEconomyPlus.log.info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
											.replace("%ac%", ac.getName()));
									player.sendMessage(ChatApi.tl(
											"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
											.replace("%ac%", ac.getName())));
									return false;
								}
								return false;
							} else
							{
								player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
								return false;
							}
						} else
						{
							AdvancedEconomyPlus.log.info("Cannot access ArgumentModule! Command is not for ConsoleAccess and Executer is Console "
									+ "or Executor is Player and a other Error set place!"
									.replace("%ac%", ac.getName()));
						}
					} else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		if(sender != null)
		{
			sender.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, CommandSuggest.get(null, CommandExecuteType.AEP)));
		}
		return true;
	}
	
	private void balance(Player player, String[] args)
	{
		EconomyEntity.EconomyType et = EconomyEntity.EconomyType.PLAYER;
		String uuid = player.getUniqueId().toString();
		String name = player.getName();
		int page = 0;
		if(args.length > 1 && MatchApi.isInteger(args[0]))
		{
			page = Integer.parseInt(args[0]);
		}
		int start = page*10;
		int quantity = 10;
		if(args.length > 3)
		{
			try
			{
				et = EconomyEntity.EconomyType.valueOf(args[1]);
			} catch(Exception e)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.NotCorrectEconomyEntityEconomyType")
						.replace("%arg%", args[1])));
				return;
			}
			name = args[2];
			UUID id = Utility.convertNameToUUID(name, et);
			if(id == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.UUIDIsNull."+et.toString())
						.replace("%name%", name)));
				return;
			}
			uuid = id.toString();
		}
		ArrayList<AccountManagement> listAM = ConvertHandler.convertListIX(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.ACCOUNTMANAGEMENT,
						"`id` ASC", start, quantity,
						"`player_uuid` = ?", uuid));
		ArrayList<Integer> acclist = new ArrayList<>();
		for(AccountManagement am : listAM)
		{
			if(!acclist.contains(am.getAccountID()))
			{
				acclist.add(am.getAccountID());
			}
		}
		int count = plugin.getMysqlHandler().getCount(MysqlHandler.Type.ACCOUNTMANAGEMENT, "`player_uuid` = ?", uuid);
		while(acclist.size() < quantity)
		{
			if(count < start)
			{
				break;
			}
			start = start + quantity;
			ArrayList<AccountManagement> listAMII = ConvertHandler.convertListIX(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.ACCOUNTMANAGEMENT,
							"`id` ASC", start, quantity,
							"`player_uuid` = ?", uuid));
			for(AccountManagement am : listAMII)
			{
				if(!acclist.contains(am.getAccountID()))
				{
					acclist.add(am.getAccountID());
				}
			}
		}
		if(acclist.isEmpty())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Balance.HaveNotOneAccountToSeeTheBalance")));
			return;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> headline = new ArrayList<>();
		headline.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Balance.Headline").replace("%player%", name)));
		msg.add(headline);
		for(Integer i : acclist)
		{
			Account a = plugin.getIFHApi().getAccount(i);
			if(a == null || a.getCurrency() == null)
			{
				continue;
			}
			String owner = Utility.convertUUIDToName(a.getOwner().getUUID().toString(), EconomyEntity.EconomyType.PLAYER);
			ArrayList<BaseComponent> list = new ArrayList<>();
			list.add(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.Info")
					.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.ACCOUNT).replace(" ", "+").trim())));
			if(a.getOwner() != null &&
					a.getOwner().getUUID().toString().equals(player.getUniqueId().toString()))
			{
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.ActionLog")
						.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.ACTIONLOG).replace(" ", "+").trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.TrendLog")
						.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.TRENDLOG).replace(" ", "+").trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.IsOwnerCannotSeeBalance")
						.replace("%account%", a.getAccountName()),
						Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("Log.ActionLog.FromAccountHover")
						.replace("%fromaccountowner%", owner)));	
				
			} else if(a.getOwner() != null &&
					a.getOwner().getUUID().toString().equals(player.getUniqueId().toString())
					&& !plugin.getIFHApi().canManageAccount(a, UUID.fromString(uuid), AccountManagementType.CAN_SEE_BALANCE))
			{
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.ActionLog")
						.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.ACTIONLOG).replace(" ", "+").trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.TrendLog")
						.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.TRENDLOG).replace(" ", "+").trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.IsOwner")
						.replace("%account%", a.getAccountName())
						.replace("%balance%", plugin.getIFHApi().format(a.getBalance(), a.getCurrency())),
						Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("Log.ActionLog.FromAccountHover")
						.replace("%fromaccountowner%", owner)));
			} else if(plugin.getIFHApi().canManageAccount(a, UUID.fromString(uuid), AccountManagementType.CAN_SEE_LOG)
					&& plugin.getIFHApi().canManageAccount(a, UUID.fromString(uuid), AccountManagementType.CAN_SEE_BALANCE))
			{
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.ActionLog")
						.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.ACTIONLOG).replace(" ", "+").trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.TrendLog")
						.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.TRENDLOG).replace(" ", "+").trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.CanSeeLog")
						.replace("%account%", a.getAccountName())
						.replace("%balance%", plugin.getIFHApi().format(a.getBalance(), a.getCurrency())),
								Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("Log.ActionLog.FromAccountHover")
								.replace("%fromaccountowner%", owner)));
			} else if(plugin.getIFHApi().canManageAccount(a, UUID.fromString(uuid), AccountManagementType.CAN_SEE_BALANCE))
			{
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.ActionLogDeny")));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.TrendLogDeny")));
				list.add(ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.CanSeeBalance")
						.replace("%account%", a.getAccountName())
						.replace("%balance%", plugin.getIFHApi().format(a.getBalance(), a.getCurrency())),
						Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("Log.ActionLog.FromAccountHover")
						.replace("%fromaccountowner%", owner)));
			} else if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_ACCOUNTMANAGEMENT)))
			{
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.ActionLog")
						.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.ACTIONLOG).replace(" ", "+").trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.TrendLog")
						.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.TRENDLOG).replace(" ", "+").trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.IsAdmin")
						.replace("%account%", a.getAccountName())
						.replace("%balance%", plugin.getIFHApi().format(a.getBalance(), a.getCurrency())),
						Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("Log.ActionLog.FromAccountHover")
						.replace("%fromaccountowner%", owner)));
				
			} else
			{
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.ActionLogDeny")));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.TrendLogDeny")));
				list.add(ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.CannotSeeBalance")
						.replace("%account%", a.getAccountName()),
						Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("Log.ActionLog.FromAccountHover")
						.replace("%fromaccountowner%", owner)));			
			}
			msg.add(list);
		}
		LogHandler.pastNextPage(plugin, player, msg, page, false, cc.getCommandString(), null, null);
	}
}
