package net.ck.mtbg.util.communication.keyboard;

public class SpellbookAction extends AbstractKeyboardAction
{


    public KeyboardActionType getType()
    {
        return KeyboardActionType.SPELLBOOK;
    }

    public boolean isActionimmediately()
    {
        return false;
    }


}
