package net.ck.mtbg.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class SyncWeatherSystem extends AbstractWeatherSystem
{


	/**
	 * start with sunny weather always :)
	 */
	public SyncWeatherSystem(int randomness) {
		super(randomness);
		logger.error("initializing synchronized Weather");
		setSynchronized(true);
	}

	public void getWeather() {
		checkWeather();
		logger.info(getCurrentWeather().getType().name());
	}
}
