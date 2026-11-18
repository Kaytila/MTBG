package net.ck.mtbg.ui.components.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class FantasyPanel extends JPanel
{

    private static final Color GOLD = new Color(180, 145, 75);
    private static final Color TEXT = new Color(235, 222, 190);

    @Override
    protected void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics.create();
        try
        {
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            g.setPaint(new GradientPaint(
                    0, 0, new Color(55, 51, 46),
                    0, height, new Color(24, 23, 22)));
            g.fillRect(0, 0, width, height);

            g.setColor(new Color(15, 13, 10));
            g.setStroke(new BasicStroke(6));
            g.drawRect(3, 3, width - 7, height - 7);

            g.setColor(GOLD);
            g.setStroke(new BasicStroke(2));
            g.drawRect(7, 7, width - 15, height - 15);

            g.setColor(new Color(105, 83, 45));
            g.setStroke(new BasicStroke(1));
            g.drawRect(11, 11, width - 23, height - 23);

            drawRivet(g, 7, 7);
            drawRivet(g, width - 15, 7);
            drawRivet(g, 7, height - 15);
            drawRivet(g, width - 15, height - 15);
        }
        finally
        {
            g.dispose();
        }
    }

    private void drawRivet(Graphics2D g, int x, int y)
    {
        g.setColor(GOLD);
        g.fillOval(x, y, 8, 8);
        g.setColor(new Color(245, 215, 145));
        g.fillOval(x + 2, y + 1, 3, 3);
    }
}
