package net.ck.util;

import net.ck.game.backend.configuration.GameConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

public class MissileUtilTimer extends Timer
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public MissileUtilTimer()
	{
		super(true);
		MissileTimerTask missileTimerTask = new MissileTimerTask();
		this.schedule(missileTimerTask, 0, GameConfiguration.missileWait);
	}
}
