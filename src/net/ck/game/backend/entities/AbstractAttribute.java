package net.ck.game.backend.entities;

import net.ck.util.GameUtils;
import net.ck.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public abstract class AbstractAttribute
{

	private final Logger logger = LogManager.getLogger(getRealClass());

	protected AttributeTypes type;
	protected int value;
	protected BufferedImage image;
	
	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
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

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public Logger getLogger()
	{
		return logger;
	}

	public AbstractAttribute()
	{
		setImage(Scalr.resize(ImageUtils.loadImage("players" + File.separator + "attributes", getType().toString()), Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, GameUtils.getLineHeight(), GameUtils.getLineHeight(), Scalr.OP_ANTIALIAS));
	}
}
