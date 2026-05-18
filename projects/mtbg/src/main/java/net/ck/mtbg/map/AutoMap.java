package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Log4j2
@ToString
public class AutoMap extends Map
{
    private ConcurrentHashMap<Rectangle, String> labels = new ConcurrentHashMap<>();

    public AutoMap(Map that)
    {
        this(that.getMapTiles(), that.getSize(), that.getName());
        syncDiscoveredFrom(that);
    }

    public AutoMap(MapTile[][] mapTiles1, Point size, String name)
    {
        setMapTiles(new MapTile[size.x][size.y]);
        for (int row = 0; row < size.y; row++)
        {
            for (int column = 0; column < size.x; column++)
            {
                MapTile tile = new MapTile(mapTiles1[column][row]);
                this.mapTiles[column][row] = tile;
            }
        }
        this.setSize(new Point(size.x, size.y));
        this.setName(name);

        if (Game.getCurrent().getAutomaps().contains(this))
        {
            logger.debug("automap already contained");
        }
        else
        {
            Game.getCurrent().getAutomaps().add(this);
        }
    }

    /**
     * Sync discovered-state from the source map into this AutoMap.
     * Keeps discovered tiles discovered (never resets to false).
     */
    public synchronized void syncDiscoveredFrom(Map source)
    {
        if (source == null || source.getMapTiles() == null || this.mapTiles == null)
        {
            return;
        }

        Point sourceSize = source.getSize();
        Point targetSize = this.getSize();
        if (sourceSize == null || targetSize == null)
        {
            return;
        }

        int width = Math.min(sourceSize.x, targetSize.x);
        int height = Math.min(sourceSize.y, targetSize.y);
        MapTile[][] sourceTiles = source.getMapTiles();

        for (int row = 0; row < height; row++)
        {
            for (int column = 0; column < width; column++)
            {
                MapTile sourceTile = sourceTiles[column][row];
                MapTile targetTile = this.mapTiles[column][row];
                if (sourceTile == null || targetTile == null)
                {
                    continue;
                }

                if (sourceTile.isDiscovered() && !targetTile.isDiscovered())
                {
                    targetTile.setDiscovered(true);
                    // force lazy rebuild in AutoMapCanvas for newly discovered tiles
                    targetTile.setScaledImage(null);
                }
            }
        }
    }
}
