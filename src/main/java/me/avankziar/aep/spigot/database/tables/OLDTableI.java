package main.java.me.avankziar.aep.spigot.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;

public interface OLDTableI
{	
	default boolean createI(AdvancedEconomyPlus plugin, Object object) 
	{
		if(!(object instanceof OLD_AEPUser))
		{
			return false;
		}
		OLD_AEPUser ep = (OLD_AEPUser) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.PLAYER.getValue()
						+ "`(`player_uuid`, `player_name`, `balance`, `bankaccountlist`,"
						+ " `moneyplayerflow`, `moneybankflow`, `generalmessage`, `pendinginvite`, `frozen`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, ep.getUUID());
		        preparedStatement.setString(2, ep.getName());
		        preparedStatement.setDouble(3, ep.getBalance());
		        preparedStatement.setString(4, String.join(";", ep.getBankAccountNumber()));
		        preparedStatement.setBoolean(5, ep.isMoneyBankFlow());
		        preparedStatement.setBoolean(6, ep.isMoneyPlayerFlow());
		        preparedStatement.setBoolean(7, ep.isGeneralMessage());
		        preparedStatement.setString(8, ep.getPendingInvite());
		        preparedStatement.setBoolean(9, ep.isFrozen());
		        
		        preparedStatement.executeUpdate();
		        return true;
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return false;
	}
	
	default boolean updateDataI(AdvancedEconomyPlus plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof OLD_AEPUser))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		OLD_AEPUser ep = (OLD_AEPUser) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.PLAYER.getValue()
						+ "` SET `player_uuid` = ?, `player_name` = ?, `balance` = ?,"
						+ " `bankaccountlist` = ?, `moneyplayerflow` = ?, `moneybankflow` = ?, `generalmessage` = ?,"
						+ " `pendinginvite` = ?, `frozen` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, ep.getUUID());
		        preparedStatement.setString(2, ep.getName());
		        preparedStatement.setDouble(3, ep.getBalance());
		        preparedStatement.setString(4, String.join(";", ep.getBankAccountNumber()));
		        preparedStatement.setBoolean(5, ep.isMoneyPlayerFlow());
		        preparedStatement.setBoolean(6, ep.isMoneyBankFlow());
		        preparedStatement.setBoolean(7, ep.isGeneralMessage());
		        preparedStatement.setString(8, ep.getPendingInvite());
		        preparedStatement.setBoolean(9, ep.isFrozen());
		        int i = 10;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
				
				preparedStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					if (preparedStatement != null) 
					{
						preparedStatement.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        return false;
	}
	
	default Object getDataI(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PLAYER.getValue() 
						+ "` WHERE "+whereColumn+" LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	String bankacc = result.getString("bankaccountlist");
		        	List<String> lists = new ArrayList<>();
		        	if(bankacc != null)
		        	{
		        		lists = Arrays.asList(result.getString("bankaccountlist").split(";"));
		        	}
		        	return new OLD_AEPUser(result.getInt("id"),
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getDouble("balance"),
		        			lists,
		        			result.getBoolean("moneyplayerflow"),
		        			result.getBoolean("moneybankflow"),
		        			result.getBoolean("generalmessage"),
		        			result.getString("pendinginvite"),
		        			result.getBoolean("frozen"));
		        }
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	default ArrayList<OLD_AEPUser> getListI(AdvancedEconomyPlus plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PLAYER.getValue()
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<OLD_AEPUser> list = new ArrayList<OLD_AEPUser>();
		        while (result.next()) 
		        {
		        	String bankacc = result.getString("bankaccountlist");
		        	List<String> lists = new ArrayList<>();
		        	if(bankacc != null)
		        	{
		        		lists = Arrays.asList(result.getString("bankaccountlist").split(";"));
		        	}
		        	OLD_AEPUser ep = new OLD_AEPUser(result.getInt("id"),
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getDouble("balance"),
		        			lists,
		        			result.getBoolean("moneyplayerflow"),
		        			result.getBoolean("moneybankflow"),
		        			result.getBoolean("generalmessage"),
		        			result.getString("pendinginvite"),
		        			result.getBoolean("frozen"));
		        	list.add(ep);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	default ArrayList<OLD_AEPUser> getTopI(AdvancedEconomyPlus plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PLAYER.getValue() 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<OLD_AEPUser> list = new ArrayList<OLD_AEPUser>();
		        while (result.next()) 
		        {
		        	String bankacc = result.getString("bankaccountlist");
		        	List<String> lists = new ArrayList<>();
		        	if(bankacc != null)
		        	{
		        		lists = Arrays.asList(result.getString("bankaccountlist").split(";"));
		        	}
		        	OLD_AEPUser ep = new OLD_AEPUser(result.getInt("id"),
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getDouble("balance"),
		        			lists,
		        			result.getBoolean("moneyplayerflow"),
		        			result.getBoolean("moneybankflow"),
		        			result.getBoolean("generalmessage"),
		        			result.getString("pendinginvite"),
		        			result.getBoolean("frozen"));
		        	list.add(ep);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	default ArrayList<OLD_AEPUser> getAllListAtI(AdvancedEconomyPlus plugin, String orderByColumn,
			String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PLAYER.getValue()
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<OLD_AEPUser> list = new ArrayList<OLD_AEPUser>();
		        while (result.next()) 
		        {
		        	String bankacc = result.getString("bankaccountlist");
		        	List<String> lists = new ArrayList<>();
		        	if(bankacc != null)
		        	{
		        		lists = Arrays.asList(result.getString("bankaccountlist").split(";"));
		        	}
		        	OLD_AEPUser ep = new OLD_AEPUser(result.getInt("id"),
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getDouble("balance"),
		        			lists,
		        			result.getBoolean("moneyplayerflow"),
		        			result.getBoolean("moneybankflow"),
		        			result.getBoolean("generalmessage"),
		        			result.getString("pendinginvite"),
		        			result.getBoolean("frozen"));
		        	list.add(ep);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
}