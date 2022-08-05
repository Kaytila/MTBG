package net.ck.game.animation;

import net.ck.game.backend.Game;
import net.ck.game.backend.ThreadNames;
import net.ck.util.communication.graphics.BackgroundRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ConcurrentModificationException;
import java.util.Objects;

public class BackgroundAnimationSystem extends IndividualAnimationSystem
{

	private final Logger logger = LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
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

	/**
	 * just trying to iterate over i to get a same looking animation for background images
	 */
	@Override
	public void run()
	{
		while (Game.getCurrent().isRunning() == true)
		{
			int i;
			for (i = 0; i <= Game.getCurrent().getAnimationCycles(); i++)
			{
				setCurrentBackgroundImage(i);
				if (Game.getCurrent().getController() != null && Game.getCurrent().getController().getFrame().isVisible())
				{
					EventBus.getDefault().post(new BackgroundRepresentationChanged(getCurrentBackgroundImage()));
				}
				try
				{
					Game.getCurrent().getThreadController().sleep(1500, ThreadNames.BACKGROUND_ANIMATION);
				}
				catch (ConcurrentModificationException e)
				{
					logger.error("caught ConcurrentModificationException");
				}
                catch (Throwable e)
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
