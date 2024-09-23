package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.map.AutoMap;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.utils.ImageUtils;
import net.ck.mtbg.util.utils.MapUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;

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
    private final int spacer = 20;
    private AutoMap map;
    private boolean drag;
    private boolean inital;
    private BufferedImage playerPositionImage = ImageUtils.createImage(Color.RED, GameConfiguration.autoMapTileSize - 2);

    /**
     * Constructor for AutoMapCanvas
     * <p>
     * sets the size of the component - will need to adjust the parent perhaps?
     * or implement scrolling?
     * calculates all images for the map
     * also calculates the scaled images
     * also adds the labels already as children
     * paintComponent is cleared now
     *
     * @param map the map to draw or rather the part of the map to draw
     */
    public AutoMapCanvas(AutoMap map)
    {
        long start = System.nanoTime();
        this.map = map;
        MapUtils.calculateAllTileImages(map, this.getGraphics());
        setSize(spacer + (map.getSize().x * GameConfiguration.autoMapTileSize), spacer + (map.getSize().y * GameConfiguration.autoMapTileSize));
        inital = true;
        for (int row = 0; row < map.getSize().y; row++)
        {
            for (int column = 0; column < map.getSize().x; column++)
            {
                MapTile tile = map.mapTiles[column][row];
                //first if the tile has already been discovered, i.e. seen
                //for showing off purposes inverted
                //TODO switch this
                if (tile.isDiscovered())
                {
                    //if there is no scaled image yet
                    if (tile.getScaledImage() == null)
                    {
                        //create the scaled image and store it on the tile
                        tile.setScaledImage(ImageUtils.scaledImage(tile.getCalculatedImage(), GameConfiguration.autoMapTileSize, GameConfiguration.autoMapTileSize));
                    }
                }

            }
        }

        if (map != null)
        {
            if (map.getLabels() != null)
            {
                if (isInital())
                {
                    Iterator<Map.Entry<Rectangle, String>> it = map.getLabels().entrySet().iterator();
                    while (it.hasNext())
                    {
                        Map.Entry<Rectangle, String> entry = it.next();

                        MapLabel label = new MapLabel(entry.getValue(), this);
                        label.setBounds(entry.getKey());
                        this.add((label));

                    }
                    setInital(false);
                }
            }
        }


        logger.debug("AutoCanvas constructor: {}", System.nanoTime() - start);
    }

    /**
     * paintComponent iterates over the whole map and paints all the tiles on the screen.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g)
    {
        long start = System.nanoTime();
        for (int row = 0; row < map.getSize().y; row++)
        {
            for (int column = 0; column < map.getSize().x; column++)
            {
                MapTile tile = map.mapTiles[column][row];
                //draw the scaled image
                g.drawImage(tile.getScaledImage(), spacer + (column * GameConfiguration.autoMapTileSize), spacer + (row * GameConfiguration.autoMapTileSize), null);
                if ((column == (Game.getCurrent().getCurrentPlayer().getMapPosition().x)) && (row == (Game.getCurrent().getCurrentPlayer().getMapPosition().y)))
                {
                    g.drawImage(playerPositionImage, spacer + (column * GameConfiguration.autoMapTileSize), spacer + (row * GameConfiguration.autoMapTileSize), null);
                }
            }
        }

        //paint JLabels manually just to check whether this works,
        //properly handle it in child components which are added in the constructor.
        //if I really want to draw these manually set the flag to true
        if (GameConfiguration.drawLabelsOnAutomap)
        {
            if (map != null)
            {
                if (map.getLabels() != null)
                {
                    if (isInital())
                    {
                        Iterator<Map.Entry<Rectangle, String>> it = map.getLabels().entrySet().iterator();
                        while (it.hasNext())
                        {
                            Map.Entry<Rectangle, String> entry = it.next();
                            g.setColor(Color.WHITE);
                            g.setFont(GameConfiguration.font);
                            g.drawString(entry.getValue(), entry.getKey().x, entry.getKey().y);
                        }
                    }
                    setInital(false);
                }
            }
        }
        logger.debug("end painting: {}", System.nanoTime() - start);
    }


    public void switchTextFieldToLabel(MapLabelTextField textfield)
    {
        if (GameConfiguration.debugAutoMap == true)
        {
            logger.debug("entering switch text field for label");
        }
        map.getLabels().put(textfield.getBounds(), textfield.getText());
        MapLabel label = new MapLabel(textfield.getText(), this);
        label.setBounds(textfield.getBounds());
        label.setVisible(true);
        this.remove(textfield);
        this.add(label);
        this.requestFocus();
        this.revalidate();
        this.repaint();
        if (GameConfiguration.debugAutoMap == true)
        {
            logger.debug("finish switch text field for label");
        }
    }


    public void switchLabelToTextField(MapLabel label)
    {
        if (GameConfiguration.debugAutoMap == true)
        {
            logger.debug("entering switch label for text field");
        }
        map.getLabels().remove(label.getBounds());
        MapLabelTextField textfield = new MapLabelTextField(this);
        textfield.setBounds(label.getBounds());
        textfield.setText(label.getText());
        textfield.setVisible(true);
        this.remove(label);
        this.add(textfield);
        label.setVisible(false);
        textfield.requestFocus();
        this.revalidate();
        this.repaint();
        if (GameConfiguration.debugAutoMap == true)
        {
            logger.debug("finish switch label to text field");
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
