package me.avankziar.aep.general.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import me.avankziar.aep.general.database.MysqlBaseHandler;
import me.avankziar.aep.general.database.MysqlHandable;
import me.avankziar.aep.general.database.QueryType;

public class LoanRepayment implements MysqlHandable
{
	//Schulden Tilgung
	private int id;
	private String name;
	private int fromID;
	private int toID;
	private UUID owner; //Kann nur ein Spieler sein. Und nur dieser kann die Schulden als "getilgt" setzen.
	private UUID debtor; //Der Schuldner
	private double loanAmount; //Betrag welcher geliehen wurde, ohne zinsen, steuern etc.
	private double totalAmount; //Gesamtbetrag der zu zahlen ist.
	private double amountRatio; //Schulden Rate, wieviel pro Zahlung gezahlt werden soll.
	private double taxInDecimal; //Steuern in decimalzahl: 1.0 == 100%
	private double amountPaidSoFar; //Bisher gezahlter Betrag
	private double amountPaidToTax; //Bisher gezahlter Betrag
	private double interest; //Zinsen, nur auf komplett summe gerechnet. Prozent angabe
	private long startTime; //Beginn der Zahlung
	private long repeatingTime; //Millisekunden, wann wieder gezahlt werden muss.
	private long lastTime; //Millisekunden, wann das letzte mal gezahlt wird.
	private long endTime; //Millisekunden, wann die restliche Betrag fällig ist.
	private boolean forgiven; //Sind die Schulden vom Eigentümer vergeben worden.
	private boolean paused; //Sind die Schulden vom Eigentümer pausiert worden.
	private boolean finished; //Ist alles bezahlt worden.
	
	public LoanRepayment() {}
	
	public LoanRepayment(int id, String name, int fromID, int toID, UUID owner, UUID debtor,
			double totalAmount, double loanAmount, double amountRatio, double taxInDecimal,
			double amountPaidSoFar, double amountPaidToTax, double interest,
			long startTime, long repeatingTime, long lastTime, long endTime,
			boolean forgiven, boolean paused, boolean finished)
	{
		setId(id);
		setName(name);
		setAccountFromID(fromID);
		setAccountToID(toID);
		setOwner(owner);
		setDebtor(debtor);
		setTotalAmount(totalAmount);
		setLoanAmount(loanAmount);
		setAmountRatio(amountRatio);
		setTaxInDecimal(taxInDecimal);
		setAmountPaidSoFar(amountPaidSoFar);
		setAmountPaidToTax(amountPaidToTax);
		setInterest(interest);
		setStartTime(startTime);
		setRepeatingTime(repeatingTime);
		setLastTime(lastTime);
		setEndTime(endTime);
		setForgiven(forgiven);
		setPaused(paused);
		setFinished(finished);
	}

	public double getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	public double getAmountRatio()
	{
		return amountRatio;
	}

	public void setAmountRatio(double amountRatio)
	{
		this.amountRatio = amountRatio;
	}

	public double getAmountPaidSoFar()
	{
		return amountPaidSoFar;
	}

