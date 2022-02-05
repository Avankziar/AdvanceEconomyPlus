package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;

public class ARGStandingOrder_Cancel extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGStandingOrder_Cancel(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isStandingOrder())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoStandingOrder")));
			return;
		}
		if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdStandingOrder.NoPendingOrder")));
			return;
		}
		PendingHandler.standingOrder.remove(player.getUniqueId().toString());
		player.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Cancel.IsCancelled")));
		return;
	}
}