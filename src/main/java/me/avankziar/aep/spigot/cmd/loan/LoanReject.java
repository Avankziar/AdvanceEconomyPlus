package main.java.me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.LoanRepayment;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;

public class LoanReject extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public LoanReject(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!PendingHandler.loanToAccept.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.NoToAcceptLoan")));
			return;
		}
		LoanRepayment lr = PendingHandler.loanToAccept.get(player.getUniqueId().toString());
		String toplayer = Utility.convertUUIDToName(lr.getOwner().toString(), EconomyType.PLAYER);
		String tomsg = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Reject.isRejecting")
				.replace("%toplayer%", toplayer))
				.replace("%name%", lr.getName())
				.replace("%player%", player.getName());
		String tomsgII  = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Reject.isCancelled"));
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