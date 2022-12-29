package net.ck.util.communication.keyboard;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * so search will just search the vicinity around the player, all 9 tiles, this is done immediately, no cross-hair
 *
 */
public class SearchAction extends AbstractKeyboardAction
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public KeyboardActionType getType()
    {
        return KeyboardActionType.SEARCH;
    }

    public boolean isActionimmediately()
    {
        return true;
    }
}
