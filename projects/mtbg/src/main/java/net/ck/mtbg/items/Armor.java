package net.ck.mtbg.items;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public class Armor extends AbstractItem
{

	private ArmorTypes type;
	private ArmorPositions position;
	private int armorClass;

	public Armor()
	{
		this.setContainer(false);
	}

	public Armor(Armor that)
	{
		this(that.getId(), that.getArmorClass(), that.getName(), that.getType(), that.getAdditionalEffects(), that.getValue(), that.getWeight(), that.getPosition());
	}

	public Armor(int id, int armorClass, String name, ArmorTypes type, ArrayList<Effects> additionalEffects, double value, double weight, ArmorPositions position)
	{
		this.setId(id);
		this.setArmorClass(armorClass);
		this.setName(name);
		this.setType(type);
		this.setAdditionalEffects(additionalEffects);
		this.setValue(value);
		this.setWeight(weight);
		this.setPosition(position);
		this.setContainer(false);
	}

	@Override
	public BufferedImage getItemImage()
	{
		return (ImageUtils.loadImage("armor" + File.separator + getType().name(), getPosition().name()));
	}
}
