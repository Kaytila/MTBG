package net.ck.mtbg.backend.entities;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public class Missile
{
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

    private ArrayList<Point> line;
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
}
