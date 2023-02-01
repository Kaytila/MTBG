package net.ck.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.Player;
import net.ck.game.backend.game.Game;
import net.ck.game.graphics.TileTypes;
import net.ck.game.map.AbstractMap;
import net.ck.game.map.Map;
import net.ck.game.map.MapTile;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapUtils
{

    private static final Logger logger = LogManager.getLogger(MapUtils.class);

    private static final int middle = (int) Math.floor(GameConfiguration.numberOfTiles / 2);

    public static int getMiddle()
    {
        return middle;
    }

    public static int getIDOfMapTileEast(MapTile tile)
    {
        return tile.getEast().getId();
    }

    public static int getIDOfMapTileNorth(MapTile tile)
    {
        return tile.getNorth().getId();
    }

    public static int getIDOfMapTileSouth(MapTile tile)
    {
        return tile.getSouth().getId();
    }

    public static int getIDOfMapTileWest(MapTile tile)
    {
        return tile.getWest().getId();
    }

    /**
     * Helper Method because I am lazy not actually sure how useful this in a real context is, but for testing purposes
     *
     * @param map the map to search
     * @param ID  the id
     * @return the MapTile the id matches
     */
    public static MapTile getMapTileByID(AbstractMap map, int ID)
    {
        for (MapTile tile : map.getTiles())
        {
            if (tile.getId() == ID)
            {
                return tile;
            }
        }
        return null;
    }


    public static Point calculateMapSize(AbstractMap map)
    {
        int x = 0;
        int y = 0;
        for (MapTile tile : map.getTiles())
        {
            if (tile.getX() > x)
            {
                x = tile.getX();
            }

            if (tile.getY() > y)
            {
                y = tile.getY();
            }
        }
        return new Point(x + 1, y + 1);
    }


    /**
     * calculates the visible tiles based on player position.
     * will be used for drawing afterwards.
     * <p>
     * negative coordinates can be used to paint black right away
     * with a tranformation to screen coordinates.
     *
     * @return a list of points (map positions) as I do not have a better map
     * utility yet
     */
    public static List<Point> getVisibleMapPointsAroundPlayer()
    {
        List<Point> points = new ArrayList<>(middle + middle + middle + middle + 1);
        Point center = Game.getCurrent().getCurrentPlayer().getMapPosition();
        //topleft corner tile

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

    /**
     * calculates the visible tiles based on player position.
     * will be used for drawing afterwards.
     * <p>
     * negative coordinates can be used to paint black right away
     * with a tranformation to screen coordinates.
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


    public static void calculateTileDirections(ArrayList<MapTile> list)
    {
        logger.info("start: calculate tile directions");
        // iterate over all tiles
        for (MapTile tile : list)
        {
            int x = tile.getX();
            int y = tile.getY();
            tile.setMapPosition(new Point(x, y));
            // now for each tile try to find whether there is a tile whose x and y coordinates are in the +-1 range
            // do i really want to do this in one go? hmmm
            for (MapTile otherTile : list)
            {
                // check if the other tile is one tile north
                if (otherTile.getY() - y == -1)
                {
                    // if X is the same coordinate, then its really north
                    if (otherTile.getX() - x == 0)
                    {
                        tile.setNorth(otherTile);
                        otherTile.setSouth(tile);
                    }
                }

                // check if the other tile is one tile south
                if (otherTile.getY() - y == 1)
                {
                    // if X is the same coordinate, then its really south
                    if (otherTile.getX() - x == 0)
                    {
                        tile.setSouth(otherTile);
                        otherTile.setNorth(tile);
                    }
                }

                // check if the other tile is one tile east
                if (otherTile.getX() - x == 1)
                {
                    // if Y is the same coordinate, then its really east
                    if (otherTile.getY() - y == 0)
                    {
                        tile.setEast(otherTile);
                        otherTile.setWest(tile);
                    }
                }

                // check if the other tile is one tile west
                if (otherTile.getX() - x == -1)
                {
                    // if Y is the same coordinate, then its really west
                    if (otherTile.getY() - y == 0)
                    {
                        tile.setWest(otherTile);
                        otherTile.setEast(tile);
                    }
                }
            }
        }
        logger.info("end: calculate tile directions");
    }

    /**
     * creates a map, all of type grassland or ocean in random with a little help from stackoverflow:
     * <a href="https://stackoverflow.com/questions/7366266/best-way-to-write-string-to-file-using-java-nio">https://stackoverflow.com/questions/7366266/best-way-to-write-string-to-file-using-java-nio</a>
     *
     * @param x size (zero indexed, so size 12 is 11)
     * @param y size (zero indexed, so size 12 is 11)
     */
    public static void createMap(int x, int y, TileTypes type)
    {
        logger.info("begin creating Map with (zero-indexed) x: {} and y: {} and type: {}", x, y, type);
        int id;
        String north;
        String south;
        String east;
        String west;

        String fileName = ("maps" + File.separator + "Testmap2.xml");
        StringBuilder contents = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<map>\r\n" + "\t<meta>\r\n" + "\t\t<weather>true</weather>\r\n" + "\t\t<weatherrandomness>10</weatherrandomness>\r\n"
                + "\t\t<wrapping>true</wrapping>\r\n" + "\t\t<name>testname</name>\r\n" + "\t\t<parent></parent>\r\n" + "\t</meta>\r\n" + "\t<tiles>\n");

        for (int j = 0; j <= y; j++)
        {
            for (int i = 0; i <= x; i++)
            {
                id = (i + 1) + ((y + 1) * j);

                if (id % 2 == 0)
                {
                    type = TileTypes.GRASS;
                }
                else
                {
                    type = TileTypes.OCEAN;
                }

                north = String.valueOf(id - (x + 1));
                south = String.valueOf(id + (x + 1));
                east = String.valueOf(id + 1);
                west = String.valueOf(id - 1);

                if (j == 0)
                {
                    north = "";
                }

                if (i == 0)
                {
                    west = "";
                }

                if (i == x)
                {
                    east = "";
                }

                if (j == y)
                {
                    south = "";
                }

                contents.append("\t\t<tile>\r\n").append("\t\t\t<id>").append(id).append("</id>\r\n").append("\t\t\t<type>").append(type).append("</type>\r\n").append("\t\t\t<x>").append(i).append("</x>\r\n").append("\t\t\t<y>").append(j).append("</y>\r\n").append("\t\t\t<east>").append(east).append("</east>\r\n").append("\t\t\t<west>").append(west).append("</west>\r\n").append("\t\t\t<south>").append(south).append("</south>\r\n").append("\t\t\t<north>").append(north).append("</north>\r\n").append("\t\t</tile>\r\n");
            }
        }

        contents.append("	</tiles>\r\n").append("</map>");

        try
        {
            Files.writeString(Paths.get(fileName), contents.toString(), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        logger.info("finished writing map");
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
        return getTileByCoordinates(x - offSet.x, y - offSet.y);
    }

    /*
     * public static MapTile calculateMapTileUnderCursorOld(Point mousePosition) { int x = Math.floorDiv(mousePosition.x, Game.getCurrent().getTileSize()); int y = Math.floorDiv(mousePosition.y,
     * Game.getCurrent().getTileSize()); Point uiTile = new Point(x,y); //logger.info("on map or not: {} ", UILense.getCurrent().isPointOnMap(uiTile)); Point mapPos =
     * Game.getCurrent().getCurrentPlayer().getMapPosition(); Point uiPos = Game.getCurrent().getCurrentPlayer().getUIPosition(); Point offSet = calculateUIOffsetFromMapPoint(uiPos, mapPos);
     *
     * logger.info("ui tile: {}", uiTile); logger.info("map position: {}", mapPos); logger.info("offset: {}", calculateUIOffsetFromMapPoint(mapPos, uiTile)); logger.info("player offset: {}",
     * calculateUIOffsetFromMapPoint(uiPos, mapPos)); //Point offSet = MapUtils.calculateMapOffsetFromPlayer(new Point(x, y)); //logger.info("offset: {}", offSet);
     *
     * return getTileByCoordinates(new Point(uiTile.x - offSet.x, uiTile.y - offSet.y)); }
     */


    public static MapTile calculateMapTileFromUIPosition()
    {
        MapTile tile = null;
        Point p = MapUtils.calculateUIOffsetFromMapPoint();
        logger.info("uioffsetfrommappoint: {}", p);

        return tile;
    }

    public static Rectangle calculateVisibleRectangle(Player p)
    {
        Point playerPosition = p.getMapPosition();

        return new Rectangle(playerPosition.x - MapUtils.getMiddle(), playerPosition.y - MapUtils.getMiddle(), MapUtils.getMiddle() + MapUtils.getMiddle(),
                MapUtils.getMiddle() + MapUtils.getMiddle());
    }

    public static ArrayList<MapTile> calculateVisibleTiles(Rectangle visibleRect, Map map)
    {
        ArrayList<MapTile> visibleTiles = new ArrayList<>();

        Range<Integer> rangeX = Range.between(visibleRect.x, visibleRect.x + (int) visibleRect.getWidth());
        Range<Integer> rangeY = Range.between(visibleRect.y, visibleRect.y + (int) visibleRect.getHeight());

        for (MapTile t : map.getTiles())
        {

            if ((rangeY.contains(t.getY()) && (rangeX.contains(t.getX()))))
            {
                visibleTiles.add(t);
            }
        }

        return visibleTiles;
    }

    public static ArrayList<MapTile> calculateVisibleTiles(MapTile tile, int range, Map map)
    {
        ArrayList<MapTile> visibleTiles = new ArrayList<>();
        Rectangle visibleRect = new Rectangle(tile.x - range, tile.y - range, range + range, range + range);
        Range<Integer> rangeX = Range.between(visibleRect.x, visibleRect.x + (int) visibleRect.getWidth());
        Range<Integer> rangeY = Range.between(visibleRect.y, visibleRect.y + (int) visibleRect.getHeight());

        for (MapTile t : map.getTiles())
        {
            if ((rangeY.contains(t.getY()) && (rangeX.contains(t.getX()))))
            {
                visibleTiles.add(t);
            }
        }
        return visibleTiles;
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
     * @return Point with the offset as point from the player position. In case of player, this is 0,0 of course.
     */
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
        // long start = System.nanoTime();
        for (MapTile t : Game.getCurrent().getCurrentMap().getTiles())
        {
            // same tile
            if ((t.getX() != x))
            {
                continue;
            }
            if ((t.getY() != y))
            {
                continue;
            }
            if ((t.getX() == x) && (t.getY() == y))
            {
                //logger.info("found tile: {}, blocked: {}", t.toString(), t.isBlocked());
                //logger.info("time it takes here: {}", System.nanoTime() - start);
                return t.isBlocked();
            }
        }
        logger.error("Big Problem, did not find the tile on the map - returning true");
        return true;
    }

    /**
     * Helper Method - returns the tile at coordinates point on the currently active map
     *
     * @param p Point
     * @return the maptile which is found with P coordinates
     */
    public static MapTile getTileByCoordinatesAsPoint(Point p)
    {
        if ((p.x >= 0) && (p.y >= 0))
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
    public static MapTile getTileByCoordinates(int x, int y)
    {
        if ((x >= 0) && (y >= 0))
        {
            return Game.getCurrent().getCurrentMap().mapTiles[x][y];
        }
        else
        {
            return null;
        }
    }


    public static void listMaps()
    {
        for (Map ma : Game.getCurrent().getMaps())
        {
            logger.info("map: {}", ma);
        }
    }

    /**
     * Bresenhams Algorithm
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
        Map ultima4Map = new Map();
        ultima4Map.setName("Ultima4");
        ultima4Map.setWrapping(false);
        ultima4Map.setWeatherSystem(true);
        ultima4Map.setSyncedWeatherSystem(false);
        ultima4Map.setWeatherRandomness(10);
        ultima4Map.setSize(new Point(255, 255));
        try (CSVReader reader = new CSVReader(new FileReader("maps" + File.separator + "ultima4._Clean terrain.csv")))
        {
            List<String[]> r = reader.readAll();
            //r.forEach(x -> logger.info(Arrays.toString(x)));
            int row = 0;
            int id = 0;
            for (String[] line : r)
            {

                for (int column = 0; column <= 255; column++)
                {
                    MapTile tile = new MapTile();
                    tile.setMapPosition(new Point(column, row));
                    tile.setX(column);
                    tile.setY(row);
                    tile.setId(id);
                    tile.setTargetID(-1);
                    tile.setTargetMap("");
                    switch (line[column])
                    {
                        case "1":
                            tile.setType(TileTypes.OCEAN);
                            break;
                        case "2":
                            tile.setType(TileTypes.SHALLOWOCEAN);
                            break;
                        case "3":
                            tile.setType(TileTypes.REEF);
                            break;
                        case "4":
                            tile.setType(TileTypes.SWAMP);
                            break;
                        case "5":
                            tile.setType(TileTypes.GRASS);
                            break;
                        case "6":
                            tile.setType(TileTypes.BUSH);
                            break;
                        case "9":
                            tile.setType(TileTypes.DENSEFOREST);
                            break;
                        case "11":
                            tile.setType(TileTypes.HILL);
                            break;
                        case "12":
                            tile.setType(TileTypes.MOUNTAIN);
                            break;
                        case "13":
                            tile.setType(TileTypes.STEEPMOUNTAIN);
                            break;
                        case "143":
                            tile.setType(TileTypes.LAVA);
                            break;
                        default:
                            logger.info("value: {} still unknown", line[column]);
                            break;
                    }
                    //logger.info("tile: {}", tile);
                    ultima4Map.getTiles().add(tile);
                    id++;
                }
                row++;
            }
        }
        catch (IOException | CsvException e)
        {
            e.printStackTrace();
        }
        try
        {
            MapUtils.writeMapToXML(ultima4Map);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return ultima4Map;
    }

    /**
     * <tile>
     * <id>1</id>
     * <type>LADDERUP</type>
     * <x>0</x>
     * <y>0</y>
     * <east>2</east>
     * <south>4</south>
     * <targetMap>testname</targetMap>
     * <targetID>2</targetID>
     * </tile>
     */
    public static void writeMapToXML(Map map) throws IOException
    {
        BufferedWriter writer = null;
        String fileName = "maps" + File.separator + map.getName() + ".xml";
        try
        {
            writer = new BufferedWriter(new FileWriter(fileName));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Objects.requireNonNull(writer).write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.write("<map>");
        writer.write("<meta>");
        writer.write("<weather>" + map.isWeatherSystem() + "</weather>");
        writer.write("<weatherrandomness>10</weatherrandomness>");
        writer.write("<wrapping>false</wrapping>");
        writer.write("<name>" + map.getName() + "</name>");
        writer.write("<!-- <visibility>1</visibility>-->");
        writer.write("<parent>" + map.getParentMap() + "</parent>");
        writer.write("</meta>");
        writer.write("<tiles>");
        ArrayList<MapTile> tiles = map.getTiles();

        for (MapTile tile : tiles)
        {
            writer.write("<tile>");
            writer.write("<id>" + tile.getId() + "</id>");
            writer.write("<type>" + tile.getType() + "</type>");
            writer.write("<x>" + tile.getX() + "</x>");
            writer.write("<y>" + tile.getY() + "</y>");
            if (tile.getNorth() != null)
            {
                writer.write("<north>" + tile.getNorth().getId() + "</north>");
            }
            else
            {
                writer.write("<north>" + "" + "</north>");
            }
            if (tile.getEast() != null)
            {
                writer.write("<east>" + tile.getEast().getId() + "</east>");
            }
            else
            {
                writer.write("<east>" + "" + "</east>");
            }
            if (tile.getSouth() != null)
            {
                writer.write("<south>" + tile.getSouth().getId() + "</south>");
            }
            else
            {
                writer.write("<south>" + "" + "</south>");
            }
            if (tile.getWest() != null)
            {
                writer.write("<west>" + tile.getWest().getId() + "</west>");
            }
            else
            {
                writer.write("<west>" + "" + "</west>");
            }
            writer.write("<targetMap>" + tile.getTargetMap() + "</targetMap>");
            writer.write("<targetID>" + tile.getTargetID() + "</targetID>");
            writer.write("</tile>");
        }
        writer.write("</tiles>");
        writer.write("</map>");
        writer.close();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }

        Document doc = null;
        try
        {
            doc = Objects.requireNonNull(db).parse(new FileInputStream(fileName));
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        try
        {
            prettyPrint(doc, fileName);
        }
        catch (TransformerException e)
        {
            e.printStackTrace();
        }

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
        StringWriter strWriter = new StringWriter(1000000);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }

    /**
     * so this will need to be a little bit more flexible to have more ranges for light dawn, heavy dawn
     * and light dusk and heavy dusk
     */
    public static void calculateDayOrNight()
    {
        int hours = Game.getCurrent().getGameTime().getCurrentHour();

        Range<Integer> rangeDay = Range.between(8, 18);
        Range<Integer> rangeDawn = Range.between(5, 7);
        Range<Integer> rangeDusk = Range.between(19, 21);
        if (rangeDay.contains(hours))
        {
            Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.numberOfTiles);
            return;
        }
        if (rangeDawn.contains(hours))
        {
            Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.numberOfTiles / 4);
            return;
        }

        if (rangeDusk.contains(hours))
        {
            Game.getCurrent().getCurrentMap().setVisibilityRange(GameConfiguration.numberOfTiles / 4);
            return;
        }
        //night
        Game.getCurrent().getCurrentMap().setVisibilityRange(1);
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
        Range<Integer> range = Range.between(-1, 1);

        return (range.contains(source.x - target.x)) && (range.contains(source.y - target.y));
    }

}
