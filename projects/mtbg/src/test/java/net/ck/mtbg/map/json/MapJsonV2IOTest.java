package net.ck.mtbg.map.json;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MapJsonV2IOTest
{
    @Test
    void roundTripStringKeepsCoreFields() throws IOException
    {
        MapJsonV2 model = new MapJsonV2();
        MapJsonV2.MapData mapData = new MapJsonV2.MapData();
        mapData.setName("json_test_map");

        MapJsonV2.PositionData size = new MapJsonV2.PositionData();
        size.setX(64);
        size.setY(64);
        mapData.setSize(size);

        MapJsonV2.TileData tile = new MapJsonV2.TileData();
        MapJsonV2.PositionData pos = new MapJsonV2.PositionData();
        pos.setX(3);
        pos.setY(5);
        tile.setPosition(pos);
        tile.setType("GRASS");

        MapJsonV2.FlagsData flags = new MapJsonV2.FlagsData();
        flags.setBlocked(false);
        flags.setDiscovered(true);
        flags.setHidden(false);
        flags.setOpen(false);
        flags.setOpenable(false);
        flags.setBlocksLOS(false);
        flags.setSelected(false);
        tile.setFlags(flags);

        MapJsonV2.LegacyData legacy = new MapJsonV2.LegacyData();
        legacy.setId(42);
        legacy.setTargetID(-1);
        tile.setLegacy(legacy);

        mapData.getTiles().add(tile);
        model.setMap(mapData);

        String json = MapJsonV2IO.toJson(model);
        MapJsonV2 readBack = MapJsonV2IO.fromJson(json);

        assertEquals(2, readBack.getSchemaVersion());
        assertEquals("json_test_map", readBack.getMap().getName());
        assertEquals(1, readBack.getMap().getTiles().size());
        assertEquals(3, readBack.getMap().getTiles().get(0).getPosition().getX());
        assertEquals(5, readBack.getMap().getTiles().get(0).getPosition().getY());
        assertEquals("GRASS", readBack.getMap().getTiles().get(0).getType());
        assertEquals(42, readBack.getMap().getTiles().get(0).getLegacy().getId());
    }

    @Test
    void writeAndReadFileWorks() throws IOException
    {
        MapJsonV2 model = new MapJsonV2();
        model.setMap(new MapJsonV2.MapData());
        model.getMap().setName("file_test_map");

        Path tempFile = Files.createTempFile("map-json-v2-", ".json");
        try
        {
            MapJsonV2IO.writeToFile(model, tempFile);
            MapJsonV2 readBack = MapJsonV2IO.readFromFile(tempFile);

            assertNotNull(readBack);
            assertEquals(2, readBack.getSchemaVersion());
            assertEquals("file_test_map", readBack.getMap().getName());
        }
        finally
        {
            Files.deleteIfExists(tempFile);
        }
    }
}

