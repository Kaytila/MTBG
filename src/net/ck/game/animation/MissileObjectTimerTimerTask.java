package net.ck.game.animation;

import net.ck.game.backend.Game;
import net.ck.util.communication.graphics.MissilePositionChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Objects;
import java.util.TimerTask;

public class MissileObjectTimerTimerTask extends TimerTask
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    @Override
    public void run()
    {
        Game.getCurrent().getMissileObjectTimer().setRunning(true);
        while (Game.getCurrent().getCurrentMap().getMissiles() != null)
        {
            if (Game.getCurrent().getCurrentMap().getMissiles().size() > 0)
            {
                //logger.info("posting message");
                EventBus.getDefault().post(new MissilePositionChanged());
                try
                {
                    Thread.sleep(30);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                this.cancel();
            }
        }
    }
}
