package net.ck.mtbg.animation.foreground;

import net.ck.mtbg.animation.lifeform.IndividualAnimationSystem;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.graphics.ForegroundRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ConcurrentModificationException;

public class ForegroundAnimationSystem extends IndividualAnimationSystem
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private int currentForegroundImage;

	public int getCurrentForegroundImage()
	{
		return currentForegroundImage;
	}

	public void setCurrentForegroundImage(int currentForegroundImage)
	{
		this.currentForegroundImage = currentForegroundImage;
	}

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
			for (i = 0; i <= GameConfiguration.animationCycles; i++) {
                setCurrentForegroundImage(i);
                if (UIStateMachine.isUiOpen()) {
                    EventBus.getDefault().post(new ForegroundRepresentationChanged(getCurrentForegroundImage()));
                }
                try {
					ThreadController.sleep(GameConfiguration.animationForeGroundDelay, ThreadNames.FOREGROUND_ANIMATION);
                } catch (ConcurrentModificationException e) {
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
