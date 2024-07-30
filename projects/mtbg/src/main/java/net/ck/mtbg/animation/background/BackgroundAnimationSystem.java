package net.ck.mtbg.animation.background;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.BackgroundRepresentationChanged;
import org.greenrobot.eventbus.EventBus;

import java.util.ConcurrentModificationException;

@Log4j2
@Getter
@Setter
public class BackgroundAnimationSystem implements Runnable
{
    /**
     * number of the list of background image
     */
    private int currentBackgroundImage;

    public BackgroundAnimationSystem()
    {
        setCurrentBackgroundImage(0);
    }


    /**
     * just trying to iterate over i to get a same looking animation for background images
     */

    public void run()
    {
        while (Game.getCurrent().isRunning() == true)
        {
            int i;
            for (i = 0; i <= GameConfiguration.animationCycles; i++)
            {
                setCurrentBackgroundImage(i);
                if (UIStateMachine.getUiState().equals(UIState.OPENED))
                {
                    if (GameConfiguration.debugTimers == true)
                    {
                        logger.debug("fire new background");
                    }

                    if (GameConfiguration.debugEvents == true)
                    {
                        logger.debug("fire new background");
                    }
                    EventBus.getDefault().post(new BackgroundRepresentationChanged(getCurrentBackgroundImage()));
                }
                try
                {
                    ThreadController.sleep(GameConfiguration.animationBackGroundDelay, ThreadNames.BACKGROUND_ANIMATION);
                }
                catch (ConcurrentModificationException e)
                {
                    logger.error("caught ConcurrentModificationException");
                }
                catch (Throwable e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
