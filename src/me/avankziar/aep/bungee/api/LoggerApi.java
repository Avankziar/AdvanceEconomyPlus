package me.avankziar.aep.bungee.api;

import java.time.LocalDate;
import java.util.ArrayList;

import me.avankziar.aep.bungee.AEP;
import me.avankziar.aep.bungee.handler.ConvertHandler;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.assistance.TimeHandler;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.ActionLogger;
import me.avankziar.aep.general.objects.TrendLogger;

public class LoggerApi
{
	private static AEP plugin;
	
	public LoggerApi(AEP plugin)
	{
		LoggerApi.plugin = plugin;
	}
	
	/**
	 * Add a mysql Entry in the 4. mysql table from the economy plugin.
	 * @param  economylogger | A object which contains a the EconomyLogger.class, which contains all needed informations.
	 * @return void
	 * @author Christoph Steins/Avankziar
	 * @see ActionLogger
	 */
	public static void addActionLogger(ActionLogger economylogger)
	{
		if(economylogger != null)
		{
			plugin.getMysqlHandler().create(MysqlType.ACTION, economylogger);
		}
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
	 * @see ActionLogger
	 */
	public static ArrayList<ActionLogger> getActionLoggerList(String orderByColumn,
			int start, int end, String whereColumn, Object... whereObjects)
	{
		return ConvertHandler.convertListIII(
				plugin.getMysqlHandler().getList(MysqlType.ACTION, orderByColumn, start, end, whereColumn, whereObjects));
	}
	
	/**
	 * Create or update a TrendLogger in the 4. mysql table.
	 * @param date
	 * @param accountID | the accountID which is involved.
	 * @param relativeAmountChange  | The relative money change.
	 * @param balance
	 * @return void
	 * @author Christoph Steins/Avankziar
	 * @see TrendLogger
	 */
	public static void addTrendLogger(LocalDate date, int accountID, double relativeAmountChange, double balance)
	{
		TrendLogger.Type newtype = TrendLogger.Type.STABIL;
		TrendLogger trendLogger = new TrendLogger(date, newtype, accountID, relativeAmountChange, balance, balance);
		if(plugin.getMysqlHandler().exist(MysqlType.TREND,
				"`dates` = ? AND `account_id` = ?", TimeHandler.getTime(date), accountID))
		{
			TrendLogger oldtrendLogger = (TrendLogger) plugin.getMysqlHandler().getData(MysqlType.TREND,
					"`dates` = ? AND `account_id` = ?", TimeHandler.getTime(date), accountID);
			double newrelative = trendLogger.getRelativeAmountChange()+oldtrendLogger.getRelativeAmountChange();
			if(MatchApi.isPositivNumber(newrelative) 
					&& newrelative > plugin.getYamlHandler().getConfig().getDouble("TrendLogger.ValueIsStabil"))
			{
				newtype = TrendLogger.Type.UP;
			} else if(!MatchApi.isPositivNumber(newrelative) 
					&& newrelative < plugin.getYamlHandler().getConfig().getDouble("TrendLogger.ValueIsStabil"))
			{
				newtype = TrendLogger.Type.DOWN;
			}
			TrendLogger newtl = new TrendLogger(date, newtype, accountID, newrelative,
					oldtrendLogger.getFirstValue(), trendLogger.getLastValue());
			plugin.getMysqlHandler().updateData(MysqlType.TREND, newtl, 
					"`dates` = ? AND `account_id` = ?", TimeHandler.getTime(date), accountID);
		} else
		{
			trendLogger = new TrendLogger(date, newtype, accountID, relativeAmountChange, balance+relativeAmountChange, balance);
			plugin.getMysqlHandler().create(MysqlType.TREND, trendLogger);
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
		if(plugin.getMysqlHandler().exist(MysqlType.TREND,
				"`dates` = ? AND `account_id` = ?", trendLogger.getUnixTime(),
				trendLogger.getAccountID()))
		{
			TrendLogger oldtrendLogger = (TrendLogger) plugin.getMysqlHandler().getData(MysqlType.TREND,
					"`dates` = ? AND `account_id` = ?", trendLogger.getUnixTime(),
					trendLogger.getAccountID());
			double newrelative = trendLogger.getRelativeAmountChange()+oldtrendLogger.getRelativeAmountChange();
			if(MatchApi.isPositivNumber(newrelative) 
					&& newrelative > plugin.getYamlHandler().getConfig().getDouble("TrendLogger.ValueIsStabil"))
			{
				newtype = TrendLogger.Type.UP;
			} else if(!MatchApi.isPositivNumber(newrelative) 
					&& newrelative < -plugin.getYamlHandler().getConfig().getDouble("TrendLogger.ValueIsStabil"))
			{
				newtype = TrendLogger.Type.DOWN;
			}
			TrendLogger newtl = new TrendLogger(trendLogger.getUnixTime(), newtype, trendLogger.getAccountID(), newrelative,
					oldtrendLogger.getFirstValue(), trendLogger.getLastValue());
			plugin.getMysqlHandler().updateData(MysqlType.TREND, newtl, 
					"`dates` = ? AND `account_id` = ?", trendLogger.getUnixTime(),
					trendLogger.getAccountID());
		} else
		{
			plugin.getMysqlHandler().create(MysqlType.TREND, trendLogger);
		}
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
		return ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getList(MysqlType.TREND, orderByColumn, start, end, whereColumn, whereObjects));
	}
}
