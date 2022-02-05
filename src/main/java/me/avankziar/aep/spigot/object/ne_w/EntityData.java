package main.java.me.avankziar.aep.spigot.object.ne_w;

import java.util.UUID;

import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity;

public class EntityData
{
	private UUID uuid;
	private String name;
	private EconomyEntity.EconomyType type;
	
	public EntityData(UUID uuid, String name, EconomyEntity.EconomyType type)
	{
		setUUID(uuid);
		setName(name);
		setType(type);
	}

	public UUID getUUID()
	{
		return uuid;
	}

	public void setUUID(UUID uuid)
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

	public EconomyEntity.EconomyType getType()
	{
		return type;
	}

	public void setType(EconomyEntity.EconomyType type)
	{
		this.type = type;
	}
}