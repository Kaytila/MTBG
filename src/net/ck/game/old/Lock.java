package net.ck.game.old;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Lock
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

}
