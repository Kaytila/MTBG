package net.ck.mtbg.map.json;

import lombok.Getter;
import lombok.Setter;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple DTO model for the map JSON schema v2.
 * Primary key for tiles is position (x,y), while legacy IDs stay optional.
 */
@Getter
@Setter
public class MapJsonV2
{
    private int schemaVersion = 2;
    private MapData map;

    public static MapJsonV2 fromMap(Map source)
    {
        MapJsonV2 dto = new MapJsonV2();
        MapData mapData = new MapData();
        mapData.setName(source.getName());

        if (source.getSize() != null)
        {
            mapData.setSize(PositionData.fromPoint(source.getSize()));
        }

        List<TileData> tiles = new ArrayList<>();
        if (source.mapTiles != null)
        {
            for (MapTile[] row : source.mapTiles)
            {
                if (row == null)
                {
                    continue;
                }
                for (MapTile tile : row)
                {
                    if (tile != null)
                    {
                        tiles.add(TileData.fromTile(tile));
                    }
                }
            }
        }
        mapData.setTiles(tiles);
        dto.setMap(mapData);
        return dto;
    }

    @Getter
    @Setter
    public static class MapData
    {
        private String name;
        private PositionData size;
        private List<TileData> tiles = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class TileData
    {
        private PositionData position;
        private String type;
        private FlagsData flags;
        private Integer furnitureId;
        private Integer lock;
        private ExitData exit;
        private List<Integer> inventoryItemIds = new ArrayList<>();
        private LegacyData legacy;

        public static TileData fromTile(MapTile tile)
        {
            TileData dto = new TileData();
            Point pos = tile.getMapPosition() != null ? tile.getMapPosition() : new Point(tile.getX(), tile.getY());
            dto.setPosition(PositionData.fromPoint(pos));
            dto.setType(tile.getType() != null ? tile.getType().name() : null);

            FlagsData flags = new FlagsData();
            flags.setBlocked(tile.isBlocked());
            flags.setHidden(tile.isHidden());
            flags.setDiscovered(tile.isDiscovered());
            flags.setOpenable(tile.isOpenable());
            flags.setOpen(tile.isOpen());
            flags.setBlocksLOS(tile.isBlocksLOS());
            flags.setSelected(tile.isSelected());
            dto.setFlags(flags);

            if (tile.getFurniture() != null)
            {
                dto.setFurnitureId(tile.getFurniture().getId());
            }
            dto.setLock(tile.getLock());

            if (tile.getTargetMap() != null || tile.getTargetCoordinates() != null || tile.getMessage() != null)
            {
                ExitData exitData = new ExitData();
                exitData.setTargetMap(tile.getTargetMap());
                if (tile.getTargetCoordinates() != null)
                {
                    exitData.setTargetCoordinates(PositionData.fromPoint(tile.getTargetCoordinates()));
                }
                if (tile.getMessage() != null)
                {
                    exitData.setMessage(MessageData.fromMessage(tile.getMessage()));
                }
                dto.setExit(exitData);
            }

            if (tile.getInventory() != null && tile.getInventory().getInventory() != null)
            {
                for (AbstractItem item : tile.getInventory().getInventory())
                {
                    dto.getInventoryItemIds().add(item.getId());
                }
            }

            LegacyData legacyData = new LegacyData();
            legacyData.setId(tile.getId());
            legacyData.setTargetID(tile.getTargetID());
            dto.setLegacy(legacyData);

            return dto;
        }
    }

    @Getter
    @Setter
    public static class PositionData
    {
        private int x;
        private int y;

        public static PositionData fromPoint(Point point)
        {
            PositionData data = new PositionData();
            data.setX(point.x);
            data.setY(point.y);
            return data;
        }
    }

    @Getter
    @Setter
    public static class FlagsData
    {
        private boolean blocked;
        private boolean hidden;
        private boolean discovered;
        private boolean openable;
        private boolean open;
        private boolean blocksLOS;
        private boolean selected;
    }

    @Getter
    @Setter
    public static class ExitData
    {
        private String targetMap;
        private PositionData targetCoordinates;
        private MessageData message;
    }

    @Getter
    @Setter
    public static class MessageData
    {
        private String description;
        private boolean repeat;
        private String messageType;

        public static MessageData fromMessage(Message message)
        {
            MessageData data = new MessageData();
            data.setDescription(message.getDescription());
            data.setRepeat(message.isRepeat());
            data.setMessageType(message.getMessageType() != null ? message.getMessageType().name() : null);
            return data;
        }
    }

    @Getter
    @Setter
    public static class LegacyData
    {
        private Integer id;
        private Integer targetID;
    }
}

