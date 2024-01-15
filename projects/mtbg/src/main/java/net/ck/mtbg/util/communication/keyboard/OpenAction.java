package net.ck.mtbg.util.communication.keyboard;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.MapTile;

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
}
