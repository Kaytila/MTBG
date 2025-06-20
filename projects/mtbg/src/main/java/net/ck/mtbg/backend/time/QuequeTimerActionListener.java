package net.ck.mtbg.backend.time;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.state.TimerManager;
import net.ck.mtbg.util.communication.keyboard.framework.ActionFactory;
import net.ck.mtbg.util.communication.keyboard.gameactions.AbstractKeyboardAction;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Log4j2
@Getter
@Setter
public class QuequeTimerActionListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //logger.info("action in queue timer");
        if (Game.getCurrent().getCurrentPlayer().getQueuedActions().getActionList().isEmpty())
        {
            TimerManager.getQuequeTimer().stop();
            return;
        }
        AbstractKeyboardAction action = Game.getCurrent().getCurrentPlayer().getQueuedActions().poll();
        if (GameConfiguration.debugEvents == true)
        {
            logger.debug("fire action from the queque");
        }
        EventBus.getDefault().post(ActionFactory.createAction(action.getType()));
    }
}
