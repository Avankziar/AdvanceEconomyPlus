package main.java.me.avankziar.aep.spigot.cmd.loan;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;

public class ARGLoan_Reject extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGLoan_Reject(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoLoan")));
			return;
		}
		if(!PendingHandler.loanToAccept.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdLoan.NoToAcceptLoan")));
			return;
		}
		LoanRepayment dr = PendingHandler.loanToAccept.get(player.getUniqueId().toString());
		OLD_AEPUser toplayer = _AEPUserHandler_OLD.getEcoPlayer(dr.getLoanOwner());
		String tomsg = ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Reject.isRejecting")
				.replace("%toplayer%", toplayer.getName())
				.replace("%name%", dr.getName())
				.replace("%player%", player.getName()));
		String tomsgII  = ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Reject.isCancelled"));
		boolean bungee = AEPSettings.settings.isBungee();
		if(toplayer.isMoneyPlayerFlow())
		{
			if(bungee)
			{
				BungeeBridge.sendBungeeMessage(player, toplayer.getUUID(), tomsg, false, "");
				BungeeBridge.sendBungeeMessage(player, toplayer.getUUID(), tomsgII, false, "");
			} else
			{
				if(Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())) != null)
				{
					Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).sendMessage(tomsg);
					Bukkit.getPlayer(UUID.fromString(toplayer.getUUID())).sendMessage(tomsgII);
				}
			}
		}
		PendingHandler.loanRepayment.remove(dr.getLoanOwner());
		PendingHandler.loanToAccept.remove(player.getUniqueId().toString());
		return;
	}
}