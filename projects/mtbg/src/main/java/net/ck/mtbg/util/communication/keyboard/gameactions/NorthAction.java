package net.ck.mtbg.util.communication.keyboard.gameactions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

@Getter
@Setter
@Log4j2
@ToString
public class NorthAction extends AbstractKeyboardAction
{
    @Override
    public KeyboardActionType getType()
    {
        return KeyboardActionType.NORTH;
    }

    public boolean isActionimmediately()
    {
        return true;
    }

    public int getSoundReach()
    {
        return 1;
    }


}
