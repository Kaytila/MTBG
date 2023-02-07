package net.ck.util;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.game.Game;
import net.ck.game.map.MapTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

/**
 * So the UI Lense is the Class which acts as "Lense" over the map to figure out which
 * tiles are actually in the visible area and therefore need to be drawn.
 * Also, this is what calculates which "UI Tiles" are empty tiles and therefore black.
 * Currently, this is used from the paintComponent() Method in the Canvas but actually why?
 * As long as nothing moves, nothing can change here.
 * Something to think about at a later date
 *
 * @author Claus
 */
public class UILense
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    /**
     * Singleton
     */
    private static final UILense UILense = new UILense();

    /**
     * Singleton access - now I can use Lense in a lot of things :D
     */
    public static UILense getCurrent()
    {
        return UILense;
    }


    private final ArrayList<Boolean> xCoordinateSystem;
    private final ArrayList<Boolean> yCoordinateSystem;

    /**
     * contains the visible map tiles, as the UI Tiles are calculated
     */
    private ArrayList<MapTile> visibleMapTiles;

    public MapTile[][] mapTiles;
    private ArrayList<Point> visibleUICoordinates;

    /**
     * initialize the lens and of course stumble over add and set as always
     */
    public UILense()
    {
        logger.info("initializing lense");

        visibleMapTiles = new ArrayList<>();
        mapTiles = new MapTile[GameConfiguration.numberOfTiles][GameConfiguration.numberOfTiles];
        xCoordinateSystem = new ArrayList<>(GameConfiguration.numberOfTiles);
        yCoordinateSystem = new ArrayList<>(GameConfiguration.numberOfTiles);

        for (int i = 0; i < GameConfiguration.numberOfTiles; i++)
        {
            xCoordinateSystem.add(i, false);
            yCoordinateSystem.add(i, false);
        }
        initialize();
    }

    /**
     * something might have changed, reinitialize the Lense to defaults.
     */
    public void initialize()
    {
        getVisibleMapTiles().clear();
        for (int i = 0; i < GameConfiguration.numberOfTiles; i++)
        {
            xCoordinateSystem.set(i, false);
            yCoordinateSystem.set(i, false);
        }
    }

    /**
     * add a point hence maptile to both coordinate lists
     * this says, yes, this tile is in the visible area.
     *
     * @param p adding a UI point
     */
    public void add(Point p)
    {
        //logger.info("adding point: {}", p.toString());
        xCoordinateSystem.set(p.x, true);
        yCoordinateSystem.set(p.y, true);
    }

    /**
     * Calculates the UI Tiles that are not on the map so to speak
     *
     * @return returns an arraylist of points
     */
    public ArrayList<Point> identifyEmptyCoordinates()
    {
        ArrayList<Point> emptyTiles = new ArrayList<>();

        for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
            {
                if (xCoordinateSystem.get(column) == false)
                {
                    emptyTiles.add(new Point(column, row));
                }

                if (yCoordinateSystem.get(row) == false)
                {
                    emptyTiles.add(new Point(column, row));
                }
            }
        }
        return emptyTiles;
    }

    /**
     * This is a helper method that just lists all the visible Tiles the lense can currently see
     */
    public void listEntries()
    {
        for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
            {
                logger.info("X: {}, value: {}, Y: {}, value: {}, type: {}", column, xCoordinateSystem.get(column), row, yCoordinateSystem.get(row));
            }
        }
    }


    public ArrayList<Point> getVisibleUICoordinates()
    {
        if (visibleUICoordinates == null)
        {
            visibleUICoordinates = new ArrayList();
            for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
            {
                for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
                {
                    visibleUICoordinates.add(new Point(column, row));
                }
            }
        }
        return visibleUICoordinates;
    }

    public boolean isPointOnMap(Point p)
    {
        return Boolean.logicalAnd(xCoordinateSystem.get(p.x), yCoordinateSystem.get(p.y));
    }

    public synchronized ArrayList<MapTile> getVisibleMapTiles()
    {
        return visibleMapTiles;
    }

    public synchronized void setVisibleMapTiles(ArrayList<MapTile> visibleTiles)
    {
        this.visibleMapTiles = visibleTiles;
    }

    /**
     * identify which maptiles are visible around the player
     * currently this is a dumb list but needs to be switched to array
     * where screenposition is the x and y coordinate of the 2d array and the tile is the value.
     */
    public synchronized void identifyVisibleTilesNew()
    {
        //long start = System.nanoTime();
        getVisibleMapTiles().clear();
        for (Point p : MapUtils.getVisibleMapPointsAroundPlayer())
        {
            if ((p.x >= 0 && p.y >= 0) && (p.x < Game.getCurrent().getCurrentMap().getSize().x && p.y < Game.getCurrent().getCurrentMap().getSize().y))
            {
                MapTile tile = Game.getCurrent().getCurrentMap().mapTiles[p.x][p.y];
                if (tile != null)
                {
                    Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                    // these are the visible tiles
                    add(screenPosition);
                    getVisibleMapTiles().add(tile);
                    tile.setHidden(false);
                }
            }
        }
        logger.info("how many visible map tiles: {}", getVisibleMapTiles().size());
        //logger.info("time taken identifying tiles: {}", System.nanoTime() - start);
    }

    public synchronized void identifyVisibleTilesBroken()
    {
        long start = System.nanoTime();
        getVisibleMapTiles().clear();
        Point[][] points = MapUtils.getVisibleMapPointsAroundPlayerasArray();
        for (int i = 0; i < GameConfiguration.numberOfTiles; i++)
        {
            for (int j = 0; j < GameConfiguration.numberOfTiles; j++)
            {
                if ((points[i][j].x >= 0 && points[i][j].y >= 0) && (points[i][j].x < Game.getCurrent().getCurrentMap().getSize().x && points[i][j].y < Game.getCurrent().getCurrentMap().getSize().y))
                {
                    MapTile tile = Game.getCurrent().getCurrentMap().mapTiles[points[i][j].x][points[i][j].y];
                    if (tile != null)
                    {
                        Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                        // these are the visible tiles
                        add(screenPosition);
                        getVisibleMapTiles().add(tile);
                        tile.setHidden(false);
                    }
                }
            }
        }
        //logger.info("how many visible map tiles: {}", getVisibleMapTiles().size());
        logger.info("time taken identifying tiles array: {}", System.nanoTime() - start);
    }


    /**
     * identify which tiles of the map are currently visible
     * also set back hidden state cause this is calculated again
     * Currently doing this every paint
     * question is do I need to?
     * Think i only need to do it once something changes.
     */
  /*  private void identifyVisibleTiles()
    {
        for (MapTile tile : Game.getCurrent().getCurrentMap().getTiles())
        {
            Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
            // these are the visible tiles
            if (rangeX.contains(screenPosition.x) && rangeY.contains(screenPosition.y))
            {
                UILense.getCurrent().add(screenPosition);
                UILense.getCurrent().getVisibleMapTiles().add(tile);
                tile.setHidden(false);
            }
        }
    }*/

}
