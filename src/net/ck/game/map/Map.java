package net.ck.game.map;

import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.entities.NPC;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.state.GameState;
import net.ck.game.weather.Weather;
import net.ck.game.weather.WeatherTypes;
import net.ck.util.CodeUtils;
import net.ck.util.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	/**
	 * if the map wraps, calculate the easternmost tiles and connect them to the westernmost stores the ids
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
	 * is there a weather system? is the weather system changing on a separate thread or not? let us try for synced and asynchronous
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

	private ArrayList<LifeForm> lifeForms;

	private GameState gameState;

	public Map()
	{
		setName(null);
		setNpcs(new ArrayList<>());
		setTiles(new ArrayList<>());
		setMissiles(new ArrayList<>());

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
	 * most of it should have already been done by loading the map from XML.
	 */
	public void initialize()
	{
		//should not be necessary, but better be safe than sorry
		if (getItems() == null)
		{
			setItems(new ArrayList<>());
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
			if (isWeatherSystem() != true)
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

		listNPCs();
	}

	/**
	 * Calculates the eastern edge map tiles
	 * 
	 * @return the list of map tiles which are the eastern edge
	 */
	public ArrayList<MapTile> calculateEasternEdge()
	{
		ArrayList<MapTile> tiles = new ArrayList<>();

		for (MapTile tile : getTiles())
		{
			// no tile east, we have found an eastern edge
			if (tile.getEast() == null)
			{
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
	 * this is a pretty dumb implementation, I think, but it should work:
	 * 
	 * 1. we have calculated the eastern tiles, now take each tile, check the y coordinates and set the east tile to the one with x=0 and the same y coordinate. also take the x=0, y1=y2 tile and
	 * connect it via west to the easternmost tile
	 * @param eastTiles2 - array list of all easternmost tiles
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
			+ super.toString() +  "]";
	}

	
	public void listNPCs()
	{
		for (NPC n : getNpcs())
		{
			logger.info("npc: {}", n);
		}
	}

	public ArrayList<LifeForm> getLifeForms()
	{
		if (lifeForms == null)
		{
			lifeForms = new ArrayList<>();
			lifeForms.addAll(getNpcs());
			lifeForms.add(Game.getCurrent().getCurrentPlayer());
		}
		return lifeForms;
	}

	public GameState getGameState()
	{
		return gameState;
	}

	public void setGameState(GameState gameState)
	{
		this.gameState = gameState;
	}
}
