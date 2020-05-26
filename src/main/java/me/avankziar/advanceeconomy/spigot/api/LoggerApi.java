package main.java.me.avankziar.advanceeconomy.spigot.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomyLogger;
import main.java.me.avankziar.advanceeconomy.spigot.object.TrendLogger;

public class LoggerApi
{
	private static AdvanceEconomy plugin;
	
	public LoggerApi(AdvanceEconomy plugin)
	{
		LoggerApi.plugin = plugin;
	}
	
	/**
	 * Add a mysql Entry in the 3. mysql table from the economy plugin.
	 * @param  dateTime | The exact date and time
	 * @param  fromUUIDOrNumber | The playeruuid or the bankaccountnumber from the money came. If the money came from a plugin, write here "<i>System</i>".
	 * @param  fromName | The playername or bankaccountname from the money came. If the money came from a plugin, write here "<i>System</i>".
	 * @param  toUUIDOrNumber | the playeruuid or bankaccountnumber where to the money go.
	 * @param  toName | the playername or bankaccountname where to the money go.
	 * @param  ordererUUID | the playeruuid whitch has the money flow order. If the money cam from a plugin, write here "<i>PluginName</i>".
	 * @param  type | Which Type is the Logger
	 * @param  comment | The comment regarging the money flow.
	 *         The objects that replace the "?" from the whereColumn String
	 * @return void
	 * @author Christoph Steins/Avankziar
	 * @see EconomyLogger
	 */
	public static void addEconomyLogger(LocalDateTime dateTime,
			String fromUUIDOrNumber, String toUUIDOrNumber, String fromName, String toName, String ordererUUID,
			double amount, EconomyLogger.Type type, String comment)
	{
		EconomyLogger economylogger = new EconomyLogger(0, dateTime, fromUUIDOrNumber, toUUIDOrNumber, fromName, toName,
				ordererUUID, amount, type, comment);
		if(economylogger != null)
		{
			plugin.getMysqlHandler().create(Type.LOGGER, economylogger);
		}
	}
	
	/**
	 * Add a mysql Entry in the 4. mysql table from the economy plugin.
	 * @param  economylogger | A object which contains a the EconomyLogger.class, which contains all needed informations.
	 * @return void
	 * @author Christoph Steins/Avankziar
	 * @see EconomyLogger
	 */
	public static void addEconomyLogger(EconomyLogger economylogger)
	{
		if(economylogger != null)
		{
			plugin.getMysqlHandler().create(Type.LOGGER, economylogger);
		}
	}
	
	/**
	 * Return a EconomyLogger if the EconomyLogger exist, else return it null.
	 * @param  whereColumn | The String for the mysql query. <p>Example: "`id` = ? AND `datetime` = ?"
	 * @param  whereObjects | The objects that replace the "?" from the whereColumn String.
	 * @return EconomyLogger
	 * @author Christoph Steins/Avankziar
	 * @see EconomyLogger
	 */
	public static EconomyLogger getEconomyLogger(String whereColumn, Object... whereObjects)
	{
		if(plugin.getMysqlHandler().exist(Type.LOGGER, whereColumn, whereObjects))
		{
			return (EconomyLogger) plugin.getMysqlHandler().getData(Type.LOGGER, whereColumn, whereObjects);
		}
		return null;
	}
	
	/**
	 * Return a EconomyLogger ArrayList if every Object in the list a EconomyLogger is, otherwise it returns null.
	 * @param  orderByColumn | Select a Column witch sort mysql desc. <p>Example: "`id`"
	 * @param  start | The startpoint. Dont forget 0 as frist point of the list.
	 * @param  end | The endpoint. Start and end define the size of the list.
	 * @param  whereColumn | The String for the mysql query. <p>Example: "`id` = ? AND `datetime` = ?"
	 * @param  whereObjects | The objects that replace the "?" from the whereColumn String.
	 * @return ArrayList(EconomyLogger)
	 * @author Christoph Steins/Avankziar
	 * @see EconomyLogger
	 */
	public static ArrayList<EconomyLogger> getEconomyLoggerList(String orderByColumn,
			int start, int end, String whereColumn, Object... whereObjects)
	{
		return EconomyLogger.convertList(
				plugin.getMysqlHandler().getList(Type.LOGGER, orderByColumn, true, start, end, whereColumn, whereObjects));
	}
	
