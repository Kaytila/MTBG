package net.ck.game.map;

import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.game.backend.entities.Inventory;
import net.ck.game.graphics.TileTypes;

/**
 * 
 * @author Claus each individual tile, has x and y coordinates, connectors in
 *         each direction and a type
 * 
 */
public class MapTile
{
	
	
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}
		
	private Inventory inventory;
	
	public Inventory getInventory()
	{
		return inventory;
	}

	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}


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

	/**
	 * what maptile is to the east?
	 */
	private MapTile east;
	
	/**
	 * id is just the ID of the maptile, so we have three ways to find a tile:
	 * by ID, by listPosition, by coordinates
	 */
	private int id;
	/**
	 * describes the position in the Map's list, probably easier to handle than to
	 * iterating all the time
	 */
	private int listPosition;

	/**
	 * what is the maptile to the north?
	 */
	private MapTile north;

	/**
	 * describes the position on the map in x and y coordinates
	 * currently also have x and y as separate attributes, not sure yet which to use
	 */
	private Point mapPosition;

	/**
	 * what is the maptile to the south;
	 */
	private MapTile south;

	/**
	 * what type is the tile?
	 */
	private TileTypes type;

	/**
	 * what map tile is to the west?
	 */
	private MapTile west;

	/**
	 * x coordinate, not sure I need that at all
	 * basically position.getX()
	 */
	public int x;

	/**
	 * y coordinate, basically position.getY()
	 */
	public int y;

	public MapTile()
	{
		super();
		inventory = new Inventory();
		setBlocked(false);
	}

	public MapTile getEast()
	{
		return east;
	}

	public int getId()
	{
		return id;
	}

	public int getListPosition()
	{
		return this.listPosition;
	}

	public MapTile getNorth()
	{
		return north;
	}

	public Point getMapPosition()
	{
		return this.mapPosition;
	}

	public MapTile getSouth()
	{
		return south;
	}

	public TileTypes getType()
	{
		return type;
	}

	public MapTile getWest()
	{
		return west;
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

	public void setEast(MapTile east)
	{
		this.east = east;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setListPosition(int listPosition)
	{
		this.listPosition = listPosition;
	}

	public void setNorth(MapTile north)
	{
		this.north = north;
	}

	public void setMapPosition(Point position)
	{
		this.mapPosition = position;
	}

	public void setSouth(MapTile south)
	{
		this.south = south;
	}
	
	public void setType(TileTypes type)
	{
		this.type = type;
	}
	public void setWest(MapTile west)
	{
		this.west = west;
	}
	public void setX(int x)
	{
		this.x = x;
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
	
	
	
	public Logger getLogger()
	{
		return logger;
	}
	



	@Override
	public String toString()
	{
		return "MapTile [targetMap=" + targetMap + ", targetID=" + targetID + ", id=" + id + ", mapPosition=" + mapPosition + ", type=" + type + "]";
	}

	/**
	 * helper method for using same protocol as Point 
	 * @return
	 */
	public int y()
	{
		return getY();
	}
	
	/**
	 * helper method for using same protocol as Point
	 * @return
	 */
	public int x()
	{
		return getX();
	}

	public boolean isBlocked()
	{
		/*if (getType().getType() == TileTypes.OCEAN)
		{
			return true;
		}
		
		if (getType().getType() == TileTypes.MOUNTAIN)
		{
			return true;
		}*/
		return blocked;
	}

	public void setBlocked(boolean blocked)
	{
		this.blocked = blocked;
	}

	public boolean isBlocksLOS()
	{
		
		switch (getType())
		{
			case CASTLEEAST :
				break;
			case CASTLEENTRANCE :
				break;
			case CASTLEWEST :
				break;
			case CAVEENTRANCE :
				break;
			case DESERT :
				break;
			case DIRTFLOOR :
				break;
			case DIRTROAD :
				break;
			case FOUNTAIN :
				break;
			case GATECLOSED :
				break;
			case GATEOPEN :
				break;
			case GRASS :
				break;
			case HILL :
				break;
			case LADDERDOWN :
				break;
			case LADDERUP :
				break;
			case MARBLEFLOOR :
				break;
			case MOUNTAIN :
				break;
			case OCEAN :
				break;
			case PAVEDROAD :
				break;
			case RIVEREE :
				break;
			case RIVEREN :
				break;
			case RIVERES :
				break;
			case RIVERNE :
				break;
			case RIVERNS :
				break;
			case RIVERNW :
				break;
			case RIVERSE :
				break;
			case RIVERSS :
				break;
			case RIVERSW :
				break;
			case RIVERWN :
				break;
			case RIVERWS :
				break;
			case RIVERWW :
				break;
			case STAIRSDOWN :
				break;
			case STAIRSUP :
				break;
			case STONEDOORCLOSED :
				break;
			case STONEDOOROPEN :
				break;
			case STONEFLOOR :
				break;
			case STONEWALL :
				break;
			case STONEWINDOW :
				break;
			case SWAMP :
				break;
			case TOWNENTRANCE :
				break;
			case VILLAGEENTRANCE :
				break;
			case WELL :
				break;
			case WOODDOORCLOSED :
				break;
			case WOODDOOROPEN :
				break;
			case WOODFLOOR :
				break;
			case WOODWALL :
				break;
			case WOODWINDOW :
				break;
			default :
				break;
		
		}
		
		return blocksLOS;
	}

	public void setBlocksLOS(boolean blocksLOS)
	{
		this.blocksLOS = blocksLOS;
	}
}
