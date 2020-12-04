package main.java.me.avankziar.aep.spigot.cmd.loan;

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
import main.java.me.avankziar.aep.spigot.object.AEPSettings;

public class ARGLoan_Transfer extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGLoan_Transfer(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		String ids = args[1];
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdLoan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(dr.isForgiven())
		{
			player.sendMessage(plugin.getYamlHandler().getL().getString("CmdLoan.LoanAlreadyForgiven"));
			return;
		}
		if(dr.isFinished())
		{
			player.sendMessage(plugin.getYamlHandler().getL().getString("CmdLoan.LoanAlreadyPaidOff"));
			return;
		}
		if(!dr.getLoanOwner().equals(player.getUniqueId().toString())
				&& !player.hasPermission(Utility.PERM_BYPASS_LOAN_TRANSFER))
		{
			player.sendMessage(plugin.getYamlHandler().getL().getString("CmdLoan.NotLoanOwner"));
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
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdLoan.Transfer.YouHasTransfered")
				.replace("%player%", othername)
				.replace("%name%", dr.getName())
				.replace("%id%", String.valueOf(id))));
		String toomessage = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdLoan.Transfer.YouHasBecomeLoanOwner")
				.replace("%player%", player.getName())
				.replace("%name%", dr.getName())
				.replace("%id%", String.valueOf(id)));
		if(AEPSettings.settings.isBungee())
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