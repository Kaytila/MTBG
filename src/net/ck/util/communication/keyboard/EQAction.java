package net.ck.util.communication.keyboard;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

public class EQAction extends AbstractKeyboardAction {





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
		return KeyboardActionType.EQ;
	}
}
