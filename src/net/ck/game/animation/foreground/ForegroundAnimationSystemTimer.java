package net.ck.game.animation.foreground;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class ForegroundAnimationSystemTimer extends Timer
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


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