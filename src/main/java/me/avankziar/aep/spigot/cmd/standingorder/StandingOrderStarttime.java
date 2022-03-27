package main.java.me.avankziar.aep.spigot.cmd.standingorder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.StandingOrder;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.ChatApiSmall;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

public class StandingOrderStarttime extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public StandingOrderStarttime(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String st = args[1];
		long starttime = 0;
		if(!PendingHandler.standingOrder.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.NoPendingOrder")));
			return;
		}
		StandingOrder so = PendingHandler.standingOrder.get(player.getUniqueId().toString());
		if(so.getAmount() <= 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.StartTime.Amount")
					.replace("%amountcmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_AMOUNT).replace(" ", "+"))));
			return;
		}
		if(so.getRepeatingTime() <= 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.StartTime.RepeatingTime")
					.replace("%repeatingtimecmd%", CommandSuggest.get(null, CommandExecuteType.STORDER_REPEATINGTIME).replace(" ", "+"))));
			return;
		}
		Account fac = plugin.getIFHApi().getAccount(so.getAccountFrom());
		Account tac = plugin.getIFHApi().getAccount(so.getAccountTo());
		if(fac == null || tac == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.AccountDontExist")
					.replace("%account%", fac != null ? String.valueOf(so.getAccountFrom()) : String.valueOf(so.getAccountTo()))));
			return;
		}
		if(so.getRepeatingTime() < ConfigHandler.getStandingOrderSpamProtection(fac.getCurrency().getUniqueName())
				|| so.getAmount() < ConfigHandler.getStandingOrderValueProtection(fac.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.StartTime.SpamProtection")
					.replace("%amount%", String.valueOf(ConfigHandler.getStandingOrderValueProtection(fac.getCurrency().getUniqueName())))
					.replace("%repeatingtime%", TimeHandler.getRepeatingTime(ConfigHandler.getStandingOrderSpamProtection(fac.getCurrency().getUniqueName())))));
			return;
		}
		if(so.getRepeatingTime() < ConfigHandler.getStandingOrderSpamProtection(tac.getCurrency().getUniqueName())
				|| so.getAmount() < ConfigHandler.getStandingOrderValueProtection(tac.getCurrency().getUniqueName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.StartTime.SpamProtection")
					.replace("%amount%", String.valueOf(ConfigHandler.getStandingOrderValueProtection(tac.getCurrency().getUniqueName())))
					.replace("%repeatingtime%", TimeHandler.getRepeatingTime(ConfigHandler.getStandingOrderSpamProtection(tac.getCurrency().getUniqueName())))));
			return;
		}
		starttime = TimeHandler.getTime(st);
		if(starttime == 0)
		{
			player.spigot().sendMessage(ChatApiSmall.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.StartTime.WrongSyntax")));
			return;
		}
		so.setStartTime(starttime);
		so.setLastTime(starttime - so.getRepeatingTime());
		PendingHandler.standingOrder.remove(player.getUniqueId().toString());
		plugin.getMysqlHandler().create(Type.STANDINGORDER, so);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.StandingOrder.StartTime.SetStartTime")
				.replace("%starttime%", st)));
		return;
		
	}
}