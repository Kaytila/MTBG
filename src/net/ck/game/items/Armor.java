package net.ck.game.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Armor extends AbstractItem
{

	@Override
	public String toString()
	{
		return "Armor [type=" + type + ", position=" + position + ", armorClass=" + armorClass + ", toString()=" + super.toString() + "]";
	}

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private ArmorTypes type;
	private ArmorPositions position;
	private int armorClass;

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public Logger getLogger()
	{
		return logger;
	}
	public Armor()
	{

	}

	public ArmorTypes getType()
	{
		return type;
	}

	public void setType(ArmorTypes type)
	{
		this.type = type;
	}

	public ArmorPositions getPosition()
	{
		return position;
	}

	public void setPosition(ArmorPositions position)
	{
		this.position = position;
	}

	public int getArmorClass()
	{
		return armorClass;
	}

	public void setArmorClass(int armorClass)
	{
		this.armorClass = armorClass;
	}
}