	public void setAmountPaidSoFar(double amountPaidSoFar)
	{
		this.amountPaidSoFar = amountPaidSoFar;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	public long getRepeatingTime()
	{
		return repeatingTime;
	}

	public void setRepeatingTime(long repeatingTime)
	{
		this.repeatingTime = repeatingTime;
	}

	public long getLastTime()
	{
		return lastTime;
	}

	public void setLastTime(long lastTime)
	{
		this.lastTime = lastTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public boolean isForgiven()
	{
		return forgiven;
	}

	public void setForgiven(boolean forgiven)
	{
		this.forgiven = forgiven;
	}

	public boolean isPaused()
	{
		return paused;
	}

	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}

	public boolean isFinished()
	{
		return finished;
	}

	public void setFinished(boolean finished)
	{
		this.finished = finished;
	}

	public double getInterest()
	{
		return interest;
	}

	public void setInterest(double interest)
	{
		this.interest = interest;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public void addPayment(double amount)
	{
		amountPaidSoFar = amountPaidSoFar + amount;
	}
	
	public void addTax(double amount)
	{
		amountPaidToTax = amountPaidToTax + amount;
	}

	public int getAccountFromID()
	{
		return fromID;
	}

	public void setAccountFromID(int fromID)
	{
		this.fromID = fromID;
	}

	public int getAccountToID()
	{
		return toID;
	}

	public void setAccountToID(int toID)
	{
		this.toID = toID;
	}

	public UUID getOwner()
	{
		return owner;
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	public double getAmountPaidToTax()
	{
		return amountPaidToTax;
	}

	public void setAmountPaidToTax(double amountPaidToTax)
	{
		this.amountPaidToTax = amountPaidToTax;
	}

	/**
	 * @return the debtor
	 */
	public UUID getDebtor()
	{
		return debtor;
	}

	/**
	 * @param debtor the debtor to set
	 */
	public void setDebtor(UUID debtor)
	{
		this.debtor = debtor;
	}

	/**
	 * @return the loanAmount
	 */
	public double getLoanAmount()
	{
		return loanAmount;
	}

	/**
	 * @param loanAmount the loanAmount to set
	 */
	public void setLoanAmount(double loanAmount)
	{
		this.loanAmount = loanAmount;
	}

	/**
	 * @return the taxInDecimal
	 */
	public double getTaxInDecimal()
	{
		return taxInDecimal;
	}

	/**
	 * @param taxInDecimal the taxInDecimal to set
	 */
	public void setTaxInDecimal(double taxInDecimal)
	{
		this.taxInDecimal = taxInDecimal;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`name`, `from_account`, `to_account`, `loan_owner`, `debtor`,"
					+ " `total_amount`, `loan_amount`, `amount_ratio`, `tax_in_decimal`, `amount_paid_so_far`, `amount_paid_to_tax`, `interest`,"
					+ " `start_time`, `repeating_time`, `last_time`, `end_time`,"
					+ " `forgiven`, `paused`, `finished`) " 
					+ "VALUES("
					+ "?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getName());
	        ps.setInt(2, getAccountFromID());
	        ps.setInt(3, getAccountToID());
	        ps.setString(4, getOwner().toString());
	        ps.setString(5, getDebtor().toString());
	        ps.setDouble(6, getTotalAmount());
	        ps.setDouble(7, getLoanAmount());
	        ps.setDouble(8, getAmountRatio());
	        ps.setDouble(9, getTaxInDecimal());
	        ps.setDouble(10, getAmountPaidSoFar());
	        ps.setDouble(11, getAmountPaidToTax());
	        ps.setDouble(12, getInterest());
	        ps.setLong(13, getStartTime());
	        ps.setLong(14, getRepeatingTime());
	        ps.setLong(15, getLastTime());
	        ps.setLong(16, getEndTime());
	        ps.setBoolean(17, isForgiven());
	        ps.setBoolean(18, isPaused());
	        ps.setBoolean(19, isFinished());
	        
	        int i = ps.executeUpdate();
	        MysqlBaseHandler.addRows(QueryType.INSERT, i);
	        return true;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not create a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "UPDATE `" + tablename
					+ "` SET `name` = ?, `from_account` = ?, `to_account` = ?,"
					+ " `loan_owner` = ?, `debtor` = ?," 
					+ " `total_amount` = ?, `loan_amount` = ?, `amount_ratio` = ?, `tax_in_decimal` = ?,"
					+ " `amount_paid_so_far` = ?, `amount_paid_to_tax` = ?, `interest` = ?," 
					+ " `start_time` = ?, `repeating_time` = ?, `last_time` = ?, `end_time` = ?," 
					+ " `forgiven` = ?, `paused` = ?, `finished` = ?" 
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getName());
	        ps.setInt(2, getAccountFromID());
	        ps.setInt(3, getAccountToID());
	        ps.setString(4, getOwner().toString());
	        ps.setString(5, getDebtor().toString());
	        ps.setDouble(6, getTotalAmount());
	        ps.setDouble(7, getLoanAmount());
	        ps.setDouble(8, getAmountRatio());
	        ps.setDouble(9, getTaxInDecimal());
	        ps.setDouble(10, getAmountPaidSoFar());
	        ps.setDouble(11, getAmountPaidToTax());
	        ps.setDouble(12, getInterest());
	        ps.setLong(13, getStartTime());
	        ps.setLong(14, getRepeatingTime());
	        ps.setLong(15, getLastTime());
	        ps.setLong(16, getEndTime());
	        ps.setBoolean(17, isForgiven());
	        ps.setBoolean(18, isPaused());
	        ps.setBoolean(19, isFinished());
	        
	        int i = 20;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}			
			int u = ps.executeUpdate();
			MysqlBaseHandler.addRows(QueryType.UPDATE, u);
			return true;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not update a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "SELECT * FROM `" + tablename
				+ "` WHERE "+whereColumn+" ORDER BY "+orderby+limit;
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 1;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}
			
			ResultSet rs = ps.executeQuery();
			MysqlBaseHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new LoanRepayment(
	        			rs.getInt("id"),
	        			rs.getString("name"),
	        			rs.getInt("from_account"),
	        			rs.getInt("to_account"), 
	        			UUID.fromString(rs.getString("loan_owner")),
	        			UUID.fromString(rs.getString("debtor")),
	        			rs.getDouble("totalamount"), 
	        			rs.getDouble("loan_amount"), 
	        			rs.getDouble("amount_ratio"),
	        			rs.getDouble("tax_in_decimal"),
	        			rs.getDouble("amount_paid_so_far"),
	        			rs.getDouble("amount_paid_to_tax"),
	        			rs.getDouble("interest"),
	        			rs.getLong("start_time"), 
	        			rs.getLong("repeating_time"), 
	        			rs.getLong("last_time"), 
	        			rs.getLong("end_time"), 
	        			rs.getBoolean("forgiven"), 
	        			rs.getBoolean("paused"), 
	        			rs.getBoolean("finished")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<LoanRepayment> convert(ArrayList<Object> arrayList)
	{
		ArrayList<LoanRepayment> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof LoanRepayment)
			{
				l.add((LoanRepayment) o);
			}
		}
		return l;
	}
}