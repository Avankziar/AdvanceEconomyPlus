package main.java.me.avankziar.aep.spigot.cmd.money;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;

public class ARGMoneyFreeze extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyFreeze(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
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
					plugin.getYamlHandler().getL().getString("CmdMoney.Freeze.YouDefrozenSomeone")
					.replace("%player%", eco.getName())));
			message = ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Freeze.YourAccountWasDefrozen")
					.replace("%player%", player.getName()));
		} else
		{
			eco.setFrozen(true);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYER, eco, "`id` = ?", eco.getId());
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Freeze.YouFrozenSomeone")
					.replace("%player%", eco.getName())));
			message = ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Freeze.YourAccountWasFrozen")
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