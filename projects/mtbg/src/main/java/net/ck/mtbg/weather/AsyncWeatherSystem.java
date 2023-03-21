package net.ck.mtbg.weather;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsyncWeatherSystem extends AbstractWeatherSystem implements Runnable
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public AsyncWeatherSystem(int randomness)
	{
		super(randomness);
		logger.info("initializing asynchronized Weather");
		setSynchronized(false);
	}

	@Override
	public void run()
	{
		while (Game.getCurrent().isRunning() == true)
		{
			switchWeather();
			try
			{
				for (Thread t : ThreadController.getThreads())
				{
					if (t.getName().equalsIgnoreCase("Weather System Thread"))
					{
						ThreadController.sleep(GameConfiguration.weatherWait, ThreadNames.WEATHER_ANIMATION);
					}
				}

			}
			catch (Exception e)
			{
				logger.error("e {}", e.getMessage());
			}
		}
		logger.info("game no longer running, thread {} is closing hopefully?", "Weather System Thread");
	}
}
