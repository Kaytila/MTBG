package net.ck.game.backend.actions;

import net.ck.util.CodeUtils;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NPCAction extends AbstractAction
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


	public NPCAction(AbstractKeyboardAction ev)
	{
		setEvent(ev);
	}

	public String toString()
	{
		return "action: " + getEvent().getType() + " of NPC";
	}
}
