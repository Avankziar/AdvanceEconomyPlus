package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;

public class ARGStandingOrder_Amount extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGStandingOrder_Amount(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		String amounts = args[1];
		double amount = 0;
		if(!MatchApi.isDouble(amounts))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", amounts)));
			return;
		}
		amount = Double.parseDouble(amounts);
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", amounts)));
			return;
		}
		if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdStandingOrder.NoPendingOrder")));
			return;
		}
		StandingOrder so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
		so.setAmount(amount);
		PendingHandler.standingOrder.replace(player.getUniqueId().toString(), so);
		player.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getL().getString("CmdStandingOrder.Amount.SetAmount")
				.replace("%name%", so.getName())
				.replace("%amount%", amounts)
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())));
		return;
	}

}