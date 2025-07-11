package net.ck.mtbg.util.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.TileTypes;
import net.ck.mtbg.ui.components.game.AbstractMapCanvas;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;
import net.ck.mtbg.util.ui.WindowBuilder;
import net.ck.mtbg.weather.DayNight;
import org.apache.commons.lang3.Range;
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
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
     * <a href="https://stackoverflow.com/questions/7366266/best-way-to-write-string-to-file-using-java-nio">https://stackoverflow.com/questions/7366266/best-way-to-write-string-to-file-using-java-nio</a>
     *
     * @param x size (zero indexed, so size 12 is 11)
     * @param y size (zero indexed, so size 12 is 11)
     */
    public static void createMap(int x, int y, TileTypes type)
    {
        logger.info("begin creating Map with (zero-indexed) x: {} and y: {} and type: {}", x, y, type);
        int id;

        String fileName = ("maps" + File.separator + "Testmap2.xml");
        StringBuilder contents = new StringBuilder("""
                <?xml version="1.0" encoding="UTF-8"?>\r
                <map>\r
                \t<meta>\r
                \t\t<weather>true</weather>\r
                \t\t<weatherrandomness>10</weatherrandomness>\r
                \t\t<wrapping>true</wrapping>\r
                \t\t<name>testname</name>\r
                \t\t<parent></parent>\r
                \t</meta>\r
                \t<tiles>
                """);

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


                contents.append("\t\t<tile>\r\n").append("\t\t\t<id>").append(id).append("</id>\r\n").append("\t\t\t<type>").append(type).append("</type>\r\n").append("\t\t\t<x>").append(i).append("</x>\r\n").append("\t\t\t<y>").append(j).append("</y>\r\n").append("\t\t</tile>\r\n");
            }
        }

        contents.append("	</tiles>\r\n").append("</map>");

        try
        {
            Files.writeString(Paths.get(fileName), contents.toString(), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            logger.error("issue writing map file");
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
        return getMapTileByCoordinates(x - offSet.x, y - offSet.y);
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
                    //logger.debug("tile: {}", UILense.getCurrent().mapTiles[row][column]);
                    visibleTiles.add(UILense.getCurrent().mapTiles[row][column]);
                }

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
        Map ultima4Map = new Map();
        ultima4Map.setName("Ultima4");
        ultima4Map.setWrapping(false);
        ultima4Map.setWeatherSystem(true);
        ultima4Map.setSyncedWeatherSystem(false);
        ultima4Map.setWeatherRandomness(10);
        ultima4Map.setSize(new Point(255, 255));
        MapTile[][] mapTiles = new MapTile[255][255];
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
                    mapTiles[column][row] = tile;
                    id++;
                }
                row++;
            }
        }
        catch (IOException | CsvException e)
        {
            logger.error("Error reading ultima 4 map file");
        }
        try
        {
            MapUtils.writeMapToXML(ultima4Map);
        }
        catch (IOException e)
        {
            logger.error("Error writing ultima 4 xml file");
        }
        ultima4Map.setMapTiles(mapTiles);
        return ultima4Map;
    }


    public static Map importMapFromTXT()
    {
        Map map = new Map();
        map.setName("Ultima4");
        map.setWrapping(false);
        map.setWeatherSystem(true);
        map.setSyncedWeatherSystem(false);
        map.setWeatherRandomness(10);
        map.setSize(new Point(255, 255));
        MapTile[][] mapTiles = new MapTile[255][255];
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
                            logger.debug("value: {} still unknown", line[column]);
                            break;
                    }
                    //logger.info("tile: {}", tile);
                    mapTiles[column][row] = tile;
                    id++;
                }
                row++;
            }
        }
        catch (IOException | CsvException e)
        {
            //e.printStackTrace();
            logger.debug("issue here: {}", e.toString());
        }
        try
        {
            MapUtils.writeMapToXML(map);
        }
        catch (IOException e)
        {
            logger.debug("issue here: {}", e.toString());
            //e.printStackTrace();
        }
        map.setMapTiles(mapTiles);
        return map;
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
        ArrayList<LifeForm> npcs = new ArrayList<>();
        BufferedWriter writer = null;
        String fileName = GameConfiguration.mapFileRootPath + File.separator + CodeUtils.removeFileExtension(map.getName(), true) + ".xml";

        Path filePath = Paths.get(fileName);
        if (Files.exists(filePath))
        {
            logger.debug("Map: {} already exists", fileName);
            //return;
        }
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
        writer.write("<name>" + CodeUtils.removeFileExtension(map.getName(), true) + "</name>");
        writer.write("<visibility>1</visibility>");
        writer.write("<visibility>1</visibility>");
        writer.write("<parent>" + map.getParentMap() + "</parent>");
        writer.write("</meta>");
        writer.write("<tiles>");


        for (int x = 0; x < map.getSize().x; x++)
        {
            for (int y = 0; y < map.getSize().y; y++)
            {
                MapTile tile = map.mapTiles[x][y];
                if (tile.getLifeForm() != null)
                {
                    npcs.add(tile.getLifeForm());
                }
                writer.write(tile.toXML());
            }
        }
        writer.write("</tiles>");
        writer.write("<npcs>");
        for (LifeForm n : npcs)
        {
            writer.write(n.toXML());
        }
        writer.write("</npcs>");
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
        //reset
        for (int row = 0; row < GameConfiguration.numberOfTiles + 2; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles + 2; column++)
            {
                MapTile t = UILense.getCurrent().bufferedMapTiles[row][column];

                if (t == null)
                {
                    //this is null for all the empty tiles that are drawn at the outer limits to fill out the blank space between the end of the map
                    //and the end of the UI element
                    //logger.debug("null tile");
                    continue;
                }
                t.setHidden(false);
                t.setBrightenFactor(0);

                if (MapUtils.checkForLightSourceAround(t))
                {
                    t.setBrightenFactor(1);
                }

                /*else
                {
                    t.setBrightenFactor(0);
                }*/
            }
        }
        //paint LoS
