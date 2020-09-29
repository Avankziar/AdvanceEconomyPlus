package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.md_5.bungee.api.chat.HoverEvent;

public class ARGMoneyLoan_Amount extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Amount(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		String ta = args[2]; //totalamount
		String ar = args[3]; //amountration
		String it = args[4]; //interest
		if(!MatchApi.isDouble(ta))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", ta)));
			return;
		}
		if(!MatchApi.isDouble(ar))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", ar)));
			return;
		}
		if(!MatchApi.isDouble(it))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", it)));
			return;
		}
		double toam = Double.parseDouble(ta);
		double amra = Double.parseDouble(ar);
		double inst = Double.parseDouble(it);
		if(!MatchApi.isPositivNumber(toam))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", ta)));
			return;
		}
		if(!MatchApi.isPositivNumber(amra))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", ta)));
			return;
		}
		if(inst <= -100)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("InterestCantBeUnderMinus100")
					.replace("%args%", ta)));
			return;
		}
		toam = toam+toam*inst; //GesamtBetrag+Gesamtbetrag*Zinsen <=> Gesamtbetrag/(1+(zinsen/100))
		double minimumpayment = Math.floor(toam/amra); //Minimum anzahl von Zahlungen
		LoanRepayment dr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		dr.setTotalAmount(toam);
		dr.setAmountRatio(amra);
		dr.setInterest(inst);
		PendingHandler.loanRepayment.replace(player.getUniqueId().toString(), dr);
		player.spigot().sendMessage(ChatApi.hoverEvent(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Amount.SetsAmounts"),
				HoverEvent.Action.SHOW_TEXT,
				plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Amount.Hover")
				.replace("%ta%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(toam)))
				.replace("%ar%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(amra)))
				.replace("%in%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(inst)))
				.replace("%min%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(minimumpayment)))));
		return;
	}
}