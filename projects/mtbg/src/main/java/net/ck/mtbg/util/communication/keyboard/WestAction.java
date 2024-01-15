package net.ck.mtbg.util.communication.keyboard;

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

}
