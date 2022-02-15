package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountManagementType;
import net.md_5.bungee.api.chat.ClickEvent;

public class StandingOrderCreate extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public StandingOrderCreate(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String name = args[1];
		int fromid = -1;
		int toid = -1;
		if(!MatchApi.isInteger(args[2]))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[2])));
			return;
		}
		fromid = Integer.parseInt(args[2]);
		if(!MatchApi.isInteger(args[3]))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", args[3])));
			return;
		}
		toid = Integer.parseInt(args[3]);
		Account fac = plugin.getIFHApi().getAccount(fromid);
		Account tac = plugin.getIFHApi().getAccount(toid);
		if(fac == null || tac == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.AccountDontExist")
					.replace("%account%", fac == null ? args[2] : args[3])));
			return;
		}
		if(!ConfigHandler.isStandingOrderEnabled(fac.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.CurrencyDontMayUseStandingOrder")
					.replace("%currency%", fac.getCurrency().getUniqueName())));
			return;
		}
		if(!ConfigHandler.isStandingOrderEnabled(tac.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.CurrencyDontMayUseStandingOrder")
					.replace("%currency%", tac.getCurrency().getUniqueName())));
			return;
		}
		if(!fac.getCurrency().getUniqueName().equals(tac.getCurrency().getUniqueName()))
		{
			if(!fac.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", fac.getCurrency().getUniqueName())));
				return;
			}
			if(!tac.getCurrency().isExchangeable())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Exchange.CurrencyDontAllowExchange")
						.replace("%currency%", tac.getCurrency().getUniqueName())));
				return;
			}
		}
		boolean am = plugin.getIFHApi().canManageAccount(fac, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW);
		if(!am && !tac.getOwner().getName().equals(player.getName())
				&& !player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_STANDINGORDER)))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.ContractAtTheExpenseOfThirdParties")));
			return;
		}
		if(!am && tac.getOwner().getName().equals(player.getName())
				&& !player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_STANDINGORDER)))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.ContractAtTheExpenseOfOthersInYourFavour")));
			return;
		}
		if(PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.AlreadyPendingOrder")));
			return;
		}
		StandingOrder so = new StandingOrder(0, name, player.getUniqueId(), fac.getID(), tac.getID(), 0, 0, 0, 0, 0, 0, false, false);
		PendingHandler.standingOrder.put(player.getUniqueId().toString(), so);
		player.spigot().sendMessage(ChatApi.clickEvent(
				plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Create.OrderCreated"),
				ClickEvent.Action.RUN_COMMAND, CommandSuggest.get(null, CommandExecuteType.STORDER_INFO)));
		return;
	}
}