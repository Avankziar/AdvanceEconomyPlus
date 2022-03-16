package main.java.me.avankziar.aep.spigot.cmd.cet.base;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.ActionLogger;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.ifh.general.economy.action.OrdererType;

public class Recomment extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public Recomment(ArgumentConstructor ac)
	{
		super(ac);
		this.plugin = BaseConstructor.getPlugin();
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
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
			player.sendMessage(ChatApi.tl(
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
		ActionLogger el = (ActionLogger) plugin.getMysqlHandler().getData(MysqlHandler.Type.ACTION, "`id` = ?",id);
		if(el == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.ReComment.LogNotExist")));
			return;
		}
		if((el.getOrderType() == OrdererType.PLUGIN || el.getOrdererUUID() == null 
				|| (el.getOrdererUUID() != null && !el.getOrdererUUID().toString().equals(player.getUniqueId().toString())))
				&& !player.hasPermission(ExtraPerm.get(Type.BYPASS_RECOMMENT)))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.ReComment.NoOrderer")));
			return;
		}
		el.setCategory(category);
		el.setComment(recomment);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.ACTION, el, "`id` = ?", id);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.ReComment.CommentWasChange")
				.replace("%id%", idstring)
				.replace("%category%", category)
				.replace("%comment%", recomment)));
		return;
	}
}