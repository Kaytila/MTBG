package net.ck.game.items;

import net.ck.util.CodeUtils;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

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
        return null;
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
