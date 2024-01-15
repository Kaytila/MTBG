package net.ck.mtbg.util.communication.keyboard;

public class ZStatsAction extends AbstractKeyboardAction
{

    public KeyboardActionType getType()
    {
        return KeyboardActionType.ZSTATS;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

}