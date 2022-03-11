package net.ck.game.animation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.ck.game.backend.Game;

public class IndividualAnimationSystem extends AnimationSystem
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
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

	public IndividualAnimationSystem(Game game)
	{
		super(game);
	}

	/**
	 * this is the version where I am trying to figure out how to get different
	 * AnimatedAppearances with different numbers of images to work in the same
	 * thread TODO TODO
	 */
	@Override
	public void run()
	{
		while (getGame().isRunning() == true)
		{
			for (i = 1; i < getGame().getAnimationCycles(); i++)
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
					getGame().getThreadController().sleep(200, "Animation System Thread");
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (getGame().getAnimationCycles() - i == 1)
				{
					i = 1;
				}
			}
		}

		logger.error("game no longer running, thread {} is closing hopefully?", "Animation Thread");
	}

}
