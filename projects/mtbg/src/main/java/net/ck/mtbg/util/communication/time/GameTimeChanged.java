package net.ck.mtbg.util.communication.time;

import net.ck.mtbg.util.communication.graphics.ChangedEvent;
import net.ck.mtbg.util.utils.CodeUtils;
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
