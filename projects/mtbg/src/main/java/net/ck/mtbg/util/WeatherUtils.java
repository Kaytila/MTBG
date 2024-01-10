package net.ck.mtbg.util;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.weather.WeatherTypes;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

@Getter
@Setter
@Log4j2
public class WeatherUtils
{

    @Getter
    @Setter
    private static Hashtable<WeatherTypes, BufferedImage> types = null;


    /**
     * lazy initializing weather type images in helper class
     *
     * @param t weather type of the corresponding weather
     * @return the image for the requested weather type
     */
    public static BufferedImage getWeatherImage(WeatherTypes t)
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