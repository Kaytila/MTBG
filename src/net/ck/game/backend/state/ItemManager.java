package net.ck.game.backend.state;

import net.ck.game.items.Armor;
import net.ck.game.items.FurnitureItem;
import net.ck.game.items.Utility;
import net.ck.game.items.Weapon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Hashtable;

/**
 * ItemManager takes care of all different types of items
 * they do not need to be stored in Game, as they are all loaded again with each game start.
 * what needs to be serialized is what PC is carrying, and what is strewn on the map.
 */
public class ItemManager
{
    private static final Logger logger = LogManager.getLogger(ItemManager.class);

    /**
     * list that contains all utility items
     */
    private static Hashtable<Integer, Utility> utilityList;
    /**
     * list that contains all furniture items
     */
    private static Hashtable<Integer, FurnitureItem> furnitureList;
    /**
     * so weaponList has two entries now, but as these are referenced, I need to make an item factory which gets a prototype from the list and creates a new instance with the values instead.
     * weapon list get returns ID, not position.
     */
    private static Hashtable<Integer, Weapon> weaponList;
    /**
     * this is the list of all armor items that exist, ids will need to match the npc equipment. how much will this be used? not really sure
     */
    private static Hashtable<Integer, Armor> armorList;

    public static Hashtable<Integer, Utility> getUtilityList()
    {
        return utilityList;
    }

    public static void setUtilityList(Hashtable<Integer, Utility> utilityList)
    {
        ItemManager.utilityList = utilityList;
    }

    public static Hashtable<Integer, FurnitureItem> getFurnitureList()
    {
        return furnitureList;
    }

    public static void setFurnitureList(Hashtable<Integer, FurnitureItem> furnitureList)
    {
        ItemManager.furnitureList = furnitureList;
    }

    public static Hashtable<Integer, Weapon> getWeaponList()
    {
        return weaponList;
    }

    public static void setWeaponList(Hashtable<Integer, Weapon> weaponList)
    {
        ItemManager.weaponList = weaponList;
    }

    public static Hashtable<Integer, Armor> getArmorList()
    {
        return armorList;
    }

    public static void setArmorList(Hashtable<Integer, Armor> armorList)
    {
        ItemManager.armorList = armorList;
    }
}
