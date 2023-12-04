package net.ck.mtbg.animation.missile;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import java.util.Timer;

@Log4j2
@Getter
@Setter
public class MissileUtilTimer extends Timer
{
	private MissileTimerTask missileTimerTask;

	public MissileUtilTimer()
	{
		super(true);
		missileTimerTask = new MissileTimerTask();
		this.schedule(missileTimerTask, 0, GameConfiguration.missileWait);
	}
}
