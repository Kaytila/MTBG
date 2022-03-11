package net.ck.util.communication.keyboard;

import java.awt.event.ActionEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;


public class EnterAction extends AbstractKeyboardAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public Logger getLogger()
	{
		return logger;
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
		return KeyboardActionType.ENTER;
	}
	
	public boolean isActionimmediately()
	{
		return true;
	}
	
}
