package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.listeners.MapEditorCanvasListener;
import net.ck.mtbg.util.utils.ImageUtils;
import net.ck.mtbg.util.utils.MapUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

@Log4j2
@Getter
@Setter
public class MapEditorCanvas extends AbstractMapCanvas
{

    private final BufferedImage blackImage = ImageUtils.createImage(Color.black, GameConfiguration.tileSize);
    private MapTilePane mapTilePane;
    private boolean dragEnabled;
    private Map map;

    public MapEditorCanvas(MapTilePane mapTilePane)
    {
        MapEditorCanvasListener listener = new MapEditorCanvasListener(this);
        this.mapTilePane = mapTilePane;
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addFocusListener(listener);

        map = new Map();
        map.setSize(new Point(13, 13));
        MapTile[][] mapTiles = new MapTile[12][12];
        map.setMapTiles(mapTiles);
    }

    public void paintComponent(Graphics g)
    {
        long start = System.nanoTime();
        paintGridLines(g);
        if (map == null)
        {
            return;
        }

        int x = map.mapTiles[0].length;
        int y = map.mapTiles[1].length;

        MapUtils.calculateAllTileImages(map, g, this, x, y);

        for (int row = 0; row < y; row++)
        {
            for (int column = 0; column < x; column++)
            {
                if (map.mapTiles[row][column] == null)
                {
                    g.drawImage(blackImage, (row * GameConfiguration.tileSize), (column * GameConfiguration.tileSize), this);
                    continue;
                }

                MapTile tile = map.mapTiles[row][column];
                g.drawImage(tile.getCalculatedImage(), (row * GameConfiguration.tileSize), (column * GameConfiguration.tileSize), this);
            }
        }
        long end = System.nanoTime();
        long duration = end - start;
        logger.info("duration: {}", duration);

    }

    private void paintGridLines(Graphics g)
    {
        int rows = this.getHeight() / GameConfiguration.tileSize;
        int cols = this.getWidth() / GameConfiguration.tileSize;
        int i;
        for (i = 0; i < rows; i++)
        {
            g.drawLine(0, i * GameConfiguration.tileSize, this.getWidth(), i * GameConfiguration.tileSize);
        }

        for (i = 0; i < cols; i++)
        {
            g.drawLine(i * GameConfiguration.tileSize, 0, i * GameConfiguration.tileSize, this.getHeight());
        }
    }

}
