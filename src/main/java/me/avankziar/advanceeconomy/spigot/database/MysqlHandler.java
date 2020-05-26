package main.java.me.avankziar.advanceeconomy.spigot.database;

import java.util.ArrayList;

import main.java.me.avankziar.advanceeconomy.spigot.database.tables.TableI;
import main.java.me.avankziar.advanceeconomy.spigot.database.tables.TableII;
import main.java.me.avankziar.advanceeconomy.spigot.database.tables.TableIII;
import main.java.me.avankziar.advanceeconomy.spigot.database.tables.TableIV;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;

public class MysqlHandler implements TableI, TableII, TableIII, TableIV
{
	public enum Type
	{
		PLAYER, BANKACCOUNT, LOGGER, TREND;
	}
	
	private AdvanceEconomy plugin;
	public String tableNameI; //Spieler
	public String tableNameII; //BankAccount
	public String tableNameIII; //Logger
	public String tableNameIV; //Trend
	
	public MysqlHandler(AdvanceEconomy plugin) 
	{
		this.plugin = plugin;
		loadMysqlHandler();
	}
	
	public boolean loadMysqlHandler()
	{
		tableNameI = plugin.getYamlHandler().get().getString("Mysql.TableNameI");
		if(tableNameI == null)
		{
			return false;
		}
		tableNameII = plugin.getYamlHandler().get().getString("Mysql.TableNameII");
		if(tableNameII == null)
		{
			return false;
		}
		tableNameIII = plugin.getYamlHandler().get().getString("Mysql.TableNameIII");
		if(tableNameIII == null)
		{
			return false;
		}
		tableNameIV = plugin.getYamlHandler().get().getString("Mysql.TableNameIV");
		if(tableNameIV == null)
		{
			return false;
		}
		return true;
	}
	
	public boolean exist(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.existI(plugin, whereColumn, whereObject);
		case BANKACCOUNT:
			return TableII.super.existII(plugin, whereColumn, whereObject);
		case LOGGER:
			return TableIII.super.existIII(plugin, whereColumn, whereObject);
		case TREND:
			return TableIV.super.existIV(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public boolean create(Type type, Object object)
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.createI(plugin, object);
		case BANKACCOUNT:
			return TableII.super.createII(plugin, object);
		case LOGGER:
			return TableIII.super.createIII(plugin, object);
		case TREND:
			return TableIV.super.createIV(plugin, object);
		}
		return false;
	}
	
	public boolean updateData(Type type, Object object, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.updateDataI(plugin, object, whereColumn, whereObject);
		case BANKACCOUNT:
			return TableII.super.updateDataII(plugin, object, whereColumn, whereObject);
		case LOGGER:
			return TableIII.super.updateDataIII(plugin, object, whereColumn, whereObject);
		case TREND:
			return TableIV.super.updateDataIV(plugin, object, whereColumn, whereObject);
		}
		return false;
	}
	
	public Object getData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.getDataI(plugin, whereColumn, whereObject);
		case BANKACCOUNT:
			return TableII.super.getDataII(plugin, whereColumn, whereObject);
		case LOGGER:
			return TableIII.super.getDataIII(plugin, whereColumn, whereObject);
		case TREND:
			return TableIV.super.getDataIV(plugin, whereColumn, whereObject);
		}
		return null;
	}
	
	public boolean deleteData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.deleteDataI(plugin, whereColumn, whereObject);
		case BANKACCOUNT:
			return TableII.super.deleteDataII(plugin, whereColumn, whereObject);
		case LOGGER:
			return TableIII.super.deleteDataIII(plugin, whereColumn, whereObject);
		case TREND:
			return TableIV.super.deleteDataIV(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public int lastID(Type type)
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.lastIDI(plugin);
		case BANKACCOUNT:
			return TableII.super.lastIDII(plugin);
		case LOGGER:
			return TableIII.super.lastIDIII(plugin);
		case TREND:
			return TableIV.super.lastIDIV(plugin);
		}
		return 0;
	}
	
	public int countWhereID(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.countWhereIDI(plugin, whereColumn, whereObject);
		case BANKACCOUNT:
			return TableII.super.countWhereIDII(plugin, whereColumn, whereObject);
		case LOGGER:
			return TableIII.super.countWhereIDIII(plugin, whereColumn, whereObject);
		case TREND:
			return TableIV.super.countWhereIDIV(plugin, whereColumn, whereObject);
		}
		return 0;
	}
	
	public ArrayList<?> getList(Type type, String orderByColumn, boolean desc, int start, int end, String whereColumn, Object...whereObject)
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.getListI(plugin, orderByColumn, start, end, whereColumn, whereObject);
		case BANKACCOUNT:
			return TableII.super.getListII(plugin, orderByColumn, start, end, whereColumn, whereObject);
		case LOGGER:
			return TableIII.super.getListIII(plugin, orderByColumn, desc, start, end, whereColumn, whereObject);
		case TREND:
			return TableIV.super.getListIV(plugin, orderByColumn, start, end, whereColumn, whereObject);
		}
		return null;
	}
	
	public ArrayList<?> getTop(Type type, String orderByColumn, int start, int end)
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.getTopI(plugin, orderByColumn, start, end);
		case BANKACCOUNT:
			return TableII.super.getTopII(plugin, orderByColumn, start, end);
		case LOGGER:
			return TableIII.super.getTopIII(plugin, orderByColumn, start, end);
		case TREND:
			return TableIV.super.getTopIV(plugin, orderByColumn, start, end);
		}
		return null;
	}
}
