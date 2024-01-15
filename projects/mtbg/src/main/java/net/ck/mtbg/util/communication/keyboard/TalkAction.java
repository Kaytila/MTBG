package net.ck.mtbg.util.communication.keyboard;

public class TalkAction extends AbstractKeyboardAction
{

    public KeyboardActionType getType()
    {
        return KeyboardActionType.TALK;
    }

    public boolean isActionimmediately()
    {
        return false;
    }
}
