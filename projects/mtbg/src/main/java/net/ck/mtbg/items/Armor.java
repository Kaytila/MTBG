package net.ck.mtbg.items;

import net.ck.mtbg.util.utils.CodeUtils;
import net.ck.mtbg.util.utils.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;

public class Armor extends AbstractItem
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	@Override
	public String toString()
	{
		return "Armor [type=" + type + ", position=" + position + ", armorClass=" + armorClass + ", toString()=" + super.toString() + "]";
	}


	private ArmorTypes type;
	private ArmorPositions position;
	private int armorClass;

	public Armor()
	{

	}

	@Override
	public BufferedImage getItemImage()
	{
		return (ImageUtils.loadImage("armor" + File.separator + getType().name(), getPosition().name()));
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
