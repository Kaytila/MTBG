package net.ck.game.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import net.ck.util.communication.keyboard.ActionFactory;
import net.ck.util.communication.keyboard.KeyboardActionType;

public class IdleActionListener implements ActionListener{

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

	public IdleActionListener()
	{
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{		
		//logger.info("sending space");
		EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.SPACE));
		//Game.getCurrent().listThreads();
	}

	public Logger getLogger()
	{
		return logger;
	}
}