	/**
	 * Create or update a TrendLogger in the 4. mysql table.
	 * @param  date | The exact local date.
	 * @param  UUIDOrNumber | The playeruuid or bankaccountnumber wich is involved.
	 * @param  relativeAmountChange | The relative money change.
	 * @return void
	 * @author Christoph Steins/Avankziar
	 * @see TrendLogger
	 */
	public static void addTrendLogger(LocalDate date, String UUIDOrNumber, double relativeAmountChange,
			double balance)
	{
		TrendLogger.Type newtype = TrendLogger.Type.STABIL;
		TrendLogger trendLogger = new TrendLogger(date, newtype, UUIDOrNumber, relativeAmountChange, balance, balance);
		if(plugin.getMysqlHandler().exist(Type.TREND,
				"`dates` = ? AND `uuidornumber` = ?", TrendLogger.serialised(date), UUIDOrNumber))
		{
			TrendLogger oldtrendLogger = (TrendLogger) plugin.getMysqlHandler().getData(Type.TREND,
					"`dates` = ? AND `uuidornumber` = ?", TrendLogger.serialised(date), UUIDOrNumber);
			double newrelative = trendLogger.getRelativeAmountChange()+oldtrendLogger.getRelativeAmountChange();
			if(MatchApi.isPositivNumber(newrelative) 
					&& newrelative > plugin.getYamlHandler().get().getDouble("TrendLogger.ValueIsStabil"))
			{
				newtype = TrendLogger.Type.UP;
			} else if(!MatchApi.isPositivNumber(newrelative) 
					&& newrelative < plugin.getYamlHandler().get().getDouble("TrendLogger.ValueIsStabil"))
			{
				newtype = TrendLogger.Type.DOWN;
			}
			TrendLogger newtl = new TrendLogger(date, newtype, UUIDOrNumber, newrelative,
					oldtrendLogger.getFirstValue(), trendLogger.getLastValue());
			plugin.getMysqlHandler().updateData(Type.TREND, newtl, 
					"`dates` = ? AND `uuidornumber` = ?", TrendLogger.serialised(date), UUIDOrNumber);
		} else
		{
			trendLogger = new TrendLogger(date, newtype, UUIDOrNumber, relativeAmountChange, balance-relativeAmountChange, balance);
			plugin.getMysqlHandler().create(Type.TREND, trendLogger);
		}
	}
	
	/**
	 * Create or update a TrendLogger in the 4. mysql table.
	 * @param  trendLogger | The Object which contains all needed infos.
	 * @return void
	 * @author Christoph Steins/Avankziar
	 * @see TrendLogger
	 */
	public static void addTrendLogger(TrendLogger trendLogger)
	{
		TrendLogger.Type newtype = TrendLogger.Type.STABIL;
		if(plugin.getMysqlHandler().exist(Type.TREND,
				"`dates` = ? AND `uuidornumber` = ?", TrendLogger.serialised(trendLogger.getDate()),
				trendLogger.getUUIDOrNumber()))
		{
			TrendLogger oldtrendLogger = (TrendLogger) plugin.getMysqlHandler().getData(Type.TREND,
					"`dates` = ? AND `uuidornumber` = ?", TrendLogger.serialised(trendLogger.getDate()),
					trendLogger.getUUIDOrNumber());
			double newrelative = trendLogger.getRelativeAmountChange()+oldtrendLogger.getRelativeAmountChange();
			if(MatchApi.isPositivNumber(newrelative) 
					&& newrelative > plugin.getYamlHandler().get().getDouble("TrendLogger.ValueIsStabil"))
			{
				newtype = TrendLogger.Type.UP;
			} else if(!MatchApi.isPositivNumber(newrelative) 
					&& newrelative < -plugin.getYamlHandler().get().getDouble("TrendLogger.ValueIsStabil"))
			{
				newtype = TrendLogger.Type.DOWN;
			}
			TrendLogger newtl = new TrendLogger(trendLogger.getDate(), newtype, trendLogger.getUUIDOrNumber(), newrelative,
					oldtrendLogger.getFirstValue(), trendLogger.getLastValue());
			plugin.getMysqlHandler().updateData(Type.TREND, newtl, 
					"`dates` = ? AND `uuidornumber` = ?", TrendLogger.serialised(trendLogger.getDate()),
					trendLogger.getUUIDOrNumber());
		} else
		{
			plugin.getMysqlHandler().create(Type.TREND, trendLogger);
		}
	}
	
	/**
	 * Return a TrendLogger if the TrendLogger exist, else return it null.
	 * @param  whereColumn | The String for the mysql query. <p>Example: "`id` = ? AND `datetime` = ?"
	 * @param  whereObjects | The objects that replace the "?" from the whereColumn String.
	 * @return TrendLogger
	 * @author Christoph Steins/Avankziar
	 * @see TrendLogger
	 */
	public static TrendLogger getTrendLogger(String whereColumn, Object... whereObjects)
	{
		if(plugin.getMysqlHandler().exist(Type.TREND, whereColumn, whereObjects))
		{
			return (TrendLogger) plugin.getMysqlHandler().getData(Type.TREND, whereColumn, whereObjects);
		}
		return null;
	}
	
	/**
	 * Return a TrendLogger ArrayList if every Object in the list a TrendLogger is, otherwise it returns null.
	 * @param  orderByColumn | Select a Column witch sort mysql desc. <p>Example: "`id`"
	 * @param  start | The startpoint. Dont forget 0 as frist point of the list.
	 * @param  end | The endpoint. Start and end define the size of the list.
	 * @param  whereColumn | The String for the mysql query. <p>Example: "`id` = ? AND `datetime` = ?"
	 * @param  whereObjects | The objects that replace the "?" from the whereColumn String.
	 * @return ArrayList(TrendLogger)
	 * @author Christoph Steins/Avankziar
	 * @see TrendLogger
	 */
	public static ArrayList<TrendLogger> getTrendLoggerList(String orderByColumn,
			int start, int end, String whereColumn, Object... whereObjects)
	{
		return TrendLogger.convertList(
				plugin.getMysqlHandler().getList(Type.TREND, orderByColumn, true, start, end, whereColumn, whereObjects));
	}
}
