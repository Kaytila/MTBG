package net.ck.game.animation;

import net.ck.game.backend.Game;
import net.ck.util.communication.graphics.ForegroundRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ConcurrentModificationException;

public class ForegroundAnimationSystem extends IndividualAnimationSystem
{

	private int currentForegroundImage;

	public int getCurrentForegroundImage()
	{
		return currentForegroundImage;
	}

	public void setCurrentForegroundImage(int currentForegroundImage)
	{
		this.currentForegroundImage = currentForegroundImage;
	}

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

	public Logger getLogger()
	{
		return logger;
	}

	public ForegroundAnimationSystem()
	{
		setCurrentForegroundImage(0);
	}

	private int i = 0;

	/**
	 * just trying to iterate over i to get a same looking animation for background images
	 */
	@Override
	public void run()
	{
		while (Game.getCurrent().isRunning() == true)
		{
			for (i = 0; i <= Game.getCurrent().getAnimationCycles(); i++)
			{
				setCurrentForegroundImage(i);
				EventBus.getDefault().post(new ForegroundRepresentationChanged(getCurrentForegroundImage()));

				try
				{
					Game.getCurrent().getThreadController().sleep(500, "Foreground Animation System Thread");
				}
				catch (ConcurrentModificationException e)
				{
					logger.error("caught ConcurrentModificationException");
				}
                catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
