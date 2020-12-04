package main.java.me.avankziar.aep.spigot.cmd.money;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;

public class ARGMoneyToggle extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyToggle(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoBank")));
			return;
		}
		AEPUser eco = AEPUserHandler.getEcoPlayer(player);
		if(eco.isMoneyPlayerFlow())
		{
			eco.setMoneyPlayerFlow(false);
			plugin.getMysqlHandler().updateData(Type.PLAYER, eco, "`id` = ?", eco.getId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Toggle.SetOff")));
		} else
		{
			eco.setMoneyPlayerFlow(true);
			plugin.getMysqlHandler().updateData(Type.PLAYER, eco, "`id` = ?", eco.getId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Toggle.SetOn")));
		}
		return;
	}
}