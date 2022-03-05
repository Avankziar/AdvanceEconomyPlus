package main.java.me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.LoanRepayment;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import net.md_5.bungee.api.chat.HoverEvent;

//<starttime> <endtime> <repeatingtime>
public class LoanTime extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public LoanTime(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.NoWaitingLoanProposal")));
			return;
		}
		String st = args[1];
		String et = args[2];
		String rt = args[3];
		long starttime = TimeHandler.getTime(st);
		long endtime = TimeHandler.getTime(et);
		long repeatingtime = TimeHandler.getRepeatingTime(rt);
		LoanRepayment dr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		dr.setStartTime(starttime);
		dr.setEndTime(endtime);
		dr.setRepeatingTime(repeatingtime);
		PendingHandler.loanRepayment.replace(player.getUniqueId().toString(), dr);
		player.spigot().sendMessage(ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Times.SetTimes"),
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("Cmd.Loan.Times.Hover")
				.replace("%st%", st)
				.replace("%et%", et)
				.replace("%rt%", rt)));
		return;
	}
}