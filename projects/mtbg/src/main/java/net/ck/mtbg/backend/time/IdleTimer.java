package net.ck.mtbg.backend.time;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * idletime does exactly this - after PC has done a turn, idle timer starts
 * for N milliseconds, until a space, i.e. pass is being sent.
 */
@Log4j2
@Getter
@Setter
@ToString
public class IdleTimer extends Timer
{
    private boolean paused = false;

    public IdleTimer(int delay, ActionListener listener)
    {
        super(delay, listener);
        if (GameConfiguration.debugTimers == true)
        {
            logger.debug("delay:{}, listener: {}, running: {}", delay, listener, isRunning());
        }
    }

    @Override
    public void start()
    {
        if (isPaused())
        {
            if (GameConfiguration.debugTimers == true)
            {
                logger.debug("timer is paused, dont start again");
                return;
            }
        }
        if (GameConfiguration.debugTimers == true)
        {
            logger.debug("starting idle timer");
        }
        if (isRunning() == true)
        {
            if (GameConfiguration.debugTimers == true)
            {
                logger.debug("timer already running, dont start again");
            }
        }
        else
        {
            super.start();
        }

    }

    @Override
    public void stop()
    {
        if (GameConfiguration.debugTimers == true)
        {
            logger.debug("stopping idle timer");
        }
        if (isRunning() == true)
        {
            super.stop();
        }
        else
        {
            if (GameConfiguration.debugTimers == true)
            {
                logger.debug("timer already stopped, dont stop again");
            }
        }

    }
}
