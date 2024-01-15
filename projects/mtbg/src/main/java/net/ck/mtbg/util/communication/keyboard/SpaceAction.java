package net.ck.mtbg.util.communication.keyboard;

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

}
