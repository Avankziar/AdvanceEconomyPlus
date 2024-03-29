package main.java.me.avankziar.aep.spigot.cmd.loan;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.LoanRepayment;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;

public class LoanPause extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public LoanPause(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanDontExist")));
			return;
		}
		LoanRepayment dr = (LoanRepayment) plugin.getMysqlHandler().getData(MysqlHandler.Type.LOAN, "`id` = ?", id);
		if(!dr.getOwner().equals(player.getUniqueId().toString())
				&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_LOAN)))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.NotLoanOwner")));
			return;
		}
		if(dr.isForgiven())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyForgiven")));
			return;
		}
		if(dr.isFinished())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.LoanAlreadyPaidOff")));
			return;
		}
		if(dr.isPaused())
		{
			dr.setPaused(false);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
			String msg = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Pause.Unpaused")
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
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
			String msg = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Pause.Paused")
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