package net.ck.mtbg.map.json;

import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.TileTypes;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class MapJsonV2Test
{
    @Test
    void fromMapBuildsSchemaV2WithPositionBasedTileIdentity()
    {
        Map map = new Map();
        map.setName("test_map");
        map.setSize(new Point(2, 2));

        MapTile tile = new MapTile();
        tile.setId(123);
        tile.setTargetID(-1);
        tile.setMapPosition(new Point(1, 1));
        tile.setType(TileTypes.GRASS);
        tile.setHidden(false);
        tile.setDiscovered(true);
        tile.setSelected(false);
        tile.setInventory(new Inventory());

        MapTile[][] tiles = new MapTile[2][2];
        tiles[1][1] = tile;
        map.mapTiles = tiles;

        MapJsonV2 dto = MapJsonV2.fromMap(map);

        assertEquals(2, dto.getSchemaVersion());
        assertNotNull(dto.getMap());
        assertEquals("test_map", dto.getMap().getName());
        assertEquals(1, dto.getMap().getTiles().size());

        MapJsonV2.TileData target = dto.getMap().getTiles().stream()
                .filter(t -> t.getPosition().getX() == 1 && t.getPosition().getY() == 1)
                .findFirst()
                .orElseThrow();

        assertEquals("GRASS", target.getType());
        assertTrue(target.getFlags().isDiscovered());
        assertFalse(target.getFlags().isHidden());
        assertEquals(123, target.getLegacy().getId());
    }
}

