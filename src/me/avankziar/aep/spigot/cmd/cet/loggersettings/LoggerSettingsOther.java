package me.avankziar.aep.spigot.cmd.cet.loggersettings;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import me.avankziar.ifh.general.economy.currency.CurrencyType;

public class LoggerSettingsOther extends ArgumentModule
{
	private AEP plugin;
	private ArgumentConstructor ac;
	
	public LoggerSettingsOther(ArgumentConstructor ac)
	{
		super(ac);
		this.plugin = AEP.getPlugin();
		this.ac = ac;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Cmd only for Players!");
			return;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(ac.getPermission()))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		middlePart(player, args);
	}
	
	/*
	 * 
	 */
	private void middlePart(Player player, String[] args)
	{
		int page = 0;
		String otherplayername = args[2];
		AEPUser aep = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlType.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
		if(aep == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(!LoggerSettingsHandler.getLoggerSettings().containsKey(aep.getUUID()))
		{
			player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("NoOtherLoggerSettingsFound")));
			return;
		}
		int accountID = plugin.getIFHApi().getQuickPayAccount(plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL), aep.getUUID());
		if(accountID < 0)
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.QuickPayDontExist")));
			return;
		}
		new LoggerSettingsHandler(plugin).generateGUI(player, aep.getUUID(), accountID, null, page);
		return;
	}
}