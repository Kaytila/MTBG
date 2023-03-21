package net.ck.mtbg.items;

import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageUtils;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;

public class Weapon extends AbstractItem
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	@Override
	public String toString()
	{
		//return "Weapon [type=" + type + ", damageType=" + damageType + ", weaponDamage=" + weaponDamage + ", averageDamage=" + averageDamage + ", toString()=" + super.toString() + "]";
		return "Weapon: " + super.toString();
	}


	private WeaponTypes type;
	private WeaponDamageTypes damageType;
	private Range<Integer> weaponDamage;
	private double averageDamage;

	private int range;

	public int getRange()
	{
		if (getType().equals(WeaponTypes.MELEE))
		{
			return 1;
		}
		return range;
	}

	public void setRange(int range)
	{
		this.range = range;
	}

	public Weapon()
	{
		setContainer(false);
	}

	public Weapon(int iD)
	{
		setContainer(false);
	}

	public WeaponTypes getType()
	{
		return type;
	}

	public void setType(WeaponTypes type)
	{
		this.type = type;
	}

    public double getAverageDamage()
    {
        return averageDamage;
    }

    public void setAverageDamage(double averageDamage)
    {
        this.averageDamage = averageDamage;
    }


    @Override
    public BufferedImage getItemImage()
    {
        return (ImageUtils.loadImage("weapons" + File.separator, getName()));
    }

    public Range<Integer> getWeaponDamage()
    {
        return weaponDamage;
    }

    public void setWeaponDamage(Range<Integer> weaponDamage)
    {
        this.weaponDamage = weaponDamage;
    }

	public WeaponDamageTypes getDamageType()
	{
		return damageType;
	}

	public void setDamageType(WeaponDamageTypes damageType)
	{
		this.damageType = damageType;
	}
}
