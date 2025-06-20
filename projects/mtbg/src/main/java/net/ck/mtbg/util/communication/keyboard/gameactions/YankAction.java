package net.ck.mtbg.util.communication.keyboard.gameactions;

import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

public class YankAction extends AbstractKeyboardAction
{

    public KeyboardActionType getType()
    {
        return KeyboardActionType.YANK;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

    public int getSoundReach()
    {
        return 3;
    }


}