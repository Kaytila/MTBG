package net.ck.mtbg.util.communication.keyboard.gameactions;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

@Log4j2
@Getter
@Setter
public class OpenAction extends AbstractKeyboardAction
{
    MapTile tile;

    public KeyboardActionType getType()
    {
        return KeyboardActionType.OPEN;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

    public int getSoundReach()
    {
        return 2;
    }


}
