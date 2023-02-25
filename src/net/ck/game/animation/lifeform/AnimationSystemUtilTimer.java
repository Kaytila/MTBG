package net.ck.game.animation.lifeform;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

public class AnimationSystemUtilTimer extends Timer
{

    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private AnimationSystemTimerTask animationSystemTimerTask;


    public AnimationSystemUtilTimer()
    {
        super(true);
        animationSystemTimerTask = new AnimationSystemTimerTask();
        this.schedule(animationSystemTimerTask, 0, GameConfiguration.animationLifeformDelay);
    }
}
