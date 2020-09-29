package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class ARGMoneyLoan_Create extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Create(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		String name = args[2];
		String from = args[3];
		String to = args[4];
		String fuuid = Utility.convertNameToUUID(from);
		String tuuid = Utility.convertNameToUUID(to);
		if(from.equals(player.getName()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Loan.YouCantBeTheOwnerOfYourOwnLoan")));
			return;
		}
		if(!to.equals(player.getName()) && !player.hasPermission(Utility.PERM_BYPASS_LOAN_CREATE))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Loan.YouCantBeTheOwnerOfYourOwnLoan")));
			return;
		}
		if(PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
		{
			if(fuuid == null || tuuid == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdMoney.Loan.ThereAreNoPlayers")));
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
						plugin.getYamlHandler().getL().getString("CmdMoney.Loan.ThereAreNoPlayers")));
				return;
			}
			LoanRepayment dr = new LoanRepayment(0, name, fuuid, tuuid, player.getUniqueId().toString(),
					0, 0, 0, 0, 0, 0, 0, 0, false, false, false);
			PendingHandler.loanRepayment.put(player.getUniqueId().toString(), dr);
			player.sendMessage(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Create.isCreate"));
		}
		return;
	}
}
