package main.java.me.avankziar.aep.spigot.cmd;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.LogHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LoanCommandExecutor implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private static CommandConstructor cc;
	
	public LoanCommandExecutor(AdvancedEconomyPlus plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		LoanCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if (!(sender instanceof Player)) 
		{
			AdvancedEconomyPlus.log.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
			return false;
		}
		Player player = (Player) sender;
		if(cc == null)
		{
			return false;
		}
		if (args.length >= 1) 
		{
			if(MatchApi.isInteger(args[0]))
			{
				if(!player.hasPermission(cc.getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
				if(args.length == 1)
				{
					baseCommands(player, Integer.parseInt(args[0]), null);
					return true;
				} else if(args.length == 2)
				{
					baseCommands(player, Integer.parseInt(args[0]), args[1]);
					return true;
				}
			}
		} else if(args.length == 0)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			baseCommands(player, 0, null);
			return true;
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
								player.spigot().sendMessage(ChatApi.tctl(
										"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName())));
								return false;
							}
							return false;
						} else
						{
							///Du hast dafür keine Rechte!
							player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
		player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
				ClickEvent.Action.RUN_COMMAND, AdvancedEconomyPlus.infoCommand));
		return false;
	}
	
	public void baseCommands(final Player player, int page, String otherplayer)
	{
		if(!AEPSettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoLoan")));
			return;
		}
		String playeruuid = player.getUniqueId().toString();
		if(otherplayer != null)
		{
			if(!otherplayer.equals(playeruuid))
			{
				if(!player.hasPermission(Utility.PERM_BYPASS_LOAN_LIST))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				OLD_AEPUser user = _AEPUserHandler_OLD.getEcoPlayer(otherplayer);
				if(user != null)
				{
					playeruuid = user.getUUID();
				}
			}
		}
		int start = page*25;
		int end = 24;
		ArrayList<LoanRepayment> list = ConvertHandler.convertListVI(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.LOAN, "`id` DESC", start, end,
						"`from_player` = ? OR `to_player` = ? OR `loanowner` = ?",
						playeruuid, playeruuid, playeruuid));
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.LOAN,
				"`from_player` = ? OR `to_player` = ? OR `loanowner` = ?",
				playeruuid, playeruuid, playeruuid);
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
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
			bc.add(ChatApi.hoverEvent(color+dr.getId()+"&f:"+color+dr.getName()+"&f:",
					HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getLang().getString("CmdMoney.Loan.HoverInfo")
					.replace("%id%", String.valueOf(dr.getId()))
					.replace("%name%", dr.getName())
					.replace("%from%", dr.getFrom())
					.replace("%to%", dr.getTo())
					.replace("%owner%", dr.getLoanOwner())
					.replace("%st%", TimeHandler.getTime(dr.getStartTime()))
					.replace("%et%", TimeHandler.getTime(dr.getEndTime()))
					.replace("%rt%", TimeHandler.getRepeatingTime(dr.getRepeatingTime()))
					.replace("%apsf%", color+String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getAmountPaidSoFar())))
					.replace("%ta%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getTotalAmount())))
					.replace("%ar%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getAmountRatio())))
					.replace("%in%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getInterest())))
					.replace("%pa%", String.valueOf(dr.isPaused()))
					.replace("%fo%", String.valueOf(dr.isForgiven()))
					.replace("%fi%", String.valueOf(dr.isFinished()))
					));
			bc.add(ChatApi.apiChat("&eⓘ", 
					ClickEvent.Action.RUN_COMMAND, plugin.getYamlHandler().getLang().getString("CmdMoney.Loan.InfoCmd")+" "+dr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.apiChat("&a✔", 
					ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getLang().getString("CmdMoney.Loan.ForgiveCmd")+" "+dr.getId(),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("GeneralHover")));
			bc.add(ChatApi.tctl(" &1| "));
		}
		TextComponent tx = ChatApi.tc("");
		tx.setExtra(bc);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMoney.Loan.List.Headline")
				.replace("%name%", player.getName())));
		player.spigot().sendMessage(tx);
		String cmdstring = plugin.getYamlHandler().getCom().getString(cc.getPath()+".CommandString");
		LogHandler.pastNextPage(plugin, player, "CmdMoney.", playeruuid, page, lastpage, cmdstring);
		return;
	}
}