package net.ck.game.backend.actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.util.communication.keyboard.KeyboardActionType;

/**
 * random action is something world is supposed to do.
 * @author Claus
 *
 */
public class RandomAction extends AbstractAction
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
	
	public RandomAction()
	{
		super();

	}
	
	public KeyboardActionType getType()
	{
		return KeyboardActionType.NULL;
	}

	public Logger getLogger()
	{
		return logger;
	}
	
}
