package net.ck.mtbg.util.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.TileTypes;
import net.ck.mtbg.map.json.MapJsonV2;
import net.ck.mtbg.map.json.MapJsonV2IO;
import net.ck.mtbg.util.xml.RunXMLParser;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public final class MapPersistenceUtils
{
    private MapPersistenceUtils()
    {
    }

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

                contents.append("\t\t<tile>\r\n")
                        .append("\t\t\t<id>").append(id).append("</id>\r\n")
                        .append("\t\t\t<type>").append(type).append("</type>\r\n")
                        .append("\t\t\t<x>").append(i).append("</x>\r\n")
                        .append("\t\t\t<y>").append(j).append("</y>\r\n")
                        .append("\t\t</tile>\r\n");
            }
        }

        contents.append("\t</tiles>\r\n").append("</map>");

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
                        case "1" -> tile.setType(TileTypes.OCEAN);
                        case "2" -> tile.setType(TileTypes.SHALLOWOCEAN);
                        case "3" -> tile.setType(TileTypes.REEF);
                        case "4" -> tile.setType(TileTypes.SWAMP);
                        case "5" -> tile.setType(TileTypes.GRASS);
                        case "6" -> tile.setType(TileTypes.BUSH);
                        case "9" -> tile.setType(TileTypes.DENSEFOREST);
                        case "11" -> tile.setType(TileTypes.HILL);
                        case "12" -> tile.setType(TileTypes.MOUNTAIN);
                        case "13" -> tile.setType(TileTypes.STEEPMOUNTAIN);
                        case "143" -> tile.setType(TileTypes.LAVA);
                        default -> logger.info("value: {} still unknown", line[column]);
                    }
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
            writeMapToXML(ultima4Map);
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
                        case "1" -> tile.setType(TileTypes.OCEAN);
                        case "2" -> tile.setType(TileTypes.SHALLOWOCEAN);
                        case "3" -> tile.setType(TileTypes.REEF);
                        case "4" -> tile.setType(TileTypes.SWAMP);
                        case "5" -> tile.setType(TileTypes.GRASS);
                        case "6" -> tile.setType(TileTypes.BUSH);
                        case "9" -> tile.setType(TileTypes.DENSEFOREST);
                        case "11" -> tile.setType(TileTypes.HILL);
                        case "12" -> tile.setType(TileTypes.MOUNTAIN);
                        case "13" -> tile.setType(TileTypes.STEEPMOUNTAIN);
                        case "143" -> tile.setType(TileTypes.LAVA);
                        default -> logger.debug("value: {} still unknown", line[column]);
                    }
                    mapTiles[column][row] = tile;
                    id++;
                }
                row++;
            }
        }
        catch (IOException | CsvException e)
        {
            logger.debug("issue here: {}", e.toString());
        }
        try
        {
            writeMapToXML(map);
        }
        catch (IOException e)
        {
            logger.debug("issue here: {}", e.toString());
        }
        map.setMapTiles(mapTiles);
        return map;
    }

    public static void writeMapToXML(Map map) throws IOException
    {
        ArrayList<LifeForm> npcs = new ArrayList<>();
        BufferedWriter writer = null;
        String fileName = GameConfiguration.mapFileRootPath + File.separator + CodeUtils.removeFileExtension(map.getName(), true) + ".xml";

        Path filePath = Paths.get(fileName);
        if (Files.exists(filePath))
        {
            logger.debug("Map: {} already exists", fileName);
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

    public static void exportCurrentMapAsJsonV2(Path jsonFile) throws IOException
    {
        Map currentMap = Game.getCurrent().getCurrentMap();
        if (currentMap == null)
        {
            throw new IOException("No current map available for JSON export");
        }
        MapJsonV2 model = MapJsonV2.fromMap(currentMap);
        MapJsonV2IO.writeToFile(model, jsonFile);
    }

    public static void translateXmlMapToJsonV2(Path xmlFile, Path jsonFile) throws IOException
    {
        Map parsedMap = RunXMLParser.parseMap(xmlFile.toFile().getAbsolutePath());
        if (parsedMap == null)
        {
            throw new IOException("Failed to parse XML map: " + xmlFile);
        }
        MapJsonV2 model = MapJsonV2.fromMap(parsedMap);
        MapJsonV2IO.writeToFile(model, jsonFile);
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

    public static void translateTextMaps()
    {
        logger.debug("START: text map translate");
        File folder = new File(GameConfiguration.txtMapRootFilePath);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles)
        {
            logger.debug("File: {}", file.getName());
            if (file.isFile() && file.getName().endsWith(".txt"))
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
        logger.info("END: text map translate");
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
            if (file.isFile() && file.getName().endsWith(".txt"))
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
        logger.info("END: json map translate");
    }

    private static MapTile[][] calculateMapTileArray(ArrayList<MapTile> mapTiles, Point size)
    {
        MapTile[][] tileArray = new MapTile[size.x][size.y];

        for (MapTile tile : mapTiles)
        {
            tileArray[tile.x][tile.y] = tile;
        }

        return tileArray;
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
}

