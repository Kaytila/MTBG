package net.ck.util.communication.graphics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class AdvanceTurnEvent extends ChangedEvent
{

    private boolean npcAction;

    public AdvanceTurnEvent(boolean npcAction)
    {
        setNpcAction(npcAction);
    }

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
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
