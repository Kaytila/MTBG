package net.ck.mtbg.map.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Streaming JSON reader/writer for the Map JSON v2 DTO model.
 * Uses jackson-core only, so no databind dependency is required.
 */
public final class MapJsonV2IO
{
    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private MapJsonV2IO()
    {
    }

    public static String toJson(MapJsonV2 model) throws IOException
    {
        StringWriter writer = new StringWriter();
        write(model, writer);
        return writer.toString();
    }

    public static void writeToFile(MapJsonV2 model, Path path) throws IOException
    {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
        {
            write(model, writer);
        }
    }

    public static MapJsonV2 fromJson(String json) throws IOException
    {
        try (Reader reader = new StringReader(json))
        {
            return read(reader);
        }
    }

    public static MapJsonV2 readFromFile(Path path) throws IOException
    {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8))
        {
            return read(reader);
        }
    }

    private static void write(MapJsonV2 model, Writer writer) throws IOException
    {
        try (JsonGenerator g = JSON_FACTORY.createGenerator(writer))
        {
            g.useDefaultPrettyPrinter();
            g.writeStartObject();
            g.writeNumberField("schemaVersion", model.getSchemaVersion());

            g.writeFieldName("map");
            writeMapData(g, model.getMap());

            g.writeEndObject();
        }
    }

    private static void writeMapData(JsonGenerator g, MapJsonV2.MapData map) throws IOException
    {
        g.writeStartObject();
        if (map != null)
        {
            g.writeStringField("name", map.getName());

            g.writeFieldName("size");
            writePosition(g, map.getSize());

            g.writeArrayFieldStart("tiles");
            if (map.getTiles() != null)
            {
                for (MapJsonV2.TileData tile : map.getTiles())
                {
                    writeTile(g, tile);
                }
            }
            g.writeEndArray();
        }
        g.writeEndObject();
    }

    private static void writeTile(JsonGenerator g, MapJsonV2.TileData tile) throws IOException
    {
        g.writeStartObject();
        g.writeFieldName("position");
        writePosition(g, tile.getPosition());
        g.writeStringField("type", tile.getType());

        g.writeFieldName("flags");
        writeFlags(g, tile.getFlags());

        if (tile.getFurnitureId() == null)
        {
            g.writeNullField("furnitureId");
        }
        else
        {
            g.writeNumberField("furnitureId", tile.getFurnitureId());
        }

        if (tile.getLock() == null)
        {
            g.writeNullField("lock");
        }
        else
        {
            g.writeNumberField("lock", tile.getLock());
        }

        g.writeFieldName("exit");
        writeExit(g, tile.getExit());

        g.writeArrayFieldStart("inventoryItemIds");
        if (tile.getInventoryItemIds() != null)
        {
            for (Integer itemId : tile.getInventoryItemIds())
            {
                if (itemId == null)
                {
                    g.writeNull();
                }
                else
                {
                    g.writeNumber(itemId);
                }
            }
        }
        g.writeEndArray();

        g.writeFieldName("legacy");
        writeLegacy(g, tile.getLegacy());

        g.writeEndObject();
    }

    private static void writePosition(JsonGenerator g, MapJsonV2.PositionData pos) throws IOException
    {
        if (pos == null)
        {
            g.writeNull();
            return;
        }
        g.writeStartObject();
        g.writeNumberField("x", pos.getX());
        g.writeNumberField("y", pos.getY());
        g.writeEndObject();
    }

    private static void writeFlags(JsonGenerator g, MapJsonV2.FlagsData flags) throws IOException
    {
        g.writeStartObject();
        if (flags != null)
        {
            g.writeBooleanField("blocked", flags.isBlocked());
            g.writeBooleanField("hidden", flags.isHidden());
            g.writeBooleanField("discovered", flags.isDiscovered());
            g.writeBooleanField("openable", flags.isOpenable());
            g.writeBooleanField("open", flags.isOpen());
            g.writeBooleanField("blocksLOS", flags.isBlocksLOS());
            g.writeBooleanField("selected", flags.isSelected());
        }
        g.writeEndObject();
    }

    private static void writeExit(JsonGenerator g, MapJsonV2.ExitData exit) throws IOException
    {
        if (exit == null)
        {
            g.writeNull();
            return;
        }
        g.writeStartObject();
        g.writeStringField("targetMap", exit.getTargetMap());
        g.writeFieldName("targetCoordinates");
        writePosition(g, exit.getTargetCoordinates());

        g.writeFieldName("message");
        writeMessage(g, exit.getMessage());
        g.writeEndObject();
    }

    private static void writeMessage(JsonGenerator g, MapJsonV2.MessageData message) throws IOException
    {
        if (message == null)
        {
            g.writeNull();
            return;
        }
        g.writeStartObject();
        g.writeStringField("description", message.getDescription());
        g.writeBooleanField("repeat", message.isRepeat());
        g.writeStringField("messageType", message.getMessageType());
        g.writeEndObject();
    }

    private static void writeLegacy(JsonGenerator g, MapJsonV2.LegacyData legacy) throws IOException
    {
        if (legacy == null)
        {
            g.writeNull();
            return;
        }
        g.writeStartObject();
        if (legacy.getId() == null)
        {
            g.writeNullField("id");
        }
        else
        {
            g.writeNumberField("id", legacy.getId());
        }
        if (legacy.getTargetID() == null)
        {
            g.writeNullField("targetID");
        }
        else
        {
            g.writeNumberField("targetID", legacy.getTargetID());
        }
        g.writeEndObject();
    }

    private static MapJsonV2 read(Reader reader) throws IOException
    {
        try (JsonParser p = JSON_FACTORY.createParser(reader))
        {
            MapJsonV2 model = new MapJsonV2();
            expectToken(p.nextToken(), JsonToken.START_OBJECT);

            while (p.nextToken() != JsonToken.END_OBJECT)
            {
                String field = p.currentName();
                p.nextToken();
                if ("schemaVersion".equals(field))
                {
                    model.setSchemaVersion(p.getIntValue());
                }
                else if ("map".equals(field))
                {
                    model.setMap(readMapData(p));
                }
                else
                {
                    p.skipChildren();
                }
            }
            return model;
        }
    }

    private static MapJsonV2.MapData readMapData(JsonParser p) throws IOException
    {
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return null;
        }
        MapJsonV2.MapData map = new MapJsonV2.MapData();
        expectToken(p.currentToken(), JsonToken.START_OBJECT);

        while (p.nextToken() != JsonToken.END_OBJECT)
        {
            String field = p.currentName();
            p.nextToken();
            if ("name".equals(field))
            {
                map.setName(readNullableString(p));
            }
            else if ("size".equals(field))
            {
                map.setSize(readPosition(p));
            }
            else if ("tiles".equals(field))
            {
                map.setTiles(readTiles(p));
            }
            else
            {
                p.skipChildren();
            }
        }
        return map;
    }

    private static ArrayList<MapJsonV2.TileData> readTiles(JsonParser p) throws IOException
    {
        ArrayList<MapJsonV2.TileData> tiles = new ArrayList<>();
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return tiles;
        }
        expectToken(p.currentToken(), JsonToken.START_ARRAY);
        while (p.nextToken() != JsonToken.END_ARRAY)
        {
            tiles.add(readTile(p));
        }
        return tiles;
    }

    private static MapJsonV2.TileData readTile(JsonParser p) throws IOException
    {
        MapJsonV2.TileData tile = new MapJsonV2.TileData();
        expectToken(p.currentToken(), JsonToken.START_OBJECT);

        while (p.nextToken() != JsonToken.END_OBJECT)
        {
            String field = p.currentName();
            p.nextToken();
            if ("position".equals(field))
            {
                tile.setPosition(readPosition(p));
            }
            else if ("type".equals(field))
            {
                tile.setType(readNullableString(p));
            }
            else if ("flags".equals(field))
            {
                tile.setFlags(readFlags(p));
            }
            else if ("furnitureId".equals(field))
            {
                tile.setFurnitureId(readNullableInt(p));
            }
            else if ("lock".equals(field))
            {
                tile.setLock(readNullableInt(p));
            }
            else if ("exit".equals(field))
            {
                tile.setExit(readExit(p));
            }
            else if ("inventoryItemIds".equals(field))
            {
                tile.setInventoryItemIds(readIntegerArray(p));
            }
            else if ("legacy".equals(field))
            {
                tile.setLegacy(readLegacy(p));
            }
            else
            {
                p.skipChildren();
            }
        }
        return tile;
    }

    private static MapJsonV2.PositionData readPosition(JsonParser p) throws IOException
    {
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return null;
        }
        MapJsonV2.PositionData pos = new MapJsonV2.PositionData();
        expectToken(p.currentToken(), JsonToken.START_OBJECT);
        while (p.nextToken() != JsonToken.END_OBJECT)
        {
            String field = p.currentName();
            p.nextToken();
            if ("x".equals(field))
            {
                pos.setX(p.getIntValue());
            }
            else if ("y".equals(field))
            {
                pos.setY(p.getIntValue());
            }
            else
            {
                p.skipChildren();
            }
        }
        return pos;
    }

    private static MapJsonV2.FlagsData readFlags(JsonParser p) throws IOException
    {
        MapJsonV2.FlagsData flags = new MapJsonV2.FlagsData();
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return flags;
        }
        expectToken(p.currentToken(), JsonToken.START_OBJECT);
        while (p.nextToken() != JsonToken.END_OBJECT)
        {
            String field = p.currentName();
            p.nextToken();
            if ("blocked".equals(field))
            {
                flags.setBlocked(p.getBooleanValue());
            }
            else if ("hidden".equals(field))
            {
                flags.setHidden(p.getBooleanValue());
            }
            else if ("discovered".equals(field))
            {
                flags.setDiscovered(p.getBooleanValue());
            }
            else if ("openable".equals(field))
            {
                flags.setOpenable(p.getBooleanValue());
            }
            else if ("open".equals(field))
            {
                flags.setOpen(p.getBooleanValue());
            }
            else if ("blocksLOS".equals(field))
            {
                flags.setBlocksLOS(p.getBooleanValue());
            }
            else if ("selected".equals(field))
            {
                flags.setSelected(p.getBooleanValue());
            }
            else
            {
                p.skipChildren();
            }
        }
        return flags;
    }

    private static MapJsonV2.ExitData readExit(JsonParser p) throws IOException
    {
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return null;
        }
        MapJsonV2.ExitData exit = new MapJsonV2.ExitData();
        expectToken(p.currentToken(), JsonToken.START_OBJECT);
        while (p.nextToken() != JsonToken.END_OBJECT)
        {
            String field = p.currentName();
            p.nextToken();
            if ("targetMap".equals(field))
            {
                exit.setTargetMap(readNullableString(p));
            }
            else if ("targetCoordinates".equals(field))
            {
                exit.setTargetCoordinates(readPosition(p));
            }
            else if ("message".equals(field))
            {
                exit.setMessage(readMessage(p));
            }
            else
            {
                p.skipChildren();
            }
        }
        return exit;
    }

    private static MapJsonV2.MessageData readMessage(JsonParser p) throws IOException
    {
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return null;
        }
        MapJsonV2.MessageData msg = new MapJsonV2.MessageData();
        expectToken(p.currentToken(), JsonToken.START_OBJECT);
        while (p.nextToken() != JsonToken.END_OBJECT)
        {
            String field = p.currentName();
            p.nextToken();
            if ("description".equals(field))
            {
                msg.setDescription(readNullableString(p));
            }
            else if ("repeat".equals(field))
            {
                msg.setRepeat(p.getBooleanValue());
            }
            else if ("messageType".equals(field))
            {
                msg.setMessageType(readNullableString(p));
            }
            else
            {
                p.skipChildren();
            }
        }
        return msg;
    }

    private static MapJsonV2.LegacyData readLegacy(JsonParser p) throws IOException
    {
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return null;
        }
        MapJsonV2.LegacyData legacy = new MapJsonV2.LegacyData();
        expectToken(p.currentToken(), JsonToken.START_OBJECT);
        while (p.nextToken() != JsonToken.END_OBJECT)
        {
            String field = p.currentName();
            p.nextToken();
            if ("id".equals(field))
            {
                legacy.setId(readNullableInt(p));
            }
            else if ("targetID".equals(field))
            {
                legacy.setTargetID(readNullableInt(p));
            }
            else
            {
                p.skipChildren();
            }
        }
        return legacy;
    }

    private static ArrayList<Integer> readIntegerArray(JsonParser p) throws IOException
    {
        ArrayList<Integer> values = new ArrayList<>();
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return values;
        }
        expectToken(p.currentToken(), JsonToken.START_ARRAY);
        while (p.nextToken() != JsonToken.END_ARRAY)
        {
            if (p.currentToken() == JsonToken.VALUE_NULL)
            {
                values.add(null);
            }
            else
            {
                values.add(p.getIntValue());
            }
        }
        return values;
    }

    private static String readNullableString(JsonParser p) throws IOException
    {
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return null;
        }
        return p.getValueAsString();
    }

    private static Integer readNullableInt(JsonParser p) throws IOException
    {
        if (p.currentToken() == JsonToken.VALUE_NULL)
        {
            return null;
        }
        return p.getIntValue();
    }

    private static void expectToken(JsonToken actual, JsonToken expected) throws IOException
    {
        if (actual != expected)
        {
            throw new IOException("Unexpected JSON token. Expected " + expected + " but got " + actual);
        }
    }
}