//        long start = System.nanoTime();
        int pX = Game.getCurrent().getCurrentPlayer().getUIPosition().x;
        int pY = Game.getCurrent().getCurrentPlayer().getUIPosition().y;

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


                if (GameConfiguration.calculateBrightenUpImageInPaint == false)
                {
                    t.setBrightenedImage(null);
                    img = ImageUtils.getTileTypeImages().get(t.getType()).get(WindowBuilder.getGridCanvas().getCurrentBackgroundImage());
                }
                /*
                 * identify the light range
                 * pre-brighten the images
                 */
                if (t.getFurniture() != null)
                {
                    FurnitureItem item = t.getFurniture();
                    if (item.isLightSource() == true)
                    {
                        if (item.isBurning() == true)
                        {
                            int lightrange = item.getLightRange();
                            ArrayList<MapTile> tiles = MapUtils.calculateVisibleTiles(t, lightrange);
                            for (MapTile tile : tiles)
                            {
                                // logger.debug("tile: {}", tile);
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
                    /*if (checkForLightSourceAround(t))
                    {
                        t.setBrightenFactor(1);
                    }*/


                    if (t.getBrightenFactor() > 0)
                    {

                    }
                    else
                    {
                        int absX = Math.abs(pX - row);
                        int absY = Math.abs(pY - column);
                        //logger.debug("t: {} {}", t, Math.max(absX, absY));
                        t.setBrightenFactor(Math.max(absX, absY));
                    }
                }
                /*
                 * raycasting starts here
                 */
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
                        g.drawLine(Game.getCurrent().getCurrentPlayer().getUIPosition().x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), Game.getCurrent().getCurrentPlayer().getUIPosition().x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), po.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), po.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2));
                    }
                    if (tl.isBlocksLOS())
                    {
                        //logger.info("t: {}", t);
                        blocked = true;
                        if (first)
                        {
                            first = false;
                            continue;
                        }
                        else
                        {
                            if (isAdjacent(tl.getMapPosition(), Game.getCurrent().getCurrentPlayer().getMapPosition()))
                            {
                                if (GameConfiguration.debugLOS)
                                {
                                    logger.debug("next to player, do not block");
                                }
                            }
                            else
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
                        if (isAdjacent(tl.getMapPosition(), Game.getCurrent().getCurrentPlayer().getMapPosition()))
                        {
                            if (GameConfiguration.debugLOS)
                            {
                                logger.debug("next to player, do not block");
                            }
                        }
                        else
                        {
                            tl.setHidden(true);
                            if (GameConfiguration.debugLOS)
                            {
                                g.setColor(Color.GRAY);
                                g.drawString("B", po.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), po.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2));
                            }
                            //logger.info("Maptile {} is hidden", t);
                        }
                    }
                    else
                    {
                        //TODO
                        t.setDiscovered(true);
                        if (GameConfiguration.debugDiscovered)
                        {
                            g.drawString("D", po.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), po.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2));
                        }
                    }
                }
            }
        }
        //logger.info("raycasting calculation takes: {} nanoseconds", System.nanoTime() - start);
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
        int x = 0;
        int y = 0;
        for (MapTile tile : maptiles)
        {
            if (tile.x > x)
            {
                x = tile.x;
            }

            if (tile.y > y)
            {
                y = tile.y;
            }
        }
        return new Point(x + 1, y + 1);


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
        logger.debug("START: text map translate");
        File folder = new File(GameConfiguration.txtMapRootFilePath);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles)
        {
            logger.debug("File: {}", file.getName());
            if (file.isFile())
            {
                //hopefully it is a valid map!
                if (file.getName().endsWith(".txt"))
                {
                    logger.debug("START: parse map");
                    ArrayList<MapTile> mapTiles = new ArrayList<>();
                    Map map = new Map();
                    map.setWeatherRandomness(0);
                    map.setName(file.getName());
                    try
                    {
                        int id = 0;
                        BufferedReader reader = new BufferedReader(new FileReader(GameConfiguration.txtMapRootFilePath + File.separator + file.getName()));
                        String line = reader.readLine();
                        int lineIndex = 0;
                        while (line != null)
                        {
                            id = parseMapLine(map, line, lineIndex, mapTiles, id);
                            lineIndex++;
                            line = reader.readLine();
                        }
                        map.setSize(calculateMapSize(mapTiles));
                        map.setMapTiles(calculateMapTileArray(mapTiles, map.getSize()));
                        //Game.getCurrent().getMaps().add(map);
                        reader.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    logger.info("END: parse File");
                    logger.info("START: write xml file");
                    try
                    {
                        writeMapToXML(map);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    logger.info("END: write xml file");

                }
            }
        }
        logger.info("END: text map translate");
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
        AtomicInteger rowIndex = new AtomicInteger();
        AtomicInteger ide = new AtomicInteger(id);
        line.chars().forEach(c ->
        {
            MapTile tile = new MapTile();
            tile.setY(lineIndex);
            tile.setX(rowIndex.getAndIncrement());
            tile.setId(ide.getAndIncrement());
            tile.setMapPosition(new Point(tile.getX(), tile.getY()));
            tile.setType(mapTXTtoTerrainTypes((String.valueOf((char) c))));
            tiles.add(tile);
        });
        return ide.intValue();
    }

    public static void translateJSONMap()
    {
        logger.debug("START: json map translate");
        File folder = new File(GameConfiguration.txtMapRootFilePath);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles)
        {
            logger.debug("File: {}", file.getName());
            if (file.isFile())
            {
                //hopefully it is a valid map!
                if (file.getName().endsWith(".txt"))
                {
                    logger.debug("START: parse map");
                    ArrayList<MapTile> mapTiles = new ArrayList<>();
                    Map map = new Map();
                    map.setWeatherRandomness(0);
                    map.setName(file.getName());
                    try
                    {
                        int id = 0;
                        BufferedReader reader = new BufferedReader(new FileReader(GameConfiguration.txtMapRootFilePath + File.separator + file.getName()));
                        String line = reader.readLine();
                        int lineIndex = 0;
                        while (line != null)
                        {
                            id = parseMapLine(map, line, lineIndex, mapTiles, id);
                            lineIndex++;
                            line = reader.readLine();
                        }
                        map.setSize(calculateMapSize(mapTiles));
                        map.setMapTiles(calculateMapTileArray(mapTiles, map.getSize()));
                        //Game.getCurrent().getMaps().add(map);
                        reader.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    logger.info("END: parse File");
                    logger.info("START: write xml file");
                    try
                    {
                        writeMapToXML(map);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    logger.info("END: write xml file");

                }
            }
        }
        logger.info("END: json map translate");
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
        long start = System.nanoTime();
        for (int row = 0; row < GameConfiguration.numberOfTiles + 2; row++)
        {
            for (int column = 0; column < GameConfiguration.numberOfTiles + 2; column++)
            {
                MapTile t = UILense.getCurrent().bufferedMapTiles[row][column];
                if (t == null)
                {
                    //not existing tiles, we need to handle somewhere else probably
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

                    /*if (t.getLifeForm() != null)
                    {
                        if (GameConfiguration.useImageManager == true)
                        {
                            BufferedImage bufferedImage = ImageManager.getLifeformImages().get(t.getLifeForm().getType())[t.getLifeForm().getCurrImage()];
                            g.drawImage(bufferedImage, (GameConfiguration.tileSize / 4), (GameConfiguration.tileSize / 4), null);

                        }
                    }
                    else*/
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
                    //not existing tiles, we need to handle somewhere else probably
                    continue;
                }

                //if (t.isHidden())
                //{
                //    t.setCalculatedImage(ImageUtils.createImage(Color.BLACK, GameConfiguration.tileSize));
                //}
                //else
                //{
                BufferedImage image = new BufferedImage(GameConfiguration.tileSize, GameConfiguration.tileSize, BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();

                BufferedImage bgImage = ImageUtils.getTileTypeImages().get(t.getType()).get(WindowBuilder.getGridCanvas().getCurrentBackgroundImage());
                //g.drawImage(ImageUtils.brightenUpImage(bgImage, t.getBrightenFactor(), t.getBrightenFactor()), 0, 0, null);
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
                    //not existing tiles, we need to handle somewhere else probably
                    continue;
                }

                //if (t.isHidden())
                //{
                //    t.setCalculatedImage(ImageUtils.createImage(Color.BLACK, GameConfiguration.tileSize));
                //}
                //else
                //{
                BufferedImage image = new BufferedImage(GameConfiguration.tileSize, GameConfiguration.tileSize, BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();

                BufferedImage bgImage = ImageUtils.getTileTypeImages().get(t.getType()).get(canvas.getCurrentBackgroundImage());
                //g.drawImage(ImageUtils.brightenUpImage(bgImage, t.getBrightenFactor(), t.getBrightenFactor()), 0, 0, null);
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


    /**
     * returns the first tile where a light source is and its burning during day, or not during the night
     *
     * @param tile  - the source map tile
     * @param range - determines how big the range to check is
     * @return the maptile closest which contains a light source
     */
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


    public static Point getUICoordinateUnderCursor(Point point)
    {
        Point uiCoordinate = new Point(point.x / GameConfiguration.tileSize, point.y / GameConfiguration.tileSize);
        logger.debug("UI Coordinate: {}", uiCoordinate);
        return uiCoordinate;
    }
}
