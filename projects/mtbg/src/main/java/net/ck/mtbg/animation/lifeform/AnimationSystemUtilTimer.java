package net.ck.mtbg.animation.lifeform;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

public class AnimationSystemUtilTimer extends Timer
{

    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public AnimationSystemUtilTimer()
    {
        super(true);

    }
}
