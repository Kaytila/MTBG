package net.ck.mtbg.util.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.TileTypes;
import net.ck.mtbg.ui.components.game.AbstractMapCanvas;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;
import net.ck.mtbg.weather.DayNight;
import org.apache.commons.lang3.Range;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Getter
@Setter
public class MapUtils
{

    @Getter
    private static final int middle = (int) (double) (GameConfiguration.numberOfTiles / 2);


    public static MapTile getMapTileByCoordinates(Map map, int x, int y)
    {
        return map.mapTiles[x][y];
    }


    /**
     * calculates the visible tiles based on player position.
     * will be used for drawing afterwards.
     * <p>
     * negative coordinates can be used to paint black right away
     * with a transformation to screen coordinates.
     *
     * @return a list of points (map positions) as I do not have a better map
     * utility yet
     * TODO - rewrite as array
     */
    public static List<Point> getVisibleMapPointsAroundPlayer()
    {
        List<Point> points = new ArrayList<>(middle + middle + middle + middle + 1);
        Point center = Game.getCurrent().getCurrentPlayer().getMapPosition();
        //top left corner tile

        int maxX = center.x + middle;
        int minX = center.x - middle;
        int maxY = center.y + middle;
        int minY = center.y - middle;

        for (int x = minX; x <= maxX; x++)
        {
            for (int y = minY; y <= maxY; y++)
            {
                points.add(new Point(x, y));
            }
        }
        return points;
    }

    public static Point[][] getVisibleMapPointsAroundPlayerAsArray()
    {
        Point[][] points = new Point[GameConfiguration.numberOfTiles][GameConfiguration.numberOfTiles];
        Point center = Game.getCurrent().getCurrentPlayer().getMapPosition();
        //top left corner tile

        int maxX = center.x + middle;
        int minX = center.x - middle;
        int maxY = center.y + middle;
        int minY = center.y - middle;

        for (int x = minX; x <= maxX; x++)
        {
            for (int y = minY; y <= maxY; y++)
            {
                points[x][y] = new Point(x, y);
            }
        }
        return points;
    }


    /**
     * calculates the visible tiles based on player position.
     * will be used for drawing afterwards.
     * <p>
     * negative coordinates can be used to paint black right away
     * with a transformation to screen coordinates.
     *
     * @return a list of points (map positions) as I do not have a better map
     * utility yet
     */
    public static Rectangle getVisibleRectAroundPlayer()
    {
        Point center = Game.getCurrent().getCurrentPlayer().getMapPosition();

        //int maxX = center.x + middle;
        int minX = center.x - middle;
        //int maxY = center.y + middle;
        int minY = center.y - middle;
        return new Rectangle(minX, minY, GameConfiguration.numberOfTiles, GameConfiguration.numberOfTiles);
    }


    /**
     * creates a map, all of type grassland or ocean in random with a little help from stackoverflow:
     * <a href="https://stackoverflow.com/questions/7366266/best-way-to-write-string-to-file-using-java-nio">https://stackoverflow.com/questions/7366266/best-way-to/write-string-to/file-using-java-nio</a>
     *
     * @param x size (zero indexed, so size 12 is 11)
     * @param y size (zero indexed, so size 12 is 11)
     */
    public static void createMap(int x, int y, TileTypes type)
    {
        MapPersistenceUtils.createMap(x, y, type);
    }

    /**
     * @param p - position of the tile or entity
     *          returns the point where the position of Point p is (tile) from the UI Position of the player
     */
    public static Point calculateUIPositionFromMapOffset(Point p)
    {
        Point offSet = MapUtils.calculateMapOffsetFromPlayerMapPosition(p);
        return new Point(Game.getCurrent().getCurrentPlayer().getUIPosition().x + offSet.x, Game.getCurrent().getCurrentPlayer().getUIPosition().y + offSet.y);
    }

