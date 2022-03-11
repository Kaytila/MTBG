package net.ck.game.animation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import net.ck.game.backend.Game;

/**
 * factory which decides which appearence class to choose heavy TODO here
 * :D:D:D:D:D https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html
 * https://docs.oracle.com/javase/8/javafx/graphics-tutorial/javafx-3d-graphics.htm
 * https://openjfx.io/javadoc/13/ https://gluonhq.com/products/javafx/
 * 
 * @author Claus
 *
 */
public class AnimationSystemFactory
{

	@SuppressWarnings("unused")
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

	/**
	 * If no animations, then there is no animation system, is there?
	 */
	public static AnimationSystem createAnymationSystem(Game game)
	{

		if (game.isAnimated() == true)
		{
			if (game.getAnimationCycles() > 0)
			{
				return new RandomAnimationSystem(game);
			}
			else
			{
				return new IndividualAnimationSystem(game);
			}
		}
		return null;

	}

	public AnimationSystemFactory()
	{
	
	}

}
