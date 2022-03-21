package net.ck.util.communication.graphics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeatherChangedEvent extends ChangedEvent
{

	private final Logger logger = LogManager.getLogger(getRealClass());
	public final String message;

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}

	public WeatherChangedEvent(String message)
	{
		this.message = message;
	}

	public Logger getLogger()
	{
		return logger;
	}
}
