package net.ck.game.backend.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class Result
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
