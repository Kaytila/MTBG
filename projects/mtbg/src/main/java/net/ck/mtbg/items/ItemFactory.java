package net.ck.mtbg.items;

import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.util.utils.CodeUtils;
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
