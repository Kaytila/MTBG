package net.ck.mtbg.animation.lifeform;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import java.util.concurrent.locks.LockSupport;

@Log4j2
public class IndividualAnimationSystem extends AnimationSystem implements Runnable
{

    public IndividualAnimationSystem()
    {

    }

    /**
     * this is the version where I am trying to figure out how to get different
     * AnimatedAppearances with different numbers of images to work in the same
     * thread
     */
    @Override
    public void run()
    {
        while (Game.getCurrent().isRunning() == true)
        {
            for (int i = 1; i < GameConfiguration.animationCycles; i++)
            {
                try
                {
                    Game.getCurrent().getCurrentPlayer().setCurrImage(i);
                }
                catch (Exception e)
                {
                    logger.error("problem setting image");
                }

                // Non-blocking sleep instead of ThreadController.sleep()
                LockSupport.parkNanos(200_000_000L); // 200ms in nanoseconds

                if (GameConfiguration.animationCycles - i == 1)
                {
                    i = 1;
                }
            }
        }

        logger.error("game no longer running, thread {} is closing hopefully?", "Animation Thread");
    }

}
