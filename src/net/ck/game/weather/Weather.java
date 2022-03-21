package net.ck.game.weather;

import net.ck.util.WeatherUtils;
import net.ck.util.communication.graphics.WeatherChangedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * @author Claus weather will have an impact on things, perhaps? perhaps not, perhaps only graphics
 */
public class Weather
{
    private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

    private WeatherTypes type;
    private BufferedImage weatherImage;


    /**
     * so yeah, we dont need to dynamically load the weather image again from disk each time we change the weather, do we? load them all in the constructor and cache them.
     * <p>
     * unfortuately, I have forgotten the source of the icons fog is from https://uxwing.com/cloud-fog-icon/
     */
    public Weather()
    {
        super();
    }

    public BufferedImage getWeatherImage()
    {
        return this.weatherImage;
    }

    public void setWeatherImage(BufferedImage weatherImage)
    {
        this.weatherImage = weatherImage;
    }

    public WeatherTypes getType()
    {
        return type;
    }

    public void setType(WeatherTypes typ)
    {
        this.type = typ;
        EventBus.getDefault().post(new WeatherChangedEvent("imageChanged"));
        try
        {
            setWeatherImage(WeatherUtils.getWeatherImage(type));
        }
        catch (Exception e)
        {
            logger.error("image {} does not exist", type);
        }
    }

    public Logger getLogger()
    {
        return logger;
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    @Override
    public String toString()
    {
        return "Weather [type=" + type + ", weatherImage=" + weatherImage + "]";
    }
}
