package net.ck.game.weather;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FixedWeatherSystem extends AbstractWeatherSystem
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public FixedWeatherSystem(int randomness, WeatherTypes weatherType)
    {
        logger.info("initializing Fixed Weather System");
        Weather weather = new Weather();
        getCurrentWeather().setType(WeatherTypes.SUN);
        setCurrentWeather(weather);
        setRandomness(0);
        setSynchronized(false);
        getCurrentWeather().setType(weatherType);
    }


    @Override
    public void checkWeather()
    {
        getCurrentWeather().setType(getCurrentWeather().getType());
    }
}
