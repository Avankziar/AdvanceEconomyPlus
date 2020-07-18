package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.general.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.handler.ConvertHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;

public class ARGMoneyTop extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyTop(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_TOP, StringValues.PERM_CMD_MONEY_TOP,
				AdvanceEconomy.moneyarguments,1,2,StringValues.ARG_MONEY_TOP_ALIAS,
				StringValues.MONEY_SUGGEST_TOP);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
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
		ArrayList<EcoPlayer> top = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getTop(Type.PLAYER, "`balance`", start, end));

		if(top.size()<10)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Top.NotEnoughValues")));
			return;
		}
		player.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getL().getString(path+"Top.Headline")
				.replace("%page%", String.valueOf(page))));
		for(EcoPlayer eco : top)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Top.TopLine")
					.replace("%place%", String.valueOf(start+1))
					.replace("%player%", eco.getName())
					.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
					.replace("%balance%", AdvanceEconomy.getVaultApi().format(eco.getBalance()))));
			start++;
		}
		return;
	}
}