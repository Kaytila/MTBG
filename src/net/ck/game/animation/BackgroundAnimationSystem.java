package net.ck.game.animation;

import net.ck.game.backend.Game;
import net.ck.util.communication.graphics.BackgroundRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ConcurrentModificationException;

public class BackgroundAnimationSystem extends IndividualAnimationSystem
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

	public Logger getLogger()
	{
		return logger;
	}

	public BackgroundAnimationSystem()
	{
		setCurrentBackgroundImage(0);
	}

	private int currentBackgroundImage;
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
				setCurrentBackgroundImage(i);
				EventBus.getDefault().post(new BackgroundRepresentationChanged(getCurrentBackgroundImage()));

				try
				{
					Game.getCurrent().getThreadController().sleep(1500, "Background Animation System Thread");
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

	public int getCurrentBackgroundImage()
	{
		return currentBackgroundImage;
	}

	public void setCurrentBackgroundImage(int currentBackgroundImage)
	{
		this.currentBackgroundImage = currentBackgroundImage;
	}
}
