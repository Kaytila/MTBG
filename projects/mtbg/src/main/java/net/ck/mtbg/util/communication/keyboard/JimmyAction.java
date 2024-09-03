package net.ck.mtbg.util.communication.keyboard;

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
}
