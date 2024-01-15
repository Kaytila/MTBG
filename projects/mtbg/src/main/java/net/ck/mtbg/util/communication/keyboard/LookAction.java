package net.ck.mtbg.util.communication.keyboard;

public class LookAction extends AbstractKeyboardAction
{
    public KeyboardActionType getType()
    {
        return KeyboardActionType.LOOK;
    }

    public boolean isActionimmediately()
    {
        return false;
    }
}
