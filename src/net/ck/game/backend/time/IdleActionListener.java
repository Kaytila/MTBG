package net.ck.game.backend.time;

import net.ck.game.backend.game.Game;
import net.ck.util.CodeUtils;
import net.ck.util.communication.graphics.PlayerPositionChanged;
import net.ck.util.communication.keyboard.ActionFactory;
import net.ck.util.communication.keyboard.KeyboardActionType;
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
		//logger.info("sending space");
		EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.SPACE));
		EventBus.getDefault().post(new PlayerPositionChanged(Game.getCurrent().getCurrentPlayer()));
		//Game.getCurrent().listThreads();
	}
}
