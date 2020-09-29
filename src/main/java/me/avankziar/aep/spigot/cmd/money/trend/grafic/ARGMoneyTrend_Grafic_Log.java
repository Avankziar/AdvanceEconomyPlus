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

public class ARGMoneyTrend_Grafic_Log extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public ARGMoneyTrend_Grafic_Log(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
		this.ac = argumentConstructor;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String playername = player.getName();
		int page = 0;
		if(args.length >= 4)
		{
			String pagenumber = args[3];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length >= 5)
		{
			if(!player.hasPermission(Utility.PERM_CMD_MONEY_TRENDGRAFIC_OTHER)
					&& !args[4].equals(playername))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return;
			}
			playername = args[4];
		}
		String cmdstring = plugin.getYamlHandler().getCom().getString(ac.getPath()+".CommandString");
		LogMethodeHandler.trendLog(plugin, Methode.GRAFIC, player, playername, page, cmdstring);
		return;
	}
}