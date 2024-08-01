package net.ck.mtbg.util.communication.keyboard;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;

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


    public Logger getLogger()
    {
        return logger;
    }

    public boolean isActionimmediately()
    {
        return false;
    }


}
