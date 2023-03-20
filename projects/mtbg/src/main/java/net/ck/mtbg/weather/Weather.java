package net.ck.mtbg.weather;

import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.WeatherUtils;
import net.ck.mtbg.util.communication.graphics.WeatherChangedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author Claus weather will have an impact on things, perhaps? perhaps not, perhaps only graphics
 */
public class Weather implements Serializable
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    /**
     * what type is the current weather?
     */
    private WeatherTypes type;
    /**
     * contains the weather image of the currently decided weather
     */

    /**
     * so yeah, we do not need to dynamically load the weather image again from disk
     * each time we change the weather, do we?
     * We do not load them all in the constructor and cache them.
     * instead we cache them during game startup.
     * <p>
     * unfortunately, I have forgotten the source of the icons fog
     * is from <a href="https://uxwing.com/cloud-fog-icon/">https://uxwing.com/cloud-fog-icon/</a>
     */
    public Weather()
    {
        super();
    }

    public BufferedImage getWeatherImage()
    {
        return WeatherUtils.getWeatherImage(type);
    }


    public WeatherTypes getType()
    {
        return type;
    }

    public void setType(WeatherTypes typ)
    {
        this.type = typ;
        if (UIStateMachine.isUiOpen())
        {
            EventBus.getDefault().post(new WeatherChangedEvent("imageChanged"));
        }
    }

    @Override
    public String toString()
    {
        return "Weather [type=" + type + "]";
    }
}
