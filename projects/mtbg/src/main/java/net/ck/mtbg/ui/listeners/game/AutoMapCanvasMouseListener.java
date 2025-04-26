package net.ck.mtbg.ui.listeners.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.AutoMapCanvas;
import net.ck.mtbg.ui.components.MapLabelTextField;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@Log4j2
@Getter
@Setter
public class AutoMapCanvasMouseListener implements MouseListener, MouseMotionListener, MouseInputListener
{
    AutoMapCanvas autoMapCanvas;

    public AutoMapCanvasMouseListener(AutoMapCanvas autoMapCanvas)
    {
        setAutoMapCanvas(autoMapCanvas);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            MapLabelTextField textfield = new MapLabelTextField(autoMapCanvas);
            autoMapCanvas.add(textfield);
            textfield.setBounds(MouseInfo.getPointerInfo().getLocation().x - autoMapCanvas.getLocationOnScreen().x, MouseInfo.getPointerInfo().getLocation().y - autoMapCanvas.getLocationOnScreen().y, 50, 20);
            textfield.setFocusable(true);
            textfield.requestFocus();
            textfield.setVisible(true);
            autoMapCanvas.validate();
        }
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

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
}
