package net.ck.mtbg.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class NoWeatherSystem extends AbstractWeatherSystem
{
	public NoWeatherSystem(int randomness) {
		super(0);
		setRandomness(0);
		setSynchronized(false);
		getCurrentWeather().setType(WeatherTypes.NONE);
	}

	@Override
	public void checkWeather() {
		getCurrentWeather().setType(WeatherTypes.NONE);
	}
}
