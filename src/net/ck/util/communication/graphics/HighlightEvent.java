package net.ck.util.communication.graphics;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class HighlightEvent extends ChangedEvent
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private Point mapPosition;

    public HighlightEvent(Point mapPosition)
    {
        this.mapPosition = mapPosition;
    }


    public Point getMapPosition()
    {
        return mapPosition;
    }

    public void setMapPosition(Point mapPosition)
    {
        this.mapPosition = mapPosition;
    }
}
