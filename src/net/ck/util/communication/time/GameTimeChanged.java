package net.ck.util.communication.time;

import net.ck.util.CodeUtils;
import net.ck.util.communication.graphics.ChangedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameTimeChanged extends ChangedEvent
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    final GameTimeChangeType type;

    public GameTimeChanged(GameTimeChangeType typ)
    {
        super();
        this.type = typ;
    }


}
