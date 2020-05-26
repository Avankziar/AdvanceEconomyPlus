package main.java.me.avankziar.advanceeconomy.spigot.object;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;

public class BankAccount
{
	private int id;
	private String name;
	private String accountNumber;
	private double balance;
	private String ownerUUID;
	private ArrayList<String> viceUUID;
	private ArrayList<String> memberUUID;

	public BankAccount(int id, String name, String accountNumber, double balance,
			String ownerUUID, ArrayList<String> viceUUID, ArrayList<String> memberUUID)
	{
		setId(id);
		setName(name);
		setaccountNumber(accountNumber);
		setBalance(balance);
		setOwnerUUID(ownerUUID);
		setViceUUID(viceUUID);
		setMemberUUID(memberUUID);
	}
	
	public static BankAccount createBankAccount(AdvanceEconomy plugin, String accountNumber, String name, String ownerUUID)
	{
		BankAccount ba = new BankAccount(0, name, accountNumber,
				0.0, ownerUUID, new ArrayList<String>(), new ArrayList<String>());
		int id = plugin.getMysqlHandler().lastID(MysqlHandler.Type.BANKACCOUNT);
		ba.setId(id);
		return ba;
	}
	
	public static BankAccount getBankAccount(int id)
	{
		BankAccount ba = (BankAccount) AdvanceEconomy.getPlugin().getMysqlHandler().getData(MysqlHandler.Type.BANKACCOUNT, "id", id);
		return ba;
	}
	
	public static BankAccount getBankAccount(String accountNumber)
	{
		BankAccount ba = (BankAccount) AdvanceEconomy.getPlugin().getMysqlHandler().getData(MysqlHandler.Type.BANKACCOUNT, "accountnumber", accountNumber);
		return ba;
	}
	
	public static BankAccount getBankAccountFromName(String name)
	{
		if(!existMoreBankAccountsWithTheSameName(name))
		{
			return null;
		}
		return (BankAccount) AdvanceEconomy.getPlugin().getMysqlHandler().getData(MysqlHandler.Type.BANKACCOUNT, "`bank_name` = ?", name);
	}
	
