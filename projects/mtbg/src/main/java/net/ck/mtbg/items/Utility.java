package net.ck.mtbg.items;

import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;

public class Utility extends AbstractItem
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private Double    capacity;
	private Inventory inventory;
	
	public void setType(ArmorTypes valueOf)
    {


    }

    public Utility()
    {
        super();
        inventory = new Inventory();
    }

    public BufferedImage getItemImage()
    {
        return (ImageUtils.loadImage("utilities" + File.separator, getName()));
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
