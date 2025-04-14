package net.ck.mtbg.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;

import java.util.Random;

@Log4j2
@Getter
@Setter
public abstract class AbstractWeatherSystem
{
    boolean isSynchronized;
    int randomness;

    public AbstractWeatherSystem(int randomness)
    {
        logger.info("initializing Abstract Weather System");
        setRandomness(randomness);
        Weather weather = new Weather();
        getCurrentWeather().setType(WeatherTypes.SUN);
        setCurrentWeather(weather);
    }

    protected AbstractWeatherSystem()
    {
    }

    /**
     * on turn rollover, or at x miliseconds, depending on selected implementation check the weather and update accordingly Randomness is set by the game map, so depending on map type, as implemented,
     * the higher the number, the less often the weather will change
     * <p>
     * switch the weather here
     */
    public void switchWeather()
    {
        // logger.info("check weather");
        // logger.info("Thread {} executing checkWeather()", Thread.currentThread().getName());
        if (Game.getCurrent().getCurrentMap().isWeatherSystem())
        {

            Random rand = new Random();
            int number = rand.nextInt(getRandomness());

            // note to self, switch statements are evil, I had completely forgotten
            // that you
            // need breaks;
            switch (number)
            {
                case 0:
                    getCurrentWeather().setType(WeatherTypes.RAIN);
                    break;
                case 1:
                    getCurrentWeather().setType(WeatherTypes.CLOUD);
                    break;
                case 2:
                    getCurrentWeather().setType(WeatherTypes.FOG);
                    break;
                case 3:
                    getCurrentWeather().setType(WeatherTypes.HAIL);
                    break;
                case 4:
                    getCurrentWeather().setType(WeatherTypes.SNOW);
                    break;
                case 5:
                    getCurrentWeather().setType(WeatherTypes.STORM);
                    break;
                case 6:
                    getCurrentWeather().setType(WeatherTypes.SUN);
                    break;
                default:
                    break;
            }
        }
        else
        {
            logger.info("no weather, setting weather to none");
            getCurrentWeather().setType(WeatherTypes.NONE);
        }
    }

    public Weather getCurrentWeather()
    {
        return Game.getCurrent().getCurrentMap().getWeather();
    }


    public void setCurrentWeather(Weather currentWeather)
    {
        Game.getCurrent().getCurrentMap().setWeather(currentWeather);
    }

    /**
     *
     */
    public void checkWeather()
    {
        if (Game.getCurrent().getCurrentMap().isWeatherSystem())
        {
            if (Game.getCurrent().getCurrentMap().getWeather() == null)
            {
                getCurrentWeather().setType(WeatherTypes.NONE);
            }
            else
            {
                //awful but triggers the repaint()
                getCurrentWeather().setType(getCurrentWeather().getType());
            }
        }
        else
        {
            logger.info("no weather, setting weather to none");
            getCurrentWeather().setType(WeatherTypes.NONE);
        }
    }
}
