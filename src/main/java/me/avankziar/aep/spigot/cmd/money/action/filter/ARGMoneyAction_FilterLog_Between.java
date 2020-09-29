package main.java.me.avankziar.aep.spigot.cmd.money.action.filter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.LogMethodeHandler;
import main.java.me.avankziar.aep.spigot.handler.LogMethodeHandler.Methode;

public class ARGMoneyAction_FilterLog_Between extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public ARGMoneyAction_FilterLog_Between(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
		this.ac = argumentConstructor;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		double min = 0.0;
		double max = 0.0; 
		int page = 0;
		String playername = player.getName();
		if(!MatchApi.isDouble(args[3]))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", args[3])));
			return;
		}
		min = Double.parseDouble(args[3]);
		if(!MatchApi.isDouble(args[4]))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", args[4])));
			return;
		}
		max = Double.parseDouble(args[4]);
		if(args.length >= 6)
		{
			String pagenumber = args[5];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length >= 7)
		{
			if(args[6].equals(playername))
			{
				playername = args[6];
			} else
			{
				if(!player.hasPermission(Utility.PERM_CMD_MONEY_FILTERLOGOTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				playername = args[6];
			}
		}
		String cmdstring = plugin.getYamlHandler().getCom().getString(ac.getPath()+".CommandString")+min+" "+max+" ";
		LogMethodeHandler.actionLogBetween(plugin, Methode.LOG, player, playername, min, max, page, cmdstring);
		return;
	}
}