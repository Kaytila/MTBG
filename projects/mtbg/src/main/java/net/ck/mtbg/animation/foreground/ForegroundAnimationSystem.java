package net.ck.mtbg.animation.foreground;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.animation.lifeform.IndividualAnimationSystem;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.ForegroundRepresentationChanged;
import org.greenrobot.eventbus.EventBus;

import java.util.ConcurrentModificationException;

@Log4j2
@Getter
@Setter
public class ForegroundAnimationSystem extends IndividualAnimationSystem
{

    private int currentForegroundImage;

    public ForegroundAnimationSystem()
    {
        setCurrentForegroundImage(0);
    }

    /**
     * just trying to iterate over i to get a same looking animation for background images
     */
    @Override
    public void run()
    {
        while (Game.getCurrent().isRunning() == true)
        {
            int i;
            for (i = 0; i <= GameConfiguration.animationCycles; i++)
            {
                setCurrentForegroundImage(i);
                if (UIStateMachine.isUiOpen())
                {
                    EventBus.getDefault().post(new ForegroundRepresentationChanged(getCurrentForegroundImage()));
                }
                try
                {
                    ThreadController.sleep(GameConfiguration.animationForeGroundDelay, ThreadNames.FOREGROUND_ANIMATION);
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
        }
    }
}
