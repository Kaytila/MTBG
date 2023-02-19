package net.ck.game.ui.listeners;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.state.UIStateMachine;
import net.ck.util.CodeUtils;
import net.ck.util.communication.graphics.ForegroundRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ForegroundAnimationSystemActionListener implements ActionListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private int currentForegroundImage;

	private final Random rand = new Random();

	public ForegroundAnimationSystemActionListener()
	{
		setCurrentForegroundImage(0);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (Game.getCurrent().isRunning() == true)
		{
			if (GameConfiguration.animated == true)
			{
				setCurrentForegroundImage((rand.nextInt(GameConfiguration.animationCycles)));
			}
			else
			{
				setCurrentForegroundImage(0);
			}

			if (UIStateMachine.isUiOpen())
			{
				EventBus.getDefault().post(new ForegroundRepresentationChanged(getCurrentForegroundImage()));
			}
		}
	}


	public int getCurrentForegroundImage()
	{
		return currentForegroundImage;
	}

	public void setCurrentForegroundImage(int currentForeroundImage)
	{
		this.currentForegroundImage = currentForeroundImage;
	}
}

