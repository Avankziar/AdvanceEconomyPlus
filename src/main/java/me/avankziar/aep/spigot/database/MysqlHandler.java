package main.java.me.avankziar.aep.spigot.database;

import java.io.IOException;
import java.util.ArrayList;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.tables.TableI;
import main.java.me.avankziar.aep.spigot.database.tables.TableII;
import main.java.me.avankziar.aep.spigot.database.tables.TableIII;
import main.java.me.avankziar.aep.spigot.database.tables.TableIV;
import main.java.me.avankziar.aep.spigot.database.tables.TableV;
import main.java.me.avankziar.aep.spigot.database.tables.TableVI;

public class MysqlHandler implements TableI, TableII, TableIII, TableIV, TableV, TableVI
{
	public enum Type
	{
		PLAYER, BANKACCOUNT, ACTION, TREND, STANDINGORDER, LOAN;
	}
	
	private AdvancedEconomyPlus plugin;
	public String tableNameI; //Spieler
	public String tableNameII; //BankAccount
	public String tableNameIII; //ACTION
	public String tableNameIV; //Trend
	public String tableNameV; //DauerAuftrag
	public String tableNameVI; //Schuldentilgung
	
	public MysqlHandler(AdvancedEconomyPlus plugin) 
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
		tableNameV = plugin.getYamlHandler().get().getString("Mysql.TableNameV");
		if(tableNameV == null)
		{
			return false;
		}
		tableNameVI = plugin.getYamlHandler().get().getString("Mysql.TableNameVI");
		if(tableNameVI == null)
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
		case ACTION:
			return TableIII.super.existIII(plugin, whereColumn, whereObject);
		case TREND:
			return TableIV.super.existIV(plugin, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.existV(plugin, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.existVI(plugin, whereColumn, whereObject);
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
		case ACTION:
			return TableIII.super.createIII(plugin, object);
		case TREND:
			return TableIV.super.createIV(plugin, object);
		case STANDINGORDER:
			return TableV.super.createV(plugin, object);
		case LOAN:
			return TableVI.super.createVI(plugin, object);
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
		case ACTION:
			return TableIII.super.updateDataIII(plugin, object, whereColumn, whereObject);
		case TREND:
			return TableIV.super.updateDataIV(plugin, object, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.updateDataV(plugin, object, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.updateDataVI(plugin, object, whereColumn, whereObject);
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
		case ACTION:
			return TableIII.super.getDataIII(plugin, whereColumn, whereObject);
		case TREND:
			return TableIV.super.getDataIV(plugin, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.getDataV(plugin, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.getDataVI(plugin, whereColumn, whereObject);
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
		case ACTION:
			return TableIII.super.deleteDataIII(plugin, whereColumn, whereObject);
		case TREND:
			return TableIV.super.deleteDataIV(plugin, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.deleteDataV(plugin, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.deleteDataVI(plugin, whereColumn, whereObject);
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
		case ACTION:
			return TableIII.super.lastIDIII(plugin);
		case TREND:
			return TableIV.super.lastIDIV(plugin);
		case STANDINGORDER:
			return TableV.super.lastIDV(plugin);
		case LOAN:
			return TableVI.super.lastIDVI(plugin);
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
		case ACTION:
			return TableIII.super.countWhereIDIII(plugin, whereColumn, whereObject);
		case TREND:
			return TableIV.super.countWhereIDIV(plugin, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.countWhereIDV(plugin, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.countWhereIDVI(plugin, whereColumn, whereObject);
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
		case ACTION:
			return TableIII.super.getListIII(plugin, orderByColumn, desc, start, end, whereColumn, whereObject);
		case TREND:
			return TableIV.super.getListIV(plugin, orderByColumn, start, end, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.getListV(plugin, orderByColumn, start, end, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.getListVI(plugin, orderByColumn, start, end, whereColumn, whereObject);
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
		case ACTION:
			return TableIII.super.getTopIII(plugin, orderByColumn, start, end);
		case TREND:
			return TableIV.super.getTopIV(plugin, orderByColumn, start, end);
		case STANDINGORDER:
			return TableV.super.getTopV(plugin, orderByColumn, start, end);
		case LOAN:
			return TableVI.super.getTopVI(plugin, orderByColumn, start, end);
		}
		return null;
	}
	
	public ArrayList<?> getAllListAt(Type type, String orderByColumn,
			boolean desc, String whereColumn, Object...whereObject) throws IOException
	{
		switch(type)
		{
		case PLAYER:
			return TableI.super.getAllListAtI(plugin, orderByColumn, whereColumn, whereObject);
		case BANKACCOUNT:
			return TableII.super.getAllListAtII(plugin, orderByColumn, whereColumn, whereObject);
		case ACTION:
			return TableIII.super.getAllListAtIII(plugin, orderByColumn, whereColumn, whereObject);
		case TREND:
			return TableIV.super.getAllListAtIV(plugin, orderByColumn, whereColumn, whereObject);
		case STANDINGORDER:
			return TableV.super.getAllListAtV(plugin, orderByColumn, whereColumn, whereObject);
		case LOAN:
			return TableVI.super.getAllListAtVI(plugin, orderByColumn, whereColumn, whereObject);
		}
		return null;
	}
}
