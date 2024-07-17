package me.avankziar.aep.spigot.cmd.loan;

import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.general.objects.TaxationCase;
import me.avankziar.aep.general.objects.TaxationSet;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.api.economy.CurrencyHandler;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.PendingHandler;
import me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.HoverEvent;

public class LoanAmount extends ArgumentModule
{
	private AEP plugin;
	
	public LoanAmount(ArgumentConstructor argumentConstructor)
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
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.NoWaitingLoanProposal")));
			return;
		}
		String ab = args[1]; //amountBeforeInterestOrTax
		String ar = args[2]; //amountration
		String it = args[3]; //interest
		if(!MatchApi.isDouble(ab))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ab)));
			return;
		}
		if(!MatchApi.isDouble(ar))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ar)));
			return;
		}
		if(!MatchApi.isDouble(it))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", it)));
			return;
		}
		double loam = Double.parseDouble(ab); //Betrag welcher man leiht
		double toam = 0.0; //Betrag mit Zinsen und Steuern, welcher der Schuldner zahlen muss.
		double amra = Double.parseDouble(ar);
		double inst = Double.parseDouble(it)/100;
		if(!MatchApi.isPositivNumber(loam))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", ab)));
			return;
		}
		if(!MatchApi.isPositivNumber(amra))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", ar)));
			return;
		}
		if(inst <= -1.0)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("InterestCantBeUnderMinus100")
					.replace("%args%", it)));
			return;
		}
		LoanRepayment lr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		Account from = plugin.getIFHApi().getAccount(lr.getAccountFromID());
		LinkedHashMap<TaxationCase, TaxationSet> map = CurrencyHandler.taxationMap
				.get(from.getCurrency().getUniqueName());
		TaxationSet ts = map.containsKey(TaxationCase.LOANREPAYING) ? map.get(TaxationCase.LOANREPAYING) : null;
		double taxation = ts != null ? ts.getTaxInPercent() : 0.0;
		boolean taxAreExclusive = ts != null ? ts.isTaxAreExclusive() : true;
		/*
		 * loam = 10000
		 * inst = 0.1
		 * tax = 0.05
		 * toam = 10000*1.1*1.05 = 1
		 * amra = 100 : 115
		 * toam = 10000*1.1-10000*0.05 = 10500
		 * amra = 100 : 105
		 */
		if(taxAreExclusive)
		{
			toam =  loam+loam*inst+loam*taxation/100; //GesamtBetrag+Gesamtbetrag*Zinsen+Gesamtbetrag*Steuern
		} else
		{
			toam = loam+loam*inst;
		}
		double minimumpayment = Math.floor(toam/amra); //Minimum anzahl von Zahlungen
		lr.setTotalAmount(toam);
		lr.setLoanAmount(loam);
		lr.setAmountRatio(amra);
		lr.setInterest(inst);
		lr.setTaxInDecimal(taxation/100);
		PendingHandler.loanRepayment.replace(player.getUniqueId().toString(), lr);
		player.spigot().sendMessage(ChatApiOld.hover(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Amount.SetsAmounts"),
				HoverEvent.Action.SHOW_TEXT,
				plugin.getYamlHandler().getLang().getString("Cmd.Loan.Amount.Hover")
				.replace("%ta%", plugin.getIFHApi().format(toam, from.getCurrency()))
				.replace("%ar%", plugin.getIFHApi().format(amra, from.getCurrency()))
				.replace("%ar%", plugin.getIFHApi().format(loam, from.getCurrency()))
				.replace("%tax%", String.valueOf(taxation))
				.replace("%in%", String.valueOf(inst*100))
				.replace("%min%", String.valueOf(minimumpayment))));
		return;
	}
}