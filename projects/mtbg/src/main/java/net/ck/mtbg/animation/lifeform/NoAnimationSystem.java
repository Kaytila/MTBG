package net.ck.mtbg.animation.lifeform;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.entities.entities.LifeFormState;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageManager;
import net.ck.mtbg.util.communication.graphics.AnimatedRepresentationChanged;
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
                    p.setCurrImage(ImageManager.getActionImage(ActionStates.KILL));
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
                ThreadController.sleep(GameConfiguration.animationLifeformDelay, ThreadNames.LIFEFORM_ANIMATION);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        logger.error("game no longer running, thread {} is closing hopefully?", "Animation Thread");
    }
}
