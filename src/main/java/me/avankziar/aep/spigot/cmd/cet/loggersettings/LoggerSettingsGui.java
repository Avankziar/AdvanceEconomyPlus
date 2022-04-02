package main.java.me.avankziar.aep.spigot.cmd.cet.loggersettings;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm.Type;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler;
import main.java.me.avankziar.aep.spigot.handler.LoggerSettingsHandler.Methode;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

public class LoggerSettingsGui extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public LoggerSettingsGui(ArgumentConstructor ac)
	{
		super(ac);
		this.plugin = BaseConstructor.getPlugin();
		this.ac = ac;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Cmd only for Players!");
			return;
		}
		Player player = (Player) sender;
		middlePart(player, args);
	}
	
	/*
	 * 
	 */
	private void middlePart(Player player, String[] args)
	{
		int page = 0;
		int accountID = -1;
		Methode methode = Methode.LOG;
		if(args.length >= 4)
		{
			String otherplayername = player.getName();
			String acn = args[3];
			if(args[2].equals(otherplayername))
			{
				otherplayername = args[2];
			} else
			{
				if(!player.hasPermission(ExtraPerm.get(Type.BYPASS_LOGGERSETTINGS_OTHER)))
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				otherplayername = args[2];
			}
			AEPUser aep = (AEPUser) plugin.getMysqlHandler().getData(
					MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", otherplayername);
			if(aep == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
				return;
			}
			Account ac = plugin.getIFHApi().getAccount(new EconomyEntity(EconomyType.PLAYER, aep.getUUID(), aep.getName()), acn);
			if(ac == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.Pay.AccountDontExist")));
				return;
			}
			accountID = ac.getID();
		}
		if(args.length >= 5)
		{
			String pagenumber = args[4];
			if(MatchApi.isInteger(pagenumber))
			{
				page = Integer.parseInt(pagenumber);
			}
		}
		if(args.length >= 6)
		{
			try
			{
				methode = Methode.valueOf(args[5]);
			} catch(EnumConstantNotPresentException e) {}
		}
		if(args.length < 3)
		{
			AEPUser aep = (AEPUser) plugin.getMysqlHandler().getData(
					MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", player.getName());
			if(aep == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
				return;
			}
			accountID = plugin.getIFHApi().getQuickPayAccount(plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL), aep.getUUID());
			if(accountID < 0)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("Cmd.QuickPayDontExist")));
				return;
			}
			new LoggerSettingsHandler(plugin).generateGUI(player, player.getUniqueId(), accountID, null, page);
		} else
		{
			if(!LoggerSettingsHandler.getLoggerSettings().containsKey(player.getUniqueId()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Log.NoLoggerSettingsFound")));
				return;
			}
			LoggerSettings fst = LoggerSettingsHandler.getLoggerSettings().get(player.getUniqueId());
			try
			{
				new LoggerSettingsHandler(plugin).forwardingToOutput(player, fst, LoggerSettingsHandler.Access.GUI, methode, page, ac.getCommandString());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return;
	}
}