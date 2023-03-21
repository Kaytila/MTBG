package net.ck.mtbg.animation.background;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.graphics.BackgroundRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ConcurrentModificationException;

public class BackgroundAnimationSystem implements Runnable
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public BackgroundAnimationSystem()
    {
        setCurrentBackgroundImage(0);
    }

    private int currentBackgroundImage;

	/**
	 * just trying to iterate over i to get a same looking animation for background images
	 */

    public void run()
	{
		while (Game.getCurrent().isRunning() == true)
		{
			int i;
			for (i = 0; i <= GameConfiguration.animationCycles; i++) {
                setCurrentBackgroundImage(i);
                if (UIStateMachine.isUiOpen()) {
                    EventBus.getDefault().post(new BackgroundRepresentationChanged(getCurrentBackgroundImage()));
                }
                try {
					ThreadController.sleep(GameConfiguration.animationBackGroundDelay, ThreadNames.BACKGROUND_ANIMATION);
                } catch (ConcurrentModificationException e) {
                    logger.error("caught ConcurrentModificationException");
                }
                catch (Throwable e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public int getCurrentBackgroundImage()
	{
		return currentBackgroundImage;
	}

	public void setCurrentBackgroundImage(int currentBackgroundImage)
	{
		this.currentBackgroundImage = currentBackgroundImage;
	}
}
