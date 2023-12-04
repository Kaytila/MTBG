package net.ck.mtbg.animation.lifeform;

import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.event.ActionListener;

@Log4j2
public class AnimationSystemTimer extends Timer
{
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

    public AnimationSystemTimer(int delay, ActionListener listener)
    {
        super(delay, listener);
    }
}
