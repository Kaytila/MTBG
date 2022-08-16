package net.ck.game.animation;

import net.ck.game.backend.Game;
import net.ck.game.backend.GameConfiguration;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.Random;

public class RandomAnimationSystem extends AnimationSystem
{

	private final Logger logger = LogManager.getLogger(getRealClass());

	private final Random rand = new Random();
	private int randomNumber;	
	public int getRandomNumber()
	{
		return randomNumber;
	}

	public void setRandomNumber(int randomNumber)
	{
		this.randomNumber = randomNumber;
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public RandomAnimationSystem()
	{
		// logger.info("initializing RandomAnimationSystem");
	}

	public void run()
	{
		while (Game.getCurrent().isRunning() == true)
		{ // I hate 0 index //if I make the standard
			// image part of the animation list, then I don't need the default
			// state at all.
			// that means, 2 animation cycles, no +1 needed.

			// random variant
			for (LifeForm p : Game.getCurrent().getAnimatedEntities())
			{
				// logger.info("player: {}", p.toString());
				setRandomNumber(rand.nextInt(GameConfiguration.animationCycles));
				// logger.info("rand: {}", getRandomNumber());

				if (p.getAppearance().getCurrentImage() != ((AnimatedRepresentation) p.getAppearance()).getAnimationImageList().get(getRandomNumber()))
				{
					// logger.info("player {} image has changed, pass message on to the bus", p.toString());
					p.getAppearance().setCurrentImage(((AnimatedRepresentation) p.getAppearance()).getAnimationImageList().get(getRandomNumber()));
					if (Game.getCurrent().getController() != null && Game.getCurrent().getController().getFrame().isVisible())
					{
						EventBus.getDefault().post(new AnimatedRepresentationChanged(p));
					}
				}
				else
				{
					// logger.info("same image for player {}", p.toString());
				}
			}

			try
			{
				Game.getCurrent().getThreadController().sleep(2000, ThreadNames.LIFEFORM_ANIMATION);
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

		logger.error("game no longer running, thread {} is closing hopefully?", "Animation Thread");
	}

}
