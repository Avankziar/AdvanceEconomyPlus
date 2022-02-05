package main.java.me.avankziar.aep.spigot.assistance;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;
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
		if(AEPSettings.settings.isLoanRepayment() && AEPSettings.settings.isExecuteLoanPayment())
		{
			plugin.getLogger().info("Loan Task Timer activate...");
			runDebtRepayment();
		}
		if(AEPSettings.settings.isStandingOrder() && AEPSettings.settings.isExecuteStandingOrderPayment())
		{
			plugin.getLogger().info("StandingOrder Task Timer activate...");
			runStandingOrderPayment();
		}
		return true;
	}
	
	public void runDebtRepayment()
	{
		int repeat = plugin.getYamlHandler().getConfig().getInt("LoanRepaymentRepeatTime", 60);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<LoanRepayment> list = new ArrayList<>();
				try
				{
					list = ConvertHandler.convertListVI(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.LOAN, "`id` ASC",
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
					String from = Utility.convertUUIDToName(dr.getFrom(), EconomyType.PLAYER);
					String to = Utility.convertUUIDToName(dr.getTo(), EconomyType.PLAYER);
					OLD_AEPUser ecofrom = _AEPUserHandler_OLD.getEcoPlayer(from);
					OLD_AEPUser ecoto = _AEPUserHandler_OLD.getEcoPlayer(to);
					if(from == null || to == null)
					{
						continue;
					}
					EconomyResponse er = AdvancedEconomyPlus.getVault().withdrawPlayer(
							Bukkit.getOfflinePlayer(UUID.fromString(dr.getFrom())), dr.getAmountRatio());
					if(!er.transactionSuccess())
					{
						continue;
					}
					EconomyResponse err = AdvancedEconomyPlus.getVault().depositPlayer(
							Bukkit.getOfflinePlayer(UUID.fromString(dr.getTo())), dr.getAmountRatio());	
					if(!err.transactionSuccess())
					{
						AdvancedEconomyPlus.getVault().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(dr.getFrom())), dr.getAmountRatio());
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
							from, to, plugin.getYamlHandler().getLang().getString("LoanRepayment.Orderer"), dr.getAmountRatio(), 
							ActionLoggerEvent.Type.DEPOSIT_WITHDRAW, 
							plugin.getYamlHandler().getLang().getString("LoanRepayment.Comment")
							.replace("%name%", dr.getName())
							.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
							.replace("%totalpaid%", String.valueOf(dr.getAmountPaidSoFar()))
							.replace("%waitingamount%", String.valueOf(dr.getTotalAmount()-dr.getAmountPaidSoFar()))
							));
					Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
							LocalDate.now(), dr.getFrom(), -dr.getAmountRatio(), ecofrom.getBalance()));
					Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), dr.getTo(), dr.getAmountRatio(), ecoto.getBalance()));
				}
			}
		}.runTaskTimer(plugin, 10L, 20L*repeat);;
	}
	
	public void runStandingOrderPayment()
	{
		int repeat = plugin.getYamlHandler().getConfig().getInt("StandingOrderRepeatTime", 60);
		AEPSettings.debug(plugin, "INIT METHODE : RunStandingOrderPayment | Repeattime [s] = "+repeat);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				AEPSettings.debug(plugin, "> START RUN : StandingOrderPayment");
				ArrayList<StandingOrder> list = new ArrayList<>();
				try
				{
					list = ConvertHandler.convertListV(
							plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.STANDINGORDER, "`id` ASC",
									"`cancelled` = ? AND `paused` = ?", false, false));
					AEPSettings.debug(plugin, "> TRY : StandingOrderPayment");
				} catch (IOException e)
				{
					AEPSettings.debug(plugin, "> CATCH : StandingOrderPayment IOException: "+e.toString());
					return;
				}
				AEPSettings.debug(plugin, "> FOR-LOOP : StandingOrderPayment");
				for(StandingOrder so : list)
				{
					AEPSettings.debug(plugin, ">> NULLCHECK : StandingOrder == null == "+(so == null));
					long now = System.currentTimeMillis();
					long sum = so.getLastTime()+so.getRepeatingTime();
					if(sum >= now)
					{
						AEPSettings.debug(plugin, ">> CONTINUE : LastTime + RepeatingTime >= System.currentTimeMillis");
						continue;
					}
					AEPSettings.debug(plugin, ">> TRY : Convert UUID to Name");
					String from = Utility.convertUUIDToName(so.getFrom(), EconomyType.PLAYER);
					String to = Utility.convertUUIDToName(so.getTo(), EconomyType.PLAYER);
					if(from == null || to == null)
					{
						AEPSettings.debug(plugin, ">> CATCH : Convert UUID to Name failed");
						continue;
					}
					OLD_AEPUser ecofrom = _AEPUserHandler_OLD.getEcoPlayer(from);
					OLD_AEPUser ecoto = _AEPUserHandler_OLD.getEcoPlayer(to);
					if(from == null || to == null)
					{
						AEPSettings.debug(plugin, ">> CONTINUE : One is a Bank, Bank not implemented yet.");
						continue;
						//TODO Einer von Beiden ist ne bank.
					}
					if(ecofrom == null || ecoto == null)
					{
						AEPSettings.debug(plugin, ">> CONTINUE : One dont exist");
						continue; //Spieler existieren nicht
					}
					EconomyResponse er = AdvancedEconomyPlus.getVault().withdrawPlayer(
							Bukkit.getOfflinePlayer(UUID.fromString(so.getFrom())), so.getAmount());
					if(!er.transactionSuccess())
					{
						so.setCancelled(true);
						plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getId());
						AEPSettings.debug(plugin, ">> CONTINUE : Transaction WITHDRAW failed : "+er.errorMessage);
						continue;
					}
					EconomyResponse err = AdvancedEconomyPlus.getVault().depositPlayer(
							Bukkit.getOfflinePlayer(UUID.fromString(so.getTo())), so.getAmount());	
					if(!err.transactionSuccess())
					{
						AdvancedEconomyPlus.getVault().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(so.getFrom())), so.getAmount());
						AEPSettings.debug(plugin, ">> CONTINUE : Transaction DEPOSIT failed : "+err.errorMessage);
						continue;
					}
					so.setLastTime(now);
					double totalamount = so.getAmountPaidSoFar() + so.getAmount();
					so.setAmountPaidSoFar(totalamount);
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getId());
					Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
							LocalDateTime.now(),
							so.getFrom(), so.getTo(),
							from, to,
							plugin.getYamlHandler().getLang().getString("StandingOrder.Orderer"),
							so.getAmount(), 
							ActionLoggerEvent.Type.DEPOSIT_WITHDRAW, 
							plugin.getYamlHandler().getLang().getString("StandingOrder.Comment")
								.replace("%name%", so.getName())
								.replace("%totalpaid%", String.valueOf(so.getAmountPaidSoFar()))
								.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())));
					Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(
							LocalDate.now(), so.getFrom(), -so.getAmount(), ecofrom.getBalance()));
					Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), so.getTo(), so.getAmount(), ecoto.getBalance()));
					AEPSettings.debug(plugin, ">> FOR-LOOP END | START AT BEGIN");
				}
				AEPSettings.debug(plugin, "> LOOP-END");
			}
		}.runTaskTimer(plugin, 5L, 20L*repeat);
	}
}
