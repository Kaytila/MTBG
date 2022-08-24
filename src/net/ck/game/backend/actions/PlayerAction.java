package net.ck.game.backend.actions;

import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.KeyboardActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

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
	

	//private KeyboardActionType type;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public PlayerAction(AbstractKeyboardAction ev)
	{
		setEvent(ev);
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
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
	

	public Logger getLogger()
	{
		return logger;
	}
	
	
	public String toString()
	{
		return "action: " + getEvent().getType() + " of player: " + getEntity().getNumber(); 
	}
}
