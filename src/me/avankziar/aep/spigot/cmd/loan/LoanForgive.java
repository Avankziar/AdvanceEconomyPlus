package me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.assistance.Utility;
import me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;

public class LoanForgive extends ArgumentModule
{
	private AEP plugin;
	
	public LoanForgive(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String ids = args[1];
		String confirm = "";
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(args.length >= 3)
		{
			confirm = args[2];
		}
		if(!plugin.getMysqlHandler().exist(MysqlType.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment lr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlType.LOAN, "`id` = ?", id);
		if(lr.isForgiven())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Forgive.CanBeUndone")));
			return;
		}
		if(!lr.getOwner().toString().equals(player.getUniqueId().toString())
				&& !player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_LOAN)))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.NotLoanOwner")));
			return;
		}
		if(!confirm.equalsIgnoreCase(plugin.getYamlHandler().getLang().getString("Cmd.Loan.ConfirmTerm")))
		{
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.Accept.PleaseConfirm")
					.replace("%cmd%", CommandSuggest.get(null, CommandExecuteType.LOAN_FORGIVE)
							.replace(" ", "+")+"+"+plugin.getYamlHandler().getLang().getString("Cmd.Loan.ConfirmTerm"))));
			return;
		}
		double dif = lr.getTotalAmount()-lr.getAmountPaidSoFar();
		if(dif <= 0)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyPaidOff")));
			return;
		}
		lr.setForgiven(true);
		String low = Utility.convertUUIDToName(lr.getOwner().toString(), EconomyType.PLAYER);
		String ldb = Utility.convertUUIDToName(lr.getDebtor().toString(), EconomyType.PLAYER);
		plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", lr.getId());
		String tomsg = ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Forgive.LoanIsForgiven")
				.replace("%currency%", AEP.getVault().currencyNamePlural())
				.replace("%dif%", plugin.getIFHApi().format(dif, plugin.getIFHApi().getAccount(lr.getAccountFromID()).getCurrency()))
				.replace("%debtor%", ldb != null ? ldb : "N.A.")
				.replace("%owner%", low != null ? low : "N.A.")
				.replace("%player%", player.getName()));
		if(Bukkit.getPlayer(lr.getDebtor()) == null)
		{
			//BungeeBridge.sendBungeeMessage(player, dr.getDebtor().toString(), tomsg, false, "");
		} else
		{
			Bukkit.getPlayer(lr.getDebtor()).sendMessage(tomsg);
		}
		if(lr.getOwner().toString().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(tomsg);
		} else
		{
			player.sendMessage(tomsg);
			if(Bukkit.getPlayer(lr.getOwner()) == null)
			{
				//BungeeBridge.sendBungeeMessage(player, dr.getOwner().toString(), tomsg, false, "");
			} else
			{
				Bukkit.getPlayer(lr.getOwner()).sendMessage(tomsg);
			}
		}
		return;
	}
}