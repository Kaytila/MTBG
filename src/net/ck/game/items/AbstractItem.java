package net.ck.game.items;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class AbstractItem implements Transferable, Serializable
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private ArrayList<Effects> additionalEffects;
	private int id;
	private boolean isContainer;

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

	public abstract BufferedImage getItemImage();

	public Logger getLogger()
	{
		return logger;
	}

	public String getName()
	{		
		return name;
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
		return "AbstractItem [value=" + value + ", weight=" + weight + ", isContainer=" + isContainer + ", id=" + id + ", name=" + name + ", itemImage=" + getItemImage() + "]";
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
		logger.info("getTransferDataFlavors used");
		DataFlavor flavor1 = new DataFlavor(Object.class, "X-test/test; class=<java.lang.Object>; foo=bar");
		DataFlavor flavor2 = new DataFlavor(Object.class, "X-test/test; class=<java.lang.Object>; x=y");
		DataFlavor[] dataFlavor = new DataFlavor[2];
		dataFlavor[0] = flavor1;
		dataFlavor[1] = flavor2;
		return dataFlavor;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{		
		logger.info("this is used");
		return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
	{
		logger.info("this is used");
		return this;
	}	
}
