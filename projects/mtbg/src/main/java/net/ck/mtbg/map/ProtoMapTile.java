package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.util.utils.ImageUtils;

import javax.swing.*;
import java.io.File;

@Getter
@Setter
@Log4j2
public class ProtoMapTile
{
    /**
     * what type is the tile?
     */
    private TileTypes type;
    /**
     * furniture - items you can take, furniture you can only see
     */
    private FurnitureItem furniture;
    /**
     * is the tile currently hidden from view? If so, dont allow it to be selected.
     */
    private boolean hidden;
    /**
     * there can only be one lifeform on the tile, either player or an npc
     * move the npc to here instead of iterating over all npcs on the map
     * for drawing. for calculating what they are doing keep using getCurrentMap().getLifeForms()
     */
    private LifeForm lifeForm;

    private String name;

    private Icon icon;

    public ProtoMapTile(TileTypes type)
    {
        this.type = type;
        setName(type.name());
        //TODO create icon here for the protomaptile
        setIcon(ImageUtils.createImageIcon(GameConfiguration.miscImages + type.name() + File.separator + "0.png", type.name()));
    }
}
