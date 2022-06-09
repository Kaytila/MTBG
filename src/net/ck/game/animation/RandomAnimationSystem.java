package net.ck.game.animation;

import net.ck.game.backend.Game;
import net.ck.game.backend.entities.AbstractEntity;
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

	private Random rand = new Random();
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
		{ // i hate 0 index //if I make the standard
			// image part of the animation list, then I dont need the default
			// state at all.
			// that means, 2 animation cycles, no +1 needed.

			// random variant
			for (AbstractEntity p : Game.getCurrent().getAnimatedEntities())
			{
				// logger.info("player: {}", p.toString());
				setRandomNumber(rand.nextInt(Game.getCurrent().getAnimationCycles()));
				// logger.info("rand: {}", getRandomNumber());

				if (p.getAppearance().getCurrentImage() != ((AnimatedRepresentation) p.getAppearance()).getAnimationImageList().get(getRandomNumber()))
				{
					// logger.info("player {} image has changed, pass message on to the bus", p.toString());
					p.getAppearance().setCurrentImage(((AnimatedRepresentation) p.getAppearance()).getAnimationImageList().get(getRandomNumber()));
					if (Game.getCurrent().getController() != null)
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
				Game.getCurrent().getThreadController().sleep(200, "Animation System Thread");
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
