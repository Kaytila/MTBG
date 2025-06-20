package net.ck.mtbg.util.communication.keyboard.gameactions;

import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

public class JimmyAction extends AbstractKeyboardAction
{
    public KeyboardActionType getType()
    {
        return KeyboardActionType.JIMMY;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

    public int getSoundReach()
    {
        return 1;
    }


}
