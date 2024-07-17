package me.avankziar.aep.spigot.cmd;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.assistance.TimeHandler;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.cmd.tree.CommandConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.Utility;
import me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import me.avankziar.aep.spigot.handler.ConfigHandler;
import me.avankziar.aep.spigot.handler.ConvertHandler;
import me.avankziar.aep.spigot.handler.LogHandler;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class LoanCommandExecutor implements CommandExecutor
{
	private AEP plugin;
	private static CommandConstructor cc;
	
	private static String db1 = "loan";
	
	public LoanCommandExecutor(CommandConstructor cc)
	{
		this.plugin = AEP.getPlugin();
		LoanCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		ConfigHandler.debug(db1, "> Loan Begin");
		if (!(sender instanceof Player)) 
		{
			AEP.logger.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
			return false;
		}
		Player player = (Player) sender;
		if(!ConfigHandler.isLoanEnabled())
		{
			ConfigHandler.debug(db1, "> Loan isnt enabled");
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("NoLoan")));
			return false;
		}
		if(cc == null)
		{
			ConfigHandler.debug(db1, "> cc == null");
			return false;
		}
		if (args.length >= 0) 
		{
			ConfigHandler.debug(db1, "> args.lenght == "+args.length);
			if(args.length == 0)
			{
				if(!player.hasPermission(cc.getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						baseCommands(player, 0, null);
					}
				}.runTaskAsynchronously(plugin);
				return true;
			}
			if(MatchApi.isInteger(args[0]))
			{
				if(!player.hasPermission(cc.getPermission()))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
				if(args.length == 1)
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							baseCommands(player, Integer.parseInt(args[0]), null);
						}
					}.runTaskAsynchronously(plugin);
					return true;
				} else if(args.length == 2)
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							baseCommands(player, Integer.parseInt(args[0]), args[1]);
						}
					}.runTaskAsynchronously(plugin);
					return true;
				}
			}
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
						if(player.hasPermission(ac.getPermission()))
						{
							ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
							if(am != null)
							{
								new BukkitRunnable()
								{
									@Override
									public void run()
									{
										try
										{
											am.run(sender, args);
										} catch (IOException e)
										{
											e.printStackTrace();
										}
									}
								}.runTaskAsynchronously(plugin);
							} else
							{
								AEP.logger.info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName()));
								player.spigot().sendMessage(ChatApiOld.tctl(
										"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName())));
								return false;
							}
							return false;
						} else
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
							return false;
						}
					}/* else if(length > ac.maxArgsConstructor) 
					{
						///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
						player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, AdvancedEconomy.infoCommand));
						return false;
					}*/ else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
		player.spigot().sendMessage(ChatApiOld.click(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
				ClickEvent.Action.RUN_COMMAND, CommandSuggest.get(null, CommandExecuteType.AEP)));
		return false;
	}
	
	public void baseCommands(final Player player, int page, String otherplayer)
	{
		ConfigHandler.debug(db1, "> baseCommand start");
		String playeruuid = player.getUniqueId().toString();
		if(otherplayer != null)
		{
			ConfigHandler.debug(db1, "> otherplayer != null");
			if(!otherplayer.equals(player.getName()))
			{
				if(!player.hasPermission(ExtraPerm.get(Type.BYPASS_LOAN)))
				{
					player.sendMessage(ChatApiOld.tl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				AEPUser user = (AEPUser) plugin.getMysqlHandler().getData(
						MysqlType.PLAYERDATA, "`player_name` = ?", otherplayer);
				if(user == null)
				{
					player.sendMessage(ChatApiOld.tl(
							plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
					return;
				}
				playeruuid = user.getUUID().toString();
			}
		}
		int start = page*25;
		int end = 24;
		ArrayList<LoanRepayment> list = ConvertHandler.convertListVI(
				plugin.getMysqlHandler().getList(MysqlType.LOAN, "`id` DESC", start, end,
						"`debtor` = ? OR `loan_owner` = ?",	playeruuid, playeruuid));
		int last = plugin.getMysqlHandler().getCount(MysqlType.LOAN,
				"`debtor` = ? OR `loan_owner` = ?",	playeruuid, playeruuid);
		ConfigHandler.debug(db1, "> list.size : "+list.size()+" | last : "+last);
		if(list.isEmpty())
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.NoLoans")));
			return;
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> bc = new ArrayList<>();
		for(LoanRepayment dr : list)
		{
			String color = "";
			double percent = (dr.getAmountPaidSoFar()/dr.getTotalAmount())*100;
			if(percent < 16.66666666)
			{
				color = "&4";
			} else if(percent >= 16.6666666667 && percent < 33.3333333334)
			{
				color = "&c";
			} else if(percent >= 33.3333333334 && percent < 50.0)
			{
				color = "&6";
			} else if(percent >= 50.0 && percent < 66.6666666667)
			{
				color = "&e";
			} else if(percent >= 66.6666666667 && percent < 87.5)
			{
				color = "&a";
			} else
			{
				color = "&2";
			}
			String owner = Utility.convertUUIDToName(dr.getOwner().toString(), EconomyType.PLAYER);
			if(owner == null)
			{
				owner = Utility.convertUUIDToName(dr.getOwner().toString(), EconomyType.ENTITY);
				if(owner == null)
				{
					owner = "N.A.";
				}
			}
			String debtor = Utility.convertUUIDToName(dr.getDebtor().toString(), EconomyType.PLAYER);
			if(debtor == null)
			{
				debtor = Utility.convertUUIDToName(dr.getDebtor().toString(), EconomyType.ENTITY);
				if(debtor == null)
				{
					debtor = "N.A.";
				}
			}
			Account from = plugin.getIFHApi().getAccount(dr.getAccountFromID());
			Account to = plugin.getIFHApi().getAccount(dr.getAccountToID());
			bc.add(ChatApiOld.hover(color+dr.getId()+"&f:"+color+dr.getName()+"&f:",
					HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getLang().getString("Cmd.Loan.HoverInfo")
					.replace("%id%", String.valueOf(dr.getId()))
					.replace("%name%", dr.getName())
					.replace("%fromaccount%", from != null ? from.getOwner().getName() : "N.A.")
					.replace("%fromowner%", from != null ? from.getAccountName() : "N.A.")
					.replace("%toaccount%", to != null ? to.getAccountName() : "N.A.")
					.replace("%toowner%", to != null ? to.getOwner().getName() : "N.A.")
					.replace("%owner%", owner)
					.replace("%debtor%", debtor)
					.replace("%st%", TimeHandler.getTime(dr.getStartTime()))
					.replace("%et%", TimeHandler.getTime(dr.getEndTime()))
					.replace("%rt%", TimeHandler.getRepeatingTime(dr.getRepeatingTime()))
					.replace("%apsf%", plugin.getIFHApi().format(dr.getAmountPaidSoFar(), from.getCurrency()))
					.replace("%apsfip%", color+percent)
					.replace("%ta%", plugin.getIFHApi().format(dr.getTotalAmount(), from.getCurrency()))
					.replace("%ar%", plugin.getIFHApi().format(dr.getAmountRatio(), from.getCurrency()))
					.replace("%tax%", plugin.getIFHApi().format(dr.getAmountPaidToTax(), from.getCurrency()))
					.replace("%in%", String.valueOf(dr.getInterest()))
					.replace("%pa%", String.valueOf(dr.isPaused()))
					.replace("%fo%", String.valueOf(dr.isForgiven()))
					.replace("%fi%", String.valueOf(dr.isFinished()))
					));
			bc.add(ChatApiOld.clickHover("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, plugin.getYamlHandler().getLang().getString("Cmd.Loan.InfoCmd")+" "+dr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApiOld.clickHover("&a✔", 
					ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getLang().getString("Cmd.Loan.ForgiveCmd")+" "+dr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApiOld.tctl(" &1| "));
		}
		ArrayList<BaseComponent> bcII = new ArrayList<>();
		bcII.add(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.List.Headline")
				.replace("%name%", player.getName())));
		msg.add(bcII);
		msg.add(bc);
		LogHandler.pastNextPage(plugin, player, msg, page, lastpage, cc.getCommandString(), otherplayer, null);
		return;
	}
}