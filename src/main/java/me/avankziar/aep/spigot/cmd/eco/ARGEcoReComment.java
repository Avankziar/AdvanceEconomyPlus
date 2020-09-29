package main.java.me.avankziar.aep.spigot.cmd.eco;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;

public class ARGEcoReComment extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGEcoReComment(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		int id = 0;
		String idstring = args[1];
		String recomment = "";
		if(!MatchApi.isInteger(idstring))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", idstring)));
			return;
		}
		id = Integer.parseInt(idstring);
		if(args.length >= 3)
		{
			for(int i = 2; i < args.length; i++)
			{
				if(i == args.length-1)
				{
					recomment += args[i];
				} else
				{
					recomment += args[i]+" ";
				}
				
			}
		}
		ActionLogger el = (ActionLogger) plugin.getMysqlHandler().getData(MysqlHandler.Type.ACTION, "`id` = ?",id);
		if(el == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdEco.ReComment.LogNotExist")));
			return;
		}
		if(!el.getOrdereruuid().equals(player.getUniqueId().toString()) && !player.hasPermission(Utility.PERM_BYPASS_RECOMMENT))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdEco.ReComment.NoOrderer")));
			return;
		}
		el.setComment(recomment);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.ACTION, el, "`id` = ?",id);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdEco.ReComment.CommentWasChange")
				.replace("%id%", idstring)
				.replace("%comment%", recomment)));
		return;
	}
}