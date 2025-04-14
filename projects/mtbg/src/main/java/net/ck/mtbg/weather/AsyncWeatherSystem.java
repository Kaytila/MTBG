package net.ck.mtbg.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;

@Log4j2
@Getter
@Setter
public class AsyncWeatherSystem extends AbstractWeatherSystem implements Runnable
{


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
