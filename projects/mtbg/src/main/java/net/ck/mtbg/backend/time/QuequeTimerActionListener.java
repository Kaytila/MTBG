package net.ck.mtbg.backend.time;

import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.state.TimerManager;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.ActionFactory;
import net.ck.mtbg.util.utils.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuequeTimerActionListener implements ActionListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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
        EventBus.getDefault().post(ActionFactory.createAction(action.getType()));
    }
}
