package me.avankziar.aep.spigot.cmd.cet.base;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.ActionLogger;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;

public class DeleteLog extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public DeleteLog(ArgumentConstructor ac)
	{
		super(ac);
		this.plugin = AEP.getPlugin();
		this.ac = ac;
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
		if(!player.hasPermission(ac.getPermission()))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				middlePart(player, args);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	/*
	 * aep deletelog <id>
	 */
	private void middlePart(Player player, String[] args)
	{
		int id = 0;
		String idstring = args[1];
		if(!MatchApi.isInteger(idstring))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", idstring)));
			return;
		}
		id = Integer.parseInt(idstring);
		ActionLogger el = (ActionLogger) plugin.getMysqlHandler().getData(MysqlType.ACTION, "`id` = ?",id);
		if(el == null)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.DeleteLog.LogNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlType.ACTION, "`id` = ?", id);
		player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.DeleteLog.LogWasDeleted")
				.replace("%id%", idstring)));
		return;
	}
}