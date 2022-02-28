package main.java.me.avankziar.aep.spigot.hook;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.LoggerApi;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;
import main.java.me.avankziar.ifh.spigot.economy.currency.CurrencyType;

public class JobsHook implements Listener
{
	private static AdvancedEconomyPlus plugin;
	private static HashMap<String,HashMap<String,Double>> joblist = new HashMap<>(); //Job, Playeruuid, Geld
	
	public JobsHook(AdvancedEconomyPlus plugin)
	{
		JobsHook.plugin = plugin;
		runTask();
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onJobs(JobsPrePaymentEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		String jobname = event.getJob().getName();
		String puuid = event.getPlayer().getUniqueId().toString();
		double amount = event.getAmount();
		if(joblist.containsKey(jobname))
		{
			HashMap<String,Double> playerlist = joblist.get(jobname);
			if(playerlist.containsKey(puuid))
			{
				double value = playerlist.get(puuid)+amount;
				playerlist.replace(puuid, value);
			} else
			{
				playerlist.put(puuid, amount);
			}
		} else
		{
			HashMap<String, Double> playermap = new HashMap<String,Double>();
			playermap.put(puuid, amount);
			joblist.put(jobname, playermap);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		loggerJobs(event.getPlayer().getUniqueId().toString());
	}
	
	public void runTask()
	{
		List<String> times = plugin.getYamlHandler().getConfig().getStringList("JobsReborn.HookTaskTimer");
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				LocalTime lt = LocalTime.now();
				if(times.contains(String.valueOf(lt.getMinute())))
				{
					loggerRunTask();
				}
			}
		}.runTaskTimer(plugin, 0L, 20L*60);
	}
	
	public static void loggerRunTask()
	{
		for(String job : joblist.keySet())
		{
			HashMap<String,Double> playerlist = joblist.get(job);
			for(String playeruuid : playerlist.keySet())
			{
				UUID puuid = UUID.fromString(playeruuid);
				double amount = playerlist.get(playeruuid);
				if(amount > 0.0)
				{
					String category = plugin.getYamlHandler().getLang().getString("JobsRebornHook.Category");
					String comment = plugin.getYamlHandler().getLang().getString("JobsRebornHook.Comment")
							.replace("%job%", job);
					Account to = plugin.getIFHApi().getDefaultAccount(puuid, AccountCategory.JOB, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
					if(to == null)
					{
						to = plugin.getIFHApi().getDefaultAccount(puuid, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
					}
					LoggerApi.addActionLogger(new ActionLogger(
							0,
							System.currentTimeMillis(),
							-1, to.getID(),
							-1, OrdererType.PLAYER, puuid, null, amount, amount, 0.0, category, comment));
					LoggerApi.addTrendLogger(LocalDate.now(), to.getID(), amount, to.getBalance());
				}
			}
		}
	}
	
	private void loggerJobs(final String playerUUID)
	{
		for(String job : joblist.keySet())
		{
			HashMap<String,Double> playerlist = joblist.get(job);
			for(String playeruuid : playerlist.keySet())
			{
				if(playeruuid.equals(playerUUID))
				{
					UUID puuid = UUID.fromString(playeruuid);
					double amount = playerlist.get(playeruuid);
					if(amount > 0.0)
					{
						String category = plugin.getYamlHandler().getLang().getString("JobsRebornHook.Category");
						String comment = plugin.getYamlHandler().getLang().getString("JobsRebornHook.Comment")
								.replace("%job%", job);
						Account to = plugin.getIFHApi().getDefaultAccount(puuid, AccountCategory.JOB, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
						if(to == null)
						{
							to = plugin.getIFHApi().getDefaultAccount(puuid, AccountCategory.MAIN, plugin.getIFHApi().getDefaultCurrency(CurrencyType.DIGITAL));
						}
						LoggerApi.addActionLogger(new ActionLogger(
								0,
								System.currentTimeMillis(),
								-1, to.getID(),
								-1, OrdererType.PLAYER, puuid, null, amount, amount, 0.0, category, comment));
						LoggerApi.addTrendLogger(LocalDate.now(), to.getID(), amount, to.getBalance());
						playerlist.replace(playeruuid, 0.0);
					}
				}
			}
		}
	}

}
