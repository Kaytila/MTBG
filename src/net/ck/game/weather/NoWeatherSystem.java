package net.ck.game.weather;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoWeatherSystem extends AbstractWeatherSystem
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

	public Logger getLogger()
	{
		return logger;
	}

	public NoWeatherSystem(int randomness)
	{
		super(0);
		setRandomness(0);
		setSynchronized(false);

	}

	@Override
	public void checkWeather()
	{
		getCurrentWeather().setType(WeatherTypes.NONE);
	}
}
