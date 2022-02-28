package main.java.me.avankziar.aep.spigot.assistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.economy.CurrencyHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;
import main.java.me.avankziar.aep.spigot.object.TaxationCase;
import main.java.me.avankziar.aep.spigot.object.TaxationSet;
import main.java.me.avankziar.aep.spigot.object.ne_w.AEPUser;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.spigot.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.spigot.economy.action.OrdererType;

public class BackgroundTask
{
	private static AdvancedEconomyPlus plugin;
	private static String dbm1 = "backgroundtaskGeneral";
	private static String dbm2 = "backgroundtaskLoanRepayment";
	private static String dbm3 = "backgroundtaskStandingOrder";
	
	public BackgroundTask(AdvancedEconomyPlus plugin)
	{
		BackgroundTask.plugin = plugin;
		initBackgroundTask();
	}
	
	public boolean initBackgroundTask()
	{
		if(ConfigHandler.isLoanEnabled())
		{
			ConfigHandler.debug(dbm1, "Loan Task Timer activate...");
			runDebtRepayment();
		}
		if(ConfigHandler.isStandingOrderEnabled() && ConfigHandler.doStandingOrderPaymentTask())
		{
			ConfigHandler.debug(dbm1, "StandingOrder Task Timer activate...");
			runStandingOrderPayment();
		}
		int deletedays = plugin.getYamlHandler().getConfig().getInt("Do.DeleteAccountsDaysAfterOverdue", 30);
		if(deletedays > 0)
		{
			runPlayerDataDelete(deletedays);
		}
		return true;
	}
	
