package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.items.FurnitureItem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author Claus each individual tile, has x and y coordinates, connectors in
 * each direction and a type
 */
@Log4j2
@Getter
@Setter
public class MapTile implements Comparable<MapTile>, Serializable
{
    /**
     * x coordinate, not sure I need that at all
     * basically position.getX()
     */
    public int x;
    /**
     * y coordinate, basically position.getY()
     */
    public int y;
    /**
     * parent tile for a* calculation
     */
    public MapTile parent;

    /**
     * Evaluation functions
     */
    public int finalCost;

    /**
     * evaluation function 2
     */
    public int g;

    /**
     * Hardcoded heuristic
     */
    public int h;
    /**
     * what things are piled on the tile?
     */
    private Inventory inventory;
    /**
     * is the tile blocked or not?
     * either via tile type,
     * or because other npc is there
     * or perhaps door or whatever,
     * needs to be mutable due to doors and other things.
     */
    private boolean blocked;

    /**
     * can the tile be opened and closed again?
     */
    private boolean openable;

    /**
     * does this block the LoS?
     */
    private boolean blocksLOS;
    /**
     * this refers to the map the tile points to for using (E)nter
     */
    private String targetMap;
    /**
     * this refers to the ID of the tile
     * this is the tile the exit leads to
     */
    private int targetID;
    /**
     * id is just the ID of the maptile, so we have three ways to find a tile:
     * by ID, by listPosition, by coordinates
     */
    private int id;
    /**
     * describes the position on the map in x and y coordinates
     * currently also have x and y as separate attributes, not sure yet which to use
     */
    private Point mapPosition;
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
    /**
     * how bright is the tile?
     */
    private int brightenFactor;
    /**
     * this is the brightened image that is calculated
     */
    private BufferedImage brightenedImage;
    /**
     * the target coordinates of the exit
     * might be better wrapped in a separate object
     */
    private Point targetCoordinates;
    /**
     * this is the message being displayed for ENTER/LEAVE
     */
    private Message message;
    /**
     * image pre-calculated for the maptile
     */
    private BufferedImage calculatedImage;
    /**
     * has player already discovered the tile
     */
    private boolean discovered = false;
    /**
     * store the scaled image for the map
     */
    private BufferedImage scaledImage;
    /**
     * this handles the door lock.
     * the following values are ok:
     * null not locked
     * 0 locked generally but can be opened with a lockpick
     * 1 locked generally but can be picked or bashed in
     * > 1 refers to the item ID of the key which needs to be used to open the door
     * <p>
     * needs to be handled in map xml
     * the way this is handled also means that an unlocked door cannot be locked again
     * for this would need a second variable to keep the lock state
     */
    private Integer lock;

    /**
     * has the tile been selected in MapEditor?
     * Can perhaps be used to draw the frames in normal canvas as well
     * TODO look into refactoring the selected frame just to the normal paintComponent
     */
    private boolean selected;


    public MapTile()
    {
        super();
        inventory = new Inventory();
        //setBlocked(false);
        this.h = 1;
    }

    public MapTile(int i, int j)
    {
        setX(i);
        setY(j);
        inventory = new Inventory();
        //setBlocked(false);
    }


    /**
     * used for creating the automap
     *
     * @param mapTile - the original map tile which is being copied
     */
    public MapTile(MapTile mapTile)
    {
        this.setId(mapTile.id);
        this.setMapPosition(mapTile.mapPosition);
        this.setFurniture(mapTile.furniture);
        this.setType(mapTile.type);
        this.setCalculatedImage(mapTile.calculatedImage);
        this.setInventory(mapTile.inventory);
        this.setDiscovered(mapTile.discovered);
    }

    public void setDiscovered(boolean discovered)
    {
        if (this.discovered != discovered)
        {
            logger.debug("Maptile {} {}, {}", x, y, discovered);
        }
        this.discovered = discovered;
    }

    public int getX()
    {
        if (getMapPosition() != null)
        {
            return getMapPosition().x;
        }
        else
        {
            return x;
        }
    }

    public int getY()
    {
        if (getMapPosition() != null)
        {
            return getMapPosition().y;
        }
        else
        {
            return y;
        }
    }

    /**
     * helper method for using same protocol as Point
     *
     * @return y coordinate
     */
    public int y()
    {
        return getY();
    }

    /**
     * helper method for using same protocol as Point
     *
     * @return x coordinate
     */
    public int x()
    {
        return getX();
    }

