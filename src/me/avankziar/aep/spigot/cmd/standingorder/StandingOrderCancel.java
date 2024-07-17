package me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import me.avankziar.aep.spigot.handler.PendingHandler;

public class StandingOrderCancel extends ArgumentModule
{
	private AEP plugin;
	
	public StandingOrderCancel(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = AEP.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoPendingOrder")));
			return;
		}
		PendingHandler.standingOrder.remove(player.getUniqueId().toString());
		player.sendMessage(ChatApiOld.tl(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Cancel.IsCancelled")));
		return;
	}
}