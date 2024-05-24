package net.ck.mtbg.util.communication.keyboard;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Setter
@Getter
public class MoveAction extends AbstractKeyboardAction
{
    @Override
    public KeyboardActionType getType()
    {
        return KeyboardActionType.MOVE;
    }

    public boolean isActionimmediately()
    {
        return false;
    }
}