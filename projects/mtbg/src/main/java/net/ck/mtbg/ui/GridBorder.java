package net.ck.mtbg.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Line2D;

@Log4j2
@Getter
@Setter
public class GridBorder implements Border
{

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        //Left Border
        g2d.draw(new Line2D.Double((double) x + 10, (double) y + 10, (double) x + 10, (double) y + 20));
        g2d.draw(new Line2D.Double((double) x + 10, (double) y + 10, (double) x + 20, (double) y + 10));
        // Right Border
        g2d.draw(new Line2D.Double((double) width - 10, (double) y + 10, (double) width - 10, (double) y + 20));
        g2d.draw(new Line2D.Double((double) width - 10, (double) y + 10, (double) width - 20, (double) y + 10));
        // Lower Left Border
        g2d.draw(new Line2D.Double((double) x + 10, (double) height - 10, (double) x + 20, (double) height - 10));
        g2d.draw(new Line2D.Double((double) x + 10, (double) height - 10, (double) x + 10, (double) height - 20));
        // Lower Right Border
        g2d.draw(new Line2D.Double((double) width - 10, (double) height - 10, (double) width - 20, (double) height - 10));
        g2d.draw(new Line2D.Double((double) width - 10, (double) height - 10, (double) width - 10, (double) height - 20));
    }

    @Override
    public Insets getBorderInsets(Component c)
    {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque()
    {
        return false;
    }
}
