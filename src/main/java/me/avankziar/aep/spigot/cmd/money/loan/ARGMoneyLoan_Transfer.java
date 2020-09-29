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

public class ARGMoneyLoan_Transfer extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Transfer(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(dr.isForgiven())
		{
			player.sendMessage(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanAlreadyForgiven"));
			return;
		}
		if(dr.isFinished())
		{
			player.sendMessage(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.LoanAlreadyPaidOff"));
			return;
		}
		if(!dr.getLoanOwner().equals(player.getUniqueId().toString())
				&& !player.hasPermission(Utility.PERM_BYPASS_LOAN_TRANSFER))
		{
			player.sendMessage(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.NotLoanOwner"));
			return;
		}
		String othername = args[3];
		String otheruuid = Utility.convertNameToUUID(othername);
		if(otheruuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		dr.setTo(otheruuid);
		dr.setLoanOwner(otheruuid);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Transfer.YouHasTransfered")
				.replace("%player%", othername)
				.replace("%name%", dr.getName())
				.replace("%id%", String.valueOf(id))));
		String toomessage = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Transfer.YouHasBecomeLoanOwner")
				.replace("%player%", player.getName())
				.replace("%name%", dr.getName())
				.replace("%id%", String.valueOf(id)));
		if(EconomySettings.settings.isBungee())
		{
			BungeeBridge.sendBungeeMessage(player, otheruuid, toomessage, false, "");
		} else
		{
			if(Bukkit.getPlayer(UUID.fromString(otheruuid)) != null)
			{
				Bukkit.getPlayer(UUID.fromString(otheruuid)).sendMessage(toomessage);
			}
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", id);
		return;
	}
}