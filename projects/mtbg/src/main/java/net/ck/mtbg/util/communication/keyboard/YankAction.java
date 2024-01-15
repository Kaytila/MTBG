package net.ck.mtbg.util.communication.keyboard;

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

}