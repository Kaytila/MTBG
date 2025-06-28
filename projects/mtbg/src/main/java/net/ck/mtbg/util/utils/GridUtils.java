package net.ck.mtbg.util.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.game.AbstractMapCanvas;
import net.ck.mtbg.util.ui.WindowBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@Setter
@Log4j2
public class GridUtils
{
    public static void paintLines(JComponent characterTinyCanvas, Graphics g, int characterEditorTinyTileSize)
    {
        int rows = characterTinyCanvas.getHeight() / characterEditorTinyTileSize;
        int cols = characterTinyCanvas.getWidth() / characterEditorTinyTileSize;
        int i;
        g.setColor(Color.GRAY);
        for (i = 0; i < rows; i++)
        {
            g.drawLine(0, i * characterEditorTinyTileSize, characterTinyCanvas.getWidth(), i * characterEditorTinyTileSize);
        }

        for (i = 0; i < cols; i++)
        {
            g.drawLine(i * characterEditorTinyTileSize, 0, i * characterEditorTinyTileSize, characterTinyCanvas.getHeight());
        }
    }

    /**
     * method flow with inner class is very weird, but it works.
     * a
     *
     * @param canvas - the map canvas to render
     * @param amount - the amount (+1) of shakes, needs to be odd.
     */
    public static void doEarthquake(AbstractMapCanvas canvas, int amount)
    {
        if (amount % 2 == 0)
        {
            logger.error("ERROR: needs to be odd as its zero indexed for starting to the right");
            return;
        }

        logger.info("running earthquake!");
        final int[] count = {0};
        final Timer timer = new Timer(250, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (count[0] <= amount)
                {
                    if (count[0] % 2 == 0)
                    {
                        logger.info("shake to the right");
                        canvas.setLocation(canvas.getX() + 5, canvas.getY());
                    }
                    else
                    {
                        logger.info("shake to the left");
                        canvas.setLocation(canvas.getX() - 5, canvas.getY());
                    }
                    count[0]++;
                }
                else
                {
                    logger.info("stop shaking");
                    //timer.stop();
                }
            }
        });

        timer.setInitialDelay(0);
        timer.setRepeats(true);
        timer.start();
        /**
         * code jumps up into the action listener now of the inner class
         * 0 -> right
         * 1 - left
         * 2 - right
         * 3 - left
         * now it is done continue here
         */
        if (count[0] == amount)
        {
            timer.setRepeats(false);
            timer.stop();
            logger.debug("still running: {}", timer.isRunning());
        }
    }

    /**
     * method flow with inner class is very weird, but it works.
     *
     * @param canvas
     */
    public static void flashColor(AbstractMapCanvas canvas, Color color, int amount)
    {
        logger.info("blinding light!");
        final int[] count = {1};
        ActionListener listener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Graphics2D g = (Graphics2D) canvas.getGraphics();
                if (count[0] <= amount)
                {
                    g.setColor(color);
                    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    count[0]++;
                }
            }
        };
        Timer timer = new Timer(250, listener);
        timer.setInitialDelay(0);
        timer.setRepeats(true);
        timer.start();
        /**
         * code jumps up into the action listener now of the inner class
         * now it is done continue here
         */
        if (count[0] == amount)
        {
            timer.setRepeats(false);
            timer.stop();
        }
    }


    public static void overlayColor(Color color, int timeInMilliseconds)
    {
        logger.debug("setting overlay to true, flash color");
        Color col = new Color(color.getRed(), color.getRed(), color.getBlue(), 20);
        WindowBuilder.getEffectsOverlay().setBackground(col);
        WindowBuilder.getEffectsOverlay().setVisible(true);
        ActionListener listener = e ->
        {
            logger.debug("fire action to hide");
            WindowBuilder.getEffectsOverlay().setVisible(false);
        };
        Timer timer = new Timer(timeInMilliseconds, listener);
        timer.setRepeats(false);
        timer.start();
    }
}
