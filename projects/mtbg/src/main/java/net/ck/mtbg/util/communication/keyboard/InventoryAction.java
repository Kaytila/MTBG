package net.ck.mtbg.util.communication.keyboard;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InventoryAction extends AbstractKeyboardAction
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	
	@Override
	public KeyboardActionType getType()
	{
		return KeyboardActionType.INVENTORY;
	}
	
	public boolean isActionimmediately()
	{
		return false;
	}

	
}
