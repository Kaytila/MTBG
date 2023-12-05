package net.ck.mtbg.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.Map;

/**
 * Factory Class to create the weather system for the Map. If the map has a weather system, return either synchronized or asynchronized weather If the map does not have a weather system, i.e. indoors
 * or under ground, return null for now not the correct way, need to implement a NoWeatherSystem for protocoll depdendency
 *
 * @author Claus
 */
@Getter
@Setter
@Log4j2
public class WeatherSystemFactory
{
    public static AbstractWeatherSystem createWeatherSystem(Map gameMap)
    {
        if (gameMap.isWeatherSystem())
        {
            if (gameMap.getFixedWeather() != null)
            {
                return new FixedWeatherSystem(0, gameMap.getFixedWeather());
            }
            else
            {
                if (gameMap.isSyncedWeatherSystem())
                {
                    return new SyncWeatherSystem(gameMap.getWeatherRandomness());
                }
                else
                {
                    return new AsyncWeatherSystem(gameMap.getWeatherRandomness());
                }
            }
        }
        else
        {
            return new FixedWeatherSystem(0, gameMap.getFixedWeather());
        }
    }
}