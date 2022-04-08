package net.ck.game.backend.entities;


import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.graphics.UnanimatedRepresentation;
import net.ck.game.map.MapTile;
import net.ck.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Objects;

public class Missile
{

    private final Logger logger = LogManager.getLogger(getRealClass());
    /**
     * where is the shot fired from?
     */
    private MapTile sourceTile;
    /**
     * where is the shot fired to?
     */
    private MapTile targetTile;
    private Point currentPosition;
    private AbstractRepresentation appearance;
    private Point sourceCoordinates;
    private Point targetCoordinates;
    private boolean finished;

    public Missile(Point source, Point target)
    {
        setAppearance(new UnanimatedRepresentation(ImageUtils.loadImage("combat", "missile")));
        setSourceCoordinates(source);
        setTargetCoordinates(target);
    }
    public Missile(MapTile source, MapTile target)
    {
        setSourceTile(source);
        setTargetTile(target);
        setAppearance(new UnanimatedRepresentation(ImageUtils.loadImage("combat", "missile")));
    }

    public Point getCurrentPosition()
    {
        return currentPosition;
    }

    public void setCurrentPosition(Point currentPosition)
    {
        this.currentPosition = currentPosition;
    }

    public Logger getLogger()
    {
        return logger;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    public MapTile getSourceTile()
    {
        return sourceTile;
    }

    public void setSourceTile(MapTile sourceTile)
    {
        this.sourceTile = sourceTile;
    }

    public MapTile getTargetTile()
    {
        return targetTile;
    }

    public void setTargetTile(MapTile targetTile)
    {
        this.targetTile = targetTile;
    }

    public AbstractRepresentation getAppearance()
    {
        return appearance;
    }

    public void setAppearance(AbstractRepresentation appearance)
    {
        this.appearance = appearance;
    }

    public Point getSourceCoordinates()
    {
        return sourceCoordinates;
    }

    public void setSourceCoordinates(Point sourceCoordinates)
    {
        this.sourceCoordinates = sourceCoordinates;
    }

    public Point getTargetCoordinates()
    {
        return targetCoordinates;
    }

    public void setTargetCoordinates(Point targetCoordinates)
    {
        this.targetCoordinates = targetCoordinates;
    }
}
