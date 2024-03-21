package net.ck.mtbg.backend.time;

import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * idletime does exactly this - after PC has done a turn, idle timer starts
 * for N milliseconds, until a space, i.e. pass is being sent.
 */
@Log4j2
public class IdleTimer extends Timer
{
    public IdleTimer(int delay, ActionListener listener)
    {
        super(delay, listener);
    }

    @Override
    public void start()
    {
        //logger.info("starting idle timer");
        super.start();
    }

    @Override
    public void stop()
    {
        //logger.info("stopping idle timer");
        super.stop();
    }
}
