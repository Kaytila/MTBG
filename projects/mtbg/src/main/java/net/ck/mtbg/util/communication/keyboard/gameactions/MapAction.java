package net.ck.mtbg.util.communication.keyboard.gameactions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

@Log4j2
@Getter
@Setter
@ToString
public class MapAction extends AbstractKeyboardAction
{
    @Override
    public KeyboardActionType getType()
    {
        return KeyboardActionType.MAP;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

    public int getSoundReach()
    {
        return 0;
    }


}
