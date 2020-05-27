package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;

public class ARGMoneyFreeze extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyFreeze(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_FREEZE,StringValues.PERM_CMD_MONEY_FREEZE,
				AdvanceEconomy.moneyarguments,2,2,StringValues.ARG_MONEY_FREEZE_ALIAS,
				StringValues.MONEY_SUGGEST_FREEZE);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
		String playername = args[1];
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayerFromName(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		String message = "";
		if(eco.isFrozen())
		{
			eco.setFrozen(false);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYER, eco, "`id` = ?", eco.getId());
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Freeze.YouDefrozenSomeone")
					.replace("%player%", eco.getName())));
			message = ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Freeze.YourAccountWasDefrozen")
					.replace("%player%", player.getName()));
		} else
		{
			eco.setFrozen(true);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYER, eco, "`id` = ?", eco.getId());
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Freeze.YouFrozenSomeone")
					.replace("%player%", eco.getName())));
			message = ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Freeze.YourAccountWasFrozen")
					.replace("%player%", player.getName()));
		}
		boolean bungee = EconomySettings.settings.isBungee();
		if(eco.isMoneyPlayerFlow())
		{
			if(bungee)
			{
				BungeeBridge.sendBungeeMessage(player, eco.getUUID(), message, false, "");
			} else
			{
				if(Bukkit.getPlayer(UUID.fromString(eco.getUUID())) != null)
				{
					Bukkit.getPlayer(UUID.fromString(eco.getUUID())).sendMessage(message);
				}
			}
		}
		return;
	}
}