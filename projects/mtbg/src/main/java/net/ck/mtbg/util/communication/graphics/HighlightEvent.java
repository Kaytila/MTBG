package net.ck.mtbg.util.communication.graphics;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;

@Getter
@Setter
@Log4j2
public class HighlightEvent extends ChangedEvent
{
    private Point mapPosition;

    public HighlightEvent(Point mapPosition)
    {
        this.mapPosition = mapPosition;
    }
}
