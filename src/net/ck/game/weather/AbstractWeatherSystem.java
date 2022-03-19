package net.ck.game.weather;

import net.ck.game.backend.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.util.Objects;
import java.util.Random;

public abstract class AbstractWeatherSystem
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	boolean isSynchronized;
	int randomness;


	public AbstractWeatherSystem(int randomness)
	{
		logger.info("initializing Abstract Weather System");
		setRandomness(randomness);
		Weather weather = new Weather();
		getCurrentWeather().setType(WeatherTypes.SUN);
		setCurrentWeather(weather);
	}

	/**
	 * on turn rollover, or at x miliseconds, depending on selected implementation check the weather and update accordingly Randomness is set by the game map, so depending on map type, as implemented,
	 * the higher the number, the less often the weather will change
	 *
	 * switch the weather here
	 */
	public void switchWeather()
	{
		// logger.info("check weather");
		// logger.info("Thread {} executing checkWeather()", Thread.currentThread().getName());
		if (Game.getCurrent().getCurrentMap().isWeatherSystem())
		{

			Random rand = new Random();
			int number = rand.nextInt(getRandomness());

			// note to self, switch statements are evil, I had completely forgotten
			// that you
			// need breaks;
			switch (number)
			{
				case 0 :
					getCurrentWeather().setType(WeatherTypes.RAIN);
					break;
				case 1 :
					getCurrentWeather().setType(WeatherTypes.CLOUD);
					break;
				case 2 :
					getCurrentWeather().setType(WeatherTypes.FOG);
					break;
				case 3 :
					getCurrentWeather().setType(WeatherTypes.HAIL);
					break;
				case 4 :
					getCurrentWeather().setType(WeatherTypes.SNOW);
					break;
				case 5 :
					getCurrentWeather().setType(WeatherTypes.STORM);
					break;
				case 6 :
					getCurrentWeather().setType(WeatherTypes.SUN);
					break;
				default :
					break;
			}
		}
		else
		{
			logger.info("no weather, setting weather to none");
			getCurrentWeather().setType(WeatherTypes.NONE);
		}
	}

	public Weather getCurrentWeather()
	{
		return Game.getCurrent().getCurrentMap().getCurrentWeather();
	}

	public int getRandomness()
	{
		return randomness;
	}

	public boolean isSynchronized()
	{
		return isSynchronized;
	}

	public void setCurrentWeather(Weather currentWeather)
	{
		Game.getCurrent().getCurrentMap().setCurrentWeather(currentWeather);
	}

	public void setRandomness(int randomness)
	{
		this.randomness = randomness;
	}

	public void setSynchronized(boolean isSynchronized)
	{
		this.isSynchronized = isSynchronized;
	}

	/**
	 *
	 */
	public void checkWeather()
	{
		if (Game.getCurrent().getCurrentMap().isWeatherSystem())
		{
			if (Game.getCurrent().getCurrentMap().getCurrentWeather() == null)
			{
				getCurrentWeather().setType(WeatherTypes.SUN);
			}
			else
			{
				//awful but triggers the repaint()
				getCurrentWeather().setType(getCurrentWeather().getType());
			}
		}
		else
		{
			logger.info("no weather, setting weather to none");
			getCurrentWeather().setType(WeatherTypes.NONE);
		}
	}
}
