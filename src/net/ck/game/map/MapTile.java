package net.ck.game.map;

import net.ck.game.backend.entities.Inventory;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.graphics.TileTypes;
import net.ck.game.items.FurnitureItem;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Claus each individual tile, has x and y coordinates, connectors in
 * each direction and a type
 */
public class MapTile implements Comparable<MapTile>
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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
    // Evaluation functions
    public int finalCost;
    public int g;
    // Hardcoded heuristic
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


    private int brightenFactor;

    private BufferedImage brightenedImage;

    public MapTile()
    {
        super();
        inventory = new Inventory();
        setBlocked(false);
        this.h = 1;
    }

    public MapTile(int i, int j)
    {
        setX(i);
        setY(j);
        inventory = new Inventory();
        setBlocked(false);
    }

    public int getFinalCost()
    {
        return finalCost;
    }

    public void setFinalCost(int finalCost)
    {
        this.finalCost = finalCost;
    }

    public int getG()
    {
        return g;
    }

    public void setG(int g)
    {
        this.g = g;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public void setInventory(Inventory inventory)
    {
        this.inventory = inventory;
    }

    public String getTargetMap()
    {
        return targetMap;
    }

    public void setTargetMap(String string)
    {
        this.targetMap = string;
    }

    public int getTargetID()
    {
        return targetID;
    }

    public void setTargetID(int targetID)
    {
        this.targetID = targetID;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public Point getMapPosition()
    {
        return this.mapPosition;
    }

    public void setMapPosition(Point position)
    {
        this.mapPosition = position;
    }

    public TileTypes getType()
    {
        return type;
    }

    public void setType(TileTypes type)
    {
        this.type = type;
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

    public void setX(int x)
    {
        this.x = x;
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

    public void setY(int y)
    {
        this.y = y;
    }
	
	
	/*public String toString()
	{
		int northID = -1;
		int eastID = -1;
		int southID = -1;
		int westID = -1;
		if (getNorth() != null)
		{
			northID = getNorth().getId(); 
		}
		if (getEast() != null)
		{
			 eastID = getEast().getId(); 
		}
		if (getSouth() != null)
		{
			 southID = getSouth().getId(); 
		}
		if (getWest() != null)
		{
			 westID = getWest().getId(); 
		}
		return ("id: " + getId() + ", " + "type: " + getType().name() + ", " + "x: " + getX() + ", " + "y: " + getY() + ", " + "north tile id: " + northID + ", " + "east tile id: " + eastID + ", " + "south tile id: " + southID + ", " + "west tile id: " + westID);
	}
*/

    @Override
    public String toString()
    {
        return "MapTile [targetMap=" + targetMap + ", targetID=" + targetID + ", id=" + id + ", mapPosition=" + mapPosition + ", type=" + type + ", blocked=" + blocked + "]";
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

        switch (getType())
        {
            case DESERT:
            case HILL:
            case GRASS:
            case SWAMP:
            case LADDERUP:
            case LADDERDOWN:
            case STAIRSUP:
            case STAIRSDOWN:
            case CASTLEENTRANCE:
            case TOWNENTRANCE:
            case VILLAGEENTRANCE:
            case GATEOPEN:
            case WOODDOOROPEN:
            case STONEDOOROPEN:
            case DIRTROAD:
            case PAVEDROAD:
            case WOODFLOOR:
            case STONEFLOOR:
            case MARBLEFLOOR:
            case DIRTFLOOR:
            case CAVEENTRANCE:
            case LIGHTFOREST:
            case BUSHES:
            case BUSH:
            case DENSEFOREST:
                return blocked;

            case MOUNTAIN:
            case RIVERES:
            case RIVEREE:
            case RIVEREN:
            case RIVERNE:
            case OCEAN:
            case RIVERNS:
            case RIVERNW:
            case RIVERSE:
            case RIVERSS:
            case RIVERSW:
            case RIVERWN:
            case RIVERWS:
            case RIVERWW:
            case CASTLEWEST:
            case CASTLEEAST:
            case STONEWALL:
            case STONEWINDOW:
            case WOODWALL:
            case WOODWINDOW:
            case GATECLOSED:
            case WOODDOORCLOSED:
            case STONEDOORCLOSED:
            case FOUNTAIN:
            case WELL:
            case SHALLOWOCEAN:
            case REEF:
            case LAVA:
            case STEEPMOUNTAIN:
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + getType());
        }
    }

    public void setBlocked(boolean blocked)
    {
        this.blocked = blocked;
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
                return false;

            default:
                logger.error("forgotten a tile type - how? {}", getType().toString());
                return false;
        }
    }

    public void setBlocksLOS(boolean blocksLOS)
    {
        this.blocksLOS = blocksLOS;
    }

    public FurnitureItem getFurniture()
    {
        return furniture;
    }

    public void setFurniture(FurnitureItem furniture)
    {
        logger.info("setting furniture");
        this.furniture = furniture;
        setBlocked(true);
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

    public MapTile getParent()
    {
        return parent;
    }

    public void setParent(MapTile parent)
    {
        this.parent = parent;
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

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public LifeForm getLifeForm()
    {
        return lifeForm;
    }

    public void setLifeForm(LifeForm lifeForm)
    {
        this.lifeForm = lifeForm;
    }

    public int getBrightenFactor()
    {
        return brightenFactor;
    }

    public void setBrightenFactor(int brightenFactor)
    {
        this.brightenFactor = brightenFactor;
    }

    public BufferedImage getBrightenedImage()
    {
        return brightenedImage;
    }

    public void setBrightenedImage(BufferedImage brightenedImage)
    {
        this.brightenedImage = brightenedImage;
    }
}
