package net.ck.game.weather;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class SyncWeatherSystem extends AbstractWeatherSystem
{

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

	/**
	 * start with sunny weather always :)
	 */
	public SyncWeatherSystem(int randomness)
	{
		super(randomness);
		logger.error("initializing synchronized Weather");
		setSynchronized(true);
	}

	
	public void getWeather()
	{
		checkWeather();
		logger.info(getCurrentWeather().getType().name());
	}

}
