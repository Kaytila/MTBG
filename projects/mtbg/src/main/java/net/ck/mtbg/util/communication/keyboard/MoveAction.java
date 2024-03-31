package net.ck.mtbg.util.communication.keyboard;

import net.ck.mtbg.util.utils.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveAction extends AbstractKeyboardAction
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));



    @Override
    public KeyboardActionType getType()
    {
        return KeyboardActionType.MOVE;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

}
