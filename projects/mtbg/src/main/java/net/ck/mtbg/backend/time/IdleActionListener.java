package net.ck.mtbg.backend.time;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.PlayerPositionChanged;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.ActionFactory;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@Setter
@Log4j2
public class IdleActionListener implements ActionListener
{
    public IdleActionListener()
    {

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (UIStateMachine.getUiState().equals(UIState.OPENED))
        {
            //logger.info("sending space");
            AbstractKeyboardAction spaceAction = ActionFactory.createAction(KeyboardActionType.SPACE);
            Game.getCurrent().setPlayerAction(new PlayerAction(spaceAction));
            EventBus.getDefault().post(spaceAction);
            EventBus.getDefault().post(new PlayerPositionChanged(Game.getCurrent().getCurrentPlayer()));
            //Game.getCurrent().listThreads();
        }
    }

}
