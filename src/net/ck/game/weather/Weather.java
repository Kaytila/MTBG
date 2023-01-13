package net.ck.game.weather;

import net.ck.game.backend.state.UIStateMachine;
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
public class Weather {
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    /**
     * what type is the current weather?
     */
    private WeatherTypes type;
    /**
     * contains the weather image of the currently decided weather
     */
    private BufferedImage weatherImage;


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

    public void setType(WeatherTypes typ) {
        this.type = typ;
        if (UIStateMachine.isUiOpen()) {
            EventBus.getDefault().post(new WeatherChangedEvent("imageChanged"));
        }
        try {
            setWeatherImage(WeatherUtils.getWeatherImage(type));
        } catch (Exception e) {
            logger.error("image {} does not exist", type);
        }
    }

    @Override
    public String toString()
    {
        return "Weather [type=" + type + ", weatherImage=" + weatherImage + "]";
    }
}
