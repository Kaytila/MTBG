package net.ck.util.communication.graphics;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvanceTurnEvent extends ChangedEvent
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private boolean npcAction;

    public AdvanceTurnEvent(boolean npcAction)
    {
        setNpcAction(npcAction);
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
