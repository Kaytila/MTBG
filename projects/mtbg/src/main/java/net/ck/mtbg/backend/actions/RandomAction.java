package net.ck.mtbg.backend.actions;

import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * random action is something world is supposed to do.
 * @author Claus
 *
 */
public class RandomAction extends AbstractAction
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public RandomAction()
	{
		super();

	}

	public KeyboardActionType getType()
	{
		return KeyboardActionType.NULL;
	}
}
