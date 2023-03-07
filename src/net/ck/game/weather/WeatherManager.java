package net.ck.game.weather;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeatherManager
{
    private static AbstractWeatherSystem weatherSystem;
    private final Logger logger = LogManager.getLogger(WeatherManager.class);

    public static AbstractWeatherSystem getWeatherSystem()
    {
        return weatherSystem;
    }

    public static void setWeatherSystem(AbstractWeatherSystem ws)
    {
        weatherSystem = ws;
    }

}
