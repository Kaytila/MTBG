package net.ck.game.weather;

import net.ck.game.map.Map;

/**
 * Factory Class to create the weather system for the Map. If the map has a weather system, return either synchronized or asynchronized weather If the map does not have a weather system, i.e. indoors
 * or under ground, return null for now not the correct way, need to implement a NoWeatherSystem for protocoll depdendency
 * 
 * @author Claus
 *
 */
public class WeatherSystemFactory
{

	public static AbstractWeatherSystem createWeatherSystem(Map gameMap)
	{
		if (gameMap.isWeatherSystem())
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
		// we are indoors!
		else
		{
			return new NoWeatherSystem(0);
		}
	}
}