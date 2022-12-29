package net.ck.game.animation;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * factory which decides which appearence class to choose heavy
 * :D:D:D:D:D https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html
 * https://docs.oracle.com/javase/8/javafx/graphics-tutorial/javafx-3d-graphics.htm
 * https://openjfx.io/javadoc/13/ https://gluonhq.com/products/javafx/
 * 
 * @author Claus
 *
 */
public class AnimationSystemFactory
{
	private static final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(AnimationSystemFactory.class));

	/**
	 * If no animations, then there is no animation system, is there?
	 */
	public static AnimationSystem createAnymationSystem()
	{

		if (GameConfiguration.animated == true)
		{
			if (GameConfiguration.animationCycles > 0)
			{
				return new RandomAnimationSystem();
			}
			else
			{
				return new IndividualAnimationSystem();
			}
		}
		return null;

	}

	public AnimationSystemFactory()
	{
	
	}

}
