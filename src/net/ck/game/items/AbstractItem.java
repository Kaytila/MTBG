package net.ck.game.items;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractItem implements Transferable
{
	private ArrayList<Effects> additionalEffects;
	private int id;
	private boolean isContainer;
	private BufferedImage itemImage;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private String name;
	private double value;
	private double weight;
	private Point mapPosition;
	
	public AbstractItem()
	{
		
	}
	
	public ArrayList<Effects> getAdditionalEffects()
	{
		return additionalEffects;
	}
	
	public int getId()
	{
		return id;
	}

	public BufferedImage getItemImage()
	{
		return itemImage;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public String getName()
	{		
		return name;
	}

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
	public double getValue()
	{
		return value;
	}

	public double getWeight()
	{
		return weight;
	}

	public boolean isContainer()
	{
		return isContainer;
	}

	public void setAdditionalEffects(ArrayList<Effects> additionalEffects)
	{
		this.additionalEffects = additionalEffects;
	}

	public void setContainer(boolean isContainer)
	{
		this.isContainer = isContainer;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setItemImage(BufferedImage itemImage)
	{
		this.itemImage = itemImage;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	public void setWeight(double weight)
	{
		this.weight = weight;
	}

	@Override
	public String toString()
	{
		return "AbstractItem [value=" + value + ", weight=" + weight + ", isContainer=" + isContainer + ", id=" + id + ", name=" + name + ", itemImage=" + itemImage + "]";
	}

	public Point getMapPosition()
	{
		return mapPosition;
	}

	public void setMapPosition(Point mapPosition)
	{
		this.mapPosition = mapPosition;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors()
	{

		DataFlavor flavor1 = new DataFlavor(Object.class, "X-test/test; class=<java.lang.Object>; foo=bar");
		DataFlavor flavor2 = new DataFlavor(Object.class, "X-test/test; class=<java.lang.Object>; x=y");
		DataFlavor[] dataFlavor = new DataFlavor[1];
		dataFlavor[0] = flavor1;
		dataFlavor[1] = flavor2;
		return dataFlavor;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{		
		return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		return this;
	}	
}
