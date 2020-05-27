package main.java.me.avankziar.advanceeconomy.spigot.assistance;

import java.time.LocalDate;
import java.time.LocalTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.handler.BankAccountHandler;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.BankAccount;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;

public class BackgroundTask
{
	private static AdvanceEconomy plugin;
	
	public BackgroundTask(AdvanceEconomy plugin)
	{
		BackgroundTask.plugin = plugin;
		initBackgroundTask();
	}
	
	public boolean initBackgroundTask()
	{
		runInsertMidnightTrendLog();
		return true;
	}
	
	public void runInsertMidnightTrendLog()
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				LocalTime lt = LocalTime.now();
				if(lt.getMinute() == 0 && lt.getHour() == 0 && (lt.getSecond() <= 5 || lt.getSecond() > 0))
				{
					for(Player player : Bukkit.getOnlinePlayers())
					{
						EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(player.getUniqueId().toString());
						if(eco == null)
						{
							AdvanceEconomy.getVaultApi().createPlayerAccount(player);
						}
						Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
								LocalDate.now(), eco.getUUID(), 0, eco.getBalance()));
					}
					int end = plugin.getMysqlHandler().lastID(MysqlHandler.Type.BANKACCOUNT);
					for(int i = 1; i <= end; i++)
					{
						if(plugin.getMysqlHandler().exist(MysqlHandler.Type.BANKACCOUNT, "`id` = ?", i))
						{
							BankAccount ba = BankAccountHandler.getBankAccount(i);
							if(ba != null)
							{
								Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
										LocalDate.now(), ba.getaccountNumber(), 0, ba.getBalance()));
							}
						}
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 20L*5);
	}
}
