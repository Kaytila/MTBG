package net.ck.mtbg.ui.components.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class EffectsOverlay extends JPanel
{
    private final int alpha = 100;
    Color col;
    private boolean paint = false;

    public EffectsOverlay(AbstractMapCanvas canvas, Color color)
    {
        this.setLocation(canvas.getLocation());
        this.setSize(canvas.getSize());
        canvas.getParent().add(this);
        col = new Color(color.getRed(), color.getRed(), color.getBlue(), alpha);
        this.setBackground(col);
        this.setVisible(true);
    }

    public EffectsOverlay(MapCanvas gridCanvas)
    {
        this.setLocation(gridCanvas.getLocation());
        this.setSize(gridCanvas.getSize());
        this.setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (paint)
        {
            g.setColor(col);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        else
        {
            super.paintComponent(g);
        }
    }

}
