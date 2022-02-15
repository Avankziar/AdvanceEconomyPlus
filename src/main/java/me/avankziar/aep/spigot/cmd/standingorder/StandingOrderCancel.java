package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;

public class StandingOrderCancel extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public StandingOrderCancel(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoPendingOrder")));
			return;
		}
		PendingHandler.standingOrder.remove(player.getUniqueId().toString());
		player.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Cancel.IsCancelled")));
		return;
	}
}