package main.java.me.avankziar.aep.spigot.handler;

import java.util.LinkedHashMap;

import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.StandingOrder;

public class PendingHandler
{
	public static LinkedHashMap<String, StandingOrder> standingOrder = new LinkedHashMap<>();
	public static LinkedHashMap<String, LoanRepayment> loanRepayment = new LinkedHashMap<>();
	public static LinkedHashMap<String, LoanRepayment> loanToAccept = new LinkedHashMap<>();
}
