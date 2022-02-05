package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.KeyHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;
import net.md_5.bungee.api.chat.ClickEvent;

public class ARGStandingOrder_Create extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGStandingOrder_Create(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		String name = args[1];
		String from = args[2];
		String to = args[3];
		UUID fuuid = Utility.convertNameToUUID(from, EconomyType.PLAYER);
		UUID tuuid = Utility.convertNameToUUID(to, EconomyType.PLAYER);
		if(fuuid == null && tuuid == null)
		{
			//TODO Zwei Banken
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdStandingOrder.BankNotImplemented")));
			return;
		} else if(fuuid == null || tuuid == null)
		{
			//TODO Eine Banken
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdStandingOrder.BankNotImplemented")));
			return;
		} else
		{
			if(!from.equals(player.getName()) && !to.equals(player.getName())
					&& !player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_CREATE))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("CmdStandingOrder.ContractAtTheExpenseOfThirdParties")));
				return;
			}
			if(!from.equals(player.getName()) && to.equals(player.getName())
					&& !player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_CREATE))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("CmdStandingOrder.ContractAtTheExpenseOfOthersInYourFavour")));
				return;
			}
			if(PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("CmdStandingOrder.AlreadyPendingOrder")));
				return;
			}
			StandingOrder so = new StandingOrder(0, name, fuuid.toString(), tuuid.toString(), 0, 0, 0, 0, 0, false, false);
			PendingHandler.standingOrder.put(player.getUniqueId().toString(), so);
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("CmdStandingOrder.Create.OrderCreated"),
					ClickEvent.Action.RUN_COMMAND, AEPSettings.settings.getCommands(KeyHandler.SO_INFO)));
			return;
		}
	}
}