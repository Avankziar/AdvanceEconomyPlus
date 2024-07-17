package me.avankziar.aep.spigot.assistance;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.general.objects.StandingOrder;
import me.avankziar.aep.general.objects.TaxationCase;
import me.avankziar.aep.general.objects.TaxationSet;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.api.economy.CurrencyHandler;
import me.avankziar.aep.spigot.cmd.cst.transaction.Transfer;
import me.avankziar.aep.spigot.handler.ConfigHandler;
import me.avankziar.aep.spigot.handler.ConvertHandler;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class BackgroundTask
{
	private static AEP plugin;
	private static String dbm1 = "backgroundtaskGeneral";
	private static String dbm2 = "backgroundtaskLoanRepayment";
	private static String dbm3 = "backgroundtaskStandingOrder";
	
	public BackgroundTask(AEP plugin)
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
			runStandingOrderCleanUp();
		}
		int deletedays = plugin.getYamlHandler().getConfig().getInt("Do.DeleteAccountsDaysAfterOverdue", 30);
		if(deletedays > 0)
		{
			runPlayerDataDelete(deletedays);
		}
		runAccountManagementFee();
		runAccountInterest();
		runLogCleanTask();
		return true;
	}
	
	public void runLogCleanTask()
	{
		long deletedays = plugin.getYamlHandler().getConfig().getLong("Do.DeleteLogsAfterDays", 365L);
		if(deletedays < 0)
		{
			return;
		}
		long time = System.currentTimeMillis()-(deletedays*1000L*60*60*24);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				final int acount = plugin.getMysqlHandler().getCount(MysqlType.ACTION, "`unixtime` < ?", time);
				final int tcount = plugin.getMysqlHandler().getCount(MysqlType.TREND, "`dates` < ?", time);
				plugin.getMysqlHandler().deleteData(MysqlType.ACTION, "`unixtime` < ?", time);
				plugin.getMysqlHandler().deleteData(MysqlType.TREND, "`dates` < ?", time);
				AEP.logger.info("==========AEP Database DeleteTask==========");
				AEP.logger.info("Deleted Actionlog: "+acount);
				AEP.logger.info("Deleted Trendlog: "+tcount);
				AEP.logger.info("===========================================");
			}
		}.runTaskAsynchronously(plugin);
	}
	
	//Better reading quality than compact code
	public void runAccountManagementFee()
	{
		String l = plugin.getYamlHandler().getConfig().getString("Do.Bankaccount.TimeToWithdrawAccountManagementFees", "SUNDAY-11:00");
		TimeFormater tf = new TimeFormater(l);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				LocalDateTime lt = LocalDateTime.now();
				if(lt.getDayOfWeek().getValue() != tf.dayOfWeek.day 
						|| lt.getHour() != tf.localTime.getHour() || lt.getMinute() != tf.localTime.getMinute())
				{
					return;
				}
				String categoryI = plugin.getYamlHandler().getLang().getString("Bankaccount.AccountManagementFees.CategoryI");
				String commentI = plugin.getYamlHandler().getLang().getString("Bankaccount.AccountManagementFees.CommentI");
				for(String eca : plugin.getYamlHandler().getConfig().getStringList("Do.Bankaccount.AccountManagementFeesAsLumpSum"))
				{
					String[] sp = eca.split(";");
					if(sp.length != 3)
					{
						continue;
					}
					if(!MatchApi.isDouble(sp[1]))
					{
						continue;
					}	
					EconomyCurrency ec = plugin.getIFHApi().getCurrency(sp[0]);
					if(ec == null)
					{
						continue;
					}
					double amount = Double.parseDouble(sp[1]);
					if(amount < 0.0)
					{
						continue;
					}
					AccountCategory acc;
					try
					{
						acc = AccountCategory.valueOf(sp[2]);
					} catch(Exception e)
					{
						continue;
					}
					new BukkitRunnable()
					{
						int start = 0;
						int quantity = 10;
						@Override
						public void run()
						{
							ArrayList<Account> aclist = ConvertHandler.convertListII(
									plugin.getMysqlHandler().getList(
											MysqlType.ACCOUNT_SPIGOT, "`id` ASC", start, quantity, 
											"`account_type` = ? AND `account_category` = ? AND `account_currency` = ?",
											AccountType.BANK.toString(), acc.toString(), ec.getUniqueName()));
							if(aclist.isEmpty())
							{
								cancel();
								return;
							}
							for(Account ac : aclist)
							{
								if(ac == null || ac.getCurrency() == null)
								{
									continue;
								}
								Account tax = plugin.getIFHApi().getDefaultAccount(ac.getOwner().getUUID(), AccountCategory.TAX, ec);
								if(tax == null)
								{
									plugin.getIFHApi().withdraw(
											ac, amount,
											OrdererType.PLAYER, ac.getOwner().getUUID().toString(), categoryI, commentI);
								} else if(tax != null)
								{
									plugin.getIFHApi().transaction(
											ac, tax, amount,
											OrdererType.PLAYER, ac.getOwner().getUUID().toString(), categoryI, commentI);
								}
							}
							start += 10;
						}
					}.runTaskTimerAsynchronously(plugin, 20L, 20L*2);
				}
				String categoryII = plugin.getYamlHandler().getLang().getString("Bankaccount.AccountManagementFees.CategoryII");
				String commentII = plugin.getYamlHandler().getLang().getString("Bankaccount.AccountManagementFees.CommentII");
				for(String eca : plugin.getYamlHandler().getConfig().getStringList("Do.Bankaccount.AccountManagementFeesAsPercent"))
				{
					String[] sp = eca.split(";");
					if(sp.length != 3)
					{
						continue;
					}
					if(!MatchApi.isDouble(sp[1]))
					{
						continue;
					}	
					EconomyCurrency ec = plugin.getIFHApi().getCurrency(sp[0]);
					if(ec == null)
					{
						continue;
					}
					double amount = Double.parseDouble(sp[1]);
					if(amount < 0.0 || amount > 100.0)
					{
						continue;
					}
					AccountCategory acc;
					try
					{
						acc = AccountCategory.valueOf(sp[2]);
					} catch(Exception e)
					{
						continue;
					}
					new BukkitRunnable()
					{
						int start = 0;
						int quantity = 10;
						@Override
						public void run()
						{
							ArrayList<Account> aclist = ConvertHandler.convertListII(
									plugin.getMysqlHandler().getList(
											MysqlType.ACCOUNT_SPIGOT, "`id` ASC", start, quantity, 
											"`account_type` = ? AND `account_category` = ? AND `account_currency` = ?",
											AccountType.BANK.toString(), acc.toString(), ec.getUniqueName()));
							if(aclist.isEmpty())
							{
								cancel();
								return;
							}
							for(Account ac : aclist)
							{
								if(ac == null || ac.getCurrency() == null)
								{
									continue;
								}
								Account tax = plugin.getIFHApi().getDefaultAccount(ac.getOwner().getUUID(), AccountCategory.TAX, ec);
								if(tax == null)
								{
									plugin.getIFHApi().withdraw(
											ac, ac.getBalance()*amount/100,
											OrdererType.PLAYER, ac.getOwner().getUUID().toString(), categoryII, commentII);
								} else if(tax != null)
								{
									plugin.getIFHApi().transaction(
											ac, tax, ac.getBalance()*amount/100,
											OrdererType.PLAYER, ac.getOwner().getUUID().toString(), categoryII, commentII);
								}
							}
							start += 10;
						}
					}.runTaskTimerAsynchronously(plugin, 20L, 20L*2);
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 20L*60);
	}
	
	public void runAccountInterest()
	{
		String l = plugin.getYamlHandler().getConfig().getString("Do.Bankaccount.TimeToDepositInterest", "SUNDAY-11:00");
		TimeFormater tf = new TimeFormater(l);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				LocalDateTime lt = LocalDateTime.now();
				if(lt.getDayOfWeek().getValue() != tf.dayOfWeek.day 
						|| lt.getHour() != tf.localTime.getHour() || lt.getMinute() != tf.localTime.getMinute())
				{
					return;
				}
				String categoryI = plugin.getYamlHandler().getLang().getString("Bankaccount.AccountManagementFees.CategoryI");
				String commentI = plugin.getYamlHandler().getLang().getString("Bankaccount.AccountManagementFees.CommentI");
				for(String eca : plugin.getYamlHandler().getConfig().getStringList("Do.Bankaccount.InterestAsPercent"))
				{
					String[] sp = eca.split(";");
					if(sp.length != 3)
					{
						continue;
					}
					if(!MatchApi.isDouble(sp[1]))
					{
						continue;
					}	
					EconomyCurrency ec = plugin.getIFHApi().getCurrency(sp[0]);
					if(ec == null)
					{
						continue;
					}
					double amount = Double.parseDouble(sp[1]);
					if(amount < 0.0)
					{
						continue;
					}
					AccountCategory acc;
					try
					{
						acc = AccountCategory.valueOf(sp[2]);
					} catch(Exception e)
					{
						continue;
					}
					new BukkitRunnable()
					{
						int start = 0;
						int quantity = 10;
						@Override
						public void run()
						{
							ArrayList<Account> aclist = ConvertHandler.convertListII(
									plugin.getMysqlHandler().getList(
											MysqlType.ACCOUNT_SPIGOT, "`id` ASC", start, quantity, 
											"`account_type` = ? AND `account_category` = ? AND `account_currency` = ?",
											AccountType.BANK.toString(), acc.toString(), ec.getUniqueName()));
							if(aclist.isEmpty())
							{
								cancel();
								return;
							}
							for(Account ac : aclist)
							{
								if(ac == null || ac.getCurrency() == null)
								{
									continue;
								}
								plugin.getIFHApi().deposit(
										ac, ac.getBalance()*amount/100,
										OrdererType.PLAYER, ac.getOwner().getUUID().toString(), categoryI, commentI);
							}
							start += 10;
						}
					}.runTaskTimerAsynchronously(plugin, 20L, 20L*2);
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 20L*60);
	}
	
	public void runPlayerDataDelete(int deletedays)
	{
		int days = plugin.getYamlHandler().getConfig().getInt("Do.OverdueTimeInDays", 180);
		long deletedate = System.currentTimeMillis()-((long) days+(long) deletedays)*1000L*60*60*24;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<AEPUser> user = ConvertHandler.convertListI(plugin.getMysqlHandler().getFullList(MysqlType.PLAYERDATA, "`id` ASC", "`unixtime` < ?", deletedate));
				if(user.isEmpty())
				{
					return;
				}
				int uCount = 0;
				int acCount = 0;
				int acmCount = 0;
				int money = 0;
				int acdCount = 0;
				int acqCount = 0;
				for(AEPUser u : user)
				{
					acCount += plugin.getMysqlHandler().getCount(MysqlType.ACCOUNT_SPIGOT, "`owner_uuid` = ?", u.getUUID().toString());
					ArrayList<Account> acs = ConvertHandler.convertListII(
							plugin.getMysqlHandler().getFullList(MysqlType.ACCOUNT_SPIGOT, "`id`", "`owner_uuid` = ?", u.getUUID().toString()));
					for(Account ac : acs)
					{
						money += ac.getBalance();
						acmCount += plugin.getMysqlHandler().getCount(MysqlType.ACCOUNTMANAGEMENT, "`account_id` = ?", ac.getID());
						plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNTMANAGEMENT, "`account_id` = ?", ac.getID());
						acdCount += plugin.getMysqlHandler().getCount(MysqlType.DEFAULTACCOUNT, "`account_id` = ?", ac.getID());
						plugin.getMysqlHandler().deleteData(MysqlType.DEFAULTACCOUNT, "`account_id` = ?", ac.getID());
						acqCount += plugin.getMysqlHandler().getCount(MysqlType.QUICKPAYACCOUNT, "`account_id` = ?", ac.getID());
						plugin.getMysqlHandler().deleteData(MysqlType.QUICKPAYACCOUNT, "`account_id` = ?", ac.getID());
					}
					plugin.getMysqlHandler().deleteData(MysqlType.ACCOUNT_SPIGOT, "`owner_uuid` = ?", u.getUUID().toString());
				}
				uCount += plugin.getMysqlHandler().getCount(MysqlType.PLAYERDATA, "`unixtime` < ?", deletedate);
				plugin.getMysqlHandler().deleteData(MysqlType.PLAYERDATA, "`unixtime` < ?", deletedate);
				AEP.logger.info("==========AEP Database DeleteTask==========");
				AEP.logger.info("Deleted User: "+uCount);
				AEP.logger.info("Deleted Account: "+acCount);
				AEP.logger.info("Deleted Money: "+money);
				AEP.logger.info("Deleted AccountManagement: "+acmCount);
				AEP.logger.info("Deleted DefaultAccount: "+acdCount);
				AEP.logger.info("Deleted QuickPayAccount: "+acqCount);
				AEP.logger.info("===========================================");
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
				ArrayList<LoanRepayment> list = ConvertHandler.convertListVI(plugin.getMysqlHandler().getFullList(MysqlType.LOAN, "`id` ASC",
						"`forgiven` = ? AND `paused` = ? AND `finished` = ? AND `end_time` > ?",
						false, false, false, System.currentTimeMillis()+1000L*60*60*30));			
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
					if(from == null || to == null || from.getCurrency() == null || to.getCurrency() == null)
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
						plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", lr.getId());
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
						plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", lr.getId());
						ConfigHandler.debug(dbm2, ">> CurrencyExchange success");
					}
					
					lr.setLastTime(now);
					lr.setAmountPaidSoFar(lr.getAmountPaidSoFar()+lr.getAmountRatio()-lr.getTaxInDecimal()*lr.getAmountRatio());
					lr.setAmountPaidToTax(lr.getAmountPaidToTax()+lr.getTaxInDecimal()*lr.getAmountRatio());
					
					if(lr.getTotalAmount() <= lr.getAmountPaidSoFar()+lr.getAmountPaidToTax())
					{
						lr.setFinished(true);
					}
					plugin.getMysqlHandler().updateData(MysqlType.LOAN, lr, "`id` = ?", lr.getId());
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
				ArrayList<StandingOrder> solist = ConvertHandler.convertListV(
						plugin.getMysqlHandler().getFullList(MysqlType.STANDINGORDER, "`id` ASC",
								"`cancelled` = ? AND `paused` = ?", false, false));
				ConfigHandler.debug(dbm3, "> TRY : GET : StandingOrder List : Amount : "+solist.size());
				ConfigHandler.debug(dbm3, "> FOR-LOOP : StandingOrderPayment");
				for(StandingOrder so : solist)
				{
					ConfigHandler.debug(dbm3, ">> NULLCHECK : StandingOrder == null : "+(so == null));
					if(so == null)
					{
						continue;
					}
					long now = System.currentTimeMillis();
					if(so.getEndtime() > 0)
					{
						if(now > so.getEndtime())
						{
							so.setCancelled(true);
							plugin.getMysqlHandler().updateData(MysqlType.STANDINGORDER, so, "`id` = ?", so.getID());
							continue;
						}
					}
					long sum = so.getLastTime()+so.getRepeatingTime();
					if(sum >= now)
					{
						ConfigHandler.debug(dbm3, ">> CONTINUE : LastTime + RepeatingTime >= System.currentTimeMillis");
						continue;
					}
					ConfigHandler.debug(dbm3, ">> TRY : GET Accounts");
					Account from = plugin.getIFHApi().getAccount(so.getAccountFrom());
					Account to = plugin.getIFHApi().getAccount(so.getAccountTo());
					if(from == null || to == null || from.getCurrency() == null || to.getCurrency() == null)
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
					ArrayList<String> msg = new ArrayList<>();
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
									taxation/100, taxAreExclusive, tax, 
									OrdererType.PLAYER, so.getOwner().toString(), category, comment);
						}
						if(!ea.isSuccess())
						{
							so.setCancelled(true);
							plugin.getMysqlHandler().updateData(MysqlType.STANDINGORDER, so, "`id` = ?", so.getID());
							ConfigHandler.debug(dbm3, ">> CONTINUE : Transaction WITHDRAW failed : "+ea.getDefaultErrorMessage());
							continue;
						}
						ConfigHandler.debug(dbm3, ">> Transaction success");
						String wformat = plugin.getIFHApi().format(ea.getWithDrawAmount(), from.getCurrency());
						String dformat = plugin.getIFHApi().format(ea.getDepositAmount(), from.getCurrency());
						String tformat = plugin.getIFHApi().format(ea.getTaxAmount(), from.getCurrency());
						for(String s : plugin.getYamlHandler().getLang().getStringList("Cmd.StandingOrder.Transaction"))
						{
							String a = s.replace("%fromaccount%", from.getAccountName())
							.replace("%toaccount%", to.getAccountName())
							.replace("%formatwithdraw%", wformat)
							.replace("%formatdeposit%", dformat)
							.replace("%formattax%", tformat)
							.replace("%category%", category != null ? category : "/")
							.replace("%comment%", comment != null ? comment : "/");
							msg.add(a);
						}
						Transfer.sendToOther(plugin, from, to, msg);
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
									taxation/100, taxAreExclusive, tax, taxII,
									OrdererType.PLAYER, so.getOwner().toString(), category, comment);
						}
						ConfigHandler.debug(dbm3, ">> CurrencyExchange success");
					}
					so.setLastTime(now);
					double totalamount = so.getAmountPaidSoFar() + ea.getWithDrawAmount();
					so.setAmountPaidSoFar(totalamount);
					double taxes = so.getAmountPaidToTax() + ea.getTaxAmount();
					so.setAmountPaidToTax(taxes);
					plugin.getMysqlHandler().updateData(MysqlType.STANDINGORDER, so, "`id` = ?", so.getID());
					ConfigHandler.debug(dbm3, ">> FOR-LOOP END | START AT BEGIN");
				}
				ConfigHandler.debug(dbm3, "> LOOP-END");
			}
		}.runTaskTimerAsynchronously(plugin, 5L, 20L*repeat);
	}
	
	public void runStandingOrderCleanUp()
	{
		long deleteafter = System.currentTimeMillis()
				-plugin.getYamlHandler().getConfig().getLong("StandingOrder.DeleteAfterIsCancelledOrPausedInDays", 120L)*1000L*60*60*24;
		if(deleteafter < 0)
		{
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				final int count = plugin.getMysqlHandler().getCount(MysqlType.STANDINGORDER, 
						"(`cancelled` = ? OR `paused` = ?) AND `last_time` < ?", true, true, deleteafter);
				if(count <= 0)
				{
					return;
				}
				plugin.getMysqlHandler().deleteData(MysqlType.STANDINGORDER, 
						"(`cancelled` = ? OR `paused` = ?) AND `last_time` < ?", true, true, deleteafter);
				AEP.logger.info("==========AEP Database DeleteTask==========");
				AEP.logger.info("Deleted StandingOrder: "+count);
				AEP.logger.info("===========================================");				
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public class TimeFormater
	{
		public LocalTime localTime;
		public DayOfWeek dayOfWeek;
		
		public TimeFormater(String s)
		{
			String[] sp = s.split("-");
			localTime = LocalTime.parse(sp[1], DateTimeFormatter.ofPattern("HH:mm"));
			dayOfWeek = DayOfWeek.value(sp[0]);
		}
	}
	
	public enum DayOfWeek
	{
		MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);
		
		private int day;
		
		DayOfWeek(int i)
		{
			day = i;
		}

		public static DayOfWeek value(String s)
		{
			if(s.equalsIgnoreCase(MONDAY.toString()) || s.equalsIgnoreCase("1"))
			{
				return MONDAY;
			} else if(s.equalsIgnoreCase(TUESDAY.toString()) || s.equalsIgnoreCase("2"))
			{
				return TUESDAY;
			} else if(s.equalsIgnoreCase(WEDNESDAY.toString()) || s.equalsIgnoreCase("3"))
			{
				return WEDNESDAY;
			} else if(s.equalsIgnoreCase(THURSDAY.toString()) || s.equalsIgnoreCase("4"))
			{
				return THURSDAY;
			} else if(s.equalsIgnoreCase(FRIDAY.toString()) || s.equalsIgnoreCase("5"))
			{
				return FRIDAY;
			} else if(s.equalsIgnoreCase(SATURDAY.toString()) || s.equalsIgnoreCase("6"))
			{
				return SATURDAY;
			} else if(s.equalsIgnoreCase(SUNDAY.toString()) || s.equalsIgnoreCase("7"))
			{
				return SUNDAY;
			}
			return SUNDAY;
		}
	}
}