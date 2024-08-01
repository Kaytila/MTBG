package net.ck.mtbg.util.communication.keyboard;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

/**
 * so search will just search the vicinity around the player, all 9 tiles, this is done immediately, no cross-hair
 */
@Getter
@Setter
@Log4j2
@ToString
public class SearchAction extends AbstractKeyboardAction
{
    public KeyboardActionType getType()
    {
        return KeyboardActionType.SEARCH;
    }

    public boolean isActionimmediately()
    {
        return true;
    }
}
