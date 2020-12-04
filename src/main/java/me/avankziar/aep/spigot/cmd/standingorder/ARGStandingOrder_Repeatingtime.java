package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.ChatApiSmall;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;

public class ARGStandingOrder_Repeatingtime extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGStandingOrder_Repeatingtime(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
					plugin.getYamlHandler().getL().getString("NoStandingOrder")));
			return;
		}
		String rt = args[1];
		long repeatingtime = 0;
		repeatingtime = TimeHandler.getRepeatingTime(rt);
		if(repeatingtime == 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdStandingOrder.RepeatingTime.WrongSyntax")));
			return;
		}
		if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdStandingOrder.NoPendingOrder")));
			return;
		}
		StandingOrder so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
		so.setRepeatingTime(repeatingtime);
		PendingHandler.standingOrder.replace(player.getUniqueId().toString(), so);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.RepeatingTime.SetRepeatingTime")
				.replace("%rt%", rt)));
		return;
	}
}