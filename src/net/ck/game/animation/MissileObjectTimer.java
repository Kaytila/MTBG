package net.ck.game.animation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MissileObjectTimer extends Timer
{

    private final Logger logger = LogManager.getLogger(getRealClass());
    private boolean running;

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    @Override
    public void schedule(TimerTask task, long delay)
    {
        super.schedule(task, delay);
        setRunning(true);
    }

    @Override
    public void cancel()
    {
        super.cancel();
        setRunning(false);
    }

    @Override
    public int purge()
    {
        return super.purge();
    }

    public MissileObjectTimer(String name, boolean isDaemon)
    {
        super(name, isDaemon);
    }

    public boolean isRunning()
    {
        return running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }
}
