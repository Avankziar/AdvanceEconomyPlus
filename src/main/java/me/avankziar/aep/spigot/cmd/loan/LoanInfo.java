package main.java.me.avankziar.aep.spigot.cmd.loan;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.LoanRepayment;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class LoanInfo extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public LoanInfo(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String ids = args[1];
		int id = 0;
		LoanRepayment lr = null;
		Account from = null;
		Account to = null;
		if(args.length >= 2)
		{
			if(!MatchApi.isInteger(ids))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%args%", ids)));
				return;
			}
			id = Integer.parseInt(ids);
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanDontExist")));
				return;
			}
			lr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
			from = plugin.getIFHApi().getAccount(lr.getAccountFromID());
			to = plugin.getIFHApi().getAccount(lr.getAccountToID());
			if(!lr.getOwner().toString().equals(player.getUniqueId().toString())
					&& !lr.getDebtor().toString().equals(player.getUniqueId().toString())
					&& !from.getOwner().getUUID().toString().equals(player.getUniqueId().toString())
					&& !to.getOwner().getUUID().toString().equals(player.getUniqueId().toString())
					&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_LOAN)))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.NotLoanOwner")));
				return;
			}
		} else
		{
			if(!PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Loan.NoWaitingLoanProposal")));
				return;
			}
			lr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		}
		
		String low = Utility.convertUUIDToName(lr.getOwner().toString(), EconomyType.PLAYER);
		String ldb = Utility.convertUUIDToName(lr.getDebtor().toString(), EconomyType.PLAYER);
		if(low == null)
		{
			low = "/";
		}
		if(ldb == null)
		{
			ldb = "/";
		}
		double interestLoan = 0.0;
		boolean taxAreExclusive = (lr.getLoanAmount()+lr.getLoanAmount()*lr.getInterest()+lr.getLoanAmount()*lr.getTaxInDecimal()) > lr.getTotalAmount() ? true : false;
		double tnor = (lr.getTotalAmount()-lr.getAmountPaidSoFar())/lr.getAmountRatio();
		if(taxAreExclusive)
		{
			interestLoan = lr.getTotalAmount()-lr.getTotalAmount()*lr.getTaxInDecimal()-(lr.getTotalAmount()*(lr.getInterest()/100));
		} else
		{
			interestLoan = lr.getTotalAmount()-(lr.getTotalAmount()-(lr.getTotalAmount()*(lr.getInterest()/100)))*lr.getTaxInDecimal();
		}
		ArrayList<ArrayList<BaseComponent>> msg = new ArrayList<>();
		ArrayList<BaseComponent> m1 = new ArrayList<>();
		m1.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Info.Headline")
				.replace("%id%", ids)
				.replace("%name%", lr.getName())));
		msg.add(m1);
		ArrayList<BaseComponent> m2 = new ArrayList<>();
		m2.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Info.Participants")
				.replace("%debtor%", ldb)
				.replace("%owner%", low)));
		msg.add(m2);
		ArrayList<BaseComponent> m8 = new ArrayList<>();
		m8.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Info.Accounts")
				.replace("%fromaccount%", from.getAccountName())
				.replace("%fromowner%", from.getOwner().getName())
				.replace("%fromid%", String.valueOf(from.getID()))
				.replace("%toaccount%", to.getAccountName())
				.replace("%toowner%", to.getOwner().getName())
				.replace("%toid%", String.valueOf(to.getID()))));
		msg.add(m8);
		ArrayList<BaseComponent> m3 = new ArrayList<>();
		m3.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Info.Amounts")
				.replace("%amountpaidsofar%", plugin.getIFHApi().format(lr.getAmountPaidSoFar(), from.getCurrency()))
				.replace("%totalamount%", plugin.getIFHApi().format(lr.getTotalAmount(), from.getCurrency()))
				));
		msg.add(m3);
		ArrayList<BaseComponent> m4 = new ArrayList<>();
		m4.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Info.Interest")
				.replace("%interestamount%", plugin.getIFHApi().format(interestLoan, from.getCurrency()))
				.replace("%interest%", String.valueOf(lr.getInterest()*100))));
		msg.add(m4);
		ArrayList<BaseComponent> m9 = new ArrayList<>();
		m9.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Info.Tax")
				.replace("%amountpaidtotax%", plugin.getIFHApi().format(lr.getAmountPaidToTax(), from.getCurrency()))
				.replace("%taxrate%", String.valueOf(lr.getTaxInDecimal()))));
		msg.add(m9);
		ArrayList<BaseComponent> m5 = new ArrayList<>();
		m5.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Info.Ratio")
				.replace("%repeatingtime%", TimeHandler.getRepeatingTime(lr.getRepeatingTime()))
				.replace("%amountratio%", plugin.getIFHApi().format(lr.getAmountRatio(), from.getCurrency()))
				.replace("%tax%", plugin.getIFHApi().format(lr.getTaxInDecimal()*lr.getAmountRatio(), from.getCurrency()))));
		msg.add(m5);
		ArrayList<BaseComponent> m6 = new ArrayList<>();
		m6.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Info.Times")
				.replace("%starttime%", TimeHandler.getTime(lr.getStartTime()))
				.replace("%endtime%", TimeHandler.getTime(lr.getEndTime()))));
		msg.add(m6);
		ArrayList<BaseComponent> m7 = new ArrayList<>();
		m7.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Info.TheoreticalNumberOfRates")
				.replace("%theoreticalnumber%", String.valueOf(tnor))));
		msg.add(m7);
		for(ArrayList<BaseComponent> list : msg)
		{
			TextComponent tx = ChatApi.tctl("");
			tx.setExtra(list);
			player.spigot().sendMessage(tx);
		}
		return;
	}
}