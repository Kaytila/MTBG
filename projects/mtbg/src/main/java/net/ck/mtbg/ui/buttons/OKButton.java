package net.ck.mtbg.ui.buttons;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.actions.OKButtonAction;
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
public class OKButton extends JButton implements MouseListener
{
    private boolean hovered;
    private String label = "OK";

    public OKButton()
    {
        setIcon(ImageUtils.createImageIcon(GameConfiguration.miscImages + "BUTTONS" + File.separator + "cleanButton.png", ""));
        this.setFont(getFont());
        //setText(getLabel());
        //this.setToolTipText(getLogger().getName());
        this.setActionCommand("OK");
        //this.addActionListener(WindowBuilder.getController());
        this.setVisible(true);
        hovered = false;
        this.addMouseListener(this);
        this.setAction(new OKButtonAction());
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
