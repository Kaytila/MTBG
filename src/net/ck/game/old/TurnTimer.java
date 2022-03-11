package net.ck.game.old;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import net.ck.game.backend.Game;
import net.ck.util.communication.keyboard.ActionFactory;
import net.ck.util.communication.keyboard.KeyboardActionType;
public class TurnTimer implements Runnable
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	@Override
	public void run()
	{
		/**
		 * 
		 */
		while (Game.getCurrent().isRunning() == true)
		{
			if (Game.getCurrent().isMoved() == true)
			{
				try
				{
					logger.info("sleep 5 seconds");
					Thread.sleep(5000);
					Game.getCurrent().setMoved(false);					
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					try
					{
						Thread.sleep(5000);
					}
					catch (InterruptedException e1)
					{						
						e1.printStackTrace();
					}
				}
			}
			else
			{
				logger.info("sending space event");
				EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.SPACE));
				Game.getCurrent().setMoved(true);
			}
			try
			{
				logger.info("sleep 5 seconds");
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
