package net.ck.mtbg.util.communication.graphics;

import net.ck.mtbg.util.utils.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MissilePositionChanged extends ChangedEvent
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
}
