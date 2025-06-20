package net.ck.mtbg.util.communication.keyboard.gameactions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

import java.awt.*;

@Getter
@Setter
@Log4j2
@ToString
public class GetAction extends AbstractKeyboardAction
{

    public GetAction(Point getWhere)
    {
        super();
        this.setGetWhere(getWhere);
    }


    public GetAction()
    {
        super();
        this.setGetWhere(new Point(-1, -1));
    }


    public KeyboardActionType getType()
    {
        return KeyboardActionType.GET;
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
