package net.ck.game.animation;

import net.ck.game.backend.Game;
import net.ck.game.backend.GameConfiguration;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.game.backend.entities.AbstractEntity;
import net.ck.game.graphics.AnimatedRepresentation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.util.Objects;

/**
 * animation system on separate thread, lets see how bad this implementation
 * will be in a year or two or three or more or ever never ?
 *
 * @author Claus
 *
 *
 *         <a href="https://stackoverflow.com/questions/17940329/javafx-for-server-side-image-generation">https://stackoverflow.com/questions/17940329/javafx-for-server-side-image-generation</a>
 *         <a href="https://stackoverflow.com/questions/20370888/generating-image-at-server-side-using-java-fx">https://stackoverflow.com/questions/20370888/generating-image-at-server-side-using-java-fx</a>
 *
 */
public class AnimationSystem implements Runnable
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	/**
	 * 
	 */
	private AnimatedRepresentation app;

	private AbstractEntity player;

	/**
	 *
	 *
	 */
	public AnimationSystem()
	{
		logger.info("initializing Animation system");
	}



	public AbstractEntity getPlayer()
	{
		return player;
	}

	protected int i = 0;

	@Override
	/*
	 * trying to get a kind of fluid animation with updates every 200 ms Problem
	 * is, do you animate each type of object? do you animate each animated
	 * object on its own? i.e. Player p2 = new Player(AnimationSystem); but that
	 * would be a new thread for each - probably not a good idea alternatives:
	 * have one fix animation system and hand in animatedRepresentations?
	 * 
	 */
	public void run()
	{
		while (Game.getCurrent().isRunning() == true)
		{
			// I hate 0 index
			// if I make the standard image part of the animation list, then I
			// don't need the
			// default state at all.
			// that means, 2 animation cycles, no +1 needed.

			// random variant
			// number = rand.nextInt(game.getAnimationCycles());
			// getPlayer().getAppearance().setCurrentImage(app.getAnimationImageList().get(number));

			// fluid variant :DDD
			for (i = 1; i < GameConfiguration.animationCycles; i++)
			{
				try
				{
					getPlayer().getAppearance().setCurrentImage(app.getAnimationImageList().get(i));
				}
				catch (Exception e)
				{
					logger.error("problem setting image");
				}

				try
				{
					Game.getCurrent().getThreadController().sleep(200, ThreadNames.LIFEFORM_ANIMATION);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				if (GameConfiguration.animationCycles - i == 1)
				{
					i = 1;
				}
			}
		}

		logger.info("game no longer running, thread {} is closing hopefully?", "Animation Thread");
	}

	public AnimatedRepresentation getApp()
	{
		return this.app;
	}

	public void setApp(AnimatedRepresentation app)
	{
		this.app = app;
	}

	public void setPlayer(AbstractEntity abstractEntity)
	{
		this.player = abstractEntity;
	}
}