    public static MapTile calculateMapTileUnderCursor(Point mousePosition)
    {
        int x = Math.floorDiv(mousePosition.x, GameConfiguration.tileSize);
        int y = Math.floorDiv(mousePosition.y, GameConfiguration.tileSize);
        Point offSet = calculateUIOffsetFromMapPoint();
        return getMapTileByCoordinates(x - offSet.x, y - offSet.y);
    }


    public static ArrayList<MapTile> calculateVisibleTiles(MapTile tile, int range)
    {
        return MapRenderUtils.calculateVisibleTiles(tile, range);
    }

    /**
     * @param position describes the point the mouse is at
     * @return Point with the offset as point from the player position. In case of player, this is 0,0 of course.
     */
    public static Point calculateMapOffsetFromPlayerMapPosition(Point position)
    {
        Point pP = Game.getCurrent().getCurrentPlayer().getMapPosition();

        int xDiff = position.x - pP.x;
        int yDiff = position.y - pP.y;

        return new Point(xDiff, yDiff);
    }

    /**
     * @return Point with the offset as point from the player position.
     * return x offset - negative for left, positive for right
     * return y offset - negative for up, positive for down
     */
    @NonNull
    public static Point calculateUIOffsetFromMapPoint()
    {
        Point mapPos = Game.getCurrent().getCurrentPlayer().getMapPosition();
        Point uiPos = Game.getCurrent().getCurrentPlayer().getUIPosition();
        int xDiff = uiPos.x - mapPos.x;
        int yDiff = uiPos.y - mapPos.y;

        return new Point(xDiff, yDiff);
    }


    /**
     * @param x x coordinate of the tile
     * @param y y coordinate of the tile
     * @return if the tile is blocked, so need to check for FALSE instead of TRUE
     */
    public static boolean lookAhead(int x, int y)
    {
        MapTile tile = getMapTileByCoordinates(x, y);
        if (tile != null)
        {
            return tile.isBlocked();//|| tile.isOpenable();
        }
        else
        {
            return true;
        }

    }

    /**
     * Helper Method - returns the tile at coordinates point on the currently active map
     *
     * @param p Point
     * @return the maptile which is found with P coordinates
     */
    @NonNull
    public static MapTile getMapTileByCoordinatesAsPoint(Point p)
    {
        if ((p.x >= 0) && (p.y >= 0) && (p.x < Game.getCurrent().getCurrentMap().getSize().x) && (p.y < Game.getCurrent().getCurrentMap().getSize().y))
        {
            return Game.getCurrent().getCurrentMap().mapTiles[p.x][p.y];
        }
        else
        {
            return null;
        }
    }

    /**
     * Helper Method - returns the tile at coordinates point on the currently active map
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @return the maptile which is found with P coordinates
     */
    public static MapTile getMapTileByCoordinates(int x, int y)
    {
        if ((x >= 0) && (y >= 0) && (x < Game.getCurrent().getCurrentMap().getSize().x) && (y < Game.getCurrent().getCurrentMap().getSize().y))
        {
            return Game.getCurrent().getCurrentMap().mapTiles[x][y];
        }
        else
        {
            return null;
        }
    }


    public static MapTile getMapTileByCoordinates(Point p)
    {
        return getMapTileByCoordinates(p.x, p.y);
    }


    public static void listMaps()
    {
        for (Map ma : Game.getCurrent().getMaps())
        {
            logger.info("map: {}", ma);
        }
    }

