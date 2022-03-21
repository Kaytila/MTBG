package net.ck.game.map;

import net.ck.game.backend.entities.AbstractEntity;
import net.ck.game.backend.entities.NPC;
import net.ck.game.items.AbstractItem;
import net.ck.game.weather.Weather;
import net.ck.game.weather.WeatherTypes;
import net.ck.util.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.util.ArrayList;

/**
 * Map can be any map in the game but the main Map, so to speak. That is handled by GameMap
 * 
 * additional properties? TBD. so this could be a small indoors map, or a cave, or anything really.
 * 
 * @author Claus
 *
 */
public class Map extends AbstractMap
{

	/**
	 * if the map wraps, calulate the eastern most tiles and connect them to the western most stores the ids
	 */
	private ArrayList<MapTile> eastTiles;

	/**
	 * is it a synchronized weather system or not?
	 */
	private boolean syncedWeatherSystem;

	/**
	 * how random is the weather? are we talking rand 10 or rand 100? the lower the number, the more random the weather is going to be
	 */
	private int weatherRandomness;

	/**
	 * is there a weather system? is the weather system changing on a separate thread or not? lets try for synced and asynched
	 */
	private boolean weatherSystem;

	/**
	 * does the map wrap around or not?
	 */
	private boolean wrapping;

	public boolean isWrapping()
	{
		return wrapping;
	}

	public void setWrapping(boolean wrapping)
	{
		this.wrapping = wrapping;
	}

	public boolean isSyncedWeatherSystem()
	{
		return syncedWeatherSystem;
	}

	public void setSyncedWeatherSystem(boolean syncedWeatherSystem)
	{
		this.syncedWeatherSystem = syncedWeatherSystem;
	}

	public int getWeatherRandomness()
	{
		return weatherRandomness;
	}

	public void setWeatherRandomness(int weatherRandomness)
	{
		this.weatherRandomness = weatherRandomness;
	}

	public Map()
	{
		setName(null);
		setPlayers(new ArrayList<AbstractEntity>());
		setNpcs(new ArrayList<NPC>());
		setTiles(new ArrayList<MapTile>());
	}

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public Logger getLogger()
	{
		return logger;
	}

	public boolean isWeatherSystem()
	{
		return weatherSystem;
	}

	public void setWeatherSystem(boolean weatherSystem)
	{
		this.weatherSystem = weatherSystem;
	}

	/**
	 * initializing the map before use - to make sure all moving parts are set properly.
	 * most of it shuold have already been done by loading the map from XML.
	 */
	public void initialize()
	{
		//should not be necessary, but better be safe than sorry
		if (getItems() == null)
		{
			setItems(new ArrayList<AbstractItem>());
		}

		if (getWeather() == null)
		{
			logger.info("setting weather");
			Weather weather = new Weather();
			if (isWeatherSystem() == true)
			{
				setWeather(weather);
				weather.setType(WeatherTypes.SUN);
				logger.info("setting weather to sun");
			}
			else
			{
				setWeather(weather);
				weather.setType(WeatherTypes.NONE);
				logger.info("setting weather to none");
			}
		}
		else
		{
			if (isWeatherSystem() == true)
			{
				//getWeather().setType(WeatherTypes.SUN);
			}
			else
			{
				getWeather().setType(WeatherTypes.NONE);
			}
		}

		setSize(MapUtils.calculateMapSize(this));

		if (isWrapping())
		{
			logger.info("wrapping map initializing");
			setEastTiles(calculateEasternEdge());
			connectEastTilesToWestTiles(getEastTiles());
		}
		// MapUtils.calculateTileDirections(getTiles());
		//listNPCs();
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
				// logger.info("eastern tile: " + tile.toString());
			}
			// we have a winner
			else
			{
				// logger.info("has no eastern connection: " + tile.toString());
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

	/**
	 * this is a pretty dumb implementation I think but it should work:
	 * 
	 * 1. we have calculated the eastern tiles, now take each tile, check the y coordinates and set the east tile to the one with x=0 and the same y coordinate. also take the x=0, y1=y2 tile and
	 * connect it via west to the easternmost tile
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

	@Override
	public String toString()
	{
		return "Map [syncedWeatherSystem=" + syncedWeatherSystem + ", weatherRandomness=" + weatherRandomness + ", weatherSystem=" + weatherSystem + ", wrapping=" + wrapping + " , toString()="
			+ super.toString() + "]";
	}

	
	public void listNPCs()
	{
		for (NPC n : getNpcs())
		{
			logger.info("npc: {}", n);
		}
	}
	
}
