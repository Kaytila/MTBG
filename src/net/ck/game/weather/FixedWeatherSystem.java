package net.ck.game.weather;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class FixedWeatherSystem extends AbstractWeatherSystem
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public FixedWeatherSystem(int randomness, WeatherTypes weatherType)
    {
        super(0);
        setRandomness(0);
        setSynchronized(false);
        getCurrentWeather().setType(weatherType);
    }


    @Override
    public void checkWeather()
    {
        getCurrentWeather().setType(getCurrentWeather().getType());
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

}
