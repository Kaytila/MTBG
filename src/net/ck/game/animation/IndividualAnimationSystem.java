package net.ck.game.animation;

import net.ck.game.backend.Game;
import net.ck.game.backend.ThreadNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class IndividualAnimationSystem extends AnimationSystem
{

	private final Logger logger = LogManager.getLogger(getRealClass());
	protected int i;

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

	public IndividualAnimationSystem()
	{

	}

	/**
	 * this is the version where I am trying to figure out how to get different
	 * AnimatedAppearances with different numbers of images to work in the same
	 * thread TODO TODO
	 */
	@Override
	public void run()
	{
		while (Game.getCurrent().isRunning() == true)
		{
			for (i = 1; i < Game.getCurrent().getAnimationCycles(); i++)
			{
				try
				{
					getPlayer().getAppearance().setCurrentImage(getApp().getAnimationImageList().get(i));
				} catch (Exception e)
				{
					logger.error("problem setting image");
				}

				try
				{
					Game.getCurrent().getThreadController().sleep(200, ThreadNames.LIFEFORM_ANIMATION);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (Game.getCurrent().getAnimationCycles() - i == 1)
				{
					i = 1;
				}
			}
		}

		logger.error("game no longer running, thread {} is closing hopefully?", "Animation Thread");
	}

}
