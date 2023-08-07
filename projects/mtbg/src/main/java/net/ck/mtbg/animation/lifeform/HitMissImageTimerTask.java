package net.ck.mtbg.animation.lifeform;

import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.entities.entities.LifeFormState;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageManager;
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
		setRunning(true);
	}

	@Override
	public void run()
	{
		logger.info("HitMissImageTimerTask is running");
		setRunning(false);
		if (getLifeForm().getState().equals(LifeFormState.DEAD))
		{
			getLifeForm().setCurrImage(ImageManager.getActionImage(ActionStates.KILL));
		}
		else
		{
			getLifeForm().setCurrImage(0);
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
