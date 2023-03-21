package net.ck.mtbg.map;

import net.ck.mtbg.backend.entities.LifeForm;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.state.GameState;
import net.ck.mtbg.weather.Weather;
import net.ck.mtbg.weather.WeatherTypes;
import net.ck.mtbg.util.CodeUtils;
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
    private boolean             wrapping;
    private ArrayList<LifeForm> lifeForms;
    private GameState           gameState;

    public Map()
    {
        setName(null);
        setMissiles(new ArrayList<>());
        lifeForms = new ArrayList<>();
    }

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



	@Override
	public String toString()
	{
		return "Map [syncedWeatherSystem=" + syncedWeatherSystem + ", weatherRandomness=" + weatherRandomness + ", weatherSystem=" + weatherSystem + ", wrapping=" + wrapping + " , toString()="
			+ super.toString() +  "]";
	}

	public synchronized ArrayList<LifeForm> getLifeForms()
	{
		if (lifeForms == null)
		{
			lifeForms = new ArrayList<>();
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
