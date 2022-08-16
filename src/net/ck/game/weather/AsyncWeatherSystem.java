package net.ck.game.weather;

import net.ck.game.backend.threading.ThreadNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.game.backend.Game;

public class AsyncWeatherSystem extends AbstractWeatherSystem implements Runnable
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
				for (Thread t : Game.getCurrent().getThreadController().getThreads())
				{
					if (t.getName().equalsIgnoreCase("Weather System Thread"))
					{
						Game.getCurrent().getThreadController().sleep(10000, ThreadNames.WEATHER_ANIMATION);
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
