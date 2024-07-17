package me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.PendingHandler;

public class LoanCancel extends ArgumentModule
{
	private AEP plugin;
	
	public LoanCancel(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("CmdLoan.NoWaitingLoanProposal")));
			return;
		}
		PendingHandler.loanRepayment.remove(player.getUniqueId().toString());
		player.sendMessage(ChatApiOld.tl(
				plugin.getYamlHandler().getLang().getString("CmdLoan.Cancel.IsCancelled")));
		return;
	}
}