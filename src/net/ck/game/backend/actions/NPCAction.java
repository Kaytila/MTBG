package net.ck.game.backend.actions;

import net.ck.util.CodeUtils;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.KeyboardActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NPCAction extends AbstractAction
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


	public NPCAction(AbstractKeyboardAction ev)
	{
		setEvent(ev);
	}


	/**
	 * what type is the action?
	 *
	 * @return the keyboard action type, as basically all actions a npc can do
	 * can be done by a player as well.
	 */
	public KeyboardActionType getType()
	{
		return getEvent().getType();
	}

	public String toString()
	{
		return "NPC action: " + getEvent().getType() + " of NPC";
	}
}
