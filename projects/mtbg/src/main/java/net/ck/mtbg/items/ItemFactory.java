package net.ck.mtbg.items;

import net.ck.mtbg.backend.state.ItemManager;

public class ItemFactory
{
    public static Weapon createWeapon(int ID)
    {
        return new Weapon(ItemManager.getWeaponList().get(ID));
    }

    public static Armor createArmor(int ID)
    {
        return new Armor(ItemManager.getArmorList().get(ID));
    }

    public static Utility createUtility(int ID)
    {
        return new Utility(ItemManager.getUtilityList().get(ID));
    }

    public static FurnitureItem createFurniture(int ID)
    {
        return new FurnitureItem(ItemManager.getFurnitureList().get(ID));
    }
}
