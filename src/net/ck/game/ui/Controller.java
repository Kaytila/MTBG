package net.ck.game.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller
{

	private static final long serialVersionUID = 1L;
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

	public Logger getLogger()
	{
		return logger;
	}
	public Controller()
	{

	}
}
