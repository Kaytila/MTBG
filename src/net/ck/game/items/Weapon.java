package net.ck.game.items;

import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Weapon extends AbstractItem
{

	@Override
	public String toString()
	{
		//return "Weapon [type=" + type + ", damageType=" + damageType + ", weaponDamage=" + weaponDamage + ", averageDamage=" + averageDamage + ", toString()=" + super.toString() + "]";
		return "Weapon: " + super.toString();
	}

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private WeaponTypes type;
	private WeaponDamageTypes damageType;
	private Range<Integer> weaponDamage;
	private double averageDamage;
	
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

	public Logger getLogger()
	{
		return logger;
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
