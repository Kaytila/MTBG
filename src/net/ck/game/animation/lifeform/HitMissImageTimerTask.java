package net.ck.game.animation.lifeform;

import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.entities.LifeFormState;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

public class HitMissImageTimerTask extends TimerTask
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private boolean running;
	private LifeForm lifeForm;

	public HitMissImageTimerTask(LifeForm n)
	{
		setLifeForm(n);
	}

	@Override
	public void run()
	{
		logger.info("HitMissImageTimerTask is running running running why does it not do anything");
		setRunning(false);
		if (getLifeForm().getState().equals(LifeFormState.DEAD))
		{
			getLifeForm().setCurrentImage(ImageUtils.getBloodstainImage());
		}
		else
		{
			getLifeForm().setCurrentImage(getLifeForm().getAnimationImageList().get(0));
		}
	}

	public synchronized boolean isRunning()
	{
		return running;
	}

	public synchronized void setRunning(boolean running)
	{
		this.running = running;
	}

	public LifeForm getLifeForm()
	{
		return lifeForm;
	}

	public void setLifeForm(LifeForm lifeForm)
	{
		this.lifeForm = lifeForm;
	}
}
