package net.ck.game.weather;

import net.ck.game.backend.game.Game;
import net.ck.util.CodeUtils;
import net.ck.util.WeatherUtils;
import net.ck.util.communication.graphics.WeatherChangedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.image.BufferedImage;

/**
 * @author Claus weather will have an impact on things, perhaps? perhaps not, perhaps only graphics
 */
public class Weather
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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
        if (Game.getCurrent().getController() != null && Game.getCurrent().getController().getFrame().isVisible())
        {
            EventBus.getDefault().post(new WeatherChangedEvent("imageChanged"));
        }
        try
        {
            setWeatherImage(WeatherUtils.getWeatherImage(type));
        }
        catch (Exception e)
        {
            logger.error("image {} does not exist", type);
        }
    }

    @Override
    public String toString()
    {
        return "Weather [type=" + type + ", weatherImage=" + weatherImage + "]";
    }
}
