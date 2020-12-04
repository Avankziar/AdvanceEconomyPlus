package main.java.me.avankziar.aep.spigot.cmd.loan;

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
import main.java.me.avankziar.aep.spigot.handler.KeyHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import net.milkbowl.vault.economy.EconomyResponse;

public class ARGLoan_Accept extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGLoan_Accept(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		if(!PendingHandler.loanToAccept.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdLoan.NoToAcceptLoan")));
			return;
		}
		String confirm = "";
		if(args.length >= 2)
		{
			confirm = args[1];
		}
		if(!confirm.equalsIgnoreCase(plugin.getYamlHandler().getL().getString("CmdLoan.ConfirmTerm")))
		{
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdLoan.PleaseConfirm")
					.replace("%cmd%", AEPSettings.settings.getCommands(KeyHandler.L_ACCEPT).replace(" ", "+")
							+"+"+plugin.getYamlHandler().getL().getString("CmdLoan.ConfirmTerm"))));
			return;
		}
		LoanRepayment dr = PendingHandler.loanToAccept.get(player.getUniqueId().toString());
		AEPUser toplayer = AEPUserHandler.getEcoPlayer(dr.getTo());
		
		EconomyResponse withdraw = AdvancedEconomyPlus.getVault().withdrawPlayer(
				Bukkit.getOfflinePlayer(UUID.fromString(dr.getTo())), dr.getTotalAmount());
		
		if(!withdraw.transactionSuccess())
		{
			player.sendMessage(ChatApi.tl(withdraw.errorMessage));
			return;
		}
		EconomyResponse deposit = AdvancedEconomyPlus.getVault().depositPlayer(player, dr.getTotalAmount());
		if(!deposit.transactionSuccess())
		{
			AdvancedEconomyPlus.getVault().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(dr.getTo())), dr.getTotalAmount());
			player.sendMessage(ChatApi.tl(deposit.errorMessage));
			return;
		}
		
		plugin.getMysqlHandler().create(MysqlHandler.Type.LOAN, dr);
		
		String drowner = Utility.convertUUIDToName(dr.getLoanOwner());
		if(drowner == null)
		{
			drowner = "/";
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdLoan.Accept.YouHaveAccepted")
				.replace("%drowner%", drowner)
				.replace("%name%", dr.getName())));
		
		String tomsg = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdLoan.Accept.PayerHasAccepted")
				.replace("%toplayer%", toplayer.getName())
				.replace("%name%", dr.getName())
				.replace("%player%", player.getName()));
		boolean bungee = AEPSettings.settings.isBungee();
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