package net.ck.mtbg.util.communication.keyboard;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpellbookAction extends AbstractKeyboardAction
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public KeyboardActionType getType()
    {
        return KeyboardActionType.SPELLBOOK;
    }

    public boolean isActionimmediately()
    {
        return false;
    }


}
