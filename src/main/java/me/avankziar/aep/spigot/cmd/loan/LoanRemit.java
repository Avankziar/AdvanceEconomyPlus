package main.java.me.avankziar.aep.spigot.cmd.loan;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;

public class LoanRemit extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public LoanRemit(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String ids = args[1];
		String confirm = "";
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(args.length >= 3)
		{
			confirm = args[2];
		}
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Forgive.CanBeUndone")));
			return;
		}
		if(!dr.getLoanOwner().equals(player.getUniqueId().toString())
				&& !player.hasPermission(Utility.PERM_BYPASS_LOAN_FORGIVE))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.NotLoanOwner")));
			return;
		}
		if(!confirm.equalsIgnoreCase(plugin.getYamlHandler().getLang().getString("CmdLoan.ConfirmTerm")))
		{
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdLoan.Accept.PleaseConfirm")
					.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.LOAN_REMIT)
							.replace(" ", "+")+"+"+plugin.getYamlHandler().getLang().getString("CmdLoan.ConfirmTerm"))));
			return;
		}
		double dif = dr.getTotalAmount()-dr.getAmountPaidSoFar();
		if(dif <= 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanAlreadyPaidOff")));
			return;
		}
		dr.setForgiven(true);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
		OLD_AEPUser fromplayer = _AEPUserHandler_OLD.getEcoPlayer(dr.getFrom());
		OLD_AEPUser toplayer = _AEPUserHandler_OLD.getEcoPlayer(dr.getTo());
		String tomsg = ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Forgive.LoanIsForgiven")
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%dif%", String.valueOf(AdvancedEconomyPlus.getVault().format(dif)))
				.replace("%from%", fromplayer.getName())
				.replace("%to%", toplayer.getName())
				.replace("%player%", player.getName()));
		if(fromplayer.isMoneyPlayerFlow())
		{
			if(Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())) == null)
			{
				BungeeBridge.sendBungeeMessage(player, fromplayer.getUUID(), tomsg, false, "");
			} else
			{
				if(Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())) != null)
				{
					Bukkit.getPlayer(UUID.fromString(fromplayer.getUUID())).sendMessage(tomsg);
				}
			}
		}
		if(dr.getTo().equals(dr.getLoanOwner()) && dr.getTo().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(tomsg);
		} else if(dr.getTo().equals(dr.getLoanOwner()) && !dr.getTo().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(tomsg);
			if(toplayer.isMoneyPlayerFlow())
			{
				if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) == null)
				{
					BungeeBridge.sendBungeeMessage(player, toplayer.getUUID(), tomsg, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).sendMessage(tomsg);
					}
				}
			}
		} else if(!dr.getTo().equals(dr.getLoanOwner()) && dr.getTo().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(tomsg);
			OLD_AEPUser doplayer = _AEPUserHandler_OLD.getEcoPlayer(dr.getLoanOwner());
			if(toplayer.isMoneyPlayerFlow())
			{
				if(bungee)
				{
					BungeeBridge.sendBungeeMessage(player, doplayer.getUUID(), tomsg, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(doplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(doplayer.getUUID())).sendMessage(tomsg);
					}
				}
			}
		} else
		{
			player.sendMessage(tomsg);
			if(toplayer.isMoneyPlayerFlow())
			{
				if(bungee)
				{
					BungeeBridge.sendBungeeMessage(player, toplayer.getUUID(), tomsg, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).sendMessage(tomsg);
					}
				}
			}
			OLD_AEPUser doplayer = _AEPUserHandler_OLD.getEcoPlayer(dr.getLoanOwner());
			if(toplayer.isMoneyPlayerFlow())
			{
				if(bungee)
				{
					BungeeBridge.sendBungeeMessage(player, doplayer.getUUID(), tomsg, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(doplayer.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(doplayer.getUUID())).sendMessage(tomsg);
					}
				}
			}
		}
		return;
	}
}