package net.ck.game.animation;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.entities.LifeFormState;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ConcurrentModificationException;
import java.util.Random;

public class RandomAnimationSystem extends AnimationSystem
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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
                //logger.info("lifeform state: {}", p.getState());
                // if dead, stay corpse, or blood stain
                if (p.getState().equals(LifeFormState.DEAD))
                {
                    p.getAppearance().setCurrentImage(ImageUtils.getBloodstainImage());
                }
                //if unconcious, stay unmoving
                else if (p.getState().equals(LifeFormState.UNCONSCIOUS))
                {
                    p.getAppearance().setCurrentImage(((AnimatedRepresentation) p.getAppearance()).getAnimationImageList().get(0));
                }


                else// (p.getState().equals(LifeFormState.ALIVE))
                {
                    setRandomNumber(rand.nextInt(GameConfiguration.animationCycles));
                    p.getAppearance().setCurrentImage(((AnimatedRepresentation) p.getAppearance()).getAnimationImageList().get(getRandomNumber()));
                }
                if (Game.getCurrent().getController() != null && Game.getCurrent().getController().getFrame().isVisible())
                {
                    EventBus.getDefault().post(new AnimatedRepresentationChanged(p));
                }
            }

            try
            {
                Game.getCurrent().getThreadController().sleep(GameConfiguration.animationLifeformDelay, ThreadNames.LIFEFORM_ANIMATION);
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
