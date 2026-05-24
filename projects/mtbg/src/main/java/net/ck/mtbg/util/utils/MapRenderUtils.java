package net.ck.mtbg.util.utils;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.components.game.AbstractMapCanvas;
import net.ck.mtbg.util.ui.WindowBuilder;
import org.apache.commons.lang3.Range;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Log4j2
public final class MapRenderUtils
{
    private MapRenderUtils()
    {
    }

    public static ArrayList<MapTile> calculateVisibleTiles(MapTile tile, int range)
    {
        ArrayList<MapTile> visibleTiles = new ArrayList<>();
        Rectangle visibleRect = new Rectangle(tile.x - range, tile.y - range, range + range, range + range);
        Range<Integer> rangeX = Range.of(visibleRect.x, visibleRect.x + (int) visibleRect.getWidth());
        Range<Integer> rangeY = Range.of(visibleRect.y, visibleRect.y + (int) visibleRect.getHeight());

        for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
            {
                if (UILense.getCurrent().mapTiles[row][column] == null)
                {
                    continue;
                }
                if ((rangeY.contains(UILense.getCurrent().mapTiles[row][column].getY()) && (rangeX.contains(UILense.getCurrent().mapTiles[row][column].getX()))))
                {
                    visibleTiles.add(UILense.getCurrent().mapTiles[row][column]);
                }
            }
        }
        return visibleTiles;
    }

    public static void calculateTiles(Graphics g)
    {
        for (int row = 0; row < GameConfiguration.numberOfTiles + 2; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles + 2; column++)
            {
                MapTile t = UILense.getCurrent().bufferedMapTiles[row][column];

                if (t == null)
                {
                    continue;
                }
                t.setHidden(false);
                t.setBrightenFactor(0);

                if (checkForLightSourceAround(t))
                {
                    t.setBrightenFactor(1);
                }
            }
        }

        int pX = Game.getCurrent().getCurrentPlayer().getUIPosition().x;
        int pY = Game.getCurrent().getCurrentPlayer().getUIPosition().y;
        int visibilityRange = Game.getCurrent().getCurrentMap().getVisibilityRange();

        for (int row = 0; row < GameConfiguration.numberOfTiles + 2; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles + 2; column++)
            {
                boolean blocked = false;
                boolean first = true;
                BufferedImage img;
                MapTile t = UILense.getCurrent().bufferedMapTiles[row][column];
                if (t == null)
                {
                    continue;
                }

                if (Math.abs(row - pX) > visibilityRange || Math.abs(column - pY) > visibilityRange)
                {
                    if (t.getBrightenFactor() <= 0)
                    {
                        t.setHidden(true);
                        continue;
                    }
                }

                if (GameConfiguration.calculateBrightenUpImageInPaint == false)
                {
                    t.setBrightenedImage(null);
                    img = ImageUtils.getTileTypeImages().get(t.getType()).get(WindowBuilder.getGridCanvas().getCurrentBackgroundImage());
                }

                if (t.getFurniture() != null)
                {
                    FurnitureItem item = t.getFurniture();
                    if (item.isLightSource() && item.isBurning())
                    {
                        int lightrange = item.getLightRange();
                        ArrayList<MapTile> tiles = calculateVisibleTiles(t, lightrange);
                        for (MapTile tile : tiles)
                        {
                            if (GameConfiguration.calculateBrightenUpImageInPaint == false)
                            {
                                tile.setBrightenedImage(ImageUtils.brightenUpImage(img, tile.getBrightenFactor(), tile.getBrightenFactor()));
                            }
                            else
                            {
                                tile.setBrightenFactor(1);
                            }
                        }
                    }
                }

                if (!t.getInventory().isEmpty())
                {
                    for (int invIdx = 0; invIdx < t.getInventory().getSize(); invIdx++)
                    {
                        AbstractItem invItem = t.getInventory().get(invIdx);
                        if (invItem instanceof FurnitureItem lightItem && lightItem.isLightSource() && lightItem.isBurning())
                        {
                            int lightrange = lightItem.getLightRange();
                            ArrayList<MapTile> tiles = calculateVisibleTiles(t, lightrange);
                            for (MapTile tile : tiles)
                            {
                                if (GameConfiguration.calculateBrightenUpImageInPaint == false)
                                {
                                    tile.setBrightenedImage(ImageUtils.brightenUpImage(img, tile.getBrightenFactor(), tile.getBrightenFactor()));
                                }
                                else
                                {
                                    tile.setBrightenFactor(1);
                                }
                            }
                        }
                    }
                }

                if (GameConfiguration.calculateBrightenUpImageInPaint == false)
                {
                    if (t.getBrightenedImage() == null)
                    {
                        int absX = Math.abs(pX - row);
                        int absY = Math.abs(pY - column);
                        t.setBrightenedImage(ImageUtils.brightenUpImage(img, absX, absY));
                    }
                }
                else
                {
                    if (t.getBrightenFactor() <= 0)
                    {
                        int absX = Math.abs(pX - row);
                        int absY = Math.abs(pY - column);
                        t.setBrightenFactor(Math.max(absX, absY));
                    }
                }

                ArrayList<Point> line = MapUtils.getLine(Game.getCurrent().getCurrentPlayer().getUIPosition(), new Point(row, column));
                for (Point po : line)
                {
                    MapTile tl = UILense.getCurrent().bufferedMapTiles[po.x][po.y];

                    if (tl == null)
                    {
                        continue;
                    }
                    if (GameConfiguration.debugLOS)
                    {
                        g.setColor(Color.YELLOW);
                        g.drawLine(Game.getCurrent().getCurrentPlayer().getUIPosition().x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2),
                                Game.getCurrent().getCurrentPlayer().getUIPosition().x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2),
                                po.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2),
                                po.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2));
                    }
                    if (tl.isBlocksLOS())
                    {
                        blocked = true;
                        if (first)
                        {
                            first = false;
                            continue;
                        }
                        else
                        {
                            if (!MapUtils.isAdjacent(tl.getMapPosition(), Game.getCurrent().getCurrentPlayer().getMapPosition()))
                            {
                                tl.setHidden(true);
                                if (GameConfiguration.debugLOS)
                                {
                                    g.setColor(Color.GRAY);
                                    g.drawString("B", po.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), po.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2));
                                }
                            }
                        }
                    }
                    if (blocked)
                    {
                        if (!MapUtils.isAdjacent(tl.getMapPosition(), Game.getCurrent().getCurrentPlayer().getMapPosition()))
                        {
                            tl.setHidden(true);
                            if (GameConfiguration.debugLOS)
                            {
                                g.setColor(Color.GRAY);
                                g.drawString("B", po.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), po.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2));
                            }
                        }
                    }
                }

                if (!t.isHidden())
                {
                    t.setDiscovered(true);
                    if (GameConfiguration.debugDiscovered)
                    {
                        g.drawString("D", row * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), column * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2));
                    }
                }
            }
        }
    }

    public static void calculateVisibleTileImages(Graphics graphics)
    {
        long start = System.nanoTime();
        for (int row = 0; row < GameConfiguration.numberOfTiles + 2; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles + 2; column++)
            {
                MapTile t = UILense.getCurrent().bufferedMapTiles[row][column];
                if (t == null)
                {
                    continue;
                }

                if (t.isHidden())
                {
                    t.setCalculatedImage(ImageUtils.createImage(Color.BLACK, GameConfiguration.tileSize));
                }
                else
                {
                    BufferedImage image = new BufferedImage(GameConfiguration.tileSize, GameConfiguration.tileSize, BufferedImage.TYPE_INT_ARGB);
                    Graphics g = image.getGraphics();

                    BufferedImage bgImage = ImageUtils.getTileTypeImages().get(t.getType()).get(WindowBuilder.getGridCanvas().getCurrentBackgroundImage());
                    g.drawImage(ImageUtils.brightenUpImage(bgImage, t.getBrightenFactor(), t.getBrightenFactor()), 0, 0, null);

                    if (t.getFurniture() != null)
                    {
                        g.drawImage(t.getFurniture().getItemImage(), 0, 0, null);
                    }
                    else if ((t.getInventory().isEmpty() == false) && (t.getInventory().get(0) != null))
                    {
                        g.drawImage(t.getInventory().get(0).getItemImage(), 0, 0, null);
                    }
                    t.setCalculatedImage(image);
                    t.setDiscovered(true);
                }
            }
        }
        if (GameConfiguration.debugPaint == true)
        {
            long convert = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            logger.debug("calculation time: {}", convert);
        }
    }

    public static void calculateAllTileImages(Map map)
    {
        long start = System.nanoTime();

        for (int row = 0; row < map.getSize().y; row++)
        {
            for (int column = 0; column < map.getSize().x; column++)
            {
                MapTile t = map.mapTiles[row][column];
                if (t == null)
                {
                    continue;
                }

                BufferedImage image = new BufferedImage(GameConfiguration.tileSize, GameConfiguration.tileSize, BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();

                BufferedImage bgImage = ImageUtils.getTileTypeImages().get(t.getType()).get(WindowBuilder.getGridCanvas().getCurrentBackgroundImage());
                g.drawImage(bgImage, 0, 0, null);

                if (GameConfiguration.drawFurnitureOnAutoMap == true)
                {
                    if (t.getFurniture() != null)
                    {
                        g.drawImage(t.getFurniture().getItemImage(), 0, 0, null);
                    }
                    else if ((t.getInventory().isEmpty() == false) && (t.getInventory().get(0) != null))
                    {
                        g.drawImage(t.getInventory().get(0).getItemImage(), 0, 0, null);
                    }
                    t.setCalculatedImage(image);
                }
            }
        }
        if (GameConfiguration.debugPaint == true)
        {
            long convert = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            logger.debug("calculation time: {}", convert);
        }
    }

    public static void calculateAllTileImages(Map map, Graphics graphics, AbstractMapCanvas canvas, int x, int y)
    {
        long start = System.nanoTime();

        for (int row = 0; row < y; row++)
        {
            for (int column = 0; column < x; column++)
            {
                MapTile t = map.mapTiles[row][column];
                if (t == null)
                {
                    continue;
                }

                BufferedImage image = new BufferedImage(GameConfiguration.tileSize, GameConfiguration.tileSize, BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();

                BufferedImage bgImage = ImageUtils.getTileTypeImages().get(t.getType()).get(canvas.getCurrentBackgroundImage());
                g.drawImage(bgImage, 0, 0, null);

                if (GameConfiguration.drawFurnitureOnAutoMap == true)
                {
                    if (t.getFurniture() != null)
                    {
                        g.drawImage(t.getFurniture().getItemImage(), 0, 0, null);
                    }
                    else if ((t.getInventory().isEmpty() == false) && (t.getInventory().get(0) != null))
                    {
                        g.drawImage(t.getInventory().get(0).getItemImage(), 0, 0, null);
                    }
                    t.setCalculatedImage(image);
                }
            }
        }
        if (GameConfiguration.debugPaint == true)
        {
            long convert = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            logger.debug("calculation time: {}", convert);
        }
    }

    public static MapTile getClosestLightSourceInVicinity(MapTile tile, int range, boolean burning)
    {
        ArrayList<MapTile> visibleTiles = getMapTilesAroundPointByDistance(tile, range);

        for (MapTile t : visibleTiles)
        {
            if (t.getFurniture() != null)
            {
                if (t.getFurniture().isLightSource())
                {
                    if (t.getFurniture().isBurning() == burning)
                    {
                        return t;
                    }
                }
            }
        }

        return null;
    }

    public static void calculateVisibleTilesAroundPlayer(Graphics graphics)
    {
        final long start = System.nanoTime();

        final int tileSize = GameConfiguration.tileSize;
        final int numberOfTiles = GameConfiguration.numberOfTiles;
        final int half = numberOfTiles / 2;
        final Point playerMapPos = Game.getCurrent().getCurrentPlayer().getMapPosition();
        final Point mapSize = Game.getCurrent().getCurrentMap().getSize();
        final int visibilityRange = Game.getCurrent().getCurrentMap().getVisibilityRange();
        final MapTile[][] world = Game.getCurrent().getCurrentMap().mapTiles;
        final MapTile[][] lense = UILense.getCurrent().mapTiles;
        final int bgIndex = WindowBuilder.getGridCanvas().getCurrentBackgroundImage();
        final BufferedImage blackTile = ImageUtils.createImage(Color.BLACK, tileSize);
        final int originX = playerMapPos.x - half;
        final int originY = playerMapPos.y - half;

        for (int uiRow = 0; uiRow < numberOfTiles; uiRow++)
        {
            final int mapX = originX + uiRow;
            for (int uiCol = 0; uiCol < numberOfTiles; uiCol++)
            {
                final int mapY = originY + uiCol;

                if (mapX < 0 || mapY < 0 || mapX >= mapSize.x || mapY >= mapSize.y)
                {
                    lense[uiRow][uiCol] = null;
                    continue;
                }

                final MapTile tile = world[mapX][mapY];
                lense[uiRow][uiCol] = tile;
                if (tile == null)
                {
                    continue;
                }

                tile.setHidden(false);
                tile.setBrightenFactor(checkForLightSourceAround(tile) ? 1 : 0);
            }
        }

        final int playerUiX = half;
        final int playerUiY = half;

        for (int uiRow = 0; uiRow < numberOfTiles; uiRow++)
        {
            for (int uiCol = 0; uiCol < numberOfTiles; uiCol++)
            {
                final MapTile target = lense[uiRow][uiCol];
                if (target == null)
                {
                    continue;
                }

                if (Math.abs(uiRow - playerUiX) > visibilityRange || Math.abs(uiCol - playerUiY) > visibilityRange)
                {
                    if (target.getBrightenFactor() <= 0)
                    {
                        target.setHidden(true);
                        continue;
                    }
                }

                int x0 = playerUiX;
                int y0 = playerUiY;
                final int x1 = uiRow;
                final int y1 = uiCol;
                final int dxAbs = Math.abs(x1 - x0);
                final int dyAbsNeg = -Math.abs(y1 - y0);
                final int sx = x0 < x1 ? 1 : -1;
                final int sy = y0 < y1 ? 1 : -1;
                int err = dxAbs + dyAbsNeg;

                boolean blocked = false;
                boolean firstStep = true;

                while (true)
                {
                    final MapTile tl = lense[x0][y0];
                    if (tl != null)
                    {
                        if (tl.isBlocksLOS())
                        {
                            if (firstStep)
                            {
                                firstStep = false;
                            }
                            else if (!isAdjacentInLense(x0, y0, playerUiX, playerUiY))
                            {
                                tl.setHidden(true);
                            }
                            blocked = true;
                        }
                        else if (blocked && !isAdjacentInLense(x0, y0, playerUiX, playerUiY))
                        {
                            tl.setHidden(true);
                        }
                    }

                    if (x0 == x1 && y0 == y1)
                    {
                        break;
                    }
                    final int e2 = 2 * err;
                    if (e2 >= dyAbsNeg)
                    {
                        err += dyAbsNeg;
                        x0 += sx;
                    }
                    if (e2 <= dxAbs)
                    {
                        err += dxAbs;
                        y0 += sy;
                    }
                }

                if (!target.isHidden())
                {
                    target.setDiscovered(true);
                }
            }
        }

        for (int uiRow = 0; uiRow < numberOfTiles; uiRow++)
        {
            for (int uiCol = 0; uiCol < numberOfTiles; uiCol++)
            {
                final MapTile tile = lense[uiRow][uiCol];
                if (tile == null)
                {
                    continue;
                }
                tile.setCalculatedImage(buildTileImage(tile, tileSize, bgIndex, blackTile));
            }
        }

        if (GameConfiguration.debugPaint)
        {
            long convert = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            logger.debug("calculateVisibleTilesAroundPlayer (LoS+render): {} ms", convert);
        }
    }

    private static boolean checkForLightSourceAround(MapTile t)
    {
        ArrayList<MapTile> mapTiles = getMapTilesAroundPointByDistance(t, GameConfiguration.maxLightSourceDistance);
        for (MapTile tile : mapTiles)
        {
            if (tile.getFurniture() != null)
            {
                if (tile.getFurniture().isLightSource() && tile.getFurniture().isBurning())
                {
                    int lightRange = tile.getFurniture().getLightRange();
                    if (MapUtils.calculateMaxDistance(t.getMapPosition(), tile.getMapPosition()) <= lightRange)
                    {
                        return true;
                    }
                }
            }

            if (!tile.getInventory().isEmpty())
            {
                for (int i = 0; i < tile.getInventory().getSize(); i++)
                {
                    AbstractItem item = tile.getInventory().get(i);
                    if (item instanceof FurnitureItem lightItem)
                    {
                        if (lightItem.isLightSource() && lightItem.isBurning())
                        {
                            int lightRange = lightItem.getLightRange();
                            if (MapUtils.calculateMaxDistance(t.getMapPosition(), tile.getMapPosition()) <= lightRange)
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static ArrayList<MapTile> getMapTilesAroundPointByDistance(MapTile t, int lightSourceDistance)
    {
        Range<Integer> range = Range.of(Math.negateExact(lightSourceDistance), lightSourceDistance);
        ArrayList<MapTile> allTilesAroundTile = new ArrayList<>();

        for (int column = 0; column < Game.getCurrent().getCurrentMap().getSize().x; column++)
        {
            for (int row = 0; row < Game.getCurrent().getCurrentMap().getSize().y; row++)
            {
                MapTile otherTile = Game.getCurrent().getCurrentMap().mapTiles[column][row];
                if (range.contains(t.x - otherTile.x) && (range.contains(t.y - otherTile.y)))
                {
                    allTilesAroundTile.add(otherTile);
                }
            }
        }
        return allTilesAroundTile;
    }

    private static boolean isAdjacentInLense(int ax, int ay, int bx, int by)
    {
        final int dx = ax - bx;
        final int dy = ay - by;
        return dx >= -1 && dx <= 1 && dy >= -1 && dy <= 1;
    }

    private static BufferedImage buildTileImage(MapTile tile, int tileSize, int bgIndex, BufferedImage blackTile)
    {
        if (tile.isHidden())
        {
            return blackTile;
        }

        final BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = image.getGraphics();
        try
        {
            final BufferedImage bg = ImageUtils.getTileTypeImages().get(tile.getType()).get(bgIndex);
            g.drawImage(ImageUtils.brightenUpImage(bg, tile.getBrightenFactor(), tile.getBrightenFactor()), 0, 0, null);

            if (tile.getFurniture() != null)
            {
                g.drawImage(tile.getFurniture().getItemImage(), 0, 0, null);
            }
            else if (!tile.getInventory().isEmpty() && tile.getInventory().get(0) != null)
            {
                g.drawImage(tile.getInventory().get(0).getItemImage(), 0, 0, null);
            }
        }
        finally
        {
            g.dispose();
        }
        return image;
    }
}