    public boolean isBlocked()
    {
        return switch (getType())
        {
            case DESERT, HILL, GRASS, SWAMP, LADDERUP, LADDERDOWN, STAIRSUP, STAIRSDOWN, CASTLEENTRANCE, TOWNENTRANCE, VILLAGEENTRANCE, GATEOPEN, WOODDOOROPEN, STONEDOOROPEN, DIRTROAD, PAVEDROAD,
                 WOODFLOOR, STONEFLOOR, MARBLEFLOOR, DIRTFLOOR, CAVEENTRANCE, LIGHTFOREST, BUSHES, BUSH, DENSEFOREST -> blocked;
            case MOUNTAIN, OCEAN, CASTLEWEST, CASTLEEAST, STONEWALL, STONEWINDOW, WOODWALL,
                 WOODWINDOW, GATECLOSED, WOODDOORCLOSED, STONEDOORCLOSED, FOUNTAIN, WELL, SHALLOWOCEAN, REEF, LAVA, STEEPMOUNTAIN, SIGNPOST -> true;
            default -> throw new IllegalStateException("Unexpected value in isblocked: " + getType());
        };
    }

    /**
     * @return
     */
    public boolean isOpenable()
    {
        return switch (getType())
        {
            case GATEOPEN, GATECLOSED, WOODDOORCLOSED, STONEDOORCLOSED, WOODDOOROPEN, STONEDOOROPEN -> true;
            case DIRTROAD, WOODFLOOR, STONEFLOOR, MARBLEFLOOR, DIRTFLOOR, CAVEENTRANCE, LIGHTFOREST, BUSHES, BUSH, DENSEFOREST, DESERT, HILL, GRASS, SWAMP, LADDERUP, LADDERDOWN, STAIRSUP, STAIRSDOWN,
                 CASTLEENTRANCE, TOWNENTRANCE, VILLAGEENTRANCE, MOUNTAIN, OCEAN, CASTLEWEST,
                 CASTLEEAST, PAVEDROAD, STONEWALL, STONEWINDOW, WOODWALL,
                 WOODWINDOW, FOUNTAIN, WELL, SHALLOWOCEAN, REEF, LAVA, STEEPMOUNTAIN, SIGNPOST -> false;
            default -> throw new IllegalStateException("Unexpected value in isblocked: " + getType());
        };
    }


    public boolean isBlocksLOS()
    {
        return switch (getType())
        {
            case CASTLEEAST, CASTLEENTRANCE, CASTLEWEST, CAVEENTRANCE, FOUNTAIN, GATECLOSED, MOUNTAIN, STAIRSDOWN, STAIRSUP, STONEDOORCLOSED, STONEWALL, WOODDOORCLOSED, WOODWALL, DENSEFOREST -> true;
            case DESERT, DIRTFLOOR, DIRTROAD, GATEOPEN, GRASS, HILL, LADDERDOWN, LADDERUP, MARBLEFLOOR, OCEAN, PAVEDROAD, STONEDOOROPEN, STONEFLOOR, STONEWINDOW, TOWNENTRANCE, VILLAGEENTRANCE, SWAMP,
                 WELL, WOODDOOROPEN, WOODWINDOW, WOODFLOOR, BUSH, SHALLOWOCEAN, REEF, SIGNPOST -> false;
            default ->
            {
                logger.error("forgotten a tile type in isblockedLOS - this one? {}", getType().toString());
                yield blocksLOS;
            }
        };
    }

    public void setFurniture(FurnitureItem furniture)
    {
        if (GameConfiguration.debugMap == true)
        {
            logger.debug("maptile: {} setting furniture", this.getMapPosition());
        }
        if (furniture != null)
        {

            this.furniture = furniture;
            setBlocked(true);
            if (GameConfiguration.debugMap == true)
            {
                logger.debug("maptile: {} setting furniture - blocking tile", this.getMapPosition());
            }
        }
        else
        {

            this.furniture = null;
            setBlocked(false);
            if (GameConfiguration.debugMap == true)
            {
                logger.debug("maptile: {} unsetting furniture - unblocking tile", this.getMapPosition());
            }
        }
    }

    public void calculateHeuristic(MapTile finalNode)
    {
        this.h = Math.abs(finalNode.getY() - getY()) + Math.abs(finalNode.getX() - getX());
    }

    public void setNodeData(MapTile currentNode, int cost)
    {
        int gCost = currentNode.getG() + cost;
        setParent(currentNode);
        setG(gCost);
        calculateFinalCost();
    }

    public boolean checkBetterPath(MapTile currentNode, int cost)
    {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG())
        {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }


    private void calculateFinalCost()
    {
        //logger.info("g: {}, h: {}", g, h);
        int finalCost = g + h;
        setFinalCost(finalCost);
    }


    public boolean equals(MapTile arg0)
    {
        return this.getId() == arg0.getId();
    }


    @Override
    public int compareTo(MapTile n)
    {
        return Integer.compare(this.finalCost, n.finalCost);
    }


