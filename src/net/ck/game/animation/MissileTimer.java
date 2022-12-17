package net.ck.game.animation;

import net.ck.game.backend.game.Game;
import net.ck.util.communication.graphics.MissilePositionChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class MissileTimer implements Runnable
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    private boolean running;
    private int delay;

    /**
     * Creates a {@code Timer} and initializes both the initial delay and
     * between-event delay to {@code delay} milliseconds. If {@code delay}
     * is less than or equal to zero, the timer fires as soon as it
     * is started. If <code>listener</code> is not <code>null</code>,
     * it's registered as an action listener on the timer.
     *
     * @param delay    milliseconds for the initial and between-event delay
     */
    public MissileTimer(int delay)
    {
        setDelay(delay);
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    public synchronized boolean isRunning()
    {
        return running;
    }

    public synchronized void setRunning(boolean running)
    {
        this.running = running;
    }

    @Override
    public void run()
    {
        while (Game.getCurrent().isRunning())
        {
            if (Game.getCurrent().getCurrentMap().getMissiles() != null)
            {
                if (Game.getCurrent().getCurrentMap().getMissiles().size() > 0)
                {
                    //logger.info("posting message");
                    setRunning(true);
                    EventBus.getDefault().post(new MissilePositionChanged());
                }
                else
                {
                    setRunning(false);
                    continue;
                }
            }
            else
            {
                setRunning(false);
                continue;
            }
            try
            {
                Thread.sleep(getDelay());
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public int getDelay()
    {
        return delay;
    }

    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    public void stop()
    {

    }
}