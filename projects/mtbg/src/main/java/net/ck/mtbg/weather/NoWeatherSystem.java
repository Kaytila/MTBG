package net.ck.mtbg.weather;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoWeatherSystem extends AbstractWeatherSystem
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public Logger getLogger()
	{
		return logger;
	}

	public NoWeatherSystem(int randomness)
	{
		super(0);
		setRandomness(0);
		setSynchronized(false);
		getCurrentWeather().setType(WeatherTypes.NONE);
	}

	@Override
	public void checkWeather()
	{
		getCurrentWeather().setType(WeatherTypes.NONE);
	}
}
