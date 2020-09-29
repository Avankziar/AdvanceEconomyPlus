package main.java.me.avankziar.aep.spigot.cmd.money.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import net.md_5.bungee.api.chat.ClickEvent;

public class ARGMoneyStandingOrder_Create extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyStandingOrder_Create(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
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
		String name = args[2];
		String from = args[3];
		String to = args[4];
		String fuuid = Utility.convertNameToUUID(from);
		String tuuid = Utility.convertNameToUUID(to);
		if(fuuid == null && tuuid == null)
		{
			//TODO Zwei Banken
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.BankNotImplemented")));
			return;
		} else if(fuuid == null || tuuid == null)
		{
			//TODO Eine Banken
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.BankNotImplemented")));
			return;
		} else
		{
			if(!from.equals(player.getName()) && !to.equals(player.getName())
					&& !player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_CREATE))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.ContractAtTheExpenseOfThirdParties")));
				return;
			}
			if(!from.equals(player.getName()) && to.equals(player.getName())
					&& !player.hasPermission(Utility.PERM_BYPASS_STANDINGORDER_CREATE))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.ContractAtTheExpenseOfOthersInYourFavour")));
				return;
			}
			if(PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.AlreadyPendingOrder")));
				return;
			}
			StandingOrder so = new StandingOrder(0, name, fuuid, tuuid, 0, 0, 0, 0, 0, false, false);
			PendingHandler.standingOrder.put(player.getUniqueId().toString(), so);
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.Create.OrderCreated"),
					ClickEvent.Action.RUN_COMMAND, plugin.getYamlHandler().getL().getString("CmdMoney.StandingOrder.InfoCmd")));
			return;
		}
	}
}