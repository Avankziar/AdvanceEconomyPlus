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
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;

public class LoanPause extends ArgumentModule
{
	private AEP plugin;
	
	public LoanPause(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String ids = args[1];
		int id = 0;
		if(!MatchApi.isInteger(ids))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", ids)));
			return;
		}
		id = Integer.parseInt(ids);
		if(!plugin.getMysqlHandler().exist(MysqlType.LOAN, "`id` = ?", id))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlType.LOAN, "`id` = ?", id);
		if(!dr.getOwner().equals(player.getUniqueId().toString())
				&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_LOAN)))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.NotLoanOwner")));
			return;
		}
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyForgiven")));
			return;
		}
		if(dr.isFinished())
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyPaidOff")));
			return;
		}
		if(dr.isPaused())
		{
			dr.setPaused(false);
			plugin.getMysqlHandler().updateData(MysqlType.LOAN, dr, "`id` = ?", dr.getId());
			String msg = ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Pause.Unpaused")
					.replace("%name%", dr.getName())
					.replace("%player%", player.getName()));
			player.sendMessage(msg);
			if(Bukkit.getPlayer(dr.getDebtor()) != null)
			{
				Bukkit.getPlayer(dr.getDebtor()).sendMessage(msg);
			} else
			{
				//BungeeBridge.sendBungeeMessage(player, fromplayer.getUUID(), msg, false, "");
			}
		} else
		{
			dr.setPaused(true);
			plugin.getMysqlHandler().updateData(MysqlType.LOAN, dr, "`id` = ?", dr.getId());
			String msg = ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Pause.Paused")
					.replace("%name%", dr.getName())
					.replace("%player%", player.getName()));
			player.sendMessage(msg);
			if(Bukkit.getPlayer(dr.getDebtor()) != null)
			{
				Bukkit.getPlayer(dr.getDebtor()).sendMessage(msg);
			} else
			{
				//BungeeBridge.sendBungeeMessage(player, fromplayer.getUUID(), msg, false, "");
			}
		}
		return;
	}
}