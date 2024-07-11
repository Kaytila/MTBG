package net.ck.mtbg.backend.time;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.swing.*;
import java.awt.event.ActionListener;

@Log4j2
@Getter
@Setter
public class QuequeTimer extends Timer
{


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
    public QuequeTimer(int delay, ActionListener listener)
    {
        super(delay, listener);
    }

    @Override
    public void start()
    {
        if (GameConfiguration.debugTimers == true)
        {
            logger.debug("starting queue timer");
        }
        super.start();
    }

    @Override
    public void stop()
    {
        if (GameConfiguration.debugTimers == true)
        {
            logger.debug("stopping queue timer");
        }
        super.stop();
    }

}
