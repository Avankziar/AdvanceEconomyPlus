package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.md_5.bungee.api.chat.HoverEvent;

//<starttime> <endtime> <repeatingtime>
public class ARGMoneyLoan_Time extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Time(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		if(!PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Loan.NoWaitingLoanProposal")));
			return;
		}
		String st = args[2];
		String et = args[3];
		String rt = args[4];
		long starttime = TimeHandler.getTime(st);
		long endtime = TimeHandler.getTime(et);
		long repeatingtime = TimeHandler.getRepeatingTime(rt);
		LoanRepayment dr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		dr.setStartTime(starttime);
		dr.setEndTime(endtime);
		dr.setRepeatingTime(repeatingtime);
		PendingHandler.loanRepayment.replace(player.getUniqueId().toString(), dr);
		player.spigot().sendMessage(ChatApi.hoverEvent(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Times.SetTimes"),
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Times.Hover")
				.replace("%st%", st)
				.replace("%et%", et)
				.replace("%rt%", rt)));
		return;
	}
}