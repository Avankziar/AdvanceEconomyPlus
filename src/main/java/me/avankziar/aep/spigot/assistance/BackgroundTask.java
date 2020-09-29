package main.java.me.avankziar.aep.spigot.assistance;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler.BankAccountHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.aep.spigot.object.BankAccount;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import net.milkbowl.vault.economy.EconomyResponse;

public class BackgroundTask
{
	private static AdvancedEconomyPlus plugin;
	
	public BackgroundTask(AdvancedEconomyPlus plugin)
	{
		BackgroundTask.plugin = plugin;
		initBackgroundTask();
	}
	
	public boolean initBackgroundTask()
	{
		runInsertMidnightTrendLog();
		if(EconomySettings.settings.isLoanRepayment())
		{
			runDebtRepayment();
		}
		if(EconomySettings.settings.isStandingOrder())
		{
			runStandingOrderPayment();
		}
		return true;
	}
	
	public void runDebtRepayment()
	{
		int repeat = plugin.getYamlHandler().get().getInt("LoanRepaymentRepeatTime", 60);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<LoanRepayment> list = new ArrayList<>();
				try
				{
					list = ConvertHandler.convertListVI(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.LOAN, "`id`", false,
										"`forgiven` = ? AND `paused` = ? AND `finished` = ? AND `endtime` > ?",
										false, false, false, System.currentTimeMillis()+1000*60*60*30));
				} catch (IOException e)
				{
					return;
				}				
				for(LoanRepayment dr : list)
				{
					long now = System.currentTimeMillis();
					if(now > dr.getStartTime())
					{
						continue;
					}
					long sum = dr.getLastTime()+dr.getRepeatingTime();
					if(sum>=now)
					{
						continue;
					}
					String from = "";
					String to = "";
					EcoPlayer ecofrom = EcoPlayerHandler.getEcoPlayer(dr.getFrom());
					EcoPlayer ecoto = EcoPlayerHandler.getEcoPlayer(dr.getTo());
					try
					{
						from = Utility.convertUUIDToName(dr.getFrom());
						to = Utility.convertUUIDToName(dr.getTo());
					} catch (IOException e)
					{
						continue;
					}
					if(from == null || to == null)
					{
						continue;
					}
					EconomyResponse er = AdvancedEconomyPlus.getVaultApi().withdrawPlayer(
							Bukkit.getOfflinePlayer(UUID.fromString(dr.getFrom())), dr.getAmountRatio());
					if(!er.transactionSuccess())
					{
						continue;
					}
					EconomyResponse err = AdvancedEconomyPlus.getVaultApi().depositPlayer(
							Bukkit.getOfflinePlayer(UUID.fromString(dr.getTo())), dr.getAmountRatio());	
					if(!err.transactionSuccess())
					{
						AdvancedEconomyPlus.getVaultApi().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(dr.getFrom())), dr.getAmountRatio());
						continue;
					}
					dr.setLastTime(now);
					double totalamountPaidSoFar = dr.getAmountPaidSoFar()+dr.getAmountRatio();
					dr.setAmountPaidSoFar(totalamountPaidSoFar);
					if(dr.getTotalAmount() <= dr.getAmountPaidSoFar())
					{
						dr.setFinished(true);
					}
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, dr, "`id` = ?", dr.getId());
					Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
							LocalDateTime.now(), dr.getFrom(), dr.getTo(),
							from, to, plugin.getYamlHandler().getL().getString("LoanRepayment.Orderer"), dr.getAmountRatio(), 
							ActionLoggerEvent.Type.DEPOSIT_WITHDRAW, 
							plugin.getYamlHandler().getL().getString("LoanRepayment.Comment").replace("%name%", dr.getName())));
					Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
							LocalDate.now(), dr.getFrom(), -dr.getAmountRatio(), ecofrom.getBalance()));
					Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), dr.getTo(), dr.getAmountRatio(), ecoto.getBalance()));
				}
			}
		}.runTaskTimer(plugin, 10L, 20L*repeat);;
	}
	
	public void runStandingOrderPayment()
	{
		int repeat = plugin.getYamlHandler().get().getInt("StandingOrderRepeatTime", 60);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<StandingOrder> list = new ArrayList<>();
				try
				{
					list = ConvertHandler.convertListV(
							plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.STANDINGORDER, "`id`", false,
									"`cancelled` = ? AND `paused` = ?", false, false));
				} catch (IOException e)
				{
					return;
				}
				for(StandingOrder so : list)
				{
					long now = System.currentTimeMillis();
					long sum = so.getLastTime()+so.getRepeatingTime();
					if(sum>=now)
					{
						continue;
					}
					String from = "";
					String to = "";
					EcoPlayer ecofrom = EcoPlayerHandler.getEcoPlayer(so.getFrom());
					EcoPlayer ecoto = EcoPlayerHandler.getEcoPlayer(so.getTo());
					try
					{
						from = Utility.convertUUIDToName(so.getFrom());
						to = Utility.convertUUIDToName(so.getTo());
					} catch (IOException e)
					{
						continue;
					}
					if(from == null || to == null)
					{
						continue;
						//TODO Einer von Beiden ist ne bank.
					}
					if(ecofrom == null || ecoto == null)
					{
						continue;
						//Spieler existieren nicht
					}
					EconomyResponse er = AdvancedEconomyPlus.getVaultApi().withdrawPlayer(
							Bukkit.getOfflinePlayer(UUID.fromString(so.getFrom())), so.getAmount());
					if(!er.transactionSuccess())
					{
						so.setCancelled(true);
						plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getId());
						continue;
					}
					EconomyResponse err = AdvancedEconomyPlus.getVaultApi().depositPlayer(
							Bukkit.getOfflinePlayer(UUID.fromString(so.getTo())), so.getAmount());	
					if(!err.transactionSuccess())
					{
						AdvancedEconomyPlus.getVaultApi().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(so.getFrom())), so.getAmount());
						continue;
					}
					so.setLastTime(now);
					double totalamount = so.getAmountPaidSoFar()+so.getAmount();
					so.setAmountPaidSoFar(totalamount);
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getId());
					Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
							LocalDateTime.now(), so.getFrom(), so.getTo(),
							from, to, plugin.getYamlHandler().getL().getString("StandingOrder.Orderer"), so.getAmount(), 
							ActionLoggerEvent.Type.DEPOSIT_WITHDRAW, 
							plugin.getYamlHandler().getL().getString("StandingOrder.Comment").replace("%name%", so.getName())));
					Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
							LocalDate.now(), so.getFrom(), -so.getAmount(), ecofrom.getBalance()));
					Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), so.getTo(), so.getAmount(), ecoto.getBalance()));
				}
			}
		}.runTaskTimer(plugin, 5L, 20L*repeat);
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
							AdvancedEconomyPlus.getVaultApi().createPlayerAccount(player);
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
