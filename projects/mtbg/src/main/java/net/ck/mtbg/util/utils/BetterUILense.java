package net.ck.mtbg.util.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditorApplication;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.controllers.MapEditorController;

import java.awt.*;

@Getter
@Setter
@Log4j2
public class BetterUILense
{
    /**
     * Singleton
     */
    private static final BetterUILense UILense = new BetterUILense();

    /**
     * maptiles of the lense
     */
    public MapTile[][] maptiles;

    /**
     * max dimension of size of what can be shown
     */
    private Point visibleDimension = new Point(12, 12);

    /**
     * Singleton access - now I can use Lense in a lot of things :D
     */
    public static BetterUILense getCurrent()
    {
        return UILense;
    }

    public void updateVisibleDimension()
    {
        visibleDimension = MapEditorApplication.getCurrent().getMap().getSize();
    }

    public void updateVisibleTiles()
    {
        maptiles = new MapTile[visibleDimension.x][visibleDimension.y];
        int startX = MapEditorController.getCurrent().getScrollPositionX();
        int startY = MapEditorController.getCurrent().getScrollPositionY();
        for (int row = 0; row < visibleDimension.x; row++)
        {
            for (int column = 0; column < visibleDimension.y; column++)
            {
                MapTile tile = MapEditorApplication.getCurrent().getMap().mapTiles[startX + column][startY + row];
                maptiles[row][column] = tile;
            }
        }
    }

}
