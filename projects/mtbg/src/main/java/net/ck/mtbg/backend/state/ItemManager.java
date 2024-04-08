package net.ck.mtbg.backend.state;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.*;

import java.util.Hashtable;

/**
 * ItemManager takes care of all different types of items
 * they do not need to be stored in Game, as they are all loaded again with each game start.
 * what needs to be serialized is what PC is carrying, and what is strewn on the map.
 */
@Log4j2
@Getter
@Setter
public class ItemManager
{
    /**
     * list that contains all utility items
     */
    @Getter
    @Setter
    private static Hashtable<Integer, Utility> utilityList;
    /**
     * list that contains all furniture items
     */
    @Getter
    @Setter
    private static Hashtable<Integer, FurnitureItem> furnitureList;
    /**
     * so weaponList has two entries now, but as these are referenced, I need to make an item factory which gets a prototype from the list and creates a new instance with the values instead.
     * weapon list get returns ID, not position.
     */
    @Getter
    @Setter
    private static Hashtable<Integer, Weapon> weaponList;
    /**
     * this is the list of all armor items that exist, ids will need to match the npc equipment. how much will this be used? not really sure
     */
    @Getter
    @Setter
    private static Hashtable<Integer, Armor> armorList;


    @Getter
    @Setter
    private static Hashtable<Integer, Food> foodList;

}
