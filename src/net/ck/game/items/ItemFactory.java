package net.ck.game.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Game;

public class ItemFactory
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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
	public ItemFactory()
	{

	}

	public static Weapon createWeapon(int ID)
	{
		return (Game.getCurrent().getWeaponList().get(ID));
	}

	public static Armor createArmor(int ID)
	{
		return (Game.getCurrent().getArmorList().get(ID));
	}
}
