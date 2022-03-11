package net.ck.game.old;

import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import net.ck.game.backend.Game;
import net.ck.util.communication.keyboard.ActionFactory;
import net.ck.util.communication.keyboard.KeyboardActionType;



public class PressedTimerTask extends TimerTask {

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

	public PressedTimerTask()
	{		
	}

	@Override
	public void run()
	{
		while (Game.getCurrent().isRunning() == true)
		{			
			if (Game.getCurrent().isMoved() == true)
			{
				//cancel();				
			}
			else
			{
				logger.info("sending space event");
				EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.SPACE));				
			}
			cancel();
		}
	}
}
