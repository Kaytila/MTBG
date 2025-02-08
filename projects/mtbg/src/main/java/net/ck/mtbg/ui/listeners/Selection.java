package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.map.ProtoMapTile;

@Log4j2
@Getter
@Setter
public class Selection
{


    @Setter
    private static Object selectedItem;


    public static String getSelectedItem()
    {
        if (selectedItem instanceof FurnitureItem)
        {
            return "FurnitureItem";
        }

        if (selectedItem instanceof NPC)
        {
            return "NPC";
        }

        if (selectedItem instanceof ProtoMapTile)
        {
            return "ProtoMapTile";
        }

        return null;
    }

    /*public static void setSelectedItem(Object selectedItem)
    {
        if (selectedItem instanceof FurnitureItem)
        {

        }

        if (selectedItem instanceof NPC)
        {

        }

        if (selectedItem instanceof ProtoMapTile)
        {

        }
    }
*/

}
