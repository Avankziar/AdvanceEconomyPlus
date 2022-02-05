package main.java.me.avankziar.aep.spigot.api.economy;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.cst.ActionLog;
import main.java.me.avankziar.aep.spigot.cmd.cst.Balance;
import main.java.me.avankziar.aep.spigot.cmd.cst.BankNotification;
import main.java.me.avankziar.aep.spigot.cmd.cst.TrendLog;
import main.java.me.avankziar.aep.spigot.cmd.cst.WalletNotification;
import main.java.me.avankziar.aep.spigot.cmd.cst.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.cst.sub.TabCompletion;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.Give;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.GiveConsole;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.Pay;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.Set;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.SetConsole;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.Take;
import main.java.me.avankziar.aep.spigot.cmd.cst.transaction.TakeConsole;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandConstructor;
import main.java.me.avankziar.aep.spigot.object.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.object.CommandStructurType;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class CurrencyCommandSetup
{
	private AdvancedEconomyPlus plugin;
	
	public CurrencyCommandSetup(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	public void setupCommandCurrency()
	{
		for(EconomyCurrency ec : plugin.getIFHApi().currencyHandler.digitalCurrencies)
		{
			YamlConfiguration y = plugin.getYamlHandler().getCurrency(ec.getUniqueName());
			CommandStructurType cst;
			try
			{
				cst = CommandStructurType.valueOf(y.getString("Commands.StructurType"));
			} catch(Exception e)
			{
				continue;
			}
			CommandConstructor cmd = new CommandConstructor(plugin, "balance", false);
			plugin.registerCommand(cmd.getPath(), cmd.getName());
			plugin.getCommand(cmd.getName()).setExecutor(new Balance(plugin, cmd, cst));
			plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, CommandExecuteType.BALANCE, cst));
			CommandSuggest.set(CommandExecuteType.BALANCE, cmd.getCommandString());
			if(cst == CommandStructurType.SINGLE)
			{
				setupSingle(ec, y);
			} else
			{
				setupNested(ec, y);
			}
			/*CommandConstructor money = new CommandConstructor(plugin, baseCommandII, false);
			
			LoggerSettingsHandler.loggerSettingsCommandString = loggersettings_gui.getCommandString();
			LoggerSettingsHandler.loggerSettingsTextCommandString = loggersettings_text.getCommandString();
			
			registerCommand(money.getPath(), money.getName());
			getCommand(money.getName()).setExecutor(new MoneyCommandExecutor(plugin, money));
			getCommand(money.getName()).setTabCompleter(new TABCompletion(plugin));
			
			addingHelps(
					money,
						actionlog, trendlog,
						loggersettings, loggersettings_gui, loggersettings_other, loggersettings_text,
						freeze, give, giveconsole, pay, set, setconsole, take, takeconsole, toggle, top);*/
		}
	}
	
	public void setupSingle(EconomyCurrency ec, YamlConfiguration y)
	{
		if(y.get("Commands.SINGLE") == null)
		{
			return;
		}
		CommandStructurType cst = CommandStructurType.SINGLE;
		for(String s : y.getStringList("Commands.SINGLE"))
		{
			String[] sp = s.split(";");
			if(sp.length != 2)
			{
				return;
			}
			CommandExecuteType ces;
			String cmdpath;
			try
			{
				ces = CommandExecuteType.valueOf(sp[0]);
				cmdpath = sp[1];
			} catch(Exception e)
			{
				return;
			}
			CommandConstructor cmd = new CommandConstructor(plugin, cmdpath, false);
			plugin.registerCommand(cmd.getPath(), cmd.getName());
			switch(ces)
			{
			case BALANCE:
				break;
			case ACTIONLOG:
				plugin.getCommand(cmd.getName()).setExecutor(new ActionLog(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case TRENDLOG:
				plugin.getCommand(cmd.getName()).setExecutor(new TrendLog(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case PAY:
				plugin.getCommand(cmd.getName()).setExecutor(new Pay(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case GIVE:
				plugin.getCommand(cmd.getName()).setExecutor(new Give(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case GIVE_CONSOLE:
				plugin.getCommand(cmd.getName()).setExecutor(new GiveConsole(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case TAKE:
				plugin.getCommand(cmd.getName()).setExecutor(new Take(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case TAKE_CONSOLE:
				plugin.getCommand(cmd.getName()).setExecutor(new TakeConsole(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case SET:
				plugin.getCommand(cmd.getName()).setExecutor(new Set(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case SET_CONSOLE:
				plugin.getCommand(cmd.getName()).setExecutor(new SetConsole(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case WALLETNOTIFICATION:
				plugin.getCommand(cmd.getName()).setExecutor(new WalletNotification(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case BANKNOTIFICATION:
				plugin.getCommand(cmd.getName()).setExecutor(new BankNotification(plugin, cmd, null, cst));
				plugin.getCommand(cmd.getName()).setTabCompleter(new TabCompletion(plugin, ces, cst));
				CommandSuggest.set(ces, cmd.getCommandString());
				break;
			case ACCOUNT_CLOSE:
			case ACCOUNT_INFO:
			case ACCOUNT_MANAGE:
			case ACCOUNT_OPEN:
			case ACCOUNT_SETCATEGORY:
			case ACCOUNT_SETDEFAULT:
			case ACCOUNT_SETNAME:
			case ACCOUNT_SETOWNER:
			case ACCOUNT_SETSHORTPAY:
			case CURRENCY_INFO:
			case CURRENCY_EXCHANGE:
			case LOGGERSETTINGS:
			case LOGGERSETTINGS_GUI:
			case LOGGERSETTINGS_OTHER:
			case LOGGERSETTINGS_TEXT:
			case TOPRANKING:
			case TOPLIST:
			}
		}
	}
	
	public void setupNested(EconomyCurrency ec, YamlConfiguration y)
	{
		if(y.get("Commands.NESTED") == null)
		{
			return;
		}
		CommandStructurType cst = CommandStructurType.NESTED;
		for(String s : y.getStringList("Commands.NESTED"))
		{
			String[] sp = s.split(";");
			if(sp.length != 2)
			{
				return;
			}
			CommandExecuteType ces;
			String cmdpath;
			try
			{
				ces = CommandExecuteType.valueOf(sp[0]);
				cmdpath = sp[1];
			} catch(Exception e)
			{
				return;
			}
			CommandConstructor cmd = new CommandConstructor(plugin, cmdpath, false);
			plugin.registerCommand(cmd.getPath(), cmd.getName());
			switch(ces)
			{
			
			}
		}
	}
}
