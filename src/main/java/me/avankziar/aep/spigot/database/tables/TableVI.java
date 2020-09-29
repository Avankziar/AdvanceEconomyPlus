package main.java.me.avankziar.aep.spigot.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;

public interface TableVI
{
	
	default boolean existVI(AdvancedEconomyPlus plugin, String whereColumn, Object... object) 
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameVI 
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
		return false;
	}
	
	default boolean createVI(AdvancedEconomyPlus plugin, Object object) 
	{
		if(!(object instanceof LoanRepayment))
		{
			return false;
		}
		LoanRepayment ep = (LoanRepayment) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + plugin.getMysqlHandler().tableNameVI 
						+ "`(`name`, `from_player`, `to_player`, `debtowner`,"
						+ " `totalamount`, `amountratio`, `amountpaidsofar`, `interest`,"
						+ " `starttime`, `repeatingtime`, `lasttime`, `endtime`,"
						+ " `forgiven`, `paused`, `finished`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, ep.getName());
		        preparedStatement.setString(2, ep.getFrom());
		        preparedStatement.setString(3, ep.getTo());
		        preparedStatement.setString(4, ep.getLoanOwner());
		        preparedStatement.setDouble(5, ep.getTotalAmount());
		        preparedStatement.setDouble(6, ep.getAmountRatio());
		        preparedStatement.setDouble(7, ep.getAmountPaidSoFar());
		        preparedStatement.setDouble(8, ep.getInterest());
		        preparedStatement.setLong(9, ep.getStartTime());
		        preparedStatement.setLong(10, ep.getRepeatingTime());
		        preparedStatement.setLong(11, ep.getLastTime());
		        preparedStatement.setLong(12, ep.getEndTime());
		        preparedStatement.setBoolean(13, ep.isForgiven());
		        preparedStatement.setBoolean(14, ep.isPaused());
		        preparedStatement.setBoolean(15, ep.isFinished());
		        
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
	
	default boolean updateDataVI(AdvancedEconomyPlus plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof LoanRepayment))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		LoanRepayment ep = (LoanRepayment) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + plugin.getMysqlHandler().tableNameVI
						+ "` SET `name` = ?, `from_player` = ?, `to_player` = ?, `debtowner` = ?," 
						+ " `totalamount` = ?, `amountratio` = ?, `amountpaidsofar` = ?, `interest` = ?," 
						+ " `starttime` = ?, `repeatingtime` = ?, `lasttime` = ?, `endtime` = ?," 
						+ " `forgiven` = ?, `paused` = ?, `finished` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, ep.getName());
				preparedStatement.setString(2, ep.getFrom());
		        preparedStatement.setString(3, ep.getTo());
		        preparedStatement.setString(4, ep.getLoanOwner());
		        preparedStatement.setDouble(5, ep.getTotalAmount());
		        preparedStatement.setDouble(6, ep.getAmountRatio());
		        preparedStatement.setDouble(7, ep.getAmountPaidSoFar());
		        preparedStatement.setDouble(8, ep.getInterest());
		        preparedStatement.setLong(9, ep.getStartTime());
		        preparedStatement.setLong(10, ep.getRepeatingTime());
		        preparedStatement.setLong(11, ep.getLastTime());
		        preparedStatement.setLong(12, ep.getEndTime());
		        preparedStatement.setBoolean(13, ep.isForgiven());
		        preparedStatement.setBoolean(14, ep.isPaused());
		        preparedStatement.setBoolean(15, ep.isFinished());
		        int i = 16;
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
	
	default Object getDataVI(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVI 
						+ "` WHERE "+whereColumn+" ORDER BY `id` ASC LIMIT 1";
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
		        	return new LoanRepayment(
		        			result.getInt("id"),
		        			result.getString("name"),
		        			result.getString("from_player"),
		        			result.getString("to_player"), 
		        			result.getString("debtowner"), 
		        			result.getDouble("totalamount"), 
		        			result.getDouble("amountratio"), 
		        			result.getDouble("amountpaidsofar"),
		        			result.getDouble("interest"),
		        			result.getLong("starttime"), 
		        			result.getLong("repeatingtime"), 
		        			result.getLong("lasttime"), 
		        			result.getLong("endtime"), 
		        			result.getBoolean("forgiven"), 
		        			result.getBoolean("paused"), 
		        			result.getBoolean("finished"));
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
	
	default boolean deleteDataVI(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + plugin.getMysqlHandler().tableNameVI + "` WHERE "+whereColumn;
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
	
	default int lastIDVI(AdvancedEconomyPlus plugin)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameVI + "` ORDER BY `id` DESC LIMIT 1";
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
	
	default int countWhereIDVI(AdvancedEconomyPlus plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameVI
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
	
	default ArrayList<LoanRepayment> getListVI(AdvancedEconomyPlus plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVI
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<LoanRepayment> list = new ArrayList<LoanRepayment>();
		        while (result.next()) 
		        {
		        	LoanRepayment ep = new LoanRepayment(
		        			result.getInt("id"),
		        			result.getString("name"),
		        			result.getString("from_player"),
		        			result.getString("to_player"), 
		        			result.getString("debtowner"), 
		        			result.getDouble("totalamount"), 
		        			result.getDouble("amountratio"), 
		        			result.getDouble("amountpaidsofar"),
		        			result.getDouble("interest"),
		        			result.getLong("starttime"), 
		        			result.getLong("repeatingtime"), 
		        			result.getLong("lasttime"), 
		        			result.getLong("endtime"), 
		        			result.getBoolean("forgiven"), 
		        			result.getBoolean("paused"), 
		        			result.getBoolean("finished"));
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
	
	default ArrayList<LoanRepayment> getTopVI(AdvancedEconomyPlus plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVI 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<LoanRepayment> list = new ArrayList<LoanRepayment>();
		        while (result.next()) 
		        {
		        	LoanRepayment ep = new LoanRepayment(
		        			result.getInt("id"),
		        			result.getString("name"),
		        			result.getString("from_player"),
		        			result.getString("to_player"), 
		        			result.getString("debtowner"), 
		        			result.getDouble("totalamount"), 
		        			result.getDouble("amountratio"), 
		        			result.getDouble("amountpaidsofar"),
		        			result.getDouble("interest"),
		        			result.getLong("starttime"), 
		        			result.getLong("repeatingtime"), 
		        			result.getLong("lasttime"), 
		        			result.getLong("endtime"), 
		        			result.getBoolean("forgiven"), 
		        			result.getBoolean("paused"), 
		        			result.getBoolean("finished"));
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
	
	default ArrayList<LoanRepayment> getAllListAtVI(AdvancedEconomyPlus plugin, String orderByColumn,
			String whereColumn, Object...whereObject) throws IOException
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVI
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<LoanRepayment> list = new ArrayList<LoanRepayment>();
		        while (result.next()) 
		        {
		        	LoanRepayment ep = new LoanRepayment(
		        			result.getInt("id"),
		        			result.getString("name"),
		        			result.getString("from_player"),
		        			result.getString("to_player"), 
		        			result.getString("debtowner"), 
		        			result.getDouble("totalamount"), 
		        			result.getDouble("amountratio"), 
		        			result.getDouble("amountpaidsofar"),
		        			result.getDouble("interest"),
		        			result.getLong("starttime"), 
		        			result.getLong("repeatingtime"), 
		        			result.getLong("lasttime"), 
		        			result.getLong("endtime"), 
		        			result.getBoolean("forgiven"), 
		        			result.getBoolean("paused"), 
		        			result.getBoolean("finished"));
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