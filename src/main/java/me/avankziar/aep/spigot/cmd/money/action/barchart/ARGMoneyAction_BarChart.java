package main.java.me.avankziar.aep.spigot.cmd.money.action.barchart;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;

public class ARGMoneyAction_BarChart extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyAction_BarChart(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("OtherCmd")));
		return;
	}

}