	public static boolean hasPlayerMoreBankAccountsWithTheSameName(EcoPlayer player)
	{
		for(String number : player.getBankAccountNumber())
		{
			for(String numbers : player.getBankAccountNumber())
			{
				if(getBankAccount(number).getName().equals(getBankAccount(numbers).getName()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean existMoreBankAccountsWithTheSameName(String name)
	{
		int end = AdvanceEconomy.getPlugin().getMysqlHandler().lastID(MysqlHandler.Type.BANKACCOUNT);
		if(AdvanceEconomy.getPlugin().getMysqlHandler().getList(MysqlHandler.Type.BANKACCOUNT,
				"`id`", true, 1, end, "`bank_name` = ?", name).size() >= 2)
		{
			return true;
		}
		return false;
	}
	
	public static ArrayList<BankAccount> convertList(ArrayList<?> list)
	{
		ArrayList<BankAccount> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof BankAccount)
			{
				el.add((BankAccount) o);
			} else
			{
				return null;
			}
		}
		return el;
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

	public String getaccountNumber()
	{
		return accountNumber;
	}

	public void setaccountNumber(String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	public double getBalance()
	{
		return balance;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

	public String getOwnerUUID()
	{
		return ownerUUID;
	}

	public void setOwnerUUID(String ownerUUID)
	{
		this.ownerUUID = ownerUUID;
	}

	public ArrayList<String> getViceUUID()
	{
		return viceUUID;
	}

	public void setViceUUID(ArrayList<String> viceUUID)
	{
		this.viceUUID = viceUUID;
	}

	public ArrayList<String> getMemberUUID()
	{
		return memberUUID;
	}

	public void setMemberUUID(ArrayList<String> memberUUID)
	{
		this.memberUUID = memberUUID;
	}
	
	public static String getFreeBankAccountNumber()
	{
		Type type = EconomySettings.settings.getNumberType();
		switch(type)
		{
		case TWO_DIGITS_TIMES_ONE:
			return TIMES_ONE(99L);
		case TWO_DIGITS_TIMES_TWO:
			return TIMES_TWO(99L);
		case TWO_DIGITS_TIMES_THREE:
			return TIMES_THREE(99L);
		case TWO_DIGITS_TIMES_FOUR:
			return TIMES_FOUR(99L);
		case TWO_DIGITS_TIMES_FIVE:
			return TIMES_FIVE(99L);
		case THREE_DIGITS_TIMES_ONE:
			return TIMES_ONE(999L);
		case THREE_DIGITS_TIMES_TWO:
			return TIMES_TWO(999L);
		case THREE_DIGITS_TIMES_THREE:
			return TIMES_THREE(999L);
		case THREE_DIGITS_TIMES_FOUR:
			return TIMES_FOUR(999L);
		case FOUR_DIGITS_TIMES_ONE:
			return TIMES_ONE(9999L);
		case FOUR_DIGITS_TIMES_TWO:
			return TIMES_TWO(9999L);
		case FOUR_DIGITS_TIMES_THREE:
			return TIMES_THREE(9999L);
		case FIVE_DIGITS_TIMES_ONE:
			return TIMES_ONE(99999L);
		case FIVE_DIGITS_TIMES_TWO:
			return TIMES_TWO(99999L);
		case SIX_DIGITS_TIMES_ONE:
			return TIMES_ONE(999999L);
		case SIX_DIGITS_TIMES_TWO:
			return TIMES_TWO(999999L);
		case SEVEN_DIGITS_TIMES_ONE:
			return TIMES_ONE(9999999L);
		case SEVEN_DIGITS_TIMES_TWO:
			return TIMES_TWO(9999999L);
		case EIGHT_DIGITS_TIMES_ONE:
			return TIMES_ONE(99999999L);
		case NINE_DIGITS_TIMES_ONE:
			return TIMES_ONE(999999999L);
		case TEN_DIGITS_TIMES_ONE:
			return TIMES_ONE(9999999999L);
		case ELEVEN_DIGITS_TIMES_ONE:
			return TIMES_ONE(99999999999L);
		case TWELVE_DIGITS_TIMES_ONE:
			return TIMES_ONE(999999999999L);
		case THRITEEN_DIGITS_TIMES_ONE:
			return TIMES_ONE(9999999999999L);
		case FOURTEEN_DIGITS_TIMES_ONE:
			return TIMES_ONE(99999999999999L);
		case FIFTEEN_DIGITS_TIMES_ONE:
			return TIMES_ONE(999999999999999L);
		}
		return null;
	}
	
	public enum Type
	{
		//Charlenght n*t+(t-1)
		//Maximum Amount of Number 10^(n*t+1)
		TWO_DIGITS_TIMES_ONE,		//2*1+0 = 2  |	00				|  1.000
		TWO_DIGITS_TIMES_TWO,		//2*2+1 = 5  |	00-00			|  100.000
		TWO_DIGITS_TIMES_THREE,		//2*3+2 = 8  |	00-00-00		|  1.000.000
		TWO_DIGITS_TIMES_FOUR,		//2*4+3 = 11 |  00-00-00-00		|  100.000.000
		TWO_DIGITS_TIMES_FIVE,		//2*5+4 = 14 |  00-00-00-00-00	|  10.000.000.000
		
		THREE_DIGITS_TIMES_ONE,		//3*1+0 = 3  |	000				|  1.000
		THREE_DIGITS_TIMES_TWO,		//3*2+1 = 7  |	000-000			|  1.000.000
		THREE_DIGITS_TIMES_THREE,	//3*3+2 = 11 |  000-000-000		|  1.000.000.000
		THREE_DIGITS_TIMES_FOUR,	//3*4+3 = 15 |	000-000-000-000	|  1.000.000.000.000
		
		FOUR_DIGITS_TIMES_ONE,		//4*1+0 = 4  |	0000			|  10.000
		FOUR_DIGITS_TIMES_TWO,		//4*2+1 = 9  |	0000-0000		|  100.000.000
		FOUR_DIGITS_TIMES_THREE,	//4*3+2 = 14 |	0000-0000-0000	|  1.000.000.000.000
		
		FIVE_DIGITS_TIMES_ONE,		//5*1+0 = 5  |	00000			|  100.000
		FIVE_DIGITS_TIMES_TWO,		//5*2+1 = 11 |	00000-00000		|  10.000.000.000
		
		SIX_DIGITS_TIMES_ONE,		//6*1+0 = 6  |	000000			|  1.000.000
		SIX_DIGITS_TIMES_TWO,		//6*2+1 = 13 |	000000-000000	|  1.000.000.000.000
		
		SEVEN_DIGITS_TIMES_ONE,		//7*1+0 = 7  |	0000000			|  10.000.000 
		SEVEN_DIGITS_TIMES_TWO,		//7*2+1 = 15 |	0000000-0000000	|  100.000.000.000.000
		
		EIGHT_DIGITS_TIMES_ONE,		//8*1+0 = 8  |	00000000		|  100.000.000
		NINE_DIGITS_TIMES_ONE,		//9*1+0 = 9  |	000000000		|  1.000.000.000
		TEN_DIGITS_TIMES_ONE,		//10*1+0 = 10|	0000000000		|  10.000.000.000
		ELEVEN_DIGITS_TIMES_ONE,	//11*1+0 = 11|	00000000000		|  100.000.000.000
		TWELVE_DIGITS_TIMES_ONE,	//12*1+0 = 12|	000000000000	|  1.000.000.000.000
		THRITEEN_DIGITS_TIMES_ONE,	//13*1+0 = 13|	0000000000000	|  10.000.000.000.000
		FOURTEEN_DIGITS_TIMES_ONE,	//14*1+0 = 14|  00000000000000	|  100.000.000.000.000
		FIFTEEN_DIGITS_TIMES_ONE;	//15*1+0 = 15|  000000000000000	|  1.000.000.000.000.000
	}
	
	private static String TIMES_ONE(long charamount)
	{
		String accountNumber = "";
		boolean check = false;
		long first = 0;
		while(first<charamount)
		{
			String f = giveZeros(charamount, first);
			String n = f+first;
			if(MatchApi.isBankAccountNumber(n))
			{
				BankAccount ba = BankAccount.getBankAccount(n);
				if(ba == null)
				{
					check = true;
					accountNumber = n;
				}
			}
			first++;
		}
		if(check == false)
		{
			return null;
		}
		return accountNumber;
	}
	
	private static String TIMES_TWO(long charamount)
	{
		String accountNumber = "";
		boolean check = false;
		long first = 0;
		long second = 0;
		while(first<charamount)
		{
			String f = giveZeros(charamount, first);
			while(second<charamount)
			{
				String s = giveZeros(charamount, second);
				String n = f+first+"-"+s+second;
				if(MatchApi.isBankAccountNumber(n))
				{
					BankAccount ba = BankAccount.getBankAccount(n);
					if(ba == null)
					{
						check = true;
						accountNumber = n;
					}
				}
				second++;
			}
			first++;
		}
		if(check == false)
		{
			return null;
		}
		return accountNumber;
	}
	
	private static String TIMES_THREE(long charamount)
	{
		String accountNumber = "";
		boolean check = false;
		long first = 0;
		long second = 0;
		long third = 0;
		while(first<charamount)
		{
			String f = giveZeros(charamount, first);
			while(second<charamount)
			{
				String s = giveZeros(charamount, second);
				while(third<charamount)
				{
					String t = giveZeros(charamount, third);
					String n = f+first+"-"+s+second+"-"+t+third;
					if(MatchApi.isBankAccountNumber(n))
					{
						BankAccount ba = BankAccount.getBankAccount(n);
						if(ba == null)
						{
							check = true;
							accountNumber = n;
						}
					}
					third++;
				}
				second++;
			}
			first++;
		}
		if(check == false)
		{
			return null;
		}
		return accountNumber;
	}
	
	private static String TIMES_FOUR(long charamount)
	{
		String accountNumber = "";
		boolean check = false;
		long first = 0;
		long second = 0;
		long third = 0;
		long four = 0;
		while(first<charamount)
		{
			String f = giveZeros(charamount, first);
			while(second<charamount)
			{
				String s = giveZeros(charamount, second);
				while(third<charamount)
				{
					String t = giveZeros(charamount, third);
					while(four<charamount)
					{
						String fo = giveZeros(charamount, four);
						String n = f+first+"-"+s+second+"-"+t+third+"-"+fo+four;
						if(MatchApi.isBankAccountNumber(n))
						{
							BankAccount ba = BankAccount.getBankAccount(n);
							if(ba == null)
							{
								check = true;
								accountNumber = n;
							}
						}
						four++;
					}
					third++;
				}
				second++;
			}
			first++;
		}
		if(check == false)
		{
			return null;
		}
		return accountNumber;
	}
	
	private static String TIMES_FIVE(long charamount)
	{
		String accountNumber = "";
		boolean check = false;
		long first = 0;
		long second = 0;
		long third = 0;
		long four = 0;
		long five = 0;
		while(first<charamount)
		{
			String f = giveZeros(charamount, first);
			while(second<charamount)
			{
				String s = giveZeros(charamount, second);
				while(third<charamount)
				{
					String t = giveZeros(charamount, third);
					while(four<charamount)
					{
						String fo = giveZeros(charamount, four);
						while(four<charamount)
						{
							String fi = giveZeros(charamount, five);
							String n = f+first+"-"+s+second+"-"+t+third+"-"+fo+four+"-"+fi+five;
							if(MatchApi.isBankAccountNumber(n))
							{
								BankAccount ba = BankAccount.getBankAccount(n);
								if(ba == null)
								{
									check = true;
									accountNumber = n;
								}
							}
							five++;
						}
						four++;
					}
					third++;
				}
				second++;
			}
			first++;
		}
		if(check == false)
		{
			return null;
		}
		return accountNumber;
	}
	
	private static String giveZeros(long charamount, long i)
	{
		String ca = String.valueOf(charamount);//charamount = 999 or 9999 etc
		String ia = String.valueOf(i);
		int cal = ca.length(); // by 999 = 3
		int ial = ia.length();
		
		int j = cal - ial;
		if(j < 15)
		{
			StringUtils.leftPad("", j, '0');
		}
		return null;
	}
}
