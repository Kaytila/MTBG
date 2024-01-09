package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.state.GameState;
import net.ck.mtbg.weather.Weather;
import net.ck.mtbg.weather.WeatherTypes;

import java.util.ArrayList;
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

    public Map()
    {
        setName(null);
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



	@Override
	public String toString()
	{
		return "Map [syncedWeatherSystem=" + syncedWeatherSystem + ", weatherRandomness=" + weatherRandomness + ", weatherSystem=" + weatherSystem + ", wrapping=" + wrapping + " , toString()="
			+ super.toString() +  "]";
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
}
