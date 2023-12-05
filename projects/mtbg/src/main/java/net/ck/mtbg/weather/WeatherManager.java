package net.ck.mtbg.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class WeatherManager {
    @Getter
    @Setter
    private static AbstractWeatherSystem weatherSystem;
}
