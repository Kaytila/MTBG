package net.ck.game.backend;

import net.ck.util.communication.keyboard.ActionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class QuequeTimerActionListener implements ActionListener
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
        //logger.info("action in queue timer");
        if (Game.getCurrent().getCommandQueue().getActionList().isEmpty())
        {
            Game.getCurrent().getQuequeTimer().stop();
            return;
        }
        EventBus.getDefault().post(ActionFactory.createAction(Game.getCurrent().getCommandQueue().getActionList().get(0).getType()));
        Game.getCurrent().getCommandQueue().getActionList().remove(0);
    }

    public Logger getLogger()
    {
        return logger;
    }

}
