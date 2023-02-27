package net.ck.game.ui.buttons;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
import net.ck.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class SaveButton extends JButton implements MouseListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private boolean hovered;

    private String label = "Save";


    public SaveButton(Point p)
    {
        setIcon(ImageUtils.createImageIcon(GameConfiguration.miscImages + "BUTTONS" + File.separator + "cleanButton.png", ""));
        this.setFont(GameConfiguration.font);
        setText(label);
        setBounds(p.x, p.y, 70, 30);
        this.setActionCommand(label);
        this.addActionListener(WindowBuilder.getController());
        hovered = false;
        this.addMouseListener(this);
        this.setVisible(true);
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (hovered)
        {
            g.setColor(Color.white);
        }
        else
        {
            g.setColor(Color.black);
        }

        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(label, g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString(label, x, y);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        hovered = true;
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        hovered = false;
    }
}
