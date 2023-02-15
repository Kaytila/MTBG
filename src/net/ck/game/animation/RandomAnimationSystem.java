package net.ck.game.animation;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.entities.LifeFormState;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.state.UIStateMachine;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * RandomAnimationSystem takes all lifeforms on map and
 */
public class RandomAnimationSystem extends AnimationSystem implements Runnable
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private final Random rand = new Random();

    public RandomAnimationSystem()
    {
        // logger.info("initializing RandomAnimationSystem");
    }

    public void run()
    {
        while (Game.getCurrent().isRunning() == true)
        {
            // random variant
            for (LifeForm p : Game.getCurrent().getCurrentMap().getLifeForms())
            {
                //logger.info("p: {}", p);
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
                    p.getAppearance().setCurrentImage(((AnimatedRepresentation) p.getAppearance()).getAnimationImageList().get(rand.nextInt(GameConfiguration.animationCycles)));
                }
            }

            if (UIStateMachine.isUiOpen())
            {
                EventBus.getDefault().post(new AnimatedRepresentationChanged(null));
            }

            try
            {
                Game.getCurrent().getThreadController().sleep(GameConfiguration.animationLifeformDelay, ThreadNames.LIFEFORM_ANIMATION);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        logger.error("game no longer running, thread {} is closing hopefully?", ThreadNames.LIFEFORM_ANIMATION);
    }

}
