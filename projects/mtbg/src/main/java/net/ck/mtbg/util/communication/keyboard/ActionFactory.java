package net.ck.mtbg.util.communication.keyboard;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class ActionFactory
{


    public static AbstractKeyboardAction createAction(KeyboardActionType type)
    {
        switch (type)
        {
            case EAST:
                return new EastAction();
            case ENTER:
                return new EnterAction();
            case ESC:
                return new ESCAction();
            case NORTH:
                return new NorthAction();
            case NULL:
                return new AbstractKeyboardAction();
            case SOUTH:
                return new SouthAction();
            case SPACE:
                return new SpaceAction();
            case WEST:
                return new WestAction();
            case INVENTORY:
                return new InventoryAction();
            case ZSTATS:
                return new ZStatsAction();
            default:
                logger.error("how did we end up here ?????");
                return new AbstractKeyboardAction();
        }
    }

}
