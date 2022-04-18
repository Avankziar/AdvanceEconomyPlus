package main.java.me.avankziar.aep.spigot.handler;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.database.YamlHandler;

public class ConfigHandler
{
	private static AdvancedEconomyPlus plugin;
	private static YamlHandler yh;
	
	public static void init(AdvancedEconomyPlus plugin)
	{
		ConfigHandler.plugin = plugin;
		yh = plugin.getYamlHandler();
	}
	
	public static boolean isDebugEnabled(String debugmode)
	{
		if(!yh.getConfig().getBoolean("Enable.DebugMode"))
		{
			return false;
		}
		for(String s : yh.getConfig().getStringList("Do.DebugMode"))
		{
			if(s.equals(debugmode))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void debug(String debugmode, String s)
	{
		if(isDebugEnabled(debugmode))
		{
			plugin.getLogger().info(s);
		}
	}
	
	public static boolean isStandingOrderEnabled()
	{
		return yh.getConfig().getBoolean("EnableCommands.StandingOrder", false);
	}
	
	public static boolean isStandingOrderEnabled(String currencyuniquename)
	{
		if(yh.getConfig().get("Enable.CurrencyMayUseStandingOrder") == null)
		{
			return false;
		}
		for(String s : yh.getConfig().getStringList("Enable.CurrencyMayUseStandingOrder"))
		{
			if(s.equals(currencyuniquename))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean doStandingOrderPaymentTask()
	{
		return yh.getConfig().getBoolean("StandingOrder.DoPaymentTask", false);
	}
	
	public static boolean isLoanEnabled()
	{
		return yh.getConfig().getBoolean("EnableCommands.Loan", false);
	}
	
	public static boolean isLoanEnabled(String currencyuniquename)
	{
		if(yh.getConfig().get("Enable.CurrencyMayUseLoanRepayment") == null)
		{
			return false;
		}
		for(String s : yh.getConfig().getStringList("Enable.CurrencyMayUseLoanRepayment"))
		{
			if(s.equals(currencyuniquename))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isLoanRetakeEnabled()
	{
		return yh.getConfig().getBoolean("Loan.RetakePayment", false);
	}
	
	public static double getStandingOrderValueProtection(String currencyuniquename)
	{
		double d = 0.0;
		for(String s : yh.getConfig().getStringList("StandingOrder.ValueSpamProtection"))
		{
			if(!s.contains(";"))
			{
				continue;
			}
			String[] sp = s.split(";");
			if(sp.length != 2)
			{
				continue;
			}
			if(sp[0].equals(currencyuniquename))
			{
				if(MatchApi.isDouble(sp[1]))
				{
					d = Double.parseDouble(sp[1]);
					break;
				}
			}
		}
		return d;
	}
	
	public static long getStandingOrderSpamProtection(String currencyuniquename)
	{
		long l = 0;
		for(String s : yh.getConfig().getStringList("StandingOrder.TimeSpamProtection"))
		{
			if(!s.contains(";"))
			{
				continue;
			}
			String[] sp = s.split(";");
			if(sp.length != 2)
			{
				continue;
			}
			if(sp[0].equals(currencyuniquename))
			{
				l = TimeHandler.getRepeatingTime(sp[1]);
				break;
			}
		}
		return l;
	}
	
	public static boolean getDefaultMoneyFlowNotification(boolean wallet)
	{
		if(wallet)
		{
			return plugin.getYamlHandler().getConfig().getBoolean("Do.Default.WalletMoneyFlowNotification");
		} else
		{
			return plugin.getYamlHandler().getConfig().getBoolean("Do.Default.BankMoneyFlowNotification");
		}
	}
	
	public enum CountType
	{
		HIGHEST, ADDUP;
	}
	
	public CountType getCountPermType()
	{
		String s = plugin.getYamlHandler().getConfig().getString("Do.OpenAccount.CountPerm", "HIGHEST");
		CountType ct;
		try
		{
			ct = CountType.valueOf(s);
		} catch (Exception e)
		{
			ct = CountType.HIGHEST;
		}
		return ct;
	}
}
