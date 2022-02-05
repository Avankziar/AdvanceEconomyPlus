package main.java.me.avankziar.aep.spigot.cmd.cst;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.object.CommandStructurType;
import main.java.me.avankziar.aep.spigot.object.ne_w.AEPUser;

public class WalletNotification implements CommandExecutor
{
	private AdvancedEconomyPlus plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public WalletNotification(AdvancedEconomyPlus plugin, CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
	{
		this.plugin = plugin;
		this.cc = cc;
		this.ac = ac;
		this.cst = cst;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Cmd only for Players!");
			return false;
		}
		Player player = (Player) sender;
		String cmdString;
		if(cst == CommandStructurType.SINGLE)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			cmdString = cc.getCommandString();
			int zero = 0;
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero);
				}
			}.runTaskAsynchronously(plugin);
		} else
		{
			if(!player.hasPermission(ac.getPermission()))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			int zero = 0+1;
			cmdString = ac.getCommandString();
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero);
				}
			}.runTaskAsynchronously(plugin);
		}
		return true;
	}
	
	private void middlePart(Player player, String cmdString, String[] args,
			int zero)
	{
		if(args.length != zero)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cc.getCommandString())
					.replace("%amount%", String.valueOf(zero))));
			return;
		}
		AEPUser aepu = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
		if(aepu.isWalletMoneyFlowNotification())
		{
			aepu.setWalletMoneyFlowNotification(false);
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.WalletNotification.Deactive")));
		} else
		{
			aepu.setWalletMoneyFlowNotification(true);
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.WalletNotification.Active")));
		}
		plugin.getMysqlHandler().updateData(Type.PLAYERDATA, aepu, "`player_uuid` = ?", aepu.getUUID().toString());
	}
}