package net.ck.game.backend.actions;

import net.ck.util.CodeUtils;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.KeyboardActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 
 * @author Claus This is one player action - currently this is there to
 *         distinguish between random event and one user action per turn.
 *
 *         No real idea yet as to how to structure this any further probably
 *         necessary to implement some kind of input and some kind of output.
 *         output comes via AbstractEvent
 *
 */
public class PlayerAction extends AbstractAction
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private boolean haveNPCAction;

	public PlayerAction(AbstractKeyboardAction ev)
	{
		setEvent(ev);
	}


	/**
	 * who is the player or the NPC
	 */
	private int number;

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	/**
	 * what type is the action?
	 * @return the keyboard action type, as basically all actions a npc can do
	 * can be done by a player as well.
	 */
	public KeyboardActionType getType()
	{
		return getEvent().getType();
	}

	/*public void setType(KeyboardActionType type)
	{
		this.type = type;
	}*/


	public String toString()
	{
		return "Player action: " + getEvent().getType() + " of player: ";
	}

	public boolean isHaveNPCAction()
	{
		return haveNPCAction;
	}

	public void setHaveNPCAction(boolean haveNPCAction)
	{
		this.haveNPCAction = haveNPCAction;
	}
}
