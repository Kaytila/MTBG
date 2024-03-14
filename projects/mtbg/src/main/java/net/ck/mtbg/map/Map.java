package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.Missile;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.state.GameState;
import net.ck.mtbg.weather.Weather;
import net.ck.mtbg.weather.WeatherTypes;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Map can be any map in the game but the main Map, so to speak. That is handled by GameMap
 * 
 * additional properties? TBD. so this could be a small indoors map, or a cave, or anything really.
 * 
 * @author Claus
 *
 */
@Log4j2
@Getter
@Setter
public class Map extends AbstractMap
{
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
	private CopyOnWriteArrayList<LifeForm> lifeForms;
	private GameState gameState;


	/**
	 * holds the weather on the map. it will either continue running, or it will be frozen
	 * but it will be peristed with the map
	 */
	private Weather weather;

	private String name;

	/**
	 * hold the children
	 */
	private ArrayList<Map> childMaps;

	/**
	 * the parent map - the game map has no parent
	 */
	private String parentMap;

	/**
	 * Point is (x,y), you start top left and move left and then downwards
	 */
	private Point size;

	private Point targetCoordinates;

	/**
	 *
	 */
	public MapTile[][] mapTiles;

	/**
	 * defines standard visibility range in tile rings around the player
	 * 1 =
	 */
	private int visibilityRange;

	/**
	 * how many minutes pass per turn
	 */
	private int minutesPerTurn;

	/**
	 * if the map has a fixed weather, then store it here.
	 */
	private WeatherTypes fixedWeather;

	private ArrayList<Missile> missiles;

    public Map()
    {
        //setName(null);
		setMissiles(new ArrayList<>());
		lifeForms = new CopyOnWriteArrayList<>();
    }

	/**
	 * initializing the map before use - to make sure all moving parts are set properly.
	 * most of it should have already been done by loading the map from XML.
	 */
	public void initialize()
	{
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

		if (isWrapping())
		{
			logger.info("wrapping map initializing");
		}
	}

	public CopyOnWriteArrayList<LifeForm> getLifeForms()
	{
		if (lifeForms == null)
		{
			lifeForms = new CopyOnWriteArrayList<>();
			lifeForms.add(Game.getCurrent().getCurrentPlayer());
		}
		return lifeForms;
	}

	public GameState getGameState() {
		logger.error("getting game state in map{} to {}", name, gameState);
		return gameState;
	}

	public void setGameState(GameState gameState) {
		logger.error("setting game state in map to {}", gameState);
		this.gameState = gameState;
	}

	public String getParentMap() {
		return parentMap;
	}

	public void setParentMap(String parentMap) {
		logger.info("setting parentMap in map {} to {}", name, parentMap);
		this.parentMap = parentMap;
	}

	@Override
	public String toString() {
		return "Map{" +
				"syncedWeatherSystem=" + syncedWeatherSystem +
				", weatherRandomness=" + weatherRandomness +
				", weatherSystem=" + weatherSystem +
				", wrapping=" + wrapping +
				", lifeForms=" + lifeForms +
				", gameState=" + gameState +
				", weather=" + weather +
				", name='" + name + '\'' +
				", childMaps=" + childMaps +
				", parentMap='" + parentMap + '\'' +
				", size=" + size +
				", targetCoordinates=" + targetCoordinates +
				", mapTiles=" + Arrays.toString(mapTiles) +
				", visibilityRange=" + visibilityRange +
				", minutesPerTurn=" + minutesPerTurn +
				", fixedWeather=" + fixedWeather +
				", missiles=" + missiles +
				'}';
	}
}
