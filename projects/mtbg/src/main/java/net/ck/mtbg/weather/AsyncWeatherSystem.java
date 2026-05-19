package net.ck.mtbg.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import java.util.concurrent.locks.LockSupport;

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
            // Non-blocking sleep instead of ThreadController.sleep()
            LockSupport.parkNanos(GameConfiguration.weatherWait * 1_000_000L);

        }
        logger.info("game no longer running, thread {} is closing hopefully?", "Weather System Thread");
    }
}
