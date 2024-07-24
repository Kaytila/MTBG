package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.graphics.TileTypes;
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
            case MOUNTAIN, RIVERES, RIVEREE, RIVEREN, RIVERNE, OCEAN, RIVERNS, RIVERNW, RIVERSE, RIVERSS, RIVERSW, RIVERWN, RIVERWS, RIVERWW, CASTLEWEST, CASTLEEAST, STONEWALL, STONEWINDOW, WOODWALL,
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
                 CASTLEENTRANCE, TOWNENTRANCE, VILLAGEENTRANCE, MOUNTAIN, RIVERES, RIVEREE, RIVEREN, RIVERNE, OCEAN, RIVERNS, RIVERNW, RIVERSE, RIVERSS, RIVERSW, RIVERWN, RIVERWS, RIVERWW, CASTLEWEST,
                 CASTLEEAST, PAVEDROAD, STONEWALL, STONEWINDOW, WOODWALL,
                 WOODWINDOW, FOUNTAIN, WELL, SHALLOWOCEAN, REEF, LAVA, STEEPMOUNTAIN, SIGNPOST -> false;
            default -> throw new IllegalStateException("Unexpected value in isblocked: " + getType());
        };
    }


    public boolean isBlocksLOS()
    {
        switch (getType())
        {
            case CASTLEEAST:
            case CASTLEENTRANCE:
            case CASTLEWEST:
            case CAVEENTRANCE:
            case FOUNTAIN:
            case GATECLOSED:
            case MOUNTAIN:
            case STAIRSDOWN:
            case STAIRSUP:
            case STONEDOORCLOSED:
            case STONEWALL:
            case WOODDOORCLOSED:
            case WOODWALL:
            case DENSEFOREST:
                return true;

            case DESERT:
            case DIRTFLOOR:
            case DIRTROAD:
            case GATEOPEN:
            case GRASS:
            case HILL:
            case LADDERDOWN:
            case LADDERUP:
            case MARBLEFLOOR:
            case OCEAN:
            case PAVEDROAD:
            case RIVEREE:
            case RIVEREN:
            case RIVERES:
            case RIVERNE:
            case RIVERNS:
            case RIVERNW:
            case RIVERSE:
            case RIVERSS:
            case RIVERSW:
            case RIVERWN:
            case RIVERWS:
            case RIVERWW:
            case STONEDOOROPEN:
            case STONEFLOOR:
            case STONEWINDOW:
            case TOWNENTRANCE:
            case VILLAGEENTRANCE:
            case SWAMP:
            case WELL:
            case WOODDOOROPEN:
            case WOODWINDOW:
            case WOODFLOOR:
            case BUSH:
            case SHALLOWOCEAN:
            case REEF:
            case SIGNPOST:
                return false;

            default:
                logger.error("forgotten a tile type in isblockedLOS - this one? {}", getType().toString());
                return blocksLOS;
        }
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
     * @return
     */
    public boolean hasInventory()
    {
        switch (getType())
        {
            case CASTLEEAST:
            case CASTLEENTRANCE:
            case CASTLEWEST:
            case CAVEENTRANCE:
            case FOUNTAIN:
            case GATECLOSED:
            case MOUNTAIN:
            case STAIRSDOWN:
            case STAIRSUP:
            case STONEDOORCLOSED:
            case STONEWALL:
            case WOODDOORCLOSED:
            case WOODWALL:
            case GATEOPEN:
            case HILL:
            case LADDERDOWN:
            case LADDERUP:
            case OCEAN:
            case RIVEREE:
            case RIVEREN:
            case RIVERES:
            case RIVERNE:
            case RIVERNS:
            case RIVERNW:
            case RIVERSE:
            case RIVERSS:
            case RIVERSW:
            case RIVERWN:
            case RIVERWS:
            case RIVERWW:
            case STONEWINDOW:
            case TOWNENTRANCE:
            case VILLAGEENTRANCE:
            case SWAMP:
            case WELL:
            case WOODDOOROPEN:
            case WOODWINDOW:
            case SHALLOWOCEAN:
            case REEF:
            case STONEDOOROPEN:
            case SIGNPOST:
            case LAVA:
            case STEEPMOUNTAIN:
                //deep forest? you wont find it again.
            case DENSEFOREST:
                return false;

            case DESERT:
            case DIRTFLOOR:
            case DIRTROAD:
            case GRASS:
            case MARBLEFLOOR:
            case PAVEDROAD:
            case STONEFLOOR:
            case WOODFLOOR:
            case BUSH:
            case BUSHES:
            case LIGHTFOREST:
                return true;

            default:
                logger.error("forgotten a tile type in hasInventory - this one? {}", getType().toString());
                return false;
        }
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
}
