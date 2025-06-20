package net.ck.mtbg.util.communication.keyboard.gameactions;

import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

public class SpaceAction extends AbstractKeyboardAction
{

    public KeyboardActionType getType()
    {
        return KeyboardActionType.SPACE;
    }

    public boolean isActionimmediately()
    {
        return true;
    }

    public int getSoundReach()
    {
        return 0;
    }


}
