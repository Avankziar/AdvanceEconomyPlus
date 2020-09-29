package main.java.me.avankziar.aep.spigot.cmd.money.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.ChatApiSmall;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;

public class ARGMoneyStandingOrder_Starttime extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyStandingOrder_Starttime(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isStandingOrder())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoStandingOrder")));
			return;
		}
		String st = args[2];
		long starttime = 0;
		if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.NoPendingOrder")));
			return;
		}
		StandingOrder so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
		if(so.getAmount() <= 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.StartTime.Amount")));
			return;
		}
		if(so.getRepeatingTime() <= 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.StartTime.RepeatingTime")));
			return;
		}
		starttime = TimeHandler.getTime(st); //TODO wirft das keinen fehler?
		so.setStartTime(starttime);
		PendingHandler.standingOrder.replace(player.getUniqueId().toString(), so);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.StartTime.SetStartTime")
				.replace("%starttime%", st)));
		return;
	}
}