    public synchronized void setLifeForm(LifeForm lifeForm)
    {
        if (lifeForm == null)
        {
            this.setBlocked(false);
        }
        this.lifeForm = lifeForm;
    }

    /**
     * helper method used in making sure you can only drop items in places that are valid for it
     * no inventory on walls and so on
     *
     * @return does the tile have the possibility to have an inventory?
     */
    public boolean hasInventory()
    {
        return switch (getType())
        {
            //deep forest? you wont find it again.
            case CASTLEEAST, CASTLEENTRANCE, CASTLEWEST, CAVEENTRANCE, FOUNTAIN, GATECLOSED, MOUNTAIN, STAIRSDOWN, STAIRSUP, STONEDOORCLOSED, STONEWALL, WOODDOORCLOSED, WOODWALL, GATEOPEN, HILL,
                 LADDERDOWN, LADDERUP, OCEAN, STONEWINDOW, TOWNENTRANCE, VILLAGEENTRANCE, SWAMP, WELL, WOODDOOROPEN, WOODWINDOW, SHALLOWOCEAN, REEF, STONEDOOROPEN, SIGNPOST, LAVA, STEEPMOUNTAIN,
                 DENSEFOREST -> false;
            case DESERT, DIRTFLOOR, DIRTROAD, GRASS, MARBLEFLOOR, PAVEDROAD, STONEFLOOR, WOODFLOOR, BUSH, BUSHES, LIGHTFOREST -> true;
            default ->
            {
                logger.error("forgotten a tile type in hasInventory - this one? {}", getType().toString());
                yield false;
            }
        };
    }


    public void add(AbstractItem item)
    {
        if (hasInventory())
        {
            getInventory().add(item);
        }
    }

    public void paint(Graphics g, int x, int y)
    {
        g.drawImage(getCalculatedImage(), x, y, null);
    }

    /**
     * delegation method, if npc open/closes door, the tile actually will need to switch its type.
     * this should be handled on the tile, not by the npc
     */
    public void switchTileType()
    {
        switch (getType())
        {
            case GATEOPEN -> setType(TileTypes.GATECLOSED);
            case WOODDOOROPEN -> setType(TileTypes.WOODDOORCLOSED);
            case STONEDOOROPEN -> setType(TileTypes.STONEDOORCLOSED);
            case GATECLOSED -> setType(TileTypes.GATEOPEN);
            case WOODDOORCLOSED -> setType(TileTypes.WOODDOOROPEN);
            case STONEDOORCLOSED -> setType(TileTypes.STONEDOOROPEN);
            default -> logger.info("cannot switch the tile type, do nothing");
        }
    }

    /**
     * helper method for checking lock state
     *
     * @return whether the tile is locked
     */
    public boolean isLocked()
    {
        if (lock == null)
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "MapTile{" +
                "x=" + x +
                ", y=" + y +
                ", parent=" + parent +
                ", finalCost=" + finalCost +
                ", g=" + g +
                ", h=" + h +
                ", inventory=" + inventory +
                ", blocked=" + blocked +
                ", blocksLOS=" + blocksLOS +
                ", targetMap='" + targetMap + '\'' +
                ", targetID=" + targetID +
                ", id=" + id +
                ", mapPosition=" + mapPosition +
                ", type=" + type +
                ", furniture=" + furniture +
                ", hidden=" + hidden +
                ", lifeForm=" + lifeForm +
                ", brightenFactor=" + brightenFactor +
                ", brightenedImage=" + brightenedImage +
                ", targetCoordinates=" + targetCoordinates +
                ", message=" + message +
                ", calculatedImage=" + calculatedImage +
                '}';
    }

    /**
     * @return <tile>
     * <id>49</id>
     * <type>GRASS</type>
     * <x>9</x>
     * <y>2</y>
     * </tile>
     */
    public String toXML()
    {
        String furniture = "";
        if (getFurniture() != null)
        {
            furniture = "<furniture>" + this.getFurniture().getId() + "</furniture>" + "\n";
        }

        String exit = "";
        if (getTargetCoordinates() != null)
        {
            exit = "<exit>" + "\n"
                    + "<targetCoordinates>" + "\n"
                    + "<x>" + getTargetCoordinates().x + "</x>" + "\n"
                    + "<y>" + getTargetCoordinates().y + "</y>" + "\n"
                    + "</targetCoordinates>" + "\n"
                    + "</exit>" + "\n";
        }

        return "<tile>\n"
                + "<id>" + this.getId() + "</id>" + "\n"
                + "<type>" + this.getType().toString() + "</type>" + "\n"
                + "<x>" + this.x + "</x>" + "\n"
                + "<y>" + this.y + "</y>" + "\n"
                + furniture
                + exit
                + "</tile>\n";
    }
}
