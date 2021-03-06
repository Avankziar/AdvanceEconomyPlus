package main.java.me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;

public class ARGLoan_Create extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGLoan_Create(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
					plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		String name = args[1];
		String from = args[2];
		String to = args[3];
		String fuuid = Utility.convertNameToUUID(from);
		String tuuid = Utility.convertNameToUUID(to);
		if(from.equals(player.getName()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdLoan.YouCantBeTheOwnerOfYourOwnLoan")));
			return;
		}
		if(!to.equals(player.getName()) && !player.hasPermission(Utility.PERM_BYPASS_LOAN_CREATE))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdLoan.YouCantBeTheOwnerOfYourOwnLoan")));
			return;
		}
		if(PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
		{
			if(fuuid == null || tuuid == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdLoan.ThereAreNoPlayers")));
				return;
			}
			LoanRepayment dr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
			dr.setName(name);
			dr.setFrom(from);
			dr.setTo(to);
		} else
		{
			if(fuuid == null || tuuid == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdLoan.ThereAreNoPlayers")));
				return;
			}
			LoanRepayment dr = new LoanRepayment(0, name, fuuid, tuuid, player.getUniqueId().toString(),
					0, 0, 0, 0, 0, 0, 0, 0, false, false, false);
			PendingHandler.loanRepayment.put(player.getUniqueId().toString(), dr);
			player.sendMessage(plugin.getYamlHandler().getL().getString("CmdLoan.Create.isCreate"));
		}
		return;
	}
}
