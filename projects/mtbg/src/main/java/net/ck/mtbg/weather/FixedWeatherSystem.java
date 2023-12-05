package net.ck.mtbg.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class FixedWeatherSystem extends AbstractWeatherSystem
{
    public FixedWeatherSystem(int randomness, WeatherTypes weatherType) {
        logger.info("initializing Fixed Weather System");
        Weather weather = new Weather();
        getCurrentWeather().setType(WeatherTypes.SUN);
        setCurrentWeather(weather);
        setRandomness(0);
        setSynchronized(false);
        getCurrentWeather().setType(weatherType);
    }


    @Override
    public void checkWeather() {
        getCurrentWeather().setType(getCurrentWeather().getType());
    }
}
