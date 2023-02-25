package net.ck.game.animation.missile;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

public class MissileUtilTimer extends Timer
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private MissileTimerTask missileTimerTask;

	public MissileTimerTask getMissileTimerTask()
	{
		return missileTimerTask;
	}

	public void setMissileTimerTask(MissileTimerTask missileTimerTask)
	{
		this.missileTimerTask = missileTimerTask;
	}

	public MissileUtilTimer()
	{
		super(true);
		missileTimerTask = new MissileTimerTask();
		this.schedule(missileTimerTask, 0, GameConfiguration.missileWait);
	}
}