    /**
     * Bresenham's Algorithm
     *
     * @param start  a point, probably player position
     * @param target an end point
     * @return returns the list of points calculated by the direct line.
     */
    public static ArrayList<Point> getLine(Point start, Point target)
    {
        ArrayList<Point> ret = new ArrayList<>();
        int x0 = start.x;
        int y0 = start.y;

        int x1 = target.x;
        int y1 = target.y;

        int sx;
        int sy;

        int dx = Math.abs(x1 - x0);
        sx = x0 < x1 ? 1 : -1;
        int dy = -1 * Math.abs(y1 - y0);
        sy = y0 < y1 ? 1 : -1;
        int err = dx + dy, e2; /* error value e_xy */

        for (; ; )
        { /* loop */
            ret.add(new Point(x0, y0));
            if (x0 == x1 && y0 == y1)
            {
                break;
            }
            e2 = 2 * err;
            if (e2 >= dy)
            {
                err += dy;
                x0 += sx;
            } /* e_xy+e_x > 0 */
            if (e2 <= dx)
            {
                err += dx;
                y0 += sy;
            } /* e_xy+e_y < 0 */
        }
        return ret;
    }

    public static Map importUltima4MapFromCSV()
    {
        return MapPersistenceUtils.importUltima4MapFromCSV();
    }


    public static Map importMapFromTXT()
    {
        return MapPersistenceUtils.importMapFromTXT();
    }


    /**
     * <tile>
     * <id>1</id>
     * <type>LADDERUP</type>
     * <x>0</x>
     * <y>0</y>
     * <east>2</east>
     * <south>4</south>
     * <targetMap>test name</targetMap>
     * <targetID>2</targetID>
     * </tile>
     */
    public static void writeMapToXML(Map map) throws IOException
    {
        MapPersistenceUtils.writeMapToXML(map);
    }

    /**
     * Integration method for the new JSON v2 format.
     * Exports the current in-memory map to the given JSON file.
     */
    public static void exportCurrentMapAsJsonV2(Path jsonFile) throws IOException
    {
        MapPersistenceUtils.exportCurrentMapAsJsonV2(jsonFile);
    }

    /**
     * Translation method from legacy XML map format to JSON schema v2.
     */
    public static void translateXmlMapToJsonV2(Path xmlFile, Path jsonFile) throws IOException
    {
        MapPersistenceUtils.translateXmlMapToJsonV2(xmlFile, jsonFile);
    }

