package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.utils.ImageUtils;
import net.ck.mtbg.util.utils.MapUtils;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
@ToString
/**
 * canvas to draw the full map on -
 * question is how small this will need to be in order to make sense
 *
 */
public class AutoMapCanvas extends JComponent
{
    private Map map;

    public AutoMapCanvas(Map map)
    {
        this.map = map;
        MapUtils.calculateAllTileImages(map, this.getGraphics());
        setSize(20 + map.getSize().x * GameConfiguration.autoMapTileSize, 20 + map.getSize().y * GameConfiguration.autoMapTileSize);
    }

    public void paintComponent(Graphics g)
    {
        for (int row = 0; row < map.getSize().y; row++)
        {
            for (int column = 0; column < map.getSize().x; column++)
            {
                MapTile tile = map.mapTiles[column][row];
                ImageUtils.scaledImage(tile.getCalculatedImage(), GameConfiguration.autoMapTileSize, GameConfiguration.autoMapTileSize);
                g.drawImage(tile.getCalculatedImage(), column * GameConfiguration.autoMapTileSize, row * GameConfiguration.autoMapTileSize, null);
            }
        }
    }

    public void paint()
    {
        javax.swing.SwingUtilities.invokeLater(() ->
        {
            this.repaint();
        });
    }
}
