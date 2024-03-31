package net.ck.mtbg.music;

import net.ck.mtbg.util.utils.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * music timer takes care of timing when a song will be finished if in VICTORY state.
 *
 */
public class MusicTimer extends Timer
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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
    public MusicTimer(int delay, ActionListener listener)
    {
        super(delay, listener);
    }
}
