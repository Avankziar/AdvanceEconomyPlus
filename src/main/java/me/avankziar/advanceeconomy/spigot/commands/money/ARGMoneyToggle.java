package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;

public class ARGMoneyToggle extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyToggle(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_TOGGLE,StringValues.PERM_CMD_MONEY_TOGGLE,
				AdvanceEconomy.moneyarguments,1,1,StringValues.ARG_MONEY_TOGGLE_ALIAS,
				StringValues.MONEY_SUGGEST_TOGGLE);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoBank")));
			return;
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(player);
		if(eco.isMoneyPlayerFlow())
		{
			eco.setMoneyPlayerFlow(false);
			plugin.getMysqlHandler().updateData(Type.PLAYER, eco, "`id` = ?", eco.getId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Toggle.SetOff")));
		} else
		{
			eco.setMoneyPlayerFlow(true);
			plugin.getMysqlHandler().updateData(Type.PLAYER, eco, "`id` = ?", eco.getId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Toggle.SetOn")));
		}
		return;
	}
}