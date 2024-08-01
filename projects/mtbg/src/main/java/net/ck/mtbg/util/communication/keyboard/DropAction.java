package net.ck.mtbg.util.communication.keyboard;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
@ToString
public class DropAction extends AbstractKeyboardAction
{


    @Override
    public KeyboardActionType getType()
    {
        return KeyboardActionType.DROP;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

}
