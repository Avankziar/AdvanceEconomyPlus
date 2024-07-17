package me.avankziar.aep.spigot.object.subs;

public class TrendFilterSettings
{
	private Double firstStand;
	private Double lastStand;
	
	public TrendFilterSettings()
	{
		setFirstStand(null);
		setLastStand(null);
	}

	public Double getFirstStand()
	{
		return firstStand;
	}

	public void setFirstStand(Double firstStand)
	{
		this.firstStand = firstStand;
	}

	public Double getLastStand()
	{
		return lastStand;
	}

	public void setLastStand(Double lastStand)
	{
		this.lastStand = lastStand;
	}
}
