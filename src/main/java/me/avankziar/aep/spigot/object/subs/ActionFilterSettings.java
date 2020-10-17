package main.java.me.avankziar.aep.spigot.object.subs;

public class ActionFilterSettings
{	
	private String from;
	private String to;
	private String orderer;
	private String comment;
	
	public ActionFilterSettings()
	{
		setFrom(null);
		setTo(null);
		setOrderer(null);
		setComment(null);
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getOrderer()
	{
		return orderer;
	}

	public void setOrderer(String orderer)
	{
		this.orderer = orderer;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

}
