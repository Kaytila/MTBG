package net.ck.mtbg.animation.foreground;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;

@Log4j2
@Getter
@Setter
public class ForegroundAnimationSystemTimer extends Timer
{
    public ForegroundAnimationSystemTimer(int delay, ForegroundAnimationSystemActionListener listener)
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