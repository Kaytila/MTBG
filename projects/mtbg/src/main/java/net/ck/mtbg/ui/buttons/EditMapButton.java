package net.ck.mtbg.ui.buttons;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;

@Log4j2
@Getter
@Setter
public class EditMapButton extends JButton implements MouseListener
{
    private boolean hovered;
    private String label = "Edit Map";


    public EditMapButton(ActionListener actionListener)
    {
        setIcon(ImageUtils.createImageIcon(GameConfiguration.miscImages + "BUTTONS" + File.separator + "cleanButton.png", ""));
        this.setFont(GameConfiguration.font);
        //setText(label);
        this.setActionCommand(label);
        this.addActionListener(actionListener);
        hovered = false;
        this.addMouseListener(this);
        this.setPreferredSize(GameConfiguration.preferredButtonSize);
        this.setMinimumSize(GameConfiguration.preferredButtonSize);
        this.setMaximumSize(GameConfiguration.preferredButtonSize);
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
        Rectangle2D r = fm.getStringBounds(getLabel(), g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString(getLabel(), x, y);
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
