package me.avankziar.aep.spigot.cmd.loan;

import java.io.IOException;
import java.util.UUID;

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
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class LoanTransfer extends ArgumentModule
{
	private AEP plugin;
	
	public LoanTransfer(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String ids = args[1];
		int id = 0;
		String acid = args[2];
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!MatchApi.isInteger(acid))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		Account ac = plugin.getIFHApi().getAccount(Integer.parseInt(acid));
		if(ac == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.AccountDontExist")
					.replace("%account%", acid)));
			return;
		}
		if(!plugin.getMysqlHandler().exist(MysqlType.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment lr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlType.LOAN, "`id` = ?", id);
		if(lr.isForgiven())
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyForgiven"));
			return;
		}
		if(lr.isFinished())
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyPaidOff"));
			return;
		}
		if(!lr.getOwner().toString().equals(player.getUniqueId().toString())
				&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_LOAN)))
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.Loan.NotLoanOwner"));
			return;
		}
		String othername = args[3];
		UUID otheruuid = Utility.convertNameToUUID(othername, EconomyType.PLAYER);
		if(otheruuid == null)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		lr.setAccountToID(ac.getID());
		lr.setOwner(otheruuid);
		player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Transfer.YouHasTransfered")
				.replace("%player%", othername)
				.replace("%name%", lr.getName())
				.replace("%id%", String.valueOf(id))));
		String toomessage = ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Transfer.YouHasBecomeLoanOwner")
				.replace("%player%", player.getName())
				.replace("%name%", lr.getName())
				.replace("%id%", String.valueOf(id)));
		if(Bukkit.getPlayer(otheruuid) == null)
		{
			//BungeeBridge.sendBungeeMessage(player, otheruuid.toString(), toomessage, false, "");
		} else
		{
			Bukkit.getPlayer(otheruuid.toString()).sendMessage(toomessage);
		}
		plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", id);
		return;
	}
}