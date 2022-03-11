package net.ck.game.demos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class FocusEventDemo {



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
