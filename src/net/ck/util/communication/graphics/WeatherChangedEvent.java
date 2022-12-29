package net.ck.util.communication.graphics;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeatherChangedEvent extends ChangedEvent
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	public final String message;



	public WeatherChangedEvent(String message)
	{
		this.message = message;
	}

}
