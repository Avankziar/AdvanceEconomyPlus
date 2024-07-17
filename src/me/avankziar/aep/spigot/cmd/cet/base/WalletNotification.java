package me.avankziar.aep.spigot.cmd.cet.base;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.cmd.tree.CommandConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.cmd.tree.CommandStructurType;

public class WalletNotification extends ArgumentModule implements CommandExecutor
{
	private AEP plugin;
	private CommandConstructor cc;
	private ArgumentConstructor ac;
	private CommandStructurType cst;
	
	public WalletNotification(CommandConstructor cc, ArgumentConstructor ac, CommandStructurType cst)
	{
		super(ac);
		this.plugin = AEP.getPlugin();
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
		if(cst == CommandStructurType.SINGLE)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			String cmdString = cc.getCommandString();
			int zero = 0;
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
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Cmd only for Players!");
			return;
		}
		Player player = (Player) sender;
		if(cst == CommandStructurType.NESTED)
		{
			if(!player.hasPermission(ac.getPermission()))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
			String cmdString = ac.getCommandString();
			int zero = 0+1;
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					middlePart(player, cmdString, args, zero);
				}
			}.runTaskAsynchronously(plugin);
		}
	}
	
	private void middlePart(Player player, String cmdString, String[] args,
			int zero)
	{
		if(args.length != zero)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.NotEnoughArguments")
					.replace("%cmd%", cmdString)
					.replace("%amount%", String.valueOf(zero))));
			return;
		}
		AEPUser aepu = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlType.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
		if(aepu.isWalletMoneyFlowNotification())
		{
			aepu.setWalletMoneyFlowNotification(false);
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.WalletNotification.Deactive")));
		} else
		{
			aepu.setWalletMoneyFlowNotification(true);
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.WalletNotification.Active")));
		}
		plugin.getMysqlHandler().updateData(MysqlType.PLAYERDATA, aepu, "`player_uuid` = ?", aepu.getUUID().toString());
	}
}