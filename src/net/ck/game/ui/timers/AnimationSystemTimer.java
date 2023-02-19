package net.ck.game.ui.timers;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;

public class AnimationSystemTimer extends Timer
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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
