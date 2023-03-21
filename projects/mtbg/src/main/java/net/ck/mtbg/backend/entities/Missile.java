package net.ck.mtbg.backend.entities;


import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Missile
{

    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    /**
     * where is the shot fired from?
     */
    private MapTile sourceTile;
    /**
     * where is the shot fired to?
     */
    private MapTile targetTile;
    private Point currentPosition;

    private Point sourceCoordinates;
    private Point targetCoordinates;
    private boolean finished;

    private BufferedImage standardImage;

    public ArrayList<Point> getLine()
    {
        return line;
    }

    private ArrayList<Point> line;

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        logger.info("setting success: {}", success);
        this.success = success;
    }

    private boolean success;

    public Missile(Point source, Point target)
    {
        setStandardImage(ImageUtils.loadImage("combat", "missile"));
        setSourceCoordinates(source);
        setTargetCoordinates(target);
    }

    public Missile(MapTile source, MapTile target)
    {
        setSourceTile(source);
        setTargetTile(target);
        setStandardImage(ImageUtils.loadImage("combat", "missile"));
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

    @Override
    public String toString()
    {
        return "Missile{" +
                "sourceTile=" + sourceTile +
                ", targetTile=" + targetTile +
                ", currentPosition=" + currentPosition +
                ", standardImage=" + standardImage +
                ", sourceCoordinates=" + sourceCoordinates +
                ", targetCoordinates=" + targetCoordinates +
                ", finished=" + finished +
                ", line=" + line +
                ", success=" + success +
                '}';
    }

    public void setFinished(boolean finished)
    {
        logger.info("finished missile");
        this.finished = finished;
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

    public void setLine(ArrayList<Point> line)
    {
        this.line = line;
    }

    public void setStandardImage(BufferedImage loadImage)
    {
        standardImage = loadImage;
    }

    public BufferedImage getStandardImage()
    {
        return standardImage;
    }
}
