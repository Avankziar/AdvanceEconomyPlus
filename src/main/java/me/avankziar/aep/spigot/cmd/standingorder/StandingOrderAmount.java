package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.StandingOrder;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;

public class StandingOrderAmount extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public StandingOrderAmount(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String amounts = args[1];
		int id = -1;
		double amount = 0;
		if(!MatchApi.isDouble(amounts))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%args%", amounts)));
			return;
		}
		amount = Double.parseDouble(amounts);
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NumberIsNegativ")
					.replace("%args%", amounts)));
			return;
		}
		if(args.length >= 3)
		{
			if(MatchApi.isInteger(args[2]))
			{
				id = Integer.parseInt(args[2]);
			}
		}
		if(id < 0)
		{
			if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
			{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoPendingOrder")));
					return;
			}
			StandingOrder so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
			so.setAmount(amount);
			PendingHandler.standingOrder.replace(player.getUniqueId().toString(), so);
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Amount.SetAmount")
					.replace("%name%", so.getName())
					.replace("%fromat%", plugin.getIFHApi().format(amount, plugin.getIFHApi().getAccount(so.getAccountFrom()).getCurrency()))));
		} else
		{
			StandingOrder so = (StandingOrder) plugin.getMysqlHandler().getData(MysqlHandler.Type.STANDINGORDER, "`id` = ?", id);
			if(!so.getOwner().toString().equals(player.getUniqueId().toString()) 
					&& !player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_STANDINGORDER)))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NotOrderer")));
				return;
			}
			so.setAmount(amount);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getID());
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.Amount.ChangeAmount")
					.replace("%name%", so.getName())
					.replace("%fromat%", plugin.getIFHApi().format(amount, plugin.getIFHApi().getAccount(so.getAccountFrom()).getCurrency()))));
		}		
		return;
	}
}