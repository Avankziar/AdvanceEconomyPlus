package main.java.me.avankziar.advanceeconomy.spigot.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;

public interface TableI
{
	
	default boolean existI(AdvanceEconomy plugin, String whereColumn, Object... object) 
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameI 
						+ "` WHERE "+whereColumn+" LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : object)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return true;
		        }
		    } catch (SQLException e) 
			{
				  AdvanceEconomy.log.warning("Error: " + e.getMessage());
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
		return false;
	}
	
	default boolean createI(AdvanceEconomy plugin, Object object) 
	{
		if(!(object instanceof EcoPlayer))
		{
			return false;
		}
		EcoPlayer ep = (EcoPlayer) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + plugin.getMysqlHandler().tableNameI 
						+ "`(`player_uuid`, `player_name`, `balance`, `bankaccountlist`,"
						+ " `moneyplayerflow`, `moneybankflow`, `generalmessage`, `pendinginvite`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, ep.getUUID());
		        preparedStatement.setString(2, ep.getName());
		        preparedStatement.setDouble(3, ep.getBalance());
		        preparedStatement.setString(4, String.join(";", ep.getBankAccountNumber()));
		        preparedStatement.setBoolean(5, ep.isMoneyBankFlow());
		        preparedStatement.setBoolean(6, ep.isMoneyPlayerFlow());
		        preparedStatement.setBoolean(7, ep.isGeneralMessage());
		        preparedStatement.setString(8, ep.getPendingInvite());
		        
		        preparedStatement.executeUpdate();
		        return true;
		    } catch (SQLException e) 
			{
				  AdvanceEconomy.log.warning("Error: " + e.getMessage());
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
	
	default boolean updateDataI(AdvanceEconomy plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof EcoPlayer))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		EcoPlayer ep = (EcoPlayer) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + plugin.getMysqlHandler().tableNameI
						+ "` SET `player_uuid` = ?, `player_name` = ?, `balance` = ?,"
						+ " `bankaccountlist` = ?, `moneyplayerflow` = ?, `moneybankflow` = ?, `generalmessage` = ?,"
						+ " `pendinginvite` = ?" 
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
		        int i = 9;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
				
				preparedStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				AdvanceEconomy.log.warning("Error: " + e.getMessage());
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
	
	default Object getDataI(AdvanceEconomy plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameI 
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
		        	return new EcoPlayer(result.getInt("id"),
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getDouble("balance"),
		        			lists,
		        			result.getBoolean("moneyplayerflow"),
		        			result.getBoolean("moneybankflow"),
		        			result.getBoolean("generalmessage"),
		        			result.getString("pendinginvite"));
		        }
		    } catch (SQLException e) 
			{
				  AdvanceEconomy.log.warning("Error: " + e.getMessage());
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
	
	default boolean deleteDataI(AdvanceEconomy plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + plugin.getMysqlHandler().tableNameI + "` WHERE "+whereColumn;
			preparedStatement = conn.prepareStatement(sql);
			int i = 1;
	        for(Object o : whereObject)
	        {
	        	preparedStatement.setObject(i, o);
	        	i++;
	        }
			preparedStatement.execute();
			return true;
		} catch (Exception e) 
		{
			e.printStackTrace();
		} finally 
		{
			try {
				if (preparedStatement != null) 
				{
					preparedStatement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	default int lastIDI(AdvanceEconomy plugin)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameI + "` ORDER BY `id` DESC LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        while(result.next())
		        {
		        	return result.getInt("id");
		        }
		    } catch (SQLException e) 
			{
		    	e.printStackTrace();
		    	return 0;
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
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return 0;
	}
	
	default int countWhereIDI(AdvanceEconomy plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameI
						+ "` WHERE "+whereColumn
						+ " ORDER BY `id` DESC";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        int count = 0;
		        while(result.next())
		        {
		        	count++;
		        }
		        return count;
		    } catch (SQLException e) 
			{
		    	e.printStackTrace();
		    	return 0;
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
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return 0;
	}
	
	default ArrayList<EcoPlayer> getListI(AdvanceEconomy plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameI
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<EcoPlayer> list = new ArrayList<EcoPlayer>();
		        while (result.next()) 
		        {
		        	String bankacc = result.getString("bankaccountlist");
		        	List<String> lists = new ArrayList<>();
		        	if(bankacc != null)
		        	{
		        		lists = Arrays.asList(result.getString("bankaccountlist").split(";"));
		        	}
		        	EcoPlayer ep = new EcoPlayer(result.getInt("id"),
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getDouble("balance"),
		        			lists,
		        			result.getBoolean("moneyplayerflow"),
		        			result.getBoolean("moneybankflow"),
		        			result.getBoolean("generalmessage"),
		        			result.getString("pendinginvite"));
		        	list.add(ep);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  AdvanceEconomy.log.warning("Error: " + e.getMessage());
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
	
	default ArrayList<EcoPlayer> getTopI(AdvanceEconomy plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameI 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<EcoPlayer> list = new ArrayList<EcoPlayer>();
		        while (result.next()) 
		        {
		        	String bankacc = result.getString("bankaccountlist");
		        	List<String> lists = new ArrayList<>();
		        	if(bankacc != null)
		        	{
		        		lists = Arrays.asList(result.getString("bankaccountlist").split(";"));
		        	}
		        	EcoPlayer ep = new EcoPlayer(result.getInt("id"),
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getDouble("balance"),
		        			lists,
		        			result.getBoolean("moneyplayerflow"),
		        			result.getBoolean("moneybankflow"),
		        			result.getBoolean("generalmessage"),
		        			result.getString("pendinginvite"));
		        	list.add(ep);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  AdvanceEconomy.log.warning("Error: " + e.getMessage());
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