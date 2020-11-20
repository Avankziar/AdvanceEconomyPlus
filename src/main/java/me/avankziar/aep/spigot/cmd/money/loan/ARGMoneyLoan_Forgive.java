package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class ARGMoneyLoan_Forgive extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Forgive(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		String ids = args[2];
		String confirm = "";
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(args.length >= 4)
		{
			confirm = args[3];
		}
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Forgive.CanBeUndone")));
			return;
		}
		if(!dr.getLoanOwner().equals(player.getUniqueId().toString())
				&& !player.hasPermission(Utility.PERM_BYPASS_LOAN_FORGIVE))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.NotLoanOwner")));
			return;
		}
		if(!confirm.equalsIgnoreCase(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.ConfirmTerm")))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Accept.PleaseConfirm")
					.replace("%cmd%", plugin.getYamlHandler().getL().getString("CmdMoney.Loan.AcceptCmd"))));
			return;
		}
		double dif = dr.getTotalAmount()-dr.getAmountPaidSoFar();
		if(dif <= 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanAlreadyPaidOff")));
			return;
		}
		dr.setForgiven(true);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
		AEPUser fromplayer = AEPUserHandler.getEcoPlayer(dr.getFrom());
		AEPUser toplayer = AEPUserHandler.getEcoPlayer(dr.getTo());
		String tomsg = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Forgive.LoanIsForgiven")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%dif%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dif)))
				.replace("%from%", fromplayer.getName())
				.replace("%to%", toplayer.getName())
				.replace("%player%", player.getName()));
		boolean bungee = EconomySettings.settings.isBungee();
		if(fromplayer.isMoneyPlayerFlow())
		{
			if(bungee)
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
		} else if(!dr.getTo().equals(dr.getLoanOwner()) && dr.getTo().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(tomsg);
			AEPUser doplayer = AEPUserHandler.getEcoPlayer(dr.getLoanOwner());
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
			AEPUser doplayer = AEPUserHandler.getEcoPlayer(dr.getLoanOwner());
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