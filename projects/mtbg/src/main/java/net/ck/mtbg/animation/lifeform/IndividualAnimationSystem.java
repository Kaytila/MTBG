package net.ck.mtbg.animation.lifeform;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;

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

                try
                {
                    ThreadController.sleep(200, ThreadNames.LIFEFORM_ANIMATION);
                }
                catch (Exception e)
                {
                    logger.error("exception during sleeping thread");
                }

                if (GameConfiguration.animationCycles - i == 1)
                {
                    i = 1;
                }
            }
        }

        logger.error("game no longer running, thread {} is closing hopefully?", "Animation Thread");
    }

}
