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
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.ifh.general.economy.action.OrdererType;

public class Recomment extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public Recomment(ArgumentConstructor ac)
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
	 * aep recomment <id> <category> <comment...>
	 */
	private void middlePart(Player player, String[] args)
	{
		int id = 0;
		String idstring = args[1];
		String category = args[2];
		String recomment = "";
		if(!MatchApi.isInteger(idstring))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", idstring)));
			return;
		}
		id = Integer.parseInt(idstring);
		StringBuilder sb = new StringBuilder();
		if(args.length >= 4)
		{
			for(int i = 3; i < args.length; i++)
			{
				if(i == args.length-1)
				{
					sb.append(args[i]);
				} else
				{
					sb.append(args[i]+" ");
				}
			}
			recomment = sb.toString();
		}
		ActionLogger el = (ActionLogger) plugin.getMysqlHandler().getData(MysqlType.ACTION, "`id` = ?",id);
		if(el == null)
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.ReComment.LogNotExist")));
			return;
		}
		if((el.getOrderType() == OrdererType.PLUGIN || el.getOrdererUUID() == null 
				|| (el.getOrdererUUID() != null && !el.getOrdererUUID().toString().equals(player.getUniqueId().toString())))
				&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_RECOMMENT)))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.ReComment.NoOrderer")));
			return;
		}
		el.setCategory(category);
		el.setComment(recomment);
		plugin.getMysqlHandler().updateData(MysqlType.ACTION, el, "`id` = ?", id);
		player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("Cmd.ReComment.CommentWasChange")
				.replace("%id%", idstring)
				.replace("%category%", category)
				.replace("%comment%", recomment)));
		return;
	}
}