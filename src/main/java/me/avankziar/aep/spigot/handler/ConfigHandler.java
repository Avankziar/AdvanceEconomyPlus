package main.java.me.avankziar.aep.spigot.handler;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;

public class ConfigHandler
{
	private static AdvancedEconomyPlus plugin;
	
	public static void init(AdvancedEconomyPlus plugin)
	{
		ConfigHandler.plugin = plugin;
	}
	
	public static long getStandingOrderSpamProtection()
	{
		return TimeHandler.getRepeatingTime(plugin.getConfig().getString("StandingOrderTimeSpamProtection", "00-00:15"));
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
	
	//ADDME see AEPSettings
}
