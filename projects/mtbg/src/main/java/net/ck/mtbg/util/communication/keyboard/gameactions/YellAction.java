package net.ck.mtbg.util.communication.keyboard.gameactions;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

@Log4j2
@Getter
@Setter
public class YellAction extends AbstractKeyboardAction
{
    @Override
    public KeyboardActionType getType()
    {
        return KeyboardActionType.YELL;
    }

    public boolean isActionimmediately()
    {
        return true;
    }

    public int getSoundReach()
    {
        return 10;
    }
}
