package net.ck.game.items;

import net.ck.game.backend.state.ItemManager;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemFactory
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public ItemFactory()
	{

	}

	public static Weapon createWeapon(int ID)
	{
		return (ItemManager.getWeaponList().get(ID));
	}

	public static Armor createArmor(int ID)
	{
		return (ItemManager.getArmorList().get(ID));
	}
}
