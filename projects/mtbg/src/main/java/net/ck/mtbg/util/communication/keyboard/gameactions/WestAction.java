package net.ck.mtbg.util.communication.keyboard.gameactions;

import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

public class WestAction extends AbstractKeyboardAction
{
    @Override
    public KeyboardActionType getType()
    {
        return KeyboardActionType.WEST;
    }

    public boolean isActionimmediately()
    {
        return true;
    }

    public int getSoundReach()
    {
        return 1;
    }


}
