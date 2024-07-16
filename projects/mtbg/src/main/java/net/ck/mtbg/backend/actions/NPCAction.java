package net.ck.mtbg.backend.actions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;

@Log4j2
@Getter
@Setter
@ToString
public class NPCAction extends AbstractAction
{
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
}
