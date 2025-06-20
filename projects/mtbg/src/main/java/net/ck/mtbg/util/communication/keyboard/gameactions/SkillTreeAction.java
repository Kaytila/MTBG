package net.ck.mtbg.util.communication.keyboard.gameactions;

import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

public class SkillTreeAction extends AbstractKeyboardAction
{
    public KeyboardActionType getType()
    {
        return KeyboardActionType.SKILLTREE;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

    public int getSoundReach()
    {
        return 0;
    }


}
