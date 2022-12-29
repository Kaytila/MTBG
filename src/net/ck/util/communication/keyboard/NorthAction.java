package net.ck.util.communication.keyboard;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NorthAction extends AbstractKeyboardAction
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	@Override
	public KeyboardActionType getType()
	{
		return KeyboardActionType.NORTH;
	}
	
	public boolean isActionimmediately()
	{
		return true;
	}

	
}
