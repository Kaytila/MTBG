package net.ck.game.animation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MissileTimer extends Timer
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    /**
     * Creates a {@code Timer} and initializes both the initial delay and
     * between-event delay to {@code delay} milliseconds. If {@code delay}
     * is less than or equal to zero, the timer fires as soon as it
     * is started. If <code>listener</code> is not <code>null</code>,
     * it's registered as an action listener on the timer.
     *
     * @param delay    milliseconds for the initial and between-event delay
     * @param listener an initial listener; can be <code>null</code>
     * @see #addActionListener
     * @see #setInitialDelay
     * @see #setRepeats
     */
    public MissileTimer(int delay, ActionListener listener)
    {
        super(delay, listener);
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }


    @Override
    public void start()
    {
        logger.info("starting missile queue timer");
        super.start();
    }

    @Override
    public void stop()
    {
        if (this.isRunning())
        {
            logger.info("stopping missile queue timer");
            super.stop();
        }
    }

}