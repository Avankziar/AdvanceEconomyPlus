package me.avankziar.aep.spigot.object.subs;

public class ActionFilterSettings
{
	private String orderer;
	private String category;
	private String comment;
	
	public ActionFilterSettings()
	{
		setCategory(null);
		setOrderer(null);
		setComment(null);
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
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
