package net.ck.util.communication.keyboard;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PushAction extends AbstractKeyboardAction
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public KeyboardActionType getType()
    {
        return KeyboardActionType.PUSH;
    }

    public boolean isActionimmediately()
    {
        return false;
    }
}
