package main.java.me.avankziar.aep.spigot.cmd.loan;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

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
		String acid = args[2];
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!MatchApi.isInteger(acid))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		Account ac = plugin.getIFHApi().getAccount(Integer.parseInt(acid));
		if(ac == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.AccountDontExist")
					.replace("%account%", acid)));
			return;
		}
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment lr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		lr.setAccountToID(ac.getID());
		lr.setOwner(otheruuid);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Transfer.YouHasTransfered")
				.replace("%player%", othername)
				.replace("%name%", lr.getName())
				.replace("%id%", String.valueOf(id))));
		String toomessage = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Transfer.YouHasBecomeLoanOwner")
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
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, lr, "`id` = ?", id);
		return;
	}
}