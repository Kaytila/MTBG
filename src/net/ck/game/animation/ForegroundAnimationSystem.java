package net.ck.game.animation;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.state.UIStateMachine;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.util.CodeUtils;
import net.ck.util.communication.graphics.ForegroundRepresentationChanged;
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
                    Game.getCurrent().getThreadController().sleep(GameConfiguration.animationForeGroundDelay, ThreadNames.FOREGROUND_ANIMATION);
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
