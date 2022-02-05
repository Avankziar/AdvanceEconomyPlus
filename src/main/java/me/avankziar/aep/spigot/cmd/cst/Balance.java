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
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.cst.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.cst.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.object.CommandStructurType;
import main.java.me.avankziar.aep.spigot.object.ne_w.AccountManagement;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Balance implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private CommandConstructor cc;
	private CommandStructurType cst;
	
	public Balance(AdvancedEconomyPlus plugin, CommandConstructor cc, CommandStructurType cst)
	{
		this.plugin = plugin;
		this.cc = cc;
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
		if(!player.hasPermission(cc.getPermission()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return false;
		}
		if(args.length == 0 || (args.length > 0 && MatchApi.isInteger(args[0])))
		{
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
						if(ac.canConsoleAccess() && player == null)
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
						} else if(player != null)
						{
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
								///Du hast dafür keine Rechte!
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
		if(player != null)
		{
			player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, AdvancedEconomyPlus.infoCommand));
			//AdvancedEconomyPlus.log.info(sender.getName() + " send command: " + cmd.getName() + arg);
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
						"`uuid` = ? AND `account_management_type` = ?", uuid, AccountManagementType.CAN_SEE_BALANCE.toString()));
		if(listAM.isEmpty())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Balance.HaveNotOneAccountToSeeTheBalance")));
			return;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> headline = new ArrayList<>();
		headline.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Balance.Headline")));
		msg.add(headline);
		for(AccountManagement am : listAM)
		{
			Account a = plugin.getIFHApi().getAccount(am.getAccountID());
			String owner = Utility.convertUUIDToName(a.getOwner().toString(), EconomyEntity.EconomyType.PLAYER);
			ArrayList<BaseComponent> list = new ArrayList<>();
			if(a.getOwner() != null &&
					(a.getOwner().toString().equals(player.getUniqueId().toString())
					|| player.hasPermission(ExtraPerm.map.get(ExtraPerm.Type.BYPASS_ACCOUNTMANAGEMENT))
					|| plugin.getIFHApi().canManageAccount(a, 
							UUID.fromString(uuid), AccountManagementType.CAN_ADMINISTRATE_ACCOUNT)))
			{
				list.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.IsOwner")
						.replace("%account%", a.getAccountName())
						.replace("%balance%", plugin.getIFHApi().format(a.getBalance(), a.getCurrency()))));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.Info")
						.replace("%cmd%", CommandSuggest.ACCOUNT_INFO.trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.ActionLog")
						.replace("%cmd%", CommandSuggest.ACTIONLOG.trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.TrendLog")
						.replace("%cmd%", CommandSuggest.TRENDLOG.trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
			} else if(plugin.getIFHApi().canManageAccount(a, 
					UUID.fromString(uuid), AccountManagementType.CAN_SEE_LOG))
			{
				list.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.CanSeeLog")
						.replace("%account%", a.getAccountName())
						.replace("%balance%", plugin.getIFHApi().format(a.getBalance(), a.getCurrency()))));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.ActionLog")
						.replace("%cmd%", CommandSuggest.ACTIONLOG.trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
				list.add(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.TrendLog")
						.replace("%cmd%", CommandSuggest.TRENDLOG.trim())
						.replace("%account%", a.getAccountName())
						.replace("%player%", owner)));
			} else
			{
				list.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Balance.AccountDisplay.CanSeeBalance")
						.replace("%account%", a.getAccountName())
						.replace("%balance%", plugin.getIFHApi().format(a.getBalance(), a.getCurrency()))));
			}
			msg.add(list);
		}
		for(ArrayList<BaseComponent> l : msg)
		{
			TextComponent tc = new TextComponent("");
			tc.setExtra(l);
			player.spigot().sendMessage(tc);
		}
	}
}
