package net.ck.game.items;

import net.ck.game.backend.entities.Inventory;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class Utility extends AbstractItem
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private Double capacity;
	private Inventory inventory;
	
	public void setType(ArmorTypes valueOf)
    {


    }

    public Utility()
    {
        super();
        inventory = new Inventory();
    }

    @Override
    public BufferedImage getItemImage()
    {
        return null;
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
