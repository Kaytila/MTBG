package net.ck.util.communication.keyboard;

import java.awt.event.ActionEvent;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

public class WestAction extends AbstractKeyboardAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		//logger.info(getType() + " pressed");
		EventBus.getDefault().post(this);
	}

	@Override
	public KeyboardActionType getType()
	{
		return KeyboardActionType.WEST;
	}

	public Logger getLogger()
	{
		return logger;
	}
	
	public boolean isActionimmediately()
	{
		return true;
	}

}
