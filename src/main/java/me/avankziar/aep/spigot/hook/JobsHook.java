package main.java.me.avankziar.aep.spigot.hook;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;

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
		String playeruuid = event.getPlayer().getUniqueId().toString();
		double amount = event.getAmount();
		if(joblist.containsKey(jobname))
		{
			HashMap<String,Double> playerlist = joblist.get(jobname);
			if(playerlist.containsKey(playeruuid))
			{
				double value = playerlist.get(playeruuid)+amount;
				playerlist.replace(playeruuid, value);
			} else
			{
				playerlist.put(playeruuid, amount);
			}
		} else
		{
			HashMap<String, Double> playermap = new HashMap<String,Double>();
			playermap.put(playeruuid, amount);
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
		List<String> times = plugin.getYamlHandler().getConfig().getStringList("JobsRebornHookTaskTimer");
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
				OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(playeruuid));
				String playername = op.getName();
				double value = playerlist.get(playeruuid);
				if(value > 0.0)
				{
					//FIXME 
					/*
					Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
							LocalDateTime.now(),
							plugin.getYamlHandler().getLang().getString("JobsRebornHook.UUID"),
							playeruuid,
							plugin.getYamlHandler().getLang().getString("JobsRebornHook.Name"),
							playername,
							plugin.getYamlHandler().getLang().getString("JobsRebornHook.Orderer"),
							value,
							ActionLoggerEvent.Type.GIVEN,
							plugin.getYamlHandler().getLang().getString("JobsRebornHook.Comment")
							.replace("%job%", job)));
					Bukkit.getPluginManager().callEvent(
							new TrendLoggerEvent(LocalDate.now(), playeruuid, value, _AEPUserHandler_OLD.getEcoPlayer(op).getBalance()));
					playerlist.replace(playeruuid, 0.0);
					*/
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
					OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(playeruuid));
					String playername = op.getName();
					double value = playerlist.get(playeruuid);
					if(value > 0.0)
					{
						//FIXME 
						/*
						Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
								LocalDateTime.now(),
								plugin.getYamlHandler().getLang().getString("JobsRebornHook.UUID"),
								playeruuid,
								plugin.getYamlHandler().getLang().getString("JobsRebornHook.Name"),
								playername,
								plugin.getYamlHandler().getLang().getString("JobsRebornHook.Orderer"),
								value,
								ActionLoggerEvent.Type.GIVEN,
								plugin.getYamlHandler().getLang().getString("JobsRebornHook.Comment")
								.replace("%job%", job)));
						Bukkit.getPluginManager().callEvent(
								new TrendLoggerEvent(LocalDate.now(), playeruuid, value, _AEPUserHandler_OLD.getEcoPlayer(op).getBalance()));
						*/
						playerlist.replace(playeruuid, 0.0);
					}
				}
			}
		}
	}

}
