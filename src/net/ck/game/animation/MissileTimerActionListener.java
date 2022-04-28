package net.ck.game.animation;

import net.ck.game.backend.Game;
import net.ck.util.communication.graphics.MissilePositionChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MissileTimerActionListener implements ActionListener
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (Game.getCurrent().getCurrentMap().getMissiles() != null)
        {
            if (Game.getCurrent().getCurrentMap().getMissiles().size() > 0)
            {
                //logger.info("posting message");
                EventBus.getDefault().post(new MissilePositionChanged());
            }
            else
            {
                Game.getCurrent().getMissileTimer().stop();
            }
        }
        else
        {
            Game.getCurrent().getMissileTimer().stop();
        }
    }

    public Logger getLogger()
    {
        return logger;
    }

}
