package net.ck.mtbg.util.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.map.MapTile;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * So the UI Lense is the Class which acts as "Lense" over the map to figure out which
 * tiles are actually in the visible area and therefore need to be drawn.
 * Just keeping it for
 *
 * @author Claus
 */
@Getter
@Setter
@Log4j2
public class UILense
{
    /**
     * Singleton
     */
    private static final UILense UILense = new UILense();

    /**
     * includes the maptiles that actually are visible on the screen
     */
    public MapTile[][] mapTiles;

    /**
     * includes an additional row and column of maptiles
     */
    public MapTile[][] bufferedMapTiles;

    /**
     * initialize the lens and of course stumble over add and set as always
     */
    public UILense()
    {
        logger.info("initializing lense");
        mapTiles = new MapTile[GameConfiguration.numberOfTiles][GameConfiguration.numberOfTiles];
        bufferedMapTiles = new MapTile[GameConfiguration.numberOfTiles + 2][GameConfiguration.numberOfTiles + 2];
    }

    /**
     * Singleton access - now I can use Lense in a lot of things :D
     */
    public static UILense getCurrent()
    {
        return UILense;
    }

    /**
     * identify the map tiles around player:
     * <p>
     * in a numberofTiles 2D array
     * <p>
     * |00|01|02|03|
     * ------------------------
     * 00|xx|mt|mt|mt|
     * 01|xx|mt|mt|mt|
     * 02|xx|mt|mt|mt|
     */
    public synchronized void identifyVisibleTilesBest()
    {
        //long start = System.nanoTime();
        Point offSet = MapUtils.calculateUIOffsetFromMapPoint();
        for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
            {
                int x = row - offSet.x;
                int y = column - offSet.y;
                if ((x >= 0) && (y >= 0) && (x < Game.getCurrent().getCurrentMap().getSize().x) && (y < Game.getCurrent().getCurrentMap().getSize().y))
                {
                    mapTiles[row][column] = Game.getCurrent().getCurrentMap().mapTiles[x][y];
                }
                else
                {
                    mapTiles[row][column] = null;
                }
                //logger.info("new maptile calculation {}, {}, {}", row, column, mapTiles[row][column]);
            }
        }
        //logger.info("time taken identifying tiles best: {}", System.nanoTime() - start);
    }

    public synchronized void identifyBufferedTiles()
    {
        long start = System.nanoTime();
        boolean firstRow = true;
        boolean firstColumn = true;
        boolean lastRow = true;
        boolean lastColumn = true;

        Point offSet = MapUtils.calculateUIOffsetFromMapPoint();
        for (int row = 0; row < GameConfiguration.numberOfTiles + 1; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles + 1; column++)
            {
                int x;
                int y;
                //TODO somewhere here, there is a small issue with the edges
                //TODO the optimazation for the edges does not work properly
                if (row == 0)
                {
                    if (firstRow)
                    {
                        x = row - offSet.x - 1;
                        firstRow = false;
                    }
                    else
                    {
                        x = row - offSet.x;
                    }
                }
                else if (column == GameConfiguration.numberOfTiles)
                {
                    if (lastRow == true)
                    {
                        x = row - offSet.x + 1;
                        lastRow = false;
                    }
                    else
                    {
                        x = row - offSet.x;
                    }
                }
                else
                {
                    x = row - offSet.x;
                }

                if (column == 0)
                {
                    if (firstColumn)
                    {
                        y = column - offSet.y - 1;
                        firstColumn = false;
                    }
                    else
                    {
                        y = column - offSet.y;
                    }
                }
                else if (row == GameConfiguration.numberOfTiles)
                {
                    if (lastColumn == true)
                    {
                        y = column - offSet.y + 1;
                        lastColumn = false;
                    }
                    else
                    {
                        y = column - offSet.y;
                    }
                }
                else
                {
                    y = column - offSet.y;
                }

                if ((x >= 0) && (y >= 0) && (x < Game.getCurrent().getCurrentMap().getSize().x) && (y < Game.getCurrent().getCurrentMap().getSize().y))
                {
                    bufferedMapTiles[row][column] = Game.getCurrent().getCurrentMap().mapTiles[x][y];
                }
                else
                {
                    bufferedMapTiles[row][column] = null;
                }
                //logger.info("new maptile calculation {}, {}, {}", row, column, mapTiles[row][column]);
            }
        }
        if (GameConfiguration.debugPaint == true)
        {
            long convert = TimeUnit.MICROSECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            logger.info("calculation time extended lense in microseconds: {}", convert);
        }
    }
}
