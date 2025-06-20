package net.ck.mtbg.backend.actions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;
import net.ck.mtbg.util.communication.keyboard.gameactions.AbstractKeyboardAction;

import java.io.Serializable;


/**
 * @author Claus This is one player action - currently this is there to
 * distinguish between random event and one user action per turn.
 * <p>
 * No real idea yet as to how to structure this any further probably
 * necessary to implement some kind of input and some kind of output.
 * output comes via AbstractEvent
 */
@Log4j2
@Setter
@Getter
@ToString
public class PlayerAction extends AbstractAction implements Serializable
{
    /**
     * does the action trigger an npc action
     */
    private boolean haveNPCAction;

    /**
     * who is the player or the NPC
     */
    private int number;

    public PlayerAction(AbstractKeyboardAction ev)
    {
        setEvent(ev);
        setHaveNPCAction(ev.isHaveNPCAction());
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
