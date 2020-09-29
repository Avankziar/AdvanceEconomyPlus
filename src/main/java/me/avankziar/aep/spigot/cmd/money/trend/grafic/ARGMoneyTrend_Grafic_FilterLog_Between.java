package main.java.me.avankziar.aep.spigot.cmd.money.trend.grafic;

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

public class ARGMoneyTrend_Grafic_FilterLog_Between extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public ARGMoneyTrend_Grafic_FilterLog_Between(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		if(!MatchApi.isDouble(args[4]))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", args[4])));
			return;
		}
		min = Double.parseDouble(args[4]);
		if(!MatchApi.isDouble(args[5]))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", args[5])));
			return;
		}
		max = Double.parseDouble(args[5]);
		if(args.length >= 7)
		{
			String pagenumber = args[6];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length >= 8)
		{
			if(args[7].equals(playername))
			{
				playername = args[7];
			} else
			{
				if(!player.hasPermission(Utility.PERM_CMD_MONEY_FILTERLOGOTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				playername = args[7];
			}
		}		
		String cmdstring = plugin.getYamlHandler().getCom().getString(ac.getPath()+".CommandString")+min+" "+max+" ";
		LogMethodeHandler.trendLogBetween(plugin, Methode.GRAFIC, player, playername, min, max, page, cmdstring);
		return;
	}
}