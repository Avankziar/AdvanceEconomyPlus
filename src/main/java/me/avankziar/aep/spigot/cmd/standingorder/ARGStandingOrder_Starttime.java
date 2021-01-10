package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.ChatApiSmall;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.KeyHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;

public class ARGStandingOrder_Starttime extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGStandingOrder_Starttime(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		String st = args[1];
		long starttime = 0;
		if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdStandingOrder.NoPendingOrder")));
			return;
		}
		StandingOrder so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
		if(so.getAmount() <= 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdStandingOrder.StartTime.Amount")
					.replace("%amountcmd%", AEPSettings.settings.getCommands(KeyHandler.SO_AMOUNT).replace(" ", "+"))));
			return;
		}
		if(so.getRepeatingTime() <= 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdStandingOrder.StartTime.RepeatingTime")
					.replace("%repeatingtimecmd%", AEPSettings.settings.getCommands(KeyHandler.SO_REPEATINGTIME).replace(" ", "+"))));
			return;
		}
		if(so.getRepeatingTime() < AEPSettings.settings.getStandingOrderSpamProtection()
				&& so.getAmount() < AEPSettings.settings.getStandingOrderValueProtection())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.StartTime.SpamProtection")
					.replace("%amount%", String.valueOf(AEPSettings.settings.getStandingOrderValueProtection()))
					.replace("%repeatingtime%", TimeHandler.getRepeatingTime(AEPSettings.settings.getStandingOrderSpamProtection()))));
			return;
		}
		starttime = TimeHandler.getTime(st);
		if(starttime == 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdStandingOrder.StartTime.WrongSyntax")));
			return;
		}
		so.setStartTime(starttime);
		long lastTime = starttime- (long) (so.getRepeatingTime()*0.9);
		so.setLastTime(lastTime);
		PendingHandler.standingOrder.remove(player.getUniqueId().toString());
		plugin.getMysqlHandler().create(Type.STANDINGORDER, so);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdStandingOrder.StartTime.SetStartTime")
				.replace("%starttime%", st)));
		return;
	}
}