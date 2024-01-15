package net.ck.mtbg.util.communication.keyboard;

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
}
