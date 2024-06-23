package net.ck.mtbg.animation.lifeform;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.Timer;

@Log4j2
@ToString
public class HitMissImageTimer extends Timer
{

    private HitMissImageTimerTask hitMissImageTimerTask;

    public HitMissImageTimer()
    {
        super(true);
    }

    public synchronized HitMissImageTimerTask getHitMissImageTimerTask()
    {
        return hitMissImageTimerTask;
    }

    public synchronized void setHitMissImageTimerTask(HitMissImageTimerTask hitMissImageTimerTask)
    {
        this.hitMissImageTimerTask = hitMissImageTimerTask;
    }
}
