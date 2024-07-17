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
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.ConfigHandler;
import me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class LoanInherit extends ArgumentModule
{
	private AEP plugin;
	
	public LoanInherit(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String ids = args[1];
		String acid = args[2];
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		if(!MatchApi.isInteger(acid))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", acid)));
			return;
		}
		id = Integer.parseInt(ids);
		Account newfrom = plugin.getIFHApi().getAccount(Integer.parseInt(acid));
		if(newfrom == null)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.AccountDontExist")
					.replace("%account%", acid)));
			return;
		}
		if(!ConfigHandler.isLoanEnabled(newfrom.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.CurrencyDontAllowLoan")
					.replace("%currency%", newfrom.getCurrency().getUniqueName())));
			return;
		}
		if(!plugin.getMysqlHandler().exist(MysqlType.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanDontExist")));
			return;
		}
		LoanRepayment lr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlType.LOAN, "`id` = ?", id);
		if(lr.isForgiven())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanAlreadyForgiven")));
			return;
		}
		if(lr.isFinished())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.LoanAlreadyPaidOff")));
			return;
		}
		Account to = plugin.getIFHApi().getAccount(lr.getAccountToID());
		if(!ConfigHandler.isLoanEnabled(to.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.CurrencyDontAllowLoan")
					.replace("%currency%", to.getCurrency().getUniqueName())));
			return;
		}
		if(!newfrom.getCurrency().getUniqueName().equals(to.getCurrency().getUniqueName()))
		{
			if(!newfrom.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", newfrom.getCurrency().getUniqueName())));
				return;
			}
			if(!to.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", to.getCurrency().getUniqueName())));
				return;
			}
		}
		String othername = args[3];
		UUID otheruuid = Utility.convertNameToUUID(othername, EconomyType.PLAYER);
		if(otheruuid == null)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		String olduuid = lr.getDebtor().toString();
		String oldname = Utility.convertUUIDToName(olduuid, EconomyType.PLAYER);
		if(oldname == null)
		{
			oldname = "/";
		}
		lr.setAccountFromID(newfrom.getID());
		lr.setDebtor(otheruuid);
		player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Inherit.SomeoneInherit")
				.replace("%newplayer%", othername)
				.replace("%oldplayer%", oldname)
				.replace("%name%", lr.getName())
				.replace("%id%", String.valueOf(id))));
		String toomessage = ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("CmdLoan.Inherit.YouInherit")
				.replace("%player%", player.getName())
				.replace("%oldplayer%", oldname)
				.replace("%name%", lr.getName())
				.replace("%id%", String.valueOf(id)));
		if(Bukkit.getPlayer(UUID.fromString(otheruuid.toString())) != null)
		{
			Bukkit.getPlayer(UUID.fromString(otheruuid.toString())).sendMessage(toomessage);
		} else
		{
			//BungeeBridge.sendBungeeMessage(player, otheruuid.toString(), toomessage, false, "");
		}
		plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", id);
		return;
	}
}