    private static void prettyPrint(Document document, String fileName) throws TransformerException
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }

    /**
     * so this will need to be a little bit more flexible to have more ranges for light dawn, heavy dawn
     * and light dusk and heavy dusk
     */
    public static DayNight calculateDayOrNight()
    {
        int hours = Game.getCurrent().getGameTime().getCurrentHour();

        Range<Integer> rangeDay = Range.of(8, 18);
        Range<Integer> rangeDawn = Range.of(5, 7);
        Range<Integer> rangeDusk = Range.of(19, 21);
        if (rangeDay.contains(hours))
        {
            //Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.numberOfTiles);
            return DayNight.DAY;
        }
        if (rangeDawn.contains(hours))
        {
            //Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.numberOfTiles / 4);
            return DayNight.DAWN;
        }

        if (rangeDusk.contains(hours))
        {
            //Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.numberOfTiles / 4);
            return DayNight.DUSK;
        }
        //night
        //Game.getCurrent().getCurrentMap().setVisibilityRange(1);
        return DayNight.NIGHT;
    }

    /**
     *
     */
    public static void setVisibility(DayNight dayNight)
    {
        switch (dayNight)
        {
            case NIGHT -> Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.nightVisibility);
            case DAY -> Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.numberOfTiles);
            case DAWN -> Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.dawnVisibility);
            case DUSK -> Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.duskVisibility);
        }
    }

    /**
     * method calculates whether a point is adjacient to another one.
     *
     * @param source - source coordinates
     * @param target - target coordinates
     * @return true if in the range or false
     */
    public static boolean isAdjacent(Point source, Point target)
    {
        Range<Integer> range = Range.of(-1, 1);

        return (range.contains(source.x - target.x)) && (range.contains(source.y - target.y));
    }

    /**
     * this is used to properly calculate the tiles before they are drawn
     * ideally, there is no more logic in paintcomponent but everything is moved here.
     * <p>
     * what needs to be done here still:
     * <p>
     * calculate darkness
     * pre-calculate brightened up images
     *
     * @param g - Grahpics context
     */
    public static void calculateTiles(Graphics g)
    {
        MapRenderUtils.calculateTiles(g);
    }

    /**
     * @param t maptile
     * @return true if there is a lightsource in max distance
     */
    private static boolean checkForLightSourceAround(MapTile t)
    {
        ArrayList<MapTile> mapTiles = getMapTilesAroundPointByDistance(t, GameConfiguration.maxLightSourceDistance);
        for (MapTile tile : mapTiles)
        {
            // check furniture on the tile
            if (tile.getFurniture() != null)
            {
                if (tile.getFurniture().isLightSource())
                {
                    if (tile.getFurniture().isBurning())
                    {
                        int lightRange = tile.getFurniture().getLightRange();
                        if (calculateMaxDistance(t.getMapPosition(), tile.getMapPosition()) <= lightRange)
                        {
                            return true;
                        }
                    }
                }
            }

            // check items in the tile's inventory (e.g. a torch lying on the floor)
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
                            if (calculateMaxDistance(t.getMapPosition(), tile.getMapPosition()) <= lightRange)
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

    public static Point calculateMapSize(ArrayList<MapTile> maptiles)
    {
        return MapPersistenceUtils.calculateMapSize(maptiles);
    }

    /**
     * @param tP targetPosition
     * @param pP playerPosition
     * @return the direction
     */
    public static Directions calculateDirectionOfMapTileFromPlayer(Point tP, Point pP)
    {
        String value = "";

        if (pP.y > tP.y)
        {
            //logger.info("point {} is to top of player {}", tP, pP);
            value += "N";
        }
        else if (pP.y == tP.y)
        {
            //logger.info("point {} is at same y as player {}", tP, pP);
            value = "";
        }
        else //(pP.y < tP.y)
        {
            //logger.info("point {} is to the bottom of player {}", tP, pP);
            value += "S";
        }

        if (pP.x > tP.x)
        {
            //logger.info("point {} is to left of player {}", tP, pP);
            value += "W";
        }
        else if (pP.x == tP.x)
        {
            //logger.info("point {} is at same x as player {}", tP, pP);
            value += "";
        }
        else //(pP.x < tP.x)
        {
            //logger.info("point {} is to the right of player {}", tP, pP);
            value += "E";
        }
        //logger.info("direction string value: {}", value);
        return Directions.valueOf(value);
    }

    public static Directions invertDirection(Directions sourceDir)
    {
        return switch (sourceDir)
        {
            case N -> Directions.S;
            case NE -> Directions.SW;
            case E -> Directions.W;
            case SE -> Directions.NW;
            case S -> Directions.N;
            case SW -> Directions.NE;
            case W -> Directions.E;
            case NW -> Directions.SE;
        };
    }

    public static MapTile calculateTileByDirection(Point pos, Directions targetDir)
    {
        return switch (targetDir)
        {
            case N -> Game.getCurrent().getCurrentMap().mapTiles[pos.x][pos.y - 1];
            case NE -> Game.getCurrent().getCurrentMap().mapTiles[pos.x + 1][pos.y - 1];
            case E -> Game.getCurrent().getCurrentMap().mapTiles[pos.x + 1][pos.y];
            case SE -> Game.getCurrent().getCurrentMap().mapTiles[pos.x + 1][pos.y + 1];
            case S -> Game.getCurrent().getCurrentMap().mapTiles[pos.x][pos.y + 1];
            case SW -> Game.getCurrent().getCurrentMap().mapTiles[pos.x - 1][pos.y + 1];
            case W -> Game.getCurrent().getCurrentMap().mapTiles[pos.x - 1][pos.y];
            case NW -> Game.getCurrent().getCurrentMap().mapTiles[pos.x - 1][pos.y - 1];
        };
    }

    public static int calculateMaxDistance(Point mapPosition, Point mapPosition1)
    {
        int xDistance = Math.abs(mapPosition.x - mapPosition1.x);
        int yDistance = Math.abs(mapPosition.y - mapPosition1.y);
        return (Math.max(xDistance, yDistance));
    }

    public static boolean lookAheadForTile(Point mapPos, KeyboardActionType type)
    {
        int x = mapPos.x;
        int y = mapPos.y;

        switch (type)
        {
            case EAST:
                x++;
                break;
            case SOUTH:
                y++;
                break;
            case WEST:
                x--;
                break;
            case NORTH:
                y--;
                break;
        }
        //logger.debug("looking at x: {}, y: {}", x , y);
        return getMapTileByCoordinates(x, y) != null;
    }

    public static void translateTextMaps()
    {
        MapPersistenceUtils.translateTextMaps();
    }

    /**
     * @param size     has max x and max y
     * @param mapTiles has the arraylist, translate into array just like in xml parser
     * @return 2d array
     */
    private static MapTile[][] calculateMapTileArray(ArrayList<MapTile> mapTiles, Point size)
    {
        MapTile[][] tileArray = new MapTile[size.x][size.y];

        for (MapTile tile : mapTiles)
        {
            tileArray[tile.x][tile.y] = tile;
        }

        return tileArray;
    }

    public static int parseMapLine(Map map, String line, int lineIndex, ArrayList<MapTile> tiles, int id)
    {
        return MapPersistenceUtils.parseMapLine(map, line, lineIndex, tiles, id);
    }

    public static void translateJSONMap()
    {
        MapPersistenceUtils.translateJSONMap();
    }

    private static TileTypes mapTXTtoTerrainTypes(String s)
    {
        return switch (s)
        {
            case ("S") -> TileTypes.STONEWALL;
            case ("w") -> TileTypes.WOODFLOOR;
            case ("G") -> TileTypes.GATECLOSED;
            case ("D") -> TileTypes.WOODDOORCLOSED;
            case (".") -> TileTypes.GRASS;
            default -> null;
        };
    }

    public static void calculateVisibleTileImages(Graphics graphics)
    {
        MapRenderUtils.calculateVisibleTileImages(graphics);
    }

    public static void calculateAllTileImages(Map map)
    {
        MapRenderUtils.calculateAllTileImages(map);
    }

    public static void calculateAllTileImages(Map map, Graphics graphics, AbstractMapCanvas canvas, int x, int y)
    {
        MapRenderUtils.calculateAllTileImages(map, graphics, canvas, x, y);
    }


    /**
     * returns the first tile where a light source is and its burning during day, or not during the night
     *
     * @param tile  - the source map tile
     * @param range - determines how big the range to check is
     * @return the maptile closest which contains a light source
     */
    public static MapTile getClosestLightSourceInVicinity(MapTile tile, int range, boolean burning)
    {
        return MapRenderUtils.getClosestLightSourceInVicinity(tile, range, burning);
    }


    public static Point getUICoordinateUnderCursor(Point point)
    {
        Point uiCoordinate = new Point(point.x / GameConfiguration.tileSize, point.y / GameConfiguration.tileSize);
        logger.debug("UI Coordinate: {}", uiCoordinate);
        return uiCoordinate;
    }


    public static void calculateVisibleTilesAroundPlayer(Graphics graphics)
    {
        MapRenderUtils.calculateVisibleTilesAroundPlayer(graphics);
    }

    /**
     * Fast adjacency check in lense coordinates (player-to-tile), avoids the * {@code Range.of(...)} allocation that {@link #isAdjacent(Point, Point)} does.
     */
    private static boolean isAdjacentInLense(int ax, int ay, int bx, int by)
    {
        final int dx = ax - bx;
        final int dy = ay - by;
        return dx >= -1 && dx <= 1 && dy >= -1 && dy <= 1;
    }

    /**
     * Builds the composite image for a single visible {@link MapTile}: hidden tiles * return a shared black image; visible tiles get a brightened background plus * furniture or first inventory item overlay.
     */
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
