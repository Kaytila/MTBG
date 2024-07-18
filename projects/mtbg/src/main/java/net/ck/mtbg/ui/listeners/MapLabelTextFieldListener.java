package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.components.AutoMapCanvas;
import net.ck.mtbg.ui.components.MapLabelTextField;

import java.awt.event.*;

@Log4j2
@Getter
@Setter
public class MapLabelTextFieldListener implements FocusListener, KeyListener, MouseListener, MouseMotionListener
{
    MapLabelTextField mapLabelTextField;
    AutoMapCanvas autoMapCanvas;


    public MapLabelTextFieldListener(MapLabelTextField mapLabelTextField, AutoMapCanvas autoMapCanvas)
    {
        this.autoMapCanvas = autoMapCanvas;
        this.mapLabelTextField = mapLabelTextField;
    }

    @Override
    public void focusGained(FocusEvent e)
    {

    }

    @Override
    public void focusLost(FocusEvent e)
    {

    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (GameConfiguration.debugAutoMap == true)
            {
                logger.debug("enter pressed, should loose focus now");
            }
            autoMapCanvas.requestFocus();
            autoMapCanvas.switchTextFieldToLabel(mapLabelTextField);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            mapLabelTextField.requestFocus();
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