	public void runPlayerDataDelete(int deletedays)
	{
		int days = plugin.getYamlHandler().getConfig().getInt("Do.OverdueTimeInDays", 90);
		long deletedate = System.currentTimeMillis()-((long) days+(long) deletedays)*1000*60*60*24;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<AEPUser> user = new ArrayList<>();
				try
				{
					user = ConvertHandler.convertListI(plugin.getMysqlHandler().getAllListAt(Type.PLAYERDATA, "`id` ASC", "`unixtime` < ?", deletedate));
				} catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
				int uCount = 0;
				int acCount = 0;
				int acmCount = 0;
				int acdCount = 0;
				int acqCount = 0;
				for(AEPUser u : user)
				{
					acCount += plugin.getMysqlHandler().getCount(Type.ACCOUNT, "`owner_uuid` = ?", u.getUUID().toString());
					ArrayList<Account> acs = new ArrayList<>();
					try
					{
						acs = ConvertHandler.convertListII(
								plugin.getMysqlHandler().getAllListAt(Type.ACCOUNT, "`id` = ?", "`owner_uuid` = ?", u.getUUID().toString()));
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					for(Account ac : acs)
					{
						acmCount += plugin.getMysqlHandler().getCount(Type.ACCOUNTMANAGEMENT, "`account_id` = ?", ac.getID());
						plugin.getMysqlHandler().deleteData(Type.ACCOUNTMANAGEMENT, "`account_id` = ?", ac.getID());
						acdCount += plugin.getMysqlHandler().getCount(Type.DEFAULTACCOUNT, "`account_id` = ?", ac.getID());
						plugin.getMysqlHandler().deleteData(Type.DEFAULTACCOUNT, "`account_id` = ?", ac.getID());
						acqCount += plugin.getMysqlHandler().getCount(Type.QUICKPAYACCOUNT, "`account_id` = ?", ac.getID());
						plugin.getMysqlHandler().deleteData(Type.QUICKPAYACCOUNT, "`account_id` = ?", ac.getID());
					}
					plugin.getMysqlHandler().deleteData(Type.ACCOUNT, "`owner_uuid` = ?", u.getUUID().toString());
				}
				uCount += plugin.getMysqlHandler().getCount(Type.PLAYERDATA, "`unixtime` < ?", deletedate);
				plugin.getMysqlHandler().deleteData(Type.PLAYERDATA, "`unixtime` < ?", deletedate);
				AdvancedEconomyPlus.log.info("==========AEP Database DeleteTask==========");
				AdvancedEconomyPlus.log.info("Deleted User: "+uCount);
				AdvancedEconomyPlus.log.info("Deleted Account: "+acCount);
				AdvancedEconomyPlus.log.info("Deleted AccountManagement: "+acmCount);
				AdvancedEconomyPlus.log.info("Deleted DefaultAccount: "+acdCount);
				AdvancedEconomyPlus.log.info("Deleted QuickPayAccount: "+acqCount);
				AdvancedEconomyPlus.log.info("===========================================");
			}
		}.runTaskAsynchronously(plugin);
		
	}
	
	public void runDebtRepayment()
	{
		int repeat = plugin.getYamlHandler().getConfig().getInt("Loan.RepaymentRepeatTime", 60);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ConfigHandler.debug(dbm2, "Loan Repayment loop start");
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
				for(LoanRepayment lr : list)
				{
					long now = System.currentTimeMillis();
					if(now > lr.getStartTime())
					{
						continue;
					}
					long sum = lr.getLastTime()+lr.getRepeatingTime();
					if(sum>=now)
					{
						continue;
					}
					Account from = plugin.getIFHApi().getAccount(lr.getAccountFromID());
					Account to = plugin.getIFHApi().getAccount(lr.getAccountToID());
					if(from == null || to == null)
					{
						ConfigHandler.debug(dbm2, ">> CATCH : Account(s) == null");
						continue;
					}
					String category = plugin.getYamlHandler().getLang().getString("LoanRepayment.CategoryII", null);
					String comment = plugin.getYamlHandler().getLang().getString("LoanRepayment.CommentII", null);
					if(comment != null)
					{
						comment = comment.replace("%name%", lr.getName())
								.replace("%totalpaid%", plugin.getIFHApi().format(
										lr.getAmountPaidSoFar()+lr.getLoanAmount(), from.getCurrency()))
								.replace("%waitingamount%", plugin.getIFHApi().format(
										lr.getTotalAmount()-lr.getAmountPaidSoFar()-lr.getLoanAmount(), from.getCurrency()));
					}
					double amount = lr.getAmountRatio();
					Account tax = plugin.getIFHApi().getDefaultAccount(to.getOwner().getUUID(), AccountCategory.TAX, to.getCurrency());
					double taxation = lr.getTaxInDecimal();
					boolean taxAreExclusive = (lr.getLoanAmount()+lr.getLoanAmount()*lr.getInterest()+lr.getLoanAmount()*taxation) > lr.getTotalAmount() ? true : false;
					
					EconomyAction ea = null;
					if(from.getCurrency().getUniqueName().equals(to.getCurrency().getUniqueName()))
					{
						if(tax == null && category != null)
						{
							ea = plugin.getIFHApi().transaction(
									from, to, amount,
									OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
						} else if(tax != null && category != null)
						{
							ea = plugin.getIFHApi().transaction(
									from, to, amount,
									taxation, taxAreExclusive, tax, 
									OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
						}
						if(!ea.isSuccess())
						{
							ConfigHandler.debug(dbm2, ">> CONTINUE : Transaction WITHDRAW failed : "+ea.getDefaultErrorMessage());
							continue;
						}
						plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, lr, "`id` = ?", lr.getId());
						ConfigHandler.debug(dbm2, ">> Transaction success");
					} else
					{
						if(!from.getCurrency().isExchangeable() || !to.getCurrency().isExchangeable())
						{
							continue;
						}
						Account taxII = plugin.getIFHApi().getDefaultAccount(to.getOwner().getUUID(), AccountCategory.TAX, to.getCurrency());
						if(tax == null && taxII == null && category != null)
						{
							ea = plugin.getIFHApi().exchangeCurrencies(
									from, to, amount,
									OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
						} else if(tax != null && taxII == null && category != null)
						{
							ea = plugin.getIFHApi().exchangeCurrencies(
									from, to, amount,
									taxation, taxAreExclusive, tax, taxII,
									OrdererType.PLAYER, lr.getDebtor().toString(), category, comment);
						}
						plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, lr, "`id` = ?", lr.getId());
						ConfigHandler.debug(dbm2, ">> CurrencyExchange success");
					}
					
					lr.setLastTime(now);
					lr.setAmountPaidSoFar(lr.getAmountPaidSoFar()+lr.getAmountRatio()-lr.getTaxInDecimal()*lr.getAmountRatio());
					lr.setAmountPaidToTax(lr.getAmountPaidToTax()+lr.getTaxInDecimal()*lr.getAmountRatio());
					
					if(lr.getTotalAmount() <= lr.getAmountPaidSoFar()+lr.getAmountPaidToTax())
					{
						lr.setFinished(true);
					}
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.LOAN, lr, "`id` = ?", lr.getId());
					ConfigHandler.debug(dbm2, ">> FOR-LOOP END | START AT BEGIN");
				}
				ConfigHandler.debug(dbm2, "> LOOP-END");
			}
		}.runTaskTimer(plugin, 10L, 20L*repeat);;
	}
	
	public void runStandingOrderPayment()
	{
		int repeat = plugin.getYamlHandler().getConfig().getInt("StandingOrder.RepeatTime", 60);
		ConfigHandler.debug(dbm3, "INIT METHODE : RunStandingOrderPayment | Repeattime [s] = "+repeat);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ConfigHandler.debug(dbm3, "> START RUN : StandingOrderPayment");
				ArrayList<StandingOrder> list = new ArrayList<>();
				try
				{
					list = ConvertHandler.convertListV(
							plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.STANDINGORDER, "`id` ASC",
									"`cancelled` = ? AND `paused` = ?", false, false));
					ConfigHandler.debug(dbm3, "> TRY : GET : StandingOrder List : Amount : "+list.size());
				} catch (IOException e)
				{
					ConfigHandler.debug(dbm3, "> CATCH : StandingOrderPayment IOException: "+e.toString());
					return;
				}
				ConfigHandler.debug(dbm3, "> FOR-LOOP : StandingOrderPayment");
				for(StandingOrder so : list)
				{
					ConfigHandler.debug(dbm3, ">> NULLCHECK : StandingOrder == null == "+(so == null));
					if(so == null)
					{
						continue;
					}
					long now = System.currentTimeMillis();
					long sum = so.getLastTime()+so.getRepeatingTime();
					if(sum >= now)
					{
						ConfigHandler.debug(dbm3, ">> CONTINUE : LastTime + RepeatingTime >= System.currentTimeMillis");
						continue;
					}
					ConfigHandler.debug(dbm3, ">> TRY : GET Accounts");
					Account from = plugin.getIFHApi().getAccount(so.getAccountFrom());
					Account to = plugin.getIFHApi().getAccount(so.getAccountTo());
					if(from == null || to == null)
					{
						ConfigHandler.debug(dbm3, ">> CATCH : Account(s) == null");
						continue;
					}
					double amount = so.getAmount();
					Account tax = plugin.getIFHApi().getDefaultAccount(from.getOwner().getUUID(), AccountCategory.TAX, from.getCurrency());
					String category = plugin.getYamlHandler().getLang().getString("StandingOrder.Category");
					String comment = plugin.getYamlHandler().getLang().getString("StandingOrder.Comment")
							.replace("%name%", so.getName())
							.replace("%format%", plugin.getIFHApi().format(so.getAmountPaidSoFar(), from.getCurrency()));
					LinkedHashMap<TaxationCase, TaxationSet> map = CurrencyHandler.taxationMap.get(from.getCurrency().getUniqueName());
					TaxationSet ts = map.containsKey(TaxationCase.STANDINGORDER) ? map.get(TaxationCase.STANDINGORDER) : null;
					double taxation = ts != null ? ts.getTaxInPercent() : 0.0;
					boolean taxAreExclusive = ts != null ? ts.isTaxAreExclusive() : true;
					EconomyAction ea = null;
					if(from.getCurrency().getUniqueName().equals(to.getCurrency().getUniqueName()))
					{
						if(tax == null && category != null)
						{
							ea = plugin.getIFHApi().transaction(
									from, to, amount,
									OrdererType.PLAYER, so.getOwner().toString(), category, comment);
						} else if(tax != null && category != null)
						{
							ea = plugin.getIFHApi().transaction(
									from, to, amount,
									taxation, taxAreExclusive, tax, 
									OrdererType.PLAYER, so.getOwner().toString(), category, comment);
						}
						if(!ea.isSuccess())
						{
							so.setCancelled(true);
							plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getID());
							ConfigHandler.debug(dbm3, ">> CONTINUE : Transaction WITHDRAW failed : "+ea.getDefaultErrorMessage());
							continue;
						}
						ConfigHandler.debug(dbm3, ">> Transaction success");
					} else
					{
						if(!from.getCurrency().isExchangeable() || !to.getCurrency().isExchangeable())
						{
							continue;
						}
						Account taxII = plugin.getIFHApi().getDefaultAccount(to.getOwner().getUUID(), AccountCategory.TAX, to.getCurrency());
						if(tax == null && taxII == null && category != null)
						{
							ea = plugin.getIFHApi().exchangeCurrencies(
									from, to, amount,
									OrdererType.PLAYER, so.getOwner().toString(), category, comment);
						} else if(tax != null && taxII == null && category != null)
						{
							ea = plugin.getIFHApi().exchangeCurrencies(
									from, to, amount,
									taxation, taxAreExclusive, tax, taxII,
									OrdererType.PLAYER, so.getOwner().toString(), category, comment);
						}
						ConfigHandler.debug(dbm3, ">> CurrencyExchange success");
					}
					so.setLastTime(now);
					double totalamount = so.getAmountPaidSoFar() + ea.getWithDrawAmount();
					so.setAmountPaidSoFar(totalamount);
					double taxes = so.getAmountPaidToTax() + ea.getTaxAmount();
					so.setAmountPaidToTax(taxes);
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.STANDINGORDER, so, "`id` = ?", so.getID());
					ConfigHandler.debug(dbm3, ">> FOR-LOOP END | START AT BEGIN");
				}
				ConfigHandler.debug(dbm3, "> LOOP-END");
			}
		}.runTaskTimerAsynchronously(plugin, 5L, 20L*repeat);
	}
}
