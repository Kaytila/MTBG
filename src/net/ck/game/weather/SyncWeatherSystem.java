package net.ck.game.weather;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SyncWeatherSystem extends AbstractWeatherSystem
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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
