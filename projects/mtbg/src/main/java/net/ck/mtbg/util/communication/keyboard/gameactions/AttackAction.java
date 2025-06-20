package net.ck.mtbg.util.communication.keyboard.gameactions;

import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

public class AttackAction extends AbstractKeyboardAction
{
    public KeyboardActionType getType()
    {
        return KeyboardActionType.ATTACK;
    }

    public boolean isActionimmediately()
    {
        return false;
    }


    public int getSoundReach()
    {
        return 10;
    }
}
