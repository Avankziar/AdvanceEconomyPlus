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
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;

public class LoanTransfer extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public LoanTransfer(ArgumentConstructor argumentConstructor)
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(dr.isForgiven())
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanAlreadyForgiven"));
			return;
		}
		if(dr.isFinished())
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanAlreadyPaidOff"));
			return;
		}
		if(!dr.getLoanOwner().equals(player.getUniqueId().toString())
				&& !player.hasPermission(Utility.PERM_BYPASS_LOAN_TRANSFER))
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("CmdLoan.NotLoanOwner"));
			return;
		}
		String othername = args[3];
		UUID otheruuid = Utility.convertNameToUUID(othername, EconomyType.PLAYER);
		if(otheruuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		dr.setTo(otheruuid.toString());
		dr.setLoanOwner(otheruuid.toString());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Transfer.YouHasTransfered")
				.replace("%player%", othername)
				.replace("%name%", dr.getName())
				.replace("%id%", String.valueOf(id))));
		String toomessage = ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Transfer.YouHasBecomeLoanOwner")
				.replace("%player%", player.getName())
				.replace("%name%", dr.getName())
				.replace("%id%", String.valueOf(id)));
		if(Bukkit.getPlayer(otheruuid) == null)
		{
			BungeeBridge.sendBungeeMessage(player, otheruuid.toString(), toomessage, false, "");
		} else
		{
			if(Bukkit.getPlayer(UUID.fromString(otheruuid.toString())) != null)
			{
				Bukkit.getPlayer(UUID.fromString(otheruuid.toString())).sendMessage(toomessage);
			}
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", id);
		return;
	}
}