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
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;

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
		if(!AEPSettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoPlayerAccount")));
			return;
		}
		OLD_AEPUser eco = _AEPUserHandler_OLD.getEcoPlayer(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		String message = "";
		if(eco.isFrozen())
		{
			eco.setFrozen(false);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYER, eco, "`id` = ?", eco.getId());
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdMoney.Freeze.YouDefrozenSomeone")
					.replace("%player%", eco.getName())));
			message = ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdMoney.Freeze.YourAccountWasDefrozen")
					.replace("%player%", player.getName()));
		} else
		{
			eco.setFrozen(true);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYER, eco, "`id` = ?", eco.getId());
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdMoney.Freeze.YouFrozenSomeone")
					.replace("%player%", eco.getName())));
			message = ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdMoney.Freeze.YourAccountWasFrozen")
					.replace("%player%", player.getName()));
		}
		boolean bungee = AEPSettings.settings.isBungee();
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