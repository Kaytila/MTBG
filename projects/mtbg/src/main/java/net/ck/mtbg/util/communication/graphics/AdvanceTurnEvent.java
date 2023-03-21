package net.ck.mtbg.util.communication.graphics;

import net.ck.mtbg.backend.actions.PlayerAction;

public class AdvanceTurnEvent extends ChangedEvent
{
    private boolean npcAction;

    private PlayerAction action;

    public PlayerAction getAction()
    {
        return action;
    }

    public void setAction(PlayerAction action)
    {
        this.action = action;
    }


    public AdvanceTurnEvent(boolean npcAction)
    {
        setNpcAction(npcAction);
    }

    public AdvanceTurnEvent(PlayerAction action)
    {
        setAction(action);
    }


    public boolean isNpcAction()
    {
        return npcAction;
    }

    public void setNpcAction(boolean npcAction)
    {
        this.npcAction = npcAction;
    }
}
