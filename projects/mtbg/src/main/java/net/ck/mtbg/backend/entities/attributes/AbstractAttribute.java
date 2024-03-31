package net.ck.mtbg.backend.entities.attributes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.utils.ImageManager;

import java.awt.image.BufferedImage;
import java.io.Serializable;

@Log4j2
@Getter
@Setter
public abstract class AbstractAttribute implements Serializable
{
	/**
	 * which attribute type is it?
	 */
	protected AttributeTypes type;

	/**
	 * what is the value?
	 */
	protected int value;

	/**
	 * what is the image number?
	 */
	private int imageNumber;

	public AbstractAttribute()
	{

	}

	public BufferedImage getImage()
	{
		return ImageManager.getAttributeImages().get(getType());
	}

	@Override
	public String toString()
	{
		return String.valueOf(getValue());
	}


}
