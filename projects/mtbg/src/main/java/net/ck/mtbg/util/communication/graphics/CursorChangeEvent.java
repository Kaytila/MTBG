package net.ck.mtbg.util.communication.graphics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.awt.*;

@Getter
@Setter
@Log4j2
@ToString
public class CursorChangeEvent extends ChangedEvent
{

    private Cursor cursor;

    public CursorChangeEvent(Cursor cursor)
    {
        super();
        setCursor(cursor);
    }
}
