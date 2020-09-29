package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class ARGMoneyLoan_Info extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Info(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		String ids = args[2];
		int id = 0;
		LoanRepayment dr = null;
		if(args.length >= 3)
		{
			if(!MatchApi.isInteger(ids))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("NoNumber")
						.replace("%args%", ids)));
				return;
			}
			id = Integer.parseInt(ids);
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanDontExist")));
				return;
			}
			dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
			if(!dr.getLoanOwner().equals(player.getUniqueId().toString())
					&& !dr.getFrom().equals(player.getUniqueId().toString())
					&& !dr.getTo().equals(player.getUniqueId().toString())
					&& !player.hasPermission(Utility.PERM_BYPASS_LOAN_INFO))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.NotLoanOwner")));
				return;
			}
		} else
		{
			if(!PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdMoney.Loan.NoWaitingLoanProposal")));
				return;
			}
			dr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		}
		String from = Utility.convertUUIDToName(dr.getFrom());
		String to = Utility.convertUUIDToName(dr.getTo());
		String Loano = Utility.convertUUIDToName(dr.getLoanOwner());
		if(from == null)
		{
			from = "/";
		}
		if(to == null)
		{
			to = "/";
		}
		if(Loano == null)
		{
			Loano = "/";
		}
		double interestLoan = dr.getTotalAmount()-(1+(dr.getInterest()/100));
		double tnor = (dr.getTotalAmount()-dr.getAmountPaidSoFar())/dr.getAmountRatio();
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Info.Headline")
				.replace("%id%", ids)
				.replace("%name%", dr.getName())));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Info.Participants")
				.replace("%from%", from)
				.replace("%to%", to)
				.replace("%Loanowner%", Loano)));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Info.Amounts")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%amountpaidsofar%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getAmountPaidSoFar())))
				.replace("%totalamount%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getTotalAmount())))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Info.Interest")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%interestamount%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(interestLoan)))
				.replace("%interest%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getInterest())))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Info.Ratio")
				.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
				.replace("%repeatingtime%", TimeHandler.getRepeatingTime(dr.getRepeatingTime()))
				.replace("%amountratio%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getAmountRatio())))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Info.Times")
				.replace("%starttime%", TimeHandler.getTime(dr.getStartTime()))
				.replace("%endtime%", TimeHandler.getTime(dr.getEndTime()))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Info.TheoreticalNumberOfRates")
				.replace("%theoreticalnumber%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(tnor)))));
		return;
	}
}