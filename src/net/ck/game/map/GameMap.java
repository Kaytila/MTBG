package net.ck.game.map;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.util.MapUtils;

/**
 * 
 * @author Claus What do I expect of a map? It has an extension, n-s, e-w. does
 *         it wrap? configurable one level only or does it have a parent and
 *         children? weather system? probably configurable
 * 
 *         chunks? good point, not sure whether they are really necessary
 * 
 *         use XML as representation? use SQLite? JPA?
 * 
 *         questions over questions
 *         
 *         GameMap is an extension of map as it has wrapping which Map does not need.
 * 
 */
public class GameMap extends Map
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




	/**
	 * if the map wraps, calulate the eastern most tiles and connect them to the
	 * western most stores the ids
	 */
	private ArrayList<MapTile> eastTiles;

	/**
	 * default constructor, simplest way set all variables somehow, complexity i.e.
	 * loaded maps, random generated maps and so on can come later
	 */
	public GameMap()
	{		
		tiles = new ArrayList<MapTile>();
		// small size
		
		setWeatherSystem(true);
		// is it synchronized, i.e. only checking for change on turn end
		setSyncedWeatherSystem(false);
		// highly instable weather!
		setWeatherRandomness(10);
		setWrapping(true);
		if (isWrapping())
		{
			logger.info("wrapping map initliazing");
			setEastTiles(new ArrayList<MapTile>());
			setEastTiles(calculateEasternEdge());
			connectEastTilesToWestTiles(getEastTiles());
		}
	}

	/**
	 * this is the real constructor, it does not do anything at all all the data is
	 * in the XML file /datastructure. Which reminds me, I need to create an XML
	 * writer for the maps. Perhaps I store those in NoSQL or SQLlite instead of
	 * XML? Hmmm ...
	 * 
	 */


	/**
	 * do I really care about the map size?
	 * as the play constructor is bogus, lets try it this way
	 * 
	 */
	public void initialize()
	{
		setSize(MapUtils.calculateMapSize(this));
		//logger.info("Map size: {}", getSize());
		// is there weather
		if (isWrapping())
		{
			logger.info("wrapping map initliazing");			
			setEastTiles(calculateEasternEdge());
			connectEastTilesToWestTiles(getEastTiles());
		}
		
		MapUtils.calculateTileDirections(getTiles());				
	}

	/**
	 * this is a pretty dumb implementation I think but it should work:
	 * 
	 * 1. we have calculated the eastern tiles, now take each tile, check the y
	 * coordinates and set the east tile to the one with x=0 and the same y
	 * coordinate. also take the x=0, y1=y2 tile and connect it via west to the
	 * easternmost tile
	 * 
	 * @param eastTiles2
	 */
	public void connectEastTilesToWestTiles(ArrayList<MapTile> eastTiles2)
	{
		for (MapTile tile : eastTiles2)
		{
			int y = tile.getY();

			// currently ugly
			for (MapTile ti : getTiles())
			{
				// this is the first row to the left
				if (ti.getX() == 0)
				{
					// this is the actual tile as the y coordinate also fits
					if (ti.getY() == y)
					{
						// connect both t(0,1) ... t(n,1) <-> t(0,1)
						ti.setWest(tile);
						tile.setEast(ti);
					}
				}
			}
		}

	}

	/**
	 * Calculates the eastern edge map tiles
	 * 
	 * @return the list of maptiles which are the eastern edge
	 */
	public ArrayList<MapTile> calculateEasternEdge()
	{
		ArrayList<MapTile> tiles = new ArrayList<MapTile>();

		for (MapTile tile : getTiles())
		{
			// you have an eastern map tile, ignore
			if (tile.getEast() != null)
			{
				//logger.info("eastern tile: " + tile.toString());
			}
			// we have a winner
			else
			{
				//logger.info("has no eastern connection: " + tile.toString());
				tiles.add(tile);
			}
		}

		return tiles;
	}

	

	
	public ArrayList<MapTile> getEastTiles()
	{
		return eastTiles;
	}

	public void setEastTiles(ArrayList<MapTile> eastTiles)
	{
		this.eastTiles = eastTiles;
	}	
}
