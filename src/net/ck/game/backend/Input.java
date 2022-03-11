package net.ck.game.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class Input
{
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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
	
}
