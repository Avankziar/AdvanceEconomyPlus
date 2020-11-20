package main.java.me.avankziar.aep.spigot.cmd.money;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class ARGMoneyTop extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyTop(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		int page = 0;
		if(args.length == 2)
		{
			String pagenumber = args[1];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		int start = page*10;
		int end = 10;
		ArrayList<AEPUser> top = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getTop(Type.PLAYER, "`balance`", start, end));

		if(top.size()<10)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Top.NotEnoughValues")));
			return;
		}
		player.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getL().getString("CmdMoney.Top.Headline")
				.replace("%page%", String.valueOf(page))));
		for(AEPUser eco : top)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Top.TopLine")
					.replace("%place%", String.valueOf(start+1))
					.replace("%player%", eco.getName())
					.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
					.replace("%balance%", AdvancedEconomyPlus.getVaultApi().format(eco.getBalance()))));
			start++;
		}
		return;
	}
}