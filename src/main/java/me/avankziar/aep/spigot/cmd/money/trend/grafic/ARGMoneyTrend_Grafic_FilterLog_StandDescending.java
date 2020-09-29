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

public class ARGMoneyTrend_Grafic_FilterLog_StandDescending extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public ARGMoneyTrend_Grafic_FilterLog_StandDescending(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
		this.ac = argumentConstructor;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		int page = 0;
		String playername = player.getName();
		if(args.length >= 5)
		{
			String pagenumber = args[4];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length >= 6)
		{
			if(args[5].equals(playername))
			{
				playername = args[5];
			} else
			{
				if(!player.hasPermission(Utility.PERM_CMD_MONEY_FILTERLOGOTHER))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				playername = args[5];
			}
		}
		String cmdstring = plugin.getYamlHandler().getCom().getString(ac.getPath()+".CommandString");
		LogMethodeHandler.trendLogStand(plugin, Methode.GRAFIC, player, playername, true, page, cmdstring);
		return;
	}
}