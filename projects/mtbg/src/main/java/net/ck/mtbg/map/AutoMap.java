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
}
