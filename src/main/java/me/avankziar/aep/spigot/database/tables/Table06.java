package main.java.me.avankziar.aep.spigot.database.tables;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;

public interface Table06
{	
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
				String sql = "INSERT INTO `" + MysqlHandler.Type.LOAN.getValue() 
						+ "`(`name`, `from_account`, `to_account`, `loan_owner`, `debtor`,"
						+ " `total_amount`, `loan_amount`, `amount_ratio`, `tax_in_decimal`, `amount_paid_so_far`, `amount_paid_to_tax`, `interest`,"
						+ " `start_time`, `repeating_time`, `last_time`, `end_time`,"
						+ " `forgiven`, `paused`, `finished`) " 
						+ "VALUES("
						+ "?, ?, ?, ?, ?, "
						+ "?, ?, ?, ?, ?, "
						+ "?, ?, ?, ?, ?, "
						+ "?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, ep.getName());
		        preparedStatement.setInt(2, ep.getAccountFromID());
		        preparedStatement.setInt(3, ep.getAccountToID());
		        preparedStatement.setString(4, ep.getOwner().toString());
		        preparedStatement.setString(5, ep.getDebtor().toString());
		        preparedStatement.setDouble(6, ep.getTotalAmount());
		        preparedStatement.setDouble(7, ep.getLoanAmount());
		        preparedStatement.setDouble(8, ep.getAmountRatio());
		        preparedStatement.setDouble(9, ep.getTaxInDecimal());
		        preparedStatement.setDouble(10, ep.getAmountPaidSoFar());
		        preparedStatement.setDouble(11, ep.getAmountPaidToTax());
		        preparedStatement.setDouble(12, ep.getInterest());
		        preparedStatement.setLong(13, ep.getStartTime());
		        preparedStatement.setLong(14, ep.getRepeatingTime());
		        preparedStatement.setLong(15, ep.getLastTime());
		        preparedStatement.setLong(16, ep.getEndTime());
		        preparedStatement.setBoolean(17, ep.isForgiven());
		        preparedStatement.setBoolean(18, ep.isPaused());
		        preparedStatement.setBoolean(19, ep.isFinished());
		        
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
				String data = "UPDATE `" + MysqlHandler.Type.LOAN.getValue()
						+ "` SET `name` = ?, `from_account` = ?, `to_account` = ?,"
						+ " `loan_owner` = ?, `debtor` = ?," 
						+ " `total_amount` = ?, `loan_amount` = ?, `amount_ratio` = ?, `tax_in_decimal` = ?,"
						+ " `amount_paid_so_far` = ?, `amount_paid_to_tax` = ?, `interest` = ?," 
						+ " `start_time` = ?, `repeating_time` = ?, `last_time` = ?, `end_time` = ?," 
						+ " `forgiven` = ?, `paused` = ?, `finished` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, ep.getName());
		        preparedStatement.setInt(2, ep.getAccountFromID());
		        preparedStatement.setInt(3, ep.getAccountToID());
		        preparedStatement.setString(4, ep.getOwner().toString());
		        preparedStatement.setString(5, ep.getDebtor().toString());
		        preparedStatement.setDouble(6, ep.getTotalAmount());
		        preparedStatement.setDouble(7, ep.getLoanAmount());
		        preparedStatement.setDouble(8, ep.getAmountRatio());
		        preparedStatement.setDouble(9, ep.getTaxInDecimal());
		        preparedStatement.setDouble(10, ep.getAmountPaidSoFar());
		        preparedStatement.setDouble(11, ep.getAmountPaidToTax());
		        preparedStatement.setDouble(12, ep.getInterest());
		        preparedStatement.setLong(13, ep.getStartTime());
		        preparedStatement.setLong(14, ep.getRepeatingTime());
		        preparedStatement.setLong(15, ep.getLastTime());
		        preparedStatement.setLong(16, ep.getEndTime());
		        preparedStatement.setBoolean(17, ep.isForgiven());
		        preparedStatement.setBoolean(18, ep.isPaused());
		        preparedStatement.setBoolean(19, ep.isFinished());
		        int i = 20;
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
				String sql = "SELECT * FROM `" + MysqlHandler.Type.LOAN.getValue() 
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
		        			result.getInt("from_account"),
		        			result.getInt("to_account"), 
		        			UUID.fromString(result.getString("loan_owner")),
		        			UUID.fromString(result.getString("debtor")),
		        			result.getDouble("totalamount"), 
		        			result.getDouble("loan_amount"), 
		        			result.getDouble("amount_ratio"),
		        			result.getDouble("tax_in_decimal"),
		        			result.getDouble("amount_paid_so_far"),
		        			result.getDouble("amount_paid_to_tax"),
		        			result.getDouble("interest"),
		        			result.getLong("start_time"), 
		        			result.getLong("repeating_time"), 
		        			result.getLong("last_time"), 
		        			result.getLong("end_time"), 
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
				String sql = "SELECT * FROM `" + MysqlHandler.Type.LOAN.getValue()
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
		        			result.getInt("from_account"),
		        			result.getInt("to_account"), 
		        			UUID.fromString(result.getString("loan_owner")),
		        			UUID.fromString(result.getString("debtor")),
		        			result.getDouble("totalamount"), 
		        			result.getDouble("loan_amount"), 
		        			result.getDouble("amount_ratio"),
		        			result.getDouble("tax_in_decimal"),
		        			result.getDouble("amount_paid_so_far"),
		        			result.getDouble("amount_paid_to_tax"),
		        			result.getDouble("interest"),
		        			result.getLong("start_time"), 
		        			result.getLong("repeating_time"), 
		        			result.getLong("last_time"), 
		        			result.getLong("end_time"), 
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
				String sql = "SELECT * FROM `" + MysqlHandler.Type.LOAN.getValue() 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<LoanRepayment> list = new ArrayList<LoanRepayment>();
		        while (result.next()) 
		        {
		        	LoanRepayment ep = new LoanRepayment(
		        			result.getInt("id"),
		        			result.getString("name"),
		        			result.getInt("from_account"),
		        			result.getInt("to_account"), 
		        			UUID.fromString(result.getString("loan_owner")),
		        			UUID.fromString(result.getString("debtor")),
		        			result.getDouble("totalamount"), 
		        			result.getDouble("loan_amount"), 
		        			result.getDouble("amount_ratio"),
		        			result.getDouble("tax_in_decimal"),
		        			result.getDouble("amount_paid_so_far"),
		        			result.getDouble("amount_paid_to_tax"),
		        			result.getDouble("interest"),
		        			result.getLong("start_time"), 
		        			result.getLong("repeating_time"), 
		        			result.getLong("last_time"), 
		        			result.getLong("end_time"), 
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
				String sql = "SELECT * FROM `" + MysqlHandler.Type.LOAN.getValue()
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn;
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
		        			result.getInt("from_account"),
		        			result.getInt("to_account"), 
		        			UUID.fromString(result.getString("loan_owner")),
		        			UUID.fromString(result.getString("debtor")),
		        			result.getDouble("totalamount"), 
		        			result.getDouble("loan_amount"), 
		        			result.getDouble("amount_ratio"),
		        			result.getDouble("tax_in_decimal"),
		        			result.getDouble("amount_paid_so_far"),
		        			result.getDouble("amount_paid_to_tax"),
		        			result.getDouble("interest"),
		        			result.getLong("start_time"), 
		        			result.getLong("repeating_time"), 
		        			result.getLong("last_time"), 
		        			result.getLong("end_time"), 
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