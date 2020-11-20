package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.milkbowl.vault.economy.EconomyResponse;

public class ARGMoneyLoan_Accept extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Accept(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		if(!PendingHandler.loanToAccept.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Loan.NoToAcceptLoan")));
			return;
		}
		String confirm = "";
		if(args.length >= 3)
		{
			confirm = args[2];
		}
		if(!confirm.equalsIgnoreCase(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.ConfirmTerm")))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Loan.PleaseConfirm")
					.replace("%cmd%", plugin.getYamlHandler().getL().getString("CmdMoney.Loan.AcceptCmd"))));
			return;
		}
		LoanRepayment dr = PendingHandler.loanToAccept.get(player.getUniqueId().toString());
		AEPUser toplayer = AEPUserHandler.getEcoPlayer(dr.getTo());
		
		EconomyResponse withdraw = AdvancedEconomyPlus.getVaultApi().withdrawPlayer(
				Bukkit.getOfflinePlayer(UUID.fromString(dr.getTo())), dr.getTotalAmount());
		
		if(!withdraw.transactionSuccess())
		{
			player.sendMessage(ChatApi.tl(withdraw.errorMessage));
			return;
		}
		EconomyResponse deposit = AdvancedEconomyPlus.getVaultApi().depositPlayer(player, dr.getTotalAmount());
		if(!deposit.transactionSuccess())
		{
			AdvancedEconomyPlus.getVaultApi().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(dr.getTo())), dr.getTotalAmount());
			player.sendMessage(ChatApi.tl(deposit.errorMessage));
			return;
		}
		
		plugin.getMysqlHandler().create(MysqlHandler.Type.LOAN, dr);
		
		String drowner = Utility.convertUUIDToName(dr.getLoanOwner());
		if(drowner == null)
		{
			drowner = "/";
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Accept.YouHaveAccepted")
				.replace("%drowner%", drowner)
				.replace("%name%", dr.getName())));
		
		String tomsg = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Accept.PayerHasAccepted")
				.replace("%toplayer%", toplayer.getName())
				.replace("%name%", dr.getName())
				.replace("%player%", player.getName()));
		boolean bungee = EconomySettings.settings.isBungee();
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
		if(!dr.getTo().equals(dr.getLoanOwner()))
		{
			AEPUser Loanowner = AEPUserHandler.getEcoPlayer(dr.getLoanOwner());
			if(Loanowner.isMoneyPlayerFlow())
			{
				if(bungee)
				{
					BungeeBridge.sendBungeeMessage(player, Loanowner.getUUID(), tomsg, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(Loanowner.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(Loanowner.getUUID())).sendMessage(tomsg);
					}
				}
			}
		}
		PendingHandler.loanRepayment.remove(dr.getLoanOwner());
		PendingHandler.loanToAccept.remove(player.getUniqueId().toString());
		return;
	}
}