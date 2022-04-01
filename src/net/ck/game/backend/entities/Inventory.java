package net.ck.game.backend.entities;

import net.ck.game.items.AbstractItem;
import net.ck.game.items.Armor;
import net.ck.game.items.Weapon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

public class Inventory extends AbstractListModel<AbstractItem> //implements ListModel<AbstractItem>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger logger = LogManager.getLogger(getRealClass());

	private ArrayList<AbstractItem> inventory;
	private double maxWeight;
	private double currentWeight;

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public Logger getLogger()
	{
		return logger;
	}

	public Inventory()
	{
		inventory = new ArrayList<>();
	}

	@Override
	public int getSize()
	{
		return getInventory().size();
	}

	@Override
	public AbstractItem getElementAt(int index)
	{
		return getInventory().get(index);
	}



	public ArrayList<AbstractItem> getInventory()
	{
		return inventory;
	}

	public void setInventory(ArrayList<AbstractItem> inventory)
	{
		this.inventory = inventory;
	}

	/**
	 * https://stackoverflow.com/questions/16575330/jlist-not-updating-with-custom-listmodel
	 * @param item
	 */
	public void remove(AbstractItem item)
	{
		getInventory().remove(item);
		fireIntervalRemoved(this, 0, getSize());
	}

	/**
	 * https://stackoverflow.com/questions/16575330/jlist-not-updating-with-custom-listmodel
	 * @param item
	 */
	public void add(AbstractItem item)
	{
		getInventory().add(item);
		fireIntervalAdded(this, 0, getSize());
	}

	public boolean contains(Armor armor)
	{
		if (getInventory().contains(armor))
		{
			return true;
		}
		return false;
	}

	public boolean contains(Weapon weapon)
	{
		if (getInventory().contains(weapon))
		{
			return true;
		}
		return false;
	}

	public double getCurrentWeight()
	{
		return currentWeight;
	}

	public void setCurrentWeight(double w)
	{
		this.currentWeight = w;
	}

	public double getMaxWeight()
	{
		return maxWeight;
	}

	public void setMaxWeight(double maxWeight)
	{
		this.maxWeight = maxWeight;
	}
	
	
	public void calculateCurrentWeight()
	{
		double w = 0;
		for (AbstractItem i : getInventory())
		{
			w = w + i.getWeight();
		}
		setCurrentWeight(w);
	}
	
	/**
	 * this will take strength into account,
	 * perhaps even items like the space warp wand
	 * or a valet?
	 * or a horse?
	 * carriage?
	 * lets see :D
	 */
	public void calculateMaxWeight()
	{
		double w = 50.0;
		setMaxWeight(w);
	}

	public boolean isEmpty()
	{
		return getInventory().isEmpty();
	}

	public AbstractItem get(int i)
	{
		return getInventory().get(i);
	}


}
