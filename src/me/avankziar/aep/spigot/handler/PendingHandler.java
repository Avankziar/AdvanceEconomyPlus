package me.avankziar.aep.spigot.handler;

import java.util.LinkedHashMap;

import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.general.objects.StandingOrder;

public class PendingHandler
{
	public static LinkedHashMap<String, StandingOrder> standingOrder = new LinkedHashMap<>();
	public static LinkedHashMap<String, LoanRepayment> loanRepayment = new LinkedHashMap<>();
	public static LinkedHashMap<String, LoanRepayment> loanToAccept = new LinkedHashMap<>();
}
