package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditorApplication;
import net.ck.mtbg.backend.configuration.GameConfiguration;
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

    private boolean dragEnabled;
    private MapTile selectedTile;

    public MapEditorCanvas()
    {
        MapEditorCanvasListener listener = new MapEditorCanvasListener(this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addFocusListener(listener);


    }

    public void paintComponent(Graphics g)
    {
        long start = System.nanoTime();
        paintGridLines(g);
        if (MapEditorApplication.getCurrent().getMap() == null)
        {
            return;
        }

        int x = MapEditorApplication.getCurrent().getMap().mapTiles[0].length;
        int y = MapEditorApplication.getCurrent().getMap().mapTiles[1].length;

        MapUtils.calculateAllTileImages(MapEditorApplication.getCurrent().getMap(), g, this, x, y);

        for (int row = 0; row < y; row++)
        {
            for (int column = 0; column < x; column++)
            {
                if (MapEditorApplication.getCurrent().getMap().mapTiles[row][column] == null)
                {
                    g.drawImage(blackImage, (row * GameConfiguration.tileSize), (column * GameConfiguration.tileSize), this);
                    continue;
                }

                MapTile tile = MapEditorApplication.getCurrent().getMap().mapTiles[row][column];
                g.drawImage(tile.getCalculatedImage(), (row * GameConfiguration.tileSize), (column * GameConfiguration.tileSize), this);
              /*  if (tile.getFurniture() != null)
                {
                    g.drawImage(tile.getFurniture().getItemImage(), (row * GameConfiguration.tileSize), (column * GameConfiguration.tileSize), this);
                }*/
                if (tile.getLifeForm() != null)
                {
                    //g.drawImage( (row * GameConfiguration.tileSize), (column * GameConfiguration.tileSize), this);
                    g.drawImage(tile.getLifeForm().getDefaultImage(), ((GameConfiguration.tileSize * row) + (GameConfiguration.tileSize / 4)), ((GameConfiguration.tileSize * column) + (GameConfiguration.tileSize / 4)), this);
                }
                if (tile == selectedTile)
                {
                    g.setColor(Color.WHITE);
                    g.drawRect((GameConfiguration.tileSize * row), (GameConfiguration.tileSize * column), GameConfiguration.tileSize, GameConfiguration.tileSize);
                }
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
