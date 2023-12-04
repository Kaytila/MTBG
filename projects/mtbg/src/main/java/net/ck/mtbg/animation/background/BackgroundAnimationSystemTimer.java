package net.ck.mtbg.animation.background;

import lombok.extern.log4j.Log4j2;

import javax.swing.*;

@Log4j2
public class BackgroundAnimationSystemTimer extends Timer
{
    public BackgroundAnimationSystemTimer(int delay, BackgroundAnimationSystemActionListener listener)
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
