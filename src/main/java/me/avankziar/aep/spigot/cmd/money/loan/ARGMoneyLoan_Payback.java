package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.milkbowl.vault.economy.EconomyResponse;

public class ARGMoneyLoan_Payback extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Payback(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		String ids = args[2];
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanAlreadyForgiven")));
			return;
		}
		if(dr.isFinished())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanAlreadyPaidOff")));
			return;
		}
		double payback = dr.getTotalAmount() - dr.getAmountPaidSoFar();
		if(payback <= 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Payback.IsAlreadyPaidOff")));
			return;
		}
		dr.setAmountPaidSoFar(dr.getTotalAmount());
		dr.setFinished(true);
		EconomyResponse er = AdvancedEconomyPlus.getVaultApi().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(dr.getTo())), payback);
		if(!er.transactionSuccess())
		{
			player.sendMessage(ChatApi.tl(er.errorMessage));
			return;
		}
		String othername = Utility.convertUUIDToName(dr.getTo());
		if(othername == null)
		{
			othername = "/";
		}
		String message = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Payback.IsPayedBack")
				.replace("%player%", player.getName())
				.replace("%to%", othername)
				.replace("%name%", dr.getName())
				.replace("%id%", String.valueOf(id)));
		player.sendMessage(message);
		if(EconomySettings.settings.isBungee())
		{
			BungeeBridge.sendBungeeMessage(player, dr.getTo(), message, false, "");
		} else
		{
			if(Bukkit.getPlayer(UUID.fromString(dr.getTo())) != null)
			{
				Bukkit.getPlayer(UUID.fromString(dr.getTo())).sendMessage(message);
			}
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", id);
		return;
	}
}