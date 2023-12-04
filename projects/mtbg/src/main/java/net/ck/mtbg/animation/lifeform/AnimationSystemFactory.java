package net.ck.mtbg.animation.lifeform;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

/**
 * factory which decides which appearence class to choose heavy
 * :D:D:D:D:D https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html
 * https://docs.oracle.com/javase/8/javafx/graphics-tutorial/javafx-3d-graphics.htm
 * https://openjfx.io/javadoc/13/ https://gluonhq.com/products/javafx/
 *
 * @author Claus
 */
@Log4j2
public class AnimationSystemFactory
{


    public AnimationSystemFactory()
    {

    }

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
                return new NoAnimationSystem();
            }
        }
        else
        {
            return new NoAnimationSystem();
        }

    }

}
