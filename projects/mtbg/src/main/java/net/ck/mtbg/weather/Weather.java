package net.ck.mtbg.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.WeatherChangedEvent;
import net.ck.mtbg.util.utils.WeatherUtils;
import org.greenrobot.eventbus.EventBus;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author Claus weather will have an impact on things, perhaps? perhaps not, perhaps only graphics
 */
@Log4j2
@Getter
@Setter
public class Weather implements Serializable
{


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

    public void setType(WeatherTypes typ)
    {
        this.type = typ;
        if (UIStateMachine.isUiOpen())
        {
            if (GameConfiguration.debugEvents == true)
            {
                logger.debug("fire new weather changed event");
            }
            EventBus.getDefault().post(new WeatherChangedEvent("imageChanged"));
        }
    }

    @Override
    public String toString()
    {
        return "Weather [type=" + type + "]";
    }
}
