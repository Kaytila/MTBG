package net.ck.game.animation.lifeform;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.entities.LifeFormState;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.game.ui.state.UIStateMachine;
import net.ck.util.CodeUtils;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

/**
 * NoAnimationSystem currently is a bit of a fluke, as it would actually do animation
 * but I just took the code and always kept it at standard image
 * not sure how to do this better or whether I do not just want to go libgdx instead
 */
public class NoAnimationSystem extends AnimationSystem
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


    public void run()
    {
        while (Game.getCurrent().isRunning() == true)
        {
            for (LifeForm p : Game.getCurrent().getCurrentMap().getLifeForms())
            {
                //logger.info("lifeform state: {}", p.getState());
                // if dead, stay corpse, or blood stain
                if (p.getState().equals(LifeFormState.DEAD))
                {
                    p.setSpecialImage(0);
                }
                //if unconcious, stay unmoving
                else if (p.getState().equals(LifeFormState.UNCONSCIOUS))
                {
                    p.setCurrImage(0);
                }
                else // (p.getState().equals(LifeFormState.ALIVE))
                {
                    p.setCurrImage(0);
                }

                if (UIStateMachine.isUiOpen())
                {
                    EventBus.getDefault().post(new AnimatedRepresentationChanged(p));
                }
            }

            try
            {
                Game.getCurrent().getThreadController().sleep(GameConfiguration.animationLifeformDelay, ThreadNames.LIFEFORM_ANIMATION);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        logger.error("game no longer running, thread {} is closing hopefully?", "Animation Thread");
    }
}
