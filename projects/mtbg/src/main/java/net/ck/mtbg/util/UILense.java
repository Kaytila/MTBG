package net.ck.mtbg.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.map.MapTile;

import java.awt.*;

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

    public MapTile[][] mapTiles;

    /**
     * initialize the lens and of course stumble over add and set as always
     */
    public UILense()
    {
        logger.info("initializing lense");
        mapTiles = new MapTile[GameConfiguration.numberOfTiles][GameConfiguration.numberOfTiles];
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

}
