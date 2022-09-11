package net.ck.util.communication.graphics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Objects;

public class HighlightEvent extends ChangedEvent
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    private Point mapPosition;

    public HighlightEvent(Point mapPosition)
    {
        this.mapPosition = mapPosition;
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
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
