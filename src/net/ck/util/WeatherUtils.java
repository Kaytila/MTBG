package net.ck.util;


import net.ck.game.weather.WeatherTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class WeatherUtils 
{
	private static final Logger logger = LogManager.getLogger(WeatherUtils.class);

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}

	public static Hashtable<WeatherTypes, BufferedImage> getTypes()
	{
		return types;
	}

	public static void setTypes(Hashtable<WeatherTypes, BufferedImage> types)
	{
		WeatherUtils.types = types;
	}

	private static Hashtable<WeatherTypes, BufferedImage> types = null;
	
	
	/**
	 * lazy initializing weather type images in helper class
	 * @param t weather type of the corresponding weather
	 * @return the image for the requested weather type
	 */
	public static BufferedImage getWeatherImage (WeatherTypes t)
	{
		if (getTypes() == null)
		{
			logger.info("initializing weather images");
			types = new Hashtable<WeatherTypes, BufferedImage>();
			for (WeatherTypes wt : WeatherTypes.values())
			{			
				types.put(wt, ImageUtils.loadWeatherImage(wt));
				//logger.info("type: {}, image: {}", wt, types.get(wt));
			}
		}
		return types.get(t);
	}
	
}