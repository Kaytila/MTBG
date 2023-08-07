package net.ck.mtbg.backend.entities.attributes;

import net.ck.mtbg.util.ImageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class AbstractAttribute implements Serializable
{

	private final Logger logger = LogManager.getLogger(this);

	protected AttributeTypes type;
	protected int value;
	private int imageNumber;

	public BufferedImage getImage()
	{
		return ImageManager.getAttributeImages().get(getType());
	}

	@Override
	public String toString()
	{
		return String.valueOf(getValue());
	}

	public int getValue()
	{
		return 0;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public AttributeTypes getType()
	{
		return null;
	}

	public void setType(AttributeTypes type)
	{
		type = null;
	}


	public Logger getLogger()
	{
		return logger;
	}

	public AbstractAttribute()
	{

	}

	public int getImageNumber()
	{
		return imageNumber;
	}

	public void setImageNumber(int imageNumber)
	{
		this.imageNumber = imageNumber;
	}
}
