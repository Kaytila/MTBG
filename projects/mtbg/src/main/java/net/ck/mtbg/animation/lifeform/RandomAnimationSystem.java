package net.ck.mtbg.animation.lifeform;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.entities.entities.LifeFormState;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.mtbg.util.utils.ImageManager;
import net.ck.mtbg.util.utils.UILense;
import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * RandomAnimationSystem takes all lifeforms on map and
 */
@Log4j2
public class RandomAnimationSystem extends AnimationSystem implements Runnable
{
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
            //logger.info("running");
            for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
            {
                for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
                {
                    MapTile tile = UILense.getCurrent().mapTiles[row][column];
                    if (tile != null)
                    {
                        LifeForm p = tile.getLifeForm();
                        if (p != null)
                        {
                            //logger.info("p: {}", p);
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
                            else// (p.getState().equals(LifeFormState.ALIVE))
                            {
                                p.setCurrImage(rand.nextInt(GameConfiguration.animationCycles));
                            }
                        }
                    }
                }
            }

            if (UIStateMachine.isUiOpen())
            {
                if (GameConfiguration.debugEvents == true)
                {
                    logger.debug("fire new lifeform animation");
                }
                EventBus.getDefault().post(new AnimatedRepresentationChanged(null));
            }

            try
            {
                ThreadController.sleep(GameConfiguration.animationLifeformDelay, ThreadNames.LIFEFORM_ANIMATION);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        logger.error("game no longer running, thread {} is closing hopefully?", ThreadNames.LIFEFORM_ANIMATION);
    }

}
