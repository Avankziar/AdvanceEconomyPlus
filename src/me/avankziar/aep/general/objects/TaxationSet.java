package me.avankziar.aep.general.objects;

public class TaxationSet
{
	private boolean taxAreExclusive;
	private double taxInPercent;
	
	public TaxationSet(boolean taxAreExclusive, double taxInPercent)
	{
		setTaxAreExclusive(taxAreExclusive);
		setTaxInPercent(taxInPercent);
	}

	public boolean isTaxAreExclusive()
	{
		return taxAreExclusive;
	}

	public void setTaxAreExclusive(boolean taxAreExclusive)
	{
		this.taxAreExclusive = taxAreExclusive;
	}

	public double getTaxInPercent()
	{
		return taxInPercent;
	}

	public void setTaxInPercent(double taxInPercent)
	{
		this.taxInPercent = taxInPercent;
	}
}
