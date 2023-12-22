package net.ck.mtbg.util.communication.keyboard;

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

}
