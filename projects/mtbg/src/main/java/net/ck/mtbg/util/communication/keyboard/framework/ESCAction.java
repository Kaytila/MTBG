package net.ck.mtbg.util.communication.keyboard.framework;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.keyboard.gameactions.AbstractKeyboardAction;

@Log4j2
@Getter
@Setter
public class ESCAction extends AbstractKeyboardAction
{
    @Override
    public KeyboardActionType getType()
    {
        return KeyboardActionType.ESC;
    }

    public int getSoundReach()
    {
        return 0;
    }

}
