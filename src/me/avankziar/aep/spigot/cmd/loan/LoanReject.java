package me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.Utility;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.PendingHandler;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;

public class LoanReject extends ArgumentModule
{
	private AEP plugin;
	
	public LoanReject(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!PendingHandler.loanToAccept.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.NoToAcceptLoan")));
			return;
		}
		LoanRepayment lr = PendingHandler.loanToAccept.get(player.getUniqueId().toString());
		String toplayer = Utility.convertUUIDToName(lr.getOwner().toString(), EconomyType.PLAYER);
		String tomsg = ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Reject.isRejecting")
				.replace("%toplayer%", toplayer))
				.replace("%name%", lr.getName())
				.replace("%player%", player.getName());
		String tomsgII  = ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Reject.isCancelled"));
		if(Bukkit.getPlayer(lr.getOwner()) == null)
		{
			if(plugin.getMtB() != null)
			{
				plugin.getMtB().sendMessage(lr.getOwner(), tomsg, tomsgII);
			}
		} else
		{
			if(Bukkit.getPlayer(lr.getOwner()) != null)
			{
				Bukkit.getPlayer(lr.getOwner()).sendMessage(tomsg);
				Bukkit.getPlayer(lr.getOwner()).sendMessage(tomsgII);
			}
		}
		PendingHandler.loanRepayment.remove(lr.getOwner().toString());
		PendingHandler.loanToAccept.remove(player.getUniqueId().toString());
		return;
	}
}