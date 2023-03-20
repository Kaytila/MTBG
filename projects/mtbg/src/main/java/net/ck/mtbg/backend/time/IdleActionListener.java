package net.ck.mtbg.backend.time;

import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.graphics.PlayerPositionChanged;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.ActionFactory;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IdleActionListener implements ActionListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public IdleActionListener()
	{

	}

	@Override
	public void actionPerformed(ActionEvent e)
    {
        logger.info("sending space");
        AbstractKeyboardAction spaceAction = ActionFactory.createAction(KeyboardActionType.SPACE);
        Game.getCurrent().setPlayerAction(new PlayerAction(spaceAction));
        EventBus.getDefault().post(spaceAction);
        EventBus.getDefault().post(new PlayerPositionChanged(Game.getCurrent().getCurrentPlayer()));
        //Game.getCurrent().listThreads();
    }
}
