package net.ck.mtbg.ui.listeners.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.components.AutoMapCanvas;
import net.ck.mtbg.ui.components.MapLabel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@Log4j2
@Getter
@Setter
@ToString
public class MapLabelListener implements MouseListener, MouseMotionListener
{

    MapLabel label;
    AutoMapCanvas autoMapCanvas;

    public MapLabelListener(AutoMapCanvas autoMapCanvas, MapLabel label)
    {
        this.autoMapCanvas = autoMapCanvas;
        this.label = label;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            logger.debug("dbl click");
            autoMapCanvas.requestFocus();
            autoMapCanvas.switchLabelToTextField(label);
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (autoMapCanvas.isDrag())
        {
            if (GameConfiguration.debugAutoMap == true)
            {
                logger.debug("label: {}, position: {}, location:{}", label, label.getBounds(), e.getLocationOnScreen());
            }
            Rectangle oldRect = label.getBounds();
            autoMapCanvas.getMap().getLabels().remove(oldRect);
            autoMapCanvas.revalidate();
            autoMapCanvas.repaint();
            label.setLocation(MouseInfo.getPointerInfo().getLocation().x - autoMapCanvas.getLocationOnScreen().x, MouseInfo.getPointerInfo().getLocation().y - autoMapCanvas.getLocationOnScreen().y);
            autoMapCanvas.getMap().getLabels().put(label.getBounds(), label.getText());
            autoMapCanvas.revalidate();
            autoMapCanvas.repaint();
            autoMapCanvas.setDrag(false);
        }

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (GameConfiguration.debugAutoMap == true)
        {
            logger.debug("drag");
        }
        autoMapCanvas.setDrag(true);
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
}
