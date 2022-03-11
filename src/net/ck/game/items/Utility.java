package net.ck.game.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.entities.Inventory;

public class Utility extends AbstractItem
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private Double capacity;
	private Inventory inventory;
	
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

	public void setType(ArmorTypes valueOf)
	{
		
		
	}

	public Utility()
	{
		super();
		inventory = new Inventory();
	}

	public Double getCapacity()
	{
		return capacity;
	}

	public void setCapacity(Double capacity)
	{
		this.capacity = capacity;
	}

	public Inventory getInventory()
	{
		return inventory;
	}

	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}
}
