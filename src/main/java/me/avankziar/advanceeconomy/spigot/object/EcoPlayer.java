package main.java.me.avankziar.advanceeconomy.spigot.object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

import main.java.me.avankziar.advanceeconomy.spigot.database.MysqlHandler;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;

public class EcoPlayer
{
	private int id;
	private String uuid;
	private String name;
	private double balance;
	private boolean moneyPlayerFlow;
	private boolean moneyBankFlow;
	private boolean generalMessage;
	private String pendingInvite;
	private List<String> bankAccountNumber;
	
	public EcoPlayer(int id, String uuid, String name,
			double balance, List<String> bankAccountNumber,
			boolean moneyPlayerFlow, boolean moneyBankFlow, boolean generalMessage,
			String pendingInvite)
	{
		setId(id);
		setUUID(uuid);
		setName(name);
		setBalance(balance);
		setBankAccountNumber(bankAccountNumber);
		setMoneyPlayerFlow(moneyPlayerFlow);
		setMoneyBankFlow(moneyBankFlow);
		setGeneralMessage(generalMessage);
		setPendingInvite(pendingInvite);
	}
	
	public static EcoPlayer getEcoPlayer(String playeruuid)
	{
		return (EcoPlayer) AdvanceEconomy.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", playeruuid);
	}
	
	public static EcoPlayer getEcoPlayerFromName(String playername)
	{
		return (EcoPlayer) AdvanceEconomy.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_name` = ?", playername);
	}
	
	public static EcoPlayer getEcoPlayer(OfflinePlayer player)
	{
		return (EcoPlayer) AdvanceEconomy.getPlugin().getMysqlHandler().getData(
				MysqlHandler.Type.PLAYER, "`player_uuid` = ?", player.getUniqueId().toString());
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getUUID()
	{
		return uuid;
	}

	public void setUUID(String uuid)
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public double getBalance()
	{
		return balance;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

	public List<String> getBankAccountNumber()
	{
		return bankAccountNumber;
	}

	public void setBankAccountNumber(List<String> bankAccountNumber)
	{
		this.bankAccountNumber = bankAccountNumber;
	}
	
	public boolean isMoneyPlayerFlow()
	{
		return moneyPlayerFlow;
	}

	public void setMoneyPlayerFlow(boolean moneyPlayerFlow)
	{
		this.moneyPlayerFlow = moneyPlayerFlow;
	}

	public boolean isMoneyBankFlow()
	{
		return moneyBankFlow;
	}

	public void setMoneyBankFlow(boolean moneyBankFlow)
	{
		this.moneyBankFlow = moneyBankFlow;
	}

	public boolean isGeneralMessage()
	{
		return generalMessage;
	}

	public void setGeneralMessage(boolean generalMessage)
	{
		this.generalMessage = generalMessage;
	}

	public String getPendingInvite()
	{
		return pendingInvite;
	}

	public void setPendingInvite(String pendingInvite)
	{
		this.pendingInvite = pendingInvite;
	}
	
	public static ArrayList<EcoPlayer> convertList(ArrayList<?> list)
	{
		ArrayList<EcoPlayer> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof EcoPlayer)
			{
				el.add((EcoPlayer) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
}
