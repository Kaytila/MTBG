package net.ck.game.animation;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class IndividualAnimationSystem extends AnimationSystem
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    protected int i;



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
            for (i = 1; i < GameConfiguration.animationCycles; i++)
            {
                try
                {
                    getPlayer().getAppearance().setCurrentImage(getApp().getAnimationImageList().get(i));
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

        logger.error("game no longer running, thread {} is closing hopefully?", "Animation Thread");
    }

}
