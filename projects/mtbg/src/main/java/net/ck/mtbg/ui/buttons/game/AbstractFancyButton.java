package net.ck.mtbg.ui.buttons.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;

@Log4j2
@Getter
@Setter
public abstract class AbstractFancyButton extends JButton implements MouseListener
{
    protected boolean hovered;
    protected String label;

    public AbstractFancyButton()
    {
        setIcon(ImageUtils.createImageIcon(GameConfiguration.miscImages + "BUTTONS" + File.separator + "cleanButton.png", ""));
        setFont(GameConfiguration.font);
        this.addMouseListener(this);
        this.setPreferredSize(GameConfiguration.preferredButtonSize);
        this.setMinimumSize(GameConfiguration.preferredButtonSize);
        this.setMaximumSize(GameConfiguration.preferredButtonSize);
        hovered = false;
    }

    public AbstractFancyButton(Point p)
    {
        setIcon(ImageUtils.createImageIcon(GameConfiguration.miscImages + "BUTTONS" + File.separator + "cleanButton.png", ""));
        setBounds(p.x, p.y, GameConfiguration.preferredButtonSize.width, GameConfiguration.preferredButtonSize.height);
        setFont(GameConfiguration.font);
        this.addMouseListener(this);
        this.setPreferredSize(GameConfiguration.preferredButtonSize);
        this.setMinimumSize(GameConfiguration.preferredButtonSize);
        this.setMaximumSize(GameConfiguration.preferredButtonSize);
        hovered = false;
    }


    /**
     * with a little help from stackoverflow again
     * <p>
     * https://stackoverflow.com/questions/14284754/java-center-text-in-rectangle/14287270#14287270
     */
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
