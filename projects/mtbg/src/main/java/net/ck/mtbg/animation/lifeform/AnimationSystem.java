package net.ck.mtbg.animation.lifeform;

import lombok.extern.log4j.Log4j2;

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
@Log4j2
public abstract class AnimationSystem implements Runnable
{
    public AnimationSystem()
    {
        logger.info("initializing Animation system");
    }
}
